/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import java.util.function.Predicate;
import org.reflections.Configuration;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.Scanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public abstract class AbstractScanner
implements Scanner {
    private Configuration configuration;
    private Predicate<String> resultFilter = s2 -> true;

    @Override
    public boolean acceptsInput(String file) {
        return this.getMetadataAdapter().acceptsInput(file);
    }

    @Override
    public Object scan(Vfs.File file, Object classObject, Store store) {
        if (classObject == null) {
            try {
                classObject = this.configuration.getMetadataAdapter().getOrCreateClassObject(file);
            }
            catch (Exception e2) {
                throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), e2);
            }
        }
        this.scan(classObject, store);
        return classObject;
    }

    public abstract void scan(Object var1, Store var2);

    protected void put(Store store, String key, String value) {
        store.put(Utils.index(this.getClass()), key, value);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Predicate<String> getResultFilter() {
        return this.resultFilter;
    }

    public void setResultFilter(Predicate<String> resultFilter) {
        this.resultFilter = resultFilter;
    }

    @Override
    public Scanner filterResultsBy(Predicate<String> filter) {
        this.setResultFilter(filter);
        return this;
    }

    @Override
    public boolean acceptResult(String fqn) {
        return fqn != null && this.resultFilter.test(fqn);
    }

    protected MetadataAdapter getMetadataAdapter() {
        return this.configuration.getMetadataAdapter();
    }

    public boolean equals(Object o2) {
        return this == o2 || o2 != null && this.getClass() == o2.getClass();
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }
}

