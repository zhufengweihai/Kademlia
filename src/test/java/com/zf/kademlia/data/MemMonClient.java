/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.zf.kademlia.data;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class MemMonClient extends IoHandlerAdapter {

	private IoConnector connector;

	/**
	 * Default constructor.
	 */
	public MemMonClient() {
		connector = new NioDatagramConnector();
		connector.setHandler(this);
		Runtime.getRuntime().addShutdownHook(new Thread(connector::dispose));

		ConnectFuture connFuture = connector.connect(new InetSocketAddress("localhost", UdpServer.PORT));
		// connFuture.awaitUninterruptibly(5000);
		// System.exit(0);
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

		// ConnectFuture connFuture1 = connector.connect(new
		// InetSocketAddress("localhost", UdpServer.PORT));
		//
		// connFuture1.addListener(new IoFutureListener<ConnectFuture>() {
		// public void operationComplete(ConnectFuture future) {
		// if (future.isConnected()) {
		// IoSession session = future.getSession();
		// IoBuffer buffer = IoBuffer.allocate(5);
		// buffer.put("test2".getBytes());
		// buffer.flip();
		// session.write(buffer);
		// }
		// }
		// });
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.exit(0);

	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			session.closeNow();
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		// session.closeNow();
		// connector.dispose();
	}

	public static void main(String[] args) {
		new MemMonClient();
		for (;;)
			;
	}
}
