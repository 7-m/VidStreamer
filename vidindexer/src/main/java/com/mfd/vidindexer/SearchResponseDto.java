package com.mfd.vidindexer;


import java.util.List;

public class SearchResponseDto {
	List<String> ids;

	public SearchResponseDto(List<String> retrieveByText) {
		ids = retrieveByText;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}
