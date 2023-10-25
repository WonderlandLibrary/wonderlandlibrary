/*
 * Decompiled with CFR 0.150.
 */
package baritone;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.IBaritoneProvider;
import baritone.api.cache.IWorldScanner;
import baritone.api.command.ICommandSystem;
import baritone.api.schematic.ISchematicSystem;
import baritone.cache.WorldScanner;
import baritone.command.CommandSystem;
import baritone.command.ExampleBaritoneControl;
import baritone.utils.schematic.SchematicSystem;
import java.util.Collections;
import java.util.List;

public final class BaritoneProvider
implements IBaritoneProvider {
    private final Baritone primary = new Baritone();
    private final List<IBaritone> all = Collections.singletonList(this.primary);

    public BaritoneProvider() {
        new ExampleBaritoneControl(this.primary);
    }

    @Override
    public IBaritone getPrimaryBaritone() {
        return this.primary;
    }

    @Override
    public List<IBaritone> getAllBaritones() {
        return this.all;
    }

    @Override
    public IWorldScanner getWorldScanner() {
        return WorldScanner.INSTANCE;
    }

    @Override
    public ICommandSystem getCommandSystem() {
        return CommandSystem.INSTANCE;
    }

    @Override
    public ISchematicSystem getSchematicSystem() {
        return SchematicSystem.INSTANCE;
    }
}

