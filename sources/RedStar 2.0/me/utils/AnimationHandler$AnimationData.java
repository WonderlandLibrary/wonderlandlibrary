package me.utils;

import me.utils.AnimationHandler;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;

class AnimationHandler$AnimationData {
    public long timeStamp;
    public IEnumFacing chunkFacing;
    public AnimationHandler animationHandler;

    public AnimationHandler$AnimationData(AnimationHandler animationHandler, long timeStamp, IEnumFacing chunkFacing) {
        this.animationHandler = animationHandler;
        this.timeStamp = timeStamp;
        this.chunkFacing = chunkFacing;
    }
}
