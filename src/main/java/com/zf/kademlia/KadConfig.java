package com.zf.kademlia;

import java.io.Serializable;
import java.util.List;

import com.zf.kademlia.node.Node;

public class KadConfig implements Serializable {
	private Node localNode = null;
	private List<Node> nodes = null;
}
