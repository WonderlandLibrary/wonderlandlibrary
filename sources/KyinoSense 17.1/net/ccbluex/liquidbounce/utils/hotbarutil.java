/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/utils/hotbarutil;", "", "()V", "size", "", "getSize", "()F", "setSize", "(F)V", "translate", "Lnet/ccbluex/liquidbounce/utils/render/Translate;", "getTranslate", "()Lnet/ccbluex/liquidbounce/utils/render/Translate;", "renderHotbarItem", "", "index", "", "xPos", "yPos", "partialTicks", "KyinoClient"})
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
        ItemStack itemStack = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a[index];
        if (itemStack != null) {
            float lvt_7_1_ = (float)itemStack.field_77992_b - partialTicks;
            if (lvt_7_1_ > 0.0f) {
                GlStateManager.func_179094_E();
                float lvt_8_1_ = 1.0f + lvt_7_1_ / 5.0f;
                GlStateManager.func_179109_b((float)(xPos + (float)8), (float)(yPos + (float)12), (float)0.0f);
                GlStateManager.func_179152_a((float)(1.0f / lvt_8_1_), (float)((lvt_8_1_ + 1.0f) / 2.0f), (float)1.0f);
                GlStateManager.func_179109_b((float)(-(xPos + (float)8)), (float)(-(yPos + (float)12)), (float)0.0f);
            }
            Minecraft minecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_175599_af().func_180450_b(itemStack, (int)xPos, (int)yPos);
            if (lvt_7_1_ > 0.0f) {
                GlStateManager.func_179121_F();
            }
            Minecraft minecraft2 = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_175599_af().func_175030_a((FontRenderer)Fonts.font35, itemStack, (int)xPos, (int)yPos);
        }
    }
}

