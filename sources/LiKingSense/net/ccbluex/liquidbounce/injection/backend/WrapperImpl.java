/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.Wrapper;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.util.IWrappedUser;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl;
import net.ccbluex.liquidbounce.injection.backend.ExtractedFunctionsImpl;
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u00108VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/WrapperImpl;", "Lnet/ccbluex/liquidbounce/api/Wrapper;", "()V", "classProvider", "Lnet/ccbluex/liquidbounce/api/IClassProvider;", "getClassProvider", "()Lnet/ccbluex/liquidbounce/api/IClassProvider;", "functions", "Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "getFunctions", "()Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "microsoftUser", "Lnet/ccbluex/liquidbounce/api/util/IWrappedUser;", "getMicrosoftUser", "()Lnet/ccbluex/liquidbounce/api/util/IWrappedUser;", "minecraft", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "getMinecraft", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "LiKingSense"})
public final class WrapperImpl
implements Wrapper {
    @NotNull
    private static final IClassProvider classProvider;
    @NotNull
    private static final IExtractedFunctions functions;
    public static final WrapperImpl INSTANCE;

    @Override
    @NotNull
    public IClassProvider getClassProvider() {
        return classProvider;
    }

    @Override
    @NotNull
    public IMinecraft getMinecraft() {
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"Minecraft.getMinecraft()");
        return new MinecraftImpl(minecraft);
    }

    @Override
    @NotNull
    public IWrappedUser getMicrosoftUser() {
        IWrappedUser iWrappedUser = IWrappedUser.INSTANCE;
        Intrinsics.checkExpressionValueIsNotNull((Object)iWrappedUser, (String)"IWrappedUser.INSTANCE");
        return iWrappedUser;
    }

    @Override
    @NotNull
    public IExtractedFunctions getFunctions() {
        return functions;
    }

    private WrapperImpl() {
    }

    static {
        WrapperImpl wrapperImpl;
        INSTANCE = wrapperImpl = new WrapperImpl();
        classProvider = ClassProviderImpl.INSTANCE;
        functions = ExtractedFunctionsImpl.INSTANCE;
    }
}

