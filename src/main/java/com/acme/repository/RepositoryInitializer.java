/**
 *
 */
package com.acme.repository;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Workspace;

/**
 *
 */
public interface RepositoryInitializer {

	Session initializeRepository(String configFile, String repHomeDir) throws Exception;

	/**
	 * @return the repository
	 */
	public Repository getRepository();

	/**
	 * @return the root
	 */
	public Node getRepoMainNode();

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace();

	/**
	 * @return the session
	 */
	public Session getSession();

	public String getNamespace();
}
