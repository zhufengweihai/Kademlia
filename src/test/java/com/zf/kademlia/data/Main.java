package com.zf.kademlia.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// ObjectOutputStream oos = new ObjectOutputStream(new
		// FileOutputStream("d:\\employee.dat"));
		// Foo foo2 = new Foo();
		// oos.writeObject(foo2);
		// oos.flush();
		// oos.close();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("d:\\employee.dat"));
		Foo foo21 = (Foo) ois.readObject();

		System.out.println(foo21);
		ois.close();
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