package koks.module.combat;

import com.google.common.collect.Ordering;
import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:33
 */

@Module.Info(name = "AntiBot", description = "You doesn't hit antibots", category = Module.Category.COMBAT)
public class AntiBot extends Module {

    //TODO: Invisible for Others, Random Damage, Improve Ping

    @Value(name = "Health NaN Check", displayName = "Health NaN")
    boolean healthNaNCheck = false;

    @Value(name = "GroundCheck", displayName = "Ground")
    boolean groundCheck = false;

    @Value(name = "GroundSpawnCheck", displayName = "GroundSpawn")
    boolean groundSpawnCheck = false;

    @Value(name = "Name Check", displayName = "Name")
    boolean nameCheck = false;

    @Value(name = "TeamColorCheck", displayName = "TeamColor")
    boolean teamColorCheck = false;

    @Value(name = "TeamColorRGB", displayName = "Color", colorPicker = true)
    int teamColorRGB = -1;

    @Value(name = "Swing Check", displayName = "Swing")
    boolean swingCheck = false;

    @Value(name = "HitBefore")
    boolean hitBefore = false;

    @Value(name = "TabListCheck", displayName = "TabList")
    boolean tabListCheck = false;

    @Value(name = "PingCheck", displayName = "Static Ping")
    boolean staticPingCheck = false;

    @Value(name = "StaticPing", displayName = "Ping")
    int staticPing = 1;

    @Value(name = "SkinCheck", displayName = "Skin")
    boolean skinCheck = false;

    @Value(name = "DuplicateEntityCheck", displayName = "Duplicate Entity")
    boolean duplicateEntityCheck = false;

    @Value(name = "Sound Check", displayName = "Sound")
    boolean soundCheck = false;

    @Value(name = "Rotation Check", displayName = "Rotation")
    boolean rotation = false;

    @Value(name = "Ticks Existed", minimum = 0, maximum = 100)
    int ticksExisted = 0;

    private final ArrayList<Entity> madeSound = new ArrayList<>();
    private final ArrayList<Entity> swingEntity = new ArrayList<>();
    private final ArrayList<Entity> hitBeforeEntity = new ArrayList<>();
    private final ArrayList<Entity> rotationEntity = new ArrayList<>();
    private final ArrayList<Entity> groundSpawnEntity = new ArrayList<>();
    private final ArrayList<Entity> duplicates = new ArrayList<>();

