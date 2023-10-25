/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandDebug
extends CommandBase {
    private int field_147207_c;
    private long field_147206_b;
    private static final Logger logger = LogManager.getLogger();

    private void func_147205_a(long l, int n) {
        File file = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
        file.getParentFile().mkdirs();
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(this.func_147204_b(l, n));
            fileWriter.close();
        }
        catch (Throwable throwable) {
            logger.error("Could not save profiler results to " + file, throwable);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandDebug.getListOfStringsMatchingLastWord(stringArray, "start", "stop") : null;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.debug.usage", new Object[0]);
        }
        if (stringArray[0].equals("start")) {
            if (stringArray.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            CommandDebug.notifyOperators(iCommandSender, (ICommand)this, "commands.debug.start", new Object[0]);
            MinecraftServer.getServer().enableProfiling();
            this.field_147206_b = MinecraftServer.getCurrentTimeMillis();
            this.field_147207_c = MinecraftServer.getServer().getTickCounter();
        } else {
            if (!stringArray[0].equals("stop")) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (stringArray.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (!MinecraftServer.getServer().theProfiler.profilingEnabled) {
                throw new CommandException("commands.debug.notStarted", new Object[0]);
            }
            long l = MinecraftServer.getCurrentTimeMillis();
            int n = MinecraftServer.getServer().getTickCounter();
            long l2 = l - this.field_147206_b;
            int n2 = n - this.field_147207_c;
            this.func_147205_a(l2, n2);
            MinecraftServer.getServer().theProfiler.profilingEnabled = false;
            CommandDebug.notifyOperators(iCommandSender, (ICommand)this, "commands.debug.stop", Float.valueOf((float)l2 / 1000.0f), n2);
        }
    }

    private String func_147204_b(long l, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---- Minecraft Profiler Results ----\n");
        stringBuilder.append("// ");
        stringBuilder.append(CommandDebug.func_147203_d());
        stringBuilder.append("\n\n");
        stringBuilder.append("Time span: ").append(l).append(" ms\n");
        stringBuilder.append("Tick span: ").append(n).append(" ticks\n");
        stringBuilder.append("// This is approximately ").append(String.format("%.2f", Float.valueOf((float)n / ((float)l / 1000.0f)))).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringBuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.func_147202_a(0, "root", stringBuilder);
        stringBuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringBuilder.toString();
    }

    @Override
    public String getCommandName() {
        return "debug";
    }

    private static String func_147203_d() {
        String[] stringArray = new String[]{"Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."};
        try {
            return stringArray[(int)(System.nanoTime() % (long)stringArray.length)];
        }
        catch (Throwable throwable) {
            return "Witty comment unavailable :(";
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.debug.usage";
    }

    private void func_147202_a(int n, String string, StringBuilder stringBuilder) {
        List<Profiler.Result> list = MinecraftServer.getServer().theProfiler.getProfilingData(string);
        if (list != null && list.size() >= 3) {
            int n2 = 1;
            while (n2 < list.size()) {
                Profiler.Result result = list.get(n2);
                stringBuilder.append(String.format("[%02d] ", n));
                int n3 = 0;
                while (n3 < n) {
                    stringBuilder.append(" ");
                    ++n3;
                }
                stringBuilder.append(result.field_76331_c).append(" - ").append(String.format("%.2f", result.field_76332_a)).append("%/").append(String.format("%.2f", result.field_76330_b)).append("%\n");
                if (!result.field_76331_c.equals("unspecified")) {
                    try {
                        this.func_147202_a(n + 1, String.valueOf(string) + "." + result.field_76331_c, stringBuilder);
                    }
                    catch (Exception exception) {
                        stringBuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
                    }
                }
                ++n2;
            }
        }
    }
}

