/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.notifications.NotificationType;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.pathfinding.Vec3;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
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
    private final NoteSetting spacer = new NoteSetting("", this);

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
    private final Queue<Packet<?>> packet = new ConcurrentLinkedDeque<>();

    private final TimeUtil timer = new TimeUtil();

    private boolean teleported;

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

        if (mmcReach.isEnabled()) {
            Rise.INSTANCE.getNotificationManager().registerNotification("You need low ping for this disabler to not kick.", NotificationType.WARNING);
        }

        if (blocksMC.isEnabled()) {
            Rise.INSTANCE.getNotificationManager().registerNotification("If you stand still for to long disabler might break idk why.", NotificationType.WARNING);
        }

        packet.clear();
        teleported = false;
        timer.reset();
    }

    @Override
    protected void onDisable() {
        packetList.forEach(PacketUtil::sendPacketWithoutEvent);
        packetList.clear();

        packetList2.forEach(PacketUtil::sendPacketWithoutEvent);
        packetList2.clear();

        packet.clear();

        packetHashMap.clear();

        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.isIntegratedServerRunning())
            return;

        if (verusOld.isEnabled()) {
            PacketUtil.sendPacketWithoutEvent(new C0CPacketInput());
            PacketUtil.sendPacketWithoutEvent(new C18PacketSpectate(UUID.randomUUID()));
        }

        if (blocksMC.isEnabled()) {
            if (timer.hasReached(490L)) {
                timer.reset();

                if (!packet.isEmpty()) {
                    PacketUtil.sendPacketWithoutEvent(packet.poll());
                }
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
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.isIntegratedServerRunning()) return;

        final Packet<?> p = event.getPacket();

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

        if (blocksMC.isEnabled()) {
            if (p instanceof S08PacketPlayerPosLook && teleported) {
                final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;

                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(packet.x, packet.y, packet.z), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, true));

                for (final Vec3 vec : path)
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec.getX(), vec.getY(), vec.getZ(), packet.yaw, packet.pitch, true));

                event.setCancelled(true);
                teleported = false;
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

        final Packet<?> p = event.getPacket();

        if (vulcanCombat.isEnabled() && p instanceof C0FPacketConfirmTransaction) {
            packetList2.add(p);
            event.setCancelled(true);
        }

        if (blocksMC.isEnabled()) {
            if (mc.thePlayer.ticksExisted < 60 && PlayerUtil.worldChanges > 0) {
                packet.clear();
                teleported = false;
                return;
            }

            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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

            if (p instanceof C00PacketKeepAlive || p instanceof C0FPacketConfirmTransaction) {
                packet.add(p);
                event.setCancelled(true);

                if (packet.size() > 300) {
                    PacketUtil.sendPacketWithoutEvent(packet.poll());
                }
            }

            if (p instanceof C03PacketPlayer) {
                final C03PacketPlayer c03 = (C03PacketPlayer) p;

                if (mc.thePlayer.ticksExisted % 20 == 0) {
                    PacketUtil.sendPacketWithoutEvent(new C18PacketSpectate(UUID.randomUUID()));
                    PacketUtil.sendPacketWithoutEvent(new C0CPacketInput(0.98F, 0.98F, false, false));
                }

                if (mc.thePlayer.ticksExisted % 120 == 0) {
                    c03.setOnGround(false);
                    // 1 / 64
                    // Math ground
                    c03.setY(-0.015625);
                    teleported = true;
                }
            }
        }

        if (verus.isEnabled()) {
            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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

            if (p instanceof C00PacketKeepAlive) {
                final C00PacketKeepAlive c00 = (C00PacketKeepAlive) p;
                c00.key = -c00.key;
            }

            if (p instanceof C03PacketPlayer) {
                final C03PacketPlayer c03 = (C03PacketPlayer) p;

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
            if (p instanceof C0FPacketConfirmTransaction)
                event.setCancelled(true);
        }

        if (position.isEnabled() && p instanceof C03PacketPlayer) {
            final C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
            c03.setY(c03.getY() + 0.015625);
        }

        if (ghostly.isEnabled()) {
            if (p instanceof C03PacketPlayer) {
                PacketUtil.sendPacketWithoutEvent(new C0CPacketInput());
            }

            if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive)
                event.setCancelled(true);

            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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
            if (p instanceof C03PacketPlayer) {
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

            if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive)
                event.setCancelled(true);

            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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
            if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) {
//                packetHashMap.put(p, System.currentTimeMillis() + RandomUtils.nextLong(15000, 25000));
                event.setCancelled(true);
            }

            if (p instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) p;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (c0f.isEnabled() || shartemis.isEnabled()) {
            if (p instanceof C0FPacketConfirmTransaction)
                event.setCancelled(true);
        }

        if (c00.isEnabled()) {
            if (p instanceof C00PacketKeepAlive)
                event.setCancelled(true);
        }

        if (lunar.isEnabled()) {
            if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive)
                event.setCancelled(true);

            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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

            if (p instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) p;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (reverseConvertMovingPackets.isEnabled()) {
            if (p instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                final C03PacketPlayer.C06PacketPlayerPosLook packetPlayerPosition = (C03PacketPlayer.C06PacketPlayerPosLook) p;
                event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), packetPlayerPosition.isOnGround()));
            }
        }

        if (convertMovingPackets.isEnabled()) {
            if (p instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) p;
                event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
            }
        }

        if (mmcKeepSprint.isEnabled()) {
            if (p instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction c0B = (C0BPacketEntityAction) p;

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
            if (p instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        }
    }
}
