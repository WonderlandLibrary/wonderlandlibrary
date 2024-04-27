package me.aquavit.liquidsense.injection.forge.mixins.block;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.RenderBlockEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
@SideOnly(Side.CLIENT)
public class MixinBlockRendererDispatcher {

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    public void renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn, final CallbackInfoReturnable<Boolean> info) {
        RenderBlockEvent event = new RenderBlockEvent(pos.getX(), pos.getY(), pos.getZ(), state.getBlock(), pos);
        LiquidSense.eventManager.callEvent(event);
    }

}
