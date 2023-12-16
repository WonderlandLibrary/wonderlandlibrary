// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import org.lwjgl.opengl.GL11;

public class WapointRendererBase
{
    public int lIlIlIlIlIIlIIlIIllIIIIIl;
    public int IlllIIIIIIlllIlIIlllIlIIl;
    public boolean lIllIlIIIlIIIIIIIlllIlIll;
    public int llIlllIIIllllIIlllIllIIIl;
    
    public WapointRendererBase() {
        this.lIlIlIlIlIIlIIlIIllIIIIIl = 100012;
        this.IlllIIIIIIlllIlIIlllIlIIl = 100020;
        this.lIllIlIIIlIIIIIIIlllIlIll = false;
        this.llIlllIIIllllIIlllIllIIIl = 100000;
    }
    
    public void lIlIlIlIlIIlIIlIIllIIIIIl(float f, float f2, float f3) {
        final float f4 = (float)Math.sqrt(f * f + f2 * f2 + f3 * f3);
        if (f4 > 1.0E-5f) {
            f /= f4;
            f2 /= f4;
            f3 /= f4;
        }
        GL11.glNormal3f(f, f2, f3);
    }
    
    public void lIlIlIlIlIIlIIlIIllIIIIIl(final int n) {
        this.lIlIlIlIlIIlIIlIIllIIIIIl = n;
    }
    
    public void normalizePlayerFacing(final int n) {
        this.llIlllIIIllllIIlllIllIIIl = n;
    }
    
    public void lIllIlIIIlIIIIIIIlllIlIll(final int n) {
        this.IlllIIIIIIlllIlIIlllIlIIl = n;
    }
    
    public void lIlIlIlIlIIlIIlIIllIIIIIl(final boolean bl) {
        this.lIllIlIIIlIIIIIIIlllIlIll = bl;
    }
    
    public int lIlIlIlIlIIlIIlIIllIIIIIl() {
        return this.lIlIlIlIlIIlIIlIIllIIIIIl;
    }
    
    public int IlllIIIIIIlllIlIIlllIlIIl() {
        return this.llIlllIIIllllIIlllIllIIIl;
    }
    
    public int lIllIlIIIlIIIIIIIlllIlIll() {
        return this.IlllIIIIIIlllIlIIlllIlIIl;
    }
    
    public boolean llIlllIIIllllIIlllIllIIIl() {
        return this.lIllIlIIIlIIIIIIIlllIlIll;
    }
    
    public void lIlIlIlIlIIlIIlIIllIIIIIl(final float f, final float f2) {
        if (this.lIllIlIIIlIIIIIIIlllIlIll) {
            GL11.glTexCoord2f(f, f2);
        }
    }
    
    public float lIlIlIlIlIIlIIlIIllIIIIIl(final float f) {
        return (float)Math.sin(f);
    }
    
    public float IlllIIIIIIlllIlIIlllIlIIl(final float f) {
        return (float)Math.cos(f);
    }
}
