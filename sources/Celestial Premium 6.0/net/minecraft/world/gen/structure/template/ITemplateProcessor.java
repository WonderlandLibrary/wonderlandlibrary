/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure.template;

import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

public interface ITemplateProcessor {
    @Nullable
    public Template.BlockInfo processBlock(World var1, BlockPos var2, Template.BlockInfo var3);
}

