/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;

public class Teams
extends Command {
    public static ArrayList<String> teamplayers = new ArrayList();

    public Teams() {
        super("Teams", "Adds a player to your team list so that the killaura doesn't target them.", "teams (ADD/REMOVE) (NAME)", "teams");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length < 1) {
            Exodus.addChatMessage("Syntax error!");
        } else {
            if (stringArray[0].equals("add") && stringArray[1] != null) {
                teamplayers.add(stringArray[1]);
                Exodus.addChatMessage("Added " + stringArray[1].toString() + " to the team list.");
            }
            if (stringArray[0].equals("remove") && stringArray[1] != null) {
                teamplayers.remove(stringArray[1]);
                Exodus.addChatMessage("Removed " + stringArray[1].toString() + " from the team list.");
            }
            if (stringArray[0].equals("list")) {
                Exodus.addChatMessage("Current team list: " + teamplayers.toString());
            }
        }
    }
}

