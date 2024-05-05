/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.ColorManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.ValueElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u001d2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J@\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0016J0\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0007H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/impl/ListElement;", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/ValueElement;", "", "saveValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "(Lnet/ccbluex/liquidbounce/value/ListValue;)V", "expandHeight", "", "expansion", "", "maxSubWidth", "getSaveValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "unusedValues", "", "getUnusedValues", "()Ljava/util/List;", "drawElement", "mouseX", "", "mouseY", "x", "y", "width", "bgColor", "Ljava/awt/Color;", "accentColor", "onClick", "", "Companion", "KyinoClient"})
public final class ListElement
extends ValueElement<String> {
    private float expandHeight;
    private boolean expansion;
    private final float maxSubWidth;
    @NotNull
    private final ListValue saveValue;
    @NotNull
    private static final ResourceLocation expanding;
    public static final Companion Companion;

    @Override
    public float drawElement(int mouseX, int mouseY, float x, float y, float width, @NotNull Color bgColor, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(bgColor, "bgColor");
        Intrinsics.checkParameterIsNotNull(accentColor, "accentColor");
        this.expandHeight = AnimHelperKt.animSmooth(this.expandHeight, this.expansion ? 16.0f * ((float)this.saveValue.getValues().length - 1.0f) : 0.0f, 0.5f);
        float percent = this.expandHeight / (16.0f * ((float)this.saveValue.getValues().length - 1.0f));
        Fonts.font40.drawString(this.getValue().getName(), x + 10.0f, y + 10.0f - (float)Fonts.font40.field_78288_b / 2.0f + 2.0f, -1);
        RenderUtils.originalRoundedRect(x + width - 18.0f - this.maxSubWidth, y + 2.0f, x + width - 10.0f, y + 18.0f + this.expandHeight, 4.0f, ColorManager.INSTANCE.getButton().getRGB());
        GlStateManager.func_179117_G();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(x + width - 20.0f), (float)(y + 10.0f), (float)0.0f);
        GL11.glPushMatrix();
        GL11.glRotatef((float)(180.0f * percent), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawImage(expanding, -4, -4, 8, 8);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        Fonts.font40.drawString((String)this.getValue().get(), x + width - 14.0f - this.maxSubWidth, y + 6.0f, -1);
        GL11.glPushMatrix();
        GlStateManager.func_179109_b((float)(x + width - 14.0f - this.maxSubWidth), (float)(y + 7.0f), (float)0.0f);
        GlStateManager.func_179152_a((float)percent, (float)percent, (float)percent);
        float vertHeight = 0.0f;
        if (percent > 0.0f) {
            for (String subV : this.getUnusedValues()) {
                Fonts.font40.drawString(subV, 0.0f, (16.0f + vertHeight) * percent - 1.0f, new Color(0.5f, 0.5f, 0.5f, RangesKt.coerceIn(percent, 0.0f, 1.0f)).getRGB());
                vertHeight += 16.0f;
            }
        }
        GL11.glPopMatrix();
        this.setValueHeight(20.0f + this.expandHeight);
        return this.getValueHeight();
    }

    @Override
    public void onClick(int mouseX, int mouseY, float x, float y, float width) {
        if (this.isDisplayable() && MouseUtils.mouseWithinBounds(mouseX, mouseY, x, y + 2.0f, x + width, y + 18.0f)) {
            boolean bl = this.expansion = !this.expansion;
        }
        if (this.expansion) {
            float vertHeight = 0.0f;
            for (String subV : this.getUnusedValues()) {
                if (MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - 14.0f - this.maxSubWidth, y + 18.0f + vertHeight, x + width - 10.0f, y + 34.0f + vertHeight)) {
                    this.getValue().set(subV);
                    this.expansion = false;
                    break;
                }
                vertHeight += 16.0f;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<String> getUnusedValues() {
        void $this$filterTo$iv$iv;
        String[] $this$filter$iv = this.saveValue.getValues();
        boolean $i$f$filter = false;
        String[] stringArray = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        void var6_6 = $this$filterTo$iv$iv;
        int n = ((void)var6_6).length;
        for (int i = 0; i < n; ++i) {
            void element$iv$iv;
            void it = element$iv$iv = var6_6[i];
            boolean bl = false;
            if (!(Intrinsics.areEqual(it, (String)this.getValue().get()) ^ true)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    @NotNull
    public final ListValue getSaveValue() {
        return this.saveValue;
    }

    /*
     * WARNING - void declaration
     */
    public ListElement(@NotNull ListValue saveValue) {
        Collection<Integer> collection;
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(saveValue, "saveValue");
        super(saveValue);
        this.saveValue = saveValue;
        String[] stringArray = this.saveValue.getValues();
        ListElement listElement = this;
        boolean $i$f$map = false;
        void var4_5 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var7_8 = $this$mapTo$iv$iv;
        int n = ((void)var7_8).length;
        for (int i = 0; i < n; ++i) {
            void it;
            void item$iv$iv;
            void var11_12 = item$iv$iv = var7_8[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Integer n2 = -Fonts.font40.func_78256_a((String)it);
            collection.add(n2);
        }
        collection = (List)destination$iv$iv;
        Number number = (Integer)CollectionsKt.firstOrNull(CollectionsKt.sorted((Iterable)collection));
        if (number == null) {
            number = Float.valueOf(0.0f);
        }
        listElement.maxSubWidth = (float)(-((Object)number).hashCode()) + 20.0f;
    }

    static {
        Companion = new Companion(null);
        expanding = new ResourceLocation("liquidbounce/expand.png");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/impl/ListElement$Companion;", "", "()V", "expanding", "Lnet/minecraft/util/ResourceLocation;", "getExpanding", "()Lnet/minecraft/util/ResourceLocation;", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final ResourceLocation getExpanding() {
            return expanding;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

