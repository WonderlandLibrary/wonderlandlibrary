/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.sound.SoundHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class DeathSounds
extends Feature {
    public ListSetting deathSoundMode = new ListSetting("Death Sound Mode", "Oof", () -> true, "Oof", "Bruh", "Punch", "Wolf", "Villager", "Ghast", "Blaze", "Guardian", "Iron Golem", "Skeleton", "Zombie", "Chicken", "Cow", "Pig", "Enderman", "Polar Bear", "Ender Dragon");
    public NumberSetting volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);

    public DeathSounds() {
        super("DeathSounds", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a\u0438 \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u043a\u0430\u043a\u043e\u0433\u043e \u043b\u0438\u0431\u043e \u0438\u0433\u0440\u043e\u043a\u0430", Type.Misc);
        this.addSettings(this.deathSoundMode, this.volume);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.deathSoundMode.currentMode);
        for (Entity entity : DeathSounds.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).getHealth() <= 0.0f) || ((EntityLivingBase)entity).deathTime >= 1 || !(DeathSounds.mc.player.getDistanceToEntity(entity) < 10.0f) || entity.ticksExisted <= 5) continue;
            float volume = this.volume.getCurrentValue() / 10.0f;
            switch (this.deathSoundMode.currentMode) {
                case "Oof": {
                    SoundHelper.playSound("oof.wav", -30.0f + volume * 3.0f, false);
                    break;
                }
                case "Bruh": {
                    SoundHelper.playSound("bruh.wav", -30.0f + volume * 3.0f, false);
                    break;
                }
                case "Punch": {
                    SoundHelper.playSound("punch.wav", -30.0f + volume * 3.0f, false);
                    break;
                }
                case "Wolf": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_WOLF_DEATH, volume, 1.0f);
                    break;
                }
                case "Villager": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, volume, 1.0f);
                    break;
                }
                case "Blaze": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_BLAZE_DEATH, volume, 1.0f);
                    break;
                }
                case "Chicken": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_CHICKEN_DEATH, volume, 1.0f);
                    break;
                }
                case "Enderman": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ENDERMEN_DEATH, volume, 1.0f);
                    break;
                }
                case "Ender Dragon": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ENDERDRAGON_DEATH, volume, 1.0f);
                    break;
                }
                case "Cow": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_COW_DEATH, volume, 1.0f);
                    break;
                }
                case "Pig": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_PIG_DEATH, volume, 1.0f);
                    break;
                }
                case "Skeleton": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_SKELETON_DEATH, volume, 1.0f);
                    break;
                }
                case "Ghast": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_GHAST_DEATH, volume, 1.0f);
                    break;
                }
                case "Zombie": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_ZOMBIE_DEATH, volume, 1.0f);
                    break;
                }
                case "Polar Bear": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_POLAR_BEAR_DEATH, volume, 1.0f);
                    break;
                }
                case "Guardian": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_GUARDIAN_DEATH, volume, 1.0f);
                    break;
                }
                case "Iron Golem": {
                    DeathSounds.mc.player.playSound(SoundEvents.ENTITY_IRONGOLEM_DEATH, volume, 1.0f);
                }
            }
        }
    }
}

