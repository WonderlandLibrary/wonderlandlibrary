// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.ArrayDeque;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.advancements.FunctionManager;

public class FunctionObject
{
    private final Entry[] entries;
    
    public FunctionObject(final Entry[] entriesIn) {
        this.entries = entriesIn;
    }
    
    public Entry[] getEntries() {
        return this.entries;
    }
    
    public static FunctionObject create(final FunctionManager functionManagerIn, final List<String> commands) {
        final List<Entry> list = (List<Entry>)Lists.newArrayListWithCapacity(commands.size());
        for (String s : commands) {
            s = s.trim();
            if (!s.startsWith("#") && !s.isEmpty()) {
                final String[] astring = s.split(" ", 2);
                final String s2 = astring[0];
                if (!functionManagerIn.getCommandManager().getCommands().containsKey(s2)) {
                    if (s2.startsWith("//")) {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "' (if you intended to make a comment, use '#' not '//')");
                    }
                    if (s2.startsWith("/") && s2.length() > 1) {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "' (did you mean '" + s2.substring(1) + "'? Do not use a preceding forwards slash.)");
                    }
                    throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "'");
                }
                else {
                    list.add(new CommandEntry(s));
                }
            }
        }
        return new FunctionObject(list.toArray(new Entry[list.size()]));
    }
    
    public static class CacheableFunction
    {
        public static final CacheableFunction EMPTY;
        @Nullable
        private final ResourceLocation id;
        private boolean isValid;
        private FunctionObject function;
        
        public CacheableFunction(@Nullable final ResourceLocation idIn) {
            this.id = idIn;
        }
        
        public CacheableFunction(final FunctionObject functionIn) {
            this.id = null;
            this.function = functionIn;
        }
        
        @Nullable
        public FunctionObject get(final FunctionManager functionManagerIn) {
            if (!this.isValid) {
                if (this.id != null) {
                    this.function = functionManagerIn.getFunction(this.id);
                }
                this.isValid = true;
            }
            return this.function;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.id);
        }
        
        static {
            EMPTY = new CacheableFunction((ResourceLocation)null);
        }
    }
    
    public static class CommandEntry implements Entry
    {
        private final String command;
        
        public CommandEntry(final String p_i47534_1_) {
            this.command = p_i47534_1_;
        }
        
        @Override
        public void execute(final FunctionManager functionManagerIn, final ICommandSender sender, final ArrayDeque<FunctionManager.QueuedCommand> commandQueue, final int maxCommandChainLength) {
            functionManagerIn.getCommandManager().executeCommand(sender, this.command);
        }
        
        @Override
        public String toString() {
            return "/" + this.command;
        }
    }
    
    public static class FunctionEntry implements Entry
    {
        private final CacheableFunction function;
        
        public FunctionEntry(final FunctionObject functionIn) {
            this.function = new CacheableFunction(functionIn);
        }
        
        @Override
        public void execute(final FunctionManager functionManagerIn, final ICommandSender sender, final ArrayDeque<FunctionManager.QueuedCommand> commandQueue, final int maxCommandChainLength) {
            final FunctionObject functionobject = this.function.get(functionManagerIn);
            if (functionobject != null) {
                final Entry[] afunctionobject$entry = functionobject.getEntries();
                final int i = maxCommandChainLength - commandQueue.size();
                final int j = Math.min(afunctionobject$entry.length, i);
                for (int k = j - 1; k >= 0; --k) {
                    commandQueue.addFirst(new FunctionManager.QueuedCommand(functionManagerIn, sender, afunctionobject$entry[k]));
                }
            }
        }
        
        @Override
        public String toString() {
            return "/function " + this.function;
        }
    }
    
    public interface Entry
    {
        void execute(final FunctionManager p0, final ICommandSender p1, final ArrayDeque<FunctionManager.QueuedCommand> p2, final int p3);
    }
}
