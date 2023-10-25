/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.osgi.framework.FrameworkUtil
 *  org.osgi.framework.wiring.BundleWiring
 */
package org.apache.logging.log4j.core.config.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ResolverUtil {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String VFSZIP = "vfszip";
    private static final String BUNDLE_RESOURCE = "bundleresource";
    private final Set<Class<?>> classMatches = new HashSet();
    private final Set<URI> resourceMatches = new HashSet<URI>();
    private ClassLoader classloader;

    public Set<Class<?>> getClasses() {
        return this.classMatches;
    }

    public Set<URI> getResources() {
        return this.resourceMatches;
    }

    public ClassLoader getClassLoader() {
        return this.classloader != null ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public void findImplementations(Class<?> parent, String ... packageNames) {
        if (packageNames == null) {
            return;
        }
        IsA test = new IsA(parent);
        for (String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }

    public void findSuffix(String suffix, String ... packageNames) {
        if (packageNames == null) {
            return;
        }
        NameEndsWith test = new NameEndsWith(suffix);
        for (String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }

    public void findAnnotated(Class<? extends Annotation> annotation, String ... packageNames) {
        if (packageNames == null) {
            return;
        }
        AnnotatedWith test = new AnnotatedWith(annotation);
        for (String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }

    public void findNamedResource(String name, String ... pathNames) {
        if (pathNames == null) {
            return;
        }
        NameIs test = new NameIs(name);
        for (String pkg : pathNames) {
            this.findInPackage(test, pkg);
        }
    }

    public void find(Test test, String ... packageNames) {
        if (packageNames == null) {
            return;
        }
        for (String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void findInPackage(Test test, String packageName) {
        Enumeration<URL> urls;
        packageName = packageName.replace('.', '/');
        ClassLoader loader = this.getClassLoader();
        try {
            urls = loader.getResources(packageName);
        }
        catch (IOException ioe) {
            LOGGER.warn("Could not read package: " + packageName, (Throwable)ioe);
            return;
        }
        while (urls.hasMoreElements()) {
            try {
                URL url = urls.nextElement();
                String urlPath = url.getFile();
                urlPath = URLDecoder.decode(urlPath, Charsets.UTF_8.name());
                if (urlPath.startsWith("file:")) {
                    urlPath = urlPath.substring(5);
                }
                if (urlPath.indexOf(33) > 0) {
                    urlPath = urlPath.substring(0, urlPath.indexOf(33));
                }
                LOGGER.info("Scanning for classes in [" + urlPath + "] matching criteria: " + test);
                if (VFSZIP.equals(url.getProtocol())) {
                    String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
                    URL newURL = new URL(url.getProtocol(), url.getHost(), path);
                    JarInputStream stream = new JarInputStream(newURL.openStream());
                    try {
                        this.loadImplementationsInJar(test, packageName, path, stream);
                        continue;
                    }
                    finally {
                        this.close(stream, newURL);
                        continue;
                    }
                }
                if (BUNDLE_RESOURCE.equals(url.getProtocol())) {
                    this.loadImplementationsInBundle(test, packageName);
                    continue;
                }
                File file = new File(urlPath);
                if (file.isDirectory()) {
                    this.loadImplementationsInDirectory(test, packageName, file);
                    continue;
                }
                this.loadImplementationsInJar(test, packageName, file);
            }
            catch (IOException ioe) {
                LOGGER.warn("could not read entries", (Throwable)ioe);
            }
        }
    }

    private void loadImplementationsInBundle(Test test, String packageName) {
        BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
        Collection list = wiring.listResources(packageName, "*.class", 1);
        for (String name : list) {
            this.addIfMatching(test, name);
        }
    }

    private void loadImplementationsInDirectory(Test test, String parent, File location) {
        File[] files = location.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String packageOrClass;
            StringBuilder builder = new StringBuilder();
            builder.append(parent).append("/").append(file.getName());
            String string = packageOrClass = parent == null ? file.getName() : builder.toString();
            if (file.isDirectory()) {
                this.loadImplementationsInDirectory(test, packageOrClass, file);
                continue;
            }
            if (!this.isTestApplicable(test, file.getName())) continue;
            this.addIfMatching(test, packageOrClass);
        }
    }

    private boolean isTestApplicable(Test test, String path) {
        return test.doesMatchResource() || path.endsWith(".class") && test.doesMatchClass();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void loadImplementationsInJar(Test test, String parent, File jarFile) {
        JarInputStream jarStream = null;
        try {
            jarStream = new JarInputStream(new FileInputStream(jarFile));
            this.loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
            this.close(jarStream, jarFile);
            return;
        }
        catch (FileNotFoundException ex) {
            LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " file not found");
            this.close(jarStream, jarFile);
            return;
            {
                catch (Throwable throwable) {
                    this.close(jarStream, jarFile);
                    throw throwable;
                }
            }
            catch (IOException ioe) {
                LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " due to an IOException", (Throwable)ioe);
                this.close(jarStream, jarFile);
                return;
            }
        }
    }

    private void close(JarInputStream jarStream, Object source) {
        if (jarStream != null) {
            try {
                jarStream.close();
            }
            catch (IOException e) {
                LOGGER.error("Error closing JAR file stream for {}", source, e);
            }
        }
    }

    private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream) {
        try {
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory() || !name.startsWith(parent) || !this.isTestApplicable(test, name)) continue;
                this.addIfMatching(test, name);
            }
        }
        catch (IOException ioe) {
            LOGGER.error("Could not search jar file '" + path + "' for classes matching criteria: " + test + " due to an IOException", (Throwable)ioe);
        }
    }

    protected void addIfMatching(Test test, String fqn) {
        try {
            ClassLoader loader = this.getClassLoader();
            if (test.doesMatchClass()) {
                Class<?> type;
                String externalName = fqn.substring(0, fqn.indexOf(46)).replace('/', '.');
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");
                }
                if (test.matches(type = loader.loadClass(externalName))) {
                    this.classMatches.add(type);
                }
            }
            if (test.doesMatchResource()) {
                URL url = loader.getResource(fqn);
                if (url == null) {
                    url = loader.getResource(fqn.substring(1));
                }
                if (url != null && test.matches(url.toURI())) {
                    this.resourceMatches.add(url.toURI());
                }
            }
        }
        catch (Throwable t) {
            LOGGER.warn("Could not examine class '" + fqn + "' due to a " + t.getClass().getName() + " with message: " + t.getMessage());
        }
    }

    public static class NameIs
    extends ResourceTest {
        private final String name;

        public NameIs(String name) {
            this.name = "/" + name;
        }

        @Override
        public boolean matches(URI resource) {
            return resource.getPath().endsWith(this.name);
        }

        public String toString() {
            return "named " + this.name;
        }
    }

    public static class AnnotatedWith
    extends ClassTest {
        private final Class<? extends Annotation> annotation;

        public AnnotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(this.annotation);
        }

        public String toString() {
            return "annotated with @" + this.annotation.getSimpleName();
        }
    }

    public static class NameEndsWith
    extends ClassTest {
        private final String suffix;

        public NameEndsWith(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean matches(Class<?> type) {
            return type != null && type.getName().endsWith(this.suffix);
        }

        public String toString() {
            return "ends with the suffix " + this.suffix;
        }
    }

    public static class IsA
    extends ClassTest {
        private final Class<?> parent;

        public IsA(Class<?> parentType) {
            this.parent = parentType;
        }

        @Override
        public boolean matches(Class<?> type) {
            return type != null && this.parent.isAssignableFrom(type);
        }

        public String toString() {
            return "is assignable to " + this.parent.getSimpleName();
        }
    }

    public static abstract class ResourceTest
    implements Test {
        @Override
        public boolean matches(Class<?> cls) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean doesMatchClass() {
            return false;
        }

        @Override
        public boolean doesMatchResource() {
            return true;
        }
    }

    public static abstract class ClassTest
    implements Test {
        @Override
        public boolean matches(URI resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean doesMatchClass() {
            return true;
        }

        @Override
        public boolean doesMatchResource() {
            return false;
        }
    }

    public static interface Test {
        public boolean matches(Class<?> var1);

        public boolean matches(URI var1);

        public boolean doesMatchClass();

        public boolean doesMatchResource();
    }
}

