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
    private final String[] alias;
    private final String command;

    public List tabComplete(String[] stringArray) {
        return CollectionsKt.emptyList();
    }

    public abstract void execute(@NotNull String[] var1);

    public final String getCommand() {
        return this.command;
    }

    protected final void chat(String string) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lAtField\u00a78] \u00a73" + string);
    }

    public final String[] getAlias() {
        return this.alias;
    }

    protected final void playEdit() {
        MinecraftInstance.mc.getSoundHandler().playSound("random.anvil_use", 1.0f);
    }

    public Command(String string, String ... stringArray) {
        this.command = string;
        this.alias = stringArray;
    }

    protected final void chatSyntax(String string) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lAtField\u00a78] \u00a73Syntax: \u00a77" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + string);
    }

    protected final void chatSyntaxError() {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lAtField\u00a78] \u00a73Syntax error");
    }

    protected final void chatSyntax(String[] stringArray) {
        ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lAtField\u00a78] \u00a73Syntax:");
        String[] stringArray2 = stringArray;
        int n = stringArray2.length;
        for (int i = 0; i < n; ++i) {
            String string;
            String string2 = string = stringArray2[i];
            StringBuilder stringBuilder = new StringBuilder().append("\u00a78> \u00a77").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(this.command).append(' ');
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.toLowerCase();
            ClientUtils.displayChatMessage(stringBuilder.append(string4).toString());
        }
    }
}

