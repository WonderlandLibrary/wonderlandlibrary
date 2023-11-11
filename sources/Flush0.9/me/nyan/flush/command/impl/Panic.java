package me.nyan.flush.command.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.command.Command;
import me.nyan.flush.module.Module;

public class Panic extends Command {
    public Panic() {
        super("Panic", "Disables all modules instantanly", "panic");
    }

    @Override
    public void onCommand(String[] args, String message) {
        flush.getModuleManager().getModules().forEach(Module::disable);
    }
}
