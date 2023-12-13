// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.util.Iterator;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.combat.PopCounter;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.render.ExtraTab;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.friend.Friends;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.Xulu;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.module.Module;
import java.text.DecimalFormat;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class TextRadar extends Element
{
    private final Value<Boolean> pops;
    DecimalFormat decimalFormat;
    
    public TextRadar() {
        super("TextRadar");
        this.pops = this.register(new Value<Boolean>("Pop Count", this, true));
        this.decimalFormat = new DecimalFormat("#.#");
    }
    
    @Override
    public void onEnable() {
        this.width = 80.0;
        this.height = 80.0;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        float yCount = (float)this.y;
        for (final EntityPlayer entityPlayer : TextRadar.mc.world.playerEntities) {
            if (entityPlayer.getName().equals(TextRadar.mc.player.getName())) {
                continue;
            }
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(ChatFormatting.GRAY + "- " + (Friends.isFriend(entityPlayer.getName()) ? (Command.SECTIONSIGN() + ColorTextUtils.getColor(ExtraTab.INSTANCE.color.getValue()).substring(1) + entityPlayer.getName()) : entityPlayer.getName()) + " " + ChatFormatting.RED + this.decimalFormat.format(entityPlayer.getHealth()) + " " + ChatFormatting.DARK_GREEN + (int)TextRadar.mc.player.getDistance((Entity)entityPlayer) + ((PopCounter.INSTANCE.popMap.containsKey(entityPlayer) && this.pops.getValue()) ? (" " + ChatFormatting.DARK_RED + "-" + PopCounter.INSTANCE.popMap.get(entityPlayer)) : ""), (float)this.x, yCount, ColorUtils.changeAlpha(ColorUtils.Colors.WHITE, Global.hudAlpha.getValue()));
            }
            else {
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + "- " + (Friends.isFriend(entityPlayer.getName()) ? (Command.SECTIONSIGN() + ColorTextUtils.getColor(ExtraTab.INSTANCE.color.getValue()).substring(1) + entityPlayer.getName()) : entityPlayer.getName()) + " " + ChatFormatting.RED + this.decimalFormat.format(entityPlayer.getHealth()) + " " + ChatFormatting.DARK_GREEN + (int)TextRadar.mc.player.getDistance((Entity)entityPlayer) + ((PopCounter.INSTANCE.popMap.containsKey(entityPlayer) && this.pops.getValue()) ? (" " + ChatFormatting.DARK_RED + "-" + PopCounter.INSTANCE.popMap.get(entityPlayer)) : ""), (float)this.x, yCount, ColorUtils.changeAlpha(ColorUtils.Colors.WHITE, Global.hudAlpha.getValue()));
            }
            yCount += 10.0f;
        }
    }
}
