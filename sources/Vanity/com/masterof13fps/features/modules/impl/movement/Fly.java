package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.utils.entity.EntityUtils;
import com.masterof13fps.utils.entity.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Fly", category = Category.MOVEMENT, description = "Lets you fly")
public class Fly extends Module {

    /**
     * Recoded on 2020-12-08 at 7:30 PM
     */

    public Setting mode = new Setting("Mode", this, "Fly", new String[]{"Fly", "Glide"});
    public Setting flyMode = new Setting("Fly Mode", this, "Jetpack", new String[]{"Vanilla", "Jetpack",
            "Hypixel Old", "Hypixel New", "Motion", "AAC 1.9.8", "AAC 1.9.10 Old", "AAC 1.9.10 New", "AAC 3.0.5", "CubeCraft", "OmegaCraft", "MCCentral", "Intave"});
    public Setting glideMode = new Setting("Glide Mode", this, "New", new String[]{"Old", "New"});
    
    double motion;
    boolean speed;
    int time = 0;
    int dtime = 0;
    double glidespeed = 0.07000000000000001D;
    int delay = 0;

    @Override
    public void onToggle() {

    }

    @Override
    public void onDisable() {
        time = 0;
        dtime = 0;
        getPlayer().capabilities.isFlying = false;
        mc.timer.timerSpeed = 1F;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {

            switch (mode.getCurrentMode()) {
                case "Fly": {
                    setDisplayName("Fly §7" + mode.getCurrentMode() + " / " + flyMode.getCurrentMode());

                    switch (flyMode.getCurrentMode()) {
                        case "Vanilla": {
                            doVanilla();
                            break;
                        }
                        case "Motion": {
                            doMotion();
                            break;
                        }
                        case "Hypixel Old": {
                            doHypixelOld();
                            break;
                        }
                        case "Hypixel New": {
                            doHypixelNew();
                            break;
                        }
                        case "AAC 3.0.5": {
                            doAAC305();
                            break;
                        }
                        case "AAC 1.9.10 Old": {
                            doAAC1910Old();
                            break;
                        }
                        case "Jetpack": {
                            doJetpack();
                            break;
                        }
                        case "AAC 1.9.10 New": {
                            doAAC1910New();
                            break;
                        }
                        case "AAC 1.9.8": {
                            doAAC198();
                            break;
                        }
                        case "CubeCraft": {
                            doCubeCraft();
                            break;
                        }
                        case "Intave": {
                            doIntave();
                            break;
                        }
                        case "OmegaCraft": {
                            doOmegaCraft();
                            break;
                        }
                        case "MCCentral": {
                            doMCCentral();
                            break;
                        }
                    }

                    break;
                }
                case "Glide": {
                    setDisplayName("Fly [" + mode + " / " + glideMode + "]");

                    switch (glideMode.getCurrentMode()) {
                        case "New": {
                            doNew();
                            break;
                        }
                        case "Old": {
                            doOld();
                            break;
                        }
                    }

                    break;
                }
            }
        }
    }

    private void doMCCentral() {
        setMotionY(0.0D);
        setSpeed(0.7);
        if (getGameSettings().keyBindJump.isKeyDown()) {
            getPlayer().motionY += 0.75;
        }
        if (getGameSettings().keyBindSneak.isKeyDown()) {
            getPlayer().motionY -= 0.75;
        }
    }

    private void doHypixelNew() {
        Fly.mc.thePlayer.motionY = 0.0;
        PlayerUtil.setSpeed(getBaseMoveSpeed());
    }

    private void doOmegaCraft() {
        PlayerUtil.setSpeed(2.0);
        setTimerSpeed(0.3F);
        if (getPlayer().onGround) {
            setMotionY(0.6D);
        }
        if (getPlayer().ticksExisted % 2 == 0) {
            setTimerSpeed(1.0F);
        }
    }

    private void doOld() {
        EntityUtils.damagePlayer(1);
        if ((getPlayer().motionY <= -glidespeed) && (!getPlayer().isInWater())
                && (!getPlayer().onGround) && (!getPlayer().isOnLadder())) {
            getPlayer().motionY = (-glidespeed);
        }
    }

    private void doCubeCraft() {
        if (isMoving() && !getPlayer().onGround)
            mc.timer.timerSpeed = 0.29f;

        if (timeHelper.hasReached(200) && !getPlayer().isCollidedHorizontally && !getPlayer().isInWater() && !getPlayer().onGround && isMoving()) {
            double multiply = 2.1;
            double yaw = Math.toRadians(getPlayer().rotationYaw);
            double posX = -Math.sin(yaw) * multiply;
            double posZ = Math.cos(yaw) * multiply;
            getPlayer().setPosition(getPlayer().posX + posX, getPlayer().posY, getPlayer().posZ + posZ);
        }
        getPlayer().motionY = -0.01;
    }

