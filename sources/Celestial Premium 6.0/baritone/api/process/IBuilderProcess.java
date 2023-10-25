/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import baritone.api.schematic.ISchematic;
import java.io.File;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public interface IBuilderProcess
extends IBaritoneProcess {
    public void build(String var1, ISchematic var2, Vec3i var3);

    public boolean build(String var1, File var2, Vec3i var3);

    default public boolean build(String schematicFile, BlockPos origin) {
        File file = new File(new File(Minecraft.getMinecraft().gameDir, "schematics"), schematicFile);
        return this.build(schematicFile, file, (Vec3i)origin);
    }

    public void buildOpenSchematic();

    public void pause();

    public boolean isPaused();

    public void resume();

    public void clearArea(BlockPos var1, BlockPos var2);

    public List<IBlockState> getApproxPlaceable();
}

