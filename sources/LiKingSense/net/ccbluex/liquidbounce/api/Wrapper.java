/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.util.IWrappedUser;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/api/Wrapper;", "", "classProvider", "Lnet/ccbluex/liquidbounce/api/IClassProvider;", "getClassProvider", "()Lnet/ccbluex/liquidbounce/api/IClassProvider;", "functions", "Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "getFunctions", "()Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "microsoftUser", "Lnet/ccbluex/liquidbounce/api/util/IWrappedUser;", "getMicrosoftUser", "()Lnet/ccbluex/liquidbounce/api/util/IWrappedUser;", "minecraft", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "getMinecraft", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "LiKingSense"})
public interface Wrapper {
    @NotNull
    public IClassProvider getClassProvider();

    @NotNull
    public IMinecraft getMinecraft();

    @NotNull
    public IWrappedUser getMicrosoftUser();

    @NotNull
    public IExtractedFunctions getFunctions();
}

