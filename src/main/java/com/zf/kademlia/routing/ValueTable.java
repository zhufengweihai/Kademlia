package com.zf.kademlia.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.storage.Value;

/**
 * Created by zhufeng7 on 2017-11-24.
 */

public class ValueTable {
    private final ConcurrentHashMap<Key, Value> map = new ConcurrentHashMap<>();

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
