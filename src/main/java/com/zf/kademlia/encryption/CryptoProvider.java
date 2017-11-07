package com.zf.kademlia.encryption;

public interface CryptoProvider {
	byte[] encrypt(byte[] data);

	byte[] decrypt(byte[] data);
}
