/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.List;
import joptsimple.OptionSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface OptionSpec<V> {
    public List<V> values(OptionSet var1);

    public V value(OptionSet var1);

    public List<String> options();

    public boolean isForHelp();
}

