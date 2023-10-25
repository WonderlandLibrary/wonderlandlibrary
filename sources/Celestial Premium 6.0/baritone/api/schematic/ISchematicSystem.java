/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.command.registry.Registry;
import baritone.api.schematic.format.ISchematicFormat;
import java.io.File;
import java.util.Optional;

public interface ISchematicSystem {
    public Registry<ISchematicFormat> getRegistry();

    public Optional<ISchematicFormat> getByFile(File var1);
}

