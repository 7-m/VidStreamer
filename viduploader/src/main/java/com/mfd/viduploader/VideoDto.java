package com.mfd.viduploader;

import org.springframework.web.multipart.MultipartFile;

public class VideoDto {
	MultipartFile file;
	String        name;
	String        tags;


	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Video{" +
				"file=" + file +
				", name='" + name + '\'' +
				", tags='" + tags + '\'' +
				'}';
	}
}
