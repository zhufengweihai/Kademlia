package com.zf.kademlia.net;

import java.awt.Event;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
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
	private NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
	private final LocalStorage localStorage;
	private final Map<String, Consumer<Event>> eventConsumers;

	public KademliaServer(String bindingAddress, int port, LocalStorage localStorage,
			Map<String, Consumer<Event>> eventConsumers) {
		this.localStorage = localStorage;
		this.eventConsumers = eventConsumers;

		acceptor.setHandler(new MemoryMonitorHandler(this));

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);

		acceptor.bind(new InetSocketAddress(port));
	}

	public void close() {
		acceptor.dispose();
	}
}
