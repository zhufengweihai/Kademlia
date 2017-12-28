package com.zf.kademlia.routing;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhufeng
 * @date 2017-12-15
 */
@Getter
@Setter
@AllArgsConstructor
public class Value implements Serializable{
	private long lastPublished;
	private String content;
}
