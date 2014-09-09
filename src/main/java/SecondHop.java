import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.TransientRepository;

/**
 * Second hop example. Stores, retrieves, and removes example content.
 */
public class SecondHop {

	/**
	 * The main entry point of the example application.
	 * 
	 * @param args
	 *            command line arguments (ignored)
	 * @throws Exception
	 *             if an error occurs
	 */
	public static void main(String[] args) throws Exception {
//		Repository repository = new TransientRepository();
//		 session = repository.login(new SimpleCredentials("username",
//				"password".toCharArray()));

		Repository repository = JcrUtils.getRepository(
				"http://localhost:8080/rmi");

		Session session = repository.login(new SimpleCredentials("admin", "admin"
				.toCharArray()));
		Workspace workspace = session.getWorkspace();

		try {
			Node root = session.getRootNode();
			
			workspace.getNamespaceRegistry().registerNamespace("test", "http://localhost:8080/repository/test");

			// Store content
			Node hello = root.addNode("hello");
			Node world = hello.addNode("world");
			world.setProperty("message", "Hello, World!");
			session.save();

			// Retrieve content
			Node node = root.getNode("hello/world");
			System.out.println(node.getPath());
			System.out.println(node.getProperty("message").getString());

			// Remove content
			//root.getNode("hello").remove();
			//session.save();
		} finally {
			session.logout();
		}
	}

}