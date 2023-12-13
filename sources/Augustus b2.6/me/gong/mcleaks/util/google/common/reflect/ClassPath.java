// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.reflect;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import me.gong.mcleaks.util.google.common.collect.MultimapBuilder;
import me.gong.mcleaks.util.google.common.collect.SetMultimap;
import java.util.LinkedHashMap;
import java.net.URLClassLoader;
import me.gong.mcleaks.util.google.common.collect.Maps;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.net.MalformedURLException;
import java.util.jar.Attributes;
import javax.annotation.Nullable;
import java.util.jar.Manifest;
import java.util.jar.JarFile;
import java.util.Map;
import me.gong.mcleaks.util.google.common.collect.Sets;
import java.io.File;
import java.util.Set;
import me.gong.mcleaks.util.google.common.base.CharMatcher;
import me.gong.mcleaks.util.google.common.io.CharSource;
import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.common.io.Resources;
import me.gong.mcleaks.util.google.common.io.ByteSource;
import java.util.NoSuchElementException;
import java.net.URL;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.collect.FluentIterable;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.collect.ImmutableSet;
import me.gong.mcleaks.util.google.common.base.Splitter;
import me.gong.mcleaks.util.google.common.base.Predicate;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class ClassPath
{
    private static final Logger logger;
    private static final Predicate<ClassInfo> IS_TOP_LEVEL;
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR;
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;
    
    private ClassPath(final ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }
    
    public static ClassPath from(final ClassLoader classloader) throws IOException {
        final DefaultScanner scanner = new DefaultScanner();
        scanner.scan(classloader);
        return new ClassPath(scanner.getResources());
    }
    
    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }
    
    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(ClassPath.IS_TOP_LEVEL).toSet();
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
        final String packagePrefix = packageName + '.';
        final ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        for (final ClassInfo classInfo : this.getTopLevelClasses()) {
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }
    
    @VisibleForTesting
    static String getClassName(final String filename) {
        final int classNameEnd = filename.length() - ".class".length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
    
    static {
        logger = Logger.getLogger(ClassPath.class.getName());
        IS_TOP_LEVEL = new Predicate<ClassInfo>() {
            @Override
            public boolean apply(final ClassInfo info) {
                return info.className.indexOf(36) == -1;
            }
        };
        CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    }
    
    @Beta
    public static class ResourceInfo
    {
        private final String resourceName;
        final ClassLoader loader;
        
        static ResourceInfo of(final String resourceName, final ClassLoader loader) {
            if (resourceName.endsWith(".class")) {
                return new ClassInfo(resourceName, loader);
            }
            return new ResourceInfo(resourceName, loader);
        }
        
        ResourceInfo(final String resourceName, final ClassLoader loader) {
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
        
        @Override
        public int hashCode() {
            return this.resourceName.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
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
        
        ClassInfo(final String resourceName, final ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }
        
        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }
        
        public String getSimpleName() {
            final int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                final String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.digit().trimLeadingFrom(innerClassName);
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
    
    abstract static class Scanner
    {
        private final Set<File> scannedUris;
        
        Scanner() {
            this.scannedUris = (Set<File>)Sets.newHashSet();
        }
        
        public final void scan(final ClassLoader classloader) throws IOException {
            for (final Map.Entry<File, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
                this.scan(entry.getKey(), entry.getValue());
            }
        }
        
        protected abstract void scanDirectory(final ClassLoader p0, final File p1) throws IOException;
        
        protected abstract void scanJarFile(final ClassLoader p0, final JarFile p1) throws IOException;
        
        @VisibleForTesting
        final void scan(final File file, final ClassLoader classloader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                this.scanFrom(file, classloader);
            }
        }
        
        private void scanFrom(final File file, final ClassLoader classloader) throws IOException {
            try {
                if (!file.exists()) {
                    return;
                }
            }
            catch (SecurityException e) {
                ClassPath.logger.warning("Cannot access " + file + ": " + e);
                return;
            }
            if (file.isDirectory()) {
                this.scanDirectory(classloader, file);
            }
            else {
                this.scanJar(file, classloader);
            }
        }
        
        private void scanJar(final File file, final ClassLoader classloader) throws IOException {
            JarFile jarFile;
            try {
                jarFile = new JarFile(file);
            }
            catch (IOException e) {
                return;
            }
            try {
                for (final File path : getClassPathFromManifest(file, jarFile.getManifest())) {
                    this.scan(path, classloader);
                }
                this.scanJarFile(classloader, jarFile);
            }
            finally {
                try {
                    jarFile.close();
                }
                catch (IOException ex) {}
            }
        }
        
        @VisibleForTesting
        static ImmutableSet<File> getClassPathFromManifest(final File jarFile, @Nullable final Manifest manifest) {
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
                        ClassPath.logger.warning("Invalid Class-Path entry: " + path);
                        continue;
                    }
                    if (url.getProtocol().equals("file")) {
                        builder.add(new File(url.getFile()));
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
            if (classloader instanceof URLClassLoader) {
                final URLClassLoader urlClassLoader = (URLClassLoader)classloader;
                for (final URL entry : urlClassLoader.getURLs()) {
                    if (entry.getProtocol().equals("file")) {
                        final File file = new File(entry.getFile());
                        if (!entries.containsKey(file)) {
                            entries.put(file, classloader);
                        }
                    }
                }
            }
            return ImmutableMap.copyOf((Map<? extends File, ? extends ClassLoader>)entries);
        }
        
        @VisibleForTesting
        static URL getClassPathEntry(final File jarFile, final String path) throws MalformedURLException {
            return new URL(jarFile.toURI().toURL(), path);
        }
    }
    
    @VisibleForTesting
    static final class DefaultScanner extends Scanner
    {
        private final SetMultimap<ClassLoader, String> resources;
        
        DefaultScanner() {
            this.resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();
        }
        
        ImmutableSet<ResourceInfo> getResources() {
            final ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
            for (final Map.Entry<ClassLoader, String> entry : this.resources.entries()) {
                builder.add(ResourceInfo.of(entry.getValue(), entry.getKey()));
            }
            return builder.build();
        }
        
        @Override
        protected void scanJarFile(final ClassLoader classloader, final JarFile file) {
            final Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                        continue;
                    }
                    this.resources.get(classloader).add(entry.getName());
                }
            }
        }
        
        @Override
        protected void scanDirectory(final ClassLoader classloader, final File directory) throws IOException {
            this.scanDirectory(directory, classloader, "");
        }
        
        private void scanDirectory(final File directory, final ClassLoader classloader, final String packagePrefix) throws IOException {
            final File[] files = directory.listFiles();
            if (files == null) {
                ClassPath.logger.warning("Cannot read directory " + directory);
                return;
            }
            for (final File f : files) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    this.scanDirectory(f, classloader, packagePrefix + name + "/");
                }
                else {
                    final String resourceName = packagePrefix + name;
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        this.resources.get(classloader).add(resourceName);
                    }
                }
            }
        }
    }
}
