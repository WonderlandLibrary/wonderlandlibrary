/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Comparator;
import java.util.List;
import joptsimple.internal.Strings;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class HelpCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        int n;
        int n2 = 1;
        if (stringArray.length > 1) {
            try {
                String string = stringArray[1];
                boolean bl = false;
                n2 = Integer.parseInt(string);
            }
            catch (NumberFormatException numberFormatException) {
                this.chatSyntaxError();
            }
        }
        if (n2 <= 0) {
            this.chat("The number you have entered is too low, it must be over 0");
            return;
        }
        double d = (double)LiquidBounce.INSTANCE.getCommandManager().getCommands().size() / 8.0;
        int n3 = n = d > (double)((int)d) ? (int)d + 1 : (int)d;
        if (n2 > n) {
            this.chat("The number you have entered is too big, it must be under " + n + '.');
            return;
        }
        this.chat("\u00a7c\u00a7lHelp");
        ClientUtils.displayChatMessage("\u00a77> Page: \u00a78" + n2 + " / " + n);
        String[] stringArray2 = (String[])LiquidBounce.INSTANCE.getCommandManager().getCommands();
        boolean bl = false;
        String[] stringArray3 = stringArray2;
        boolean bl2 = false;
        Comparator comparator = new Comparator(){

            public final int compare(Object object, Object object2) {
                boolean bl = false;
                Command command = (Command)object;
                boolean bl2 = false;
                Comparable comparable = (Comparable)((Object)command.getCommand());
                command = (Command)object2;
                Comparable comparable2 = comparable;
                bl2 = false;
                String string = command.getCommand();
                return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)((Comparable)((Object)string)));
            }

            static {
            }
        };
        List list = CollectionsKt.sortedWith((Iterable)stringArray3, (Comparator)comparator);
        for (int i = 8 * (n2 - 1); i < 8 * n2 && i < list.size(); ++i) {
            Command command = (Command)list.get(i);
            stringArray3 = command.getAlias();
            StringBuilder stringBuilder = new StringBuilder().append("\u00a76> \u00a77").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(command.getCommand());
            bl2 = false;
            boolean bl3 = stringArray3.length == 0;
            ClientUtils.displayChatMessage(stringBuilder.append(bl3 ? "" : " \u00a77(\u00a78" + Strings.join((String[])command.getAlias(), (String)"\u00a77, \u00a78") + "\u00a77)").toString());
        }
        ClientUtils.displayChatMessage("\u00a7a------------\n\u00a77> \u00a7c" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "help \u00a78<\u00a77\u00a7lpage\u00a78>");
    }

    public HelpCommand() {
        super("help", new String[0]);
    }
}

