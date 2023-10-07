package com.ohare.client.command.impl;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.ohare.client.Client;
import com.ohare.client.command.Command;
import com.ohare.client.utils.Printer;

public class Macros extends Command {

    public Macros() {
        super("Macros", new String[]{"macros", "mac", "macro"});
    }

    @Override
    public void onRun(final String[] args) {
        switch (args[1].toLowerCase()) {
            case "list":
                if (Client.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                    Printer.print("Your macro list is empty.");
                    return;
                }
                Printer.print("Your macros are:");
                Client.INSTANCE.getMacroManager().getMacros().values().forEach(macro -> Printer.print("Label: " + macro.getLabel() + ", Keybind: " + Keyboard.getKeyName(macro.getKey()) + ", Text: " + macro.getText() + "."));
                break;
            case "reload":
                Client.INSTANCE.getMacroManager().clearMacros();
                Client.INSTANCE.getMacroManager().load();
                Printer.print("Reloaded macros.");
                break;
            case "remove":
            case "delete":
                if (args.length < 3) {
                    Printer.print("Invalid args.");
                    return;
                }
                if (Client.INSTANCE.getMacroManager().isMacro(args[2])) {
                    Client.INSTANCE.getMacroManager().removeMacroByLabel(args[2]);
                    Printer.print("Removed a macro named " + args[2] + ".");
                    if (Client.INSTANCE.getMacroManager().getMacroFile().exists()) {
                        Client.INSTANCE.getMacroManager().save();
                    } else {
                        try {
                            Client.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Client.INSTANCE.getMacroManager().save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Printer.print(args[2] + " is not a macro.");
                }
                break;
            case "clear":
                if (Client.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                    Printer.print("Your macro list is empty.");
                    return;
                }
                Printer.print("Cleared all macros.");
                Client.INSTANCE.getMacroManager().clearMacros();
                if (Client.INSTANCE.getMacroManager().getMacroFile().exists()) {
                    Client.INSTANCE.getMacroManager().save();
                } else {
                    try {
                        Client.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                        Client.INSTANCE.getMacroManager().save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "add":
            case "create":
                if (args.length < 5) {
                    Printer.print("Invalid args.");
                    return;
                }
                int keyCode = Keyboard.getKeyIndex(args[3].toUpperCase());
                if (keyCode != -1 && !Keyboard.getKeyName(keyCode).equals("NONE")) {
                    if (Client.INSTANCE.getMacroManager().getMacroByKey(keyCode) != null) {
                        Printer.print("There is already a macro bound to that key.");
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 4; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        if (i != args.length - 1) stringBuilder.append(" ");
                    }
                    Client.INSTANCE.getMacroManager().addMacro(args[2], keyCode, stringBuilder.toString());
                    Printer.print("Bound a macro named " + args[2] + " to the key " + Keyboard.getKeyName(keyCode) + ".");
                    if (Client.INSTANCE.getMacroManager().getMacroFile().exists()) {
                        Client.INSTANCE.getMacroManager().save();
                    } else {
                        try {
                            Client.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Client.INSTANCE.getMacroManager().save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Printer.print("That is not a valid key code.");
                }
                break;
        }
    }
}