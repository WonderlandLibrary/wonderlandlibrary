package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

@ModuleInfo(name="Title", description="Title", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\b\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J0\f2\r0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Title;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "CopywritingValue", "Lnet/ccbluex/liquidbounce/value/TextValue;", "H", "", "HM", "M", "S", "fakeNameValue", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Title
extends Module {
    private final TextValue fakeNameValue = new TextValue("SetTitle", "𝓟𝓻𝓲𝓭𝓮");
    private final TextValue CopywritingValue = new TextValue("Copywriting", "𝓒𝓪𝓷 𝓒𝓾𝓼𝓽𝓸𝓶");
    private int S;
    private int HM;
    private int M;
    private int H;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        ++this.HM;
        if (this.HM == 20) {
            ++this.S;
            this.HM = 0;
        }
        if (this.S == 60) {
            ++this.M;
            this.S = 0;
        }
        if (this.M == 60) {
            ++this.H;
            this.M = 0;
        }
        Display.setTitle((String)((String)this.fakeNameValue.get() + " |  " + (String)this.CopywritingValue.get() + " | 你已游玩" + this.H + "  时  " + this.M + "  分  " + this.S + "  秒  "));
    }
}
