package com.zf.kademlia.data;

public class Test {

	public static void main(String[] args) {
		int[] inputs = { 0, 26, 51, 77, 102, 128, 153, 179, 205, 230, 255 };

		for (int i : inputs) {
			double r = 1.56 * Math.pow(Math.E, 0.02 * i);
			System.out.println(r);
		}

	}

}
