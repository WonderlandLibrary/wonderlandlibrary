package koks.module.utilities;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.TimeHelper;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.BlockPos;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:28
 */

@Module.Info(name = "Disabler", description = "Disable AntiCheat Checks", category = Module.Category.UTILITIES)
public class Disabler extends Module {

    //TODO: Adding Hypixel Disabler (Interact with Entity)

    /* Mode Switch */
    @Value(name = "Mode", modes = {"None", "GommeFFA", "Cytooxien", "Accurancy", "Verus", "Redesky", "NCP"})
    String mode = "GommeFFA";

    /* NCP */
    @Value(name = "NCP-Timer Disabler", displayName = "Timer Disabler")
    boolean ncpTimerDisabler = true;

    /* Redesky */
    @Value(name = "Redesky-Packet Kick", displayName = "Redesky Packet Kick")
    boolean redeskyPacketKick = true;

    /* Accurancy */
    @Value(name = "Accurancy-Full", displayName = "Accurancy Full")
    boolean accurancyFull = false;

    @Value(name = "Accurancy-Capability", displayName = "Accurancy Capability")
    boolean accurancyCapability = false;

    /* Verus */
    @Value(name = "Verus-b3733-Flight Disabler", displayName = "Verus b3733 Flight Disabler")
    boolean verus3733FlightDisabler = false;

    @Value(name = "Verus-b3733-Scaffold Disabler", displayName = "Verus b3733 Scaffold Disabler")
    boolean verus3733ScaffoldDisabler = false;

    @Value(name = "Verus-b3733-Range Disabler", displayName = "Verus b3733 Range Disabler")
    boolean verus3733RangeDisabler = false;

    /* Basic Disabler */
    @Value(name = "Cancel Confirm Transaction")
    boolean cancelConfirmTransaction = false;

    @Value(name = "Cancel Keep Alive")
    boolean cancelKeepAlive = false;

    @Value(name = "Cancel Input Packet")
    boolean cancelInputPacket = false;

    final TimeHelper timeHelper = new TimeHelper();

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if (name.contains("-")) {
            final String[] split = name.split("-");
            return split[0].equalsIgnoreCase(mode);
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Accurancy":
                    if (accurancyCapability) {
                        if (getPlayer().ticksExisted % 5 == 0) {
                            final PlayerCapabilities capabilities = new PlayerCapabilities();
                            capabilities.isFlying = true;
                            capabilities.allowFlying = true;
                            capabilities.isCreativeMode = true;
                            sendPacket(new C13PacketPlayerAbilities(capabilities));
                        }
                    }
                    if (accurancyFull) {
                        if (mc.isSingleplayer())
                            return;
                        if (timeHelper.hasReached(100)) {
                            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), 1E159, getZ(), true));
                            timeHelper.reset();
                        }
                    }
                    break;
                case "Verus":
                    if (verus3733FlightDisabler) {
                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                        sendPacketUnlogged(new C08PacketPlayerBlockPlacement(new BlockPos(getX(), getY() - 1.5, getZ()), 1, new ItemStack(Blocks.stone.getItem(getWorld(), new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                    }
                    break;
                case "NCP":
                    if (ncpTimerDisabler) {
                        if (getPlayer().ticksExisted % 30 == 0) {
                            sendPacketUnlogged(new C03PacketPlayer.C06PacketPlayerPosLook(getX(), getY() - (getPlayer().onGround ? 0.1D : 1.1D), getZ(), getYaw(), getPitch(), getPlayer().onGround));
                        }
                    }
                    break;
            }
        }
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                switch (mode) {
                    case "Cytooxien":
                        if (packet instanceof S3FPacketCustomPayload) {
                            packetEvent.setCanceled(true);
                        }
                        break;
                    case "GommeFFA":
                        if (packet instanceof final S32PacketConfirmTransaction confirmTransactionEvent) {
                            if (confirmTransactionEvent.getActionNumber() <= 0) {
                                event.setCanceled(true);
                            }
                        }
                        break;
                }
            } else {
                switch (mode) {
                    case "Redesky":
                        if (redeskyPacketKick) {
                            if (packet instanceof C00PacketKeepAlive || packet instanceof C0FPacketConfirmTransaction || packet instanceof C13PacketPlayerAbilities || packet instanceof C17PacketCustomPayload || packet instanceof C18PacketSpectate)
                                packetEvent.setCanceled(true);
                        }
                        break;
                    case "Verus":
                        if (verus3733ScaffoldDisabler) {
                            if (packet instanceof final C08PacketPlayerBlockPlacement blockPlacement) {
                                blockPlacement.stack = null;
                            }
                        }
                        if (verus3733RangeDisabler) {
                            if (packet instanceof final C0FPacketConfirmTransaction packetConfirmTransaction) {
                                if (packetConfirmTransaction.getWindowId() >= 0)
                                    packetEvent.setCanceled(true);

                            }
                        }
                        break;
                }
                if (cancelConfirmTransaction)
                    if (packet instanceof C0FPacketConfirmTransaction)
                        event.setCanceled(true);
                if (cancelInputPacket)
                    if (packet instanceof C0CPacketInput)
                        event.setCanceled(true);
                if (cancelKeepAlive)
                    if (packet instanceof C00PacketKeepAlive)
                        event.setCanceled(true);
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
