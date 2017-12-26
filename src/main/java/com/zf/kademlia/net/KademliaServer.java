package com.zf.kademlia.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.FindNode;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.protocol.Pong;
import com.zf.kademlia.protocol.Store;
import com.zf.kademlia.protocol.StoreReply;
import com.zf.kademlia.protocol.ValueReply;
import com.zf.kademlia.routing.Value;
import com.zf.kademlia.routing.ValueTable;

/**
 * @author zhufeng
 * @date 2017-12-26
 */
public class KademliaServer extends IoHandlerAdapter {
	private NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
	private Codec codec = new Codec();
	private ValueTable valueTable = null;

	public KademliaServer(String bindingAddress, int port, ValueTable valueTable) throws IOException {
		this.valueTable = valueTable;
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.setHandler(this);
		acceptor.bind(new InetSocketAddress(bindingAddress, port));
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		KadMessage kadMessage = codec.decode((IoBuffer) message);
		Kademlia.routingTable.addNode(kadMessage.getOrigin());
		if (kadMessage.getType() == MessageType.PING) {
			session.write(codec.encode(new Pong(Kademlia.localNode)));
		} else if (kadMessage.getType() == MessageType.FIND_NODE) {
			FindNode findNode = (FindNode) kadMessage;
			List<Node> closest = Kademlia.routingTable.findClosest(findNode.getFindKey());
			session.write(codec.encode(new NodeReply(Kademlia.localNode, closest)));
		} else if (kadMessage.getType() == MessageType.FIND_VALUE) {
			FindValue findValue = (FindValue) kadMessage;
			Key key = findValue.getKey();
			if (valueTable.contains(key)) {
				String content = valueTable.get(key).getContent();
				session.write(codec.encode(new ValueReply(Kademlia.localNode, key, content)));
			} else {
				List<Node> closest = Kademlia.routingTable.findClosest(key);
				session.write(codec.encode(new NodeReply(Kademlia.localNode, closest)));
			}
		} else if (kadMessage.getType() == MessageType.STORE) {
			Store store = (Store) kadMessage;
			valueTable.put(store.getKey(), new Value(System.currentTimeMillis(), store.getValue()));
			session.write(codec.encode(new StoreReply(Kademlia.localNode)));
		}
	}

	public void close() {
		acceptor.dispose();
	}
}
