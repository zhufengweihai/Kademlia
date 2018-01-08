package com.zf.kademlia.routing;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import com.zf.kademlia.node.Key;

/**
 * @author zhufeng
 * @date 2017-12-15
 */
public class ValueTableImpl implements ValueTable {
	private Cache<Key, Value> map = null;
	private CacheManager cacheManager = null;

	public ValueTableImpl() {
		String alias = "kademlia";
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(CacheManagerBuilder.persistence(alias + File.separator + ".dat")) // 传入磁盘持久化的地址
				.withCache(alias,
						CacheConfigurationBuilder.newCacheConfigurationBuilder(Key.class, Value.class,
								ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES).disk(10,
										MemoryUnit.MB, true)) // 定义一个资源的磁盘池
				).build(true);
		map = cacheManager.getCache(alias, Key.class, Value.class);
	}

	@Override
	public void put(Key key, Value value) {
		map.put(key, value);
	}

	@Override
	public Value get(Key key) {
		return map.get(key);
	}

	@Override
	public boolean contains(Key key) {
		return map.containsKey(key);
	}

	@Override
	public void updateLastPublished(Key key, long timestamp) {
		Value node = get(key);
		node.setLastPublished(timestamp);
		put(key, node);
	}

	@Override
	public List<Key> getKeysBeforeTimestamp(long timestamp) {
		List<Key> keys = new ArrayList<>();
		Iterator<Entry<Key, Value>> it = map.iterator();
		while (it.hasNext()) {
			Entry<Key, Value> entry = it.next();
			if (entry.getValue().getLastPublished() <= timestamp) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	@Override
	public void dispose() {
		cacheManager.close();
	}

	public static void main(String[] args) {
		ValueTableImpl valueTable = new ValueTableImpl();
		// valueTable.map.clear();
		for (int i = 0; i < 200; i++) {
			String v = String.valueOf(i);
			// valueTable.put(Key.build(v), new
			// Value(System.currentTimeMillis(), v));
			System.out.println(valueTable.get(Key.build(v)));
		}

		valueTable.dispose();
	}
}
