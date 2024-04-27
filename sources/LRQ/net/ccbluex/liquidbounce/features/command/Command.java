/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command;

import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

public abstract class Command
extends MinecraftInstance {
    private final String command;
    private final String[] alias;

    public abstract void execute(@NotNull String[] var1);

    public List<String> tabComplete(String[] args) {
        return CollectionsKt.emptyList();
    }

    protected final void chat(String msg) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLRQ\u00a78] \u00a73" + msg);
    }

    protected final void chatSyntax(String syntax) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLRQ\u00a78] \u00a73Syntax: \u00a77" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + syntax);
    }

    protected final void chatSyntax(String[] syntaxes) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLRQ\u00a78] \u00a73Syntax:");
        String[] stringArray = syntaxes;
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            String syntax;
            String string = syntax = stringArray[i];
            StringBuilder stringBuilder = new StringBuilder().append("\u00a78> \u00a77").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(this.command).append(' ');
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            ClientUtils.displayChatMessage(stringBuilder.append(string3).toString());
        }
    }

    protected final void chatSyntaxError() {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLRQ\u00a78] \u00a73Syntax error");
    }

    protected final void playEdit() {
        MinecraftInstance.mc.getSoundHandler().playSound("random.anvil_use", 1.0f);
    }

    public final String getCommand() {
        return this.command;
    }

    public final String[] getAlias() {
        return this.alias;
    }

    public Command(String command, String ... alias) {
        this.command = command;
        this.alias = alias;
    }
}

