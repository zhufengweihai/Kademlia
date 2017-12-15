package com.zf.kademlia.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zf.kademlia.node.Key;

/**
 * @author zhufeng
 * @date 2017-12-15
 */
public class ValueTable {
	private ConcurrentHashMap<Key, Value> map = new ConcurrentHashMap<>();

	public void put(Key key, Value value) {
		map.put(key, value);
	}

	public Value get(Key key) {
		return map.get(key);
	}

	public boolean contains(Key key) {
		return map.containsKey(key);
	}

	public void updateLastPublished(Key key, long timestamp) {
		Value node = get(key);
		node.setLastPublished(timestamp);
		put(key, node);
	}

	public List<Key> getKeysBeforeTimestamp(long timestamp) {
		List<Key> keys = new ArrayList<>();
		Set<Map.Entry<Key, Value>> entries = map.entrySet();
		for (Map.Entry<Key, Value> entry : entries) {
			if (entry.getValue().getLastPublished() <= timestamp) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
}
