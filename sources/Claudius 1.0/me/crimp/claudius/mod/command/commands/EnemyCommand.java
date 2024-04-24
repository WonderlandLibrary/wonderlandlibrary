package me.crimp.claudius.mod.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.managers.EnemyManager;
import me.crimp.claudius.mod.command.Command;

public class EnemyCommand
        extends Command {
    public EnemyCommand() {
        super("Enemy", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            if (claudius.enemyManager.getEnemys().isEmpty()) {
                EnemyCommand.sendMessage("Enemy list empty D:.");
            } else {
                String f = "Enemys: ";
                for (EnemyManager.Enemy Enemy : claudius.enemyManager.getEnemys()) {
                    try {
                        f = f + Enemy.getUsername() + ", ";
                    } catch (Exception exception) {
                    }
                }
                EnemyCommand.sendMessage(f);
            }
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    claudius.enemyManager.onLoad();
                    EnemyCommand.sendMessage("Enemys got reset.");
                    return;
                }
            }
            EnemyCommand.sendMessage(commands[0] + (claudius.enemyManager.isEnemy(commands[0]) ? " is Enemyed." : " isn't Enemyed."));
            return;
        }
        if (commands.length >= 2) {
            switch (commands[0]) {
                case "add": {
                    claudius.enemyManager.addEnemy(commands[1]);
                    EnemyCommand.sendMessage(ChatFormatting.GREEN + commands[1] + " has been Enemyed");
                    return;
                }
                case "del": {
                    claudius.enemyManager.removeEnemy(commands[1]);
                    EnemyCommand.sendMessage(ChatFormatting.RED + commands[1] + " has been unEnemyed");
                    return;
                }
            }
            EnemyCommand.sendMessage("Unknown Command, try Enemy add/del (name)");
        }
    }
}

