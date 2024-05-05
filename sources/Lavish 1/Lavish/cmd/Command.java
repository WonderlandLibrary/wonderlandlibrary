// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd;

public class Command
{
    private final String name;
    private final String desc;
    private CommandExecutor executor;
    
    public Command(final String name, final String desc, final CommandExecutor executor) {
        this.name = name;
        this.desc = desc;
        this.executor = executor;
    }
    
    public Command(final String name, final String desc) {
        this(name, desc, null);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public CommandExecutor getExecutor() {
        return this.executor;
    }
    
    public void setExecutor(final CommandExecutor executor) {
        this.executor = executor;
    }
}
