package com.zf.kademlia.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.BaseOperation;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.KadMessage;

/**
 * @author zhufeng
 * @date 2017-12-13.
 */
public class KademliaClient2 extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(KademliaClient2.class);

	private IoConnector connector = new NioDatagramConnector();
	private Codec codec = new Codec();
	private Map<Long, BaseOperation> operationMap = new HashMap<>();
	private Map<IoSession, Long> sessionMap = new HashMap<>();

	public KademliaClient2() {
		connector.setHandler(this);
		Runtime.getRuntime().addShutdownHook(new Thread(connector::dispose));
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			session.closeNow();
			BaseOperation operation = operationMap.get(sessionMap.getOrDefault(session, 0l));
			if( operation!=null){
				
			}
			
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		session.closeNow();
	}

	public void send(Node node, KadMessage msg, BaseOperation operation) {
		operationMap.put(msg.getSeqId(), operation);
		InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
		ConnectFuture connFuture = connector.connect(address);
		connFuture.awaitUninterruptibly();

		IoSession session = connFuture.getSession();
		sessionMap.put(session, msg.getSeqId());
		int networkTimeout = KadDataManager.instance().getConfig().getNetworkTimeout();
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, networkTimeout);
		session.write(codec.encode(msg));
	}

	public void close() {
		connector.dispose();
	}
}