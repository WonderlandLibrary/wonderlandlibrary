/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.module.Module;
import me.arithmo.util.misc.ChatUtil;

public class Save
extends Command {
    public Save(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        Module.saveStatus();
        Module.saveSettings();
        ChatUtil.printChat("Saved");
    }

    @Override
    public String getUsage() {
        return null;
    }

    public void onEvent(Event event) {
    }
}

