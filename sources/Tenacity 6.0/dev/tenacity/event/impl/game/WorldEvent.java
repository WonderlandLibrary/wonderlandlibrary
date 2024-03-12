// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.game;

import net.minecraft.world.World;
import dev.tenacity.event.Event;

public class WorldEvent extends Event
{
    public final World world;
    
    public WorldEvent(final World world) {
        this.world = world;
    }
    
    public static class Load extends WorldEvent
    {
        public Load(final World world) {
            super(world);
        }
    }
    
    public static class Unload extends WorldEvent
    {
        public Unload(final World world) {
            super(world);
        }
    }
}
