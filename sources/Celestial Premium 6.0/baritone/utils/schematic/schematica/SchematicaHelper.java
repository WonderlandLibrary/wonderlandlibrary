/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.schematic.schematica;

import baritone.api.schematic.IStaticSchematic;
import baritone.utils.schematic.schematica.SchematicAdapter;
import com.github.lunatrius.core.util.math.MBlockPos;
import com.github.lunatrius.schematica.Schematica;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import com.github.lunatrius.schematica.proxy.ClientProxy;
import java.util.Optional;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

public enum SchematicaHelper {


    public static boolean isSchematicaPresent() {
        try {
            Class.forName(Schematica.class.getName());
            return true;
        }
        catch (ClassNotFoundException | NoClassDefFoundError ex) {
            return false;
        }
    }

    public static Optional<Tuple<IStaticSchematic, BlockPos>> getOpenSchematic() {
        return Optional.ofNullable(ClientProxy.schematic).map(world -> new Tuple<SchematicAdapter, MBlockPos>(new SchematicAdapter((SchematicWorld)world), world.position));
    }
}

