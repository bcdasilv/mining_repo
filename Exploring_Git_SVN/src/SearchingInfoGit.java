import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.nyu.cs.javagit.api.DotGit;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.WorkingTree;
import edu.nyu.cs.javagit.api.commands.GitCheckout;
import edu.nyu.cs.javagit.api.commands.GitCheckoutResponse;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.CommitFile;

public class SearchingInfoGit {
	
	private static HashMap<String, Integer> components = new HashMap<String, Integer>();
	
	private static final String SRC_ROOT_FOLDER = "src/";
	
	private static Pattern CLASSNAME_REGEX = Pattern.compile("src/.*/(.*?)\\.java$");
	private static Pattern PACKAGE_REGEX = Pattern.compile("src/(.*)/.*?\\.java$");
	
	//static String REPO_PATH = "git-repos/";
	static String REPO_PATH = "C:/Users/Bruno/git/Rhino/rhino/";
	//static String REPO_PATH = "D:/junit/junit/";
	//static String REPO_PATH = "C:/Users/Bruno/git/org.eclipse.mylyn.tasks/";
	//static String REPO_PATH = "C:/Users/Bruno/git/freemind/";
//	static String REPO_PATH = "C:/Users/Bruno/git/voldemort/";
	
//	public static ScmFile incrementComponentCount(HashMap<ScmFile, Integer>components, String path) {
//    	ScmFile scmFile = new ScmFile(path);
//    	
//    	Integer changeCount = new Integer(0);
//    	if (components.containsKey(scmFile)) {
//    		changeCount = (Integer) components.get(scmFile);
//    	}
//    	
//    	changeCount++;
//    	components.put(scmFile, changeCount);
//    	
//    	return scmFile;
//	}
	
		
//	private static void addInfoToFiles(HashMap<ScmFile, Integer>components) throws IOException {
//		System.out.println("computing info...");
//		
//		Collection<ScmFile> toRemove = new ArrayList<ScmFile>(); 
//		
//		for (ScmFile scmFile : components.keySet()) {
//			File file = new File(REPO_PATH + "/" + scmFile.getLocalPath());
//			String contents = null;
//			if (file.exists())
//				contents = readFile(file);
//			else
//				toRemove.add(scmFile);
//			scmFile.extractInfo(contents);
//		}
//		
//		for (ScmFile scmFile : toRemove) {
//			components.remove(scmFile);
//		}
//		
//		System.out.println("ok");
//	}
	
	// http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
//	private static String readFile(File file) throws IOException {
//		  FileInputStream stream = new FileInputStream(file);
//		  try {
//		    FileChannel fc = stream.getChannel();
//		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
//		    /* Instead of using default, pass in a decoder. */
//		    return Charset.defaultCharset().decode(bb).toString();
//		  }
//		  finally {
//		    stream.close();
//		  }
//		}
	
//	public static void writeCountToFile(HashMap<ScmFile, Integer> components, String filename)
//	throws IOException {
//		/* 
//		 * Bruno
//		 * */	        
//
//		File file = new File(filename);
//		if(file.exists())
//			file.delete();
//		else
//			file.createNewFile();
//		FileWriter output = new FileWriter(file);
//		output.write("Package\tFileName\tNumberOfChanges\tFileType\tLOC\n");
//
//		Set<ScmFile> compsSet = components.keySet();
//		for ( Iterator<ScmFile> compsSetIterator = compsSet.iterator( ); compsSetIterator.hasNext( ); ) {
//			ScmFile comp = (ScmFile)compsSetIterator.next();
//			Integer changeCount = (Integer) components.get(comp);
//
//			output.write(comp.getPackageName() + "\t" + comp.getClassName() + "\t" + changeCount + "\t" + comp.getType() + "\t" + comp.getLinesOfCode() + "\n");
//			//output.close();
//		}
//	}
	
