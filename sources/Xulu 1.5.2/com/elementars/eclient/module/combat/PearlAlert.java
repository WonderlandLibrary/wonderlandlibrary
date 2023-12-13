// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.Iterator;
import com.elementars.eclient.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class PearlAlert extends Module
{
    private final Value<Boolean> watermark;
    private final Value<String> color;
    ConcurrentHashMap<UUID, Integer> uuidMap;
    
    public PearlAlert() {
        super("PearlAlert", "Alerts pearls thrown", 0, Category.COMBAT, true);
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
        this.uuidMap = new ConcurrentHashMap<UUID, Integer>();
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @Override
    public void onUpdate() {
        for (final Entity entity : PearlAlert.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderPearl) {
                EntityPlayer closest = null;
                for (final EntityPlayer p : PearlAlert.mc.world.playerEntities) {
                    if (closest == null || entity.getDistance((Entity)p) < entity.getDistance((Entity)closest)) {
                        closest = p;
                    }
                }
                if (closest == null || closest.getDistance(entity) >= 2.0f || this.uuidMap.containsKey(entity.getUniqueID()) || closest.getName().equalsIgnoreCase(PearlAlert.mc.player.getName())) {
                    continue;
                }
                this.uuidMap.put(entity.getUniqueID(), 200);
                if (this.watermark.getValue()) {
                    Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + closest.getName() + " threw a pearl towards " + this.getTitle(entity.getHorizontalFacing().getName()) + "!");
                }
                else {
                    Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + closest.getName() + " threw a pearl towards " + this.getTitle(entity.getHorizontalFacing().getName()) + "!");
                }
            }
        }
        this.uuidMap.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.uuidMap.remove(name);
            }
            else {
                this.uuidMap.put(name, timeout - 1);
            }
        });
    }
    
    public String getTitle(final String in) {
        if (in.equalsIgnoreCase("west")) {
            return "east";
        }
        if (in.equalsIgnoreCase("east")) {
            return "west";
        }
        return in;
    }
}
