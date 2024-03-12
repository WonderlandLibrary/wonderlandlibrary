// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.render;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.AxisAlignedBB;

public class AabbFrame extends AxisAlignedBB
{
    private int frameCount;
    private boolean inFrustumFully;
    
    public AabbFrame(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        super(x1, y1, z1, x2, y2, z2);
        this.frameCount = -1;
        this.inFrustumFully = false;
    }
    
    public boolean isBoundingBoxInFrustumFully(final ICamera camera, final int frameCount) {
        if (this.frameCount != frameCount) {
            this.inFrustumFully = (camera instanceof Frustum && ((Frustum)camera).isBoxInFrustumFully(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ));
            this.frameCount = frameCount;
        }
        return this.inFrustumFully;
    }
}
