/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/UsernameCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class UsernameCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        EntityPlayerSP entityPlayerSP = UsernameCommand.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        String username = entityPlayerSP.func_70005_c_();
        this.chat("Username: " + username);
        StringSelection stringSelection = new StringSelection(username);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Intrinsics.checkExpressionValueIsNotNull(toolkit, "Toolkit.getDefaultToolkit()");
        toolkit.getSystemClipboard().setContents(stringSelection, stringSelection);
    }

    public UsernameCommand() {
        super("username", "ign");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

