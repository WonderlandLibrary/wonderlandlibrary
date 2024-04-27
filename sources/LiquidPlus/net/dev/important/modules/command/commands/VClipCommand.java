/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/VClipCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class VClipCommand
extends Command {
    public VClipCommand() {
        boolean $i$f$emptyArray = false;
        super("vclip", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length > 1) {
            try {
                double y = Double.parseDouble(args2[1]);
                Entity entity = MinecraftInstance.mc.field_71439_g.func_70115_ae() ? MinecraftInstance.mc.field_71439_g.field_70154_o : (Entity)MinecraftInstance.mc.field_71439_g;
                entity.func_70107_b(entity.field_70165_t, entity.field_70163_u + y, entity.field_70161_v);
                this.chat("You were teleported.");
            }
            catch (NumberFormatException ex) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("vclip <value>");
    }
}

