/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.adapters.JavassistAdapter;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.AbstractScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.FilterBuilder;

public class ConfigurationBuilder
implements Configuration {
    private Set<Scanner> scanners = new HashSet<AbstractScanner>(Arrays.asList(new TypeAnnotationsScanner(), new SubTypesScanner()));
    private Set<URL> urls = new HashSet<URL>();
    protected MetadataAdapter metadataAdapter;
    private Predicate<String> inputsFilter;
    private Serializer serializer;
    private ExecutorService executorService;
    private ClassLoader[] classLoaders;
    private boolean expandSuperTypes = true;

    /*
     * WARNING - void declaration
     */
    public static ConfigurationBuilder build(Object ... params) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        ArrayList<Object> parameters = new ArrayList<Object>();
        if (params != null) {
            void var5_7;
            Object[] objectArray = params;
            int n2 = objectArray.length;
            boolean bl = false;
            while (var5_7 < n2) {
                Object param = objectArray[var5_7];
                if (param != null) {
                    if (param.getClass().isArray()) {
                        for (Object p2 : (Object[])param) {
                            if (p2 == null) continue;
                            parameters.add(p2);
                        }
                    } else if (param instanceof Iterable) {
                        for (Object e2 : (Iterable)param) {
                            if (e2 == null) continue;
                            parameters.add(e2);
                        }
                    } else {
                        parameters.add(param);
                    }
                }
                ++var5_7;
            }
        }
        ArrayList<ClassLoader> loaders = new ArrayList<ClassLoader>();
        for (Object e3 : parameters) {
            if (!(e3 instanceof ClassLoader)) continue;
            loaders.add((ClassLoader)e3);
        }
        ClassLoader[] classLoaders = loaders.isEmpty() ? null : loaders.toArray(new ClassLoader[loaders.size()]);
        FilterBuilder filterBuilder = new FilterBuilder();
        ArrayList<Scanner> scanners = new ArrayList<Scanner>();
        for (Object e4 : parameters) {
            if (e4 instanceof String) {
                builder.addUrls(ClasspathHelper.forPackage((String)e4, classLoaders));
                filterBuilder.includePackage((String)e4);
                continue;
            }
            if (e4 instanceof Class) {
                if (Scanner.class.isAssignableFrom((Class)e4)) {
                    try {
                        builder.addScanners((Scanner)((Class)e4).newInstance());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                builder.addUrls(ClasspathHelper.forClass((Class)e4, classLoaders));
                filterBuilder.includePackage((Class)e4);
                continue;
            }
            if (e4 instanceof Scanner) {
                scanners.add((Scanner)e4);
                continue;
            }
            if (e4 instanceof URL) {
                builder.addUrls((URL)e4);
                continue;
            }
            if (e4 instanceof ClassLoader) continue;
            if (e4 instanceof Predicate) {
                filterBuilder.add((Predicate)e4);
                continue;
            }
            if (e4 instanceof ExecutorService) {
                builder.setExecutorService((ExecutorService)e4);
                continue;
            }
            if (Reflections.log == null) continue;
            throw new ReflectionsException("could not use param " + e4);
        }
        if (builder.getUrls().isEmpty()) {
            if (classLoaders != null) {
                builder.addUrls(ClasspathHelper.forClassLoader(classLoaders));
            } else {
                builder.addUrls(ClasspathHelper.forClassLoader());
            }
        }
        builder.filterInputsBy(filterBuilder);
        if (!scanners.isEmpty()) {
            builder.setScanners(scanners.toArray(new Scanner[scanners.size()]));
        }
        if (!loaders.isEmpty()) {
            builder.addClassLoaders(loaders);
        }
        return builder;
    }

    public ConfigurationBuilder forPackages(String ... packages) {
        for (String pkg : packages) {
            this.addUrls(ClasspathHelper.forPackage(pkg, new ClassLoader[0]));
        }
        return this;
    }

    @Override
    public Set<Scanner> getScanners() {
        return this.scanners;
    }

    public ConfigurationBuilder setScanners(Scanner ... scanners) {
        this.scanners.clear();
        return this.addScanners(scanners);
    }

    public ConfigurationBuilder addScanners(Scanner ... scanners) {
        this.scanners.addAll(Arrays.asList(scanners));
        return this;
    }

    @Override
    public Set<URL> getUrls() {
        return this.urls;
    }

    public ConfigurationBuilder setUrls(Collection<URL> urls) {
        this.urls = new HashSet<URL>(urls);
        return this;
    }

    public ConfigurationBuilder setUrls(URL ... urls) {
        this.urls = new HashSet<URL>(Arrays.asList(urls));
        return this;
    }

    public ConfigurationBuilder addUrls(Collection<URL> urls) {
        this.urls.addAll(urls);
        return this;
    }

    public ConfigurationBuilder addUrls(URL ... urls) {
        this.urls.addAll(new HashSet<URL>(Arrays.asList(urls)));
        return this;
    }

    @Override
    public MetadataAdapter getMetadataAdapter() {
        if (this.metadataAdapter != null) {
            return this.metadataAdapter;
        }
        try {
            this.metadataAdapter = new JavassistAdapter();
            return this.metadataAdapter;
        }
        catch (Throwable e2) {
            if (Reflections.log != null) {
                Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", e2);
            }
            this.metadataAdapter = new JavaReflectionAdapter();
            return this.metadataAdapter;
        }
    }

    public ConfigurationBuilder setMetadataAdapter(MetadataAdapter metadataAdapter) {
        this.metadataAdapter = metadataAdapter;
        return this;
    }

    @Override
    public Predicate<String> getInputsFilter() {
        return this.inputsFilter;
    }

    public void setInputsFilter(Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
    }

    public ConfigurationBuilder filterInputsBy(Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
        return this;
    }

    @Override
    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public ConfigurationBuilder setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public ConfigurationBuilder useParallelExecutor() {
        return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
    }

    public ConfigurationBuilder useParallelExecutor(int availableProcessors) {
        ThreadFactory threadFactory = new ThreadFactory(){
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r2) {
                Thread t2 = new Thread(r2);
                t2.setName("org.reflections-scanner-" + this.threadNumber.getAndIncrement());
                t2.setDaemon(true);
                return t2;
            }
        };
        this.setExecutorService(Executors.newFixedThreadPool(availableProcessors, threadFactory));
        return this;
    }

    @Override
    public Serializer getSerializer() {
        return this.serializer != null ? this.serializer : (this.serializer = new XmlSerializer());
    }

    public ConfigurationBuilder setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    @Override
    public ClassLoader[] getClassLoaders() {
        return this.classLoaders;
    }

    @Override
    public boolean shouldExpandSuperTypes() {
        return this.expandSuperTypes;
    }

    public ConfigurationBuilder setExpandSuperTypes(boolean expandSuperTypes) {
        this.expandSuperTypes = expandSuperTypes;
        return this;
    }

    public void setClassLoaders(ClassLoader[] classLoaders) {
        this.classLoaders = classLoaders;
    }

    public ConfigurationBuilder addClassLoader(ClassLoader classLoader) {
        return this.addClassLoaders(classLoader);
    }

    public ConfigurationBuilder addClassLoaders(ClassLoader ... classLoaders) {
        this.classLoaders = this.classLoaders == null ? classLoaders : (ClassLoader[])Stream.concat(Stream.concat(Arrays.stream(this.classLoaders), Arrays.stream(classLoaders)), Stream.of(ClassLoader.class)).distinct().toArray(ClassLoader[]::new);
        return this;
    }

    public ConfigurationBuilder addClassLoaders(Collection<ClassLoader> classLoaders) {
        return this.addClassLoaders(classLoaders.toArray(new ClassLoader[classLoaders.size()]));
    }
}

