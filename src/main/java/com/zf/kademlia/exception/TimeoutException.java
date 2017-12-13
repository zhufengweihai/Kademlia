package com.zf.kademlia.exception;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
public class TimeoutException extends RuntimeException {
    public TimeoutException(Exception e) {
        super(e);
    }

    public TimeoutException() {
    }
}
