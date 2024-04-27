package me.aquavit.liquidsense.injection.forge.mixins.item;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.EnchantEffect;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(RenderItem.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRenderItem implements IResourceManagerReloadListener {
    @Shadow
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    @Shadow
    private TextureManager textureManager;

    @Shadow
    public abstract void onResourceManagerReload(IResourceManager iResourceManager);

    @Shadow
    protected abstract void renderModel(IBakedModel iBakedModel, int p_renderModel_2_);

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    private void renderEffect(IBakedModel iBakedModel) {
        final EnchantEffect effect = (EnchantEffect) LiquidSense.moduleManager.get(EnchantEffect.class);
        final Color color = effect.getRainbow().get() ? ColorUtils.rainbow() : new Color(effect.getRedValue().get(),effect.getGreenValue().get(),effect.getBlueValue().get(),effect.getalphaValue().get());
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        if(effect.getState())
            this.renderModel(iBakedModel, color.getRGB());
        else
            this.renderModel(iBakedModel, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        if(effect.getState())
            this.renderModel(iBakedModel, color.getRGB());
        else
            this.renderModel(iBakedModel, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
    }


}
