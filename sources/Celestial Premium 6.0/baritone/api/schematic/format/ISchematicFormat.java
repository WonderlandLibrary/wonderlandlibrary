/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic.format;

import baritone.api.schematic.IStaticSchematic;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ISchematicFormat {
    public IStaticSchematic parse(InputStream var1) throws IOException;

    public boolean isFileType(File var1);
}

