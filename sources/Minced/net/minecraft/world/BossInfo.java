// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.text.ITextComponent;
import java.util.UUID;

public abstract class BossInfo
{
    private final UUID uniqueId;
    protected ITextComponent name;
    protected float percent;
    protected Color color;
    protected Overlay overlay;
    protected boolean darkenSky;
    protected boolean playEndBossMusic;
    protected boolean createFog;
    
    public BossInfo(final UUID uniqueIdIn, final ITextComponent nameIn, final Color colorIn, final Overlay overlayIn) {
        this.uniqueId = uniqueIdIn;
        this.name = nameIn;
        this.color = colorIn;
        this.overlay = overlayIn;
        this.percent = 1.0f;
    }
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }
    
    public ITextComponent getName() {
        return this.name;
    }
    
    public void setName(final ITextComponent nameIn) {
        this.name = nameIn;
    }
    
    public float getPercent() {
        return this.percent;
    }
    
    public void setPercent(final float percentIn) {
        this.percent = percentIn;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color colorIn) {
        this.color = colorIn;
    }
    
    public Overlay getOverlay() {
        return this.overlay;
    }
    
    public void setOverlay(final Overlay overlayIn) {
        this.overlay = overlayIn;
    }
    
    public boolean shouldDarkenSky() {
        return this.darkenSky;
    }
    
    public BossInfo setDarkenSky(final boolean darkenSkyIn) {
        this.darkenSky = darkenSkyIn;
        return this;
    }
    
    public boolean shouldPlayEndBossMusic() {
        return this.playEndBossMusic;
    }
    
    public BossInfo setPlayEndBossMusic(final boolean playEndBossMusicIn) {
        this.playEndBossMusic = playEndBossMusicIn;
        return this;
    }
    
    public BossInfo setCreateFog(final boolean createFogIn) {
        this.createFog = createFogIn;
        return this;
    }
    
    public boolean shouldCreateFog() {
        return this.createFog;
    }
    
    public enum Color
    {
        PINK, 
        BLUE, 
        RED, 
        GREEN, 
        YELLOW, 
        PURPLE, 
        WHITE;
    }
    
    public enum Overlay
    {
        PROGRESS, 
        NOTCHED_6, 
        NOTCHED_10, 
        NOTCHED_12, 
        NOTCHED_20;
    }
}
