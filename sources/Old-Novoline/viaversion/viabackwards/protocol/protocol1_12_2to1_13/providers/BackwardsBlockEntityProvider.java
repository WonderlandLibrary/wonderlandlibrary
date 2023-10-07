package viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import java.util.HashMap;
import java.util.Map;
import viaversion.viabackwards.ViaBackwards;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.*;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.minecraft.Position;
import viaversion.viaversion.api.platform.providers.Provider;

public class BackwardsBlockEntityProvider implements Provider {
    private final Map<String, BackwardsBlockEntityHandler> handlers = new HashMap<>();

    public BackwardsBlockEntityProvider() {
        handlers.put("minecraft:flower_pot", new FlowerPotHandler()); // TODO requires special treatment, manually send
        handlers.put("minecraft:bed", new BedHandler());
        handlers.put("minecraft:banner", new BannerHandler());
        handlers.put("minecraft:skull", new SkullHandler());
        handlers.put("minecraft:mob_spawner", new SpawnerHandler());
        handlers.put("minecraft:piston", new PistonHandler());
    }

    /**
     * Check if a block entity handler is present
     *
     * @param key Id of the NBT data ex: minecraft:bed
     * @return true if present
     */
    public boolean isHandled(String key) {
        return handlers.containsKey(key);
    }

    /**
     * Transform blocks to block entities!
     *
     * @param user     The user
     * @param position The position of the block entity
     * @param tag      The block entity tag
     */
    public CompoundTag transform(UserConnection user, Position position, CompoundTag tag) throws Exception {
        String id = (String) tag.get("id").getValue();
        BackwardsBlockEntityHandler handler = handlers.get(id);
        if (handler == null) {
            if (Via.getManager().isDebug()) {
                ViaBackwards.getPlatform().getLogger().warning("Unhandled BlockEntity " + id + " full tag: " + tag);
            }
            return tag;
        }

        BackwardsBlockStorage storage = user.get(BackwardsBlockStorage.class);
        Integer blockId = storage.get(position);
        if (blockId == null) {
            if (Via.getManager().isDebug()) {
                ViaBackwards.getPlatform().getLogger().warning("Handled BlockEntity does not have a stored block :( " + id + " full tag: " + tag);
            }
            return tag;
        }

        return handler.transform(user, blockId, tag);
    }

    /**
     * Transform blocks to block entities!
     *
     * @param user     The user
     * @param position The position of the block entity
     * @param id       The block entity id
     */
    public CompoundTag transform(UserConnection user, Position position, String id) throws Exception {
        CompoundTag tag = new CompoundTag("");
        tag.put(new StringTag("id", id));
        tag.put(new IntTag("x", Math.toIntExact(position.getX())));
        tag.put(new IntTag("y", Math.toIntExact(position.getY())));
        tag.put(new IntTag("z", Math.toIntExact(position.getZ())));

        return this.transform(user, position, tag);
    }

    public interface BackwardsBlockEntityHandler {
        CompoundTag transform(UserConnection user, int blockId, CompoundTag tag);
    }
}
