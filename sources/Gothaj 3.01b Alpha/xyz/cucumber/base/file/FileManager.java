package xyz.cucumber.base.file;

import java.util.ArrayList;

import xyz.cucumber.base.file.files.AccountsFile;
import xyz.cucumber.base.file.files.BindFile;
import xyz.cucumber.base.utils.FileUtils;

public class FileManager {

	private ArrayList<FileUtils> files = new ArrayList();

	public FileManager() {
		files.add(new BindFile());
		files.add(new AccountsFile());
	}

	public void load() {
		for(FileUtils file : files){
			file.load();
		}
	}
	public void save() {
		for(FileUtils file : files){
			file.save();
		}
	}
	
	public FileUtils getFile(Class file) {
		for(FileUtils f : files){
			if(f.getClass() == file) return f;
		}
		return null;
	}
}
