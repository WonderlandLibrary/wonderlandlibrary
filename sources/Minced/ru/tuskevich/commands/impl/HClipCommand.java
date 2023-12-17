// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.math.NumberUtils;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "hclip", description = "Allows you to clip horizontal")
public class HClipCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length >= 2) {
            try {
                if (NumberUtils.isNumber(args[1])) {
                    final Minecraft mc = HClipCommand.mc;
                    final float f = Minecraft.player.rotationYaw * 0.017453292f;
                    final double speed = Double.parseDouble(args[1]);
                    final double x = -(MathHelper.sin(f) * speed);
                    final double z = MathHelper.cos(f) * speed;
                    final Minecraft mc2 = HClipCommand.mc;
                    final EntityPlayerSP player = Minecraft.player;
                    final Minecraft mc3 = HClipCommand.mc;
                    final double x2 = Minecraft.player.posX + x;
                    final Minecraft mc4 = HClipCommand.mc;
                    final double posY = Minecraft.player.posY;
                    final Minecraft mc5 = HClipCommand.mc;
                    player.setPositionAndUpdate(x2, posY, Minecraft.player.posZ + z);
                }
                else {
                    this.sendMessage("Invalid input");
                }
            }
            catch (Exception ex) {}
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use:" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".hclip " + ChatFormatting.BLUE + "<value>");
    }
}
