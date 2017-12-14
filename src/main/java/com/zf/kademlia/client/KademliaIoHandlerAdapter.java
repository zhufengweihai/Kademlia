package com.zf.kademlia.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.operation.BaseOperation;

public class KademliaIoHandlerAdapter extends IoHandlerAdapter {
	private Map<Long, BaseOperation> operationMap = new HashMap<>();
	private Map<IoSession, Long> sessionMap = new HashMap<>();

	public void addOperation(BaseOperation operation) {

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		int networkTimeout = KadDataManager.instance().getConfig().getNetworkTimeout();
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, networkTimeout);
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.BOTH_IDLE) {
			session.closeNow();
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.exit(0);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println(message);
		session.closeNow();
	}
}
