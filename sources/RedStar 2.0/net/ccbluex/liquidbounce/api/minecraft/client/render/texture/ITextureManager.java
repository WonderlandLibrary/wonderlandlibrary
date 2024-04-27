package net.ccbluex.liquidbounce.api.minecraft.client.render.texture;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\bf\u000020J020H&J020H&J\b0\t2\n020\fH&¨\r"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "", "bindTexture", "", "image", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "bindTexture2", "Lnet/minecraft/util/ResourceLocation;", "loadTexture", "", "textureLocation", "textureObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/IAbstractTexture;", "Pride"})
public interface ITextureManager {
    public boolean loadTexture(@NotNull IResourceLocation var1, @NotNull IAbstractTexture var2);

    public void bindTexture(@NotNull IResourceLocation var1);

    public void bindTexture2(@NotNull ResourceLocation var1);
}
