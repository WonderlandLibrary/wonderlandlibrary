/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.render.AnimationUtil;

public final class Translate {
    private boolean AckerRunCRACKED_f;
    private float AckerRunCRACKED_a;
    private float AckerRunCRACKED_ALLATORIxDEMO;

    public float getX() {
        Translate IIiiiiiiIiIii = this;
        return IIiiiiiiIiIii.AckerRunCRACKED_a;
    }

    public static String AckerRunCRACKED_ALLATORIxDEMO(String IIiiiiiiIiIii) {
        int n = IIiiiiiiIiIii.length();
        int n2 = n - 1;
        char[] cArray = new char[n];
        int n3 = 12;
        int cfr_ignored_0 = 25;
        int n4 = n2;
        int n5 = 19;
        while (n4 >= 0) {
            int n6 = n2--;
            cArray[n6] = (char)(IIiiiiiiIiIii.charAt(n6) ^ n5);
            if (n2 < 0) break;
            int n7 = n2--;
            cArray[n7] = (char)(IIiiiiiiIiIii.charAt(n7) ^ n3);
            n4 = n2;
        }
        return new String(cArray);
    }

    public Translate(float f, float f2) {
        Translate IIiiiiiiIiIii2;
        float IIiiiiiiIiIii = 0.0f;
        Translate translate = IIiiiiiiIiIii2 = this;
        IIiiiiiiIiIii2.AckerRunCRACKED_f = false;
        translate.AckerRunCRACKED_a = IIiiiiiiIiIii;
        translate.AckerRunCRACKED_ALLATORIxDEMO = f2;
    }

    public float getY() {
        Translate IIiiiiiiIiIii = this;
        return IIiiiiiiIiIii.AckerRunCRACKED_ALLATORIxDEMO;
    }

    public final void interpolate2(float f, float f2, double d) {
        double IIiiiiiiIiIii = 0.0;
        Translate IIiiiiiiIiIii2 = this;
        this.AckerRunCRACKED_a = f;
        IIiiiiiiIiIii2.AckerRunCRACKED_ALLATORIxDEMO = AnimationUtil.animate(f2, IIiiiiiiIiIii2.AckerRunCRACKED_ALLATORIxDEMO, IIiiiiiiIiIii);
    }

    public final void interpolate(float IIiiiiiiIiIii, float IIiiiiiiIiIii2, double IIiiiiiiIiIii3) {
        Translate IIiiiiiiIiIii4 = this;
        if (IIiiiiiiIiIii4.AckerRunCRACKED_f) {
            IIiiiiiiIiIii4.AckerRunCRACKED_a = AnimationUtil.animate(IIiiiiiiIiIii, IIiiiiiiIiIii4.AckerRunCRACKED_a, IIiiiiiiIiIii3);
            IIiiiiiiIiIii4.AckerRunCRACKED_ALLATORIxDEMO = AnimationUtil.animate(IIiiiiiiIiIii2, IIiiiiiiIiIii4.AckerRunCRACKED_ALLATORIxDEMO, IIiiiiiiIiIii3);
            return;
        }
        Translate translate = IIiiiiiiIiIii4;
        translate.AckerRunCRACKED_a = IIiiiiiiIiIii;
        translate.AckerRunCRACKED_ALLATORIxDEMO = IIiiiiiiIiIii2;
        IIiiiiiiIiIii4.AckerRunCRACKED_f = true;
    }

    public void setX(float IIiiiiiiIiIii) {
        this.AckerRunCRACKED_a = IIiiiiiiIiIii;
    }

    public void setY(float IIiiiiiiIiIii) {
        this.AckerRunCRACKED_ALLATORIxDEMO = IIiiiiiiIiIii;
    }
}

