// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;

public class Frustum implements ICamera
{
    private final ClippingHelper clippingHelper;
    private double x;
    private double y;
    private double z;
    
    public Frustum() {
        this(ClippingHelperImpl.getInstance());
    }
    
    public Frustum(final ClippingHelper clippingHelperIn) {
        this.clippingHelper = clippingHelperIn;
    }
    
    @Override
    public void setPosition(final double xIn, final double yIn, final double zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }
    
    public boolean isBoxInFrustum(final double p_78548_1_, final double p_78548_3_, final double p_78548_5_, final double p_78548_7_, final double p_78548_9_, final double p_78548_11_) {
        return this.clippingHelper.isBoxInFrustum(p_78548_1_ - this.x, p_78548_3_ - this.y, p_78548_5_ - this.z, p_78548_7_ - this.x, p_78548_9_ - this.y, p_78548_11_ - this.z);
    }
    
    @Override
    public boolean isBoundingBoxInFrustum(final AxisAlignedBB p_78546_1_) {
        return this.isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY, p_78546_1_.maxZ);
    }
    
    public boolean isBoxInFrustumFully(final double p_isBoxInFrustumFully_1_, final double p_isBoxInFrustumFully_3_, final double p_isBoxInFrustumFully_5_, final double p_isBoxInFrustumFully_7_, final double p_isBoxInFrustumFully_9_, final double p_isBoxInFrustumFully_11_) {
        return this.clippingHelper.isBoxInFrustumFully(p_isBoxInFrustumFully_1_ - this.x, p_isBoxInFrustumFully_3_ - this.y, p_isBoxInFrustumFully_5_ - this.z, p_isBoxInFrustumFully_7_ - this.x, p_isBoxInFrustumFully_9_ - this.y, p_isBoxInFrustumFully_11_ - this.z);
    }
}
