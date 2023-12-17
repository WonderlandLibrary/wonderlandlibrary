// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.server.MinecraftServer;

public class WorldServerDemo extends WorldServer
{
    private static final long DEMO_WORLD_SEED;
    public static final WorldSettings DEMO_WORLD_SETTINGS;
    
    public WorldServerDemo(final MinecraftServer server, final ISaveHandler saveHandlerIn, final WorldInfo worldInfoIn, final int dimensionId, final Profiler profilerIn) {
        super(server, saveHandlerIn, worldInfoIn, dimensionId, profilerIn);
        this.worldInfo.populateFromWorldSettings(WorldServerDemo.DEMO_WORLD_SETTINGS);
    }
    
    static {
        DEMO_WORLD_SEED = "North Carolina".hashCode();
        DEMO_WORLD_SETTINGS = new WorldSettings(WorldServerDemo.DEMO_WORLD_SEED, GameType.SURVIVAL, true, false, WorldType.DEFAULT).enableBonusChest();
    }
}
