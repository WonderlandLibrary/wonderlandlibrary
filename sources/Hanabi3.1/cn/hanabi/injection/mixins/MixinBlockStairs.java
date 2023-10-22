package cn.hanabi.injection.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({BlockStairs.class})
public class MixinBlockStairs {
   @Final
   @Shadow
   private Block modelBlock;
}
