// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import vip.Resolute.command.Command;

public class Clientname extends Command
{
    public static String nameofwatermark;
    
    public Clientname() {
        super("Clientname", "Clientname watermark", ".clientname <name> | .clientname reset", new String[] { ".clientname" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args[0].equalsIgnoreCase("reset")) {
            Clientname.nameofwatermark = null;
        }
        else {
            Clientname.nameofwatermark = String.join(" ", (CharSequence[])args);
            Clientname.nameofwatermark = Clientname.nameofwatermark.replace("&", "§");
            Clientname.nameofwatermark = Clientname.nameofwatermark.replace("\\247", "§");
        }
    }
    
    static {
        Clientname.nameofwatermark = "Resolute";
    }
}
