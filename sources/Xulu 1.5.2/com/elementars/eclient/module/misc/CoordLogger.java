// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.command.Command;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.event.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.module.Category;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class CoordLogger extends Module
{
    private final Value<Boolean> tp;
    private final Value<Boolean> lightning;
    private final Value<Boolean> portal;
    private final Value<Boolean> wither;
    private final Value<Boolean> dragon;
    private final Value<Boolean> savetofile;
    private HashMap<Entity, Vec3d> knownPlayers;
    SPacketSoundEffect packet;
    SPacketEffect packet2;
    
    public CoordLogger() {
        super("CoordLogger", "Logs coords taken from several exploits", 0, Category.MISC, true);
        this.tp = this.register(new Value<Boolean>("TpExploit", this, false));
        this.lightning = this.register(new Value<Boolean>("Thunder", this, false));
        this.portal = this.register(new Value<Boolean>("EndPortal", this, false));
        this.wither = this.register(new Value<Boolean>("Wither", this, false));
        this.dragon = this.register(new Value<Boolean>("Dragon", this, false));
        this.savetofile = this.register(new Value<Boolean>("SaveToFile", this, false));
        this.knownPlayers = new HashMap<Entity, Vec3d>();
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket event) {
        if (this.lightning.getValue() && event.getPacket() instanceof SPacketSoundEffect) {
            this.packet = (SPacketSoundEffect)event.getPacket();
            if (this.packet.getCategory() == SoundCategory.WEATHER && this.packet.getSound() == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                this.sendNotification(ChatFormatting.RED.toString() + "Lightning spawned at X" + this.packet.getX() + " Z" + this.packet.getZ());
            }
        }
        if (event.getPacket() instanceof SPacketEffect) {
            this.packet2 = (SPacketEffect)event.getPacket();
            if (this.portal.getValue() && this.packet2.getSoundType() == 1038) {
                this.sendNotification(ChatFormatting.RED.toString() + "End Portal activated at X" + this.packet2.getSoundPos().getX() + " Y" + this.packet2.getSoundPos().getY() + " Z" + this.packet2.getSoundPos().getZ());
            }
            if (this.wither.getValue() && this.packet2.getSoundType() == 1023) {
                this.sendNotification(ChatFormatting.RED.toString() + "Wither spawned at X" + this.packet2.getSoundPos().getX() + " Y" + this.packet2.getSoundPos().getY() + " Z" + this.packet2.getSoundPos().getZ());
            }
            if (this.dragon.getValue() && this.packet2.getSoundType() == 1028) {
                this.sendNotification(ChatFormatting.RED.toString() + "Dragon killed at X" + this.packet2.getSoundPos().getX() + " Y" + this.packet2.getSoundPos().getY() + " Z" + this.packet2.getSoundPos().getZ());
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!this.tp.getValue()) {
            return;
        }
        if (CoordLogger.mc.player == null) {
            return;
        }
        final List<Entity> tickEntityList = (List<Entity>)CoordLogger.mc.world.getLoadedEntityList();
        for (final Entity entity : tickEntityList) {
            if (entity instanceof EntityPlayer && !entity.getName().equals(CoordLogger.mc.player.getName())) {
                final Vec3d targetPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
                if (this.knownPlayers.containsKey(entity)) {
                    if (Math.abs(CoordLogger.mc.player.getPositionVector().distanceTo(targetPos)) >= 128.0 && this.knownPlayers.get(entity).distanceTo(targetPos) >= 64.0) {
                        this.sendNotification(ChatFormatting.RED.toString() + "Player " + entity.getName() + " moved to Position " + targetPos.toString());
                    }
                    this.knownPlayers.put(entity, targetPos);
                }
                else {
                    this.knownPlayers.put(entity, targetPos);
                }
            }
        }
    }
    
    private void sendNotification(final String s) {
        Command.sendChatMessage(s);
        if (this.savetofile.getValue()) {
            Wrapper.getFileManager().appendTextFile(s, "CoordLogger.txt");
        }
    }
}
