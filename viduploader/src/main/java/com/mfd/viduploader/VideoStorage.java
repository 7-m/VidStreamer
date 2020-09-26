package com.mfd.viduploader;

import java.io.IOException;
import java.io.InputStream;

public interface VideoStorage {
	void init() throws IOException;

	String put(String vId, byte[] bytes)throws IOException;

	byte[] get(String vId);

	void destroy() throws IOException;
}
