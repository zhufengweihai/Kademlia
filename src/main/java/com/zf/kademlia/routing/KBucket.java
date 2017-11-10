package com.zf.kademlia.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.node.Node;

public class KBucket implements Serializable {
	private static final long serialVersionUID = -6246209996380143057L;
	private List<Node> contacts = new ArrayList<Node>(Commons.K);
	private long lastRefresh = 0;

	public void addNode(Node node) {
		contacts.add(node);
	}

	/**
	 * 更新K桶 如果新的节点已经存在于这个K桶中，则把对应项移到该该K桶的尾部
	 * 
	 * @param node
	 */
	public void update(Node node) {
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			if (contacts.remove(node) || contacts.size() < Commons.K) {
				contacts.add(node);
			} else {

			}
		} finally {
			lock.unlock();
		}
	}

	public boolean isOld() {
		return System.currentTimeMillis() - lastRefresh >= Commons.TIME_INTERVAL * 60 * 1000;
	}

	@Override
	public String toString() {
		return "KBucket [contacts=" + contacts + "]";
	}

	public static int getBucketIndex(byte[] distances) {
		for (int i = distances.length - 1; i >= 0; i--) {
			int index = getFirstBitIndex(distances[i]);
			if (index > 0) {
				return i * 8 + index;
			}
		}
		return 0;
	}

	private static int getFirstBitIndex(int b) {
		int j = 7;
		int n = 256;
		for (; j >= 0; j--) {
			if ((b & (n >>= 1)) != 0) {
				break;
			}
		}
		return j;
	}
}
