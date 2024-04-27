package me.utils.render;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "invoke"})
final class BlurUtils$blurArea$1
extends Lambda
implements Function0<Unit> {
    final float $x;
    final float $y;
    final float $x2;
    final float $y2;

    @Override
    public final void invoke() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderUtils.quickDrawRect(this.$x, this.$y, this.$x2, this.$y2);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    BlurUtils$blurArea$1(float f, float f2, float f3, float f4) {
        this.$x = f;
        this.$y = f2;
        this.$x2 = f3;
        this.$y2 = f4;
        super(0);
    }
}
