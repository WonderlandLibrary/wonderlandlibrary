// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.ArrayList;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.item.ItemPotion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.server.SPacketSoundEffect;
import com.elementars.eclient.command.Command;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import com.elementars.eclient.event.events.EventReceivePacket;
import java.util.Iterator;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class StrengthDetect extends Module
{
    private final Value<Boolean> watermark;
    private final Value<String> color;
    public static List<EntityPlayer> drinkSet;
    public static List<EntityPlayer> strPlayers;
    
    public StrengthDetect() {
        super("StrengthDetect", "Detects when someone has strength (BUGGY)", 0, Category.COMBAT, true);
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
    }
    
    @Override
    public void onUpdate() {
        if (StrengthDetect.mc.player == null) {
            return;
        }
        for (final EntityPlayer entityPlayer : StrengthDetect.mc.world.playerEntities) {
            for (final PotionEffect potionEffect : entityPlayer.getActivePotionEffects()) {
                boolean flag = true;
                if (potionEffect.getPotion() == MobEffects.STRENGTH) {
                    StrengthDetect.strPlayers.add(entityPlayer);
                    flag = false;
                }
                if (flag) {
                    StrengthDetect.strPlayers.remove(entityPlayer);
                }
            }
            if (entityPlayer.getHealth() == 0.0f && StrengthDetect.strPlayers.contains(entityPlayer)) {
                StrengthDetect.strPlayers.remove(entityPlayer);
            }
        }
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity((World)StrengthDetect.mc.world) instanceof EntityPlayer) {
                final EntityPlayer entity = (EntityPlayer)packet.getEntity((World)StrengthDetect.mc.world);
                StrengthDetect.strPlayers.remove(entity);
                Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + entity.getName() + " no longer has strength!");
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = (SPacketSoundEffect)event.getPacket();
            if (packet2.sound.getSoundName().toString().equalsIgnoreCase("minecraft:entity.generic.drink")) {
                final List<EntityPlayer> players = (List<EntityPlayer>)StrengthDetect.mc.world.getEntitiesWithinAABB((Class)EntityPlayer.class, new AxisAlignedBB(packet2.getX() - 1.0, packet2.getY() - 1.0, packet2.getZ() - 1.0, packet2.getX() + 1.0, packet2.getY() + 1.0, packet2.getZ() + 1.0));
                EntityPlayer drinker = null;
                if (players.size() > 1) {
                    for (final EntityPlayer player : players) {
                        if (drinker == null || player.getDistance(packet2.getX(), packet2.getY(), packet2.getZ()) < drinker.getDistance(packet2.getX(), packet2.getY(), packet2.getZ())) {
                            drinker = player;
                        }
                    }
                }
                else {
                    drinker = players.get(0);
                }
                if (drinker.getHeldItemMainhand().getItem() instanceof ItemPotion) {
                    final List<PotionEffect> effects = (List<PotionEffect>)PotionUtils.getEffectsFromStack(drinker.getHeldItemMainhand());
                    for (final PotionEffect p : effects) {
                        if (p.getPotion() == MobEffects.STRENGTH) {
                            StrengthDetect.drinkSet.add(drinker);
                        }
                    }
                }
            }
            else if (packet2.sound.getSoundName().toString().equalsIgnoreCase("minecraft:item.armor.equip_generic")) {
                final List<EntityPlayer> players = (List<EntityPlayer>)StrengthDetect.mc.world.getEntitiesWithinAABB((Class)EntityPlayer.class, new AxisAlignedBB(packet2.getX() - 1.0, packet2.getY() - 1.0, packet2.getZ() - 1.0, packet2.getX() + 1.0, packet2.getY() + 1.0, packet2.getZ() + 1.0));
                EntityPlayer drinker = null;
                if (players.size() > 1) {
                    for (final EntityPlayer player : players) {
                        if (drinker == null || player.getDistance(packet2.getX(), packet2.getY(), packet2.getZ()) < drinker.getDistance(packet2.getX(), packet2.getY(), packet2.getZ())) {
                            drinker = player;
                        }
                    }
                }
                else {
                    drinker = players.get(0);
                }
                if (StrengthDetect.drinkSet.contains(drinker) && drinker.getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE) {
                    StrengthDetect.strPlayers.add(drinker);
                    StrengthDetect.drinkSet.remove(drinker);
                    Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + drinker.getName() + " has drank a strength pot!");
                }
            }
        }
    }
    
    static {
        StrengthDetect.drinkSet = new ArrayList<EntityPlayer>();
        StrengthDetect.strPlayers = new ArrayList<EntityPlayer>();
    }
}
