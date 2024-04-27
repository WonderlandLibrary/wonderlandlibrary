/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="PictureColorTwo", description="\u5168\u5c40\u989c\u82722", category=ModuleCategory.HYT)
public final class PictureColor2
extends Module {
    private int h2;
    private int s1;
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private int h1;
    private int m2;
    private int s;
    private final IntegerValue coloralpha;
    private int s2;
    private int m;
    private final IntegerValue colorBlueValue;
    private int h;
    private MSTimer timer;
    private final IntegerValue colorGreenValue = new IntegerValue("G", 133, 0, 255);
    private int m1;

    public final IntegerValue getColoralpha() {
        return this.coloralpha;
    }

    public final void setM2(int n) {
        this.m2 = n;
    }

    public final int getM() {
        return this.m;
    }

    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    public final int getM1() {
        return this.m1;
    }

    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    public final void setS2(int n) {
        this.s2 = n;
    }

    public final int getH1() {
        return this.h1;
    }

    public final int getH2() {
        return this.h2;
    }

    public final void setH2(int n) {
        this.h2 = n;
    }

    public final void setH(int n) {
        this.h = n;
    }

    public final void setM1(int n) {
        this.m1 = n;
    }

    public final void setTimer(MSTimer mSTimer) {
        this.timer = mSTimer;
    }

    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    public final int getM2() {
        return this.m2;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        int n;
        if (this.s == 60) {
            this.s = 0;
            n = this.m;
            this.m = n + 1;
        }
        if (this.m == 60) {
            this.m = 0;
            n = this.h;
            this.h = n + 1;
        }
        if (this.h == 60) {
            this.h = 0;
        }
        if (this.s1 == 10) {
            n = this.s2;
            this.s2 = n + 1;
            this.s1 = 0;
        }
        if (this.s2 == 6) {
            n = this.m1;
            this.m1 = n + 1;
            this.s2 = 0;
        }
        if (this.m1 == 10) {
            this.m1 = 0;
            n = this.m2;
            this.m2 = n + 1;
        }
        if (this.m2 == 6) {
            n = this.h1;
            this.h1 = n + 1;
            this.m2 = 0;
        }
        if (this.h1 == 10) {
            this.h1 = 0;
        }
        if (!this.timer.hasTimePassed(1000L)) {
            return;
        }
        n = this.s;
        this.s = n + 1;
        n = this.s1;
        this.s1 = n + 1;
        this.timer.reset();
    }

    public final int getS2() {
        return this.s2;
    }

    public final void setM(int n) {
        this.m = n;
    }

    public final void setS1(int n) {
        this.s1 = n;
    }

    public final int getS() {
        return this.s;
    }

    public final MSTimer getTimer() {
        return this.timer;
    }

    public final int getS1() {
        return this.s1;
    }

    public final int getH() {
        return this.h;
    }

    public final void setS(int n) {
        this.s = n;
    }

    public final void setH1(int n) {
        this.h1 = n;
    }

    public PictureColor2() {
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.coloralpha = new IntegerValue("alpha", 255, 0, 255);
        this.timer = new MSTimer();
    }
}

