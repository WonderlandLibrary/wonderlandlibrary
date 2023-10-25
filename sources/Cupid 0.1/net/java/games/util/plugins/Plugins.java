package net.java.games.util.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Plugins {
  static final boolean DEBUG = true;
  
  List pluginList = new ArrayList();
  
  public Plugins(File pluginRoot) throws IOException {
    scanPlugins(pluginRoot);
  }
  
  private void scanPlugins(File dir) throws IOException {
    File[] files = dir.listFiles();
    if (files == null)
      throw new FileNotFoundException("Plugin directory " + dir.getName() + " not found."); 
    for (int i = 0; i < files.length; i++) {
      File f = files[i];
      if (f.getName().endsWith(".jar")) {
        processJar(f);
      } else if (f.isDirectory()) {
        scanPlugins(f);
      } 
    } 
  }
  
  private void processJar(File f) {
    try {
      System.out.println("Scanning jar: " + f.getName());
      PluginLoader loader = new PluginLoader(f);
      JarFile jf = new JarFile(f);
      for (Enumeration en = jf.entries(); en.hasMoreElements(); ) {
        JarEntry je = en.nextElement();
        System.out.println("Examining file : " + je.getName());
        if (je.getName().endsWith("Plugin.class")) {
          System.out.println("Found candidate class: " + je.getName());
          String cname = je.getName();
          cname = cname.substring(0, cname.length() - 6);
          cname = cname.replace('/', '.');
          Class pc = loader.loadClass(cname);
          if (loader.attemptPluginDefine(pc)) {
            System.out.println("Adding class to plugins:" + pc.getName());
            this.pluginList.add(pc);
          } 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public Class[] get() {
    Class[] pluginArray = new Class[this.pluginList.size()];
    return (Class[])this.pluginList.toArray((Object[])pluginArray);
  }
  
  public Class[] getImplementsAny(Class[] interfaces) {
    List matchList = new ArrayList(this.pluginList.size());
    Set interfaceSet = new HashSet();
    for (int j = 0; j < interfaces.length; j++)
      interfaceSet.add(interfaces[j]); 
    for (Iterator i = this.pluginList.iterator(); i.hasNext(); ) {
      Class pluginClass = i.next();
      if (classImplementsAny(pluginClass, interfaceSet))
        matchList.add(pluginClass); 
    } 
    Class[] pluginArray = new Class[matchList.size()];
    return (Class[])matchList.<Class[]>toArray((Class[][])pluginArray);
  }
  
  private boolean classImplementsAny(Class testClass, Set interfaces) {
    if (testClass == null)
      return false; 
    Class[] implementedInterfaces = testClass.getInterfaces();
    int i;
    for (i = 0; i < implementedInterfaces.length; i++) {
      if (interfaces.contains(implementedInterfaces[i]))
        return true; 
    } 
    for (i = 0; i < implementedInterfaces.length; i++) {
      if (classImplementsAny(implementedInterfaces[i], interfaces))
        return true; 
    } 
    return classImplementsAny(testClass.getSuperclass(), interfaces);
  }
  
  public Class[] getImplementsAll(Class[] interfaces) {
    List matchList = new ArrayList(this.pluginList.size());
    Set interfaceSet = new HashSet();
    for (int j = 0; j < interfaces.length; j++)
      interfaceSet.add(interfaces[j]); 
    for (Iterator i = this.pluginList.iterator(); i.hasNext(); ) {
      Class pluginClass = i.next();
      if (classImplementsAll(pluginClass, interfaceSet))
        matchList.add(pluginClass); 
    } 
    Class[] pluginArray = new Class[matchList.size()];
    return (Class[])matchList.<Class[]>toArray((Class[][])pluginArray);
  }
  
  private boolean classImplementsAll(Class testClass, Set interfaces) {
    if (testClass == null)
      return false; 
    Class[] implementedInterfaces = testClass.getInterfaces();
    int i;
    for (i = 0; i < implementedInterfaces.length; i++) {
      if (interfaces.contains(implementedInterfaces[i])) {
        interfaces.remove(implementedInterfaces[i]);
        if (interfaces.size() == 0)
          return true; 
      } 
    } 
    for (i = 0; i < implementedInterfaces.length; i++) {
      if (classImplementsAll(implementedInterfaces[i], interfaces))
        return true; 
    } 
    return classImplementsAll(testClass.getSuperclass(), interfaces);
  }
  
  public Class[] getExtends(Class superclass) {
    List matchList = new ArrayList(this.pluginList.size());
    for (Iterator i = this.pluginList.iterator(); i.hasNext(); ) {
      Class pluginClass = i.next();
      if (classExtends(pluginClass, superclass))
        matchList.add(pluginClass); 
    } 
    Class[] pluginArray = new Class[matchList.size()];
    return (Class[])matchList.<Class[]>toArray((Class[][])pluginArray);
  }
  
  private boolean classExtends(Class testClass, Class superclass) {
    if (testClass == null)
      return false; 
    if (testClass == superclass)
      return true; 
    return classExtends(testClass.getSuperclass(), superclass);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\game\\util\plugins\Plugins.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */