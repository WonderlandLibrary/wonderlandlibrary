package wtf.expensive.command.impl;

import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;

import static wtf.expensive.util.IMinecraft.mc;

@CommandInfo(name = "hclip", description = "Телепортирует вас вперед.")
public class HClipCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        Vector3d tp = Minecraft.getInstance().player.getLook(1F).mul(Double.parseDouble(args[1]), 0, Double.parseDouble(args[1]));
        Minecraft.getInstance().player.setPosition(mc.player.getPosX() + tp.getX(), mc.player.getPosY(), mc.player.getPosZ() + tp.getZ());
    }

    @Override
    public void error() {

    }
}
