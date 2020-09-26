package com.mfd.viduploader;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

@RestController
public class Api {

	public static final String MP4_MIME_TYPE = "video/mp4";

	final VideoStorage    storage;
	final VideoRepository repo;
	final Random          r = new Random();


	@Autowired
	public Api(VideoStorage storage, VideoRepository repo) {
		this.storage = storage;
		this.repo = repo;
	}

	@GetMapping(path = "/uploader/insert")
	void handleHello(Writer w) throws IOException {
		w.write("This is Video Uploader. Hi!\n");
	}

	@PostMapping(path = "/uploader/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	UploadResponseDto uploadVideo(VideoDto dto, @RequestHeader(name ="authorization" ) String authTok) throws IOException, UnauthorizedException {

		// upload file to bucket
		// add it to databse


		String uploader = validateToken(authTok);



		if (dto.getName().isBlank() || dto.getFile() == null) {
			throw new InvalidDataException("One or more required fields are empty");
		}

		if (dto.getFile().getContentType() == null || !dto.getFile().getContentType().equals(MP4_MIME_TYPE)) {
			throw new InvalidDataException("Invalid file format/MIME type. Only .mp4 video files allowed");
		}

		String vId = String.valueOf(Math.abs(r.nextLong()));
		String link = storage.put(vId+".mp4", dto.getFile().getBytes());

		repo.insertVideo(new Video(uploader, vId, link, dto.getTags(), dto.getName(), System.currentTimeMillis()));

		return new UploadResponseDto(link, "Video uploaded.");

	}

	private String validateToken(String authTok) throws UnauthorizedException {
		Algorithm alg = Algorithm.HMAC256("passwordpasswordpassword");
		var verifier = JWT.require(alg).withIssuer("vidauth").build();

		try {
			return verifier.verify(authTok).getSubject();

		} catch (JWTVerificationException e) {
			throw new UnauthorizedException("Token expired or verification failed");
		}



	}

	@GetMapping(path = "/uploader")
	void handleHome(Writer writer) throws IOException {
		writer.write("ok from uploader !");
	}
}
