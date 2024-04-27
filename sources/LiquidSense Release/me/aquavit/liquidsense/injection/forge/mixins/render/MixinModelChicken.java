package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.LiquidSense;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelChicken.class)
@SideOnly(Side.CLIENT)
public class MixinModelChicken extends ModelBase {

    @Shadow
    public ModelRenderer head;

    @Shadow
    public ModelRenderer body;

    @Shadow
    public ModelRenderer rightLeg;

    @Shadow
    public ModelRenderer leftLeg;

    @Shadow
    public ModelRenderer rightWing;

    @Shadow
    public ModelRenderer leftWing;

    @Shadow
    public ModelRenderer bill;

    @Shadow
    public ModelRenderer chin;

    /**
     * @author  CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public void render(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_) {
        this.setRotationAngles(p_render_2_, p_render_3_, p_render_4_, p_render_5_, p_render_6_, p_render_7_, p_render_1_);
        if (this.isChild) {
            float lvt_8_1_ = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * p_render_7_, 2.0F * p_render_7_);
            this.head.render(p_render_7_);
            this.bill.render(p_render_7_);
            this.chin.render(p_render_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / lvt_8_1_, 1.0F / lvt_8_1_, 1.0F / lvt_8_1_);
            GlStateManager.translate(0.0F, 24.0F * p_render_7_, 0.0F);
            this.body.render(p_render_7_);
            this.rightLeg.render(p_render_7_);
            this.leftLeg.render(p_render_7_);
            this.rightWing.render(p_render_7_);
            this.leftWing.render(p_render_7_);
            GlStateManager.popMatrix();
        } else {
            final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

            if (rc.getState() && RenderChanger.bigHeadsValue.get()) {
                GL11.glPushMatrix();
                GL11.glScaled(1.2, 1.2, 1.2);
                this.head.render(p_render_7_);
                this.bill.render(p_render_7_);
                this.chin.render(p_render_7_);
                GL11.glPopMatrix();
            } else {
                this.head.render(p_render_7_);
                this.bill.render(p_render_7_);
                this.chin.render(p_render_7_);
            }
            this.body.render(p_render_7_);
            this.rightLeg.render(p_render_7_);
            this.leftLeg.render(p_render_7_);
            this.rightWing.render(p_render_7_);
            this.leftWing.render(p_render_7_);
        }

    }
}
