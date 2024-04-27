package net.ccbluex.liquidbounce.api.minecraft.client.renderer;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000<\n\n\u0000\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\b\n\b\bf\u000020J\b0H&J\b\b0\tH&J\n020\fH&J\r020H&J02020H&J\b0H&J\b0H&R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "", "shaderGroup", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IShaderGroup;", "getShaderGroup", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IShaderGroup;", "disableLightmap", "", "isShaderActive", "", "loadShader", "resourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "loadShader2", "Lnet/minecraft/util/ResourceLocation;", "setupCameraTransform", "partialTicks", "", "i", "", "setupOverlayRendering", "stopUseShader", "Pride"})
public interface IEntityRenderer {
    @Nullable
    public IShaderGroup getShaderGroup();

    public void disableLightmap();

    public boolean isShaderActive();

    public void loadShader(@NotNull IResourceLocation var1);

    public void loadShader2(@NotNull ResourceLocation var1);

    public void stopUseShader();

    public void setupCameraTransform(float var1, int var2);

    public void setupOverlayRendering();
}
