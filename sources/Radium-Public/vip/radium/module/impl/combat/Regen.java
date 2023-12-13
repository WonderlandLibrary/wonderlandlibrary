package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Regen", category = ModuleCategory.COMBAT)
public final class Regen extends Module {

    private final DoubleProperty packetsProperty = new DoubleProperty("Packets", 10, 0, 100, 1);

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre() && MovementUtils.isOnGround() && Wrapper.getPlayer().getHealth() < Wrapper.getPlayer().getMaxHealth())
            for (int i = 0; i < packetsProperty.getValue().intValue(); i++)
                Wrapper.sendPacketDirect(new C03PacketPlayer(true));
    };
}
