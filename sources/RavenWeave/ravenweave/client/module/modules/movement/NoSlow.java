package ravenweave.client.module.modules.movement;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.weavemc.loader.api.event.SubscribeEvent;
import ravenweave.client.event.impl.PacketEvent;
import ravenweave.client.event.impl.UpdateEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.ComboSetting;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.SliderSetting;

import java.util.Objects;

public class NoSlow extends Module {
    public static SliderSetting speed;
    public static ComboSetting<modes> mode;

    public NoSlow() {
        super("NoSlow", ModuleCategory.movement);
        this.registerSetting(new DescriptionSetting("Default is 80% motion reduction."));
        this.registerSetting(mode = new ComboSetting<>("Mode", modes.VANILLA));
        this.registerSetting(speed = new SliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent e) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            if (mode.getMode() == modes.NOS30) {
                if (e.getPacket() instanceof S30PacketWindowItems) {
                    if (mc.thePlayer.isUsingItem()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent e) {
        if (mode.getMode() == modes.NCP) {
            if (mc.thePlayer.isUsingItem()) {
                if (e.isPre()) {
                    if (mc.thePlayer.isBlocking()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                } else if (e.isPost()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                }
            }
        }
    }

    public enum modes {
        VANILLA, NCP, NOS30
    }

}
