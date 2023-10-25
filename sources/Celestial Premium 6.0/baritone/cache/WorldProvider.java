/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.Baritone;
import baritone.api.cache.IWorldProvider;
import baritone.api.utils.Helper;
import baritone.cache.WorldData;
import baritone.utils.accessor.IAnvilChunkLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.commons.lang3.SystemUtils;

public class WorldProvider
implements IWorldProvider,
Helper {
    private static final Map<Path, WorldData> worldCache = new HashMap<Path, WorldData>();
    private WorldData currentWorld;

    @Override
    public final WorldData getCurrentWorld() {
        return this.currentWorld;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void initWorld(int dimension) {
        Object object;
        File readme;
        File directory;
        IntegratedServer integratedServer = mc.getIntegratedServer();
        if (mc.isSingleplayer()) {
            WorldServer localServerWorld = integratedServer.getWorld(dimension);
            ChunkProviderServer provider = localServerWorld.getChunkProvider();
            IAnvilChunkLoader loader = (IAnvilChunkLoader)((Object)provider.getChunkLoader());
            directory = loader.getChunkSaveLocation();
            if (directory.toPath().relativize(WorldProvider.mc.gameDir.toPath()).getNameCount() != 2) {
                directory = directory.getParentFile();
            }
            readme = directory = new File(directory, "baritone");
        } else {
            String folderName = WorldProvider.mc.getCurrentServerData().serverIP;
            if (SystemUtils.IS_OS_WINDOWS) {
                folderName = folderName.replace(":", "_");
            }
            directory = new File(Baritone.getDir(), folderName);
            readme = Baritone.getDir();
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(readme, "readme.txt"));
            object = null;
            try {
                out.write("https://github.com/cabaletta/baritone\n".getBytes());
            }
            catch (Throwable throwable) {
                object = throwable;
                throw throwable;
            }
            finally {
                if (out != null) {
                    if (object != null) {
                        try {
                            out.close();
                        }
                        catch (Throwable throwable) {
                            ((Throwable)object).addSuppressed(throwable);
                        }
                    } else {
                        out.close();
                    }
                }
            }
        }
        catch (IOException out) {
            // empty catch block
        }
        Path dir = new File(directory, "DIM" + dimension).toPath();
        if (!Files.exists(dir, new LinkOption[0])) {
            try {
                Files.createDirectories(dir, new FileAttribute[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        System.out.println("Baritone world data dir: " + dir);
        object = worldCache;
        synchronized (object) {
            this.currentWorld = worldCache.computeIfAbsent(dir, d -> new WorldData((Path)d, dimension));
        }
    }

    public final void closeWorld() {
        WorldData world = this.currentWorld;
        this.currentWorld = null;
        if (world == null) {
            return;
        }
        world.onClose();
    }

    public final void ifWorldLoaded(Consumer<WorldData> currentWorldConsumer) {
        if (this.currentWorld != null) {
            currentWorldConsumer.accept(this.currentWorld);
        }
    }
}

