package com.zf.kademlia.operation;

import com.zf.kademlia.protocol.KadMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhufeng
 * @date 2017/12/3
 */
@RequiredArgsConstructor
@Getter
public class OperationMessage {
    private final KadMessage message;
}
