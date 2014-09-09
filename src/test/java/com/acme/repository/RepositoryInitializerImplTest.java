/**
 *
 */
package com.acme.repository;

import static org.junit.Assert.fail;

import javax.jcr.Session;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RepositoryInitializerImplTest {

	Logger log = LoggerFactory.getLogger(RepositoryInitializerImplTest.class);

	@Test
	public void initialize() {

		log.info(RepositoryInitializerImplTest.class + ", initialize");

		Session session = null;
		RepositoryInitializer repo = new RepositoryInitializerImpl("test",
				"http://fake.com");
		try {
			String configFile = "target/classes/repository/repository.xml";
			String repHomeDir = "target/classes/repository";
			session = repo.initializeRepository(configFile, repHomeDir);
		} catch (Exception e) {
			log.error(e.toString());
			fail();
		} finally {
			if (session == null) {
				log.warn("session is NULL!");
				fail();
			} else {
				session.logout();
			}
		}
	}
}
