package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerSlowdownEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;


public class NoSlowdown extends Cheat {
    public static double RANDOM = RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    public static BlockPos HYPIXEL_BLOCK_POS = new BlockPos(RANDOM, RANDOM, RANDOM);
    protected StringsProperty prop_mode = new StringsProperty("Mode", "How this cheat will function.", null, false, false, new String[]{"NCP", "Hypixel", "Vanilla"}, new Boolean[]{true, false, false});
    Stopwatch timer;
    int counter;//Will be used in Matrix noslow
    private boolean blocking;

    public NoSlowdown() {
        super("NoSlow", "Prevents you from slowing down when blocking, eating, etc.", CheatCategory.MOVEMENT);
        registerProperties(prop_mode);
        timer = new Stopwatch();
    }


    @Collect
    public void onSendPacket(SendPacketEvent sendPacketEvent) {
        if (sendPacketEvent.getPacket() instanceof C07PacketPlayerDigging) {
            blocking = false;
        }

        if (sendPacketEvent.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            blocking = true;
        }
    }

    @Collect
    public void onPlayerSlowdown(PlayerSlowdownEvent playerSlowdownEvent) {
        if (prop_mode.getValue().get("Vanilla")) {
            if (this.getState()) {
                playerSlowdownEvent.setCancelled(true);
            }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        setMode(prop_mode.getSelectedStrings().get(0));

        if (e.isPre()) {

            if (prop_mode.getValue().get("NCP")) {
                if (holdingSword() && getPlayer().isBlocking() && blocking && getPlayer().isMoving()) {
                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") ? new BlockPos(-.8, -.8, -.8) : BlockPos.ORIGIN, EnumFacing.DOWN));
                    blocking = false;
                }
            }
        } else {
            if (prop_mode.getValue().get("NCP")) {
                if (holdingSword() && getPlayer().isBlocking() && !blocking) {
                    getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), 255, getPlayer().getHeldItem(), 0, 0, 0));
                    blocking = true;
                }
            }
        }


        if (prop_mode.getValue().get("Hypixel")) {
            if (mc.thePlayer.isBlocking()) {
                if (e.isPre())
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, HYPIXEL_BLOCK_POS, EnumFacing.DOWN));

                if (!e.isPre())
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(HYPIXEL_BLOCK_POS, 255, Minecraft.getMinecraft().thePlayer.getHeldItem(), 0, 0, 0));
            }
        }

    }


    private boolean holdingSword() {
        return getPlayer().getCurrentEquippedItem() != null && getPlayer().inventory.getCurrentItem().getItem() instanceof ItemSword;
    }
}
