// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.objects;

import net.minecraft.entity.player.PlayerCapabilities;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class ScriptPlayerCapabilites
{
    public boolean disableDamage;
    public boolean isFlying;
    public boolean allowFlying;
    public boolean isCreativeMode;
    public boolean allowEdit;
    private float flySpeed;
    private float walkSpeed;
    
    public ScriptPlayerCapabilites() {
        this.allowEdit = true;
        this.flySpeed = 0.05f;
        this.walkSpeed = 0.1f;
    }
    
    public PlayerCapabilities getActualAbilites() {
        final PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.setFlySpeed(this.flySpeed);
        playerCapabilities.setPlayerWalkSpeed(this.walkSpeed);
        playerCapabilities.allowEdit = this.allowEdit;
        playerCapabilities.disableDamage = this.disableDamage;
        playerCapabilities.isFlying = this.isFlying;
        playerCapabilities.allowFlying = this.allowFlying;
        playerCapabilities.isCreativeMode = this.isCreativeMode;
        return playerCapabilities;
    }
    
    public float getFlySpeed() {
        return this.flySpeed;
    }
    
    public void setFlySpeed(final float speed) {
        this.flySpeed = speed;
    }
    
    public float getWalkSpeed() {
        return this.walkSpeed;
    }
    
    public void setPlayerWalkSpeed(final float speed) {
        this.walkSpeed = speed;
    }
}
