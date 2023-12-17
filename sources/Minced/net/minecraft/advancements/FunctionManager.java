// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.List;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import net.minecraft.command.ICommandManager;
import net.minecraft.world.World;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandSender;
import java.util.ArrayDeque;
import net.minecraft.command.FunctionObject;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;

public class FunctionManager implements ITickable
{
    private static final Logger LOGGER;
    private final File functionDir;
    private final MinecraftServer server;
    private final Map<ResourceLocation, FunctionObject> functions;
    private String currentGameLoopFunctionId;
    private FunctionObject gameLoopFunction;
    private final ArrayDeque<QueuedCommand> commandQueue;
    private boolean isExecuting;
    private final ICommandSender gameLoopFunctionSender;
    
    public FunctionManager(@Nullable final File functionDirIn, final MinecraftServer serverIn) {
        this.functions = (Map<ResourceLocation, FunctionObject>)Maps.newHashMap();
        this.currentGameLoopFunctionId = "-";
        this.commandQueue = new ArrayDeque<QueuedCommand>();
        this.isExecuting = false;
        this.gameLoopFunctionSender = new ICommandSender() {
            @Override
            public String getName() {
                return FunctionManager.this.currentGameLoopFunctionId;
            }
            
            @Override
            public boolean canUseCommand(final int permLevel, final String commandName) {
                return permLevel <= 2;
            }
            
            @Override
            public World getEntityWorld() {
                return FunctionManager.this.server.worlds[0];
            }
            
            @Override
            public MinecraftServer getServer() {
                return FunctionManager.this.server;
            }
        };
        this.functionDir = functionDirIn;
        this.server = serverIn;
        this.reload();
    }
    
    @Nullable
    public FunctionObject getFunction(final ResourceLocation id) {
        return this.functions.get(id);
    }
    
    public ICommandManager getCommandManager() {
        return this.server.getCommandManager();
    }
    
    public int getMaxCommandChainLength() {
        return this.server.worlds[0].getGameRules().getInt("maxCommandChainLength");
    }
    
    public Map<ResourceLocation, FunctionObject> getFunctions() {
        return this.functions;
    }
    
    @Override
    public void update() {
        final String s = this.server.worlds[0].getGameRules().getString("gameLoopFunction");
        if (!s.equals(this.currentGameLoopFunctionId)) {
            this.currentGameLoopFunctionId = s;
            this.gameLoopFunction = this.getFunction(new ResourceLocation(s));
        }
        if (this.gameLoopFunction != null) {
            this.execute(this.gameLoopFunction, this.gameLoopFunctionSender);
        }
    }
    
    public int execute(final FunctionObject function, final ICommandSender sender) {
        final int i = this.getMaxCommandChainLength();
        if (this.isExecuting) {
            if (this.commandQueue.size() < i) {
                this.commandQueue.addFirst(new QueuedCommand(this, sender, new FunctionObject.FunctionEntry(function)));
            }
            return 0;
        }
        int l;
        try {
            this.isExecuting = true;
            int j = 0;
            final FunctionObject.Entry[] afunctionobject$entry = function.getEntries();
            for (int k = afunctionobject$entry.length - 1; k >= 0; --k) {
                this.commandQueue.push(new QueuedCommand(this, sender, afunctionobject$entry[k]));
            }
            while (!this.commandQueue.isEmpty()) {
                this.commandQueue.removeFirst().execute(this.commandQueue, i);
                if (++j >= i) {
                    l = j;
                    return l;
                }
            }
            l = j;
            return l;
        }
        finally {
            this.commandQueue.clear();
            this.isExecuting = false;
        }
        return l;
    }
    
    public void reload() {
        this.functions.clear();
        this.gameLoopFunction = null;
        this.currentGameLoopFunctionId = "-";
        this.loadFunctions();
    }
    
    private void loadFunctions() {
        if (this.functionDir != null) {
            this.functionDir.mkdirs();
            for (final File file1 : FileUtils.listFiles(this.functionDir, new String[] { "mcfunction" }, true)) {
                final String s = FilenameUtils.removeExtension(this.functionDir.toURI().relativize(file1.toURI()).toString());
                final String[] astring = s.split("/", 2);
                if (astring.length == 2) {
                    final ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);
                    try {
                        this.functions.put(resourcelocation, FunctionObject.create(this, Files.readLines(file1, StandardCharsets.UTF_8)));
                    }
                    catch (Throwable throwable) {
                        FunctionManager.LOGGER.error("Couldn't read custom function " + resourcelocation + " from " + file1, throwable);
                    }
                }
            }
            if (!this.functions.isEmpty()) {
                FunctionManager.LOGGER.info("Loaded " + this.functions.size() + " custom command functions");
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class QueuedCommand
    {
        private final FunctionManager functionManager;
        private final ICommandSender sender;
        private final FunctionObject.Entry entry;
        
        public QueuedCommand(final FunctionManager functionManagerIn, final ICommandSender senderIn, final FunctionObject.Entry entryIn) {
            this.functionManager = functionManagerIn;
            this.sender = senderIn;
            this.entry = entryIn;
        }
        
        public void execute(final ArrayDeque<QueuedCommand> commandQueue, final int maxCommandChainLength) {
            this.entry.execute(this.functionManager, this.sender, commandQueue, maxCommandChainLength);
        }
        
        @Override
        public String toString() {
            return this.entry.toString();
        }
    }
}
