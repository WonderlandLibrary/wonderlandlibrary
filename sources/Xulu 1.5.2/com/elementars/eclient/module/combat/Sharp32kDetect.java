// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.Iterator;
import com.elementars.eclient.command.Command;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import com.elementars.eclient.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Sharp32kDetect extends Module
{
    private final Value<Boolean> watermark;
    private final Value<Boolean> color;
    private Set<EntityPlayer> sword;
    public static final Minecraft mc;
    
    public Sharp32kDetect() {
        super("32kDetect", "Detects held 32ks", 0, Category.COMBAT, true);
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<Boolean>("Color", this, false));
        this.sword = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    private boolean is32k(final EntityPlayer player, final ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            final NBTTagList enchants = stack.getEnchantmentTagList();
            if (enchants != null) {
                for (int i = 0; i < enchants.tagCount(); ++i) {
                    if (enchants.getCompoundTagAt(i).getShort("lvl") >= 32767) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : Sharp32kDetect.mc.world.playerEntities) {
            if (player.equals((Object)Sharp32kDetect.mc.player)) {
                continue;
            }
            if (this.is32k(player, player.itemStackMainHand) && !this.sword.contains(player)) {
                if (this.watermark.getValue()) {
                    if (this.color.getValue()) {
                        Command.sendChatMessage("&4" + player.getDisplayNameString() + " is holding a 32k");
                    }
                    else {
                        Command.sendChatMessage(player.getDisplayNameString() + " is holding a 32k");
                    }
                }
                else if (this.color.getValue()) {
                    Command.sendRawChatMessage("&4" + player.getDisplayNameString() + " is holding a 32k");
                }
                else {
                    Command.sendRawChatMessage(player.getDisplayNameString() + " is holding a 32k");
                }
                this.sword.add(player);
            }
            if (!this.sword.contains(player)) {
                continue;
            }
            if (this.is32k(player, player.itemStackMainHand)) {
                continue;
            }
            if (this.watermark.getValue()) {
                if (this.color.getValue()) {
                    Command.sendChatMessage("&2" + player.getDisplayNameString() + " is no longer holding a 32k");
                }
                else {
                    Command.sendChatMessage(player.getDisplayNameString() + " is no longer holding a 32k");
                }
            }
            else if (this.color.getValue()) {
                Command.sendRawChatMessage("&2" + player.getDisplayNameString() + " is no longer holding a 32k");
            }
            else {
                Command.sendRawChatMessage(player.getDisplayNameString() + " is no longer holding a 32k");
            }
            this.sword.remove(player);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
