package dev.tenacity.command.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;

import java.nio.charset.StandardCharsets;

import static dev.tenacity.util.Utils.mc;

public final class NameCommand extends AbstractCommand {

    public static String CN = "Tenacity";
    public static String CV = "Dev 6.0";

    public NameCommand() {
        super("name", "Renames the client", ".name (new name) (version number)", 2);
    }

    @Override
    public void onCommand(final String[] arguments) {
        if (arguments.length > 0) {
            String newName = arguments[0];
            CN = newName;
            ChatUtil.notify("Name updated to: " + CN);
        } else {
            ChatUtil.warn("Please provide a new name!");
        }

        if (arguments.length > 1) {
            String newVersion = arguments[1];
            CV = newVersion;
            ChatUtil.notify("Version updated to: " + CV);
        } else {
            ChatUtil.warn("Please provide a new version.");
        }
    }
}