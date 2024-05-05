package io.github.raze.modules.collection.player;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends AbstractModule {

    private final NumberSetting timer;

    public Phase() {
        super("Phase", "You can go through doors in old versions of nocheatplus!", ModuleCategory.PLAYER);
        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                timer = new NumberSetting(this, "Timer", 0.05, 10, 1)

        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);

            if(mc.thePlayer.isCollidedHorizontally) {
                mc.timer.timerSpeed = (float) timer.get().doubleValue();
                mc.thePlayer.setPosition(mc.thePlayer.posX - x * 0.005, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.005);
            }

            if(mc.thePlayer.isEntityInsideOpaqueBlock()) {
                mc.timer.timerSpeed = (float) timer.get().doubleValue();
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x * 1.5, mc.thePlayer.posY, mc.thePlayer.posZ + z * 1.5, false));
            }

            if(!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isEntityInsideOpaqueBlock())
                mc.timer.timerSpeed = 1;
        }
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindUseItem.pressed = false;
        mc.timer.timerSpeed = 1;
    }

}
