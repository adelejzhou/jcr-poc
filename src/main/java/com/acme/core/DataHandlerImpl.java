/**
 *
 */
package com.acme.core;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.bean.Article;
import com.acme.bean.Author;
import com.acme.bean.Journal;

/**
 *
 */
public class DataHandlerImpl implements DataHandler {

	private Logger log = LoggerFactory.getLogger(DataHandlerImpl.class);

	private final String JOURNAL_PREFIX = "journal";
	private final String ID_PREFIX = "id";
	private final String ARTICLE_PREFIX = "article";
	private final String AUTHORS_LIST_PREFIX = "authors";
	private final String AUTHOR_PREFIX = "author";
	private final String AUTHOR_FIRST_NAME_PREFIX = "firstName";
	private final String AUTHOR_LAST_NAME_PREFIX = "lastName";
	private final String TITLE_PREFIX = "title";
	private final String DOI_PREFIX = "doi";
	private final String OPENACCESS_PREFIX = "openaccess";

	private Node repoMainNode;
	private Session session;
	private Workspace workspace;

	public void setSession(Session session) {
		this.session = session;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public void setRepoMainNode(Node repoMainNode) {
		this.repoMainNode = repoMainNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.acme.core.DataHandler#addArticle(com.acme.bean.Article)
	 */
	public Node addArticle(Article article) throws Exception {
		log.info("adding the node for: " + article);

		log.info("Checking if the article is already stored");
		Node articleNode = searchArticle(article);

		if (articleNode != null) {
			log.info("Article: " + article + " already in the repository");
			return articleNode;
		}

		articleNode = repoMainNode.addNode(ARTICLE_PREFIX);
		session.save();
		log.debug(articleNode.getPath());

		log.debug("Adding the doi");
		articleNode.setProperty(DOI_PREFIX, article.getDoi());

		log.debug("Adding the title");
		articleNode.setProperty(TITLE_PREFIX, article.getTitle());

		log.debug("Adding the authors list");
		Node authorsNode = articleNode.addNode(AUTHORS_LIST_PREFIX);

		log.debug("Adding the author properties");
		for (Author author : article.getAuthors()) {
			Node authorNode = authorsNode.addNode(AUTHOR_PREFIX);
			log.debug(authorNode.getPath());

			authorNode.setProperty(AUTHOR_FIRST_NAME_PREFIX,
					author.getFirstName());
			authorNode.setProperty(AUTHOR_LAST_NAME_PREFIX,
					author.getLastName());
			log.debug("Added author: " + author);
		}

		Journal journal = article.getJournal();
		Node journalNode = articleNode.addNode(JOURNAL_PREFIX);
		journalNode.setProperty(ID_PREFIX, journal.getId());
		journalNode.setProperty(TITLE_PREFIX, journal.getTitle());
		journalNode.setProperty(OPENACCESS_PREFIX, journal.getOpenAccess());

		log.debug("Committing the new article node");
		session.save();

		return articleNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.acme.core.DataHandler#addAuthor(com.acme.bean.Article,
	 * com.acme.bean.Author)
	 */
	public Node addAuthor(Article article, Author author) throws Exception {
		log.info("Adding author: " + author + " to: " + article);
		Node articleNode = searchArticle(article);
		log.debug(articleNode.getPath());

		log.debug("Getting the authors node: " + articleNode);
		Node authorsNode = articleNode.getNode(AUTHORS_LIST_PREFIX);
		log.debug(authorsNode.getPath());

		log.debug("Adding the author to: " + articleNode);
		Node authorNode = authorsNode.addNode(AUTHOR_PREFIX);
		log.debug(authorNode.getPath());

		authorNode.setProperty(AUTHOR_FIRST_NAME_PREFIX, author.getFirstName());
		authorNode.setProperty(AUTHOR_LAST_NAME_PREFIX, author.getLastName());
		log.debug("Added author: " + author);

		session.save();

		return authorNode;
	}

	/**
	 * performs an XPath query to collect the article in the repo using its doi
	 *
	 * @param article
	 * @return
	 * @throws Exception
	 *             if more than one is retrieved
	 */
	private Node searchArticle(Article article) throws Exception {
		log.debug("Searching for article: " + article);

		QueryManager queryManager = workspace.getQueryManager();
		Query query = queryManager.createQuery(
				"SELECT * FROM [nt:unstructured] as article WHERE article."
						+ DOI_PREFIX + " = '" + article.getDoi() + "'",
				Query.JCR_SQL2);
		// "//" + ARTICLE_PREFIX + "[@"
		// + DOI_PREFIX + " = '" + article.getDoi() + "']", Query.XPATH);

		log.debug(query.getStatement());
		QueryResult results = query.execute();

		NodeIterator nodeIter = results.getNodes();
		Node articleNode = null;
		try {
			articleNode = nodeIter.nextNode();

			if (nodeIter.hasNext()) {
				String message = "More than one node returned";
				log.error(message);
				throw new Exception(message);
			}

		} catch (Exception e) {
			log.debug(e.toString());
		}

		return articleNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.acme.core.DataHandler#getArticles(com.acme.bean.Author)
	 */
	public Article[] getArticles(Author author) throws Exception {
		QueryManager queryManager = workspace.getQueryManager();
		// xpath should be something like the following:
		// ====> //article[authors/author[@firstname='he' and
		// @lastname='himself']]

		String lastName = author.getLastName();
		String firstName = author.getFirstName();

		String queryStr = "//element(" + ARTICLE_PREFIX + ", nt:unstructured)["
				+ AUTHORS_LIST_PREFIX + "/" + AUTHOR_PREFIX + "/"
				+ AUTHOR_FIRST_NAME_PREFIX + "='" + firstName + "' and "
				+ AUTHORS_LIST_PREFIX + "/" + AUTHOR_PREFIX + "/"
				+ AUTHOR_LAST_NAME_PREFIX + "='" + lastName + "']";

		Query query = null;
		QueryResult results = null;

		try {

			query = queryManager.createQuery(queryStr, Query.XPATH);
			log.debug(query.getStatement());

			results = query.execute();
			log.debug("Query for searching articles by author executed");

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(
					"The parameters or the query provided are incorrect");
		}

		NodeIterator nodeIter = results.getNodes();

		List<Article> articles = new LinkedList<Article>();

		log.debug("Cycling among the results");
		while (nodeIter.hasNext()) {
			Node articleNode = nodeIter.nextNode();

			Node journalNode = articleNode.getNode(JOURNAL_PREFIX);

			String id = journalNode.getProperty(ID_PREFIX).getString();
			String journalTitle = journalNode.getProperty(TITLE_PREFIX)
					.getString();
			boolean openAccess = journalNode.getProperty(OPENACCESS_PREFIX)
					.getBoolean();

			Journal journal = new Journal(id, journalTitle, openAccess);

			String doi = articleNode.getProperty(DOI_PREFIX).getString();
			String title = articleNode.getProperty(TITLE_PREFIX).getString();
			NodeIterator authorNodes = articleNode
					.getNodes(AUTHORS_LIST_PREFIX);

			int i = 0;
			Author[] authors = new Author[Long.valueOf(authorNodes.getSize())
					.intValue()];
			while (authorNodes.hasNext()) {
				Node authorParentNode = authorNodes.nextNode();
				Node authorNode = authorParentNode.getNode(AUTHOR_PREFIX);
				firstName = authorNode.getProperty(AUTHOR_FIRST_NAME_PREFIX)
						.getString();
				lastName = authorNode.getProperty(AUTHOR_LAST_NAME_PREFIX)
						.getString();
				Author authorRetrieved = new Author(firstName, lastName);

				log.debug("Adding author: " + authorRetrieved);
				authors[i++] = authorRetrieved;
			}

			Article article = new Article(doi, title, authors, journal);

			log.debug("Adding article: " + article);
			articles.add(article);
		}

		return articles.toArray(new Article[articles.size()]);
	}
}
