package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ModelRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinModelRenderer{

    @Shadow
    public boolean showModel;
    @Shadow
    public boolean isHidden;
    @Shadow
    private boolean compiled;
    @Shadow
    public abstract void compileDisplayList(float p_compileDisplayList_1_); //
    @Shadow
    public float rotateAngleX;
    @Shadow
    public float rotateAngleY;
    @Shadow
    public float rotateAngleZ;
    @Shadow
    private int displayList;
    @Shadow
    public float rotationPointX;
    @Shadow
    public float rotationPointY;
    @Shadow
    public float rotationPointZ;
    @Shadow
    public float offsetX;
    @Shadow
    public float offsetY;
    @Shadow
    public float offsetZ;
    @Shadow
    public List<ModelRenderer> childModels;

    @Overwrite
    public void render(float p_render_1_) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(p_render_1_);
            }

            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
            int i;
            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        for(i = 0; i < this.childModels.size(); ++i) {
                            ((ModelRenderer)this.childModels.get(i)).render(p_render_1_);
                        }
                    }
                } else {
                    GlStateManager.translate(this.rotationPointX * p_render_1_, this.rotationPointY * p_render_1_, this.rotationPointZ * p_render_1_);
                    GlStateManager.callList(this.displayList);
                    if (this.childModels != null) {
                        for(i = 0; i < this.childModels.size(); ++i) {
                            ((ModelRenderer)this.childModels.get(i)).render(p_render_1_);
                        }
                    }

                    GlStateManager.translate(-this.rotationPointX * p_render_1_, -this.rotationPointY * p_render_1_, -this.rotationPointZ * p_render_1_);
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * p_render_1_, this.rotationPointY * p_render_1_, this.rotationPointZ * p_render_1_);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    for(i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer)this.childModels.get(i)).render(p_render_1_);
                    }
                }

                GlStateManager.popMatrix();
            }

            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
        }

    }
}
