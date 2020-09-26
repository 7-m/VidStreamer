package com.mfd.viduploader;

public class UploadResponseDto {
	String vidLink;
	String message;


	public UploadResponseDto(String vidLink, String message) {
		this.vidLink = vidLink;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVidLink() {
		return vidLink;
	}

	public void setVidLink(String vidLink) {
		this.vidLink = vidLink;
	}
}
