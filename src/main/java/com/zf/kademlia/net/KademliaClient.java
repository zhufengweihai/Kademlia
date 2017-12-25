package com.zf.kademlia.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.BaseOperation;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.KadMessage;

/**
 * @author zhufeng
 * @date 2017-12-13.
 */
public class KademliaClient extends IoHandlerAdapter {
	private static final String ATTR_NODE = "node";
	private static final String ATTR_MESSAGE = "message";
	private static final String ATTR_RETRIES_COUNT = "retriesCount";
	private static final String ATTR_LISTENER = "listener";
	private static KademliaClient instance = new KademliaClient();

	private IoConnector connector = new NioDatagramConnector();
	private Codec codec = new Codec();

	private KademliaClient() {
		connector.setHandler(this);
		Runtime.getRuntime().addShutdownHook(new Thread(connector::dispose));
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			onFailed(session);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		onFailed(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		session.closeNow();
		KadMessage kadMessage = codec.decode((IoBuffer) message);
		((BaseOperation) session.getAttribute(ATTR_LISTENER)).onResponse(kadMessage);
	}

	private void onFailed(IoSession session) {
		session.closeNow();
		KadResponseListener listener = (KadResponseListener) session.getAttribute(ATTR_LISTENER);
		if ((int) session.getAttribute(ATTR_RETRIES_COUNT) < Kademlia.config.getRetriesCount()) {
			send((Node) session.getAttribute(ATTR_NODE), (KadMessage) session.getAttribute(ATTR_MESSAGE), listener);
		} else {
			listener.onFailed((Node) session.getAttribute(ATTR_NODE));
		}
	}

	private void send(Node node, KadMessage msg, KadResponseListener listener) {
		InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
		ConnectFuture connFuture = connector.connect(address);
		connFuture.awaitUninterruptibly();

		IoSession session = connFuture.getSession();
		session.setAttribute(ATTR_NODE, node);
		session.setAttribute(ATTR_MESSAGE, msg);
		session.setAttribute(ATTR_LISTENER, listener);
		int networkTimeout = Kademlia.config.getNetworkTimeout();
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, networkTimeout);
		session.setAttribute(ATTR_RETRIES_COUNT, 1);
		session.write(codec.encode(msg));
	}

	public static void sendMessage(Node node, KadMessage msg, KadResponseListener listener) {
		instance.send(node, msg, listener);
	}

	public void close() {
		connector.dispose();
	}
}
