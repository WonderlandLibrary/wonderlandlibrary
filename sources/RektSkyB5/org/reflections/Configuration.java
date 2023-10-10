/*
 * Decompiled with CFR 0.152.
 */
package org.reflections;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.Scanner;
import org.reflections.serializers.Serializer;

public interface Configuration {
    public Set<Scanner> getScanners();

    public Set<URL> getUrls();

    public MetadataAdapter getMetadataAdapter();

    public Predicate<String> getInputsFilter();

    public ExecutorService getExecutorService();

    public Serializer getSerializer();

    public ClassLoader[] getClassLoaders();

    public boolean shouldExpandSuperTypes();
}

