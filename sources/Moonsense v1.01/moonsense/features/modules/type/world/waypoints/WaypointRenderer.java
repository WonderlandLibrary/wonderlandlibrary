// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import org.lwjgl.opengl.GL11;

public class WaypointRenderer extends WapointRendererBase
{
    public void lIlIlIlIlIIlIIlIIllIIIIIl(final float f, final float f2, final float f3, final int n, final int n2) {
        final float f4 = (this.IlllIIIIIIlllIlIIlllIlIIl == 100021) ? -1.0f : 1.0f;
        final float f5 = 6.2831855f / n;
        final float f6 = (f2 - f) / n2;
        final float f7 = f3 / n2;
        final float f8 = (f - f2) / f3;
        if (this.lIlIlIlIlIIlIIlIIllIIIIIl == 100010) {
            GL11.glBegin(0);
            for (int i = 0; i < n; ++i) {
                final float f9 = this.IlllIIIIIIlllIlIIlllIlIIl(i * f5);
                final float f10 = this.lIlIlIlIlIIlIIlIIllIIIIIl(i * f5);
                this.lIlIlIlIlIIlIIlIIllIIIIIl(f9 * f4, f10 * f4, f8 * f4);
                float f11 = 0.0f;
                float f12 = f;
                for (int j = 0; j <= n2; ++j) {
                    GL11.glVertex3f(f9 * f12, f10 * f12, f11);
                    f11 += f7;
                    f12 += f6;
                }
            }
            GL11.glEnd();
        }
        else if (this.lIlIlIlIlIIlIIlIIllIIIIIl != 100011 && this.lIlIlIlIlIIlIIlIIllIIIIIl != 100013) {
            if (this.lIlIlIlIlIIlIIlIIllIIIIIl == 100012) {
                final float f13 = 1.0f / n;
                final float f14 = 1.0f / n2;
                float f15 = 0.0f;
                float f16 = 0.0f;
                float f17 = f;
                for (int k = 0; k < n2; ++k) {
                    float f18 = 0.0f;
                    GL11.glBegin(8);
                    for (int l = 0; l <= n; ++l) {
                        float f19;
                        float f20;
                        if (l == n) {
                            f19 = this.lIlIlIlIlIIlIIlIIllIIIIIl(0.0f);
                            f20 = this.IlllIIIIIIlllIlIIlllIlIIl(0.0f);
                        }
                        else {
                            f19 = this.lIlIlIlIlIIlIIlIIllIIIIIl(l * f5);
                            f20 = this.IlllIIIIIIlllIlIIlllIlIIl(l * f5);
                        }
                        if (f4 == 1.0f) {
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f19 * f4, f20 * f4, f8 * f4);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f18, f15);
                            GL11.glVertex3f(f19 * f17, f20 * f17, f16);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f19 * f4, f20 * f4, f8 * f4);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f18, f15 + f14);
                            GL11.glVertex3f(f19 * (f17 + f6), f20 * (f17 + f6), f16 + f7);
                        }
                        else {
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f19 * f4, f20 * f4, f8 * f4);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f18, f15);
                            GL11.glVertex3f(f19 * f17, f20 * f17, f16);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f19 * f4, f20 * f4, f8 * f4);
                            this.lIlIlIlIlIIlIIlIIllIIIIIl(f18, f15 + f14);
                            GL11.glVertex3f(f19 * (f17 + f6), f20 * (f17 + f6), f16 + f7);
                        }
                        f18 += f13;
                    }
                    GL11.glEnd();
                    f17 += f6;
                    f15 += f14;
                    f16 += f7;
                }
            }
        }
        else {
            if (this.lIlIlIlIlIIlIIlIIllIIIIIl == 100011) {
                float f21 = 0.0f;
                float f22 = f;
                for (int k = 0; k <= n2; ++k) {
                    GL11.glBegin(2);
                    for (int n3 = 0; n3 < n; ++n3) {
                        final float f23 = this.IlllIIIIIIlllIlIIlllIlIIl(n3 * f5);
                        final float f24 = this.lIlIlIlIlIIlIIlIIllIIIIIl(n3 * f5);
                        this.lIlIlIlIlIIlIIlIIllIIIIIl(f23 * f4, f24 * f4, f8 * f4);
                        GL11.glVertex3f(f23 * f22, f24 * f22, f21);
                    }
                    GL11.glEnd();
                    f21 += f7;
                    f22 += f6;
                }
            }
            else if (f != 0.0) {
                GL11.glBegin(2);
                for (int n3 = 0; n3 < n; ++n3) {
                    final float f23 = this.IlllIIIIIIlllIlIIlllIlIIl(n3 * f5);
                    final float f24 = this.lIlIlIlIlIIlIIlIIllIIIIIl(n3 * f5);
                    this.lIlIlIlIlIIlIIlIIllIIIIIl(f23 * f4, f24 * f4, f8 * f4);
                    GL11.glVertex3f(f23 * f, f24 * f, 0.0f);
                }
                GL11.glEnd();
                GL11.glBegin(2);
                for (int n3 = 0; n3 < n; ++n3) {
                    final float f23 = this.IlllIIIIIIlllIlIIlllIlIIl(n3 * f5);
                    final float f24 = this.lIlIlIlIlIIlIIlIIllIIIIIl(n3 * f5);
                    this.lIlIlIlIlIIlIIlIIllIIIIIl(f23 * f4, f24 * f4, f8 * f4);
                    GL11.glVertex3f(f23 * f2, f24 * f2, f3);
                }
                GL11.glEnd();
            }
            GL11.glBegin(1);
            for (int n3 = 0; n3 < n; ++n3) {
                final float f23 = this.IlllIIIIIIlllIlIIlllIlIIl(n3 * f5);
                final float f24 = this.lIlIlIlIlIIlIIlIIllIIIIIl(n3 * f5);
                this.lIlIlIlIlIIlIIlIIllIIIIIl(f23 * f4, f24 * f4, f8 * f4);
                GL11.glVertex3f(f23 * f, f24 * f, 0.0f);
                GL11.glVertex3f(f23 * f2, f24 * f2, f3);
            }
            GL11.glEnd();
        }
    }
}
