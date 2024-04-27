package me.utils;

import java.util.WeakHashMap;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.features.module.modules.render.WolrdAnim;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class AnimationHandler
extends MinecraftInstance {
    private final WeakHashMap<RenderChunk, AnimationData> timeStamps = new WeakHashMap();

    public void preRender(RenderChunk renderChunk) {
        if (this.timeStamps.containsKey(renderChunk)) {
            int animationDuration;
            long timeDif;
            AnimationData animationData = this.timeStamps.get(renderChunk);
            long time = animationData.timeStamp;
            if (time == -1L) {
                animationData.timeStamp = time = System.currentTimeMillis();
            }
            if ((timeDif = System.currentTimeMillis() - time) < (long)(animationDuration = ((Integer)WolrdAnim.chunkAnimationDurationValue.get()).intValue())) {
                double chunkY = renderChunk.getPosition().func_177956_o();
                int chunkPos = (Integer)WolrdAnim.chunkPositionValue.get();
                switch ((String)WolrdAnim.animmode.get()) {
                    case "UP": {
                        GlStateManager.translate((double)0.0, (double)((double)(-chunkPos) - chunkY - ((double)(-chunkPos) - chunkY) / (double)animationDuration * (double)timeDif), (double)0.0);
                        break;
                    }
                    case "Down": {
                        GlStateManager.translate((double)0.0, (double)((double)chunkPos - chunkY - ((double)chunkPos - chunkY) / (double)animationDuration * (double)timeDif), (double)0.0);
                        break;
                    }
                    default: {
                        IEnumFacing chunkFacing = animationData.chunkFacing;
                        if (chunkFacing == null) break;
                        WVec3i vec = chunkFacing.getDirectionVec();
                        double animationPosition = -((double)chunkPos - (double)chunkPos / (double)animationDuration * (double)timeDif);
                        GlStateManager.translate((double)((double)vec.getX() * animationPosition), (double)0.0, (double)((double)vec.getZ() * animationPosition));
                        break;
                    }
                }
            } else {
                this.timeStamps.remove(renderChunk);
            }
        }
    }

    public void setPosition(RenderChunk renderChunk, WBlockPos position) {
        if (mc.getThePlayer() != null) {
            WBlockPos zeroedCenteredChunkPos;
            boolean flag;
            WBlockPos zeroedPlayerPosition = mc.getThePlayer().getPosition();
            boolean bl = flag = (zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0)).distanceSq(zeroedCenteredChunkPos = position.add(8, -position.getY(), 8)) > 4096.0;
            if (flag) {
                int difZ;
                WBlockPos dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                int difX = Math.abs(dif.getX());
                IEnumFacing chunkFacing = difX > (difZ = Math.abs(dif.getZ())) ? (dif.getX() > 0 ? classProvider.getEnumFacing(EnumFacingType.EAST) : classProvider.getEnumFacing(EnumFacingType.WEST)) : (dif.getZ() > 0 ? classProvider.getEnumFacing(EnumFacingType.SOUTH) : classProvider.getEnumFacing(EnumFacingType.NORTH));
                AnimationData animationData = new AnimationData(this, -1L, chunkFacing);
                this.timeStamps.put(renderChunk, animationData);
            }
        }
    }

    private static class AnimationData {
        public long timeStamp;
        public IEnumFacing chunkFacing;
        public AnimationHandler animationHandler;

        public AnimationData(AnimationHandler animationHandler, long timeStamp, IEnumFacing chunkFacing) {
            this.animationHandler = animationHandler;
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}