	// based on http://wiki.eclipse.org/JGit/User_Guide
	public static void main(String[] args) throws IOException, JavaGitException {
		
		//SCMtoXML scmXml;
		
		//HashMap<ScmFile, Integer> components = new HashMap<ScmFile, Integer>();

		File repositoryDirectory = new File(REPO_PATH);
		
		DotGit dotGit = DotGit.getInstance(repositoryDirectory);
//		WorkingTree tree = dotGit.getWorkingTree();
		GitLogOptions options = new GitLogOptions();
		options.setOptFileDetails(true);

		// Get the current working tree
//		WorkingTree wt = dotGit.getWorkingTree();

		// Getting the current working branch
//		Ref master = wt.getCurrentBranch();
//		System.out.println("CurrentBranch: "+master);
//		
		//Só tá vindo aqui o master
//		System.out.println("Branches: ");
//		Iterator<Ref> refs = dotGit.getBranches();
//		while(refs.hasNext()){
//			System.out.println(refs.next().getName());
//		}

		
		////////////////////////////////////
		File repoPathDot = new File(REPO_PATH + "/.");
		List<File> repoPathDotList = new ArrayList<File>();
		repoPathDotList.add(repoPathDot);
//		GitCheckout checkout = new GitCheckout();
		List<Commit> gitlog = dotGit.getLog(options);
//		int version = 1; //count the number of versions at least one java file was modified
//		boolean javaFileChanged = false;
		//scmXml = new SCMtoXML("JUnit"); //TODO: a user interface to choose the project name or get it automatically from Git
		//This is a flag used to control handling only commits after a given SHA. This SHA refers to the commit where Rhino 1.6R5 was released.
		//As the commits are gathered from the most recent to the most old, we start the flag as true.
//		boolean goAhead = true;
		try{
			System.out.println("TOTAL NUMBER OF COMMITS: "+gitlog.size());
			for (edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit commit : gitlog) {
				System.out.println(commit.getSha()+" Commit with " + commit.getFilesChanged() + " changes. Date: "+commit.getDateString());
				//System.out.println("     Msg: "+commit.getMessage());
				String hash = commit.getSha();
//				if(goAhead){
//					Ref ref = Ref.createSha1Ref(hash);
					
//					GitCheckoutResponse response = checkout.checkout(repositoryDirectory, ref, repoPathDotList);
//					
//					String logMsg = commit.getMessage();
	                //if( (logMsg!=null) && ( (logMsg.indexOf("1.0")!= -1) || (logMsg.indexOf("release")!=-1) ) ){
//	            	if( logMsg!=null ){
//		                System.out.println( "---------------------------------------------" );
//		                System.out.println ("revision: " + commit.getSha( ) );
//		                System.out.println( "author: " + commit.getAuthor( ) );
//		                System.out.println( "date: " + commit.getDateString( ) );
//		                System.out.println( "log message: " + commit.getMessage( ) );
	                
		                List<CommitFile> files = commit.getFiles();
		                if (files == null || files.size() == 0)
		                	continue;
					
//		                System.out.println( "changed paths:" );
		                for (CommitFile file : files) {	
		                	String path = file.getName();
		                	//System.out.println(path);
		                	Integer changeCount = new Integer(0);
		                	//if (isJavaFile(path)){
		                	if (path.contains(SRC_ROOT_FOLDER) && path.contains(".java")){
		                		//ScmFile scmFile = new ScmFile(path);
			        			File aafile = new File(REPO_PATH + "/" + path);
//			        			String contents = null;
			        			if (aafile.exists()) {
			        				//String className = firstGroup(CLASSNAME_REGEX, path);
			        				//String packageName = firstGroup(PACKAGE_REGEX, path); //.replace('/', '.');
			        				String className = getJavaFile(path);
			        				String packageName = getPackageFromPath(path);
			        				packageName = packageName.replace('/', '.');
			        				if(!packageName.equals("")){
			                        	if(components.containsKey(packageName+className))
			                        		changeCount = (Integer) components.get(packageName+className);
			                        	changeCount++;
		                        		//components.put(packageName+"."+className, changeCount);
			                        	components.put(packageName+className, changeCount);
		                        	}		        				
	//		        				javaFileChanged = true;
	//			    				contents = readFile(aafile);
	//			        			scmFile.extractInfo(contents);
	//			    				System.out.println("v" + version + ": " + scmFile.getPackageName() + "." + scmFile.getClassName() + " LOC =  " + scmFile.getLinesOfCode());
				    				
	//			    				Element packageElement = scmXml.addPackage(scmFile.getPackageName());
	//		    					Element classElement = scmXml.addClassToPackage(packageElement, scmFile.getClassName(), scmFile.getType());
	//								scmXml.addVersionToClass(classElement, version, scmFile.getLinesOfCode());
			        			}
		                	}
		                }
//						if(hash.equals("dd972afb55245a330860745cf34046b9ae6ebd6a")){
		                if(hash.equals("d9f64b050ef30835a63dce6a57b6d7bd420733c7")){
							//Ok, as we reached the point we want to, we don't to need to look anymore...
							break;
						}
//							goAhead = false;
//					if(javaFileChanged)
//					{
//						//scmXml.addLog(version, hash, commit.getAuthor(), commit.getDateString(), commit.getMessage());					
//						version++;
//						javaFileChanged = false;
//						//Warning: This version counting is only for our internal purpose. This is not git version counting.
//						//This counting only considers the java file modifications in the commits.
//						//When a commit is done without modifying at least one java file, the version counting is not considered.
//					}
//	                }

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
	        System.out.println(" ==============================================================");
	        System.out.println(" ======================== CHANGE COUNT ========================");	
	        System.out.println(" ==============================================================");
	        
			File file = new File("ChangeCountOnGIT.csv");
			if(file.exists())
				file.delete();
			else
				file.createNewFile();
			FileWriter output = new FileWriter(file);
			output.write("Component, CC\n");
	        Set<String> compsSet = components.keySet();
	        for (String comp : compsSet) {
	        	Integer changeCount = (Integer) components.get(comp);	        	
	        	
//	        	String packageName = "";
//	        	String javaFileName = "";
//	        	
//	        	String[] compSplit = comp.split(" ");
//	        	packageName = compSplit[0];
//	        	javaFileName = compSplit[1];
	        	//System.out.println(packageName+javaFileName+","+changeCount);
	        	System.out.println(comp+","+changeCount);
	        	//output.write(packageName+javaFileName+","+changeCount+"\n");
	        	
//	        	String revisions = (String)compsAndRevisions.get(comp);
	        	
	        	output.write(comp+","+changeCount+"\n");
//	        	output.write(comp+","+changeCount+","+revisions+"\n");
	        }
        	output.close();			
		}
//		scmXml.setProjectLastVersion(version);
//		scmXml.writeToFile();
//
//		System.out.println(components.keySet().size() + " files analyzed.");
//		
//		addInfoToFiles(components);
//		
//		writeCountToFile(components, "FilesChange2.txt");
	}
	

	private static String getJavaFile(String path)
	{
		//not all the projects have a /src/ folder...
		//int index = path.indexOf("/src");
		if(path.contains(".java")) //just confirming that we are handling a src path 
		{	
			String srcPath = path.substring(SRC_ROOT_FOLDER.length());
			StringTokenizer tokenizer = new StringTokenizer(srcPath,"/");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (token.contains(".java"))
					return token.substring(0, token.indexOf(".java"));
			}
		}
		return "";		
	}
	
	private static String getPackageFromPath(String path)
	{
		String srcPackagePath = "";
		String convertedPackagePath = "";
		//not all the projects have a /src/ folder...
		//int index = path.indexOf("/src");
		if(path.contains(".java")) //just confirming that we are handling a src folder 
		{	
			//this statement below works for projects with a /src folder
			//srcPackagePath = path.substring(index+4);
			srcPackagePath = path.substring(SRC_ROOT_FOLDER.length());
			StringTokenizer tokenizer = new StringTokenizer(srcPackagePath,"/");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (!token.contains(".java"))
					convertedPackagePath += token+".";
			}
		}
		return convertedPackagePath;
	}
	
	public static boolean isJavaFile(String path) {
		return matches(CLASSNAME_REGEX, path);
	}
	
	public static boolean matches(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find();
	}
	
	public static String firstGroup(Pattern p, String s) {
		Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

}
