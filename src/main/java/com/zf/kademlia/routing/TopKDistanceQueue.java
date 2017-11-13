package com.zf.kademlia.routing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class TopKDistanceQueue {
	private PriorityQueue<DistanceOrder> queue;
	private int n; // �ѵ��������

	public TopKDistanceQueue(int n) {
		this.n = n;
		this.queue = new PriorityQueue<DistanceOrder>(n);
	}

	public void add(DistanceOrder e) {
		if (queue.size() < n) { // δ�ﵽ���������ֱ�����
			queue.add(e);
		} else { // ��������
			DistanceOrder peek = queue.peek();
			if (e.compareTo(peek) < 0) { // ����Ԫ���뵱ǰ�Ѷ�Ԫ�رȽϣ�������С��Ԫ��
				queue.poll();
				queue.add(e);
			}
		}
	}

	public List<Contact> getResults() {
		List<Contact> contacts = new ArrayList<>(queue.size());
		Iterator<DistanceOrder> iterator = queue.iterator();
		while (iterator.hasNext()) {
			contacts.add(iterator.next().contact);
		}
		return contacts;
	}

	static class DistanceOrder implements Comparable<DistanceOrder> {
		private Contact contact = null;
		private BigInteger distance = null;

		public DistanceOrder(Contact target, Contact contact) {
			this.contact = contact;
			distance = new BigInteger(1, target.distance(contact));
		}

		public Contact getContact() {
			return contact;
		}

		@Override
		public int compareTo(DistanceOrder o) {
			return distance.compareTo(o.distance);
		}
	}

	public static void main(String[] args) {
		// Contact contact = Contact.createNode("127.0.0.1", 123);
		// List<Contact> contacts = new ArrayList<Contact>();
		// for (int i = 1; i <= 160 * 800; i++) {
		// Contact node = Contact.createNode("127.0.0.1", 123);
		// contacts.add(node);
		// }
		// long x = System.currentTimeMillis();
		//
		// TopKDistanceQueue<BigInteger> pq = new
		// TopKDistanceQueue<BigInteger>(8);
		// for (Contact contact2 : contacts) {
		// pq.add(new BigInteger(1, contact.distance(contact2)));
		// }
		//
		// BigInteger[] a = new BigInteger[8];
		// pq.queue.toArray(a);
		// System.out.println(System.currentTimeMillis() - x);
		// System.out.println(Arrays.toString(a));
		// while (!pq.queue.isEmpty()) {
		// System.out.print(pq.queue.poll() + ", ");
		// }
		// System.out.println();

	}
}