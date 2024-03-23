package moonsense.cosmetic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class CosmeticBase implements LayerRenderer<AbstractClientPlayer>
{
    protected final RenderPlayer playerRendner;
    
    public CosmeticBase(final RenderPlayer playerRendner) {
        this.playerRendner = playerRendner;
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicl, final float netHeadYaw, final float headPitch, final float scale) {
        if (player.hasPlayerInfo() && !player.isInvisible()) {
            this.render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicl, netHeadYaw, headPitch, scale);
        }
    }
    
    public abstract void render(final AbstractClientPlayer p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    public ModelRenderer bindTextureAndColor(final Color color, final ResourceLocation resourceLocation, final ModelRenderer colorModel, final ModelRenderer playerSkinModel) {
        final boolean flag = false;
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        return colorModel;
    }
}
