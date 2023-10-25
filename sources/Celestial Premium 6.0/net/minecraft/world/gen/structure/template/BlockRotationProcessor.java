/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockRotationProcessor
implements ITemplateProcessor {
    private final float chance;
    private final Random random;

    public BlockRotationProcessor(BlockPos pos, PlacementSettings settings) {
        this.chance = settings.getIntegrity();
        this.random = settings.getRandom(pos);
    }

    @Override
    @Nullable
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        return this.chance < 1.0f && this.random.nextFloat() > this.chance ? null : blockInfoIn;
    }
}

