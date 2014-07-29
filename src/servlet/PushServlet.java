package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import utils.EnvironmentProperty;

public class PushServlet extends HttpServlet {
	private static final long serialVersionUID = 8320098602619843858L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String lang = request.getParameter("lang");
		String username = request.getParameter("username");
		String projectname = request.getParameter("projectname");
		
		if(StringUtils.isEmpty(lang) || StringUtils.isEmpty(username) || StringUtils.isEmpty(projectname))
			return;
		
		String repoRoot = null;
		String repoPath = null;
		if("cpp".equals(lang)) {
			repoRoot = EnvironmentProperty.get("cppRepository");
		} else if("java".equals(lang)) {
			repoRoot = EnvironmentProperty.get("javaRepository");
		}
		repoPath = repoRoot + File.separator + username + File.separator + projectname;
		
		// initial repository, commit
		File repository = new File(repoPath);
		if(!repository.exists()) {
			System.out.println("repository does not exist!");
			return;
		}
		try {
			File gitDir = new File(repoPath + File.separator + ".git");
			if(!gitDir.exists()) {
				Git.init().setDirectory(repository).call();
			}
			Git git = Git.open(repository);
			git.add().addFilepattern(".").call();
			RevCommit revCommit = git.commit().setCommitter(username, username).setMessage("update").call();
			String version = revCommit.getName();
			response.getWriter().println(version);
		} catch (GitAPIException e) {
			System.out.println("Git API exception");
		}
		
	}

}
