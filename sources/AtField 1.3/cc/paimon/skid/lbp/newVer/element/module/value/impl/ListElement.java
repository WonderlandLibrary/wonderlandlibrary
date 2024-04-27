/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer.element.module.value.impl;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.module.value.ValueElement;
import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class ListElement
extends ValueElement {
    private final int maxSubWidth;
    private boolean expansion;
    private float expandHeight;
    private final ListValue saveValue;
    public static final Companion Companion = new Companion(null);
    private static final IResourceLocation expanding = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/expand.png");

    public static final IResourceLocation access$getExpanding$cp() {
        return expanding;
    }

    public ListElement(ListValue listValue) {
        super(listValue);
        Collection<Integer> collection;
        this.saveValue = listValue;
        String[] stringArray = this.saveValue.getValues();
        ListElement listElement = this;
        boolean bl = false;
        String[] stringArray2 = stringArray;
        Collection collection2 = new ArrayList(stringArray.length);
        boolean bl2 = false;
        String[] stringArray3 = stringArray2;
        int n = stringArray3.length;
        for (int i = 0; i < n; ++i) {
            String string;
            String string2 = string = stringArray3[i];
            collection = collection2;
            boolean bl3 = false;
            Integer n2 = -Fonts.posterama40.getStringWidth(string2);
            collection.add(n2);
        }
        collection = (List)collection2;
        Integer n3 = (Integer)CollectionsKt.firstOrNull((List)CollectionsKt.sorted((Iterable)collection));
        listElement.maxSubWidth = -(n3 != null ? n3 : 0) + 20;
    }

    @Override
    public float drawElement(int n, int n2, float f, float f2, float f3, Color color, Color color2) {
        this.expandHeight = AnimHelperKt.animSmooth(this.expandHeight, this.expansion ? 16.0f * ((float)this.saveValue.getValues().length - 1.0f) : 0.0f, 0.5f);
        float f4 = this.expandHeight / (16.0f * ((float)this.saveValue.getValues().length - 1.0f));
        Fonts.posterama40.drawString(this.getValue().getName(), f + 10.0f, f2 + 10.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
        RenderUtils.drawRoundedRect(f + f3 - 18.0f - (float)this.maxSubWidth, f2 + 2.0f, f + f3 - 10.0f, f2 + 18.0f + this.expandHeight, 4.0f, ColorManager.INSTANCE.getButton().getRGB());
        GlStateManager.func_179117_G();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(f + f3 - 20.0f), (float)(f2 + 10.0f), (float)0.0f);
        GL11.glPushMatrix();
        GL11.glRotatef((float)(180.0f * f4), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawImage(expanding, -4, -4, 8, 8);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        Fonts.posterama40.drawString((String)this.getValue().get(), f + f3 - 14.0f - (float)this.maxSubWidth, f2 + 6.0f, new Color(26, 26, 26).getRGB());
        GL11.glPushMatrix();
        GlStateManager.func_179109_b((float)(f + f3 - 14.0f - (float)this.maxSubWidth), (float)(f2 + 7.0f), (float)0.0f);
        GlStateManager.func_179152_a((float)f4, (float)f4, (float)f4);
        float f5 = 0.0f;
        if (f4 > 0.0f) {
            for (String string : this.getUnusedValues()) {
                Fonts.posterama40.drawString(string, 0.0f, (16.0f + f5) * f4 - 1.0f, new Color(0.5f, 0.5f, 0.5f, RangesKt.coerceIn((float)f4, (float)0.0f, (float)1.0f)).getRGB());
                f5 += 16.0f;
            }
        }
        GL11.glPopMatrix();
        this.setValueHeight(20.0f + this.expandHeight);
        return this.getValueHeight();
    }

    public final List getUnusedValues() {
        String[] stringArray = this.saveValue.getValues();
        boolean bl = false;
        String[] stringArray2 = stringArray;
        Collection collection = new ArrayList();
        boolean bl2 = false;
        String[] stringArray3 = stringArray2;
        int n = stringArray3.length;
        for (int i = 0; i < n; ++i) {
            String string;
            String string2 = string = stringArray3[i];
            boolean bl3 = false;
            if (!(string2.equals((String)this.getValue().get()) ^ true)) continue;
            collection.add(string);
        }
        return (List)collection;
    }

    public final ListValue getSaveValue() {
        return this.saveValue;
    }

    @Override
    public void onClick(int n, int n2, float f, float f2, float f3) {
        if (this.isDisplayable() && MouseUtils.mouseWithinBounds(n, n2, f, f2 + 2.0f, f + f3, f2 + 18.0f)) {
            boolean bl = this.expansion = !this.expansion;
        }
        if (this.expansion) {
            float f4 = 0.0f;
            for (String string : this.getUnusedValues()) {
                if (MouseUtils.mouseWithinBounds(n, n2, f + f3 - 14.0f - (float)this.maxSubWidth, f2 + 18.0f + f4, f + f3 - 10.0f, f2 + 34.0f + f4)) {
                    this.getValue().set(string);
                    this.expansion = false;
                    break;
                }
                f4 += 16.0f;
            }
        }
    }

    public static final class Companion {
        private Companion() {
        }

        public final IResourceLocation getExpanding() {
            return ListElement.access$getExpanding$cp();
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

