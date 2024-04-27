/*
 * Decompiled with CFR 0.152.
 */
package ad;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.features.module.Module;

public class StringUtils {
    public static String findLongestModuleName(List<Module> modules2) {
        return Collections.max(modules2, Comparator.comparing(module -> (module.getName() + module.getTagName()).length())).getName();
    }

    public static List<Module> getToggledModules(List<Module> modules2) {
        return modules2.stream().filter(Module::getState).collect(Collectors.toList());
    }
}

