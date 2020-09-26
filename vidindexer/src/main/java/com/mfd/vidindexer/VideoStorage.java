package com.mfd.vidindexer;

import java.io.IOException;

public interface VideoStorage {
	void init() throws IOException;

	String put(String name, byte[] bytes)throws IOException;

	byte[] get(String name);

	void destroy() throws IOException;
}
