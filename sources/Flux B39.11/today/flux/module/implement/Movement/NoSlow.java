package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ModeValue;
import today.flux.utility.ChatUtils;

public class NoSlow extends Module {
    public static ModeValue mode = new ModeValue("NoSlow", "Mode", "Normal", "Hypixel");
    public static int ticks = 0;

    public NoSlow() {
        super("NoSlow", Category.Movement, false);
    }

    @EventTarget
    public void onPacketRec(PacketReceiveEvent e) {
        if (mode.isCurrentMode("Hypixel") && e.getPacket() instanceof S30PacketWindowItems && (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking())) {
            ticks = mc.thePlayer.ticksExisted;
            e.setCancelled(true);
        }
    }

    @EventTarget
    public void onPost(PostUpdateEvent e) {
        if (mode.isCurrentMode("Hypixel")) {
            if (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) {
                int process = Math.min(Math.round(((mc.thePlayer.ticksExisted - ticks) / 30f * 100)), 100);
                ChatUtils.debug("Eating: " + process + "%");
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
                        mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
            } else {
                ticks = mc.thePlayer.ticksExisted;
            }
        }
    }

    public boolean canBlock() {
        return mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.isUsingItem();
    }
}
