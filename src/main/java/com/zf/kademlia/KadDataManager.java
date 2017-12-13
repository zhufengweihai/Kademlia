package com.zf.kademlia;

import com.zf.kademlia.node.Node;
import com.zf.kademlia.routing.RoutingTable;
import com.zf.kademlia.routing.ValueTable;

import lombok.Data;

/**
 * @author zhufeng7 on 2017-11-24.
 */
@Data
public class KadDataManager {
    private Node localNode = null;
    private RoutingTable routingTable = null;
    private ValueTable valueTable = null;
    private static KadDataManager instance = new KadDataManager();

    private KadDataManager() {
    }

    public static KadDataManager instance() {
        return instance;
    }
}
