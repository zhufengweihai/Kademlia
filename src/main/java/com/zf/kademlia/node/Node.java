package com.zf.kademlia.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Node implements Comparable<Node> {
	private Key id = null;
	private String ip = null;
	private int port = -1;
	private long lastSeen = System.currentTimeMillis();

	@Override
	public int compareTo(Node o) {
		if (this.equals(o)) {
			return 0;
		}
		return (this.lastSeen > o.lastSeen) ? 1 : -1;
	}

	@Override
	public String toString() {
		return id.toString() + ',' + ip + ',' + port + ',' + lastSeen;
	}
}
