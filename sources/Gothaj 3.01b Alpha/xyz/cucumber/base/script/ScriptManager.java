package xyz.cucumber.base.script;

import java.io.File;
import java.util.HashSet;

public class ScriptManager {

	private HashSet<Script> scripts = new HashSet();
	
	private File directory = new File("Gothaj/scripts");
	
	public ScriptManager() {
		if(!directory.exists()) directory.mkdirs();
		
		if(directory.listFiles().length == 0) return;
		
		for (File file : directory.listFiles()) {
	        if(!file.getName().endsWith(".java")) continue;
	        scripts.add(new Script(file));
	    }
	}
	
}
