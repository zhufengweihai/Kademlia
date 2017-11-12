package com.zf.kademlia.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.routing.Contact;

public class KadClient extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(KadClient.class);

	private IoConnector connector;
	public KadClient(Contact node) {
		connector = new NioDatagramConnector();
		connector.setHandler(this);
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(node.getIp(), node.getPort()));
		connFuture.awaitUninterruptibly();
		IoSession session = connFuture.getSession();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		session.closeNow();
		logger.error("sockect error", cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
	}

}
