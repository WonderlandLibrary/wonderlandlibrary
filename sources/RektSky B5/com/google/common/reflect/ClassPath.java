/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.reflect.Reflection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class ClassPath {
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>(){

        @Override
        public boolean apply(ClassInfo info) {
            return info.className.indexOf(36) == -1;
        }
    };
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        Scanner scanner = new Scanner();
        for (Map.Entry entry : ClassPath.getClassPathEntries(classloader).entrySet()) {
            scanner.scan((URI)entry.getKey(), (ClassLoader)entry.getValue());
        }
        return new ClassPath(scanner.getResources());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
        Preconditions.checkNotNull(packageName);
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (ClassInfo classInfo : this.getTopLevelClasses()) {
            if (!classInfo.getPackageName().equals(packageName)) continue;
            builder.add(classInfo);
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
        Preconditions.checkNotNull(packageName);
        String packagePrefix = packageName + '.';
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (ClassInfo classInfo : this.getTopLevelClasses()) {
            if (!classInfo.getName().startsWith(packagePrefix)) continue;
            builder.add(classInfo);
        }
        return builder.build();
    }

    @VisibleForTesting
    static ImmutableMap<URI, ClassLoader> getClassPathEntries(ClassLoader classloader) {
        LinkedHashMap<URI, ClassLoader> entries = Maps.newLinkedHashMap();
        ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll(ClassPath.getClassPathEntries(parent));
        }
        if (classloader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader)classloader;
            for (URL entry : urlClassLoader.getURLs()) {
                URI uri;
                try {
                    uri = entry.toURI();
                }
                catch (URISyntaxException e2) {
                    throw new IllegalArgumentException(e2);
                }
                if (entries.containsKey(uri)) continue;
                entries.put(uri, classloader);
            }
        }
        return ImmutableMap.copyOf(entries);
    }

    @VisibleForTesting
    static String getClassName(String filename) {
        int classNameEnd = filename.length() - CLASS_FILE_NAME_EXTENSION.length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }

    @VisibleForTesting
    static final class Scanner {
        private final ImmutableSortedSet.Builder<ResourceInfo> resources = new ImmutableSortedSet.Builder<Object>(Ordering.usingToString());
        private final Set<URI> scannedUris = Sets.newHashSet();

        Scanner() {
        }

        ImmutableSortedSet<ResourceInfo> getResources() {
            return this.resources.build();
        }

        void scan(URI uri, ClassLoader classloader) throws IOException {
            if (uri.getScheme().equals("file") && this.scannedUris.add(uri)) {
                this.scanFrom(new File(uri), classloader);
            }
        }

        @VisibleForTesting
        void scanFrom(File file, ClassLoader classloader) throws IOException {
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                this.scanDirectory(file, classloader);
            } else {
                this.scanJar(file, classloader);
            }
        }

        private void scanDirectory(File directory, ClassLoader classloader) throws IOException {
            this.scanDirectory(directory, classloader, "", ImmutableSet.<File>of());
        }

        private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, ImmutableSet<File> ancestors) throws IOException {
            File canonical = directory.getCanonicalFile();
            if (ancestors.contains(canonical)) {
                return;
            }
            File[] files = directory.listFiles();
            if (files == null) {
                logger.warning("Cannot read directory " + directory);
                return;
            }
            ImmutableCollection newAncestors = ((ImmutableSet.Builder)((ImmutableSet.Builder)ImmutableSet.builder().addAll(ancestors)).add(canonical)).build();
            for (File f2 : files) {
                String name = f2.getName();
                if (f2.isDirectory()) {
                    this.scanDirectory(f2, classloader, packagePrefix + name + "/", (ImmutableSet<File>)newAncestors);
                    continue;
                }
                String resourceName = packagePrefix + name;
                if (resourceName.equals("META-INF/MANIFEST.MF")) continue;
                this.resources.add((Object)ResourceInfo.of(resourceName, classloader));
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void scanJar(File file, ClassLoader classloader) throws IOException {
            JarFile jarFile;
            try {
                jarFile = new JarFile(file);
            }
            catch (IOException e2) {
                return;
            }
            try {
                for (URI uri : Scanner.getClassPathFromManifest(file, jarFile.getManifest())) {
                    this.scan(uri, classloader);
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) continue;
                    this.resources.add((Object)ResourceInfo.of(entry.getName(), classloader));
                }
            }
            finally {
                try {
                    jarFile.close();
                }
                catch (IOException ignored) {}
            }
        }

        @VisibleForTesting
        static ImmutableSet<URI> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
            if (manifest == null) {
                return ImmutableSet.of();
            }
            ImmutableSet.Builder builder = ImmutableSet.builder();
            String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (classpathAttribute != null) {
                for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                    URI uri;
                    try {
                        uri = Scanner.getClassPathEntry(jarFile, path);
                    }
                    catch (URISyntaxException e2) {
                        logger.warning("Invalid Class-Path entry: " + path);
                        continue;
                    }
                    builder.add(uri);
                }
            }
            return builder.build();
        }

        @VisibleForTesting
        static URI getClassPathEntry(File jarFile, String path) throws URISyntaxException {
            URI uri = new URI(path);
            if (uri.isAbsolute()) {
                return uri;
            }
            return new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
        }
    }

    @Beta
    public static final class ClassInfo
    extends ResourceInfo {
        private final String className;

        ClassInfo(String resourceName, ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.DIGIT.trimLeadingFrom(innerClassName);
            }
            String packageName = this.getPackageName();
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
            catch (ClassNotFoundException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public String toString() {
            return this.className;
        }
    }

    @Beta
    public static class ResourceInfo {
        private final String resourceName;
        final ClassLoader loader;

        static ResourceInfo of(String resourceName, ClassLoader loader) {
            if (resourceName.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(resourceName, loader);
            }
            return new ResourceInfo(resourceName, loader);
        }

        ResourceInfo(String resourceName, ClassLoader loader) {
            this.resourceName = Preconditions.checkNotNull(resourceName);
            this.loader = Preconditions.checkNotNull(loader);
        }

        public final URL url() {
            return Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", this.resourceName);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof ResourceInfo) {
                ResourceInfo that = (ResourceInfo)obj;
                return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
            }
            return false;
        }

        public String toString() {
            return this.resourceName;
        }
    }
}

