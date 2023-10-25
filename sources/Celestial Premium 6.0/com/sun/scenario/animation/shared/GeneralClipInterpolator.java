/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.ClipInterpolator;
import com.sun.scenario.animation.shared.InterpolationInterval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

class GeneralClipInterpolator
extends ClipInterpolator {
    private KeyFrame[] keyFrames;
    private long[] keyFrameTicks;
    private InterpolationInterval[][] interval = new InterpolationInterval[0][];
    private int[] undefinedStartValues = new int[0];
    private boolean invalid = true;

    GeneralClipInterpolator(KeyFrame[] arrkeyFrame, long[] arrl) {
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = arrl;
    }

    @Override
    ClipInterpolator setKeyFrames(KeyFrame[] arrkeyFrame, long[] arrl) {
        if (ClipInterpolator.getRealKeyFrameCount(arrkeyFrame) == 2) {
            return ClipInterpolator.create(arrkeyFrame, arrl);
        }
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = arrl;
        this.invalid = true;
        return this;
    }

    @Override
    void validate(boolean bl) {
        if (this.invalid) {
            int n;
            Object object;
            KeyFrame keyFrame;
            int n2;
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            int n3 = this.keyFrames.length;
            for (n2 = 0; n2 < n3; ++n2) {
                keyFrame = this.keyFrames[n2];
                if (this.keyFrameTicks[n2] != 0L) break;
                for (KeyFrame keyFrame2 : keyFrame.getValues()) {
                    hashMap.put((Object)keyFrame2.getTarget(), (Object)keyFrame2);
                }
            }
            keyFrame = new HashMap();
            HashSet hashSet = new HashSet();
            while (n2 < n3) {
                KeyFrame keyFrame2;
                keyFrame2 = this.keyFrames[n2];
                long l = this.keyFrameTicks[n2];
                for (KeyValue keyValue : keyFrame2.getValues()) {
                    object = keyValue.getTarget();
                    ArrayList<InterpolationInterval> arrayList = (ArrayList<InterpolationInterval>)keyFrame.get(object);
                    KeyValue keyValue2 = (KeyValue)hashMap.get(object);
                    if (arrayList == null) {
                        arrayList = new ArrayList<InterpolationInterval>();
                        keyFrame.put(object, arrayList);
                        if (keyValue2 == null) {
                            arrayList.add(InterpolationInterval.create(keyValue, l));
                            hashSet.add(object);
                        } else {
                            arrayList.add(InterpolationInterval.create(keyValue, l, keyValue2, l));
                        }
                    } else {
                        assert (keyValue2 != null);
                        arrayList.add(InterpolationInterval.create(keyValue, l, keyValue2, l - ((InterpolationInterval)arrayList.get((int)(arrayList.size() - 1))).ticks));
                    }
                    hashMap.put(object, (Object)keyValue);
                }
                ++n2;
            }
            int n4 = keyFrame.size();
            if (this.interval.length != n4) {
                this.interval = new InterpolationInterval[n4][];
            }
            if (this.undefinedStartValues.length != (n = hashSet.size())) {
                this.undefinedStartValues = new int[n];
            }
            int n5 = 0;
            Iterator<Object> iterator = keyFrame.entrySet().iterator();
            for (int i = 0; i < n4; ++i) {
                object = (Map.Entry)iterator.next();
                this.interval[i] = new InterpolationInterval[((List)object.getValue()).size()];
                ((List)object.getValue()).toArray(this.interval[i]);
                if (!hashSet.contains(object.getKey())) continue;
                this.undefinedStartValues[n5++] = i;
            }
            this.invalid = false;
        } else if (bl) {
            for (int n : this.undefinedStartValues) {
                this.interval[n][0].recalculateStartValue();
            }
        }
    }

    @Override
    void interpolate(long l) {
        block0: for (InterpolationInterval[] arrinterpolationInterval : this.interval) {
            int n = arrinterpolationInterval.length;
            long l2 = 0L;
            for (int i = 0; i < n - 1; ++i) {
                InterpolationInterval interpolationInterval = arrinterpolationInterval[i];
                long l3 = interpolationInterval.ticks;
                if (l <= l3) {
                    double d = (double)(l - l2) / (double)(l3 - l2);
                    interpolationInterval.interpolate(d);
                    continue block0;
                }
                l2 = l3;
            }
            InterpolationInterval interpolationInterval = arrinterpolationInterval[n - 1];
            double d = Math.min(1.0, (double)(l - l2) / (double)(interpolationInterval.ticks - l2));
            interpolationInterval.interpolate(d);
        }
    }
}

