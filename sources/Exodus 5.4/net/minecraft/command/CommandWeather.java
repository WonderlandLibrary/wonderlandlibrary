/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import java.util.Random;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length >= 1 && stringArray.length <= 2) {
            int n = (300 + new Random().nextInt(600)) * 20;
            if (stringArray.length >= 2) {
                n = CommandWeather.parseInt(stringArray[1], 1, 1000000) * 20;
            }
            WorldServer worldServer = MinecraftServer.getServer().worldServers[0];
            WorldInfo worldInfo = worldServer.getWorldInfo();
            if ("clear".equalsIgnoreCase(stringArray[0])) {
                worldInfo.setCleanWeatherTime(n);
                worldInfo.setRainTime(0);
                worldInfo.setThunderTime(0);
                worldInfo.setRaining(false);
                worldInfo.setThundering(false);
                CommandWeather.notifyOperators(iCommandSender, (ICommand)this, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(stringArray[0])) {
                worldInfo.setCleanWeatherTime(0);
                worldInfo.setRainTime(n);
                worldInfo.setThunderTime(n);
                worldInfo.setRaining(true);
                worldInfo.setThundering(false);
                CommandWeather.notifyOperators(iCommandSender, (ICommand)this, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(stringArray[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }
                worldInfo.setCleanWeatherTime(0);
                worldInfo.setRainTime(n);
                worldInfo.setThunderTime(n);
                worldInfo.setRaining(true);
                worldInfo.setThundering(true);
                CommandWeather.notifyOperators(iCommandSender, (ICommand)this, "commands.weather.thunder", new Object[0]);
            }
        } else {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.weather.usage";
    }

    @Override
    public String getCommandName() {
        return "weather";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandWeather.getListOfStringsMatchingLastWord(stringArray, "clear", "rain", "thunder") : null;
    }
}

