/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class TargetStrafe
extends Feature {
    public static NumberSetting range;
    public static NumberSetting spd;
    public static NumberSetting boostValue;
    public static NumberSetting boostTicks;
    public static NumberSetting speedIfUsing;
    public static BooleanSetting reversed;
    public static NumberSetting reversedDistance;
    public static BooleanSetting boost;
    public static BooleanSetting autoJump;
    public static BooleanSetting wellMoreExploit;
    public static BooleanSetting smartStrafe;
    public static BooleanSetting usingItemCheck;
    public static BooleanSetting autoShift;
    public static BooleanSetting autoThirdPerson;
    public static BooleanSetting autoDisable;
    public static BooleanSetting alwaysSprint;
    public static BooleanSetting switchOnLook;
    public static NumberSetting lookRadius;
    private double wrap = 0.0;
    private boolean switchDir = true;
    public boolean canShift;

    public TargetStrafe() {
        super("TargetStrafe", "\u0421\u0442\u0440\u0435\u0444\u0438\u0442 \u0432\u043e\u043a\u0440\u0443\u0433 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439", Type.Combat);
        spd = new NumberSetting("Strafe Speed", 0.23f, 0.1f, 2.0f, 0.01f, () -> true);
        range = new NumberSetting("Strafe Distance", 2.4f, 0.1f, 6.0f, 0.1f, () -> true);
        boost = new BooleanSetting("DamageBoost", false, () -> true);
        boostValue = new NumberSetting("Boost Value", 0.5f, 0.1f, 4.0f, 0.01f, boost::getCurrentValue);
        boostTicks = new NumberSetting("Boost Ticks", 8.0f, 0.0f, 9.0f, 1.0f, boost::getCurrentValue);
        reversed = new BooleanSetting("Reversed", false, () -> true);
        reversedDistance = new NumberSetting("Reversed Distance", 3.0f, 1.0f, 6.0f, 0.1f, () -> reversed.getCurrentValue());
        autoJump = new BooleanSetting("AutoJump", true, () -> true);
        autoShift = new BooleanSetting("AutoShift", false, () -> true);
        wellMoreExploit = new BooleanSetting("OldWellMore Exploit", "\u041d\u0435 \u0440\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u0442\u0441\u044f \u043a \u0432\u043a\u043b\u044e\u0447\u0430\u043d\u0438\u044e \u043d\u0430 \u043d\u043e\u0432\u043e\u043c \u0430\u043d\u0442\u0438\u0447\u0438\u0442\u0435 \u0432\u0435\u043b\u043b\u043c\u043e\u0440\u0430!", false, () -> true);
        smartStrafe = new BooleanSetting("Smart Strafe", true, () -> true);
        autoThirdPerson = new BooleanSetting("Auto Third Person", false, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", false, () -> true);
        alwaysSprint = new BooleanSetting("Always Sprint", false, () -> true);
        usingItemCheck = new BooleanSetting("Using Item Check", false, () -> true);
        speedIfUsing = new NumberSetting("Speed if using", 0.21f, 0.1f, 2.0f, 0.01f, usingItemCheck::getCurrentValue);
        switchOnLook = new BooleanSetting("Switch On Look", false, () -> true);
        lookRadius = new NumberSetting("Look Radius", 65.0f, 5.0f, 180.0f, 1.0f, () -> switchOnLook.getCurrentValue());
        this.addSettings(boost, boostTicks, boostValue, reversed, reversedDistance, autoJump, autoShift, usingItemCheck, speedIfUsing, autoThirdPerson, autoDisable, alwaysSprint, wellMoreExploit, smartStrafe, spd, range, switchOnLook, lookRadius);
    }

    public boolean needToSwitch(double x, double z) {
        if (TargetStrafe.mc.player.isCollidedHorizontally || TargetStrafe.mc.gameSettings.keyBindLeft.isPressed() || TargetStrafe.mc.gameSettings.keyBindRight.isPressed()) {
            return true;
        }
        if (RotationHelper.isAimAtMe(KillAura.target, lookRadius.getCurrentValue()) && switchOnLook.getCurrentValue()) {
            return !this.switchDir;
        }
        for (int i = (int)(TargetStrafe.mc.player.posY + 4.0); i >= 0; --i) {
            BlockPos playerPos = new BlockPos(x, (double)i, z);
            if (TargetStrafe.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA) || TargetStrafe.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE)) {
                return true;
            }
            if (TargetStrafe.mc.world.isAirBlock(playerPos)) continue;
            return false;
        }
        return true;
    }

    private float wrapDS(float x, float z) {
        double diffX = (double)x - TargetStrafe.mc.player.posX;
        double diffZ = (double)z - TargetStrafe.mc.player.posZ;
        return (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + range.getCurrentValue());
        EntityLivingBase entity = KillAura.target;
        if (TargetStrafe.mc.player == null || TargetStrafe.mc.world == null) {
            return;
        }
        if (alwaysSprint.getCurrentValue()) {
            TargetStrafe.mc.player.setSprinting(true);
        }
        if (autoThirdPerson.getCurrentValue()) {
            if (entity.getHealth() > 0.0f && TargetStrafe.mc.player.getDistanceToEntity(entity) <= KillAura.range.getCurrentValue() && TargetStrafe.mc.player.getHealth() > 0.0f) {
                if (this.getState() && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
                    TargetStrafe.mc.gameSettings.thirdPersonView = 1;
                }
            } else {
                TargetStrafe.mc.gameSettings.thirdPersonView = 0;
            }
        }
        if (autoDisable.getCurrentValue() && (TargetStrafe.mc.currentScreen instanceof GuiGameOver && !TargetStrafe.mc.player.isEntityAlive() || TargetStrafe.mc.player.ticksExisted <= 1)) {
            EventManager.unregister(this);
            this.onDisable();
            if (this.getState()) {
                this.setState(false);
            }
            NotificationManager.publicity("AutoDisable", "TargetStrafe was toggled off!", 4, NotificationType.INFO);
        }
        if (entity == null) {
            return;
        }
        if (TargetStrafe.mc.player.getDistanceToEntity(entity) <= KillAura.range.getCurrentValue() && entity.getHealth() > 0.0f) {
            this.canShift = true;
            float speed = (float)TargetStrafe.mc.player.hurtTime > boostTicks.getCurrentValue() && boost.getCurrentValue() && !TargetStrafe.mc.player.onGround ? boostValue.getCurrentValue() : ((TargetStrafe.mc.player.isUsingItem() || TargetStrafe.mc.gameSettings.keyBindUseItem.isKeyDown()) && usingItemCheck.getCurrentValue() ? speedIfUsing.getCurrentValue() : spd.getCurrentValue());
            float searchValue = Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && reversed.getCurrentValue() && TargetStrafe.mc.player.getDistanceToEntity(KillAura.target) < reversedDistance.getCurrentValue() ? -90.0f : 0.0f;
            float value = MathHelper.clamp(TargetStrafe.mc.player.getDistanceToEntity(entity), 0.01f, KillAura.range.getCurrentValue());
            this.wrap = (float)Math.atan2(TargetStrafe.mc.player.posZ - entity.posZ, TargetStrafe.mc.player.posX - entity.posX);
            this.wrap += this.switchDir ? (double)(speed / value) : (double)(-(speed / value));
            float x = (float)(entity.posX + (double)range.getCurrentValue() * Math.cos(this.wrap));
            float z = (float)(entity.posZ + (double)range.getCurrentValue() * Math.sin(this.wrap));
            if (smartStrafe.getCurrentValue() && this.needToSwitch(x, z)) {
                this.switchDir = !this.switchDir;
                this.wrap += (double)(2.0f * (this.switchDir ? speed / value : -(speed / value)));
                x = (float)(entity.posX + (double)range.getCurrentValue() * Math.cos(this.wrap));
                z = (float)(entity.posZ + (double)range.getCurrentValue() * Math.sin(this.wrap));
            }
            if ((float)TargetStrafe.mc.player.hurtTime > boostTicks.getCurrentValue() && boost.getCurrentValue() && !TargetStrafe.mc.player.onGround) {
                TargetStrafe.mc.player.jumpMovementFactor *= 60.0f;
            }
            float reversedValue = !TargetStrafe.mc.gameSettings.keyBindLeft.isKeyDown() && !TargetStrafe.mc.gameSettings.keyBindRight.isKeyDown() && !TargetStrafe.mc.player.isCollidedHorizontally ? searchValue : 0.0f;
            TargetStrafe.mc.player.motionX = (double)speed * -Math.sin((float)Math.toRadians(this.wrapDS(x + reversedValue, z + reversedValue)));
            TargetStrafe.mc.player.motionZ = (double)speed * Math.cos((float)Math.toRadians(this.wrapDS(x + reversedValue, z + reversedValue)));
            if (autoJump.getCurrentValue() && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState()) {
                if (!KillAura.autoJump.getCurrentValue()) {
                    if (TargetStrafe.mc.player.onGround) {
                        TargetStrafe.mc.player.jump();
                    }
                    if (wellMoreExploit.getCurrentValue()) {
                        TargetStrafe.mc.player.motionY = 0.419973;
                    }
                }
                if (wellMoreExploit.getCurrentValue() && TargetStrafe.mc.player.motionY > 0.0 && !TargetStrafe.mc.player.onGround) {
                    TargetStrafe.mc.player.motionY -= 0.00105;
                }
            }
            if (autoShift.getCurrentValue() && this.canShift) {
                TargetStrafe.mc.gameSettings.keyBindSneak.setPressed((double)TargetStrafe.mc.player.fallDistance > (double)KillAura.critFallDistance.getCurrentValue() + 0.1);
            }
        }
    }
}

