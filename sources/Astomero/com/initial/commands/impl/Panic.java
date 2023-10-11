package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;
import com.initial.modules.*;
import java.util.*;

public class Panic extends Command
{
    public Panic() {
        super("Panic", "Disables all mods instantly.", "panic", new String[] { "panic" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 1) {
            for (final Module m : Astomero.instance.moduleManager.getModules()) {
                m.setToggled(false);
            }
        }
    }
}
