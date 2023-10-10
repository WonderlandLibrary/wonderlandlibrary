// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.google.common.base.CharMatcher;
import com.google.common.io.CharSource;
import java.nio.charset.Charset;
import com.google.common.io.Resources;
import com.google.common.io.ByteSource;
import java.util.NoSuchElementException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import com.google.common.base.StandardSystemProperty;
import java.net.URLClassLoader;
import com.google.common.collect.ImmutableList;
import java.util.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.annotations.VisibleForTesting;
import java.net.URL;
import java.util.Iterator;
import java.net.MalformedURLException;
import java.util.jar.Attributes;
import javax.annotation.CheckForNull;
import java.util.jar.Manifest;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.io.IOException;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import java.io.File;
import java.util.HashSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Splitter;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public final class ClassPath
{
    private static final Logger logger;
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR;
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;
    
    private ClassPath(final ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }
    
    public static ClassPath from(final ClassLoader classloader) throws IOException {
        final ImmutableSet<LocationInfo> locations = locationsFrom(classloader);
        final Set<File> scanned = new HashSet<File>();
        for (final LocationInfo location : locations) {
            scanned.add(location.file());
        }
        final ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
        for (final LocationInfo location2 : locations) {
            builder.addAll(location2.scanResources(scanned));
        }
        return new ClassPath(builder.build());
    }
    
    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }
    
    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(new Predicate<ClassInfo>(this) {
            @Override
            public boolean apply(final ClassInfo info) {
                return info.isTopLevel();
            }
        }).toSet();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClasses(final String packageName) {
        Preconditions.checkNotNull(packageName);
        final ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        for (final ClassInfo classInfo : this.getTopLevelClasses()) {
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(final String packageName) {
        Preconditions.checkNotNull(packageName);
        final String packagePrefix = new StringBuilder(1 + String.valueOf(packageName).length()).append(packageName).append('.').toString();
        final ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        for (final ClassInfo classInfo : this.getTopLevelClasses()) {
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }
    
    static ImmutableSet<LocationInfo> locationsFrom(final ClassLoader classloader) {
        final ImmutableSet.Builder<LocationInfo> builder = ImmutableSet.builder();
        for (final Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
            builder.add(new LocationInfo(entry.getKey(), entry.getValue()));
        }
        return builder.build();
    }
    
    @VisibleForTesting
    static ImmutableSet<File> getClassPathFromManifest(final File jarFile, @CheckForNull final Manifest manifest) {
        if (manifest == null) {
            return ImmutableSet.of();
        }
        final ImmutableSet.Builder<File> builder = ImmutableSet.builder();
        final String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute != null) {
            for (final String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                URL url;
                try {
                    url = getClassPathEntry(jarFile, path);
                }
                catch (MalformedURLException e) {
                    final Logger logger = ClassPath.logger;
                    final String original = "Invalid Class-Path entry: ";
                    final String value = String.valueOf(path);
                    logger.warning((value.length() != 0) ? original.concat(value) : new String(original));
                    continue;
                }
                if (url.getProtocol().equals("file")) {
                    builder.add(toFile(url));
                }
            }
        }
        return builder.build();
    }
    
    @VisibleForTesting
    static ImmutableMap<File, ClassLoader> getClassPathEntries(final ClassLoader classloader) {
        final LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
        final ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll((Map<?, ?>)getClassPathEntries(parent));
        }
        for (final URL url : getClassLoaderUrls(classloader)) {
            if (url.getProtocol().equals("file")) {
                final File file = toFile(url);
                if (entries.containsKey(file)) {
                    continue;
                }
                entries.put(file, classloader);
            }
        }
        return ImmutableMap.copyOf((Map<? extends File, ? extends ClassLoader>)entries);
    }
    
    private static ImmutableList<URL> getClassLoaderUrls(final ClassLoader classloader) {
        if (classloader instanceof URLClassLoader) {
            return ImmutableList.copyOf(((URLClassLoader)classloader).getURLs());
        }
        if (classloader.equals(ClassLoader.getSystemClassLoader())) {
            return parseJavaClassPath();
        }
        return ImmutableList.of();
    }
    
    @VisibleForTesting
    static ImmutableList<URL> parseJavaClassPath() {
        final ImmutableList.Builder<URL> urls = ImmutableList.builder();
        for (final String entry : Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())) {
            try {
                try {
                    urls.add(new File(entry).toURI().toURL());
                }
                catch (SecurityException e2) {
                    urls.add(new URL("file", null, new File(entry).getAbsolutePath()));
                }
            }
            catch (MalformedURLException e) {
                final Logger logger = ClassPath.logger;
                final Level warning = Level.WARNING;
                final String original = "malformed classpath entry: ";
                final String value = String.valueOf(entry);
                logger.log(warning, (value.length() != 0) ? original.concat(value) : new String(original), e);
            }
        }
        return urls.build();
    }
    
    @VisibleForTesting
    static URL getClassPathEntry(final File jarFile, final String path) throws MalformedURLException {
        return new URL(jarFile.toURI().toURL(), path);
    }
    
    @VisibleForTesting
    static String getClassName(final String filename) {
        final int classNameEnd = filename.length() - ".class".length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
    
    @VisibleForTesting
    static File toFile(final URL url) {
        Preconditions.checkArgument(url.getProtocol().equals("file"));
        try {
            return new File(url.toURI());
        }
        catch (URISyntaxException e) {
            return new File(url.getPath());
        }
    }
    
    static {
        logger = Logger.getLogger(ClassPath.class.getName());
        CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    }
    
    @Beta
    public static class ResourceInfo
    {
        private final File file;
        private final String resourceName;
        final ClassLoader loader;
        
        static ResourceInfo of(final File file, final String resourceName, final ClassLoader loader) {
            if (resourceName.endsWith(".class")) {
                return new ClassInfo(file, resourceName, loader);
            }
            return new ResourceInfo(file, resourceName, loader);
        }
        
        ResourceInfo(final File file, final String resourceName, final ClassLoader loader) {
            this.file = Preconditions.checkNotNull(file);
            this.resourceName = Preconditions.checkNotNull(resourceName);
            this.loader = Preconditions.checkNotNull(loader);
        }
        
        public final URL url() {
            final URL url = this.loader.getResource(this.resourceName);
            if (url == null) {
                throw new NoSuchElementException(this.resourceName);
            }
            return url;
        }
        
        public final ByteSource asByteSource() {
            return Resources.asByteSource(this.url());
        }
        
        public final CharSource asCharSource(final Charset charset) {
            return Resources.asCharSource(this.url(), charset);
        }
        
        public final String getResourceName() {
            return this.resourceName;
        }
        
        final File getFile() {
            return this.file;
        }
        
        @Override
        public int hashCode() {
            return this.resourceName.hashCode();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof ResourceInfo) {
                final ResourceInfo that = (ResourceInfo)obj;
                return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return this.resourceName;
        }
    }
    
    @Beta
    public static final class ClassInfo extends ResourceInfo
    {
        private final String className;
        
        ClassInfo(final File file, final String resourceName, final ClassLoader loader) {
            super(file, resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }
        
        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }
        
        public String getSimpleName() {
            final int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                final String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.inRange('0', '9').trimLeadingFrom(innerClassName);
            }
            final String packageName = this.getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }
        
        public String getName() {
            return this.className;
        }
        
        public boolean isTopLevel() {
            return this.className.indexOf(36) == -1;
        }
        
        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            }
            catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String toString() {
            return this.className;
        }
    }
    
    static final class LocationInfo
    {
        final File home;
        private final ClassLoader classloader;
        
        LocationInfo(final File home, final ClassLoader classloader) {
            this.home = Preconditions.checkNotNull(home);
            this.classloader = Preconditions.checkNotNull(classloader);
        }
        
        public final File file() {
            return this.home;
        }
        
        public ImmutableSet<ResourceInfo> scanResources() throws IOException {
            return this.scanResources(new HashSet<File>());
        }
        
        public ImmutableSet<ResourceInfo> scanResources(final Set<File> scannedFiles) throws IOException {
            final ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
            scannedFiles.add(this.home);
            this.scan(this.home, scannedFiles, builder);
            return builder.build();
        }
        
        private void scan(final File file, final Set<File> scannedUris, final ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            try {
                if (!file.exists()) {
                    return;
                }
            }
            catch (SecurityException e) {
                final Logger access$000 = ClassPath.logger;
                final String value = String.valueOf(file);
                final String value2 = String.valueOf(e);
                access$000.warning(new StringBuilder(16 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Cannot access ").append(value).append(": ").append(value2).toString());
                return;
            }
            if (file.isDirectory()) {
                this.scanDirectory(file, builder);
            }
            else {
                this.scanJar(file, scannedUris, builder);
            }
        }
        
        private void scanJar(final File file, final Set<File> scannedUris, final ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            JarFile jarFile;
            try {
                jarFile = new JarFile(file);
            }
            catch (IOException e) {
                return;
            }
            try {
                for (final File path : ClassPath.getClassPathFromManifest(file, jarFile.getManifest())) {
                    if (scannedUris.add(path.getCanonicalFile())) {
                        this.scan(path, scannedUris, builder);
                    }
                }
                this.scanJarFile(jarFile, builder);
            }
            finally {
                try {
                    jarFile.close();
                }
                catch (IOException ex) {}
            }
        }
        
        private void scanJarFile(final JarFile file, final ImmutableSet.Builder<ResourceInfo> builder) {
            final Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                        continue;
                    }
                    builder.add(ResourceInfo.of(new File(file.getName()), entry.getName(), this.classloader));
                }
            }
        }
        
        private void scanDirectory(final File directory, final ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            final Set<File> currentPath = new HashSet<File>();
            currentPath.add(directory.getCanonicalFile());
            this.scanDirectory(directory, "", currentPath, builder);
        }
        
        private void scanDirectory(final File directory, final String packagePrefix, final Set<File> currentPath, final ImmutableSet.Builder<ResourceInfo> builder) throws IOException {
            final File[] files = directory.listFiles();
            if (files == null) {
                final Logger access$000 = ClassPath.logger;
                final String value = String.valueOf(directory);
                access$000.warning(new StringBuilder(22 + String.valueOf(value).length()).append("Cannot read directory ").append(value).toString());
                return;
            }
            for (final File f : files) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    final File deref = f.getCanonicalFile();
                    if (currentPath.add(deref)) {
                        this.scanDirectory(deref, new StringBuilder(1 + String.valueOf(packagePrefix).length() + String.valueOf(name).length()).append(packagePrefix).append(name).append("/").toString(), currentPath, builder);
                        currentPath.remove(deref);
                    }
                }
                else {
                    final String value2 = String.valueOf(packagePrefix);
                    final String value3 = String.valueOf(name);
                    final String resourceName = (value3.length() != 0) ? value2.concat(value3) : new String(value2);
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        builder.add(ResourceInfo.of(f, resourceName, this.classloader));
                    }
                }
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof LocationInfo) {
                final LocationInfo that = (LocationInfo)obj;
                return this.home.equals(that.home) && this.classloader.equals(that.classloader);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.home.hashCode();
        }
        
        @Override
        public String toString() {
            return this.home.toString();
        }
    }
}
