/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 */
package org.reflections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.reflections.Configuration;
import org.reflections.ReflectionUtils;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;
import org.slf4j.Logger;

public class Reflections {
    public static Logger log = Utils.findLogger(Reflections.class);
    protected final transient Configuration configuration;
    protected Store store;

    public Reflections(Configuration configuration) {
        this.configuration = configuration;
        this.store = new Store();
        if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
            for (Scanner scanner : configuration.getScanners()) {
                scanner.setConfiguration(configuration);
            }
            this.scan();
            if (configuration.shouldExpandSuperTypes()) {
                this.expandSuperTypes();
            }
        }
    }

    public Reflections(String prefix, Scanner ... scanners) {
        this(new Object[]{prefix, scanners});
    }

    public Reflections(Object ... params) {
        this(ConfigurationBuilder.build(params));
    }

    protected Reflections() {
        this.configuration = new ConfigurationBuilder();
        this.store = new Store();
    }

    protected void scan() {
        if (this.configuration.getUrls() == null || this.configuration.getUrls().isEmpty()) {
            if (log != null) {
                log.warn("given scan urls are empty. set urls in the configuration");
            }
            return;
        }
        if (log != null && log.isDebugEnabled()) {
            log.debug("going to scan these urls: {}", this.configuration.getUrls());
        }
        long time = System.currentTimeMillis();
        int scannedUrls = 0;
        ExecutorService executorService = this.configuration.getExecutorService();
        ArrayList futures = new ArrayList();
        for (URL uRL : this.configuration.getUrls()) {
            try {
                if (executorService != null) {
                    futures.add(executorService.submit(() -> {
                        if (log != null) {
                            log.debug("[{}] scanning {}", (Object)Thread.currentThread().toString(), (Object)url);
                        }
                        this.scan(url);
                    }));
                } else {
                    this.scan(uRL);
                }
                ++scannedUrls;
            }
            catch (ReflectionsException e2) {
                if (log == null) continue;
                log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", (Throwable)e2);
            }
        }
        if (executorService != null) {
            for (Future future : futures) {
                try {
                    future.get();
                }
                catch (Exception e3) {
                    throw new RuntimeException(e3);
                }
            }
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        if (log != null) {
            log.info(String.format("Reflections took %d ms to scan %d urls, producing %s %s", System.currentTimeMillis() - time, scannedUrls, Reflections.producingDescription(this.store), executorService instanceof ThreadPoolExecutor ? String.format("[using %d cores]", ((ThreadPoolExecutor)executorService).getMaximumPoolSize()) : ""));
        }
    }

    private static String producingDescription(Store store) {
        int keys = 0;
        int values = 0;
        for (String index : store.keySet()) {
            keys += store.keys(index).size();
            values += store.values(index).size();
        }
        return String.format("%d keys and %d values", keys, values);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void scan(URL url) {
        try (Vfs.Dir dir = Vfs.fromURL(url);){
            for (Vfs.File file : dir.getFiles()) {
                Predicate<String> inputsFilter = this.configuration.getInputsFilter();
                String path = file.getRelativePath();
                String fqn = path.replace('/', '.');
                if (inputsFilter != null && !inputsFilter.test(path) && !inputsFilter.test(fqn)) continue;
                Object classObject = null;
                for (Scanner scanner : this.configuration.getScanners()) {
                    try {
                        if (!scanner.acceptsInput(path) && !scanner.acceptsInput(fqn)) continue;
                        classObject = scanner.scan(file, classObject, this.store);
                    }
                    catch (Exception e2) {
                        if (log == null) continue;
                        log.debug("could not scan file {} in url {} with scanner {}", new Object[]{file.getRelativePath(), url.toExternalForm(), scanner.getClass().getSimpleName(), e2});
                    }
                }
            }
        }
    }

    public static Reflections collect() {
        return Reflections.collect("META-INF/reflections/", new FilterBuilder().include(".*-reflections.xml"), new Serializer[0]);
    }

    public static Reflections collect(String packagePrefix, Predicate<String> resourceNameFilter, Serializer ... optionalSerializer) {
        XmlSerializer serializer = optionalSerializer != null && optionalSerializer.length == 1 ? optionalSerializer[0] : new XmlSerializer();
        Collection<URL> urls = ClasspathHelper.forPackage(packagePrefix, new ClassLoader[0]);
        if (urls.isEmpty()) {
            return null;
        }
        long start = System.currentTimeMillis();
        Reflections reflections = new Reflections();
        Iterable<Vfs.File> files = Vfs.findFiles(urls, packagePrefix, resourceNameFilter);
        for (Vfs.File file : files) {
            InputStream inputStream = null;
            try {
                inputStream = file.openInputStream();
                reflections.merge(serializer.read(inputStream));
            }
            catch (IOException e2) {
                throw new ReflectionsException("could not merge " + file, e2);
            }
            finally {
                Utils.close(inputStream);
            }
        }
        if (log != null) {
            log.info(String.format("Reflections took %d ms to collect %d url, producing %s", System.currentTimeMillis() - start, urls.size(), Reflections.producingDescription(reflections.store)));
        }
        return reflections;
    }

    public Reflections collect(InputStream inputStream) {
        try {
            this.merge(this.configuration.getSerializer().read(inputStream));
            if (log != null) {
                log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
            }
        }
        catch (Exception ex) {
            throw new ReflectionsException("could not merge input stream", ex);
        }
        return this;
    }

    public Reflections collect(File file) {
        Reflections reflections;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            reflections = this.collect(inputStream);
        }
        catch (FileNotFoundException e2) {
            try {
                throw new ReflectionsException("could not obtain input stream from file " + file, e2);
            }
            catch (Throwable throwable) {
                Utils.close(inputStream);
                throw throwable;
            }
        }
        Utils.close(inputStream);
        return reflections;
    }

    public Reflections merge(Reflections reflections) {
        this.store.merge(reflections.store);
        return this;
    }

    public void expandSuperTypes() {
        String index = Utils.index(SubTypesScanner.class);
        Set<String> keys = this.store.keys(index);
        keys.removeAll(this.store.values(index));
        for (String key : keys) {
            Class<?> type = ReflectionUtils.forName(key, this.loaders());
            if (type == null) continue;
            this.expandSupertypes(this.store, key, type);
        }
    }

    private void expandSupertypes(Store store, String key, Class<?> type) {
        for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
            if (!store.put(SubTypesScanner.class, supertype.getName(), key)) continue;
            if (log != null) {
                log.debug("expanded subtype {} -> {}", (Object)supertype.getName(), (Object)key);
            }
            this.expandSupertypes(store, supertype.getName(), supertype);
        }
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return ReflectionUtils.forNames(this.store.getAll(SubTypesScanner.class, type.getName()), this.loaders());
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return this.getTypesAnnotatedWith(annotation, false);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation, boolean honorInherited) {
        Set<String> annotated = this.store.get(TypeAnnotationsScanner.class, annotation.getName());
        annotated.addAll(this.getAllAnnotated(annotated, annotation, honorInherited));
        return ReflectionUtils.forNames(annotated, this.loaders());
    }

    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation) {
        return this.getTypesAnnotatedWith(annotation, false);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation, boolean honorInherited) {
        Set<String> annotated = this.store.get(TypeAnnotationsScanner.class, annotation.annotationType().getName());
        Set<Class<?>> allAnnotated = Utils.filter(ReflectionUtils.forNames(annotated, this.loaders()), ReflectionUtils.withAnnotation(annotation));
        Set classes = ReflectionUtils.forNames(Utils.filter(this.getAllAnnotated(Utils.names(allAnnotated), annotation.annotationType(), honorInherited), s2 -> !annotated.contains(s2)), this.loaders());
        allAnnotated.addAll(classes);
        return allAnnotated;
    }

    protected Collection<String> getAllAnnotated(Collection<String> annotated, Class<? extends Annotation> annotation, boolean honorInherited) {
        if (honorInherited) {
            if (annotation.isAnnotationPresent(Inherited.class)) {
                Set<String> subTypes = this.store.get(SubTypesScanner.class, Utils.filter(annotated, input -> {
                    Class<?> type = ReflectionUtils.forName(input, this.loaders());
                    return type != null && !type.isInterface();
                }));
                return this.store.getAllIncluding(SubTypesScanner.class, subTypes);
            }
            return annotated;
        }
        Set<String> subTypes = this.store.getAllIncluding(TypeAnnotationsScanner.class, annotated);
        return this.store.getAllIncluding(SubTypesScanner.class, subTypes);
    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return Utils.getMethodsFromDescriptors(this.store.get(MethodAnnotationsScanner.class, annotation.getName()), this.loaders());
    }

    public Set<Method> getMethodsAnnotatedWith(Annotation annotation) {
        return Utils.filter(this.getMethodsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }

    public Set<Method> getMethodsMatchParams(Class<?> ... types) {
        return Utils.getMethodsFromDescriptors(this.store.get(MethodParameterScanner.class, Utils.names(types).toString()), this.loaders());
    }

    public Set<Method> getMethodsReturn(Class returnType) {
        return Utils.getMethodsFromDescriptors(this.store.get(MethodParameterScanner.class, Utils.names(returnType)), this.loaders());
    }

    public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return Utils.getMethodsFromDescriptors(this.store.get(MethodParameterScanner.class, annotation.getName()), this.loaders());
    }

    public Set<Method> getMethodsWithAnyParamAnnotated(Annotation annotation) {
        return Utils.filter(this.getMethodsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }

    public Set<Constructor> getConstructorsAnnotatedWith(Class<? extends Annotation> annotation) {
        return Utils.getConstructorsFromDescriptors(this.store.get(MethodAnnotationsScanner.class, annotation.getName()), this.loaders());
    }

    public Set<Constructor> getConstructorsAnnotatedWith(Annotation annotation) {
        return Utils.filter(this.getConstructorsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }

    public Set<Constructor> getConstructorsMatchParams(Class<?> ... types) {
        return Utils.getConstructorsFromDescriptors(this.store.get(MethodParameterScanner.class, Utils.names(types).toString()), this.loaders());
    }

    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return Utils.getConstructorsFromDescriptors(this.store.get(MethodParameterScanner.class, annotation.getName()), this.loaders());
    }

    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Annotation annotation) {
        return Utils.filter(this.getConstructorsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }

    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return this.store.get(FieldAnnotationsScanner.class, annotation.getName()).stream().map(annotated -> Utils.getFieldFromString(annotated, this.loaders())).collect(Collectors.toSet());
    }

    public Set<Field> getFieldsAnnotatedWith(Annotation annotation) {
        return Utils.filter(this.getFieldsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }

    public Set<String> getResources(Predicate<String> namePredicate) {
        Set<String> resources = Utils.filter(this.store.keys(Utils.index(ResourcesScanner.class)), namePredicate);
        return this.store.get(ResourcesScanner.class, resources);
    }

    public Set<String> getResources(Pattern pattern) {
        return this.getResources((String input) -> pattern.matcher((CharSequence)input).matches());
    }

    public List<String> getMethodParamNames(Method method) {
        Set<String> names = this.store.get(MethodParameterNamesScanner.class, Utils.name(method));
        return names.size() == 1 ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }

    public List<String> getConstructorParamNames(Constructor constructor) {
        Set<String> names = this.store.get(MethodParameterNamesScanner.class, Utils.name(constructor));
        return names.size() == 1 ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }

    public Set<Member> getFieldUsage(Field field) {
        return Utils.getMembersFromDescriptors(this.store.get(MemberUsageScanner.class, Utils.name(field)), new ClassLoader[0]);
    }

    public Set<Member> getMethodUsage(Method method) {
        return Utils.getMembersFromDescriptors(this.store.get(MemberUsageScanner.class, Utils.name(method)), new ClassLoader[0]);
    }

    public Set<Member> getConstructorUsage(Constructor constructor) {
        return Utils.getMembersFromDescriptors(this.store.get(MemberUsageScanner.class, Utils.name(constructor)), new ClassLoader[0]);
    }

    public Set<String> getAllTypes() {
        HashSet<String> allTypes = new HashSet<String>(this.store.getAll(SubTypesScanner.class, Object.class.getName()));
        if (allTypes.isEmpty()) {
            throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
        }
        return allTypes;
    }

    public Store getStore() {
        return this.store;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public File save(String filename) {
        return this.save(filename, this.configuration.getSerializer());
    }

    public File save(String filename, Serializer serializer) {
        File file = serializer.save(this, filename);
        if (log != null) {
            log.info("Reflections successfully saved in " + file.getAbsolutePath() + " using " + serializer.getClass().getSimpleName());
        }
        return file;
    }

    private ClassLoader[] loaders() {
        return this.configuration.getClassLoaders();
    }
}

