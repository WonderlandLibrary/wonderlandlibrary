/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 */
package com.sun.scenario.animation.shared;

import javafx.animation.Animation;

public abstract class AnimationAccessor {
    public static AnimationAccessor DEFAULT;

    public static AnimationAccessor getDefault() {
        block4: {
            if (DEFAULT != null) {
                return DEFAULT;
            }
            Class<Animation> class_ = Animation.class;
            try {
                Class.forName(class_.getName());
            }
            catch (ClassNotFoundException classNotFoundException) {
                if ($assertionsDisabled) break block4;
                throw new AssertionError((Object)classNotFoundException);
            }
        }
        assert (DEFAULT != null) : "The DEFAULT field must be initialized";
        return DEFAULT;
    }

    public abstract void setCurrentRate(Animation var1, double var2);

    public abstract void setCurrentTicks(Animation var1, long var2);

    public abstract void playTo(Animation var1, long var2, long var4);

    public abstract void jumpTo(Animation var1, long var2, long var4, boolean var6);

    public abstract void finished(Animation var1);
}

