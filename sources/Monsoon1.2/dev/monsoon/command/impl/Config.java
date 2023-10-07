package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import dev.monsoon.config.SaveLoad;

public class Config extends Command {
    public Config() {
        super("Config", "config issue", ".config <args>", "cfg");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 0) {
            String action = args[0];

            if(action.equalsIgnoreCase("load")) {
                SaveLoad saveLoad1 = new SaveLoad(args[1]);
                saveLoad1.load();
                Monsoon.sendMessage("Loaded config " + args[1]);
            }
            if(action.equalsIgnoreCase("save")) {
                SaveLoad saveLoad2 = new SaveLoad(args[1]);
                saveLoad2.save();
                Monsoon.sendMessage("Saved config " + args[1]);
            }
            if(action.equalsIgnoreCase("")) {
                SaveLoad saveLoad2 = new SaveLoad("default");
                saveLoad2.save();
                Monsoon.sendMessage("Saved config default");
            }
            if(action.equalsIgnoreCase("hypixel")) {

                //Modules
                Monsoon.manager.antiBot.setEnabled(true);
                Monsoon.manager.crasher.setEnabled(false);
                Monsoon.manager.fastEat.setEnabled(false);
                Monsoon.manager.criticals.setEnabled(false);

                //KillAura
                Monsoon.killAura.rotation.setMode("NCP");
                Monsoon.killAura.aps.setValue(10);
                Monsoon.killAura.range.setValue(4);
                Monsoon.killAura.autoBlock.setEnabled(true);

                //Speed
                Monsoon.speed.mode.setMode("Hypixel");

                //Scaffold
                Monsoon.scaffold.sprint.setEnabled(true);
                Monsoon.scaffold.safewalk.setEnabled(true);
                Monsoon.scaffold.timerBoost.setEnabled(false);
                Monsoon.scaffold.slowDown.setEnabled(true);
                Monsoon.scaffold.placeDelay.setValue(60);

                //ChestStealer
                Monsoon.cheststealer.delay.setValue(50);
                Monsoon.cheststealer.silent.setEnabled(false);

            }
        }
    }
}
