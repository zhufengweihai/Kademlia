package com.zf.kademlia.net;

import java.awt.Event;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.storage.LocalStorage;

import lombok.Data;

/**
 * Created by Christoph on 21.09.2016.
 */
@Data
public class KademliaServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(KademliaServer.class);

	private IoConnector connector = new NioDatagramConnector();
	private final LocalStorage localStorage;
	private final Map<String, Consumer<Event>> eventConsumers;

	public KademliaServer(String bindingAddress, int port, LocalStorage localStorage,
			Map<String, Consumer<Event>> eventConsumers) {
		this.localStorage = localStorage;
		this.eventConsumers = eventConsumers;

		this.group = new NioEventLoopGroup();
		new Thread(() -> {
			try {
				Bootstrap b = new Bootstrap();

				b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, false)
						.handler(new KademliaServerHandler(this.routingTable, this.localStorage, this.localNode,
								this.kValue, this.eventConsumers));

				b.bind(bindingAddress, port).sync().channel().closeFuture().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				group.shutdownGracefully();
			}
		}).start();

		LOGGER.info("Kademlia Listener started");
	}

	public void close() {
		try {
			group.shutdownGracefully().await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
