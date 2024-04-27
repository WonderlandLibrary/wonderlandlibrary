package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.LiquidSense;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelSpider.class)
@SideOnly(Side.CLIENT)
public class MixinModelSpider extends ModelBase {

    @Shadow
    public ModelRenderer spiderHead;
    @Shadow
    public ModelRenderer spiderNeck;
    @Shadow
    public ModelRenderer spiderBody;
    @Shadow
    public ModelRenderer spiderLeg1;
    @Shadow
    public ModelRenderer spiderLeg2;
    @Shadow
    public ModelRenderer spiderLeg3;
    @Shadow
    public ModelRenderer spiderLeg4;
    @Shadow
    public ModelRenderer spiderLeg5;
    @Shadow
    public ModelRenderer spiderLeg6;
    @Shadow
    public ModelRenderer spiderLeg7;
    @Shadow
    public ModelRenderer spiderLeg8;

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public void render(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_, float p_render_6_, float p_render_7_) {
        this.setRotationAngles(p_render_2_, p_render_3_, p_render_4_, p_render_5_, p_render_6_, p_render_7_, p_render_1_);
        final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

        if (rc.getState() && RenderChanger.bigHeadsValue.get()) {
            GL11.glPushMatrix();
            GL11.glScaled(1.2, 1.2, 1.2);
            this.spiderHead.render(p_render_7_);
            GL11.glPopMatrix();
        } else {
            this.spiderHead.render(p_render_7_);
        }
        this.spiderNeck.render(p_render_7_);
        this.spiderBody.render(p_render_7_);
        this.spiderLeg1.render(p_render_7_);
        this.spiderLeg2.render(p_render_7_);
        this.spiderLeg3.render(p_render_7_);
        this.spiderLeg4.render(p_render_7_);
        this.spiderLeg5.render(p_render_7_);
        this.spiderLeg6.render(p_render_7_);
        this.spiderLeg7.render(p_render_7_);
        this.spiderLeg8.render(p_render_7_);
    }
}

