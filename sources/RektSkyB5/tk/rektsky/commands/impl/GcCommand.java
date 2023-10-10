/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import tk.rektsky.commands.Command;

public class GcCommand
extends Command {
    public GcCommand() {
        super("gc", "", "Clear your ram");
    }

    @Override
    public void onCommand(String label, String[] args) {
        System.gc();
    }
}

