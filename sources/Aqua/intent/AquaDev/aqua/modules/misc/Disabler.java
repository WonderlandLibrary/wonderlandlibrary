package intent.AquaDev.aqua.modules.misc;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPacket;
import events.listeners.EventTimerDisabler;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.utils.PathFinder;
import intent.AquaDev.aqua.utils.TimeUtil;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Disabler
extends Module {
    public static int worldChanges;
    private final Queue<Packet<?>> packet = new ConcurrentLinkedDeque();
    public LinkedList packetQueue = new LinkedList();
    public int state;
    public int state2;
    public int state3;
    public int stage;
    public int stage2;
    public int stage3;
    public TimeUtil timer = new TimeUtil();
    public TimeUtil timer2 = new TimeUtil();
    public ArrayList<Packet> packets = new ArrayList();
    public TimeUtil helper3 = new TimeUtil();
    public TimeUtil helper = new TimeUtil();
    public TimeUtil helper2 = new TimeUtil();
    private final boolean inGround = true;
    private final boolean cancelFlag = true;
    private final boolean transaction = true;
    private final boolean transactionMultiply = true;
    private final boolean transactionSend = true;
    private boolean teleported;
    private boolean cancel;
    private TimeUtil delay = new TimeUtil();
    private TimeUtil release = new TimeUtil();
    private boolean releasePacket = false;
    private List<Packet> packetList = new ArrayList();

    public Disabler() {
        super("Disabler", "Disabler", 0, Category.Misc);
        Aqua.setmgr.register(new Setting("WatchdogRandom", (Module)this, false));
        Aqua.setmgr.register(new Setting("Modes", (Module)this, "Ghostly", new String[]{"Ghostly", "BMC", "Cubecraft", "BMC2", "Minebox", "Watchdog", "Watchdog2", "HycraftCombat", "Matrix", "HypixelDev", "Intave"}));
    }

    public static void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static List<Vec3> calculatePath(Vec3 startPos, Vec3 endPos) {
        System.out.println("Test-1");
        PathFinder pathfinder = new PathFinder(startPos, endPos);
        System.out.println("Test");
        pathfinder.calculatePath(5000);
        System.out.println("Test2");
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList path = new ArrayList();
        ArrayList pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add((Object)lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add((Object)pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 30.0) {
                    canContinue = false;
                } else {
                    double smallX = Math.min((double)lastDashLoc.xCoord, (double)pathElm.xCoord);
                    double smallY = Math.min((double)lastDashLoc.yCoord, (double)pathElm.yCoord);
                    double smallZ = Math.min((double)lastDashLoc.zCoord, (double)pathElm.zCoord);
                    double bigX = Math.max((double)lastDashLoc.xCoord, (double)pathElm.xCoord);
                    double bigY = Math.max((double)lastDashLoc.yCoord, (double)pathElm.yCoord);
                    double bigZ = Math.max((double)lastDashLoc.zCoord, (double)pathElm.zCoord);
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!PathFinder.checkPositionValidity((int)x, (int)y, (int)z, (boolean)false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    path.add((Object)lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    public void onEvent(Event e) {
        C0BPacketEntityAction c0B;
        Packet p;
        C16PacketClientStatus clientStatus;
        Packet packet;
        if (e instanceof EventTimerDisabler) {
            packet = EventTimerDisabler.getPacket();
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave")) {
                Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C00PacketKeepAlive());
            }
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Matrix")) {
                if (packet instanceof C00PacketKeepAlive && Disabler.mc.thePlayer.ticksExisted % 5 == 0) {
                    e.setCancelled(true);
                }
                if (packet instanceof C0FPacketConfirmTransaction && Disabler.mc.thePlayer.ticksExisted % 2 == 0) {
                    e.setCancelled(true);
                }
                if (packet instanceof C16PacketClientStatus && (clientStatus = (C16PacketClientStatus)packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    e.setCancelled(true);
                }
            }
        }
        if (e instanceof EventUpdate && mc.isSingleplayer()) {
            return;
        }
        if (e instanceof EventPacket) {
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Intave")) {
                packet = EventPacket.getPacket();
                if (packet instanceof C0FPacketConfirmTransaction && Disabler.mc.thePlayer.ticksExisted % 2 == 0 && !Aqua.moduleManager.getModuleByName("Scaffold").isToggled()) {
                    e.setCancelled(true);
                }
                if (Aqua.moduleManager.getModuleByName("Scaffold").isToggled() && packet instanceof C0BPacketEntityAction) {
                    e.setCancelled(true);
                }
            }
            p = EventPacket.getPacket();
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Matrix")) {
                if (p instanceof C00PacketKeepAlive && Disabler.mc.thePlayer.ticksExisted % 5 == 0) {
                    e.setCancelled(true);
                }
                if (p instanceof C0FPacketConfirmTransaction) {
                    e.setCancelled(true);
                }
                if (p instanceof C16PacketClientStatus && (clientStatus = (C16PacketClientStatus)p).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    e.setCancelled(true);
                }
                if (p instanceof C0BPacketEntityAction) {
                    // empty if block
                }
            }
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("BMC2")) {
                if (p instanceof C01PacketPing) {
                    Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C18PacketSpectate(UUID.randomUUID()));
                    float voidTP = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.78, (double)0.98);
                    Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C18PacketSpectate(UUID.randomUUID()));
                    Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C0CPacketInput(voidTP, voidTP, false, false));
                    e.setCancelled(true);
                }
                if (p instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                    e.setCancelled(true);
                }
            }
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Watchdog")) {
                boolean worldChange = this.packet instanceof S07PacketRespawn;
                if (!worldChange || p instanceof S08PacketPlayerPosLook) {
                    // empty if block
                }
                if (worldChange && p instanceof C0FPacketConfirmTransaction) {
                    e.setCancelled(true);
                }
                if (Disabler.mc.thePlayer.ticksExisted < 50 && p instanceof S08PacketPlayerPosLook) {
                    e.setCancelled(true);
                    S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)p;
                    float watchdogRandom1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)1.0, (double)1.0213131123);
                    s08.y += (double)watchdogRandom1;
                }
            }
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Minebox")) {
                if (p instanceof C0FPacketConfirmTransaction) {
                    e.setCancelled(true);
                }
                if (p instanceof C0BPacketEntityAction) {
                    C0BPacketEntityAction c0B2 = (C0BPacketEntityAction)p;
                    if (c0B2.getAction().equals((Object)C0BPacketEntityAction.Action.START_SPRINTING)) {
                        if (EntityPlayerSP.serverSprintState) {
                            this.sendPacketSilent((Packet)new C0BPacketEntityAction((Entity)Disabler.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            EntityPlayerSP.serverSprintState = false;
                        }
                        e.setCancelled(true);
                    }
                    if (c0B2.getAction().equals((Object)C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                        e.setCancelled(true);
                    }
                }
            }
            if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Cubecraft")) {
                C03PacketPlayer c03;
                if (p instanceof C03PacketPlayer.C06PacketPlayerPosLook && !Aqua.moduleManager.getModuleByName("Scaffold").isToggled()) {
                    e.setCancelled(true);
                }
                if (p instanceof C03PacketPlayer && !(c03 = (C03PacketPlayer)p).isMoving() && !Disabler.mc.thePlayer.isUsingItem()) {
                    e.setCancelled(true);
                }
                if (p instanceof C00PacketKeepAlive) {
                    e.setCancelled(true);
                }
                if (p instanceof C0FPacketConfirmTransaction) {
                    e.setCancelled(true);
                }
                if (p instanceof C0CPacketInput) {
                    e.setCancelled(true);
                }
                if (p instanceof C01PacketPing) {
                    e.setCancelled(true);
                }
                if (Killaura.target == null) {
                    assert (p instanceof C0BPacketEntityAction);
                    c0B = (C0BPacketEntityAction)p;
                    if (c0B.getAction().equals((Object)C0BPacketEntityAction.Action.START_SPRINTING)) {
                        if (EntityPlayerSP.serverSprintState) {
                            this.sendPacketSilent((Packet)new C0BPacketEntityAction((Entity)Disabler.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            EntityPlayerSP.serverSprintState = false;
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
        if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("BMC")) {
            p = EventPacket.getPacket();
            if (Disabler.mc.thePlayer.ticksExisted < 200 && p instanceof S07PacketRespawn) {
                this.teleported = false;
            }
            if (Disabler.mc.thePlayer.ticksExisted < 250 && p instanceof S07PacketRespawn) {
                this.packet.clear();
                return;
            }
            if (p instanceof C0BPacketEntityAction) {
                c0B = (C0BPacketEntityAction)p;
                if (c0B.getAction().equals((Object)C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        this.sendPacketSilent((Packet)new C0BPacketEntityAction((Entity)Disabler.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    e.setCancelled(true);
                }
                if (c0B.getAction().equals((Object)C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    e.setCancelled(true);
                }
            }
            if (p instanceof C00PacketKeepAlive || p instanceof C0FPacketConfirmTransaction) {
                this.packet.add((Object)p);
                e.setCancelled(true);
                if (this.packet.size() > 500) {
                    this.sendPacketSilent((Packet)this.packet.poll());
                }
            }
            if (p instanceof C03PacketPlayer) {
                C03PacketPlayer c03 = (C03PacketPlayer)p;
                if (Disabler.mc.thePlayer.ticksExisted % 50 == 0) {
                    Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C18PacketSpectate(UUID.randomUUID()));
                    float voidTP = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.78, (double)0.98);
                    Disabler.sendPacketUnlogged((Packet<? extends INetHandler>)new C0CPacketInput(voidTP, voidTP, false, false));
                }
                if (Disabler.mc.thePlayer.ticksExisted % 120 == 0) {
                    float voidTP = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.01, (double)20.0);
                }
            }
        }
        if (Aqua.setmgr.getSetting("DisablerModes").getCurrentMode().equalsIgnoreCase("Ghostly")) {
            if (Disabler.mc.thePlayer != null && Disabler.mc.thePlayer.ticksExisted == 1) {
                this.packetQueue.clear();
            }
            if ((packet = EventPacket.getPacket()) instanceof S08PacketPlayerPosLook) {
                this.getClass();
                C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)packet;
                double x = ((S08PacketPlayerPosLook)packet).getX() - Disabler.mc.thePlayer.posX;
                double y = ((S08PacketPlayerPosLook)packet).getY() - Disabler.mc.thePlayer.posY;
                double z = ((S08PacketPlayerPosLook)packet).getZ() - Disabler.mc.thePlayer.posZ;
                double diff = Math.sqrt((double)(x * x + y * y + z * z));
                if (diff > 0.0) {
                    e.setCancelled(true);
                }
            } else if (packet instanceof C03PacketPlayer) {
                this.getClass();
                if (packet instanceof C01PacketPing) {
                    // empty if block
                }
                if (packet instanceof C13PacketPlayerAbilities) {
                    // empty if block
                }
                if (Disabler.mc.thePlayer.ticksExisted % 75 == 0) {
                    System.out.println("Sda");
                    float yValue = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)1.052, (double)0.5213);
                    this.sendPacketSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY, Disabler.mc.thePlayer.posZ, false));
                    this.sendPacketSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY - 11.0, Disabler.mc.thePlayer.posZ, true));
                    this.sendPacketSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY, Disabler.mc.thePlayer.posZ, false));
                    e.setCancelled(true);
                }
            } else if (packet instanceof C0FPacketConfirmTransaction) {
                this.getClass();
                boolean c0f = true;
                this.getClass();
                boolean c0fMultiply = true;
                if (c0f) {
                    if (!c0fMultiply) {
                        this.packetQueue.add((Object)packet);
                        e.setCancelled(true);
                    } else {
                        for (int i = 0; i < 1; ++i) {
                            this.packetQueue.add((Object)packet);
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    public void sendPacketSilent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet, null, new GenericFutureListener[0]);
    }

    public void onDisable() {
        if (mc.isSingleplayer()) {
            return;
        }
        if (this.packets != null && this.packets.size() > 0) {
            this.packets.clear();
        }
        if (this.packetQueue != null && this.packetQueue.size() > 0) {
            this.packetQueue.clear();
        }
        this.timer.reset();
        this.timer2.reset();
        this.state = 0;
        this.state2 = 0;
        this.state3 = 0;
        this.stage = 0;
        this.stage2 = 0;
        this.stage3 = 0;
    }

    public void onEnable() {
        if (mc.isSingleplayer()) {
            return;
        }
        if (Disabler.mc.thePlayer != null) {
            Disabler.mc.thePlayer.ticksExisted = 0;
        }
    }

    public boolean isBlockUnder() {
        for (int i = (int)Disabler.mc.thePlayer.posY; i >= 0; --i) {
            BlockPos position = new BlockPos(Disabler.mc.thePlayer.posX, (double)i, Disabler.mc.thePlayer.posZ);
            if (Disabler.mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}
