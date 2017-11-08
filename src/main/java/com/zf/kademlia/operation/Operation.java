package com.zf.kademlia.operation;

import java.nio.ByteBuffer;

import com.zf.kademlia.node.Node;

public abstract class Operation {
	private byte flag = -1;
	private byte[] index = null;
	private Node node = null;

	public String getMessage() {
		byte[] value = getValue();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1+index.hashCode());
		return null;
	}

	abstract public byte[] getValue();
}
