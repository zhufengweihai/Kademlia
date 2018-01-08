package com.zf.kademlia.routing;

import java.util.List;

import com.zf.kademlia.node.Key;

public interface ValueTable {

	void put(Key key, Value value);

	Value get(Key key);

	boolean contains(Key key);

	void updateLastPublished(Key key, long timestamp);

	List<Key> getKeysBeforeTimestamp(long timestamp);

	void dispose();

	public static ValueTable build() {
		return new ValueTableImpl();
	}
}