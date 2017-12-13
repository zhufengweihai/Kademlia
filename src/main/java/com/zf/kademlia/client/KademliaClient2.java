package com.zf.kademlia.client;

import static com.zf.kademlia.Commons.RETRIES_COUNT;
import static com.zf.kademlia.Commons.RETRIES_INTERVAL;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.common.CommonManager;
import com.zf.kademlia.Commons;
import com.zf.kademlia.data.UdpServer;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.BaseOperation;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.util.GraceUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author zhufeng
 * @date 2017-12-13.
 */
public class KademliaClient2 extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(KademliaClient2.class);
	private IoConnector connector = new NioDatagramConnector();

	private Map<Long, BaseOperation> operationMap = new HashMap<>();
	private Codec codec = new Codec();

	public KademliaClient2() {
		init();
	}

	private void init() {
		connector.setHandler(this);
		Runtime.getRuntime().addShutdownHook(new Thread(connector::dispose));
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			session.closeNow();
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.exit(0);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		session.closeNow();
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

	private void send(Node node, KadMessage msg) throws UnsupportedEncodingException {
		InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
		ConnectFuture connFuture = connector.connect(address);
		connFuture.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {
					IoSession session = future.getSession();
					IoBuffer buffer = IoBuffer.allocate(5);
					buffer.put("test1".getBytes());
					buffer.flip();
					session.write(buffer);
				}
			}
		});
		connFuture.awaitUninterruptibly(1);
		connFuture.getSession().write(codec.encode(msg));
	}

	public void close() {
		connector.dispose();
	}
}
