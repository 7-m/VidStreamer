package com.mfd.viduploader;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;




public class TestGoogleCloudStorageCalls {

	@Test
	void createAndDeleteBlob() throws IOException {

		String PROJECT_ID = "cloud-test-254814";
		String PATH_TO_JSON_KEY = "/home/mfd/Downloads/cloud-storage-key.json";
		String BUCKET_NAME = "user-uploaded-videos";
		String OBJECT_NAME = "my-object";

		StorageOptions options = StorageOptions.newBuilder()
											   .setProjectId(PROJECT_ID)
											   .setCredentials(GoogleCredentials.fromStream(
													   new FileInputStream(PATH_TO_JSON_KEY))).build();

		Storage storage = options.getService();
		// Creates the new bucket

		Bucket bucket = storage.get(BUCKET_NAME);


		// upload a blob and delete it

		final String BLOB_NAME = "xyz123";
		bucket.create(BLOB_NAME, new byte[10], Bucket.BlobTargetOption.doesNotExist());
		bucket.get(BLOB_NAME).delete();


	}
}
