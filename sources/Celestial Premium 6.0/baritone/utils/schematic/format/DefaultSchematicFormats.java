/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.schematic.format;

import baritone.api.schematic.IStaticSchematic;
import baritone.api.schematic.format.ISchematicFormat;
import baritone.utils.schematic.format.defaults.MCEditSchematic;
import baritone.utils.schematic.format.defaults.SpongeSchematic;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FilenameUtils;

public enum DefaultSchematicFormats implements ISchematicFormat
{
    MCEDIT("schematic"){

        @Override
        public IStaticSchematic parse(InputStream input) throws IOException {
            return new MCEditSchematic(CompressedStreamTools.readCompressed(input));
        }
    }
    ,
    SPONGE("schem"){

        @Override
        public IStaticSchematic parse(InputStream input) throws IOException {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(input);
            int version = nbt.getInteger("Version");
            switch (version) {
                case 1: 
                case 2: {
                    return new SpongeSchematic(nbt);
                }
            }
            throw new UnsupportedOperationException("Unsupported Version of a Sponge Schematic");
        }
    };

    private final String extension;

    private DefaultSchematicFormats(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean isFileType(File file) {
        return this.extension.equalsIgnoreCase(FilenameUtils.getExtension(file.getAbsolutePath()));
    }
}

