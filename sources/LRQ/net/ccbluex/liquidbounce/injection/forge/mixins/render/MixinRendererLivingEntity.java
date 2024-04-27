/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.uk.hexeption.utils.OutlineUtils
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import co.uk.hexeption.utils.OutlineUtils;
import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImplKt;
import net.ccbluex.liquidbounce.injection.forge.mixins.render.MixinRender;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={RenderLivingBase.class})
public abstract class MixinRendererLivingEntity
extends MixinRender {
    @Shadow
    protected ModelBase field_77045_g;

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private <T extends EntityLivingBase> void injectChamsPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)) {
            GL11.glEnable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    private <T extends EntityLivingBase> void injectChamsPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)) {
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
        }
    }

    @Inject(method={"canRenderName"}, at={@At(value="HEAD")}, cancellable=true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || LiquidBounce.moduleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entity), false)) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Overwrite
    protected <T extends EntityLivingBase> void func_77036_a(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean semiVisible;
        boolean visible = !entitylivingbaseIn.func_82150_aj();
        TrueSight trueSight = (TrueSight)LiquidBounce.moduleManager.getModule(TrueSight.class);
        boolean bl = semiVisible = !visible && (!entitylivingbaseIn.func_98034_c((EntityPlayer)Minecraft.func_71410_x().field_71439_g) || trueSight.getState() && (Boolean)trueSight.getEntitiesValue().get() != false);
        if (visible || semiVisible) {
            ESP esp;
            if (!this.func_180548_c(entitylivingbaseIn)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.15f);
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)3042);
                GlStateManager.func_179112_b((int)770, (int)771);
                GlStateManager.func_179092_a((int)516, (float)0.003921569f);
            }
            if ((esp = (ESP)LiquidBounce.moduleManager.getModule(ESP.class)).getState() && EntityUtils.isSelected(EntityLivingBaseImplKt.wrap(entitylivingbaseIn), false)) {
                Minecraft mc = Minecraft.func_71410_x();
                boolean fancyGraphics = mc.field_71474_y.field_74347_j;
                mc.field_71474_y.field_74347_j = false;
                float gamma = mc.field_71474_y.field_74333_Y;
                mc.field_71474_y.field_74333_Y = 100000.0f;
                switch (((String)esp.modeValue.get()).toLowerCase()) {
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        RenderUtils.glColor(esp.getColor(EntityLivingBaseImplKt.wrap(entitylivingbaseIn)));
                        GL11.glLineWidth((float)((Float)esp.wireframeWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                    case "outline": {
                        ClientUtils.disableFastRender();
                        GlStateManager.func_179117_G();
                        Color color = esp.getColor(EntityLivingBaseImplKt.wrap(entitylivingbaseIn));
                        OutlineUtils.setColor((Color)color);
                        OutlineUtils.renderOne((float)((Float)esp.outlineWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor((Color)color);
                        OutlineUtils.renderTwo();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor((Color)color);
                        OutlineUtils.renderThree();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor((Color)color);
                        OutlineUtils.renderFour((Color)color);
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor((Color)color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor((Color)Color.WHITE);
                    }
                }
                mc.field_71474_y.field_74347_j = fancyGraphics;
                mc.field_71474_y.field_74333_Y = gamma;
            }
            this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            if (semiVisible) {
                GlStateManager.func_179084_k();
                GlStateManager.func_179092_a((int)516, (float)0.1f);
                GlStateManager.func_179121_F();
                GlStateManager.func_179132_a((boolean)true);
            }
        }
    }
}

