package com.ohare.client.module.modules.combat;

import java.awt.Color;
import java.util.*;

import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.apache.commons.lang3.StringUtils;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.EnumValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    public static ArrayList<EntityPlayer> bots = new ArrayList<>();
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.HYPIXEL);
    private BooleanValue remove = new BooleanValue("Remove Bots",false);

    private Map<Integer, Double> distanceMap = new HashMap<>();
    private Set<Integer> swingSet = new HashSet<>();


    public AntiBot() {
        super("AntiBot", Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        setDescription("Add bots to a list of poop heads");
        setRenderlabel("Anti Bot");
        addValues(mode,remove);
    }
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        switch (mode.getValue()) {
            case HYPIXEL:
                mc.theWorld.playerEntities.forEach(entityPlayer -> {
                    if(entityPlayer != mc.thePlayer) {
                        if(isEntityBot(entityPlayer)) {
                            if(remove.isEnabled()) mc.theWorld.removeEntity(entityPlayer);

                            bots.add(entityPlayer);
                        }
                    }
                    if(bots.contains(entityPlayer) && !entityPlayer.isInvisible()) {
                        bots.remove(entityPlayer);
                    }
                });
                break;
            case MINEPLEX:
                for (Object object : mc.theWorld.playerEntities) {
                    if (object instanceof EntityPlayer) {
                        EntityPlayer e = (EntityPlayer) object;
                        if (e.ticksExisted < 2 && e.getHealth() < 20 && e.getHealth() > 0 && e != mc.thePlayer) {
                            mc.theWorld.removeEntity(e);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if(mc.theWorld == null)
            return;
        if(event.getPacket() instanceof S0CPacketSpawnPlayer){
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();
            double d = mc.thePlayer.getDistance(x,y,z);

            distanceMap.put(packet.getEntityID(), d);
        }

        if(event.getPacket() instanceof S0BPacketAnimation){
            S0BPacketAnimation packet = (S0BPacketAnimation) event.getPacket();

            if(packet.getAnimationType() != 0)
                return;

            swingSet.add(packet.getEntityID());
        }

    }

    private boolean isEntityBot(Entity entity){
        if(!distanceMap.containsKey(entity.getEntityId()))
            return false;

        double distance = distanceMap.get(entity.getEntityId());

        if (!(entity instanceof EntityPlayer)) {
            return false;
        }

        if (!swingSet.contains(entity.getEntityId()))
            return false;

        if(mc.getCurrentServerData() == null)
            return false;

        return mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") &&
                ((distance > 14.5 && distance < 17) || entity.getName().contains("\247") ||
                        entity.getDisplayName().getFormattedText().startsWith("ยง") ||
                        entity.getDisplayName().getFormattedText().toLowerCase().contains("npc") ||
                        !isOnTab(entity)) && mc.thePlayer.ticksExisted > 100;
    }

    private boolean isOnTab(Entity entity){
        return mc.getNetHandler().getPlayerInfoMap()
                .stream()
                .anyMatch(info -> info.getGameProfile().getName().equals(entity.getName()));
    }

    private enum Mode {
        HYPIXEL, MINEPLEX
    }

    @Override
    public void onEnable() {
        bots.clear();
    }

    @Override
    public void onDisable() {
        bots.clear();
    }

    public static List<EntityPlayer> getBots() {
        return bots;
    }
}