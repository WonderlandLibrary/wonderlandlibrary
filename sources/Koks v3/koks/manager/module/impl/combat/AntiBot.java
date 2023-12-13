package koks.manager.module.impl.combat;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S29PacketSoundEffect;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kroko, Phantom, Deleteboys, Dirt
 * @created on 07.12.2020 : 06:39
 */

@ModuleInfo(name = "AntiBot", category = Module.Category.COMBAT, description = "You can detect the bots")
public class AntiBot extends Module {

    public Setting healthNaNCheck = new Setting("Health NaN Check", false, this);
    public Setting groundCheck = new Setting("GroundCheck", false, this);
    public Setting nameCheck = new Setting("Name Check", true, this);
    public Setting swingCheck = new Setting("Swing Check", false, this);
    public Setting hitBefore = new Setting("HitBefore", false, this);
    public Setting tabListCheck = new Setting("TabListCheck", false, this);
    public Setting pingCheck = new Setting("PingCheck", false, this);
    public Setting soundCheck = new Setting("Sound Check", false, this);

    public ArrayList<Entity> madeSound = new ArrayList<>();
    public ArrayList<Entity> swingEntity = new ArrayList<>();
    public ArrayList<Entity> hitBeforeEntity = new ArrayList<>();

    public CopyOnWriteArrayList<Entity> copyEntities = new CopyOnWriteArrayList<>();

    @Override
    public void onEvent(Event event) {
        if(!this.isToggled())
            return;
        if (event instanceof EventPacket) {
            Packet<? extends INetHandler> packet = ((EventPacket) event).getPacket();
            if (((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                if (packet instanceof S29PacketSoundEffect) {
                    S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;

                    copyEntities.addAll(getWorld().loadedEntityList);

                    copyEntities.forEach(entity -> {
                        if(entity != getPlayer() && entity.getDistance(soundEffect.getX(), soundEffect.getY(), soundEffect.getZ()) <= 0.8)
                            madeSound.add(entity);
                    });

                    copyEntities.clear();
                }
                if (packet instanceof S0BPacketAnimation) {
                    S0BPacketAnimation s0BPacketAnimation = (S0BPacketAnimation) packet;
                    if (!swingEntity.contains(getWorld().getEntityByID(s0BPacketAnimation.getEntityID())))
                        swingEntity.add(getWorld().getEntityByID(s0BPacketAnimation.getEntityID()));
                }
            } else if (((EventPacket) event).getType() == EventPacket.Type.SEND) {
                if (packet instanceof C02PacketUseEntity) {
                    C02PacketUseEntity c02PacketUseEntity = (C02PacketUseEntity) packet;
                    if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        Entity entity = c02PacketUseEntity.getEntityFromWorld(getWorld());
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
        hitBeforeEntity.clear();
        madeSound.clear();
        swingEntity.clear();
    }

    public boolean isBot(EntityLivingBase entity) {
        if (healthNaNCheck.isToggled() && !Float.isNaN(entity.getHealth()))
            return true;
        if (groundCheck.isToggled() && entity.onGround && getWorld().getBlockState(entity.getPosition().add(0, -0.05, 0)).getBlock() == Blocks.air)
            return true;
        if (soundCheck.isToggled() && !madeSound.contains(entity))
            return true;
        if (swingCheck.isToggled() && !swingEntity.contains(entity))
            return true;
        if (nameCheck.isToggled() && entity instanceof EntityPlayer && !checkedName(entity))
            return true;
        if (hitBefore.isToggled() && !hitBeforeEntity.contains(entity))
            return true;
        if (!isInTabList(entity) && tabListCheck.isToggled())
            return true;
        if(!hasPing(entity) && pingCheck.isToggled())
            return true;
        return false;
    }

    public boolean hasPing(Entity entity) {
        if (mc.isSingleplayer())
            return true;
        for (NetworkPlayerInfo playerInfo : getPlayer().sendQueue.getPlayerInfoMap()) {
            if (playerInfo.getGameProfile().getId().equals(entity.getUniqueID()))
                if (playerInfo.getResponseTime() > 0)
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
        if (!validEntityName(entity))
            return false;
        return true;
    }
}
