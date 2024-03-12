// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.minecraft.profiler.Profiler;
import java.io.FileWriter;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class CommandDebug extends CommandBase
{
    private static final Logger logger;
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.debug.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.debug.usage", new Object[0]);
        }
        if (args[0].equals("start")) {
            if (args.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            CommandBase.notifyOperators(sender, this, "commands.debug.start", new Object[0]);
            MinecraftServer.getServer().enableProfiling();
            this.profileStartTime = MinecraftServer.getCurrentTimeMillis();
            this.profileStartTick = MinecraftServer.getServer().getTickCounter();
        }
        else {
            if (!args[0].equals("stop")) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (args.length != 1) {
                throw new WrongUsageException("commands.debug.usage", new Object[0]);
            }
            if (!MinecraftServer.getServer().theProfiler.profilingEnabled) {
                throw new CommandException("commands.debug.notStarted", new Object[0]);
            }
            final long i = MinecraftServer.getCurrentTimeMillis();
            final int j = MinecraftServer.getServer().getTickCounter();
            final long k = i - this.profileStartTime;
            final int l = j - this.profileStartTick;
            this.saveProfileResults(k, l);
            MinecraftServer.getServer().theProfiler.profilingEnabled = false;
            CommandBase.notifyOperators(sender, this, "commands.debug.stop", k / 1000.0f, l);
        }
    }
    
    private void saveProfileResults(final long timeSpan, final int tickSpan) {
        final File file1 = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
        file1.getParentFile().mkdirs();
        try {
            final FileWriter filewriter = new FileWriter(file1);
            filewriter.write(this.getProfileResults(timeSpan, tickSpan));
            filewriter.close();
        }
        catch (Throwable throwable) {
            CommandDebug.logger.error("Could not save profiler results to " + file1, throwable);
        }
    }
    
    private String getProfileResults(final long timeSpan, final int tickSpan) {
        final StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(timeSpan).append(" ms\n");
        stringbuilder.append("Tick span: ").append(tickSpan).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", tickSpan / (timeSpan / 1000.0f))).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.func_147202_a(0, "root", stringbuilder);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }
    
    private void func_147202_a(final int p_147202_1_, final String p_147202_2_, final StringBuilder stringBuilder) {
        final List<Profiler.Result> list = MinecraftServer.getServer().theProfiler.getProfilingData(p_147202_2_);
        if (list != null && list.size() >= 3) {
            for (int i = 1; i < list.size(); ++i) {
                final Profiler.Result profiler$result = list.get(i);
                stringBuilder.append(String.format("[%02d] ", p_147202_1_));
                for (int j = 0; j < p_147202_1_; ++j) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(profiler$result.field_76331_c).append(" - ").append(String.format("%.2f", profiler$result.field_76332_a)).append("%/").append(String.format("%.2f", profiler$result.field_76330_b)).append("%\n");
                if (!profiler$result.field_76331_c.equals("unspecified")) {
                    try {
                        this.func_147202_a(p_147202_1_ + 1, p_147202_2_ + "." + profiler$result.field_76331_c, stringBuilder);
                    }
                    catch (Exception exception) {
                        stringBuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
                    }
                }
            }
        }
    }
    
    private static String getWittyComment() {
        final String[] astring = { "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
        try {
            return astring[(int)(System.nanoTime() % astring.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop") : null;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
