package net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\n\b\n\b\n\b\bf\u000020J\b0H&J020H&J\b0H&J\b02\t0\nH&J\b0H&¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/vertex/IVertexBuffer;", "", "bindBuffer", "", "bufferData", "buffer", "Ljava/nio/ByteBuffer;", "deleteGlBuffers", "drawArrays", "mode", "", "unbindBuffer", "Pride"})
public interface IVertexBuffer {
    public void deleteGlBuffers();

    public void bindBuffer();

    public void drawArrays(int var1);

    public void unbindBuffer();

    public void bufferData(@NotNull ByteBuffer var1);
}
