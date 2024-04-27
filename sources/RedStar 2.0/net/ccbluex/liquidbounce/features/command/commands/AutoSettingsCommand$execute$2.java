package net.ccbluex.liquidbounce.features.command.commands;

import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n \n\n\u0000\u000002\f\b00H\n¢\b"}, d2={"<anonymous>", "", "it", "", "", "invoke"})
final class AutoSettingsCommand$execute$2
extends Lambda
implements Function1<List<? extends String>, Unit> {
    final AutoSettingsCommand this$0;

    @Override
    public final void invoke(@NotNull List<String> it) {
        Intrinsics.checkParameterIsNotNull(it, "it");
        for (String setting : it) {
            this.this$0.chat("> " + setting);
        }
    }

    AutoSettingsCommand$execute$2(AutoSettingsCommand autoSettingsCommand) {
        this.this$0 = autoSettingsCommand;
        super(1);
    }
}
