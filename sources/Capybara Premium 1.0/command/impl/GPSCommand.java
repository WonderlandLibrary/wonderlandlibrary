package fun.rich.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.other.ChatUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityEnderPearl;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GPSCommand extends CommandAbstract {
    public static int x, z;
    public static String mode;

    public GPSCommand() {
        super("gps", "gps coommand", "§bUsage: §6.gps <x> <z> <off/on>", "gps");
    }

    @Override
    public void execute(String... args) {
        if (args.length < 4) {
            ChatUtils.addChatMessage(this.getUsage());
        } else {
            mode = args[3].toLowerCase();
            if (mode.equalsIgnoreCase("on")) {
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } else if (mode.equalsIgnoreCase("off")) {
                x = 0;
                z = 0;
            }
        }
    }
}
