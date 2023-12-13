// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.util.ColorUtils;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import com.elementars.eclient.command.Command;
import net.minecraft.item.ItemStack;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import java.util.concurrent.ConcurrentHashMap;
import com.elementars.eclient.module.Module;

public class DurabilityAlert extends Module
{
    ConcurrentHashMap<String, Integer> players;
    private final Value<String> mode;
    private final Value<Boolean> ignoreself;
    private final Value<Boolean> ignorefriends;
    private final Value<Boolean> watermark;
    private final Value<String> color;
    
    public DurabilityAlert() {
        super("DurabilityAlert", "Alerts when someone has low durability", 0, Category.COMBAT, true);
        this.players = new ConcurrentHashMap<String, Integer>();
        this.mode = this.register(new Value<String>("Mode", this, "Chat", new String[] { "Chat", "Notification" }));
        this.ignoreself = this.register(new Value<Boolean>("Ignore Self", this, false));
        this.ignorefriends = this.register(new Value<Boolean>("Ignore Friends", this, false));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : DurabilityAlert.mc.world.playerEntities) {
            if (this.ignoreself.getValue() && player.getName().equalsIgnoreCase(DurabilityAlert.mc.player.getName())) {
                return;
            }
            if (this.ignorefriends.getValue() && Friends.isFriend(player.getName())) {
                return;
            }
            for (final ItemStack itemStack : player.getArmorInventoryList()) {
                if (itemStack != null && itemStack.getItem().getDurabilityForDisplay(itemStack) > 0.75 && !this.players.containsKey(player.getName())) {
                    if (this.mode.getValue().equalsIgnoreCase("Chat")) {
                        if (this.watermark.getValue()) {
                            Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + player.getName() + " has low durability!");
                        }
                        else {
                            Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + player.getName() + " has low durability!");
                        }
                    }
                    this.players.put(player.getName(), 1500);
                }
            }
        }
        this.players.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.players.remove(name);
            }
            else {
                this.players.put(name, timeout - 1);
            }
        });
    }
    
    @Override
    public void onRender() {
        if (this.mode.getValue().equalsIgnoreCase("Notification")) {
            final ScaledResolution sr = new ScaledResolution(DurabilityAlert.mc);
            int yCount = (int)(sr.getScaledHeight() / 2 - sr.getScaledHeight() / 2 * 0.9) - DurabilityAlert.mc.fontRenderer.FONT_HEIGHT / 2;
            for (final String name : this.players.keySet()) {
                if (this.players.get(name) > 1000) {
                    DurabilityAlert.mc.fontRenderer.drawStringWithShadow(Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color.getValue()).substring(1) + name + " has low durability!", (float)(sr.getScaledWidth() / 2 - DurabilityAlert.mc.fontRenderer.getStringWidth(name + " has low durability!") / 2), (float)yCount, ColorUtils.Colors.RED);
                    yCount += 10;
                }
            }
        }
    }
}
