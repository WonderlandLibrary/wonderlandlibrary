package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.LiquidSense;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelQuadruped.class)
@SideOnly(Side.CLIENT)
public class MixinModelQuadruped extends ModelBase {

    @Shadow
    public ModelRenderer head = new ModelRenderer(this, 0, 0);

    @Shadow
    public ModelRenderer body;

    @Shadow
    public ModelRenderer leg1;

    @Shadow
    public ModelRenderer leg2;

    @Shadow
    public ModelRenderer leg3;

    @Shadow
    public ModelRenderer leg4;

    @Shadow
    protected float childYOffset = 8.0F;

    @Shadow
    protected float childZOffset = 4.0F;

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public void render(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_) {
        this.setRotationAngles(p_render_2_, p_render_3_, p_render_4_, p_render_5_, p_render_6_, p_render_7_, p_render_1_);
        if (this.isChild) {
            float lvt_8_1_ = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, this.childYOffset * p_render_7_, this.childZOffset * p_render_7_);
            this.head.render(p_render_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / lvt_8_1_, 1.0F / lvt_8_1_, 1.0F / lvt_8_1_);
            GlStateManager.translate(0.0F, 24.0F * p_render_7_, 0.0F);
            this.body.render(p_render_7_);
            this.leg1.render(p_render_7_);
            this.leg2.render(p_render_7_);
            this.leg3.render(p_render_7_);
            this.leg4.render(p_render_7_);
            GlStateManager.popMatrix();
        } else {
            final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

            if (rc.getState() && RenderChanger.bigHeadsValue.get()) {
                GL11.glPushMatrix();
                GL11.glScaled(1.2, 1.2, 1.2);
                this.head.render(p_render_7_);
                GL11.glPopMatrix();
            } else {
                this.head.render(p_render_7_);
            }
            this.body.render(p_render_7_);
            this.leg1.render(p_render_7_);
            this.leg2.render(p_render_7_);
            this.leg3.render(p_render_7_);
            this.leg4.render(p_render_7_);
        }

    }
}
