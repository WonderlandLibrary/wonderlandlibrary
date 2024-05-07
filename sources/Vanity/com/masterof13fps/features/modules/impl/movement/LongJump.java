package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.entity.EntityUtils;
import com.masterof13fps.utils.entity.PlayerUtil;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "LongJump", category = Category.MOVEMENT, description = "Lets you jump very far away")
public class LongJump extends Module {
    private boolean jump;

    public Setting mode = new Setting("Mode", this, "NCP", new String[] {"AAC Old", "NCP", "MineSecure", "CubeCraft",
    "RedeSky", "BlocksMC"});

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {
        notify.notification("Verwendungshinweis", "Drücke JUMP um LongJump zu verwenden!", NotificationType.INFO, 5);
    }

    @Override
    public void onDisable() {
        setTimerSpeed(1.0f);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion && getGameSettings().keyBindJump.isKeyDown()) {
            switch (mode.getCurrentMode()) {
                case "AAC Old": {
                    doAACOld();
                    break;
                }
                case "NCP": {
                    doNCP();
                    break;
                }
                case "MineSecure": {
                    doMineSecure();
                    break;
                }
                case "CubeCraft": {
                    doCubeCraft();
                    break;
                }
                case "RedeSky": {
                    doRedeSky();
                    break;
                }
                case "BlocksMC": {
                    doBlocksMC();
                    break;
                }
            }
        }
    }

    private void doBlocksMC() {
        if(getPlayer().onGround){
            setMotionY(0.75);
        }

        setSpeed(0.45);
    }

    private void doRedeSky() {
        if(getPlayer().onGround){
            setMotionY(0.5);
        }else{
            getPlayer().motionY += 0.025;
        }

        setSpeed(getBaseMoveSpeed() + 0.5);

        if(getPlayer().fallDistance != 0.0){
            this.toggle();
        }
    }

    private void doCubeCraft() {
        if(getPlayer().onGround){
            setMotionY(0.42);
            timeHelper.reset();
        }else{
            setMotionY(0.1);
            setSpeed(3.6);
            setTimerSpeed(0.4f);
        }

        if(timeHelper.hasReached(650L)){
            this.toggle();
        }
    }

    private void doMineSecure() {
        if (isMoving()) {
            if ((getPlayer().onGround) && !(getPlayer().isInWater())) {
                mc.timer.timerSpeed = 1.0F;
                getPlayer().motionY = 0.54D;
            } else if (!getPlayer().isInWater()) {
                PlayerUtil.setSpeed(3.0D);
            }
        } else {
            getPlayer().motionZ = (getPlayer().motionX = 0.0D);
        }
    }

    private void doNCP() {
        if ((isMoving()) && (getPlayer().fallDistance < 1.0F)) {
            float x = (float) -Math.sin(PlayerUtil.getDirection());
            float z = (float) Math.cos(PlayerUtil.getDirection());
            if (getPlayer().isCollidedVertically && mc.gameSettings.keyBindJump.pressed) {
                getPlayer().motionX = (x * 0.29F);
                getPlayer().motionZ = (z * 0.29F);
            }
            if (getPlayer().motionY == 0.33319999363422365D) {
                getPlayer().motionX = (x * 1.261D);
                getPlayer().motionZ = (z * 1.261D);
            }
        }
    }

    private void doAACOld() {
        mc.gameSettings.keyBindForward.pressed = false;
        if (getPlayer().onGround) {
            jump = true;
        }

        if (getPlayer().onGround && timeHelper.isDelayComplete(500L)) {
            getPlayer().motionY = 0.42D;
            PlayerUtil.toFwd(2.3D);
            timeHelper.reset();
        } else if (!getPlayer().onGround && jump) {
            getPlayer().motionX = getPlayer().motionZ = 0.0D;
            jump = false;
        }
    }

}
