/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import de.gerrygames.viarewind.utils.ChatUtil;
import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.motion.TeleportEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.movement.Fly;
import dev.rise.module.impl.movement.LongJump;
import dev.rise.module.impl.player.Scaffold;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This module is designed to disable an anticheat or some of its checks
 * in order to make bypassing easier/having extreme bypasses.
 */
@ModuleInfo(name = "Disabler", description = "Disables some servers AntiCheats", category = Category.OTHER)
public final class Disabler extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final BooleanSetting hypixel = new BooleanSetting("Hypixel", this, false);
    private final BooleanSetting mineland = new BooleanSetting("Mineland", this, false);
    private final BooleanSetting mmcKeepSprint = new BooleanSetting("Minemenclub Keepsprint Check", this, false);
    private final BooleanSetting mmcReach = new BooleanSetting("Minemenclub Reach Semi", this, false);
    private final BooleanSetting hypixelSlime = new BooleanSetting("Hypixel Slime", this, false);
    private final BooleanSetting dynamicPvP = new BooleanSetting("Dynamic PvP", this, false);
    private final BooleanSetting ghostlyCombat = new BooleanSetting("Ghostly Combat", this, false);
    private final BooleanSetting ghostly = new BooleanSetting("Ghostly", this, false);
    private final BooleanSetting lunar = new BooleanSetting("Lunar", this, false);
    private final BooleanSetting position = new BooleanSetting("Position Edit", this, false);
    private final BooleanSetting vulcanCombat = new BooleanSetting("Vulcan Reach A", this, false);
    private final BooleanSetting verusCombat = new BooleanSetting("Verus Combat", this, false);
    private final BooleanSetting verus = new BooleanSetting("Verus", this, false);
    private final BooleanSetting blocksMC = new BooleanSetting("BlocksMC", this, false);
    private final BooleanSetting sentinel = new BooleanSetting("Sentinel", this, false);
    private final BooleanSetting shartemis = new BooleanSetting("Shartemis Movement", this, false);

    //Any disabler after this line will be hidden if this is true
    private final BooleanSetting hideOldDisablers = new BooleanSetting("Hide Old Disablers", this, true);

    private final NoteSetting oldModeSettings = new NoteSetting("Old Mode Settings", this);

    private final BooleanSetting verusOld = new BooleanSetting("Verus Old", this, false);
    private final BooleanSetting spartan = new BooleanSetting("Spartan", this, false);
    private final BooleanSetting voidTp = new BooleanSetting("Void TP", this, false);
    private final BooleanSetting clip = new BooleanSetting("Clip", this, false);
    private final BooleanSetting tecnioRaper = new BooleanSetting("Tecnio AC Disabler", this, false);
    private final BooleanSetting convertMovingPackets = new BooleanSetting("Covert Moving Packets", this, false);
    private final BooleanSetting reverseConvertMovingPackets = new BooleanSetting("Reverse Covert Moving Packets", this, false);
    private final BooleanSetting funnyCraft = new BooleanSetting("Funcraft", this, false);
    private final BooleanSetting oxygen = new BooleanSetting("Oxygen", this, false);

    private final NoteSetting packetSettings = new NoteSetting("Packet Settings", this);

    private final BooleanSetting coc = new BooleanSetting("PacketInput", this, false);
    private final BooleanSetting c0f = new BooleanSetting("ConfirmTransaction", this, false);
    private final BooleanSetting c00 = new BooleanSetting("PacketKeepAlive", this, false);
    private final BooleanSetting c08 = new BooleanSetting("PacketPlayerBlockPlacement", this, false);
    private final BooleanSetting c13 = new BooleanSetting("PacketPlayerAbilities", this, false);
    private final BooleanSetting c16 = new BooleanSetting("PacketClientStatus", this, false);
    private final BooleanSetting c18 = new BooleanSetting("PacketSpectate", this, false);
    private final BooleanSetting c19 = new BooleanSetting("PacketResourcePacketStatus", this, false);

    private final ConcurrentLinkedQueue<Packet<?>> packetList = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Packet<?>> packetList2 = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Packet<?>, Long> packetHashMap = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Packet<?>> transactions = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Packet<?>> keepAlives = new ConcurrentLinkedQueue<>();

    public static final ConcurrentLinkedQueue<PacketUtil.TimedPacket> packets = new ConcurrentLinkedQueue<>();
    public static boolean spoofing;
    private int ticksSinceTeleport;
    private long currentDelay;
    private final TimeUtil timer = new TimeUtil();

    private long nextSend;
    private boolean teleported;
    private boolean exempt;
    private float previousYaw, previousPitch;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() != null) {
            if (ghostlyCombat.isEnabled()) {
                final double distance = mc.thePlayer.getDistanceToEntity(event.getTarget()) - 0.5657;
                if (distance > 2.9) {
                    if (mc.thePlayer.canEntityBeSeen(event.getTarget()))
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, false));
                    else
                        event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onUpdateAlwaysInGui() {
        boolean hideAllAfter = false;
        for (final Setting setting : getSettings()) {
            setting.hidden = hideAllAfter;

            if (hideOldDisablers == setting && hideOldDisablers.isEnabled()) {
                hideAllAfter = true;
            }
        }
    }

    @Override
    protected void onEnable() {
        if (mc.isIntegratedServerRunning())
            return;

        if (hypixel.isEnabled()) {
            final ChatComponentText chatcomponenttext = new ChatComponentText("Rejoin");
            mc.getNetHandler().getNetworkManager().closeChannel(chatcomponenttext);
        }

        if (mmcReach.isEnabled()) {
            Rise.INSTANCE.getNotificationManager().registerNotification("You need low ping for this disabler to not kick.", NotificationType.WARNING);
        }

        transactions.clear();
        keepAlives.clear();
        teleported = false;
        timer.reset();
    }

    @Override
    protected void onDisable() {
        packetList.forEach(PacketUtil::sendPacketWithoutEvent);
        packetList.clear();
        spoofing = false;

        packetList2.forEach(PacketUtil::sendPacketWithoutEvent);
        packetList2.clear();

        transactions.clear();
        keepAlives.clear();

        packetHashMap.clear();

        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.isIntegratedServerRunning())
            return;

        if (hypixel.isEnabled() && PlayerUtil.isOnServer("Hypixel")) {
            spoofing = true;
            for (final PacketUtil.TimedPacket p : packets) {
                if (System.currentTimeMillis() > p.getTime() + currentDelay) {
                    PacketUtil.sendPacketWithoutEvent(p.getPacket());
                    packets.remove(p);
                    this.currentDelay = Math.round(MathUtil.getRandom(200,250));
                }
            }
        }


        if (verusOld.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C0CPacketInput());
            PacketUtil.sendPacketWithoutEvent(new C18PacketSpectate(UUID.randomUUID()));
        }

        if (blocksMC.isEnabled()) {
            if (mc.thePlayer.ticksExisted % 100 == 0) {
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -0.015625, mc.thePlayer.posZ, false));
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));

                teleported = true;
            }

            if (timer.hasReached(500L) && !transactions.isEmpty()) {
                PacketUtil.sendPacketWithoutEvent(transactions.poll());

                timer.reset();
            }

            if (timer.hasReached(400L) && !keepAlives.isEmpty()) {
                PacketUtil.sendPacketWithoutEvent(keepAlives.poll());

                timer.reset();
            }
        }

        if (hypixelSlime.isEnabled() && mc.thePlayer.capabilities.allowFlying) {
            if (PlayerUtil.isOnServer("Hypixel")) {
                final PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                playerCapabilities.isFlying = true;
                PacketUtil.sendPacket(new C13PacketPlayerAbilities(playerCapabilities));
            } else
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.RIDING_JUMP));
        }

        if (vulcanCombat.isEnabled()) {
            if (timer.hasReached((long) (5000 + (Math.random() * 1000)))) {
                packetList2.forEach(PacketUtil::sendPacketWithoutEvent);
                packetList2.clear();
                timer.reset();
            }
        }

        if (sentinel.isEnabled()) {
            for (final Iterator<Map.Entry<Packet<?>, Long>> iterator = packetHashMap.entrySet().iterator(); iterator.hasNext(); ) {
                final Map.Entry<Packet<?>, Long> entry = iterator.next();

                if (entry.getValue() < System.currentTimeMillis()) {
                    PacketUtil.sendPacket(entry.getKey());
                    iterator.remove();
                }
            }
        }

        if (mmcReach.isEnabled()) {
            if (timer.hasReached((long) (500 + (Math.random() * 600)))) {
                packetList.forEach(PacketUtil::sendPacketWithoutEvent);
                packetList.clear();
                timer.reset();
            }
        }

        if (oxygen.isEnabled() && mc.thePlayer.onGround) {
            event.setY(event.getY() + 0.42F);
            event.setGround(false);
        }

        if (voidTp.isEnabled() && mc.thePlayer.ticksExisted % 20 == 0)
            event.setY(event.getY() - 20 - Math.random());

        if (clip.isEnabled()) {
            if (mc.thePlayer.ticksExisted % 20 == 0) {
                event.setY(event.getY() - 0.42);
                event.setGround(false);
            }
        }

        if (coc.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C0CPacketInput(0.98F, 0.0F, false, false));
        }

        if (c08.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }

        if (c13.isEnabled() || shartemis.isEnabled()) {
            final C13PacketPlayerAbilities abilities = new C13PacketPlayerAbilities();

            abilities.setAllowFlying(true);
            abilities.setFlying(true);

            PacketUtil.sendPacketWithoutEvent(abilities);
        }

        if (c16.isEnabled() || shartemis.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        }

        if (c18.isEnabled() || shartemis.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
        }

        if (c19.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C19PacketResourcePackStatus("", C19PacketResourcePackStatus.Action.ACCEPTED));
        }

        if (tecnioRaper.isEnabled()) {
            for (int i = 0; i < 10; i++) {
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(
                        mc.thePlayer.posX, mc.thePlayer.posY, Double.MAX_VALUE, 0.0F, 0.0F, true
                ));
            }

            for (int i = 0; i < 40; i++) {
                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(
                        Double.MAX_VALUE, mc.thePlayer.posY, mc.thePlayer.posZ, 0.0F, 0.0F, true
                ));
            }

            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(true));
            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(true));

            tecnioRaper.setEnabled(false);
        }

        if (funnyCraft.isEnabled()) {
            if (mc.thePlayer.ticksExisted % 4 == 0) {
                PacketUtil.sendPacketWithoutEvent(new C0CPacketInput(Float.MAX_VALUE, Float.MAX_VALUE, false, false));
            }
        }
    }

    @Override
    public void onTeleportEvent(final TeleportEvent event) {
        if (blocksMC.isEnabled()) {
            if (teleported) {
                event.setCancelled(true);
            }

            teleported = false;
        }
        if(hypixel.isEnabled() && PlayerUtil.isOnServer("Hypixel")){
            if (mc.thePlayer.ticksExisted < 100) {
                event.setCancelled(true);
                return;
            }

            this.ticksSinceTeleport = 0;
        }

//        if (mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ()) <= 9.5) {
//            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
//            event.setCancelled(true);
//        }
//        teleported = false;
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.isIntegratedServerRunning()) return;

        final Packet<?> p = event.getPacket();

        if (hypixel.isEnabled()) {
            final Packet<?> packet = event.getPacket();

            if (packet instanceof S08PacketPlayerPosLook) {
                nextSend = 0;
            }
        }

        if (hypixelSlime.isEnabled()) {
            if (p instanceof S02PacketChat) {
                final S02PacketChat chatMessage = (S02PacketChat) event.getPacket();
                if (chatMessage.getType() == 2 && chatMessage.getChatComponent().getUnformattedText().contains("UNAVAILABLE")) {
                    chatMessage.chatComponent = new ChatComponentText("§aDOUBLE JUMP AVAILABLE§r");
                }
            }

            if (p instanceof S29PacketSoundEffect) {
                final S29PacketSoundEffect packetSoundEffect = (S29PacketSoundEffect) event.getPacket();
                if (packetSoundEffect.getSoundName().equals("mob.slime.big")) {
                    event.setCancelled(true);
                }
            }

            if (p instanceof S2APacketParticles) {
                final S2APacketParticles particle = (S2APacketParticles) event.getPacket();
                if (particle.getParticleType() == EnumParticleTypes.SLIME) {
                    event.setCancelled(true);
                }
            }

            if (p instanceof S1FPacketSetExperience) {
                event.setCancelled(true);
            }
        }

        if (verus.isEnabled()) {
            if (p instanceof S08PacketPlayerPosLook && timer.hasReached(1000)) {
                final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
                if (mc.thePlayer.getDistanceSq(packet.x, packet.y, packet.z) <= 9.5) {
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, false));
                    event.setCancelled(true);
                }
                teleported = false;
                timer.reset();
            }
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.isIntegratedServerRunning())
            return;

        final Packet<?> packet = event.getPacket();

        if (hypixel.isEnabled() && PlayerUtil.isOnServer("Hypixel")) {

            if (packet instanceof C03PacketPlayer) {
                final C03PacketPlayer wrapper = (C03PacketPlayer) packet;

                if (wrapper instanceof C03PacketPlayer.C06PacketPlayerPosLook && ticksSinceTeleport <= 50 && mc.thePlayer.sendQueue.doneLoadingTerrain) {
                    event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(wrapper.x + Double.MIN_NORMAL, wrapper.y, wrapper.z - Double.MIN_NORMAL, ((C03PacketPlayer.C06PacketPlayerPosLook) wrapper).isOnGround()));
                }
            }

            ticksSinceTeleport++;
            if (mc.thePlayer == null || !mc.thePlayer.sendQueue.doneLoadingTerrain) {
                packets.clear();
                return;
            }

            if (mc.thePlayer.isDead || mc.isSingleplayer()) {
                packets.forEach(p -> PacketUtil.sendPacketWithoutEvent(p.getPacket()));
                packets.clear();
                return;
            }


            if (spoofing) {
                if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive) {
                    packets.add(new PacketUtil.TimedPacket(packet, System.currentTimeMillis()));
                    event.setCancelled(true);
                }
            } else if (packet instanceof C03PacketPlayer) {
                packets.forEach(p -> PacketUtil.sendPacketWithoutEvent(p.getPacket()));
                packets.clear();
            }
        }

        if (vulcanCombat.isEnabled() && packet instanceof C0FPacketConfirmTransaction) {
            packetList2.add(packet);
            event.setCancelled(true);
        }

        if (mineland.isEnabled()) {
            if (event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isSwingInProgress && !mc.thePlayer.isUsingItem() && !((C03PacketPlayer) event.getPacket()).isMoving()) {
                event.setCancelled(true);
            }
        }

        if (blocksMC.isEnabled()) {

            if (packet instanceof C0FPacketConfirmTransaction) {
                transactions.add(packet);
                event.setCancelled(true);

                if (transactions.size() > 300) {
                    PacketUtil.sendPacketWithoutEvent(transactions.poll());
                }
            }

            if (packet instanceof C00PacketKeepAlive) {
                final C00PacketKeepAlive c00 = (C00PacketKeepAlive) packet;

                c00.key -= 30;
                keepAlives.add(c00);
                event.setCancelled(true);
            }

            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (mc.thePlayer.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        mc.thePlayer.serverSprintState = false;
                    }

                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }

            if (packet instanceof C03PacketPlayer && !mc.thePlayer.isSwingInProgress &&
                    !mc.thePlayer.isUsingItem() && !((C03PacketPlayer) packet).isMoving()) {
                event.setCancelled(true);
            }
        }

        if (verus.isEnabled()) {
            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }

            if (packet instanceof C00PacketKeepAlive) {
                final C00PacketKeepAlive c00 = (C00PacketKeepAlive) packet;
                c00.key = -c00.key;
            }

            if (packet instanceof C03PacketPlayer) {
                final C03PacketPlayer c03 = (C03PacketPlayer) packet;

                mc.timer.timerSpeed = 0.75F;

                if (c03.y % 0.015625 == 0)
                    c03.setOnGround(true);

                if (timer.hasReached(1000) && !teleported) {
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition());
                    teleported = true;
                }

                if (mc.thePlayer.ticksExisted % 100 == 0)
                    teleported = false;
            }
        }

        if (verusCombat.isEnabled() && mc.thePlayer.ticksExisted > 20 && !mc.thePlayer.capabilities.allowFlying) {
            if (packet instanceof C0FPacketConfirmTransaction)
                event.setCancelled(true);
        }

        if (position.isEnabled() && packet instanceof C03PacketPlayer) {
            final C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
            c03.setY(c03.getY() + 0.015625);
        }

        if (ghostly.isEnabled()) {
            if (packet instanceof C03PacketPlayer) {
                PacketUtil.sendPacketWithoutEvent(new C0CPacketInput());

                C03PacketPlayer c03PacketPlayer = ((C03PacketPlayer) packet);
                double mathGround = Math.round(c03PacketPlayer.y / 0.015625) * 0.015625;

                c03PacketPlayer.y = mathGround;
                c03PacketPlayer.setOnGround(true);
                event.setPacket(c03PacketPlayer);
            }

            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }
        }

        if (dynamicPvP.isEnabled()) {
            if (packet instanceof C03PacketPlayer) {
                if (mc.thePlayer.ticksExisted % 3 == 0)
                    event.setCancelled(true);

                final C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;

                if (packetPlayer.isMoving()) {
                    x = packetPlayer.getPositionX();
                    y = packetPlayer.getPositionY();
                    z = packetPlayer.getPositionZ();
                }

                if (mc.thePlayer.ticksExisted % 20 == 0)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, packetPlayer.isOnGround()));

                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, packetPlayer.isOnGround()));
            }

            if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive)
                event.setCancelled(true);

            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }
        }

        if (spartan.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 91, mc.thePlayer.onGround));
        }

        if (sentinel.isEnabled()) {
            if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive) {
//                packetHashMap.put(p, System.currentTimeMillis() + RandomUtils.nextLong(15000, 25000));
                event.setCancelled(true);
            }

            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) packet;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (c0f.isEnabled() || shartemis.isEnabled()) {
            if (packet instanceof C0FPacketConfirmTransaction)
                event.setCancelled(true);
        }

        if (c00.isEnabled()) {
            if (packet instanceof C00PacketKeepAlive)
                event.setCancelled(true);
        }

        if (lunar.isEnabled()) {
            if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive)
                event.setCancelled(true);

            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }

            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) packet;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (reverseConvertMovingPackets.isEnabled()) {
            if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                final C03PacketPlayer.C06PacketPlayerPosLook packetPlayerPosition = (C03PacketPlayer.C06PacketPlayerPosLook) packet;
                event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), packetPlayerPosition.isOnGround()));
            }
        }

        if (convertMovingPackets.isEnabled()) {
            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) packet;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (mmcKeepSprint.isEnabled()) {
            if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) packet;

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                    if (EntityPlayerSP.serverSprintState) {
                        PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        EntityPlayerSP.serverSprintState = false;
                    }
                    event.setCancelled(true);
                }

                if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                    event.setCancelled(true);
                }
            }
        }

        if (mmcReach.isEnabled() && (PlayerUtil.isOnServer("minemen") || PlayerUtil.isOnServer("mineman"))) {
            if (packet instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        if (mineland.isEnabled()) {
            PacketUtil.sendPacket(new C18PacketSpectate(UUID.randomUUID()));
        }
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
    }

}