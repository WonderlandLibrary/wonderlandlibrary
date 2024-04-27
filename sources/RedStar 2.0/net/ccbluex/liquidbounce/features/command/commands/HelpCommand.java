package net.ccbluex.liquidbounce.features.command.commands;

import java.util.Comparator;
import java.util.List;
import joptsimple.internal.Strings;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/HelpCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class HelpCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        int maxPage;
        Intrinsics.checkParameterIsNotNull(args, "args");
        int page = 1;
        if (args.length > 1) {
            try {
                String string = args[1];
                boolean bl = false;
                page = Integer.parseInt(string);
            }
            catch (NumberFormatException e) {
                this.chatSyntaxError();
            }
        }
        if (page <= 0) {
            this.chat("The number you have entered is too low, it must be over 0");
            return;
        }
        double maxPageDouble = (double)LiquidBounce.INSTANCE.getCommandManager().getCommands().size() / 8.0;
        int n = maxPage = maxPageDouble > (double)((int)maxPageDouble) ? (int)maxPageDouble + 1 : (int)maxPageDouble;
        if (page > maxPage) {
            this.chat("The number you have entered is too big, it must be under " + maxPage + '.');
            return;
        }
        this.chat("§c§lHelp");
        ClientUtils.displayChatMessage("§7> Page: §8" + page + " / " + maxPage);
        String[] $this$sortedBy$iv = (String[])LiquidBounce.INSTANCE.getCommandManager().getCommands();
        boolean $i$f$sortedBy = false;
        String[] stringArray = $this$sortedBy$iv;
        boolean bl = false;
        Comparator comparator = new Comparator<T>(){

            public final int compare(T a, T b) {
                boolean bl = false;
                Command it = (Command)a;
                boolean bl2 = false;
                Comparable comparable = (Comparable)((Object)it.getCommand());
                it = (Command)b;
                Comparable comparable2 = comparable;
                bl2 = false;
                String string = it.getCommand();
                return ComparisonsKt.compareValues(comparable2, (Comparable)((Object)string));
            }
        };
        List commands = CollectionsKt.sortedWith(stringArray, comparator);
        for (int i = 8 * (page - 1); i < 8 * page && i < commands.size(); ++i) {
            Command command = (Command)commands.get(i);
            stringArray = command.getAlias();
            StringBuilder stringBuilder = new StringBuilder().append("§6> §7").append(LiquidBounce.INSTANCE.getCommandManager().getPrefix()).append(command.getCommand());
            bl = false;
            boolean bl2 = stringArray.length == 0;
            ClientUtils.displayChatMessage(stringBuilder.append(bl2 ? "" : " §7(§8" + Strings.join((String[])command.getAlias(), (String)"§7, §8") + "§7)").toString());
        }
        ClientUtils.displayChatMessage("§a------------\n§7> §c" + LiquidBounce.INSTANCE.getCommandManager().getPrefix() + "help §8<§7§lpage§8>");
    }

    public HelpCommand() {
        super("help", new String[0]);
    }
}
