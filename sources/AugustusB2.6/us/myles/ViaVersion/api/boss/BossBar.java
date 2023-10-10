// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.boss;

import java.util.Set;
import java.util.UUID;

@Deprecated
public class BossBar<T>
{
    private final com.viaversion.viaversion.api.legacy.bossbar.BossBar bossBar;
    
    public BossBar(final com.viaversion.viaversion.api.legacy.bossbar.BossBar bossBar) {
        this.bossBar = bossBar;
    }
    
    public String getTitle() {
        return this.bossBar.getTitle();
    }
    
    public BossBar setTitle(final String title) {
        this.bossBar.setTitle(title);
        return this;
    }
    
    public float getHealth() {
        return this.bossBar.getHealth();
    }
    
    public BossBar setHealth(final float health) {
        this.bossBar.setHealth(health);
        return this;
    }
    
    public BossColor getColor() {
        return BossColor.values()[this.bossBar.getColor().ordinal()];
    }
    
    public BossBar setColor(final BossColor color) {
        this.bossBar.setColor(com.viaversion.viaversion.api.legacy.bossbar.BossColor.values()[color.ordinal()]);
        return this;
    }
    
    public BossStyle getStyle() {
        return BossStyle.values()[this.bossBar.getStyle().ordinal()];
    }
    
    public BossBar setStyle(final BossStyle style) {
        this.bossBar.setStyle(com.viaversion.viaversion.api.legacy.bossbar.BossStyle.values()[style.ordinal()]);
        return this;
    }
    
    @Deprecated
    public BossBar addPlayer(final T player) {
        return this;
    }
    
    public BossBar addPlayer(final UUID player) {
        this.bossBar.addPlayer(player);
        return this;
    }
    
    @Deprecated
    public BossBar addPlayers(final T... players) {
        return this;
    }
    
    @Deprecated
    public BossBar removePlayer(final T player) {
        return this;
    }
    
    public BossBar removePlayer(final UUID uuid) {
        this.bossBar.removePlayer(uuid);
        return this;
    }
    
    public BossBar addFlag(final BossFlag flag) {
        this.bossBar.addFlag(com.viaversion.viaversion.api.legacy.bossbar.BossFlag.values()[flag.ordinal()]);
        return this;
    }
    
    public BossBar removeFlag(final BossFlag flag) {
        this.bossBar.removeFlag(com.viaversion.viaversion.api.legacy.bossbar.BossFlag.values()[flag.ordinal()]);
        return this;
    }
    
    public boolean hasFlag(final BossFlag flag) {
        return this.bossBar.hasFlag(com.viaversion.viaversion.api.legacy.bossbar.BossFlag.values()[flag.ordinal()]);
    }
    
    public Set<UUID> getPlayers() {
        return this.bossBar.getPlayers();
    }
    
    public BossBar show() {
        this.bossBar.show();
        return this;
    }
    
    public BossBar hide() {
        this.bossBar.hide();
        return this;
    }
    
    public boolean isVisible() {
        return this.bossBar.isVisible();
    }
    
    public UUID getId() {
        return this.bossBar.getId();
    }
}
