package com.zf.kademlia.routing;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhufeng
 * @date 2017-12-15
 */
@Getter
@Setter
public class Value {
	private long lastPublished;
	private String content;
}
