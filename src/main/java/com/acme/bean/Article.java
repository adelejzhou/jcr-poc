/**
 *
 */
package com.acme.bean;

import java.io.Serializable;

/**
 *
 */
public class Article implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String DELIMITER = "; ";
	private Journal journal;
	private String doi;
	private String title;
	private Author[] authors;

	/**
	 * @param doi
	 * @param title
	 * @param authors
	 */
	public Article(String doi, String title, Author[] authors, Journal journal) {
		super();
		this.doi = doi;
		this.title = title;
		this.authors = authors;
		this.journal = journal;
	}

	/**
	 * @return the journal
	 */
	public Journal getJournal() {
		return journal;
	}

	/**
	 * @param journal the journal to set
	 */
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	/**
	 * @return the doi
	 */
	public String getDoi() {
		return doi;
	}

	/**
	 * @param doi the doi to set
	 */
	public void setDoi(String doi) {
		this.doi = doi;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the authors
	 */
	public Author[] getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(Author[] authors) {
		this.authors = authors;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(journal);
		sb.append(DELIMITER);
		sb.append(doi);
		sb.append(DELIMITER);
		sb.append(title);
		sb.append(DELIMITER);
		for(Author author : authors) {
			sb.append(author);
			sb.append(DELIMITER);
		}
		return sb.toString();
	}

}
