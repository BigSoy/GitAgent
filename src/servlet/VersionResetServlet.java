package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import utils.EnvironmentProperty;

public class VersionResetServlet extends HttpServlet {
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
		String version = request.getParameter("version");
		
		if(StringUtils.isEmpty(lang) || StringUtils.isEmpty(username)
				|| StringUtils.isEmpty(projectname) || StringUtils.isEmpty(version))
			return;
		
		String repoRoot = null;
		String repoPath = null;
		if("cpp".equals(lang)) {
			repoRoot = EnvironmentProperty.get("cppRepository");
		} else if("java".equals(lang)) {
			repoRoot = EnvironmentProperty.get("javaRepository");
		}
		repoPath = repoRoot + File.separator + username + File.separator + projectname;

		// reset the specified version
		File repository = new File(repoPath);
		if(!repository.exists()) {
			System.out.println("repository does not exist!");
			return;
		}
		try {
			File gitDir = new File(repoPath + File.separator + ".git");
			if(!gitDir.exists()) {
				System.out.println("directory .git does not exist");
				return;
			}
			Git git = Git.open(repository);
			// check the specified version exists or not
			/*
			boolean versionExist = false;
			for(RevCommit revCommit : git.log().call()) {
				if(revCommit.getName().equals(version)) {
					versionExist = true;
					break;
				}
			}
			if(!versionExist) {
				System.out.println("specified version does not exist!");
				return;
			}
			*/
			git.reset().setMode(ResetCommand.ResetType.HARD).setRef(version).call();
			response.getWriter().println("success");
		} catch (GitAPIException e) {
			System.out.println("Git API exception");
		}
		
	}

}
