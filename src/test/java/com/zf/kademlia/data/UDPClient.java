package com.zf.kademlia.data;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	private static final int TIMEOUT = 5000; // ���ý������ݵĳ�ʱʱ��
	private static final int MAXNUM = 5; // �����ط����ݵ�������

	public static void main(String args[]) throws IOException {
		String str_send = "Hello UDPserver";
		byte[] buf = new byte[1024];
		DatagramSocket ds = new DatagramSocket(9000);
		InetAddress loc = InetAddress.getLocalHost();
		DatagramPacket dp_send = new DatagramPacket(str_send.getBytes(), str_send.length(), loc, 18567);
		DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
		ds.setSoTimeout(TIMEOUT);

		ds.send(dp_send);
		ds.receive(dp_receive);
		if (!dp_receive.getAddress().equals(loc)) {
			throw new IOException("Received packet from an umknown source");
		}

		String str_receive = new String(dp_receive.getData(), 0, dp_receive.getLength()) + " from "
				+ dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();
		System.out.println(str_receive);
		// ����dp_receive�ڽ���������֮�����ڲ���Ϣ����ֵ���Ϊʵ�ʽ��յ���Ϣ���ֽ�����
		// ��������Ҫ��dp_receive���ڲ���Ϣ����������Ϊ1024
		dp_receive.setLength(1024);

		ds.close();
	}
}