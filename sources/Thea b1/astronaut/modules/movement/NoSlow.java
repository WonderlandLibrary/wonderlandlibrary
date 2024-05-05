package astronaut.modules.movement;

import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;

import java.awt.*;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow", Type.Movement, 0, Category.MOVEMENT, Color.orange, "Prevents you from slowing down while using items");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.isBlocking())
            mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(0.0F, 0.0F, true, false));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
