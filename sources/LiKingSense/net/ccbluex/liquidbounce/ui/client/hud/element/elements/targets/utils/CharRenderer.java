/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JV\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020!R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/CharRenderer;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "small", "", "(Z)V", "deFormat", "Ljava/text/DecimalFormat;", "moveX", "", "getMoveX", "()[F", "setMoveX", "([F)V", "moveY", "getMoveY", "setMoveY", "numberList", "", "", "getSmall", "()Z", "renderChar", "", "number", "orgX", "orgY", "initX", "initY", "scaleX", "scaleY", "shadow", "fontSpeed", "color", "", "LiKingSense"})
public final class CharRenderer
extends MinecraftInstance {
    @NotNull
    private float[] moveY;
    @NotNull
    private float[] moveX;
    private final List<String> numberList;
    private final DecimalFormat deFormat;
    private final boolean small;

    @NotNull
    public final float[] getMoveY() {
        return this.moveY;
    }

    public final void setMoveY(@NotNull float[] fArray) {
        Intrinsics.checkParameterIsNotNull((Object)fArray, (String)"<set-?>");
        this.moveY = fArray;
    }

    @NotNull
    public final float[] getMoveX() {
        return this.moveX;
    }

    public final void setMoveX(@NotNull float[] fArray) {
        Intrinsics.checkParameterIsNotNull((Object)fArray, (String)"<set-?>");
        this.moveX = fArray;
    }

    /*
     * WARNING - void declaration
     */
    public final float renderChar(float number, float orgX, float orgY, float initX, float initY, float scaleX, float scaleY, boolean shadow2, float fontSpeed, int color) {
        String reFormat = this.deFormat.format(number);
        IFontRenderer fontRend = this.small ? Fonts.posterama40 : Fonts.tenacity80;
        int delta = RenderUtils.deltaTime;
        IMinecraft iMinecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft, (String)"mc");
        IScaledResolution scaledRes = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
        int indexX = 0;
        int indexY = 0;
        float animX = 0.0f;
        float cutY = initY + (float)fontRend.getFontHeight() * 0.75f;
        GL11.glEnable((int)3089);
        RenderUtils.makeScissorBox(0.0f, orgY + initY - 4.0f * scaleY, scaledRes.getScaledWidth(), orgY + cutY - 4.0f * scaleY);
        String string = reFormat;
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"reFormat");
        String string2 = string;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] cArray = string3.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray, (String)"(this as java.lang.String).toCharArray()");
        for (char c : cArray) {
            this.moveX[indexX] = AnimationUtils.animate(animX, this.moveX[indexX], fontSpeed * 0.025f * (float)delta);
            animX = this.moveX[indexX];
            int pos = this.numberList.indexOf(String.valueOf(c));
            float expectAnim = ((float)fontRend.getFontHeight() + 2.0f) * (float)pos;
            float expectAnimMin = ((float)fontRend.getFontHeight() + 2.0f) * (float)(pos - 2);
            float expectAnimMax = ((float)fontRend.getFontHeight() + 2.0f) * (float)(pos + 2);
            if (pos >= 0) {
                this.moveY[indexY] = AnimationUtils.animate(expectAnim, this.moveY[indexY], fontSpeed * 0.02f * (float)delta);
                GL11.glTranslatef((float)0.0f, (float)(initY - this.moveY[indexY]), (float)0.0f);
                Iterable $this$forEachIndexed$iv = this.numberList;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void num;
                    int n = index$iv++;
                    boolean bl2 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n2 = n;
                    String string4 = (String)item$iv;
                    int index = n2;
                    boolean bl3 = false;
                    if (!(((float)fontRend.getFontHeight() + 2.0f) * (float)index >= expectAnimMin) || !(((float)fontRend.getFontHeight() + 2.0f) * (float)index <= expectAnimMax)) continue;
                    fontRend.drawString((String)num, initX + this.moveX[indexX], ((float)fontRend.getFontHeight() + 2.0f) * (float)index, color, shadow2);
                }
                GL11.glTranslatef((float)0.0f, (float)(-initY + this.moveY[indexY]), (float)0.0f);
            } else {
                this.moveY[indexY] = 0.0f;
                fontRend.drawString(String.valueOf(c), initX + this.moveX[indexX], initY, color, shadow2);
            }
            animX += (float)fontRend.getStringWidth(String.valueOf(c));
            int n = indexX;
            indexX = n + 1;
            ++indexY;
        }
        GL11.glDisable((int)3089);
        return animX;
    }

    public final boolean getSmall() {
        return this.small;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    public CharRenderer(boolean bl) {
        void var3_-1;
        this.small = small;
        this.moveY = new float[20];
        this.moveX = new float[20];
        this.numberList = CollectionsKt.listOf((Object[])new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."});
        this.deFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        bl = false;
        while (bl <= var3_-1) {
            void i;
            this.moveX[i] = 0.0f;
            this.moveY[i] = 0.0f;
            ++i;
        }
    }
}

