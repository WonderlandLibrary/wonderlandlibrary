package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.friendsystem.Friend;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.movement.MovementUtil;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

public class TargetStrafe
extends Module {
    public static Setting range;
    public static Setting linewidth;
    public static Setting spd;
    public static int direction;

    public TargetStrafe() {
        super("TargetStrafe", Category.Combat);
        ArrayList<String> motMode = new ArrayList<String>();
        motMode.add("Event");
        motMode.add("Default");
        Main.instance.setmgr.rSetting(new Setting("Motion Mode", this, "Event", motMode));
        ArrayList<String> mode = new ArrayList<String>();
        mode.add("Circle");
        mode.add("CustomPoint");
        Main.instance.setmgr.rSetting(new Setting("ESP Mode", this, "Circle", mode));
        range = new Setting("Distance", this, 2.4, 0.1, 6.0, false);
        Main.instance.setmgr.rSetting(range);
        spd = new Setting("Speed", this, 0.23, 0.1, 2.0, false);
        Main.instance.setmgr.rSetting(spd);
        Main.instance.setmgr.rSetting(new Setting("BoostValue", this, 0.5, 0.1, 4.0, false));
        linewidth = new Setting("LineWidth", this, 2.0, 1.0, 10.0, false);
        Main.instance.setmgr.rSetting(linewidth);
        Main.instance.setmgr.rSetting(new Setting("Custom Points", this, 5.0, 3.0, 30.0, false));
        Main.instance.setmgr.rSetting(new Setting("Render", this, true));
        Main.instance.setmgr.rSetting(new Setting("Me", this, true));
        Main.instance.setmgr.rSetting(new Setting("DamageBoost", this, true));
        Main.instance.setmgr.rSetting(new Setting("AutoJump", this, true));
    }

    public boolean isValidEntity(Entity entity) {
        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!entity.getName().equals(friend.getName())) continue;
            return false;
        }
        if (Main.instance.setmgr.getSettingByName("Invisible").getValue() && entity.isInvisible() && (Main.instance.setmgr.getSettingByName("Players").getValue() && entity instanceof EntityPlayer || Main.instance.setmgr.getSettingByName("Mobs").getValue() && entity instanceof EntityMob)) {
            return true;
        }
        if (Main.instance.setmgr.getSettingByName("Players").getValue() && entity instanceof EntityPlayer && !entity.isInvisible()) {
            return true;
        }
        return !entity.isInvisible();
    }

    public boolean onMotionUpdate(MoveEvent e) {
        EntityLivingBase entity = KillAura.target;
        float[] rotations = RotationUtil.getRotations(entity);
        double speed = Main.instance.setmgr.getSettingByName("Speed").getValFloat();
        if ((double)Minecraft.player.getDistanceToEntity(entity) <= range.getValDouble()) {
            if (Minecraft.player.hurtTime > 0 && Main.instance.setmgr.getSettingByName("DamageBoost").getValue()) {
                MovementUtil.setMotion(e, spd.getValFloat() + Main.instance.setmgr.getSettingByName("BoostValue").getValFloat(), rotations[0], direction, 0.0);
            } else {
                MovementUtil.setMotion(e, spd.getValFloat(), rotations[0], direction, 0.0);
            }
        } else if (Minecraft.player.hurtTime > 0 && Main.instance.setmgr.getSettingByName("DamageBoost").getValue()) {
            MovementUtil.setMotion(e, spd.getValFloat() + Main.instance.setmgr.getSettingByName("BoostValue").getValFloat(), rotations[0], direction, 1.0);
        } else {
            MovementUtil.setMotion(e, spd.getValFloat(), rotations[0], direction, 1.0);
        }
        return true;
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Motion Mode").getValString();
        this.setSuffix(mode + ", " + range.getValFloat());
        EntityLivingBase entity = KillAura.target;
        if (Minecraft.player.isCollidedHorizontally) {
            this.switchDirection();
        }
        if (TargetStrafe.mc.gameSettings.keyBindLeft.isPressed()) {
            direction = 1;
        }
        if (TargetStrafe.mc.gameSettings.keyBindRight.isPressed()) {
            direction = -1;
        }
        if (KillAura.target.getHealth() > 0.0f && Main.instance.setmgr.getSettingByName("AutoJump").getValue() && Main.instance.moduleManager.getModuleByClass(KillAura.class).getState() && Main.instance.moduleManager.getModuleByClass(TargetStrafe.class).getState()) {
            if (Minecraft.player.onGround) {    
                Minecraft.player.jump();
            }
        }
    }

    @EventTarget
    public void onSwitchDir(EventUpdate event) {
        if (Minecraft.player.isCollidedHorizontally) {
            this.switchDirection();
        }
        if (TargetStrafe.mc.gameSettings.keyBindLeft.isKeyDown()) {
            direction = 1;
        }
        if (TargetStrafe.mc.gameSettings.keyBindRight.isKeyDown()) {
            direction = -1;
        }
    }

    private void switchDirection() {
        direction = direction == 1 ? -1 : 1;
    }

    @EventTarget
    public void onRender3D(Event3D e) {
        EntityLivingBase entity = KillAura.target;
        String mode = Main.instance.setmgr.getSettingByName("ESP Mode").getValString();
        int point = -1;
        if (mode.equalsIgnoreCase("CustomPoint")) {
            point = Main.instance.setmgr.getSettingByName("Custom Points").getValInt();
        } else if (mode.equalsIgnoreCase("Circle")) {
            point = 30;
        }
        if (KillAura.target != null && Main.instance.moduleManager.getModuleByClass(KillAura.class).getState()) {
            if (Minecraft.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getValFloat() && KillAura.target.getHealth() > 0.0f) {
                if (entity != null && Main.instance.setmgr.getSettingByName("Render").getValue()) {
                    RenderUtil.drawLinesAroundPlayer(KillAura.target, Main.instance.setmgr.getSettingByName("Distance").getValDouble(), e.getPartialTicks(), point, Main.instance.setmgr.getSettingByName("LineWidth").getValFloat(), -1);
                }
                if (Main.instance.setmgr.getSettingByName("Me").getValue()) {
                    RenderUtil.drawLinesAroundPlayer(Minecraft.player, Main.instance.setmgr.getSettingByName("Distance").getValDouble() * 2.0, e.getPartialTicks(), point, Main.instance.setmgr.getSettingByName("LineWidth").getValFloat(), -1);
                }
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (TargetStrafe.mc.currentScreen instanceof GuiGameOver) {
            this.toggle();
            NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
            return;
        }
        if (Minecraft.player.ticksExisted <= 1) {
            this.toggle();
            NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
            return;
        }
        if (Main.instance.moduleManager.getModuleByClass(KillAura.class).getState() && KillAura.target != null) {
            double speed = Main.instance.setmgr.getSettingByName("Speed").getValDouble();
            double range = Main.instance.setmgr.getSettingByName("Distance").getValDouble();
            if (Minecraft.player.isCollidedHorizontally) {
                this.switchDirection();
            }
            if (KillAura.target.getHealth() > 0.0f) {
                this.onMotionUpdate(e);
            }
        }
    }

    static {
        direction = -1;
    }
}

