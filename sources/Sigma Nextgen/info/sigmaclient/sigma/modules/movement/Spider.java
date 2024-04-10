package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Spider extends Module {
    public ModeValue type = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Jump", "Spider", "Clip"});
    public NumberValue vanillaSpeed = new NumberValue("Speed", 0.5, 0, 2, NumberValue.NUMBER_TYPE.FLOAT){
        @Override public boolean isHidden() { return !type.is("Vanilla") && !type.is("Spider"); }
    };
    public BooleanValue spoof = new BooleanValue("SpoofGround", false){
        @Override public boolean isHidden() { return !type.is("Vanilla") && !type.is("Jump") && !type.is("Clip") && !type.is("Spider"); }
    };
    public Spider() {
        super("Spider", Category.Movement, "Climp walls like spiders");
     registerValue(type);
     registerValue(vanillaSpeed);
     registerValue(spoof);
    }
    float totalY = 0;
    boolean init = false;
    @Override
    public void onEnable() {
        init = false;
        super.onEnable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPre()) {
            switch (type.getValue()) {
                case "Vanilla":
                    if (mc.player.collidedHorizontally) {
                        mc.player.getMotion().y = vanillaSpeed.getValue().floatValue();
                        if (spoof.isEnable()) {
                            event.onGround = true;
                        }
                    }
                    break;
                case "Spider":
                    if (mc.player.collidedHorizontally) {
                        mc.player.getMotion().y = vanillaSpeed.getValue().floatValue();
                        if (spoof.isEnable()) {
                            event.onGround = true;
                        }
                    }
                    if (mc.player.collidedVertically) {
                        mc.player.getMotion().y = 0;
                    }
                    break;
                case "Jump":
                    if (mc.player.collidedHorizontally) {
                        int round = (int) Math.round(mc.player.getPositionVec().y);
                        if (mc.player.fallDistance > 0) {
                            // |---- (air)ground
                            // | Player
                            // |
                            if (mc.player.getPositionVec().y < round) {
                                mc.player.setPosition(mc.player.getPositionVec().x, round, mc.player.getPositionVec().z);
                                event.y = round;
                                if (spoof.isEnable()) {
                                    event.onGround = true;
                                }
                                // jump
                                mc.player.getMotion().y = 0.41999998688697815;
                            }
                        } else {
                            if (mc.player.getPositionVec().y % 1 == 0) {
                                // jump
                                mc.player.getMotion().y = 0.41999998688697815;
                            }
                        }
                    }
                    break;
                case "Clip":
                    if (mc.player.collidedHorizontally) {
                        int round = (int) Math.round(mc.player.getPositionVec().y);
                        if (mc.player.fallDistance > 0) {
                            // |---- (air)ground
                            // | Player
                            // |
                            if (mc.player.getPositionVec().y < round) {
                                mc.player.setPosition(mc.player.getPositionVec().x + 1E-10, round, mc.player.getPositionVec().z + 1E-10);
                                event.y = round;
                                if (spoof.isEnable()) {
                                    event.onGround = true;
                                }
                                // jump
                                mc.player.getMotion().y = 0.41999998688697815;
                            }
                        } else {
                            if (mc.player.getPositionVec().y % 1 == 0) {
                                // jump
                                mc.player.getMotion().y = 0.41999998688697815;
                            }
                        }
                    }
                    break;
            }
        }
        super.onUpdateEvent(event);
    }
}
