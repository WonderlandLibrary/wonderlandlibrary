/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AntiProjectile
extends Feature {
    private final BooleanSetting shulkerBullet = new BooleanSetting("Shulker Bullet", true, () -> true);
    private final BooleanSetting ghastFireball = new BooleanSetting("Ghast Fireball", true, () -> true);
    private final BooleanSetting dragontFireball;
    private final NumberSetting shulkerRange = new NumberSetting("Bullet Range", 4.0f, 1.0f, 6.0f, 0.1f, this.shulkerBullet::getCurrentValue);
    private final NumberSetting ghastRange = new NumberSetting("Fireball Range", 4.0f, 1.0f, 6.0f, 0.1f, this.ghastFireball::getCurrentValue);
    private final NumberSetting dragonRange;
    private final BooleanSetting rotations;

    public AntiProjectile() {
        super("AntiProjectile", "\u041e\u0442\u0440\u0430\u0436\u0430\u0435\u0442 \u0441\u043d\u0430\u0440\u044f\u0434\u044b \u043e\u0442 \u0433\u0430\u0441\u0442\u043e\u0432, \u0434\u0440\u0430\u043a\u043e\u043d\u043e\u0432 \u0438 \u0448\u0430\u043b\u043a\u0435\u0440\u043e\u0432", Type.Combat);
        this.dragontFireball = new BooleanSetting("Dragon Fireball", true, () -> true);
        this.dragonRange = new NumberSetting("Dragon Range", 4.0f, 1.0f, 6.0f, 0.1f, this.dragontFireball::getCurrentValue);
        this.rotations = new BooleanSetting("Rotations", true, () -> true);
        this.addSettings(this.rotations, this.shulkerBullet, this.shulkerRange, this.ghastFireball, this.ghastRange, this.dragontFireball, this.dragonRange);
    }

    private Entity getEntity() {
        for (Entity entity : AntiProjectile.mc.world.loadedEntityList) {
            if (entity instanceof EntityShulkerBullet && this.shulkerBullet.getCurrentValue() && AntiProjectile.mc.player.getDistanceToEntity(entity) <= this.shulkerRange.getCurrentValue()) {
                return entity;
            }
            if (entity instanceof EntityFireball && this.ghastFireball.getCurrentValue() && AntiProjectile.mc.player.getDistanceToEntity(entity) <= this.ghastRange.getCurrentValue()) {
                return entity;
            }
            if (!(entity instanceof EntityDragonFireball) || !this.dragontFireball.getCurrentValue() || !(AntiProjectile.mc.player.getDistanceToEntity(entity) <= this.dragonRange.getCurrentValue())) continue;
            return entity;
        }
        return null;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        Entity entity = this.getEntity();
        if (entity == null) {
            return;
        }
        if (AntiProjectile.mc.player.getDistanceToEntity(entity) <= this.shulkerRange.getCurrentValue()) {
            if (this.rotations.getCurrentValue()) {
                float[] rots = RotationHelper.getRotationVector(entity.getPositionVector());
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
                RotationHelper.setRotations(rots[0], rots[1]);
            }
            if (AntiProjectile.mc.player.getCooledAttackStrength(0.0f) == 1.0f) {
                AntiProjectile.mc.playerController.attackEntity(AntiProjectile.mc.player, entity);
                AntiProjectile.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

