/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.utils.Logger;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;

public class Coords
extends Command {
    public Coords() {
        super("Coords", null, new String[]{"getcoords", "gcoords", "gc"}, "none", "Copies your current coords to clipboard.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            int x2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posX);
            int y2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posY);
            int z2 = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
            StringSelection coords = new StringSelection(String.valueOf(String.valueOf(x2)) + " " + y2 + " " + z2);
            clipboard.setContents(coords, null);
            Logger.logChat("Copied your coords to clipboard.");
            this.Toggle();
        }
    }
}

