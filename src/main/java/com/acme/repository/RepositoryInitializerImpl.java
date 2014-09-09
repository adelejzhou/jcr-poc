/**
 *
 */
package com.acme.repository;

import javax.jcr.NamespaceException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RepositoryInitializerImpl implements RepositoryInitializer {

	Logger log = LoggerFactory.getLogger(RepositoryInitializerImpl.class);

	private Repository repository;
	private Node repoMainNode;
	private Workspace workspace;
	private Session session;

	private String namespace;
	private String url;

	/**
	 * @param namespace
	 * @param url
	 */
	public RepositoryInitializerImpl(String namespace, String url) {
		super();
		this.namespace = namespace;
		this.url = url;
		log.debug("Repository initialized");
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @return the root
	 */
	public Node getRepoMainNode() {
		return repoMainNode;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	public Session initializeRepository(String configFile, String repHomeDir)
			throws Exception {

		repository = JcrUtils.getRepository(
				"http://localhost:8080/rmi");
		log.debug("Persistent repository created");

		session = repository.login(new SimpleCredentials("admin", "admin"
				.toCharArray()));
		log.debug("Session created");

		workspace = session.getWorkspace();
		log.debug("Workspace created");

		Node root = session.getRootNode();
		log.debug("Root node created");

		try {
			workspace.getNamespaceRegistry().registerNamespace(namespace, url);
			log.debug("Workspace added as: " + namespace + ", " + url);

			repoMainNode = root.addNode(namespace);
			log.debug("repoMainNode created");

		} catch (NamespaceException e) {
			log.warn(e.getMessage());

			repoMainNode = root.getNode(namespace);
			log.debug("repoMainNode collected");
		}

		log.debug("Saving the session");
		session.save();

		return session;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.acme.repository.RepositoryInitializer#getNamespace()
	 */
	public String getNamespace() {
		return namespace;
	}

}
