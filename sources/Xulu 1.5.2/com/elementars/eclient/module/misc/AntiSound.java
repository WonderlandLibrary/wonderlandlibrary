// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.server.SPacketSoundEffect;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AntiSound extends Module
{
    public final Value<Boolean> wither;
    private final Value<Boolean> witherHurt;
    public final Value<Boolean> witherSpawn;
    private final Value<Boolean> witherDeath;
    private final Value<Boolean> punches;
    private final Value<Boolean> punchW;
    private final Value<Boolean> punchKB;
    private final Value<Boolean> explosion;
    public final Value<Boolean> totem;
    public final Value<Boolean> elytra;
    public final Value<Boolean> portal;
    
    public AntiSound() {
        super("AntiSound", "Blacklists certain annoying sounds", 0, Category.MISC, true);
        this.wither = this.register(new Value<Boolean>("Wither Ambient", this, true));
        this.witherHurt = this.register(new Value<Boolean>("Wither Hurt", this, true));
        this.witherSpawn = this.register(new Value<Boolean>("Wither Spawn", this, false));
        this.witherDeath = this.register(new Value<Boolean>("Wither Death", this, false));
        this.punches = this.register(new Value<Boolean>("Punches", this, true));
        this.punchW = this.register(new Value<Boolean>("Weak Punch", this, true));
        this.punchKB = this.register(new Value<Boolean>("Knockback Punch", this, true));
        this.explosion = this.register(new Value<Boolean>("Explosion", this, false));
        this.totem = this.register(new Value<Boolean>("Totem Pop", this, false));
        this.elytra = this.register(new Value<Boolean>("Elytra Wind", this, true));
        this.portal = this.register(new Value<Boolean>("Nether Portal", this, true));
    }
    
    @Override
    public void onEnable() {
        AntiSound.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        AntiSound.EVENT_BUS.unregister((Object)this);
    }
    
    @EventTarget
    public void onRecieve(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (this.shouldCancelSound(packet.getSound())) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean shouldCancelSound(final SoundEvent soundEvent) {
        return (soundEvent == SoundEvents.ENTITY_WITHER_AMBIENT && this.wither.getValue()) || (soundEvent == SoundEvents.ENTITY_WITHER_SPAWN && this.witherSpawn.getValue()) || (soundEvent == SoundEvents.ENTITY_WITHER_HURT && this.witherHurt.getValue()) || (soundEvent == SoundEvents.ENTITY_WITHER_DEATH && this.witherDeath.getValue()) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE && this.punches.getValue()) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_WEAK && this.punchW.getValue()) || (soundEvent == SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK && this.punchKB.getValue()) || (soundEvent == SoundEvents.ENTITY_GENERIC_EXPLODE && this.explosion.getValue());
    }
}
