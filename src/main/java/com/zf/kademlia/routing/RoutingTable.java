package com.zf.kademlia.routing;

import com.zf.kademlia.Commons;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.ToString;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@ToString
public class RoutingTable {
    private final Bucket[] buckets;

    public RoutingTable() {
        buckets = new Bucket[Key.ID_LENGTH];
        for (int i = 0; i < Key.ID_LENGTH; i++) {
            buckets[i] = new Bucket(i);
        }
    }

    /**
     * 计算给定节点应放置的桶ID; bucketId是根据多远的距离来计算的节点远离本地节点。
     */
    public final int getBucketId(Key nid) {
        int bId = KadDataManager.instance().getLocalNode().getId().getDistance(nid) - 1;
        //如果我们试图将一个节点插入到它自己的路由表中，那么存储区ID将是-1，所以将其设置为0桶
        return bId < 0 ? 0 : bId;
    }

    public void addNode(Node node) {
        if (!node.equals(KadDataManager.instance().getLocalNode())) {
            buckets[getBucketId(node.getId())].addNode(node);
        }
    }

    public void addNodes(List<Node> nodes) {
        for (Node node : nodes) {
            addNode(node);
        }
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        for (Bucket bucket : buckets) {
            nodes.addAll(bucket.getNodes());
        }
        return nodes;
    }

    public List<Node> getSortedNodes(Key key) {
        List<Node> nodes = getNodes();
        Collections.sort(nodes, (node1, node2) -> node1.getId().getKey().xor(key.getKey()).abs().compareTo
                (node2.getId().getKey().xor(key.getKey()).abs()));
        return nodes;
    }

    public List<Node> findClosest(Key lookupId) {
        List<Node> nodes = getSortedNodes(lookupId);
        if (Commons.K >= nodes.size()) {
            return nodes;
        }
        return nodes.subList(0, Commons.K);
    }

    public void retireNode(Node node) {
        buckets[getBucketId(node.getId())].retireNode(node);
    }
}
