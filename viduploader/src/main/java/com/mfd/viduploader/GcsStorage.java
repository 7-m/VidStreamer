package com.mfd.viduploader;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GcsStorage implements VideoStorage {

	private final String projectId;
	private final String jsonKey;
	private final String bucketName;

	public GcsStorage(String projectId, String jsonKey, String bucketName) {
		this.projectId = projectId;
		this.jsonKey = jsonKey;
		this.bucketName = bucketName;
	}

	private Bucket bucket;

	@Override
	public void init() throws IOException {

		StorageOptions options = StorageOptions.newBuilder()
											   .setProjectId(projectId)
											   .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonKey.getBytes())))
											   .build();


		Storage storage = options.getService();
		bucket = storage.get(bucketName);

	}

	@Override
	public String put(String vId, byte[] bytes) {
		final String BLOB_NAME = vId;
		var blob = bucket.create(BLOB_NAME, bytes, Bucket.BlobTargetOption.doesNotExist());

		return String.format("https://storage.googleapis.com/%s/%s", blob.getBucket(), blob.getName());
	}

	@Override
	public byte[] get(String vId) {
		return bucket.get(vId).getContent();
	}

	@Override
	public void destroy() {

	}


}
