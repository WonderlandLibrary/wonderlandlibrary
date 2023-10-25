/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;

public class PearlLogger
extends Feature {
    private boolean canSend;
    EntityPlayer throwerEntity;

    public PearlLogger() {
        super("PearlLogger", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u044d\u043d\u0434\u0435\u0440-\u043f\u0435\u0440\u043b\u0430 \u0438\u0433\u0440\u043e\u043a\u043e\u0432", Type.Player);
    }

    @Override
    public void onEnable() {
        this.canSend = true;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String str;
        if (PearlLogger.mc.world == null || PearlLogger.mc.player == null) {
            return;
        }
        Entity enderPearl = null;
        for (Entity e : PearlLogger.mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderPearl)) continue;
            enderPearl = e;
            break;
        }
        if (enderPearl == null) {
            this.canSend = true;
            return;
        }
        for (EntityPlayer entity : PearlLogger.mc.world.playerEntities) {
            if (entity == PearlLogger.mc.player || this.throwerEntity != null && this.throwerEntity.getDistanceToEntity(enderPearl) <= entity.getDistanceToEntity(enderPearl)) continue;
            this.throwerEntity = entity;
        }
        String facing = enderPearl.getHorizontalFacing().toString();
        if (facing.equals("west")) {
            facing = "east";
        } else if (facing.equals("east")) {
            facing = "west";
        }
        if (this.throwerEntity == PearlLogger.mc.player || this.throwerEntity == null) {
            return;
        }
        if (this.throwerEntity.getName().equals(PearlLogger.mc.player.getName())) {
            return;
        }
        String pos = (Object)((Object)ChatFormatting.GOLD) + facing.toUpperCase() + (Object)((Object)ChatFormatting.WHITE) + " | " + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + enderPearl.getPosition().getX() + " " + enderPearl.getPosition().getY() + " " + enderPearl.getPosition().getZ();
        String string = str = Celestial.instance.friendManager.isFriend(this.throwerEntity.getName()) ? (Object)((Object)ChatFormatting.GREEN) + this.throwerEntity.getName() + (Object)((Object)ChatFormatting.WHITE) + " thrown pearl on " + pos : (Object)((Object)ChatFormatting.RED) + this.throwerEntity.getName() + (Object)((Object)ChatFormatting.WHITE) + " thrown pearl on " + pos;
        if (this.canSend) {
            ChatHelper.addChatMessage(str);
            this.canSend = false;
        }
    }
}

