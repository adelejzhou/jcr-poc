/**
 *
 */
package com.acme.bean;

import java.io.Serializable;

/**
 *
 */
public class Author implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String DELIMITER = ", ";
	private String firstName;
	private String lastName;

	/**
	 * @param firstName
	 * @param lastName
	 */
	public Author(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(firstName);
		sb.append(DELIMITER);
		sb.append(lastName);
		return sb.toString();
	}
}
