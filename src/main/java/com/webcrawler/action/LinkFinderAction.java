package com.webcrawler.action;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.webcrawler.handler.LinkHandler;

/**
 * @author Sanjay
 * 
 *         This Class implements the RecursiveAction of the forkjoin pool which
 *         recursively evaluates the links that it crawls adds the link to
 *         action to visit pages in parallel.
 */
public class LinkFinderAction extends RecursiveAction {
	/* Logger object */
	final static Logger logger = Logger.getLogger(LinkFinderAction.class);
	/* Computes the action recursively in parallel */
	private String link;
	/* Handler to hold the reference to webcrawler object */
	private LinkHandler linkHandler;;

	public LinkFinderAction(String link, LinkHandler linkHandler) {
		this.link = link;
		this.linkHandler = linkHandler;
	}

	/* computes the action recursively in parallel */
	@Override
	public void compute() {

		if (!linkHandler.isVisited(link)) {
			try {
				List<RecursiveAction> actions = new ArrayList<>();
				logger.debug("Started crawling the page " + link);
				System.out.println("Started crawling the page " + link);
				URL uriLink = new URL(link);
				URLConnection urlConnection = uriLink.openConnection();
				if (urlConnection == null) {
					linkHandler.addErrorLinks(link);
				} else {
					Parser parser = new Parser(urlConnection);
					NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							LinkTag extractedLinks = (LinkTag) list.elementAt(i);

							if (!extractedLinks.extractLink().isEmpty()
									&& linkHandler.isVisited(extractedLinks.extractLink().toString())) {
								actions.add(new LinkFinderAction(extractedLinks.extractLink(), linkHandler));
							}
						}

						linkHandler.addVisitedLinks(link);

					}
					// invoke recursively
					invokeAll(actions);
				}
			} catch (Exception e) {
				logger.error("Error while computinng the pagelinks", e);
			}
		} else {
			linkHandler.addSkippedLinks(link);
		}
	}
}
