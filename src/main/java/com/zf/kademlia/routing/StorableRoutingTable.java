package com.zf.kademlia.routing;

import java.util.Iterator;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

public class StorableRoutingTable extends RoutingTable {
	private Cache<Integer, Bucket> buckets = null;
	private CacheManager cacheManager = null;

	public StorableRoutingTable() {
		super();
		CacheConfigurationBuilder<Integer, Bucket> builder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Integer.class, Bucket.class, ResourcePoolsBuilder
						.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES).disk(10, MemoryUnit.MB, true));
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder().with(CacheManagerBuilder.persistence("buckets.dat"))
				.withCache("nodes", builder).build(true);

		buckets = cacheManager.getCache("buckets", Integer.class, Bucket.class);
		Iterator<Entry<Integer, Bucket>> it = buckets.iterator();
		while (it.hasNext()) {
			Cache.Entry<Integer, Bucket> entry = it.next();
			addBucket(entry.getValue());
		}
	}
}