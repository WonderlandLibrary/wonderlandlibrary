/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import java.util.function.Predicate;
import org.reflections.Configuration;
import org.reflections.Store;
import org.reflections.vfs.Vfs;

public interface Scanner {
    public void setConfiguration(Configuration var1);

    public Scanner filterResultsBy(Predicate<String> var1);

    public boolean acceptsInput(String var1);

    public Object scan(Vfs.File var1, Object var2, Store var3);

    public boolean acceptResult(String var1);
}

