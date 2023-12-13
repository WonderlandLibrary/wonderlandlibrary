// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.util.TargetPlayers;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.EntityUtil;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayer;
import dev.xulu.settings.Value;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class AutoEz extends Module
{
    private ArrayList<String> dummy;
    private final Value<String> message;
    private final Value<Boolean> mode;
    private final Value<Boolean> autocityboss;
    private final Value<String> acmessage;
    private final Value<Boolean> acmode;
    private final Value<Boolean> autoezhelmet;
    private final Value<String> ahmessage;
    private final Value<Boolean> ahmode;
    private EntityPlayer target;
    private ConcurrentHashMap<String, Integer> targettedplayers;
    private ArrayList<EntityPlayer> targets;
    private EntityEnderCrystal crystal;
    private int hasBeenCombat;
    private ConcurrentHashMap<String, Integer> totemplayers;
    private ConcurrentHashMap<String, Integer> helmetplayers;
    private int acdelay;
    private int ahdelay;
    public static AutoEz INSTANCE;
    
    public AutoEz() {
        super("AutoEZ", "Sends toxic messages for you (use NAME like in Welcome)", 0, Category.COMBAT, true);
        this.dummy = new ArrayList<String>(Arrays.asList("Change this in the settings"));
        this.message = this.register(new Value<String>("Message", this, "NAME has been put in the montage", this.dummy));
        this.mode = this.register(new Value<Boolean>("Names", this, true));
        this.autocityboss = this.register(new Value<Boolean>("AutoCityboss", this, false));
        this.acmessage = this.register(new Value<String>("AC message", this, "NAME ez pop", this.dummy));
        this.acmode = this.register(new Value<Boolean>("AC Names", this, true));
        this.autoezhelmet = this.register(new Value<Boolean>("AutoEZHelmet", this, false));
        this.ahmessage = this.register(new Value<String>("AH message", this, "NAME ez helmet", this.dummy));
        this.ahmode = this.register(new Value<Boolean>("AH Names", this, true));
        this.targettedplayers = new ConcurrentHashMap<String, Integer>();
        this.totemplayers = new ConcurrentHashMap<String, Integer>();
        this.helmetplayers = new ConcurrentHashMap<String, Integer>();
        AutoEz.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent event) {
        if (this.isToggled() && event.getTarget() instanceof EntityEnderCrystal) {
            this.crystal = (EntityEnderCrystal)event.getTarget();
        }
        if (this.isToggled() && event.getTarget() instanceof EntityPlayer) {
            final EntityPlayer target = (EntityPlayer)event.getTarget();
            if (target.getHealth() <= 0.0f || target.isDead) {
                if (this.mode.getValue()) {
                    this.sendChatMessage(this.message.getValue(), target.getName());
                }
                else {
                    this.sendChatMessage(this.message.getValue(), null);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onDeath(final LivingDeathEvent event) {
        if (AutoEz.mc.player != null) {
            final EntityLivingBase entity = event.getEntityLiving();
            if (entity != null && EntityUtil.isPlayer((Entity)entity)) {
                final EntityPlayer target = (EntityPlayer)entity;
                if (target.getHealth() <= 0.0f) {
                    final String name = target.getName();
                    if (TargetPlayers.targettedplayers.containsKey(name)) {
                        if (this.mode.getValue()) {
                            this.sendChatMessage(this.message.getValue(), name);
                        }
                        else {
                            this.sendChatMessage(this.message.getValue(), null);
                        }
                        TargetPlayers.targettedplayers.remove(name);
                    }
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoEz.mc.player.isDead) {
            this.hasBeenCombat = 0;
        }
        for (final Entity entity : AutoEz.mc.world.getLoadedEntityList()) {
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            final String name2 = player.getName();
            if (!TargetPlayers.targettedplayers.containsKey(name2)) {
                continue;
            }
            if (this.mode.getValue()) {
                this.sendChatMessage(this.message.getValue(), name2);
            }
            else {
                this.sendChatMessage(this.message.getValue(), null);
            }
            TargetPlayers.targettedplayers.remove(name2);
        }
        if (this.autocityboss.getValue() && this.acdelay == 0) {
            for (final Entity entity : AutoEz.mc.world.getLoadedEntityList()) {
                if (!EntityUtil.isPlayer(entity)) {
                    continue;
                }
                final EntityPlayer player = (EntityPlayer)entity;
                if (player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    this.totemplayers.put(player.getName(), 20);
                }
                else {
                    final String name2 = player.getName();
                    if (TargetPlayers.targettedplayers.containsKey(name2) && this.totemplayers.containsKey(name2) && AutoEz.mc.world.playerEntities.contains(player)) {
                        if (this.acmode.getValue()) {
                            this.sendChatMessage(this.acmessage.getValue(), name2);
                        }
                        else {
                            this.sendChatMessage(this.acmessage.getValue(), null);
                        }
                        TargetPlayers.targettedplayers.remove(name2);
                        this.totemplayers.remove(name2);
                        this.acdelay = 1500;
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.autoezhelmet.getValue() && this.ahdelay == 0) {
            for (final Entity entity : AutoEz.mc.world.getLoadedEntityList()) {
                if (!EntityUtil.isPlayer(entity)) {
                    continue;
                }
                final EntityPlayer player = (EntityPlayer)entity;
                boolean helmet = false;
                for (final ItemStack itemStack : player.getArmorInventoryList()) {
                    if (itemStack != null && itemStack.getItem() == Items.DIAMOND_HELMET) {
                        helmet = true;
                    }
                }
                if (helmet) {
                    this.helmetplayers.put(player.getName(), 20);
                }
                else {
                    final String name3 = player.getName();
                    if (TargetPlayers.targettedplayers.containsKey(name3) && this.helmetplayers.containsKey(name3) && AutoEz.mc.world.playerEntities.contains(player)) {
                        if (this.acmode.getValue()) {
                            this.sendChatMessage(this.ahmessage.getValue(), name3);
                        }
                        else {
                            this.sendChatMessage(this.ahmessage.getValue(), null);
                        }
                        TargetPlayers.targettedplayers.remove(name3);
                        this.helmetplayers.remove(name3);
                        this.ahdelay = 1500;
                        break;
                    }
                    continue;
                }
            }
        }
        this.totemplayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.totemplayers.remove(name);
            }
            else {
                this.totemplayers.put(name, timeout - 1);
            }
            return;
        });
        this.helmetplayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.helmetplayers.remove(name);
            }
            else {
                this.helmetplayers.put(name, timeout - 1);
            }
            return;
        });
        if (this.acdelay > 0) {
            --this.acdelay;
        }
        if (this.ahdelay > 0) {
            --this.ahdelay;
        }
    }
    
    private void sendChatMessage(final String message, @Nullable final String name) {
        final String text = (name == null) ? message : message.replaceAll("NAME", name);
        AutoEz.mc.player.sendChatMessage(text);
    }
}
