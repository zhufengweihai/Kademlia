package com.zf.kademlia.data;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.lang.RandomStringUtils;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		// ObjectOutputStream oos = new ObjectOutputStream(new
		// FileOutputStream("d:\\employee.dat"));
		// Foo foo2 = new Foo();
		// oos.writeObject(foo2);
		// oos.flush();
		// oos.close();
		// ObjectInputStream ois = new ObjectInputStream(new
		// FileInputStream("d:\\employee.dat"));
		// Foo foo21 = (Foo) ois.readObject();
		//
		// System.out.println(foo21);
		// ois.close();
		// byte[] bytes = new byte[20];
		// SecureRandom instanceStrong = SecureRandom.getInstanceStrong();
		// instanceStrong.nextBytes(bytes);
		// System.out.print(bytes.hashCode());

		MessageDigest md = MessageDigest.getInstance("SHA1");
		String random = RandomStringUtils.random(10);
		System.out.println(random);
		md.update(random.getBytes());
		byte[] bits = md.digest();
		System.out.println(bits.length);
		System.out.println(Arrays.toString(bits));
	}
}

class Foo implements Serializable {
	private int x = 8;

	public Foo() {
		x = 12;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return "Foo [x=" + x + "]";
	}

}