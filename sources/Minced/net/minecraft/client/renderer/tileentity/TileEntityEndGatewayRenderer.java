// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndGatewayRenderer extends TileEntityEndPortalRenderer
{
    private static final ResourceLocation END_GATEWAY_BEAM_TEXTURE;
    
    @Override
    public void render(final TileEntityEndPortal te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        GlStateManager.disableFog();
        final TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)te;
        if (tileentityendgateway.isSpawning() || tileentityendgateway.isCoolingDown()) {
            GlStateManager.alphaFunc(516, 0.1f);
            this.bindTexture(TileEntityEndGatewayRenderer.END_GATEWAY_BEAM_TEXTURE);
            float f = tileentityendgateway.isSpawning() ? tileentityendgateway.getSpawnPercent(partialTicks) : tileentityendgateway.getCooldownPercent(partialTicks);
            final double d0 = tileentityendgateway.isSpawning() ? (256.0 - y) : 50.0;
            f = MathHelper.sin(f * 3.1415927f);
            final int i = MathHelper.floor(f * d0);
            final float[] afloat = tileentityendgateway.isSpawning() ? EnumDyeColor.MAGENTA.getColorComponentValues() : EnumDyeColor.PURPLE.getColorComponentValues();
            TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, f, (double)tileentityendgateway.getWorld().getTotalWorldTime(), 0, i, afloat, 0.15, 0.175);
            TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, f, (double)tileentityendgateway.getWorld().getTotalWorldTime(), 0, -i, afloat, 0.15, 0.175);
        }
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.enableFog();
    }
    
    @Override
    protected int getPasses(final double p_191286_1_) {
        return super.getPasses(p_191286_1_) + 1;
    }
    
    @Override
    protected float getOffset() {
        return 1.0f;
    }
    
    static {
        END_GATEWAY_BEAM_TEXTURE = new ResourceLocation("textures/entity/end_gateway_beam.png");
    }
}
