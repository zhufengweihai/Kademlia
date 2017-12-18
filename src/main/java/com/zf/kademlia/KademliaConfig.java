package com.zf.kademlia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KademliaConfig {
	private int k = 8;
	private int timeout = 5000;
	private int networkTimeout = 5000;
	private int retriesCount = 2;
	private int retryInterval = 1000;
	private int refreshInterval = 60 * 60 * 1000;
}
