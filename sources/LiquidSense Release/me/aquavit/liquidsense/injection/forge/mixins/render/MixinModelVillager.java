package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelVillager.class)
@SideOnly(Side.CLIENT)
public class MixinModelVillager extends ModelBase {

    @Shadow
    public ModelRenderer villagerHead;
    @Shadow
    public ModelRenderer villagerBody;
    @Shadow
    public ModelRenderer villagerArms;
    @Shadow
    public ModelRenderer rightVillagerLeg;
    @Shadow
    public ModelRenderer leftVillagerLeg;

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
            GL11.glScaled(1.5, 1.5, 1.5);
            this.villagerHead.render(p_render_7_);
            GL11.glPopMatrix();
        } else {
            this.villagerHead.render(p_render_7_);
        }
        this.villagerBody.render(p_render_7_);
        this.rightVillagerLeg.render(p_render_7_);
        this.leftVillagerLeg.render(p_render_7_);
        this.villagerArms.render(p_render_7_);
    }
}

