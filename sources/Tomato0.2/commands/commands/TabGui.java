/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.ui.GuiIngameHook;
import cow.milkgod.cheese.utils.Logger;
import java.io.PrintStream;

public class TabGui
extends Command {
    public TabGui() {
        super("tabGui", "Invalid Theme! ", new String[]{"tg", "tb"}, "<Cheese | Red | Green | Blue>", "Change the tabgui's theme.");
    }

    @EventTarget
    public void onTick(EventTick tick) {
        try {
            String Theme2 = EventChatSend.getMessage().split(" ")[1];
            System.out.println("Command");
            if (Theme2 == "red") {
                System.out.println("RedCommand");
                GuiIngameHook.tbBlue = false;
                GuiIngameHook.tbRed = true;
                GuiIngameHook.tbCheese = false;
                GuiIngameHook.tbBlue = false;
            }
            if (Theme2 == "blue") {
                GuiIngameHook.tbGreen = false;
                GuiIngameHook.tbRed = false;
                GuiIngameHook.tbCheese = false;
                GuiIngameHook.tbBlue = true;
            }
            if (Theme2 == "cheese") {
                GuiIngameHook.tbGreen = false;
                GuiIngameHook.tbRed = false;
                GuiIngameHook.tbCheese = true;
                GuiIngameHook.tbBlue = false;
            }
            if (Theme2 == "green") {
                GuiIngameHook.tbGreen = true;
                GuiIngameHook.tbRed = false;
                GuiIngameHook.tbCheese = false;
                GuiIngameHook.tbBlue = false;
            }
        }
        catch (Exception e) {
            Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
        }
        this.Toggle();
    }

    public static boolean isVowel(String c2) {
        if ("AEIOUaeiou".indexOf(c2) != -1) {
            return true;
        }
        return false;
    }
}

