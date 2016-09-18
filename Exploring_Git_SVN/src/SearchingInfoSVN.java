
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class SearchingInfoSVN {

// 	private static final String SRC_ROOT_FOLDER = "/tomcat/trunk/java/";
//	private static final String SRC_ROOT_FOLDER = "/trunk/findbugs/src/";
//	private static final String SRC_ROOT_FOLDER = "/jEdit/trunk/";
	//private static final String SRC_ROOT_FOLDER = "/trunk/Jmol/src/";
//	private static final String SRC_ROOT_FOLDER = "/freecol/trunk/src";
	//private static final String SRC_ROOT_FOLDER = "/base-one/trunk/src";
	private static final String SRC_ROOT_FOLDER = "/branches/jfreechart-1.0.x-branch/source";
 	
	public static void main(String args[]) throws IOException{
		
		File logFile = new File("commitsLogSvn.txt");
		FileWriter fw = new FileWriter(logFile, false);
		
//		File specificModuleLogFile = new File("commitsLogSvn_jEdit_java.txt");
//		FileWriter fw_specificModuleLogFile = new FileWriter(specificModuleLogFile, false);
		
		//Field added by Bruno in order to keep a mapping between components and their number of changes
		//Maybe it can be turned into an specific class if we have to deal with more information besides change counting
		HashMap<String, Integer> components = new HashMap<String, Integer>();
		//Adding this hashmap field just for testing... if it becomes permanent, it is better to put all these fields in a 
		//new class as the above comment
		//HashMap<String, String> compsAndRevisions = new HashMap<String, String>();
			
		 DAVRepositoryFactory.setup( );
			//We can build an interface for choosing the url
	        //String url = "http://svn.svnkit.com/repos/svnkit";
	        //String url = "http://mybatis.googlecode.com/svn";
//		 	String url = "http://svn.apache.org/repos/asf/tomcat/trunk/";
//		 	String url = "https://findbugs.googlecode.com/svn/trunk";
		 	//String url = "https://jedit.svn.sourceforge.net/svnroot/jedit"; 
//		 	String url = "https://svn.code.sf.net/p/jedit/svn";
//			String url = "https://jfreechart.svn.sourceforge.net/svnroot/jfreechart";
			String url = "https://svn.code.sf.net/p/jfreechart/code/";
			//String url = "https://jmol.svn.sourceforge.net/svnroot/jmol";
		 	//String url = "https://jabref.svn.sourceforge.net/svnroot/jabref";
//		 	String url = "https://svn.code.sf.net/p/freecol/code/";
		 //	String url = "https://hsqldb.svn.sourceforge.net/svnroot/hsqldb";
			
			//We can also open an interface for typing username, passwd, and the revision interval
	        String name = "anonymous";
	        String password = "anonymous";
					
//	        long startRevision = 17785;//jedit 4.3.2 (used in our manual mapping)
//	        long startRevision = 921110;//tomcat 6.0.26
//	        long startRevision = 10103;//findbgus 1.3.5
	        long startRevision = 3;//jfreechart 1.0.6
//	        long startRevision = 5686;//freecol 0.8.4
	        //long endRevision = 20044; //the end revision of jedit in ICPC'12 study
	        long endRevision = -1; //HEAD (the latest) revision
			

	        SVNRepository repository = null;
	        try {
	            repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( url ) );
	            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( name, password );
	            repository.setAuthenticationManager( authManager );

	            Collection logEntries = null;
	            
	            logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );
	            for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
	                SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
	                System.out.println( "---------------------------------------------" );
	                //fw.write( "---------------------------------------------"+'\n');
	                String temp = "revision: " + logEntry.getRevision(); 
	                System.out.println (temp);
	                //fw.write(temp+'\n');
	                temp =  "author: " + logEntry.getAuthor( ) ;
	                System.out.println(temp);
	                //fw.write(temp+'\n');
	                temp = "date: " + logEntry.getDate( ) ;
	                System.out.println(temp);
	                //fw.write(temp+'\n');
	                temp = "log message: " + logEntry.getMessage( );
	                System.out.println(temp);
	                temp = logEntry.getMessage( ).replaceAll("(\\r|\\n)", " ");
	                String logMsg = temp;
	                fw.write(logMsg+'\n');

	                if ( logEntry.getChangedPaths( ).size( ) > 0 ) {
	                    System.out.println( );
	                    //fw.write('\n');
	                    System.out.println( "changed paths:" );
	                    //fw.write("changed paths:");
	                    Set changedPathsSet = logEntry.getChangedPaths( ).keySet( );

	                    for ( Iterator changedPaths = changedPathsSet.iterator( ); changedPaths.hasNext( ); ) {
	                        SVNLogEntryPath entryPath = ( SVNLogEntryPath ) logEntry.getChangedPaths( ).get( changedPaths.next( ) );
	                        temp =  " "
                                + entryPath.getType( )
                                + " "
                                + entryPath.getPath( )
                                + ( ( entryPath.getCopyPath( ) != null ) ? " (from "
                                        + entryPath.getCopyPath( ) + " revision "
                                        + entryPath.getCopyRevision( ) + ")" : "" );
                                        
	                        System.out.println(temp);
	                        
//	                        if (entryPath.getPath().equals("/jEdit/trunk/org/gjt/sp/jedit/jEdit.java"))
//	                        	fw_specificModuleLogFile.write(logMsg+'\n');
	                        
	                        //fw.write(temp+'\n');
	                        /* 
	                         * [Bruno]
	                         * In this implementation, the code only considers the full path to identify a class.
	                         * So if a class moves to another package, this code identifies the class as a different one,
	                         * because the call to the method containsKey is used by passing the full path.
	                         * */
	                        Integer changeCount = new Integer(0);
	                        if (entryPath.getPath()!=null && isJavaSourcePath(entryPath.getPath())){
	                        	//I've changed this block and include this if statement.
	                        	//I've just put the original code into this condition in order to
	                        	//count only the classes that have being modified
//	                        	if((entryPath.getType() == SVNLogEntryPath.TYPE_MODIFIED) 
//	                        			|| (entryPath.getType() == SVNLogEntryPath.TYPE_DELETED)
//	                        			|| (entryPath.getType() == SVNLogEntryPath.TYPE_REPLACED)
//	                        			|| (entryPath.getType() == SVNLogEntryPath.TYPE_ADDED)){
		                        	String javaFile = getJavaFile(entryPath.getPath());
		                        	
		                        	String packageFromPath = getPackageFromPath(entryPath.getPath());
		                        	//the method called above returns an empty string if the package is not a /src package
		                        	if(!packageFromPath.equals("")){
			                        	//if(components.containsKey(packageFromPath+" "+javaFile))
			                        	if(components.containsKey(packageFromPath+javaFile))
			                        		changeCount = (Integer) components.get(packageFromPath+javaFile);
			                        		//changeCount = (Integer) components.get(packageFromPath+" "+javaFile);
			                        	
//			                        	String revisions = "";
//			                        	if(compsAndRevisions.containsKey(packageFromPath+javaFile))
//			                        		revisions = (String)compsAndRevisions.get(packageFromPath+javaFile);
			                        	
			                        	changeCount++;
		                        		components.put(packageFromPath+javaFile, changeCount);
//		                        		revisions += logEntry.getRevision()+",";
//			                        	compsAndRevisions.put(packageFromPath+javaFile, revisions);
			                        	//components.put(packageFromPath+" "+javaFile, changeCount);
//		                        	}
	                        	}
	                        }
	                    }
	                }
	            }
	            System.out.println(logEntries.size()+" commits analyzed!");
	            //fw.write(logEntries.size()+" commits analyzed!\n");
	            fw.close();
//	            fw_specificModuleLogFile.close();
	        }catch(Exception e){e.printStackTrace();}
	        
            /* 
             * Bruno
             * */	        
	        System.out.println(" ==============================================================");
	        System.out.println(" ======================== CHANGE COUNT ========================");	
	        System.out.println(" ==============================================================");
	        
			File file = new File("ChangeCount.csv");
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
	
	//Bruno
	private static boolean isJavaSourcePath(String path)
	{
		//if (path.contains("/src/") && path.contains(".java"))
		if (path.contains(SRC_ROOT_FOLDER) && path.contains(".java"))
			return true;
		return false;
	}
	
	//Bruno
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
	
	//Bruno
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
}
