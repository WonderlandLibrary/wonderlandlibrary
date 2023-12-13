/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.ttf.FontUtils;
import cow.milkgod.cheese.utils.Wrapper;

public class Comparator
implements java.util.Comparator<Module> {
    @Override
    public int compare(Module m1, Module m2) {
        int mod1 = Wrapper.fu_default.getWidthInt(m1.getDisplayName());
        int mod2 = Wrapper.fu_default.getWidthInt(m2.getDisplayName());
        return mod2 - mod1;
    }
}

