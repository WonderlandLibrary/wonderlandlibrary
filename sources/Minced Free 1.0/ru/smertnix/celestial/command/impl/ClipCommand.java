package ru.smertnix.celestial.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class ClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("vclip", "vclip | hclip", ".vclip | (hclip) <value>", "vclip", "hclip");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("vclip")) {
                try {

                    ChatUtils.addChatMessage("vclipped " + Double.valueOf(args[1]) + " blocks.");
                    for (int i = 0; i < 10; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));

                    }
                    for (int i = 0; i < 10; i++) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + (double) (Double.parseDouble(args[1])), mc.player.posZ, false));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + Double.parseDouble(args[1]), mc.player.posZ);

                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                try {
                    ChatUtils.addChatMessage("hclipped " + Double.valueOf(args[1]) + " blocks.");
                    float f = mc.player.rotationYaw * 0.017453292F;
                    double speed = Double.valueOf(args[1]);
                    double x = -(MathHelper.sin(f) * speed);
                    double z = MathHelper.cos(f) * speed;
                    double yaw = mc.player.rotationYaw * 0.017453292;
                    mc.player.setPosition(mc.player.posX + x, mc.player.posY, mc.player.posZ + z);
                } catch (Exception ignored) {
                }
            }
        } else {
        } 
        if (args[0].equalsIgnoreCase("vclip") && !(args.length > 1)) {
        } else if (args[0].equalsIgnoreCase("hclip") && !(args.length > 1)) {
        }
    }
}
