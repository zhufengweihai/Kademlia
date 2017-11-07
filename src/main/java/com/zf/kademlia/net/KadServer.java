
package com.zf.kademlia.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.common.Commons;

public class KadServer extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(KadServer.class);
	private static KadServer instance = new KadServer();
	private String ip = null;

	private KadServer() {
	}

	public static KadServer instance() {
		return instance;
	}

	public boolean init() {
		try {
			NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
			acceptor.setHandler(this);
			acceptor.bind(new InetSocketAddress(Commons.PORT));
			ip = acceptor.getLocalAddress().getAddress().getHostAddress();
			return true;
		} catch (IOException e) {
			logger.error("Failed to init kadserver", e);
		}
		return false;
	}

	public String getIP() {
		return ip;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		session.closeNow();
		logger.error("sockect error", cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		session.write(message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

}
