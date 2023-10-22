package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventRenderBlock;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockRendererDispatcher.class})
public class MixinBlockRendererDispatcher {
   @Inject(
      method = {"renderBlock"},
      at = {@At("HEAD")}
   )
   public void eventUpdate(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn, CallbackInfoReturnable info) {
      EventRenderBlock event = new EventRenderBlock(pos.getX(), pos.getY(), pos.getZ(), state.getBlock(), pos);
      EventManager.call(event);
   }
}
