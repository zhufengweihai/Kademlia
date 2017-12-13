package com.zf.kademlia.client;

import com.zf.common.CommonManager;
import com.zf.kademlia.Commons;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.BaseOperation;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.util.GraceUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import static com.zf.kademlia.Commons.RETRIES_COUNT;
import static com.zf.kademlia.Commons.RETRIES_INTERVAL;

/**
 * @author zhufeng
 * @date 2017-11-29.
 */
public class KademliaClient extends SimpleChannelInboundHandler<DatagramPacket> {
    private static Logger logger = LoggerFactory.getLogger(KademliaClient.class);
    private static KademliaClient instance = new KademliaClient();

    private Map<Long, BaseOperation> operationMap = new HashMap<>();
    private Bootstrap bootstrap = null;
    private NioEventLoopGroup group = null;
    private Codec codec = new Codec();

    private KademliaClient() {
        init();
    }

    private void init() {
        group = new NioEventLoopGroup();
        Runtime.getRuntime().addShutdownHook(new Thread(group::shutdownGracefully));
        this.bootstrap = new Bootstrap();
        Class<NioDatagramChannel> aClass = NioDatagramChannel.class;
        bootstrap.group(group).channel(aClass).option(ChannelOption.SO_BROADCAST, false).handler(this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        KadMessage message = codec.decode(msg.content());
        operationMap.get(message.getSeqId()).onResponse(message);
        operationMap.remove(message.getSeqId());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("network error", cause);
        ctx.close();
    }

    public static KademliaClient instance() {
        return instance;
    }


    public void send(Node node, KadMessage msg, BaseOperation operation) {
        operationMap.put(msg.getSeqId(), operation);
        CommonManager.instance().getExecutorService().execute(createSendTask(node, msg, operation));
    }

    private Runnable createSendTask(Node node, KadMessage msg, BaseOperation operation) {
        return () -> {
            try {
                for (int i = 0; i < RETRIES_COUNT; i++) {
                    if (send(node, msg)) {
                        return;
                    }
                    if (i < RETRIES_COUNT - 1) {
                        GraceUtils.sleep(RETRIES_INTERVAL);
                    }
                }
                operation.onFailed(node);
            } catch (UnsupportedEncodingException e) {
                logger.error("failed to decode the response message", e);
            }
        };
    }

    private boolean send(Node node, KadMessage msg) throws UnsupportedEncodingException {
        try {
            Channel channel = bootstrap.bind(0).sync().channel();
            InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
            DatagramPacket packet = new DatagramPacket(codec.encode(msg), address);
            channel.writeAndFlush(packet).sync();
            return channel.closeFuture().await(Commons.NETWORK_TIMEOUT);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void close() {
        try {
            group.shutdownGracefully().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
