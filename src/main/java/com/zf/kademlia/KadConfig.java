package com.zf.kademlia;

import java.io.Serializable;
import java.util.List;

import com.zf.kademlia.routing.Contact;

public class KadConfig implements Serializable {
	private Contact localNode = null;
	private List<Contact> nodes = null;
}
