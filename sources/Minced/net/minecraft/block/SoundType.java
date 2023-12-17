// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class SoundType
{
    public static final SoundType WOOD;
    public static final SoundType GROUND;
    public static final SoundType PLANT;
    public static final SoundType STONE;
    public static final SoundType METAL;
    public static final SoundType GLASS;
    public static final SoundType CLOTH;
    public static final SoundType SAND;
    public static final SoundType SNOW;
    public static final SoundType LADDER;
    public static final SoundType ANVIL;
    public static final SoundType SLIME;
    public final float volume;
    public final float pitch;
    private final SoundEvent breakSound;
    private final SoundEvent stepSound;
    private final SoundEvent placeSound;
    private final SoundEvent hitSound;
    private final SoundEvent fallSound;
    
    public SoundType(final float volumeIn, final float pitchIn, final SoundEvent breakSoundIn, final SoundEvent stepSoundIn, final SoundEvent placeSoundIn, final SoundEvent hitSoundIn, final SoundEvent fallSoundIn) {
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.breakSound = breakSoundIn;
        this.stepSound = stepSoundIn;
        this.placeSound = placeSoundIn;
        this.hitSound = hitSoundIn;
        this.fallSound = fallSoundIn;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public SoundEvent getBreakSound() {
        return this.breakSound;
    }
    
    public SoundEvent getStepSound() {
        return this.stepSound;
    }
    
    public SoundEvent getPlaceSound() {
        return this.placeSound;
    }
    
    public SoundEvent getHitSound() {
        return this.hitSound;
    }
    
    public SoundEvent getFallSound() {
        return this.fallSound;
    }
    
    static {
        WOOD = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.BLOCK_WOOD_STEP, SoundEvents.BLOCK_WOOD_PLACE, SoundEvents.BLOCK_WOOD_HIT, SoundEvents.BLOCK_WOOD_FALL);
        GROUND = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_GRAVEL_BREAK, SoundEvents.BLOCK_GRAVEL_STEP, SoundEvents.BLOCK_GRAVEL_PLACE, SoundEvents.BLOCK_GRAVEL_HIT, SoundEvents.BLOCK_GRAVEL_FALL);
        PLANT = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_GRASS_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_GRASS_PLACE, SoundEvents.BLOCK_GRASS_HIT, SoundEvents.BLOCK_GRASS_FALL);
        STONE = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
        METAL = new SoundType(1.0f, 1.5f, SoundEvents.BLOCK_METAL_BREAK, SoundEvents.BLOCK_METAL_STEP, SoundEvents.BLOCK_METAL_PLACE, SoundEvents.BLOCK_METAL_HIT, SoundEvents.BLOCK_METAL_FALL);
        GLASS = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_GLASS_BREAK, SoundEvents.BLOCK_GLASS_STEP, SoundEvents.BLOCK_GLASS_PLACE, SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_FALL);
        CLOTH = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_CLOTH_BREAK, SoundEvents.BLOCK_CLOTH_STEP, SoundEvents.BLOCK_CLOTH_PLACE, SoundEvents.BLOCK_CLOTH_HIT, SoundEvents.BLOCK_CLOTH_FALL);
        SAND = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_SAND_BREAK, SoundEvents.BLOCK_SAND_STEP, SoundEvents.BLOCK_SAND_PLACE, SoundEvents.BLOCK_SAND_HIT, SoundEvents.BLOCK_SAND_FALL);
        SNOW = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_SNOW_BREAK, SoundEvents.BLOCK_SNOW_STEP, SoundEvents.BLOCK_SNOW_PLACE, SoundEvents.BLOCK_SNOW_HIT, SoundEvents.BLOCK_SNOW_FALL);
        LADDER = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_LADDER_BREAK, SoundEvents.BLOCK_LADDER_STEP, SoundEvents.BLOCK_LADDER_PLACE, SoundEvents.BLOCK_LADDER_HIT, SoundEvents.BLOCK_LADDER_FALL);
        ANVIL = new SoundType(0.3f, 1.0f, SoundEvents.BLOCK_ANVIL_BREAK, SoundEvents.BLOCK_ANVIL_STEP, SoundEvents.BLOCK_ANVIL_PLACE, SoundEvents.BLOCK_ANVIL_HIT, SoundEvents.BLOCK_ANVIL_FALL);
        SLIME = new SoundType(1.0f, 1.0f, SoundEvents.BLOCK_SLIME_BREAK, SoundEvents.BLOCK_SLIME_STEP, SoundEvents.BLOCK_SLIME_PLACE, SoundEvents.BLOCK_SLIME_HIT, SoundEvents.BLOCK_SLIME_FALL);
    }
}
