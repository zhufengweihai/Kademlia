package com.zf.kademlia.common;

import static java.io.File.separator;

public interface Commons {
	int K = 8;
	int ID_LENGTH = 160;
	String LOCAL_FOLDER = "kad";
	String PATH_ROUTING_TABLE = System.getProperty("user.home") + separator + LOCAL_FOLDER + separator + "routing.dat";
	String PATH_BOOTSTRAP = System.getProperty("user.home") + separator + LOCAL_FOLDER + separator + "bootstrap.dat";
	int TIMEOUT = 3000;

	int PORT = 19683;

	int CODE_BOOTSTRAP = 0x032D;
	byte CODE_PING = 'P';
	byte CODE_STORE = 'S';
	byte CODE_FIND_NODE = 'N';
	byte CODE_FIND_VALUE = 'V';

	int TIME_INTERVAL = 15;// ∑÷÷”

}
