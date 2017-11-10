package com.zf.kademlia.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

		short s = Short.MIN_VALUE;
		for (int i = 0; i < 65537; i++) {
			s++;

		}
		System.out.print(System.currentTimeMillis());
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