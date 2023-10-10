// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

import oshi.annotation.concurrent.Immutable;

@Immutable
public class OSService
{
    private final String name;
    private final int processID;
    private final State state;
    
    public OSService(final String name, final int processID, final State state) {
        this.name = name;
        this.processID = processID;
        this.state = state;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getProcessID() {
        return this.processID;
    }
    
    public State getState() {
        return this.state;
    }
    
    public enum State
    {
        RUNNING, 
        STOPPED, 
        OTHER;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.RUNNING, State.STOPPED, State.OTHER };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
