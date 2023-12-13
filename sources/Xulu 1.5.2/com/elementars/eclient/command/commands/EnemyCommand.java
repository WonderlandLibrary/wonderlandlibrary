// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.enemy.Enemy;
import com.elementars.eclient.command.Command;

public class EnemyCommand extends Command
{
    public EnemyCommand() {
        super("enemy", "adds or deletes enemies", new String[] { "add", "del" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Try .enemy add or .enemy del");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
        }
        if (args.length < 3) {
            Command.sendChatMessage("Specify a username");
            return;
        }
        final Enemy enemy = new Enemy(args[2]);
        if (args[1].equalsIgnoreCase("add")) {
            if (!Enemies.getEnemies().contains((Object)enemy)) {
                Enemies.addEnemy(enemy.getUsername());
                Command.sendChatMessage(enemy.getUsername() + " is now an enemy");
            }
            else {
                Command.sendChatMessage(enemy.getUsername() + " is already an enemy!");
            }
        }
        else if (args[1].equalsIgnoreCase("del")) {
            if (Enemies.getEnemyByName(enemy.getUsername()) != null) {
                Enemies.delEnemy(enemy.getUsername());
                Command.sendChatMessage(enemy.getUsername() + " is no longer an enemy");
            }
            else {
                Command.sendChatMessage(enemy.getUsername() + " isn't an enemy");
            }
        }
        else {
            Command.sendChatMessage("Unknown attribute '" + args[1] + "'");
        }
    }
}
