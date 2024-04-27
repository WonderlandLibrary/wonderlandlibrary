/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Hotbar", category=ModuleCategory.RENDER, description="233")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020\u000f2\u0006\u0010&\u001a\u00020\u000fJ\u001e\u0010'\u001a\u00020\"2\u0006\u0010(\u001a\u00020\u000f2\u0006\u0010)\u001a\u00020\u000f2\u0006\u0010*\u001a\u00020\u000fJ(\u0010+\u001a\u00020\"2\u0006\u0010%\u001a\u00020\u000f2\u0006\u0010&\u001a\u00020\u000f2\b\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010(\u001a\u00020\u000fJP\u0010.\u001a\u00020\"2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020\u000f2\u0006\u00102\u001a\u00020\u000f2\u0006\u00103\u001a\u00020\u000f2\u0006\u00104\u001a\u00020\u000f2\u0006\u00105\u001a\u00020\u000f2\u0006\u00106\u001a\u00020\u000f2\u0006\u00107\u001a\u00020\u000f2\u0006\u00108\u001a\u00020\u000fH\u0002J\u000e\u00109\u001a\u00020\u000f2\u0006\u00101\u001a\u00020\u000fJ\u0010\u0010:\u001a\u00020\"2\u0006\u0010;\u001a\u00020<H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R&\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f8B@BX\u0082\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001c\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u001f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u000b\u00a8\u0006="}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/HotbarSettings;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "BlurAmount", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "BlurValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "ItemCountValue", "ItemFontValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getItemFontValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "easeAnimation", "Lnet/ccbluex/liquidbounce/utils/render/Animation;", "value", "", "easingValue", "getEasingValue", "()I", "setEasingValue", "(I)V", "hotbarAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getHotbarAlphaValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hotbarAnimOrderValue", "hotbarAnimSpeedValue", "hotbarAnimTypeValue", "hotbarEaseValue", "getHotbarEaseValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "hotbarValue", "getHotbarValue", "HotbarDurabilityOverlay", "", "stack", "Lnet/minecraft/item/ItemStack;", "xPosition", "yPosition", "HotbarItems", "index", "xPos", "yPos", "HotbarTextOverlay", "text", "", "barDraw", "renderer", "Lnet/minecraft/client/renderer/BufferBuilder;", "x", "y", "width", "height", "red", "green", "blue", "alpha", "getHotbarEasePos", "onRender2D", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "LiKingSense"})
public final class HotbarSettings
extends Module {
    @NotNull
    private final ListValue hotbarValue;
    @NotNull
    private final IntegerValue hotbarAlphaValue;
    @NotNull
    private final BoolValue hotbarEaseValue;
    private final BoolValue BlurValue;
    private final FloatValue BlurAmount;
    private final BoolValue ItemCountValue;
    @NotNull
    private final ListValue ItemFontValue;
    private final IntegerValue hotbarAnimSpeedValue;
    private final ListValue hotbarAnimTypeValue;
    private final ListValue hotbarAnimOrderValue;
    private Animation easeAnimation;
    private int easingValue;

    @NotNull
    public final ListValue getHotbarValue() {
        return this.hotbarValue;
    }

    @NotNull
    public final IntegerValue getHotbarAlphaValue() {
        return this.hotbarAlphaValue;
    }

    @NotNull
    public final BoolValue getHotbarEaseValue() {
        return this.hotbarEaseValue;
    }

    @NotNull
    public final ListValue getItemFontValue() {
        return this.ItemFontValue;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl82 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final void HotbarItems(int index, int xPos, int yPos) {
        IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
        if (iEntity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
        }
        EntityPlayer entityplayer = (EntityPlayer)iEntity;
        Object object = entityplayer.field_71071_by.field_70462_a.get(index);
        Intrinsics.checkExpressionValueIsNotNull((Object)object, (String)"entityplayer.inventory.mainInventory[index]");
        ItemStack itemstack = (ItemStack)object;
        if (itemstack != null) {
            float f = (float)itemstack.func_190921_D() - MinecraftInstance.mc.getTimer().getRenderPartialTicks();
            if (f > 0.0f) {
                GlStateManager.func_179094_E();
                float f1 = 1.0f + f / 5.0f;
                GlStateManager.func_179109_b((float)(xPos + 8), (float)(yPos + 12), (float)0.0f);
                GlStateManager.func_179152_a((float)(1.0f / f1), (float)((f1 + 1.0f) / 2.0f), (float)1.0f);
                GlStateManager.func_179109_b((float)(-((float)(xPos + 8))), (float)(-((float)(yPos + 12))), (float)0.0f);
            }
            Minecraft minecraft = MinecraftInstance.mc2;
            Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"mc2");
            minecraft.func_175599_af().func_180450_b(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.func_179121_F();
            }
            this.HotbarDurabilityOverlay(itemstack, xPos, yPos);
            IFontRenderer fontVal = MinecraftInstance.mc.getFontRendererObj();
            if (Intrinsics.areEqual((Object)((String)this.ItemFontValue.get()), (Object)"MiSans")) {
                IFontRenderer iFontRenderer = Fonts.posterama35;
                Intrinsics.checkExpressionValueIsNotNull((Object)iFontRenderer, (String)"Fonts.posterama35");
                fontVal = iFontRenderer;
            } else if (Intrinsics.areEqual((Object)((String)this.ItemFontValue.get()), (Object)"Minecraft")) {
                fontVal = MinecraftInstance.mc.getFontRendererObj();
            }
            this.HotbarTextOverlay(xPos, yPos, null, index);
        }
    }

    /*
     * Exception decompiling
     */
    public final void HotbarDurabilityOverlay(@Nullable ItemStack stack, int xPosition, int yPosition) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl94 : RETURN - null : trying to set 0 previously set to 8
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final void HotbarTextOverlay(int xPosition, int yPosition, @Nullable String text, int index) {
        IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
        if (iEntity == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
        }
        EntityPlayer entityplayer = (EntityPlayer)iEntity;
        Object object = entityplayer.field_71071_by.field_70462_a.get(index);
        Intrinsics.checkExpressionValueIsNotNull((Object)object, (String)"entityplayer.inventory.mainInventory[index]");
        ItemStack stack = (ItemStack)object;
        if (stack != null && (stack.field_77994_a != 1 || text != null)) {
            String s;
            Color colour = null;
            String string = text;
            if (string == null) {
                string = s = String.valueOf(stack.field_77994_a);
            }
            if (((Boolean)this.ItemCountValue.get()).booleanValue()) {
                if (text == null && stack.field_77994_a < 1) {
                    s = String.valueOf(stack.field_77994_a);
                }
                if (stack.field_77994_a >= 46) {
                    colour = Color.green;
                } else if (stack.field_77994_a <= 45 && stack.field_77994_a > 20) {
                    colour = Color.orange;
                } else if (stack.field_77994_a <= 20) {
                    colour = Color.red;
                }
            } else {
                if (text == null && stack.field_77994_a < 1) {
                    s = WEnumChatFormatting.RED.toString() + String.valueOf(stack.field_77994_a);
                }
                colour = Color.white;
            }
            IFontRenderer fontVal = MinecraftInstance.mc.getFontRendererObj();
            if (Intrinsics.areEqual((Object)((String)this.ItemFontValue.get()), (Object)"MiSans")) {
                IFontRenderer iFontRenderer = Fonts.posterama35;
                Intrinsics.checkExpressionValueIsNotNull((Object)iFontRenderer, (String)"Fonts.posterama35");
                fontVal = iFontRenderer;
            } else if (Intrinsics.areEqual((Object)((String)this.ItemFontValue.get()), (Object)"Minecraft")) {
                fontVal = MinecraftInstance.mc.getFontRendererObj();
            }
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            GlStateManager.func_179084_k();
            fontVal.drawStringWithShadow(s, xPosition + 19 - 2 - fontVal.getStringWidth(s), yPosition + 6 + 3, colour.getRGB());
            GlStateManager.func_179145_e();
            GlStateManager.func_179126_j();
        }
    }

    private final void barDraw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        renderer.func_181662_b((double)(x + 0), (double)(y + 0), 0.0).func_181669_b(red, green, blue, alpha).func_181675_d();
        renderer.func_181662_b((double)(x + 0), (double)(y + height), 0.0).func_181669_b(red, green, blue, alpha).func_181675_d();
        renderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181669_b(red, green, blue, alpha).func_181675_d();
        renderer.func_181662_b((double)(x + width), (double)(y + 0), 0.0).func_181669_b(red, green, blue, alpha).func_181675_d();
        Tessellator.func_178181_a().func_78381_a();
    }

    private final int getEasingValue() {
        if (this.easeAnimation != null) {
            this.easingValue = (int)this.easeAnimation.getValue();
            if (this.easeAnimation.getState() == Animation.EnumAnimationState.STOPPED) {
                this.easeAnimation = null;
            }
        }
        return this.easingValue;
    }

    private final void setEasingValue(int value) {
        int hotbarSpeed = ((Number)this.hotbarAnimSpeedValue.get()).intValue();
        if (Intrinsics.areEqual((Object)((String)this.hotbarValue.get()), (Object)"Dock")) {
            hotbarSpeed = 4;
        }
        if (this.easeAnimation == null || this.easeAnimation != null && this.easeAnimation.getTo() != (double)value) {
            this.easeAnimation = new Animation(EaseUtils.EnumEasingType.valueOf((String)this.hotbarAnimTypeValue.get()), EaseUtils.EnumEasingOrder.valueOf((String)this.hotbarAnimOrderValue.get()), this.easingValue, value, (long)hotbarSpeed * 30L).start();
        }
    }

    public final int getHotbarEasePos(int x) {
        if (!((Boolean)this.hotbarEaseValue.get()).booleanValue()) {
            return x;
        }
        this.setEasingValue(x);
        return this.getEasingValue();
    }

    /*
     * Exception decompiling
     */
    public HotbarSettings() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl74 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static final /* synthetic */ Minecraft access$getMinecraft$p$s1046033730() {
        return MinecraftInstance.minecraft;
    }
}

