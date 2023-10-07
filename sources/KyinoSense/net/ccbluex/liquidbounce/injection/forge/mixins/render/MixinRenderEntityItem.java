/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderEntityItem
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.resources.model.IBakedModel
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.report.liquidware.modules.render.ItemPhysics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderEntityItem.class})
public abstract class MixinRenderEntityItem
extends Render<EntityItem> {
    protected MixinRenderEntityItem(RenderManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Shadow
    protected abstract int func_177078_a(ItemStack var1);

    @Shadow
    protected abstract boolean shouldBob();

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private void injectChamsPre(CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        if (chams.getState() && ((Boolean)chams.getItemsValue().get()).booleanValue()) {
            GL11.glEnable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    private void injectChamsPost(CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        if (chams.getState() && ((Boolean)chams.getItemsValue().get()).booleanValue()) {
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
        }
    }

    @Overwrite
    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
        ItemStack itemstack = itemIn.func_92059_d();
        Item item = itemstack.func_77973_b();
        if (item == null) {
            return 0;
        }
        boolean flag = p_177077_9_.func_177556_c();
        int i = this.func_177078_a(itemstack);
        float f = 0.25f;
        float f1 = MathHelper.func_76126_a((float)(((float)itemIn.func_174872_o() + p_177077_8_) / 10.0f + itemIn.field_70290_d)) * 0.1f + 0.1f;
        if (LiquidBounce.moduleManager.getModule(ItemPhysics.class).getState()) {
            f1 = 0.0f;
        }
        float f2 = p_177077_9_.func_177552_f().func_181688_b((ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND).field_178363_d.y;
        GlStateManager.func_179109_b((float)((float)p_177077_2_), (float)((float)p_177077_4_ + f1 + 0.25f * f2), (float)((float)p_177077_6_));
        if (flag || this.field_76990_c.field_78733_k != null) {
            float f3 = (((float)itemIn.func_174872_o() + p_177077_8_) / 20.0f + itemIn.field_70290_d) * 57.295776f;
            if (LiquidBounce.moduleManager.getModule(ItemPhysics.class).getState()) {
                if (itemIn.field_70122_E) {
                    GL11.glRotatef((float)itemIn.field_70177_z, (float)0.0f, (float)1.0f, (float)0.0f);
                    GL11.glRotatef((float)(itemIn.field_70125_A + 90.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                } else {
                    for (int a = 0; a < 10; ++a) {
                        GL11.glRotatef((float)f3, (float)0.5f, (float)0.5f, (float)0.0f);
                    }
                }
            } else {
                GlStateManager.func_179114_b((float)f3, (float)0.0f, (float)1.0f, (float)0.0f);
            }
        }
        if (!flag) {
            float f6 = -0.0f * (float)(i - 1) * 0.5f;
            float f4 = -0.0f * (float)(i - 1) * 0.5f;
            float f5 = -0.046875f * (float)(i - 1) * 0.5f;
            GlStateManager.func_179109_b((float)f6, (float)f4, (float)f5);
        }
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        return i;
    }
}

