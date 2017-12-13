package com.zf.kademlia.node;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Data
@EqualsAndHashCode(of = "key")
public class Key {
    public final static int ID_LENGTH = 160;

    private BigInteger key = null;

    private Key(byte[] result) {
        this.key = new BigInteger(result);
    }

    public Key(String key) {
        this.key = new BigInteger(key, 16);
    }

    public Key(BigInteger key) {
        this.key = key;
    }

    public static Key build(String key) {
        return new Key(key);
    }

    public static Key random() {
        try {
            byte[] bytes = new byte[ID_LENGTH / 8];
            SecureRandom.getInstance("SHA1").nextBytes(bytes);
            return new Key(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate key error", e);
        }
    }

    /**
     * 计算this key和to key的异或值
     */
    public Key xor(Key to) {
        return new Key(to.getKey().xor(this.key));
    }

    /**
     * 生成一个距此key指定距离的key
     */
    public Key generateNodeIdByDistance(int distance) {
        byte[] result = new byte[ID_LENGTH / 8];
        int numByteZeroes = (ID_LENGTH - distance) / 8;
        int numBitZeroes = distance % 8;

        byte b = 0;
        int n = 128;
        for (int i = numBitZeroes; i < 8; i++) {
            b += n >> i;
        }
        result[numByteZeroes] = b;
        for (int i = numByteZeroes + 1; i < result.length; i++) {
            result[i] = Byte.MAX_VALUE;
        }

        return this.xor(new Key(result));
    }

    /**
     * 计算最高有效位序号
     */
    private int getFirstSetBitIndex() {
        byte[] distances = key.toByteArray();
        for (int i = 0; i < distances.length; i++) {
            int index = getFirstBitIndex(distances[i]);
            if (index < 8) {
                return i * 8 + index;
            }
        }
        return 0;
    }

    private int getFirstBitIndex(byte b) {
        if (b == 0) {
            return 8;
        }
        int j = 0;
        int n = 256;
        for (; j < 8; j++) {
            if ((b & (n >>= 1)) > 0) {
                break;
            }
        }
        return j;
    }

    @Override
    public String toString() {
        return this.key.toString(16);
    }

    /**
     * 计算this key和to key的异或值
     * 然后获得异或返回键的第一个位的索引i
     * 计算距离为他们之间的距离是ID_LENGTH - i
     */
    public int getDistance(Key to) {
        return ID_LENGTH - this.xor(to).getFirstSetBitIndex();
    }
}