    private void doAAC198() {
        getPlayer().motionY = 0.2D;
        if (timeHelper.hasReached(100L)) {
            getPlayer().motionY = -0.3D;
            timeHelper.reset();
        }
    }

    private void doAAC1910New() {
        double y;
        getPlayer().jumpMovementFactor = 0.024F;
        if (timeHelper.hasReached(500L)) {
            if (!getPlayer().onGround) {
                if (getPlayer().fallDistance > 4.0F) {
                    getPlayer().motionX *= 0.6D;
                    getPlayer().motionZ *= 0.6D;
                    y = getPlayer().posY + 6.0D;
                    getPlayer().setPositionAndUpdate(getPlayer().posX, y, getPlayer().posZ);
                    sendPacket(new C03PacketPlayer(true));
                    timeHelper.reset();
                }
            }
        } else {
            getPlayer().motionX *= 1.3D;
            getPlayer().motionZ *= 1.3D;
            getPlayer().motionY = -0.3D;
        }
    }

    private void doJetpack() {
        if (mc.gameSettings.keyBindJump.pressed) {
            getPlayer().jump();
            getPlayer().moveForward = 0.8F;
        }
    }

    private void doAAC1910Old() {
        if ((double) getPlayer().fallDistance > 2.5D) {
            ++getPlayer().motionY;
            getPlayer().fallDistance = 0.0F;
        }
    }

    private void doAAC305() {
        sendPacket(new C02PacketUseEntity(getPlayer(), C02PacketUseEntity.Action.INTERACT));
        if (delay == 0) {
            setTimerSpeed(1.1F);
        }

        if (delay == 2) {
            getPlayer().motionX *= 1.1D;
            getPlayer().motionZ *= 1.1D;
            setMotionY(0.1D);
        } else if (delay > 2) {
            setTimerSpeed(1.0F);
            delay = 0;
        }
        ++delay;
    }

    private void doHypixelOld() {
        setMotionY(0.0D);
        getPlayer().onGround = true;
        getPlayer().motionX *= 1.1D;
        getPlayer().motionZ *= 1.1D;
    }

    private void doMotion() {
        getPlayer().onGround = true;
        setMotionY(0.0D);
        if (isMoving()) {
            PlayerUtil.setSpeed(1.0D);
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            getPlayer().motionY -= 0.5D;
        } else if (mc.gameSettings.keyBindJump.pressed) {
            getPlayer().motionY += 0.5D;
        }
    }

    private void doVanilla() {
        getPlayer().capabilities.isFlying = true;
    }

    private void doNew() {
        motion = 0f;
        motion = 0.0;
        speed = true;
        if (getPlayer().isSneaking()) {
            getPlayer().motionY = -0.5;
        } else {
            if ((getPlayer().motionY < 0.0D) && (getPlayer().isAirBorne) && (!getPlayer().isInWater())
                    && (!getPlayer().isOnLadder()) && (!getPlayer().isInsideOfMaterial(Material.lava))) {
                getPlayer().setSprinting(false);
                getPlayer().motionY = -motion;
                getPlayer().jumpMovementFactor *= 1.21337F;
            }
        }
    }

    private void doIntave() {
        if (isMoving() && !getPlayer().onGround)
            mc.timer.timerSpeed = 0.29f;

        if (timeHelper.hasReached(200) && !getPlayer().isCollidedHorizontally && !getPlayer().isInWater() && !getPlayer().onGround && isMoving()) {
            double multiply = 2.1;
            double yaw = Math.toRadians(getPlayer().rotationYaw);
            double posX = -Math.sin(yaw) * multiply;
            double posZ = Math.cos(yaw) * multiply;
            getPlayer().setPosition(getPlayer().posX + posX, getPlayer().posY, getPlayer().posZ + posZ);
        }
        getPlayer().motionY = -0.01;
    }

    @Override
    public void onEnable() {
        switch (mode.getCurrentMode()) {
            case "Glide": {

                switch (glideMode.getCurrentMode()) {
                    case "New": {
                        time = 0;
                        dtime = 0;
                        getPlayer().setSprinting(false);
                        double x = getPlayer().posX;
                        double y = getPlayer().posY;
                        double z = getPlayer().posZ;
                        double[] d = {0.2D, 0.24D};
                        for (int a = 0; a < 100; a++) {
                            for (int i = 0; i < d.length; i++) {
                                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX,
                                        getPlayer().posY + d[i], getPlayer().posZ, false));
                            }
                        }
                        break;
                    }
                }

                break;
            }
        }
    }

}