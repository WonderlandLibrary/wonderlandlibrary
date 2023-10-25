/*
 * Decompiled with CFR 0.150.
 */
package baritone.api;

import baritone.api.IBaritone;
import baritone.api.cache.IWorldScanner;
import baritone.api.command.ICommandSystem;
import baritone.api.schematic.ISchematicSystem;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;

public interface IBaritoneProvider {
    public IBaritone getPrimaryBaritone();

    public List<IBaritone> getAllBaritones();

    default public IBaritone getBaritoneForPlayer(EntityPlayerSP player) {
        for (IBaritone baritone : this.getAllBaritones()) {
            if (!Objects.equals(player, baritone.getPlayerContext().player())) continue;
            return baritone;
        }
        return null;
    }

    public IWorldScanner getWorldScanner();

    public ICommandSystem getCommandSystem();

    public ISchematicSystem getSchematicSystem();
}

