package com.canon.majik.impl.command.impl;

import com.canon.majik.impl.command.api.Command;

import java.util.List;

public class PlayerCommand extends Command {
    public PlayerCommand(String name, String description, String... syntax) {
        super(name,description,syntax);
    }

    @Override
    public void runCommand(List<String> args) {

    }
}
