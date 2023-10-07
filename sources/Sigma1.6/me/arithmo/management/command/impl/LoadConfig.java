/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.module.Module;
import me.arithmo.util.misc.ChatUtil;

public class LoadConfig
extends Command {
    public LoadConfig(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        Module.loadStatus();
        Module.loadSettings();
        ChatUtil.printChat("Loaded");
    }

    @Override
    public String getUsage() {
        return null;
    }

    public void onEvent(Event event) {
    }
}

