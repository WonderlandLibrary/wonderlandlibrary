// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu.particles;

import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.tuples.Pair;

public class ParticleImage
{
    private final Pair<Integer, Integer> dimensions;
    private final ResourceLocation location;
    private final ParticleType particleType;
    
    public ParticleImage(final int particleNumber, final Pair<Integer, Integer> dimensions) {
        this.dimensions = dimensions;
        this.particleType = ((dimensions.getFirst() > 350) ? ParticleType.BIG : ParticleType.SMALL);
        this.location = new ResourceLocation("Tenacity/MainMenu/particles" + particleNumber + ".png");
    }
    
    public Pair<Integer, Integer> getDimensions() {
        return this.dimensions;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public ParticleType getParticleType() {
        return this.particleType;
    }
}
