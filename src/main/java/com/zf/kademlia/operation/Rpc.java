package com.zf.kademlia.operation;

import java.nio.ByteBuffer;

import com.zf.kademlia.common.Commons;

public class Rpc {
	private static final int count = 1 + 2 + Commons.ID_LENGTH;

	private static short index = Short.MIN_VALUE;
	private byte type = -1;
	private byte[] id = null;

	public Rpc(byte type, byte[] id) {
		this.type = type;
		index++;
	}

	public byte[] getMessage() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(count);
		return byteBuffer.put(type).putShort(index).put(id).array();
	}
}
