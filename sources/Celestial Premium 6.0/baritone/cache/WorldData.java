/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.Baritone;
import baritone.api.cache.ICachedWorld;
import baritone.api.cache.IContainerMemory;
import baritone.api.cache.IWaypointCollection;
import baritone.api.cache.IWorldData;
import baritone.cache.CachedWorld;
import baritone.cache.ContainerMemory;
import baritone.cache.WaypointCollection;
import java.io.IOException;
import java.nio.file.Path;

public class WorldData
implements IWorldData {
    public final CachedWorld cache;
    private final WaypointCollection waypoints;
    private final ContainerMemory containerMemory;
    public final Path directory;
    public final int dimension;

    WorldData(Path directory, int dimension) {
        this.directory = directory;
        this.cache = new CachedWorld(directory.resolve("cache"), dimension);
        this.waypoints = new WaypointCollection(directory.resolve("waypoints"));
        this.containerMemory = new ContainerMemory(directory.resolve("containers"));
        this.dimension = dimension;
    }

    public void onClose() {
        Baritone.getExecutor().execute(() -> {
            System.out.println("Started saving the world in a new thread");
            this.cache.save();
        });
        Baritone.getExecutor().execute(() -> {
            System.out.println("Started saving saved containers in a new thread");
            try {
                this.containerMemory.save();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save saved containers");
            }
        });
    }

    @Override
    public ICachedWorld getCachedWorld() {
        return this.cache;
    }

    @Override
    public IWaypointCollection getWaypoints() {
        return this.waypoints;
    }

    @Override
    public IContainerMemory getContainerMemory() {
        return this.containerMemory;
    }
}

