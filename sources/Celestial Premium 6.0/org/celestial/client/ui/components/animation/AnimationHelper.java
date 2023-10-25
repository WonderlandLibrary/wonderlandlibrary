/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.animation;

import java.util.HashMap;
import org.celestial.client.ui.components.animation.Animation;
import org.celestial.client.ui.components.animation.impl.Back;
import org.celestial.client.ui.components.animation.impl.utils.Progression;

public class AnimationHelper {
    private final HashMap<Integer, Progression> progressions;
    private Animation animation;

    public AnimationHelper(Class<? extends Animation> easingClass) {
        try {
            this.animation = easingClass.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        this.progressions = new HashMap();
    }

    public double easeOut(Progression progression, double startValue, double endValue, double duration) {
        if (startValue > endValue) {
            double tempEnd = endValue;
            endValue = startValue;
            startValue = tempEnd;
        }
        if (progression.getValue() < duration) {
            progression.setValue(progression.getValue() + (double)0.005f);
        }
        if (this.animation instanceof Back) {
            return Back.easeOut(progression.getValue(), startValue, endValue, duration);
        }
        return 0.0;
    }

    public double easeOut(int animationId, double startValue, double endValue, double duration) {
        if (this.progressions.containsKey(animationId)) {
            return this.easeOut(this.progressions.get(animationId), startValue, endValue, duration);
        }
        this.progressions.put(animationId, new Progression());
        return 0.0;
    }

    public Progression getProgression(int animationId) {
        Progression progress;
        if (!this.progressions.containsKey(animationId)) {
            Progression progressNew = new Progression();
            this.progressions.put(animationId, progressNew);
            progress = progressNew;
        } else {
            progress = this.progressions.get(animationId);
        }
        return progress;
    }
}

