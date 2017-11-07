package com.zf.kademlia.node;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class NodeId {
	public final static int ID_LENGTH = 160;
	private byte[] keyBytes = new byte[ID_LENGTH / 8];

	public NodeId() throws NoSuchAlgorithmException {
		SecureRandom.getInstance("SHA1PRNG").nextBytes(keyBytes);
	}

	public byte[] getBytes() {
		return this.keyBytes;
	}

	public BigInteger getInt() {
		return new BigInteger(1, this.getBytes());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(keyBytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeId other = (NodeId) obj;
		if (!Arrays.equals(keyBytes, other.keyBytes))
			return false;
		return true;
	}

	public byte[] distance(NodeId other) {
		byte[] distance = new byte[ID_LENGTH / 8];
		for (int i = 0; i < keyBytes.length; i++) {
			distance[i] = (byte) (keyBytes[i] ^ other.keyBytes[i]);
		}
		return distance;
	}

	public String hexRepresentation() {
		BigInteger bi = new BigInteger(1, this.keyBytes);
		return String.format("%0" + (this.keyBytes.length << 1) + "X", bi);
	}

	@Override
	public String toString() {
		return this.hexRepresentation();
	}

}
