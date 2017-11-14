package com.zf.kademlia.node;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.data.UdpServer;
import com.zf.kademlia.routing.Contact;

public class RpcClient extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(RpcClient.class);
	private static RpcClient client = new RpcClient();
	private IoConnector connector;

	public static RpcClient instance() {
		return client;
	}

	private RpcClient() {
		connector = new NioDatagramConnector();
		connector.setHandler(this);

		ConnectFuture connFuture1 = connector.connect(new InetSocketAddress("localhost", UdpServer.PORT));

		connFuture1.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {
					IoSession session = future.getSession();
					IoBuffer buffer = IoBuffer.allocate(5);
					buffer.put("test2".getBytes());
					buffer.flip();
					session.write(buffer);
				}
			}
		});
	}

	public void send(Contact contact) {
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(contact.getIp(), contact.getPort()));
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
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		session.closeNow();
		// connector.dispose();
	}
}
