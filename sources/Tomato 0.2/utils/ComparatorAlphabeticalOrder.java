/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.module.Module;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ComparatorAlphabeticalOrder
implements Comparator<Module> {
    Collator collator = Collator.getInstance(Locale.US);

    @Override
    public int compare(Module o1, Module o2) {
        return this.collator.compare(o1.getName(), o2.getName());
    }
}

