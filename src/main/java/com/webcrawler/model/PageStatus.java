package com.webcrawler.model;

/**
 * Enum represents the status of pages that are either visited skipped or error.
 *
 */
public enum PageStatus {

	SUCCESS("Success"), SKIPPED("Skipped"), ERROR("Error");

	private String status;

	PageStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
