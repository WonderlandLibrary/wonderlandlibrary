package tech.dort.dortware.impl.modules.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.EnumValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.api.property.impl.interfaces.INameable;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.utils.networking.PacketUtil;

public class FastUse extends Module {

    private final NumberValue packets = new NumberValue("Packets", this, 20, 5, 100, true);
    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());

    public FastUse(ModuleData moduleData) {
        super(moduleData);
        register(mode, packets);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            for (int i = 0; i < packets.getCastedValue().intValue(); i++) {
                switch (mode.getValue()) {
                    case VANILLA:
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                        break;
                    case VERUS:
                        if (mc.thePlayer.onGround && !mc.thePlayer.isMoving()) {
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                        }
                        break;
                }
            }
        }
    }
}

enum Mode implements INameable {
    VANILLA("Vanilla"), VERUS("Old Verus");

    private final String displayName;

    Mode(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
