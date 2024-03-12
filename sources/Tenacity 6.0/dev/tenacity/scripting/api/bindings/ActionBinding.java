// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class ActionBinding
{
    public final int START_SNEAKING = 0;
    public final int STOP_SNEAKING = 1;
    public final int STOP_SLEEPING = 2;
    public final int START_SPRINTING = 3;
    public final int STOP_SPRINTING = 4;
    public final int RIDING_JUMP = 5;
    public final int OPEN_INVENTORY = 6;
    public final int INTERACT = 0;
    public final int ATTACK = 1;
    public final int INTERACT_AT = 2;
    public final int START_DESTROY_BLOCK = 0;
    public final int ABORT_DESTROY_BLOCK = 1;
    public final int STOP_DESTROY_BLOCK = 2;
    public final int DROP_ALL_ITEMS = 3;
    public final int DROP_ITEM = 4;
    public final int RELEASE_USE_ITEM = 5;
    public final int PERFORM_RESPAWN = 0;
    public final int REQUEST_STATS = 1;
    public final int OPEN_INVENTORY_ACHIEVEMENT = 2;
}
