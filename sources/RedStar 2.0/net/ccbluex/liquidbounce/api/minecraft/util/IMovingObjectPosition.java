package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\bf\u000020:R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\rR0X¦¢\bR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getBlockPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "entityHit", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getEntityHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "hitVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getHitVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "sideHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getSideHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "typeOfHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "getTypeOfHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "WMovingObjectType", "Pride"})
public interface IMovingObjectPosition {
    @Nullable
    public IEntity getEntityHit();

    @Nullable
    public WBlockPos getBlockPos();

    @Nullable
    public IEnumFacing getSideHit();

    @NotNull
    public WVec3 getHitVec();

    @NotNull
    public WMovingObjectType getTypeOfHit();

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "", "(Ljava/lang/String;I)V", "MISS", "ENTITY", "BLOCK", "Pride"})
    public static final class WMovingObjectType
    extends Enum<WMovingObjectType> {
        public static final WMovingObjectType MISS;
        public static final WMovingObjectType ENTITY;
        public static final WMovingObjectType BLOCK;
        private static final WMovingObjectType[] $VALUES;

        static {
            WMovingObjectType[] wMovingObjectTypeArray = new WMovingObjectType[3];
            WMovingObjectType[] wMovingObjectTypeArray2 = wMovingObjectTypeArray;
            wMovingObjectTypeArray[0] = MISS = new WMovingObjectType();
            wMovingObjectTypeArray[1] = ENTITY = new WMovingObjectType();
            wMovingObjectTypeArray[2] = BLOCK = new WMovingObjectType();
            $VALUES = wMovingObjectTypeArray;
        }

        public static WMovingObjectType[] values() {
            return (WMovingObjectType[])$VALUES.clone();
        }

        public static WMovingObjectType valueOf(String string) {
            return Enum.valueOf(WMovingObjectType.class, string);
        }
    }
}
