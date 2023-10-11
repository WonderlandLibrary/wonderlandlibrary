package io.github.raze.modules.collection.player;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.world.BlockUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiVoid extends AbstractModule {

    private final ArraySetting mode;
    private final NumberSetting height;
    private final BooleanSetting checkForVoid;

    public AntiVoid() {
        super("AntiVoid", "Doesn't let you to fall in the void.", ModuleCategory.PLAYER);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Flag", "Flag", "Jump", "Ground", "Position"),

                height = new NumberSetting(this, "Falling Height", 0, 10, 5),

                checkForVoid = new BooleanSetting(this, "Check for Void", true)

        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if ((!BlockUtil.isBlockUnderPlayer() || !checkForVoid.get()) && mc.thePlayer.fallDistance > height.get().floatValue() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.capabilities.isFlying) {
                switch(mode.get()) {
                    case "Flag":
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition());
                        break;

                    case "Jump":
                        mc.thePlayer.jump();
                        break;

                    case "Ground":
                        eventMotion.setOnGround(true);
                        break;

                    case "Position":
                        eventMotion.setY(eventMotion.getY() + mc.thePlayer.fallDistance);
                        break;
                }
            }
        }
    }


}
