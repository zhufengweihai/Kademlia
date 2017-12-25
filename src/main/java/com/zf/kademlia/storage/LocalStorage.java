package com.zf.kademlia.storage;

import java.util.List;

import de.cgrotz.kademlia.node.Key;

/**
 * Created by Christoph on 23.09.2016.
 */
public interface LocalStorage {
    void put(Key key, Value value);

    Value get(Key key);

    boolean contains(Key key);

    List<Key> getKeysBeforeTimestamp(long timestamp);

    default void updateLastPublished(Key key, long timestamp) {
        Value node = get(key);
        node.setLastPublished(timestamp);
        put(key, node);
    }

    default void save() {

    }
}
