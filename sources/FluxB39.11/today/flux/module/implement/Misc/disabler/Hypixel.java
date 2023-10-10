package today.flux.module.implement.Misc.disabler;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.TargetStrafe;
import today.flux.module.implement.Movement.Speed;
import today.flux.module.value.BooleanValue;
import today.flux.utility.ChatUtils;
import today.flux.utility.MoveUtils;
import today.flux.utility.ServerUtils;
import today.flux.utility.TimeHelper;
import today.flux.utility.tojatta.api.utilities.angle.Angle;
import today.flux.utility.tojatta.api.utilities.angle.AngleUtility;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Hypixel extends SubModule {
    public static AngleUtility angleUtility = new AngleUtility(110, 120, 30, 40);
    public static Angle lastAngle;
    public static float yawDiff;

    public static BooleanValue timerBypass = new BooleanValue("Disabler", "Hypixel Timer Disabler", true);
    public static BooleanValue lobbyCheck = new BooleanValue("Disabler", "Hypixel Lobby Check", true);

    Queue<C0FPacketConfirmTransaction> confirmTransactionQueue = new ConcurrentLinkedQueue<>();
    Queue<C00PacketKeepAlive> keepAliveQueue = new ConcurrentLinkedQueue<>();
    public static TimeHelper timer = new TimeHelper();
    TimeHelper lastRelease = new TimeHelper();

    int lastUid, cancelledPackets;
    public static boolean hasDisabled;

    public Hypixel() {
        super("Hypixel", "Disabler");
    }

    public CopyOnWriteArrayList<C0EPacketClickWindow> clickWindowPackets = new CopyOnWriteArrayList<>();

    public TimeHelper timedOutTimer = new TimeHelper();
    public boolean isCraftingItem = false;

    @EventTarget
    public void onRespawn(RespawnEvent e) {
        confirmTransactionQueue.clear();
        keepAliveQueue.clear();

        hasDisabled = false;
        lastUid = cancelledPackets = 0;

        clickWindowPackets.clear();
        isCraftingItem = false;
    }

    @EventTarget
    public void onSendPacket(PacketSendEvent e) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            doInvMove(e);

            if (timerBypass.getValueState() && hasDisabled) {
                if (e.getPacket() instanceof C03PacketPlayer && !(e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                    cancelledPackets ++;
                    e.setCancelled(true);
                }
            }

            // Disabler
            if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                processConfirmTransactionPacket(e);
            } else if (e.getPacket() instanceof C00PacketKeepAlive) {
                processKeepAlivePacket(e);
            } else if (e.getPacket() instanceof C03PacketPlayer) {
                processPlayerPosLooksPacket(e);
            }
        }
    }

    public void doInvMove(PacketSendEvent e) {
        if (e.getPacket() instanceof C16PacketClientStatus) {
            C16PacketClientStatus clientStatus = ((C16PacketClientStatus) e.getPacket());
            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                e.setCancelled(true);
            }
        }

        if (e.getPacket() instanceof C0DPacketCloseWindow) {
            C0DPacketCloseWindow closeWindow = ((C0DPacketCloseWindow) e.getPacket());
            if (closeWindow.windowId == 0) {
                if (isCraftingItem) {
                    isCraftingItem = false;
                }
                e.setCancelled(true);
            }
        }

        if (e.getPacket() instanceof C0EPacketClickWindow) {
            C0EPacketClickWindow clickWindow = ((C0EPacketClickWindow) e.getPacket());
            if (clickWindow.getWindowId() == 0) {
                if (!isCraftingItem && clickWindow.getSlotId() >= 1 && clickWindow.getSlotId() <= 4) {
                    isCraftingItem = true;
                }

                if (isCraftingItem && clickWindow.getSlotId() == 0 && clickWindow.getClickedItem() != null) {
                    isCraftingItem = false;
                }

                timedOutTimer.reset();
                e.setCancelled(true);
                clickWindowPackets.add(clickWindow);
            }
        }

        boolean isDraggingItem = false;

        if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
            if (Minecraft.getMinecraft().thePlayer.inventory.getItemStack() != null) {
                isDraggingItem = true;
            }
        }

        if (mc.thePlayer.ticksExisted % 5 == 0 && !clickWindowPackets.isEmpty() && !isDraggingItem && !isCraftingItem) {
            ChatUtils.debug ("Release Click Packets");
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            for (C0EPacketClickWindow clickWindowPacket : clickWindowPackets) {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(clickWindowPacket);
            }
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(new C0DPacketCloseWindow(0));
            clickWindowPackets.clear();
            timedOutTimer.reset();
        }
    }

    @EventTarget
    public void onPacket(PacketReceiveEvent e) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) e.getPacket());
                ChatUtils.debug("S08: " + packet.getX() + " " + packet.getY() + " " + packet.getZ());
                if (!hasDisabled && mc.thePlayer.ticksExisted > 20) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            if (mc.thePlayer.ticksExisted % 40 == 0) {
                int rate = (int) ((cancelledPackets / 40f) * 100);
                ChatUtils.debug("Movement Handler: " + rate + "%");
                cancelledPackets = 0;
            }

            if (MoveUtils.isMoving() && (ModuleManager.strafeMod.isEnabled() || ModuleManager.speedMod.isEnabled())) {
                if (!ModuleManager.scaffoldMod.isEnabled()) {
                    float targetYaw = e.getYaw();
                    if (Speed.isTargetStrafing) {
                        targetYaw = TargetStrafe.currentYaw;
                    } else {
                        if (mc.gameSettings.keyBindBack.pressed) {
                            targetYaw += 180;
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw += 45;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw -= 45;
                            }
                        } else if (mc.gameSettings.keyBindForward.pressed) {
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw -= 45;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw += 45;
                            }
                        } else {
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw -= 90;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw += 90;
                            }
                        }
                    }
                    Angle angle = angleUtility.smoothAngle(new Angle(targetYaw, e.getYaw()), lastAngle, 120, 360);
                    yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - angle.getYaw());
                    e.setYaw(angle.getYaw());
                }
            }

            lastAngle = new Angle(e.getYaw(), e.getPitch());


            if (hasDisabled) {
                if (confirmTransactionQueue.isEmpty()) {
                    lastRelease.reset();
                } else {
                    if (confirmTransactionQueue.size() >= 6) {
                        while (!keepAliveQueue.isEmpty())
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(keepAliveQueue.poll());

                        while (!confirmTransactionQueue.isEmpty()) {
                            C0FPacketConfirmTransaction poll = confirmTransactionQueue.poll();
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(poll);
                        }
                    }
                }
            }
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processConfirmTransactionPacket(PacketSendEvent e) {
        C0FPacketConfirmTransaction packet = ((C0FPacketConfirmTransaction) e.getPacket());
        int windowId = packet.getWindowId();
        int uid = packet.getUid();

        if (windowId != 0 || uid >= 0) {
            ChatUtils.debug("Inventory synchronized!");
        } else {
            if (uid == --lastUid) {
                if (!hasDisabled) {
                    ChatUtils.debug("Watchdog disabled.");
                    hasDisabled = true;
                }
                confirmTransactionQueue.offer(packet);
                e.setCancelled(true);
            }
            lastUid = uid;
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processKeepAlivePacket(PacketSendEvent e) {
        C00PacketKeepAlive packet = ((C00PacketKeepAlive) e.getPacket());
        if (hasDisabled) {
            keepAliveQueue.offer(packet);
            e.setCancelled(true);
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processPlayerPosLooksPacket(PacketSendEvent e) {
        if (!hasDisabled && !ServerUtils.isHypixelLobby()) {
            e.setCancelled(true);
        }
    }
}