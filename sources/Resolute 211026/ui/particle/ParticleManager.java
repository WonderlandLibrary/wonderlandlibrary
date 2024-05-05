// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.particle;

public class ParticleManager
{
    private static final ParticleGenerator particleGenerator;
    
    public static void drawParticles(final int mouseX, final int mouseY) {
        ParticleManager.particleGenerator.draw(mouseX, mouseY);
    }
    
    static {
        particleGenerator = new ParticleGenerator(100);
    }
}
