/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.scripts;

import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public class JO
extends ScriptObject {
    private static final PropertyMap map$ = PropertyMap.newMap(JO.class);

    public static PropertyMap getInitialMap() {
        return map$;
    }

    public JO(PropertyMap map) {
        super(map);
    }

    public JO(ScriptObject proto) {
        super(proto, JO.getInitialMap());
    }

    public JO(PropertyMap map, long[] primitiveSpill, Object[] objectSpill) {
        super(map, primitiveSpill, objectSpill);
    }

    public static ScriptObject allocate(PropertyMap map) {
        return new JO(map);
    }
}

