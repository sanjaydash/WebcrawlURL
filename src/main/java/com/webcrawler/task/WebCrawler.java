package com.webcrawler.task;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.webcrawler.action.LinkFinderAction;
import com.webcrawler.handler.LinkHandler;
import com.webcrawler.model.PageStatus;

/**
 * @author Sanjay The class used to store which store
 *         VisitedLink,SkippedLinks,ErrorLinks. It invokes the forkjoin pool
 *         with the starting page, prints the output of the result.
 */
public class WebCrawler implements LinkHandler {
	/* Logger object */
	final static Logger logger = Logger.getLogger(LinkFinderAction.class);
	/* Synchronized set of all the visited links */
	private Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
	/* Synchronized set of all the skipped links */
	private Collection<String> skippedLinks = Collections.synchronizedSet(new HashSet<String>());
	/* Synchronized set of all the error links */
	private Collection<String> errorLinks = Collections.synchronizedSet(new HashSet<String>());

	/* Starting page */
	private final String startPage;
	/* Forkjoin pool which runs the recursive task */
	private ForkJoinPool mainPool;

	public WebCrawler(String startPage, int maxThreads) {
		this.startPage = startPage;
		mainPool = new ForkJoinPool(maxThreads);
	}

	/**
	 * Start the Crawling operation with the starting page
	 */
	public void beginCrawling() {

		logger.info("Started Crawling with start page " + startPage);
		mainPool.invoke(new LinkFinderAction(startPage, this));
	}

	/**
	 * Add this link as VisitedLink
	 * 
	 * @param link
	 * @return Collection
	 */

	@Override
	public void addVisitedLinks(String link) {
		visitedLinks.add(link);

	}

	/**
	 * Add this link as SkippedLink
	 * 
	 * @param link
	 * @return Collection
	 */

	@Override
	public void addSkippedLinks(String link) {
		skippedLinks.add(link);

	}

	/**
	 * Add this link as Errorlinks
	 * 
	 * @param link
	 * @return Collection
	 */

	@Override
	public void addErrorLinks(String link) {
		errorLinks.add(link);

	}

	@Override
	public boolean isVisited(String link) {
		return visitedLinks.contains(link);
	}

	/**
	 * Returns the visitedlinks
	 * 
	 * 
	 */

	public Collection<String> getVisitedLinks() {
		return visitedLinks;
	}

	/**
	 * Returns the skippedlinks
	 * 
	 * 
	 */

	public Collection<String> getSkippedLinks() {
		return skippedLinks;
	}

	/**
	 * Returns the errorlinks
	 * 
	 * 
	 */

	public Collection<String> getErrorLinks() {
		return errorLinks;
	}

	/**
	 * Prints the Pages that are successful, skipped ,Error
	 */
	public void getPageStatus() {
		System.out.println(PageStatus.SUCCESS.getStatus() + ":" + visitedLinks.toString());
		System.out.println(PageStatus.SKIPPED.getStatus() + ":" + skippedLinks.toString());
		System.out.println(PageStatus.ERROR.getStatus() + ":" + errorLinks.toString());

	}

	/**
	 * @param args the command line arguments page source, starting page and the
	 *             maxThreads
	 */
	public static void main(String[] args) throws Exception {
		WebCrawler webCrawler1 = new WebCrawler("http://www.google.com", 64);
		webCrawler1.beginCrawling();
		webCrawler1.getPageStatus();
	}

}