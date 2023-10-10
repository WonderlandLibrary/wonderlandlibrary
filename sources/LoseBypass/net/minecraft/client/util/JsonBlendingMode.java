/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL14
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
        if (this.equals(field_148118_a)) return;
        if (field_148118_a == null || this.field_148119_h != field_148118_a.func_148111_b()) {
            field_148118_a = this;
            if (this.field_148119_h) {
                GlStateManager.disableBlend();
                return;
            }
            GlStateManager.enableBlend();
        }
        GL14.glBlendEquation((int)this.field_148112_f);
        if (this.field_148113_g) {
            GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
            return;
        }
        GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof JsonBlendingMode)) {
            return false;
        }
        JsonBlendingMode jsonblendingmode = (JsonBlendingMode)p_equals_1_;
        if (this.field_148112_f != jsonblendingmode.field_148112_f) {
            return false;
        }
        if (this.field_148115_e != jsonblendingmode.field_148115_e) {
            return false;
        }
        if (this.field_148114_d != jsonblendingmode.field_148114_d) {
            return false;
        }
        if (this.field_148119_h != jsonblendingmode.field_148119_h) {
            return false;
        }
        if (this.field_148113_g != jsonblendingmode.field_148113_g) {
            return false;
        }
        if (this.field_148117_c != jsonblendingmode.field_148117_c) {
            return false;
        }
        if (this.field_148116_b != jsonblendingmode.field_148116_b) return false;
        return true;
    }

    public int hashCode() {
        int i = this.field_148116_b;
        i = 31 * i + this.field_148117_c;
        i = 31 * i + this.field_148114_d;
        i = 31 * i + this.field_148115_e;
        i = 31 * i + this.field_148112_f;
        i = 31 * i + (this.field_148113_g ? 1 : 0);
        return 31 * i + (this.field_148119_h ? 1 : 0);
    }

    public boolean func_148111_b() {
        return this.field_148119_h;
    }

    public static JsonBlendingMode func_148110_a(JsonObject p_148110_0_) {
        JsonBlendingMode jsonBlendingMode;
        if (p_148110_0_ == null) {
            return new JsonBlendingMode();
        }
        int i = 32774;
        int j = 1;
        int k = 0;
        int l = 1;
        int i1 = 0;
        boolean flag = true;
        boolean flag1 = false;
        if (JsonUtils.isString(p_148110_0_, "func") && (i = JsonBlendingMode.func_148108_a(p_148110_0_.get("func").getAsString())) != 32774) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "srcrgb") && (j = JsonBlendingMode.func_148107_b(p_148110_0_.get("srcrgb").getAsString())) != 1) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "dstrgb") && (k = JsonBlendingMode.func_148107_b(p_148110_0_.get("dstrgb").getAsString())) != 0) {
            flag = false;
        }
        if (JsonUtils.isString(p_148110_0_, "srcalpha")) {
            l = JsonBlendingMode.func_148107_b(p_148110_0_.get("srcalpha").getAsString());
            if (l != 1) {
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
        if (flag) {
            jsonBlendingMode = new JsonBlendingMode();
            return jsonBlendingMode;
        }
        if (flag1) {
            jsonBlendingMode = new JsonBlendingMode(j, k, l, i1, i);
            return jsonBlendingMode;
        }
        jsonBlendingMode = new JsonBlendingMode(j, k, i);
        return jsonBlendingMode;
    }

    private static int func_148108_a(String p_148108_0_) {
        String s = p_148108_0_.trim().toLowerCase();
        if (s.equals("add")) {
            return 32774;
        }
        if (s.equals("subtract")) {
            return 32778;
        }
        if (s.equals("reversesubtract")) {
            return 32779;
        }
        if (s.equals("reverse_subtract")) {
            return 32779;
        }
        if (s.equals("min")) {
            return 32775;
        }
        if (!s.equals("max")) return 32774;
        return 32776;
    }

    private static int func_148107_b(String p_148107_0_) {
        String s = p_148107_0_.trim().toLowerCase();
        s = s.replaceAll("_", "");
        s = s.replaceAll("one", "1");
        s = s.replaceAll("zero", "0");
        if ((s = s.replaceAll("minus", "-")).equals("0")) {
            return 0;
        }
        if (s.equals("1")) {
            return 1;
        }
        if (s.equals("srccolor")) {
            return 768;
        }
        if (s.equals("1-srccolor")) {
            return 769;
        }
        if (s.equals("dstcolor")) {
            return 774;
        }
        if (s.equals("1-dstcolor")) {
            return 775;
        }
        if (s.equals("srcalpha")) {
            return 770;
        }
        if (s.equals("1-srcalpha")) {
            return 771;
        }
        if (s.equals("dstalpha")) {
            return 772;
        }
        if (!s.equals("1-dstalpha")) return -1;
        return 773;
    }
}

