/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/utils/hotbarutil;", "", "()V", "size", "", "getSize", "()F", "setSize", "(F)V", "translate", "Lnet/ccbluex/liquidbounce/utils/Translate;", "getTranslate", "()Lnet/ccbluex/liquidbounce/utils/Translate;", "renderHotbarItem", "", "index", "", "xPos", "yPos", "partialTicks", "LiKingSense"})
public final class hotbarutil {
    @NotNull
    private final Translate translate = new Translate(0.0f, 0.0f);
    private float size = 1.0f;

    @NotNull
    public final Translate getTranslate() {
        return this.translate;
    }

    public final float getSize() {
        return this.size;
    }

    public final void setSize(float f) {
        this.size = f;
    }

    public final void renderHotbarItem(int index, float xPos, float yPos, float partialTicks) {
        IItemStack itemStack = MinecraftInstance.mc.getThePlayer().getInventory().getMainInventory().get(index);
        if (itemStack != null) {
            float lvt_7_1_ = (float)itemStack.getAnimationsToGo() - partialTicks;
            if (lvt_7_1_ > 0.0f) {
                GlStateManager.func_179094_E();
                float lvt_8_1_ = 1.0f + lvt_7_1_ / 5.0f;
                GlStateManager.func_179109_b((float)(xPos + (float)8), (float)(yPos + (float)12), (float)0.0f);
                GlStateManager.func_179152_a((float)(1.0f / lvt_8_1_), (float)((lvt_8_1_ + 1.0f) / 2.0f), (float)1.0f);
                GlStateManager.func_179109_b((float)(-(xPos + (float)8)), (float)(-(yPos + (float)12)), (float)0.0f);
            }
            float f = xPos - (float)7;
            float f2 = yPos - (float)7;
            IMinecraft iMinecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft, (String)"mc");
            RenderUtils.drawTexturedRect(f, f2, 30.0f, 30.0f, "hotbar", MinecraftInstance.classProvider.createScaledResolution(iMinecraft));
            float f3 = xPos - (float)7;
            float f4 = yPos - (float)7;
            IMinecraft iMinecraft2 = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull((Object)iMinecraft2, (String)"mc");
            RenderUtils.drawTexturedRect(f3, f4, 30.0f, 30.0f, "hotbar", MinecraftInstance.classProvider.createScaledResolution(iMinecraft2));
            MinecraftInstance.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)xPos, (int)yPos);
            if (lvt_7_1_ > 0.0f) {
                GlStateManager.func_179121_F();
            }
            IRenderItem iRenderItem = MinecraftInstance.mc.getRenderItem();
            IFontRenderer iFontRenderer = Fonts.font35;
            Intrinsics.checkExpressionValueIsNotNull((Object)iFontRenderer, (String)"Fonts.font35");
            iRenderItem.renderItemOverlays(iFontRenderer, itemStack, (int)xPos, (int)yPos);
        }
    }
}

