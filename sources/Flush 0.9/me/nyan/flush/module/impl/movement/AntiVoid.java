package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.util.Vec3;

public class AntiVoid extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "NCP", "Hypixel");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", this, 2, 1, 10);
    private final BooleanSetting onlyVoid = new BooleanSetting("Only Void", this, true);

    private Vec3 lastOnGround = null;

    public AntiVoid() {
        super("AntiVoid", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (MovementUtils.isOnGround(0.1)) {
            lastOnGround = new Vec3(e.getX(), e.getY(), e.getZ());
        }

        switch (mode.getValue().toLowerCase()) {
            case "hypixel":
                if ((PlayerUtils.isBlockUnder() && onlyVoid.getValue()) || MovementUtils.isOnGround(0.1) ||
                        mc.thePlayer.motionY >= -0.05 || lastOnGround == null) {
                    return;
                }
                if (lastOnGround.yCoord - mc.thePlayer.posY < fallDistance.getValue()) {
                    e.cancel();
                    break;
                }

                e.setPosition(lastOnGround.xCoord, lastOnGround.yCoord, lastOnGround.zCoord);
                mc.thePlayer.setPosition(lastOnGround.xCoord, lastOnGround.yCoord, lastOnGround.zCoord);
                break;

            case "ncp":
                if ((!PlayerUtils.isBlockUnder() || !onlyVoid.getValue()) && mc.thePlayer.fallDistance >
                        fallDistance.getValueInt() && mc.thePlayer.motionY < -0.1 && !MovementUtils.isOnGround(0.05)) {
                    e.setY(e.getY() + 1);
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}