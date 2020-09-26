package com.mfd.vidindexer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;

@RestController
public class Api {

	final VideoRepository repo;

	@Autowired
	public Api(VideoRepository repo) {
		this.repo = repo;
	}

	@GetMapping(path = "/indexer/search/{name}")
	SearchResponseDto searchByName(@PathVariable("name") String name) throws IOException {
		return new SearchResponseDto(repo.retrieveByText(name));
	}


	/**
	 * Retrieves and sends link of the video
	 * @throws IOException
	 */
	@GetMapping(path = "/indexer/fetch/{vid}")
	Video retrieveById(@PathVariable("vid") String vid) throws IOException {
		return repo.retrieve(vid);
	}
	@GetMapping(path = "/indexer/")
	void retrieveById(Writer writer) throws IOException {
		writer.write("ok from indexer!");
	}

	@GetMapping(path = "/indexer/new")
	SearchResponseDto retrieveRecentUploads(@RequestParam("count")int maxIds){
		maxIds = Math.max(10, maxIds);
		return  new SearchResponseDto(repo.retrieveRecent(maxIds));
	}
}
