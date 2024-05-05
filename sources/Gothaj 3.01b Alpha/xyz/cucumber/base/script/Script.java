package xyz.cucumber.base.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;

public class Script {
private String code;
	
	private String version;
	
	private Mod module;
	
	private Object instance;
	
	private Method onEnableMethod;
	private Method onDisableMethod;
	
	public Script(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String st = null;

			try {
				while ((st = br.readLine()) != null)    	
					 System.out.println(st);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			code = st;

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			compiler.getTask(null, fileManager, null, null, null, fileManager.getJavaFileObjects(file)).call();
			try {
				fileManager.close();

				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.getParentFile().toURI().toURL()});
				Class<?> loadedClass = Class.forName("YourJavaFile", true, classLoader);
				
				instance = loadedClass.getDeclaredConstructor().newInstance();
				
				Method getName = loadedClass.getDeclaredMethod("getName");
				Method getDescription = loadedClass.getDeclaredMethod("getDescription");
				Method getCategory = loadedClass.getDeclaredMethod("getCategory");
				
				handleMethods(loadedClass);
				
				module = new Mod((String)getName.invoke(instance),(String)getDescription.invoke(instance),Category.COMBAT);
				Client.INSTANCE.getModuleManager().getModules().add(module);
			}catch (Exception e){
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void handleMethods(Class<?> loadedClass) {
		onEnableMethod = methodReturn("onEnable", loadedClass);
		onDisableMethod = methodReturn("onDisable", loadedClass);
	}
	private Method methodReturn(String name,Class<?> loadedClass) {
		try {
			return loadedClass.getDeclaredMethod(name);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void onEnable() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(module == null || onEnableMethod == null) return;
		onEnableMethod.invoke(instance);
	}
	public void onDisable() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(module == null) return;
		onDisableMethod.invoke(instance);
	}
}