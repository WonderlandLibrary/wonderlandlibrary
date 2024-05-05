package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityEnderPearl;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GPSCommand extends CommandAbstract {
    public static int x, z;
    public static String mode;

    public GPSCommand() {
        super("way", "way coommand", "Usage: .gps <x> <z> <off/on>", "way");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 3) {
          	ChatUtils.addChatMessage("Доступные команды:");
        	ChatUtils.addChatMessage("way c[x] [y]");
        } else {
        	if (args.length == 1 && args[1].equalsIgnoreCase("off")) {
        		mode = "off";
        	} else {
        		mode = args.length == 3 ? "on" : "off";
        	}
        	if (mode.equalsIgnoreCase("on")) {
                x = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            } else {
                x = 0;
                z = 0;
            }
        }
    }
}
