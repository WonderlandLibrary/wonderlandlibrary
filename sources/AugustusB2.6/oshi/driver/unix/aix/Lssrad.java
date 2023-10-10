// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.util.tuples.Pair;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lssrad
{
    private Lssrad() {
    }
    
    public static Map<Integer, Pair<Integer, Integer>> queryNodesPackages() {
        int node = 0;
        int slot = 0;
        final Map<Integer, Pair<Integer, Integer>> nodeMap = new HashMap<Integer, Pair<Integer, Integer>>();
        final List<String> lssrad = ExecutingCommand.runNative("lssrad -av");
        if (!lssrad.isEmpty()) {
            lssrad.remove(0);
        }
        for (final String s : lssrad) {
            String t = s.trim();
            if (!t.isEmpty()) {
                if (Character.isDigit(s.charAt(0))) {
                    node = ParseUtil.parseIntOrDefault(t, 0);
                }
                else {
                    if (t.contains(".")) {
                        final String[] split = ParseUtil.whitespaces.split(t, 3);
                        slot = ParseUtil.parseIntOrDefault(split[0], 0);
                        t = ((split.length > 2) ? split[2] : "");
                    }
                    for (final Integer proc : ParseUtil.parseHyphenatedIntList(t)) {
                        nodeMap.put(proc, new Pair<Integer, Integer>(node, slot));
                    }
                }
            }
        }
        return nodeMap;
    }
}
