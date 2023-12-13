package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MatrixDisablerImpl implements ModeImpl<Disabler> {

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Matrix";
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (!e.isPre()) return;
        e.onGround = false;
        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, EnumFacing.DOWN.getIndex(), null, 0.0F, 0.0F, 0.0F));
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}