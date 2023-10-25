/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventAttackSilent;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class HitParticles
extends Feature {
    private final NumberSetting particleMultiplier;
    private final ListSetting particleMode;
    private final Random random = new Random();

    public HitParticles() {
        super("DamageParticles", "\u041f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435 \u0441\u043f\u0430\u0432\u043d\u0438\u0442 \u043f\u0430\u0440\u0442\u0438\u043a\u043b\u044b \u0432\u043e\u043a\u0440\u0443\u0433 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0438", Type.Combat);
        this.particleMode = new ListSetting("Particle Mode", "Spell", () -> true, "Spell", "Enchant", "Criticals", "Heart", "Flame", "HappyVillager", "AngryVillager", "Portal", "Redstone", "Cloud");
        this.particleMultiplier = new NumberSetting("Particle Multiplier", 5.0f, 1.0f, 15.0f, 1.0f, () -> true);
        this.addSettings(this.particleMode, this.particleMultiplier);
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        block19: {
            String mode;
            block27: {
                block26: {
                    block25: {
                        block24: {
                            block23: {
                                block22: {
                                    block21: {
                                        block20: {
                                            block18: {
                                                mode = this.particleMode.getOptions();
                                                if (!mode.equalsIgnoreCase("Redstone")) break block18;
                                                for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                                    int i2 = 0;
                                                    while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                                        HitParticles.mc.effectRenderer.spawnEffectParticle(37, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (float)(this.random.nextInt(6) - 3) / 5.0f, 0.1, (float)(this.random.nextInt(6) - 3) / 5.0f, Block.getIdFromBlock(Blocks.REDSTONE_BLOCK));
                                                        ++i2;
                                                    }
                                                }
                                                break block19;
                                            }
                                            if (!mode.equalsIgnoreCase("Heart")) break block20;
                                            for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                                int i2 = 0;
                                                while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                                    HitParticles.mc.world.spawnParticle(EnumParticleTypes.HEART, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                                    ++i2;
                                                }
                                            }
                                            break block19;
                                        }
                                        if (!mode.equalsIgnoreCase("Flame")) break block21;
                                        for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                            int i2 = 0;
                                            while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                                HitParticles.mc.world.spawnParticle(EnumParticleTypes.FLAME, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                                ++i2;
                                            }
                                        }
                                        break block19;
                                    }
                                    if (!mode.equalsIgnoreCase("Cloud")) break block22;
                                    for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                        int i2 = 0;
                                        while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                            HitParticles.mc.world.spawnParticle(EnumParticleTypes.CLOUD, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                            ++i2;
                                        }
                                    }
                                    break block19;
                                }
                                if (!mode.equalsIgnoreCase("HappyVillager")) break block23;
                                for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                    int i2 = 0;
                                    while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                        HitParticles.mc.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                        ++i2;
                                    }
                                }
                                break block19;
                            }
                            if (!mode.equalsIgnoreCase("AngryVillager")) break block24;
                            for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                                int i2 = 0;
                                while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                    HitParticles.mc.world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                    ++i2;
                                }
                            }
                            break block19;
                        }
                        if (!mode.equalsIgnoreCase("Spell")) break block25;
                        for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                            int i2 = 0;
                            while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                                HitParticles.mc.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                                ++i2;
                            }
                        }
                        break block19;
                    }
                    if (!mode.equalsIgnoreCase("Portal")) break block26;
                    for (float i = 0.0f; i < event.getTargetEntity().height; i += 0.2f) {
                        int i2 = 0;
                        while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                            HitParticles.mc.world.spawnParticle(EnumParticleTypes.PORTAL, event.getTargetEntity().posX, event.getTargetEntity().posY + (double)i, event.getTargetEntity().posZ, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), 0.1, (double)((float)(this.random.nextInt(6) - 3) / 5.0f), new int[0]);
                            ++i2;
                        }
                    }
                    break block19;
                }
                if (!mode.equalsIgnoreCase("Enchant")) break block27;
                int i2 = 0;
                while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                    HitParticles.mc.player.onEnchantmentCritical(event.getTargetEntity());
                    ++i2;
                }
                break block19;
            }
            if (!mode.equalsIgnoreCase("Criticals")) break block19;
            int i2 = 0;
            while ((float)i2 < this.particleMultiplier.getCurrentValue()) {
                HitParticles.mc.player.onCriticalHit(event.getTargetEntity());
                ++i2;
            }
        }
    }
}

