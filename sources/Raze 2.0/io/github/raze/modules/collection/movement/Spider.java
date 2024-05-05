package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Spider extends AbstractModule {

    private final ArraySetting mode;
    private final NumberSetting motion;

    private long ticks = 0;

    public Spider() {
        super("Spider", "Automatically climbs walls for you.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Normal", "Normal", "Motion", "Vulcan", "Vulcan2", "Verus"),

                motion = new NumberSetting(this, "Motion", 0, 1, 0.8)
                        .setHidden(() -> !mode.compare("Motion"))
        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {

        ticks++;
        if (!MoveUtil.isMoving())
            return;

        if (eventMotion.getState() == Event.State.PRE) {

            switch (mode.get()) {
                case "Normal":
                    if (mc.thePlayer.isCollidedHorizontally())
                        mc.thePlayer.jump();
                    break;

                case "Motion":
                    if (mc.thePlayer.isCollidedHorizontally)
                        mc.thePlayer.motionY = this.motion.get().doubleValue();
                    break;

                case "Vulcan":
                    if (mc.thePlayer.isCollidedHorizontally()) {
                        if (ticks % 3 == 0) {
                            eventMotion.setOnGround(true);
                            mc.thePlayer.motionY = 0.5;
                        }
                    }
                    break;

                case "Vulcan2":
                    final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);
                    if(!mc.thePlayer.isEntityInsideOpaqueBlock() && mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x * 0.00005, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.00005, false));
                    }

                    if (ticks % 3 == 0 && mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.jump();
                    }

                    break;

                case "Verus":
                    if (mc.thePlayer.isCollidedHorizontally)
                        if (mc.thePlayer.ticksExisted % 3 == 0)
                            mc.thePlayer.motionY = 0.42f;
                    break;
            }
        }

    }

}