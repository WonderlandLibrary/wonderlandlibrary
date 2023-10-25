/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Effect;

public class AccessHelper {
    private static StateAccessor theStateAccessor;

    public static void setStateAccessor(StateAccessor stateAccessor) {
        if (theStateAccessor != null) {
            throw new InternalError("EffectAccessor already initialized");
        }
        theStateAccessor = stateAccessor;
    }

    public static Object getState(Effect effect) {
        if (effect == null) {
            return null;
        }
        return theStateAccessor.getState(effect);
    }

    public static interface StateAccessor {
        public Object getState(Effect var1);
    }
}

