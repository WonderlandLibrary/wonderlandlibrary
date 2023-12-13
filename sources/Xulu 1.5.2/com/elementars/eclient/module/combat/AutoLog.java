// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.Xulu;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AutoLog extends Module
{
    private Value<Integer> health;
    private boolean shouldLog;
    long lastLog;
    
    public AutoLog() {
        super("AutoLog", "Automatically Logs", 0, Category.COMBAT, true);
        this.health = this.register(new Value<Integer>("Health", this, 6, 0, 36));
        this.shouldLog = false;
        this.lastLog = System.currentTimeMillis();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @SubscribeEvent
    private void onEntity(final EntityJoinWorldEvent event) {
        if (AutoLog.mc.player == null) {
            return;
        }
        if (event.getEntity() instanceof EntityEnderCrystal && AutoLog.mc.player.getHealth() - AutoCrystal.calculateDamage((EntityEnderCrystal)event.getEntity(), (Entity)AutoLog.mc.player) < this.health.getValue()) {
            this.log();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldLog) {
            this.shouldLog = false;
            if (System.currentTimeMillis() - this.lastLog < 2000L) {
                return;
            }
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
        }
    }
    
    private void log() {
        Xulu.MODULE_MANAGER.getModuleByName("AutoReconnect").disable();
        this.shouldLog = true;
        this.lastLog = System.currentTimeMillis();
    }
}
