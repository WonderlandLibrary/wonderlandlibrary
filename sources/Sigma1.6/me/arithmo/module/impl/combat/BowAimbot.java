/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.impl.combat.Killaura;
import me.arithmo.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class BowAimbot
extends Module {
    boolean send;
    boolean isFiring;

    public BowAimbot(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class})
    public void onEvent(Event event) {
        EventMotion em;
        if (event instanceof EventMotion && (em = (EventMotion)event).isPre()) {
            EntityLivingBase target = this.getTarg();
            if (BowAimbot.mc.thePlayer.isUsingItem() && BowAimbot.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && target != BowAimbot.mc.thePlayer && target != null) {
                float[] rotations = RotationUtils.getBowAngles(target);
                em.setYaw(rotations[0]);
                em.setPitch(rotations[1]);
            }
        }
    }

    private EntityLivingBase getTarg() {
        ArrayList<EntityLivingBase> loaded = new ArrayList<EntityLivingBase>();
        for (Object o : BowAimbot.mc.theWorld.getLoadedEntityList()) {
            EntityLivingBase ent;
            if (!(o instanceof EntityLivingBase) || !((ent = (EntityLivingBase)o) instanceof EntityPlayer) || !BowAimbot.mc.thePlayer.canEntityBeSeen(ent) || BowAimbot.mc.thePlayer.getDistanceToEntity(ent) >= 15.0f || FriendManager.isFriend(ent.getName())) continue;
            if (ent == Killaura.vip) {
                return ent;
            }
            loaded.add(ent);
        }
        if (loaded.isEmpty()) {
            return null;
        }
        loaded.sort((o1, o2) -> {
            float[] rot1 = RotationUtils.getRotations(o1);
            float[] rot2 = RotationUtils.getRotations(o2);
            return (int)(RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationYaw, rot1[0]) + RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationPitch, rot1[1]) - (RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationYaw, rot2[0]) + RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationPitch, rot2[1])));
        }
        );
        EntityLivingBase target = (EntityLivingBase)loaded.get(0);
        return target;
    }
}

