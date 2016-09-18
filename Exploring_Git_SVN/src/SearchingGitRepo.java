import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class SearchingGitRepo {
	
	static String REPO_PATH = "C:/Users/Bruno/git/Rhino/rhino/";
	
	public static void main(String args[]) throws IOException{
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(REPO_PATH))
//		.readEnvironment() // scan environment GIT_* variables
//		.findGitDir() // scan up the file system tree
		.build();

		System.out.println(repository.toString());
		
		RevWalk walk = new RevWalk(repository);
		// ...
		System.out.println(walk);
		
		for (RevCommit commit : walk) {
			System.out.println(commit.getId().getName());
		}
		walk.dispose();
	}
}
