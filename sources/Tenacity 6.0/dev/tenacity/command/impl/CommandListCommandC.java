package dev.tenacity.command.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.tenacity.Tenacity;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.exception.UnknownModuleException;
import dev.tenacity.module.Module;
import dev.tenacity.util.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public final class CommandListCommandC extends AbstractCommand {

    public CommandListCommandC() {
        super("cm", "List commands", ".command | .cm", 1);
    }

    @Override
    public void onCommand(final String[] arguments) {
        boolean usage = false;
        switch (arguments[0].toLowerCase()) {
            case "list": {
                ChatUtil.notify(" | .b or .bind to bind a module to a key | .f or .friend to add/remove/list friends | .c or .config to save/load/list configs | .cm or command to view this message | .cat to show an Ascii art cat | ");
                break;
            }
        }
    }
}
