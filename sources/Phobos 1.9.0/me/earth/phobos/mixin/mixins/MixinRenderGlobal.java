package me.earth.phobos.mixin.mixins;

import me.earth.phobos.event.events.BlockBreakingEvent;
import me.earth.phobos.features.modules.movement.Speed;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderGlobal.class})
public abstract class MixinRenderGlobal {
    @Redirect(method={"setupTerrain"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
    public void initializeHook(ChunkRenderContainer chunkRenderContainer, double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        double y = viewEntityYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue().booleanValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        chunkRenderContainer.initialize(viewEntityXIn, y, viewEntityZIn);
    }

    @Redirect(method={"renderEntities"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
    public void setRenderPositionHook(RenderManager renderManager, double renderPosXIn, double renderPosYIn, double renderPosZIn) {
        double y = renderPosYIn;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue().booleanValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            y = Speed.getInstance().startY;
        }
        TileEntityRendererDispatcher.staticPlayerY = y;
        renderManager.setRenderPosition(renderPosXIn, y, renderPosZIn);
    }

    @Redirect(method={"drawSelectionBox"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
    public AxisAlignedBB offsetHook(AxisAlignedBB axisAlignedBB, double x, double y, double z) {
        double yIn = y;
        if (Speed.getInstance().isOn() && Speed.getInstance().noShake.getValue().booleanValue() && Speed.getInstance().mode.getValue() != Speed.Mode.INSTANT && Speed.getInstance().antiShake) {
            yIn = Speed.getInstance().startY;
        }
        return axisAlignedBB.offset(x, y, z);
    }

    @Inject(method={"sendBlockBreakProgress"}, at={@At(value="HEAD")})
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress, CallbackInfo ci) {
        BlockBreakingEvent event = new BlockBreakingEvent(pos, breakerId, progress);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}

