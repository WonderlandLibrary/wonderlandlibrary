package net.ccbluex.liquidbounce.api.minecraft.client.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\u0000\bf\u000020J\b0H&R0X¦¢\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "", "worldRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IWorldRenderer;", "getWorldRenderer", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IWorldRenderer;", "draw", "", "Pride"})
public interface ITessellator {
    @NotNull
    public IWorldRenderer getWorldRenderer();

    public void draw();
}
