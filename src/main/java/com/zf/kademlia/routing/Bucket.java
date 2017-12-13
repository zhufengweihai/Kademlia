package com.zf.kademlia.routing;

import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;

import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import lombok.Data;

import static com.zf.kademlia.Commons.K;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Data
public class Bucket {
    private final int bucketId;
    private final TreeSet<Node> nodes;
    private final TreeSet<Node> replacementNodes;

    public Bucket(int bucketId) {
        this.bucketId = bucketId;
        this.nodes = new TreeSet<>();
        this.replacementNodes = new TreeSet<>();
    }

    public void addNode(Node node) {
        if (nodes.size() < K) {
            nodes.add(node);
            return;
        }

        Node last = nodes.last();
        try {
            KademliaClient.instance().sendPing(last, message -> {
                nodes.remove(last);
                last.setLastSeen(System.currentTimeMillis());
                nodes.add(last);
                replacementNodes.add(node);
                if (replacementNodes.size() > K) {
                    replacementNodes.remove(replacementNodes.last());
                }
            });
        } catch (TimeoutException e) {
            nodes.remove(last);
            nodes.add(node);
        }
    }

    TreeSet<Node> getNodes() {
        TreeSet<Node> set = new TreeSet<>();
        set.addAll(nodes);
        return set;
    }

    public void refreshBucket() {
        TreeSet<Node> copySet = (TreeSet<Node>) nodes.clone();
        // Check nodes on reachability and update
        for (Node node : copySet) {
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    nodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                nodes.remove(node);
            }
        }

        // Fill up with reachable nodes from replacement set
        while (nodes.size() < K && !replacementNodes.isEmpty()) {
            Node node = replacementNodes.first();
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    replacementNodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                replacementNodes.remove(node);
            }
        }
    }

    void retireNode(Node nodeToRetire) {
        nodes.remove(nodeToRetire);
        // 从备用列表中选取一个在线节点填充到列表中
        while (nodes.size() < K && !replacementNodes.isEmpty()) {
            Node node = replacementNodes.first();
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    replacementNodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                replacementNodes.remove(node);
            }
        }
    }
}
