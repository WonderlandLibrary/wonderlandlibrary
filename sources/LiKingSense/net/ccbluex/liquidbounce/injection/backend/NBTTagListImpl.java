/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\rH\u0016\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/NBTTagListImpl;", "Lnet/ccbluex/liquidbounce/injection/backend/NBTBaseImpl;", "Lnet/minecraft/nbt/NBTTagList;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "wrapped", "(Lnet/minecraft/nbt/NBTTagList;)V", "appendTag", "", "createNBTTagString", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "getCompoundTagAt", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "index", "", "hasNoTags", "", "tagCount", "LiKingSense"})
public final class NBTTagListImpl
extends NBTBaseImpl<NBTTagList>
implements INBTTagList {
    @Override
    public boolean hasNoTags() {
        return ((NBTTagList)this.getWrapped()).func_82582_d();
    }

    @Override
    public int tagCount() {
        return ((NBTTagList)this.getWrapped()).func_74745_c();
    }

    @Override
    @NotNull
    public INBTTagCompound getCompoundTagAt(int index) {
        NBTTagCompound nBTTagCompound = ((NBTTagList)this.getWrapped()).func_150305_b(index);
        Intrinsics.checkExpressionValueIsNotNull((Object)nBTTagCompound, (String)"wrapped.getCompoundTagAt(index)");
        NBTTagCompound $this$wrap$iv = nBTTagCompound;
        boolean $i$f$wrap = false;
        return new NBTTagCompoundImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void appendTag(@NotNull INBTBase createNBTTagString) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)createNBTTagString, (String)"createNBTTagString");
        INBTBase iNBTBase = createNBTTagString;
        NBTTagList nBTTagList = (NBTTagList)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t2 = ((NBTBaseImpl)$this$unwrap$iv).getWrapped();
        nBTTagList.func_74742_a(t2);
    }

    public NBTTagListImpl(@NotNull NBTTagList wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        super((NBTBase)wrapped);
    }
}

