package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.misc.TPAura;
import dev.rise.util.player.PacketUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// todo finish this
@ModuleInfo(name = "BedwarsTP", description = "Teleports to a specified player in Bedwars", category = Category.MOVEMENT)
public final class BedwarsTP extends Module {

    private boolean awaitingTeleport = false;

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) event.getPacket()).chatComponent.getUnformattedText();

            if (message.contains(mc.thePlayer.getName()) && message.contains("fell into the void")) {
                awaitingTeleport = true;
            }
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted == 1) awaitingTeleport = false;

        if (awaitingTeleport && mc.thePlayer.posY >= 120) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, 121, mc.thePlayer.posZ);

            List<EntityPlayer> playerList = mc.theWorld.playerEntities.stream().filter(this::isValid)
                    .sorted(Comparator.comparingDouble(player -> player.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());

            if (!playerList.isEmpty()) {
                EntityPlayer target = playerList.get(0);
                BlockPos destination = new BlockPos(target.posX, target.posY, target.posZ);

                TPAura.tpToLocation(
                        100, 8, 7.5,
                        new ArrayList<>(), new ArrayList<>(),
                        destination
                );

                mc.thePlayer.setPosition(target.posX, target.posY, target.posZ);
                awaitingTeleport = false;

                // right click on player, opening spec menu
                PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT_AT));
                PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
            } else {
                Rise.addChatMessage("playerList is empty!!");
            }
        }
    }

    private boolean isValid(EntityPlayer player) {
        return (player != mc.thePlayer && !player.isInvisible() && mc.thePlayer.getDistanceToEntity(player) > 20);
    }
}
