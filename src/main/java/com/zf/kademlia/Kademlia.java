package com.zf.kademlia;

import com.zf.kademlia.common.Commons;

public interface Kademlia {
	boolean start();

	void start(Commons kadConfig);

	void stop();

	void proccess();
}
