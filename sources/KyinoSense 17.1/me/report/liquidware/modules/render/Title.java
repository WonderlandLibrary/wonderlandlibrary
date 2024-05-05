/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.Display
 */
package me.report.liquidware.modules.render;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import me.report.liquidware.utils.ui.FuckerNMSL;
import me.report.liquidware.utils.ui.utils.EmptyInputBox;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

@ModuleInfo(name="Title", description="Title", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lme/report/liquidware/modules/render/Title;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "CopywritingValue", "Lnet/ccbluex/liquidbounce/value/TextValue;", "H", "", "HM", "M", "S", "fakeNameValue", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Title
extends Module {
    private final TextValue fakeNameValue = new TextValue("SetTitle", "KyinoSense");
    private final TextValue CopywritingValue = new TextValue("Copywriting", "Report1337");
    private int S;
    private int HM;
    private int M;
    private int H;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        long durationInMillis = System.currentTimeMillis() - LiquidBounce.INSTANCE.getPlayTimeStart();
        long second = durationInMillis / (long)1000 % (long)60;
        long minute = durationInMillis / (long)60000 % (long)60;
        long hour = durationInMillis / (long)3600000 % (long)24;
        String time = null;
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = "%02dh %02dm %02ds";
        Object[] objectArray = new Object[]{hour, minute, second};
        boolean bl = false;
        String string2 = String.format(string, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkExpressionValueIsNotNull(string2, "java.lang.String.format(format, *args)");
        time = string2;
        ++this.HM;
        if (this.HM == 120) {
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
        StringBuilder stringBuilder = new StringBuilder().append((String)this.fakeNameValue.get()).append(" | ").append((String)this.CopywritingValue.get()).append(" ").append(time).append(" Username: ");
        EmptyInputBox emptyInputBox = FuckerNMSL.username;
        Intrinsics.checkExpressionValueIsNotNull((Object)emptyInputBox, "FuckerNMSL.username");
        Display.setTitle((String)stringBuilder.append(emptyInputBox.getText()).toString());
    }
}

