package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelCreeper.class)
@SideOnly(Side.CLIENT)
public class MixinModelCreeper extends ModelBase {

    @Shadow
    public ModelRenderer head;

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
            this.head.render(p_render_7_);
            GL11.glPopMatrix();
        } else {
            this.head.render(p_render_7_);
        }
        this.head.render(p_render_7_);
        this.body.render(p_render_7_);
        this.leg1.render(p_render_7_);
        this.leg2.render(p_render_7_);
        this.leg3.render(p_render_7_);
        this.leg4.render(p_render_7_);
    }
}

