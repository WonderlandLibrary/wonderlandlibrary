package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;

public class Rename extends Command
{
    public Rename() {
        super("Rename", "Renames the client", "rename <clientname>", new String[] { "rename" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            Astomero.instance.name = String.join(" ", (CharSequence[])args);
        }
    }
}
