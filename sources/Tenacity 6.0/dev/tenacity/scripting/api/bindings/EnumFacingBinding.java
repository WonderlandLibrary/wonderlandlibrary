// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class EnumFacingBinding
{
    public final int DOWN = 0;
    public final int UP = 1;
    public final int NORTH = 2;
    public final int SOUTH = 3;
    public final int WEST = 4;
    public final int EAST = 5;
}
