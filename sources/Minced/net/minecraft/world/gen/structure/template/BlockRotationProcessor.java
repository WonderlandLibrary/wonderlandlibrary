// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure.template;

import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import java.util.Random;

public class BlockRotationProcessor implements ITemplateProcessor
{
    private final float chance;
    private final Random random;
    
    public BlockRotationProcessor(final BlockPos pos, final PlacementSettings settings) {
        this.chance = settings.getIntegrity();
        this.random = settings.getRandom(pos);
    }
    
    @Nullable
    @Override
    public Template.BlockInfo processBlock(final World worldIn, final BlockPos pos, final Template.BlockInfo blockInfoIn) {
        return (this.chance < 1.0f && this.random.nextFloat() > this.chance) ? null : blockInfoIn;
    }
}
