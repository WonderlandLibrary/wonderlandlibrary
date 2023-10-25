package org.apache.logging.log4j.core.config.plugins.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ResolverUtil {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String VFSZIP = "vfszip";
  
  private static final String VFS = "vfs";
  
  private static final String JAR = "jar";
  
  private static final String BUNDLE_RESOURCE = "bundleresource";
  
  private final Set<Class<?>> classMatches = new HashSet<>();
  
  private final Set<URI> resourceMatches = new HashSet<>();
  
  private ClassLoader classloader;
  
  public Set<Class<?>> getClasses() {
    return this.classMatches;
  }
  
  public Set<URI> getResources() {
    return this.resourceMatches;
  }
  
  public ClassLoader getClassLoader() {
    return (this.classloader != null) ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
  }
  
  public void setClassLoader(ClassLoader aClassloader) {
    this.classloader = aClassloader;
  }
  
  public void find(Test test, String... packageNames) {
    if (packageNames == null)
      return; 
    for (String pkg : packageNames)
      findInPackage(test, pkg); 
  }
  
  public void findInPackage(Test test, String packageName) {
    Enumeration<URL> urls;
    packageName = packageName.replace('.', '/');
    ClassLoader loader = getClassLoader();
    try {
      urls = loader.getResources(packageName);
    } catch (IOException ioe) {
      LOGGER.warn("Could not read package: {}", packageName, ioe);
      return;
    } 
    while (urls.hasMoreElements()) {
      try {
        URL url = urls.nextElement();
        String urlPath = extractPath(url);
        LOGGER.info("Scanning for classes in '{}' matching criteria {}", urlPath, test);
        if ("vfszip".equals(url.getProtocol())) {
          String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
          URL newURL = new URL(url.getProtocol(), url.getHost(), path);
          JarInputStream stream = new JarInputStream(newURL.openStream());
          try {
            loadImplementationsInJar(test, packageName, path, stream);
          } finally {
            close(stream, newURL);
          } 
          continue;
        } 
        if ("vfs".equals(url.getProtocol())) {
          String containerPath = urlPath.substring(1, urlPath.length() - packageName.length() - 2);
          File containerFile = new File(containerPath);
          if (containerFile.exists()) {
            if (containerFile.isDirectory()) {
              loadImplementationsInDirectory(test, packageName, new File(containerFile, packageName));
              continue;
            } 
            loadImplementationsInJar(test, packageName, containerFile);
            continue;
          } 
          String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
          URL newURL = new URL(url.getProtocol(), url.getHost(), path);
          try (InputStream is = newURL.openStream()) {
            JarInputStream jarStream;
            if (is instanceof JarInputStream) {
              jarStream = (JarInputStream)is;
            } else {
              jarStream = new JarInputStream(is);
            } 
            loadImplementationsInJar(test, packageName, path, jarStream);
          } 
          continue;
        } 
        if ("bundleresource".equals(url.getProtocol())) {
          loadImplementationsInBundle(test, packageName);
          continue;
        } 
        if ("jar".equals(url.getProtocol())) {
          loadImplementationsInJar(test, packageName, url);
          continue;
        } 
        File file = new File(urlPath);
        if (file.isDirectory()) {
          loadImplementationsInDirectory(test, packageName, file);
          continue;
        } 
        loadImplementationsInJar(test, packageName, file);
      } catch (IOException|URISyntaxException ioe) {
        LOGGER.warn("Could not read entries", ioe);
      } 
    } 
  }
  
  String extractPath(URL url) throws UnsupportedEncodingException, URISyntaxException {
    String urlPath = url.getPath();
    if (urlPath.startsWith("jar:"))
      urlPath = urlPath.substring(4); 
    if (urlPath.startsWith("file:"))
      urlPath = urlPath.substring(5); 
    int bangIndex = urlPath.indexOf('!');
    if (bangIndex > 0)
      urlPath = urlPath.substring(0, bangIndex); 
    String protocol = url.getProtocol();
    List<String> neverDecode = Arrays.asList(new String[] { "vfs", "vfszip", "bundleresource" });
    if (neverDecode.contains(protocol))
      return urlPath; 
    String cleanPath = (new URI(urlPath)).getPath();
    if ((new File(cleanPath)).exists())
      return cleanPath; 
    return URLDecoder.decode(urlPath, StandardCharsets.UTF_8.name());
  }
  
  private void loadImplementationsInBundle(Test test, String packageName) {
    BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
    Collection<String> list = wiring.listResources(packageName, "*.class", 1);
    for (String name : list)
      addIfMatching(test, name); 
  }
  
  private void loadImplementationsInDirectory(Test test, String parent, File location) {
    File[] files = location.listFiles();
    if (files == null)
      return; 
    for (File file : files) {
      StringBuilder builder = new StringBuilder();
      builder.append(parent).append('/').append(file.getName());
      String packageOrClass = (parent == null) ? file.getName() : builder.toString();
      if (file.isDirectory()) {
        loadImplementationsInDirectory(test, packageOrClass, file);
      } else if (isTestApplicable(test, file.getName())) {
        addIfMatching(test, packageOrClass);
      } 
    } 
  }
  
  private boolean isTestApplicable(Test test, String path) {
    return (test.doesMatchResource() || (path.endsWith(".class") && test.doesMatchClass()));
  }
  
  private void loadImplementationsInJar(Test test, String parent, URL url) {
    JarURLConnection connection = null;
    try {
      connection = (JarURLConnection)url.openConnection();
      if (connection != null) {
        try (JarFile jarFile = connection.getJarFile()) {
          Enumeration<JarEntry> entries = jarFile.entries();
          while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name))
              addIfMatching(test, name); 
          } 
        } 
      } else {
        LOGGER.error("Could not establish connection to {}", url.toString());
      } 
    } catch (IOException ex) {
      LOGGER.error("Could not search JAR file '{}' for classes matching criteria {}, file not found", url
          .toString(), test, ex);
    } 
  }
  
  private void loadImplementationsInJar(Test test, String parent, File jarFile) {
    JarInputStream jarStream = null;
    try {
      jarStream = new JarInputStream(new FileInputStream(jarFile));
      loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
    } catch (IOException ex) {
      LOGGER.error("Could not search JAR file '{}' for classes matching criteria {}, file not found", jarFile, test, ex);
    } finally {
      close(jarStream, jarFile);
    } 
  }
  
  private void close(JarInputStream jarStream, Object source) {
    if (jarStream != null)
      try {
        jarStream.close();
      } catch (IOException e) {
        LOGGER.error("Error closing JAR file stream for {}", source, e);
      }  
  }
  
  private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream) {
    try {
      JarEntry entry;
      while ((entry = stream.getNextJarEntry()) != null) {
        String name = entry.getName();
        if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name))
          addIfMatching(test, name); 
      } 
    } catch (IOException ioe) {
      LOGGER.error("Could not search JAR file '{}' for classes matching criteria {} due to an IOException", path, test, ioe);
    } 
  }
  
  protected void addIfMatching(Test test, String fqn) {
    try {
      ClassLoader loader = getClassLoader();
      if (test.doesMatchClass()) {
        String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
        if (LOGGER.isDebugEnabled())
          LOGGER.debug("Checking to see if class {} matches criteria {}", externalName, test); 
        Class<?> type = loader.loadClass(externalName);
        if (test.matches(type))
          this.classMatches.add(type); 
      } 
      if (test.doesMatchResource()) {
        URL url = loader.getResource(fqn);
        if (url == null)
          url = loader.getResource(fqn.substring(1)); 
        if (url != null && test.matches(url.toURI()))
          this.resourceMatches.add(url.toURI()); 
      } 
    } catch (Throwable t) {
      LOGGER.warn("Could not examine class {}", fqn, t);
    } 
  }
  
  public static interface Test {
    boolean matches(Class<?> param1Class);
    
    boolean matches(URI param1URI);
    
    boolean doesMatchClass();
    
    boolean doesMatchResource();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugin\\util\ResolverUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */