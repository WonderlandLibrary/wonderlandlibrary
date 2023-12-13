/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class getName
extends Command {
    public getName() {
        super("GetName", null, new String[]{"gn", "name"}, null, "Copies your current username to clipboard.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection name = new StringSelection(Wrapper.mc.getSession().getUsername());
            clipboard.setContents(name, null);
            Logger.logChat("Copied your username to clipboard.");
            this.Toggle();
        }
    }
}

