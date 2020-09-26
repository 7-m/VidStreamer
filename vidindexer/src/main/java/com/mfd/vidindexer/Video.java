package com.mfd.vidindexer;

import java.util.Objects;

public class Video {

	String uploader;
	String vid;
	String link;
	String tags;
	String name;
	long uploadTimestamp;

	public Video(String uploader, String vid, String link, String tags, String name, long uploadTimestamp) {
		this.uploader = uploader;
		this.vid = vid;
		this.link = link;
		this.tags = tags;
		this.name = name;
		this.uploadTimestamp = uploadTimestamp;
	}

	public long getUploadTimestamp() {
		return uploadTimestamp;
	}

	public void setUploadTimestamp(long uploadTimestamp) {
		this.uploadTimestamp = uploadTimestamp;
	}



	@Override
	public String toString() {
		return "Video{" +
				"uploader='" + uploader + '\'' +
				", vid='" + vid + '\'' +
				", link='" + link + '\'' +
				", tags='" + tags + '\'' +
				", name='" + name + '\'' +
				", uploadTimestamp=" + uploadTimestamp +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Video video = (Video) o;
		return uploadTimestamp == video.uploadTimestamp &&
				uploader.equals(video.uploader) &&
				vid.equals(video.vid) &&
				link.equals(video.link) &&
				tags.equals(video.tags) &&
				name.equals(video.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uploader, vid, link, tags, name, uploadTimestamp);
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
