package com.zf.kademlia.net;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.routing.Contact;

public class BootstrapClient extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(BootstrapClient.class);
	private IoSession session;
	private IoConnector connector;

	private boolean bootstrap(Contact node) {
		connector = new NioDatagramConnector();
		connector.setHandler(this);
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(node.getIp(), node.getPort()));
		connFuture.awaitUninterruptibly(Commons.TIMEOUT,TimeUnit.MILLISECONDS);
		if (connFuture.isConnected()) {
			connFuture.getSession().write("");
		}
		return false;
	}

	private void sendData() throws InterruptedException {
		long free = Runtime.getRuntime().freeMemory();
		IoBuffer buffer = IoBuffer.allocate(8);
		buffer.putLong(free);
		buffer.flip();
		session.write(buffer);
	}
}
