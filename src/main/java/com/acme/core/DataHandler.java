/**
 *
 */
package com.acme.core;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;

import com.acme.bean.Article;
import com.acme.bean.Author;

/**
 *
 */
public interface DataHandler {

	void setSession(Session session);

	void setRepoMainNode(Node repoMainNode);

	void setWorkspace(Workspace workspace);

	Node addArticle(Article article) throws Exception;

	Node addAuthor(Article article, Author author) throws Exception;

	Article[] getArticles(Author author) throws Exception;
}
