/**
 *
 */
package com.acme.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.JcrUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.bean.Article;
import com.acme.bean.Author;
import com.acme.bean.Journal;

/**
 *
 */
public class DataHandlerImplTest {

	private Logger log = LoggerFactory.getLogger(DataHandlerImplTest.class);

	private final String namespace = "test";

	private Repository repo = null;
	private DataHandler dh = null;
	private Session session = null;

	private static Journal journal = new Journal("123", "Nature", false);

	@Before
	public void startup() {

		log.info("Initializing repo...");

		try {
			repo = JcrUtils.getRepository("http://localhost:8080/rmi");
			log.debug("Repository obtained");

			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			log.debug("Session created");

			Workspace workspace = session.getWorkspace();
			log.debug("Workspace created");

			Node root = session.getRootNode();
			log.debug("Root node created");

			Node repoMainNode = root.getNode(namespace);

			dh = new DataHandlerImpl();

			dh.setSession(session);
			dh.setRepoMainNode(repoMainNode);
			dh.setWorkspace(workspace);

			log.info("Initialization done");
		} catch (Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

	@After
	public void saveSession() {
		session.logout();
	}

	@Test
	public void addArticle() {

		log.info(DataHandlerImplTest.class + ", addArticle()");

		Author[] authors = { new Author("John", "Doe"),
				new Author("Mike", "Foo") };
		Article article = new Article("10.1038/2012.11109", "Olympic failure",
				authors, journal);

		try {
			dh.addArticle(article);
			session.save();
		} catch (Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

	@Test
	public void addAuthor() {

		log.info(DataHandlerImplTest.class + ", addAuthor()");

		Author[] authors = { new Author("John", "Doe"),
				new Author("Mike", "Foo") };
		Article article = new Article("10.1038/2012.11109", "Olympic failure",
				authors, journal);

		String firstName = "New";
		String lastName = "Author";
		Author author = new Author(firstName, lastName);

		try {
			dh.addAuthor(article, author);
		} catch (Exception e) {
			log.error(e.toString());
			fail();
		}
	}

	@Test
	public void getArticles() {

		log.info(DataHandlerImplTest.class + ", getArticles()");

		String firstName = "John";
		String lastName = "Doe";
		Author author = new Author(firstName, lastName);

		try {
			Article[] articles = dh.getArticles(author);
			assertNotNull(articles);
			assertTrue(articles.length > 0);
		} catch (Exception e) {
			log.error(e.getMessage());
			fail();
		}

	}
}
