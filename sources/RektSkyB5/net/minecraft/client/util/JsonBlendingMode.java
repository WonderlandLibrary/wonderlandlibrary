/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.util;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.JsonUtils;
import org.lwjgl.opengl.GL14;

public class JsonBlendingMode {
    private static JsonBlendingMode field_148118_a = null;
    private final int field_148116_b;
    private final int field_148117_c;
    private final int field_148114_d;
    private final int field_148115_e;
    private final int field_148112_f;
    private final boolean field_148113_g;
    private final boolean field_148119_h;

    private JsonBlendingMode(boolean p_i45084_1_, boolean p_i45084_2_, int p_i45084_3_, int p_i45084_4_, int p_i45084_5_, int p_i45084_6_, int p_i45084_7_) {
        this.field_148113_g = p_i45084_1_;
        this.field_148116_b = p_i45084_3_;
        this.field_148114_d = p_i45084_4_;
        this.field_148117_c = p_i45084_5_;
        this.field_148115_e = p_i45084_6_;
        this.field_148119_h = p_i45084_2_;
        this.field_148112_f = p_i45084_7_;
    }

    public JsonBlendingMode() {
        this(false, true, 1, 0, 1, 0, 32774);
    }

    public JsonBlendingMode(int p_i45085_1_, int p_i45085_2_, int p_i45085_3_) {
        this(false, false, p_i45085_1_, p_i45085_2_, p_i45085_1_, p_i45085_2_, p_i45085_3_);
    }

    public JsonBlendingMode(int p_i45086_1_, int p_i45086_2_, int p_i45086_3_, int p_i45086_4_, int p_i45086_5_) {
        this(true, false, p_i45086_1_, p_i45086_2_, p_i45086_3_, p_i45086_4_, p_i45086_5_);
    }

    public void func_148109_a() {
        if (!this.equals(field_148118_a)) {
            if (field_148118_a == null || this.field_148119_h != field_148118_a.func_148111_b()) {
                field_148118_a = this;
                if (this.field_148119_h) {
                    GlStateManager.disableBlend();
                    return;
                }
                GlStateManager.enableBlend();
            }
            GL14.glBlendEquation(this.field_148112_f);
            if (this.field_148113_g) {
                GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
            } else {
                GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
            }
        }
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof JsonBlendingMode)) {
            return false;
        }
        JsonBlendingMode jsonblendingmode = (JsonBlendingMode)p_equals_1_;
        return this.field_148112_f != jsonblendingmode.field_148112_f ? false : (this.field_148115_e != jsonblendingmode.field_148115_e ? false : (this.field_148114_d != jsonblendingmode.field_148114_d ? false : (this.field_148119_h != jsonblendingmode.field_148119_h ? false : (this.field_148113_g != jsonblendingmode.field_148113_g ? false : (this.field_148117_c != jsonblendingmode.field_148117_c ? false : this.field_148116_b == jsonblendingmode.field_148116_b)))));
    }

    public int hashCode() {
        int i2 = this.field_148116_b;
        i2 = 31 * i2 + this.field_148117_c;
        i2 = 31 * i2 + this.field_148114_d;
        i2 = 31 * i2 + this.field_148115_e;
        i2 = 31 * i2 + this.field_148112_f;
        i2 = 31 * i2 + (this.field_148113_g ? 1 : 0);
        i2 = 31 * i2 + (this.field_148119_h ? 1 : 0);
        return i2;
    }

    public boolean func_148111_b() {
        return this.field_148119_h;
    }

    public static JsonBlendingMode func_148110_a(JsonObject p_148110_0_) {
        if (p_148110_0_ == null) {
            return new JsonBlendingMode();
        }
        int i2 = 32774;
        int j2 = 1;
        int k2 = 0;
        int l2 = 1;
        int i1 = 0;
        boolean flag = true;
        boolean flag1 = false;
        if (JsonUtils.isString(p_148110_0_, "func") && (i2 = JsonBlendingMode.func_148108_a(p_148110_0_.get("func").getAsString())) != 32774) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "srcrgb") && (j2 = JsonBlendingMode.func_148107_b(p_148110_0_.get("srcrgb").getAsString())) != 1) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "dstrgb") && (k2 = JsonBlendingMode.func_148107_b(p_148110_0_.get("dstrgb").getAsString())) != 0) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "srcalpha")) {
            l2 = JsonBlendingMode.func_148107_b(p_148110_0_.get("srcalpha").getAsString());
            if (l2 != 1) {
                flag = false;
            }
            flag1 = true;
        }
        if (JsonUtils.isString(p_148110_0_, "dstalpha")) {
            i1 = JsonBlendingMode.func_148107_b(p_148110_0_.get("dstalpha").getAsString());
            if (i1 != 0) {
                flag = false;
            }
            flag1 = true;
        }
        return flag ? new JsonBlendingMode() : (flag1 ? new JsonBlendingMode(j2, k2, l2, i1, i2) : new JsonBlendingMode(j2, k2, i2));
    }

    private static int func_148108_a(String p_148108_0_) {
        String s2 = p_148108_0_.trim().toLowerCase();
        return s2.equals("add") ? 32774 : (s2.equals("subtract") ? 32778 : (s2.equals("reversesubtract") ? 32779 : (s2.equals("reverse_subtract") ? 32779 : (s2.equals("min") ? 32775 : (s2.equals("max") ? 32776 : 32774)))));
    }

    private static int func_148107_b(String p_148107_0_) {
        String s2 = p_148107_0_.trim().toLowerCase();
        s2 = s2.replaceAll("_", "");
        s2 = s2.replaceAll("one", "1");
        s2 = s2.replaceAll("zero", "0");
        return (s2 = s2.replaceAll("minus", "-")).equals("0") ? 0 : (s2.equals("1") ? 1 : (s2.equals("srccolor") ? 768 : (s2.equals("1-srccolor") ? 769 : (s2.equals("dstcolor") ? 774 : (s2.equals("1-dstcolor") ? 775 : (s2.equals("srcalpha") ? 770 : (s2.equals("1-srcalpha") ? 771 : (s2.equals("dstalpha") ? 772 : (s2.equals("1-dstalpha") ? 773 : -1)))))))));
    }
}

