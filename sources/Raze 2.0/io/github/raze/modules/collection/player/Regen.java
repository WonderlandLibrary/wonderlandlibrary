package io.github.raze.modules.collection.player;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.utilities.collection.visual.ChatUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends AbstractModule {

    public Regen() {
        super("Regen", "Regenerates health (and drains food) more quickly.", ModuleCategory.PLAYER);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if(mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth())
                for (int i = 0; i < 10; i++)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
    }

    @Override
    public void onEnable() {
        ChatUtil.addChatMessage("Regen needs full hunger to work!", true);
    }

}
