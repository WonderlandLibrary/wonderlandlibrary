package net.ccbluex.liquidbounce.api.minecraft.client.render;

import java.nio.ByteBuffer;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\u0000\n\b\n\b\n\n\b\n\n\b\bf\u000020J\n02\f0\r20H&J(0\u000020202020H&J\b0H&J\b0H&J 0\u0000202020H&J\b0H&J0\u00002020H&R0X¦¢\bR0X¦¢\b\b\t¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IWorldRenderer;", "", "byteBuffer", "Ljava/nio/ByteBuffer;", "getByteBuffer", "()Ljava/nio/ByteBuffer;", "vertexFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "getVertexFormat", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "begin", "", "mode", "", "color", "red", "", "green", "blue", "alpha", "endVertex", "finishDrawing", "pos", "x", "", "y", "z", "reset", "tex", "u", "v", "Pride"})
public interface IWorldRenderer {
    @NotNull
    public ByteBuffer getByteBuffer();

    @NotNull
    public IVertexFormat getVertexFormat();

    public void begin(int var1, @NotNull IVertexFormat var2);

    @NotNull
    public IWorldRenderer pos(double var1, double var3, double var5);

    public void endVertex();

    @NotNull
    public IWorldRenderer tex(double var1, double var3);

    @NotNull
    public IWorldRenderer color(float var1, float var2, float var3, float var4);

    public void finishDrawing();

    public void reset();
}
