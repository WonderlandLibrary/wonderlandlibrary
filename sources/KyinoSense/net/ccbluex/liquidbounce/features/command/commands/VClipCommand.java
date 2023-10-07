/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/VClipCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class VClipCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 1) {
            try {
                Entity entity;
                String string = args2[1];
                boolean bl = false;
                double y = Double.parseDouble(string);
                EntityPlayerSP entityPlayerSP = VClipCommand.access$getMc$p$s1046033730().field_71439_g;
                if (entityPlayerSP == null) {
                    return;
                }
                EntityPlayerSP thePlayer = entityPlayerSP;
                if (thePlayer.func_70115_ae()) {
                    entity = thePlayer.field_70154_o;
                    if (entity == null) {
                        Intrinsics.throwNpe();
                    }
                } else {
                    entity = (Entity)thePlayer;
                }
                Entity entity2 = entity;
                entity2.func_70107_b(entity2.field_70165_t, entity2.field_70163_u + y, entity2.field_70161_v);
                this.chat("You were teleported.");
            }
            catch (NumberFormatException ex) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("vclip <value>");
    }

    public VClipCommand() {
        super("vclip", new String[0]);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

