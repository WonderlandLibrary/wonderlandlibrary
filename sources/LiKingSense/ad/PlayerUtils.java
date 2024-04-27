/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.util.math.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package ad;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010\u00a8\u0006\u0012"}, d2={"Lad/PlayerUtils;", "", "()V", "getAr", "", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getBlockRelativeToPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "offsetX", "offsetY", "offsetZ", "isBlockUnder", "", "isUsingFood", "randomUnicode", "", "str", "LiKingSense"})
public final class PlayerUtils {
    public static final PlayerUtils INSTANCE;

    /*
     * Exception decompiling
     */
    @NotNull
    public final String randomUnicode(@NotNull String str) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl38 : IF_ICMPLE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final boolean isUsingFood() {
        IItem usingItem = MinecraftInstance.mc.getThePlayer().getItemInUse().getItem();
        return MinecraftInstance.mc.getThePlayer().getItemInUse() != null ? MinecraftInstance.mc.getThePlayer().isUsingItem() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) : false;
    }

    public final boolean isBlockUnder() {
        if (MinecraftInstance.mc.getThePlayer().getPosY() < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)MinecraftInstance.mc.getThePlayer().getPosY() + 2; off += 2) {
            IAxisAlignedBB bb = MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(0.0, -((double)off), 0.0);
            Collection<IAxisAlignedBB> collection = MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), bb);
            boolean bl = false;
            if (!(!collection.isEmpty())) continue;
            return true;
        }
        return false;
    }

    public final double getAr(@NotNull IEntityLivingBase player) {
        Intrinsics.checkParameterIsNotNull((Object)player, (String)"player");
        double arPercentage = (float)player.getTotalArmorValue() / player.getMaxHealth();
        arPercentage = MathHelper.func_151237_a((double)arPercentage, (double)0.0, (double)1.0);
        return (double)100 * arPercentage;
    }

    @Nullable
    public final IBlock getBlockRelativeToPlayer(double offsetX, double offsetY, double offsetZ) {
        return MinecraftInstance.mc.getTheWorld().getBlockState(new WBlockPos(MinecraftInstance.mc.getThePlayer().getPosX() + offsetX, MinecraftInstance.mc.getThePlayer().getPosY() + offsetY, MinecraftInstance.mc.getThePlayer().getPosZ() + offsetZ)).getBlock();
    }

    private PlayerUtils() {
    }

    static {
        PlayerUtils playerUtils;
        INSTANCE = playerUtils = new PlayerUtils();
    }
}

