/**
 *
 */
package com.acme.bean;

/**
 *
 */
public class Journal {

	private static final String DELIMITER = ", ";
	private String id;
	private String title;
	private boolean openAccess;
	/**
	 * @param id
	 * @param title
	 * @param openAccess
	 */
	public Journal(String id, String title, boolean openAccess) {
		super();
		this.id = id;
		this.title = title;
		this.openAccess = openAccess;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the openAccess
	 */
	public boolean getOpenAccess() {
		return openAccess;
	}
	/**
	 * @param openAccess the openAccess to set
	 */
	public void setOpenAccess(boolean openAccess) {
		this.openAccess = openAccess;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id);
		sb.append(DELIMITER);
		sb.append("title: " + title);
		sb.append(DELIMITER);
		sb.append("openAccess: " + openAccess);
		return sb.toString();
	}
}
