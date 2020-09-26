package com.mfd.vidindexer;

public class VideoNotFoundException extends RuntimeException {
	public VideoNotFoundException(String id) {
		super(String.format("Video with Id %s not found", id));
	}
}
