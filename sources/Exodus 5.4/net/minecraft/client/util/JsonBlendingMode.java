/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.lwjgl.opengl.GL14
 */
package net.minecraft.client.util;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.JsonUtils;
import org.lwjgl.opengl.GL14;

public class JsonBlendingMode {
    private final int field_148117_c;
    private final boolean field_148119_h;
    private final int field_148115_e;
    private final int field_148114_d;
    private final int field_148112_f;
    private final int field_148116_b;
    private final boolean field_148113_g;
    private static JsonBlendingMode field_148118_a = null;

    public JsonBlendingMode(int n, int n2, int n3) {
        this(false, false, n, n2, n, n2, n3);
    }

    private static int func_148107_b(String string) {
        String string2 = string.trim().toLowerCase();
        string2 = string2.replaceAll("_", "");
        string2 = string2.replaceAll("one", "1");
        string2 = string2.replaceAll("zero", "0");
        return (string2 = string2.replaceAll("minus", "-")).equals("0") ? 0 : (string2.equals("1") ? 1 : (string2.equals("srccolor") ? 768 : (string2.equals("1-srccolor") ? 769 : (string2.equals("dstcolor") ? 774 : (string2.equals("1-dstcolor") ? 775 : (string2.equals("srcalpha") ? 770 : (string2.equals("1-srcalpha") ? 771 : (string2.equals("dstalpha") ? 772 : (string2.equals("1-dstalpha") ? 773 : -1)))))))));
    }

    private static int func_148108_a(String string) {
        String string2 = string.trim().toLowerCase();
        return string2.equals("add") ? 32774 : (string2.equals("subtract") ? 32778 : (string2.equals("reversesubtract") ? 32779 : (string2.equals("reverse_subtract") ? 32779 : (string2.equals("min") ? 32775 : (string2.equals("max") ? 32776 : 32774)))));
    }

    public JsonBlendingMode(int n, int n2, int n3, int n4, int n5) {
        this(true, false, n, n2, n3, n4, n5);
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
            GL14.glBlendEquation((int)this.field_148112_f);
            if (this.field_148113_g) {
                GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
            } else {
                GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
            }
        }
    }

    public int hashCode() {
        int n = this.field_148116_b;
        n = 31 * n + this.field_148117_c;
        n = 31 * n + this.field_148114_d;
        n = 31 * n + this.field_148115_e;
        n = 31 * n + this.field_148112_f;
        n = 31 * n + (this.field_148113_g ? 1 : 0);
        n = 31 * n + (this.field_148119_h ? 1 : 0);
        return n;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof JsonBlendingMode)) {
            return false;
        }
        JsonBlendingMode jsonBlendingMode = (JsonBlendingMode)object;
        return this.field_148112_f != jsonBlendingMode.field_148112_f ? false : (this.field_148115_e != jsonBlendingMode.field_148115_e ? false : (this.field_148114_d != jsonBlendingMode.field_148114_d ? false : (this.field_148119_h != jsonBlendingMode.field_148119_h ? false : (this.field_148113_g != jsonBlendingMode.field_148113_g ? false : (this.field_148117_c != jsonBlendingMode.field_148117_c ? false : this.field_148116_b == jsonBlendingMode.field_148116_b)))));
    }

    public boolean func_148111_b() {
        return this.field_148119_h;
    }

    private JsonBlendingMode(boolean bl, boolean bl2, int n, int n2, int n3, int n4, int n5) {
        this.field_148113_g = bl;
        this.field_148116_b = n;
        this.field_148114_d = n2;
        this.field_148117_c = n3;
        this.field_148115_e = n4;
        this.field_148119_h = bl2;
        this.field_148112_f = n5;
    }

    public JsonBlendingMode() {
        this(false, true, 1, 0, 1, 0, 32774);
    }

    public static JsonBlendingMode func_148110_a(JsonObject jsonObject) {
        if (jsonObject == null) {
            return new JsonBlendingMode();
        }
        int n = 32774;
        int n2 = 1;
        int n3 = 0;
        int n4 = 1;
        int n5 = 0;
        boolean bl = true;
        boolean bl2 = false;
        if (JsonUtils.isString(jsonObject, "func") && (n = JsonBlendingMode.func_148108_a(jsonObject.get("func").getAsString())) != 32774) {
            bl = false;
        }
        if (JsonUtils.isString(jsonObject, "srcrgb") && (n2 = JsonBlendingMode.func_148107_b(jsonObject.get("srcrgb").getAsString())) != 1) {
            bl = false;
        }
        if (JsonUtils.isString(jsonObject, "dstrgb") && (n3 = JsonBlendingMode.func_148107_b(jsonObject.get("dstrgb").getAsString())) != 0) {
            bl = false;
        }
        if (JsonUtils.isString(jsonObject, "srcalpha")) {
            n4 = JsonBlendingMode.func_148107_b(jsonObject.get("srcalpha").getAsString());
            if (n4 != 1) {
                bl = false;
            }
            bl2 = true;
        }
        if (JsonUtils.isString(jsonObject, "dstalpha")) {
            n5 = JsonBlendingMode.func_148107_b(jsonObject.get("dstalpha").getAsString());
            if (n5 != 0) {
                bl = false;
            }
            bl2 = true;
        }
        return bl ? new JsonBlendingMode() : (bl2 ? new JsonBlendingMode(n2, n3, n4, n5, n) : new JsonBlendingMode(n2, n3, n));
    }
}

