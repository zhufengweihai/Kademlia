package com.zf.kademlia.protocol;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.util.Base64;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

/**
 * @author zhufeng7
 * @date 2017-11-29.
 */
public class Codec {
	public KadMessage decode(IoBuffer buffer) throws IOException {
		String message = buffer.getString(Charset.forName("UTF-8").newDecoder());
		String[] parts = message.split("\\|");
		Node origin = decodeNode(parts[1]);

		if (parts[0].equals(MessageType.FIND_NODE.name())) {
			return new FindNode(origin, Key.build(parts[2]));
		} else if (parts[0].equals(MessageType.PING.name())) {
			return new Ping(origin);
		} else if (parts[0].equals(MessageType.PONG.name())) {
			return new Pong(origin);
		} else if (parts[0].equals(MessageType.NODE_REPLY.name())) {
			List<Node> nodes = new ArrayList<>();
			for (int i = 2; i < parts.length; i++) {
				nodes.add(decodeNode(parts[i]));
			}
			return new NodeReply(origin, nodes);
		} else if (parts[0].equals(MessageType.STORE.name())) {
			String value = new String(Base64.decodeBase64(parts[3].getBytes()), Charset.forName("UTF-8"));
			return new Store(origin, Key.build(parts[2]), value);
		} else if (parts[0].equals(MessageType.STORE_REPLY.name())) {
			return new StoreReply(origin);
		} else if (parts[0].equals(MessageType.FIND_VALUE.name())) {
			return new FindValue(origin, Key.build(parts[2]));
		} else if (parts[0].equals(MessageType.VALUE_REPLY.name())) {
			return new ValueReply(origin, Key.build(parts[2]), parts[3]);
		} else {
			System.out.println("Can't decode message_type=" + parts[0]);
			throw new RuntimeException("Unknown message type=" + parts[0] + " message=" + Arrays.toString(parts));
		}
	}

	private Node decodeNode(String nodeEncoded) {
		String[] nodeParts = nodeEncoded.split(",");
		Key key = Key.build(nodeParts[0]);
		int port = Integer.parseInt(nodeParts[2]);
		long lastSeen = Long.parseLong(nodeParts[3]);
		return new Node(key, nodeParts[1], port, lastSeen);
	}

	private IoBuffer encode(Ping msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.flip();
		return buffer;
	}

	public IoBuffer encode(Pong msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.flip();
		return buffer;
	}

	private IoBuffer encode(FindNode msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.putChar('|');
		buffer.put(msg.getFindKey().toString().getBytes());
		buffer.flip();
		return buffer;
	}

	public IoBuffer encode(NodeReply msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		for (Node node : msg.getNodes()) {
			buffer.putChar('|');
			buffer.put(node.toString().getBytes());
		}
		buffer.flip();
		return buffer;
	}

	private IoBuffer encode(Store msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.putChar('|');
		buffer.put(msg.getKey().toString().getBytes());
		buffer.putChar('|');
		buffer.put(Base64.encodeBase64(msg.getValue().getBytes()));
		buffer.flip();
		return buffer;
	}

	public IoBuffer encode(StoreReply msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.flip();
		return buffer;
	}

	private IoBuffer encode(FindValue msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.putChar('|');
		buffer.put(msg.getKey().toString().getBytes());
		buffer.flip();
		return buffer;
	}

	public IoBuffer encode(ValueReply msg) {
		IoBuffer buffer = IoBuffer.allocate(10);
		encodeHeader(buffer, msg);
		buffer.putChar('|');
		buffer.put(msg.getKey().toString().getBytes());
		buffer.putChar('|');
		buffer.put(msg.getValue().toString().getBytes());
		buffer.flip();
		return buffer;
	}

	private void encodeHeader(IoBuffer buffer, KadMessage msg) {
		buffer.put(msg.getType().name().getBytes());
		buffer.putChar('|');
		buffer.put(msg.getOrigin().toString().getBytes());
	}

	public IoBuffer encode(KadMessage msg) {
		if (msg instanceof ValueReply) {
			return encode((ValueReply) msg);
		} else if (msg instanceof FindNode) {
			return encode((FindNode) msg);
		} else if (msg instanceof NodeReply) {
			return encode((NodeReply) msg);
		} else if (msg instanceof FindValue) {
			return encode((FindValue) msg);
		} else if (msg instanceof Store) {
			return encode((Store) msg);
		} else if (msg instanceof StoreReply) {
			return encode((StoreReply) msg);
		} else if (msg instanceof Ping) {
			return encode((Ping) msg);
		} else if (msg instanceof Pong) {
			return encode((Pong) msg);
		}
		return null;
	}
}