    private static final Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());

    private final CopyOnWriteArrayList<Entity> copyEntities = new CopyOnWriteArrayList<>();

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "TeamColorRGB":
                return teamColorCheck;
            case "StaticPing":
                return staticPingCheck;
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity instanceof final EntityPlayer player) {
                    if (isWrongRotation(player)) {
                        rotationEntity.add(player);
                    }
                    if (duplicateEntityCheck) {
                        final ArrayList<EntityPlayer> entities = getWorld().getPlayersByName(player.getName());
                        final ArrayList<NetworkPlayerInfo> tabList = getPlayer().searchPlayers(player.getName());
                        if (tabList.size() > 1 && entities.size() < tabList.size()) {
                            System.out.println(tabList.size() + entities.size() + " " + player.getName());
                            if(!duplicates.contains(player)) {
                                duplicates.add(player);
                            }
                            continue;
                        } else if(tabList.size() > 1) {
                            if(player != getWorld().getPlayerEntityByName(player.getName())) {
                                System.out.println(tabList.size() + entities.size() + " " + player.getName());
                                if(!duplicates.contains(player)) {
                                    duplicates.add(player);
                                }
                            }
                        }

                        if (player instanceof AbstractClientPlayer abstractClientPlayer)
                            for (EntityPlayer p : entities) {
                                if (p != player)
                                    if (p instanceof AbstractClientPlayer clientPlayer) {
                                        if (clientPlayer.hasSkin() != abstractClientPlayer.hasSkin()) {
                                            duplicates.add(player);
                                        }
                                    }
                            }
                    }
                }
            }
        }

        if (event instanceof PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                if (packet instanceof final S29PacketSoundEffect soundEffect) {
                    copyEntities.addAll(getWorld().loadedEntityList);
                    copyEntities.forEach(entity -> {
                        if (entity != getPlayer() && entity.getDistance(soundEffect.getX(), soundEffect.getY(), soundEffect.getZ()) <= 0.8)
                            madeSound.add(entity);
                    });
                    copyEntities.clear();
                }
                if (packet instanceof final S0CPacketSpawnPlayer spawnPlayer) {
                    if (getWorld() != null) {
                        final EntityPlayer player = getWorld().getPlayerEntityByUUID(spawnPlayer.getPlayer());
                        if (player != null)
                            if ((!player.onGround || getBlockUnderPlayer(player, 1) == Blocks.air) && groundSpawnCheck)
                                groundSpawnEntity.add(player);
                    }
                }
                if (packet instanceof final S0BPacketAnimation s0BPacketAnimation) {
                    if (!swingEntity.contains(getWorld().getEntityByID(s0BPacketAnimation.getEntityID())))
                        swingEntity.add(getWorld().getEntityByID(s0BPacketAnimation.getEntityID()));
                }
            } else if (((PacketEvent) event).getType() == PacketEvent.Type.SEND) {
                if (packet instanceof final C02PacketUseEntity c02PacketUseEntity) {
                    if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        final Entity entity = c02PacketUseEntity.getEntityFromWorld(getWorld());
                        if (!hitBeforeEntity.contains(entity))
                            hitBeforeEntity.add(entity);
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        duplicates.clear();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public boolean isBot(EntityLivingBase entity) {
        if (entity != null && entity != getPlayer()) {
            if (entity instanceof EntityPlayer player) {
                if (ticksExisted != 0 && entity.ticksExisted < ticksExisted) return false;
                if (healthNaNCheck && !Float.isNaN(entity.getHealth()))
                    return true;
                if (groundCheck && entity.onGround && getWorld().getBlockState(entity.getPosition().add(0, -0.05, 0)).getBlock() == Blocks.air)
                    return true;
                if (soundCheck && !madeSound.contains(entity))
                    return true;
                if (swingCheck && !swingEntity.contains(entity))
                    return true;
                if (nameCheck && !checkedName(entity))
                    return true;
                if (hitBefore && !hitBeforeEntity.contains(entity))
                    return true;
                if (!isInTabList(entity) && tabListCheck)
                    return true;
                if (!hasPing(entity) && staticPingCheck)
                    return true;
                if (rotationEntity.contains(entity) && rotation)
                    return true;
                if (teamColorCheck && entity.getTeam() != null && getTeamColor(player).getRGB() == teamColorRGB)
                    return true;
                if (groundSpawnCheck && groundSpawnEntity.contains(entity))
                    return true;
                if (player instanceof AbstractClientPlayer clientPlayer) {
                    if (skinCheck && !clientPlayer.hasSkin()) {
                        return true;
                    }
                }
                if (duplicateEntityCheck && duplicates.contains(player))
                    return true;
            } else {
                return false;
            }

            /*if (gamsterCheck && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            if (player.getTeam() != null) {
                boolean hasNumber = false;
                int numbers = 0;
                for (int i = 0; i < player.getTeam().getRegisteredName().length(); i++) {
                    String cur = player.getTeam().getRegisteredName().substring(i, i + 1);
                    if (Pattern.matches("[0-9]", cur)) {
                        numbers++;
                        hasNumber = true;
                    }
                }
                if (!hasNumber || numbers != 1) {
                    return true;
                }
            }
        }*/
        }
        return false;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public boolean isWrongRotation(EntityPlayer entity) {
        final double deltaX = entity.posX - entity.prevPosX;
        final double deltaZ = entity.posZ - entity.prevPosZ;
        final float distance = (float) (deltaX * deltaX + deltaZ * deltaZ);

        float offset = entity.renderYawOffset;
        float renderYawOffset = 0;

        if (distance > 0.0025000002F) {
            offset = (float) MathHelper.func_181159_b(deltaZ, deltaX) * 180.0F / (float) Math.PI - 90.0F;
        }

        if (entity.swingProgress > 0.0F) {
            offset = entity.rotationYaw;
        }

        float offsetCalc = MathHelper.wrapAngleTo180_float(offset - entity.renderYawOffset);
        renderYawOffset += offsetCalc * 0.3F;
        float curOffset = MathHelper.wrapAngleTo180_float(entity.rotationYaw - entity.renderYawOffset);
        boolean flag = offset < -90.0F || curOffset >= 90.0F;


        curOffset = MathHelper.clamp_float(curOffset, -75, 75);

        renderYawOffset = entity.rotationYaw - curOffset;

        if (curOffset * curOffset > 2500.0F) {
            renderYawOffset += curOffset * 0.2F;
        }

        final double rotationSpeed = Math.abs(entity.rotationYawHead - entity.prevRotationYawHead);
        final double distanceToHead = Math.abs(entity.rotationYaw - renderYawOffset);
        final boolean hasIllegalPitch = entity.rotationPitch > 90 || entity.rotationPitch < -90;

        final boolean wrongRotation = distanceToHead > 75;

        return wrongRotation || hasIllegalPitch;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public boolean hasPing(Entity entity) {
        if (mc.isSingleplayer())
            return true;
        for (NetworkPlayerInfo playerInfo : getPlayer().sendQueue.getPlayerInfoMap()) {
            if (playerInfo.getGameProfile().getId().equals(entity.getUniqueID()))
                if (playerInfo.getResponseTime() == staticPing)
                    return true;
        }
        return false;
    }

    public boolean isInTabList(Entity entity) {
        if (mc.isSingleplayer())
            return true;
        for (NetworkPlayerInfo playerInfo : getPlayer().sendQueue.getPlayerInfoMap()) {
            if (playerInfo.getGameProfile().getId().equals(entity.getUniqueID()))
                return true;
        }
        return false;
    }

    public boolean checkedName(Entity entity) {
        if (!isValidEntityName(entity))
            return false;
        return true;
    }
}
