/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.nbt.JsonToNBT
 *  net.minecraft.nbt.NBTTagCompound
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.nbt.IJsonToNBT;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/JsonToNBTImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "()V", "getTagFromJson", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "s", "", "LiKingSense"})
public final class JsonToNBTImpl
implements IJsonToNBT {
    public static final JsonToNBTImpl INSTANCE;

    @Override
    @NotNull
    public INBTTagCompound getTagFromJson(@NotNull String s) {
        Intrinsics.checkParameterIsNotNull((Object)s, (String)"s");
        NBTTagCompound nBTTagCompound = JsonToNBT.func_180713_a((String)s);
        Intrinsics.checkExpressionValueIsNotNull((Object)nBTTagCompound, (String)"JsonToNBT.getTagFromJson(s)");
        NBTTagCompound $this$wrap$iv = nBTTagCompound;
        boolean $i$f$wrap = false;
        return new NBTTagCompoundImpl($this$wrap$iv);
    }

    private JsonToNBTImpl() {
    }

    static {
        JsonToNBTImpl jsonToNBTImpl;
        INSTANCE = jsonToNBTImpl = new JsonToNBTImpl();
    }
}

