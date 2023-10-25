/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandDebug
extends CommandBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private long profileStartTime;
    private int profileStartTick;

    @Override
    public String getCommandName() {
        return "debug";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.debug.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.debug.usage", new Object[0]);
        }
        if ("start".equals(args[0])) {
            if (args.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            CommandDebug.notifyCommandListener(sender, (ICommand)this, "commands.debug.start", new Object[0]);
            server.enableProfiling();
            this.profileStartTime = MinecraftServer.getCurrentTimeMillis();
            this.profileStartTick = server.getTickCounter();
        } else {
            if (!"stop".equals(args[0])) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (args.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (!server.theProfiler.profilingEnabled) {
                throw new CommandException("commands.debug.notStarted", new Object[0]);
            }
            long i = MinecraftServer.getCurrentTimeMillis();
            int j = server.getTickCounter();
            long k = i - this.profileStartTime;
            int l = j - this.profileStartTick;
            this.saveProfilerResults(k, l, server);
            server.theProfiler.profilingEnabled = false;
            CommandDebug.notifyCommandListener(sender, (ICommand)this, "commands.debug.stop", String.format("%.2f", Float.valueOf((float)k / 1000.0f)), l);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void saveProfilerResults(long timeSpan, int tickSpan, MinecraftServer server) {
        File file1 = new File(server.getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
        file1.getParentFile().mkdirs();
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter((OutputStream)new FileOutputStream(file1), StandardCharsets.UTF_8);
            writer.write(this.getProfilerResults(timeSpan, tickSpan, server));
        }
        catch (Throwable throwable) {
            try {
                LOGGER.error("Could not save profiler results to {}", file1, throwable);
            }
            catch (Throwable throwable2) {
                IOUtils.closeQuietly(writer);
                throw throwable2;
            }
            IOUtils.closeQuietly(writer);
        }
        IOUtils.closeQuietly(writer);
    }

    private String getProfilerResults(long timeSpan, int tickSpan, MinecraftServer server) {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(CommandDebug.getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(timeSpan).append(" ms\n");
        stringbuilder.append("Tick span: ").append(tickSpan).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", Float.valueOf((float)tickSpan / ((float)timeSpan / 1000.0f)))).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.appendProfilerResults(0, "root", stringbuilder, server);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }

    private void appendProfilerResults(int p_184895_1_, String sectionName, StringBuilder builder, MinecraftServer server) {
        List<Profiler.Result> list = server.theProfiler.getProfilingData(sectionName);
        if (list != null && list.size() >= 3) {
            for (int i = 1; i < list.size(); ++i) {
                Profiler.Result profiler$result = list.get(i);
                builder.append(String.format("[%02d] ", p_184895_1_));
                for (int j = 0; j < p_184895_1_; ++j) {
                    builder.append("|   ");
                }
                builder.append(profiler$result.profilerName).append(" - ").append(String.format("%.2f", profiler$result.usePercentage)).append("%/").append(String.format("%.2f", profiler$result.totalUsePercentage)).append("%\n");
                if ("unspecified".equals(profiler$result.profilerName)) continue;
                try {
                    this.appendProfilerResults(p_184895_1_ + 1, sectionName + "." + profiler$result.profilerName, builder, server);
                    continue;
                }
                catch (Exception exception) {
                    builder.append("[[ EXCEPTION ").append(exception).append(" ]]");
                }
            }
        }
    }

    private static String getWittyComment() {
        String[] astring = new String[]{"Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."};
        try {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandDebug.getListOfStringsMatchingLastWord(args, "start", "stop") : Collections.emptyList();
    }
}

