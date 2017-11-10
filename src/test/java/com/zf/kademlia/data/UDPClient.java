package com.zf.kademlia.data;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	private static final int TIMEOUT = 5000; // 设置接收数据的超时时间
	private static final int MAXNUM = 5; // 设置重发数据的最多次数

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
		// 由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
		// 所以这里要将dp_receive的内部消息长度重新置为1024
		dp_receive.setLength(1024);

		ds.close();
	}
}