package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.module.modules.misc.Animations;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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

import java.util.UUID;

@Mixin(ModelBiped.class)
@SideOnly(Side.CLIENT)
public class MixinModelBiped extends ModelBase {

    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public int heldItemRight;

    @Shadow
    public ModelRenderer bipedHead;

    @Shadow
    public ModelRenderer bipedLeftArm;

    @Shadow
    public ModelRenderer bipedRightLeg;

    @Shadow
    public ModelRenderer bipedLeftLeg;

    @Shadow
    public ModelRenderer bipedBody;

    @Shadow
    public ModelRenderer bipedHeadwear;

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public void render(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_) {
        this.setRotationAngles(p_render_2_, p_render_3_, p_render_4_, p_render_5_, p_render_6_, p_render_7_, p_render_1_);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float lvt_8_1_ = 2.0F;
            GlStateManager.scale(1.5F / lvt_8_1_, 1.5F / lvt_8_1_, 1.5F / lvt_8_1_);
            GlStateManager.translate(0.0F, 16.0F * p_render_7_, 0.0F);
            this.bipedHead.render(p_render_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / lvt_8_1_, 1.0F / lvt_8_1_, 1.0F / lvt_8_1_);
            GlStateManager.translate(0.0F, 24.0F * p_render_7_, 0.0F);
        } else {
            if (p_render_1_.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

            if (rc.getState() && RenderChanger.bigHeadsValue.get()) {
                GL11.glPushMatrix();
                GL11.glScaled(1.5, 1.5, 1.5);
                this.bipedHead.render(p_render_7_);
                GL11.glPopMatrix();
            } else {
                this.bipedHead.render(p_render_7_);
            }

            this.bipedHead.render(p_render_7_);
        }
        this.bipedBody.render(p_render_7_);
        this.bipedRightArm.render(p_render_7_);
        this.bipedLeftArm.render(p_render_7_);
        this.bipedRightLeg.render(p_render_7_);
        this.bipedLeftLeg.render(p_render_7_);
        this.bipedHeadwear.render(p_render_7_);

        GlStateManager.popMatrix();
    }

    @Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F"))
    private void revertSwordAnimation(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_, CallbackInfo callbackInfo) {
        Aura killAura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);
        UUID uuid = p_setRotationAngles_7_.getUniqueID();
        EntityPlayer entityPlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(uuid);

        if (entityPlayer == Minecraft.getMinecraft().thePlayer && (killAura.getBlockingStatus() || Minecraft.getMinecraft().thePlayer.isBlocking())) {
            switch (Animations.GodMode.get()) {
                case "1.7":
                    heldItemRight = 3;
                    this.bipedRightArm.rotateAngleY = 0.0F;
                    break;
                case "1.8":
                    heldItemRight = 3;
                    break;
                case "Dev":
                    heldItemRight = 3;
                    this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * (float) this.heldItemRight) / 2;
                    this.bipedRightArm.rotateAngleY = -0.5235988F / 2;
                    break;
                case "Test":
                    heldItemRight = 3;
                    this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
                    this.bipedRightArm.rotateAngleY = -0.5235988F;
                    break;
                case "Custom":
                    heldItemRight = 3;
                    bipedRightArm.rotateAngleX = Animations.GodCustomX.get();
                    bipedRightArm.rotateAngleY = Animations.GodCustomY.get();
                    bipedRightArm.rotateAngleZ = Animations.GodCustomZ.get();
                    break;
            }
        }
    }

}