package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.Display;

import java.util.Arrays;

public class WaterMarkCommand extends Command {

    public WaterMarkCommand() {
        super("WaterMark", "Set the client's watermark", "watermark <Text>", "wm");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length == 1) {
            return "Usage: " + getSyntax();
        }

        String waterMarkText = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        Display.setTitle(waterMarkText);

        return String.format("Set the watermark to " + EnumChatFormatting.GREEN + "%s", waterMarkText);
    }

}
