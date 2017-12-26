package com.zf.kademlia.data;

public class Test {

	public static void main(String[] args) {
		int[] inputs = { 0, 26, 51, 77, 102, 128, 153, 179, 205, 230, 255 };
		// int[] inputs = { 0, 26, 51, 77, 102, 128, 153, 179, 205, 230, 255 };

		for (int i : inputs) {
			double r = 1.6943 * Math.pow(Math.E, 0.0196 * i);
			long round = Math.round(r);
			 System.out.println(round);
			//System.out.println(Math.round(51.002 * Math.log(round) - 26.538));

		}

	}

}
