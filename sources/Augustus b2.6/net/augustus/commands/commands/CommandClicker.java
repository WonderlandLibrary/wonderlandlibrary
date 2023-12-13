// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import net.augustus.utils.ClickRecordingUtil;
import net.augustus.commands.Command;

public class CommandClicker extends Command
{
    public final ClickRecordingUtil clickRecordingUtil;
    
    public CommandClicker() {
        super(".clicker");
        this.clickRecordingUtil = new ClickRecordingUtil();
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        if (message.length > 1) {
            if (message[1].equalsIgnoreCase("start")) {
                this.clickRecordingUtil.startRecording();
                this.sendChat("Started Recording... Please press sneak to record");
                return;
            }
            if (message[1].equalsIgnoreCase("stop")) {
                this.clickRecordingUtil.stopRecording();
                this.sendChat("Stopped Recording... ");
                return;
            }
        }
        this.sendChat("§7.clicker [§cstart/stop§7] ");
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.clicker§7 (Start or stop the click recording)");
    }
}
