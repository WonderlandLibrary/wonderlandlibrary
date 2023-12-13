// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.macro;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.command.CommandManager;

public class Macro
{
    private String macro;
    private int key;
    
    public Macro(final String msg, final int key) {
        this.macro = msg;
        this.key = key;
    }
    
    public String getMacro() {
        return this.macro;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void runMacro() {
        if (this.macro.startsWith(".")) {
            CommandManager.runCommand(this.macro.substring(1));
        }
        else if (Wrapper.getMinecraft().getConnection() != null) {
            Wrapper.getMinecraft().getConnection().sendPacket((Packet)new CPacketChatMessage(this.macro));
        }
    }
}
