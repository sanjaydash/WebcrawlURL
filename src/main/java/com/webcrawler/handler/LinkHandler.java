package com.webcrawler.handler;

/**
 * Interface Defines the LinkHanler that is used store
 * VisitedLink,SkippedLinks,ErrorLinks.
 * 
 */
public interface LinkHandler {

	/**
	 * Add this link as VisitedLink
	 * 
	 * @param link
	 * @return
	 */

	void addVisitedLinks(String link);

	/**
	 * Add this link as SkippedLink
	 * 
	 * @param extractedLinks
	 * @return
	 */

	void addSkippedLinks(String link);

	/**
	 * Add this link as ErrorLink
	 * 
	 * @param extractedLinks
	 * @return
	 */

	void addErrorLinks(String link);

	/**
	 * Checks if the link was already visited
	 * 
	 * @param link
	 * @return
	 */

	boolean isVisited(String link);
}
