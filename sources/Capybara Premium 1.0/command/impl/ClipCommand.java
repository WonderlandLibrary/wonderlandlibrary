package fun.expensive.client.command.impl;

import fun.rich.client.command.CommandAbstract;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

public class ClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("vclip", "vclip | hclip", "§6.vclip | (hclip) §7<value>", "vclip", "hclip");
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
                    mc.player.setPosition(mc.player.posX + x, mc.player.posY, mc.player.posZ + z);
                } catch (Exception ignored) {
                }
            }
        } else {
            ChatUtils.addChatMessage(getUsage());
        }
    }
}
