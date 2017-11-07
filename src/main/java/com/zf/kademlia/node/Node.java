package com.zf.kademlia.node;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import com.zf.kademlia.common.Commons;

public class Node implements Serializable {
	private static final long serialVersionUID = -1381143309364805779L;

	private byte[] id = null;
	private String ip = null;
	private int port = -1;

	public Node() {
	}

	public Node(byte[] id, String ip, int port) {
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public byte[] distance(Node other) {
		byte[] distance = new byte[Commons.ID_LENGTH / 8];
		for (int i = 0; i < id.length; i++) {
			distance[i] = (byte) (id[i] ^ other.id[i]);
		}
		return distance;
	}

	@Override
	public String toString() {
		return "Node [id=" + Arrays.toString(id) + ", ip=" + ip + ", port=" + port + "]";
	}

	public static Node createNode(String ip, int port) {
		byte[] id = new byte[Commons.ID_LENGTH / 8];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(id);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new Node(id, ip, port);
	}
}
