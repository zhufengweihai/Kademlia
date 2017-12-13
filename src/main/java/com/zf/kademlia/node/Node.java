package com.zf.kademlia.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Node implements Comparable<Node> {
    private Key id = null;
    private String ip = null;
    private int port = -1;
    private long lastSeen = System.currentTimeMillis();

    public Node(Key id, String ip, int port, long lastSeen) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.lastSeen = lastSeen;
    }

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
