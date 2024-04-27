/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.command.commands.AutoDisableCommand;
import net.dev.important.modules.command.commands.BindCommand;
import net.dev.important.modules.command.commands.BindsCommand;
import net.dev.important.modules.command.commands.FriendCommand;
import net.dev.important.modules.command.commands.HClipCommand;
import net.dev.important.modules.command.commands.HelpCommand;
import net.dev.important.modules.command.commands.HideCommand;
import net.dev.important.modules.command.commands.LocalAutoSettingsCommand;
import net.dev.important.modules.command.commands.LoginCommand;
import net.dev.important.modules.command.commands.PanicCommand;
import net.dev.important.modules.command.commands.PathfindingTeleportCommand;
import net.dev.important.modules.command.commands.PingCommand;
import net.dev.important.modules.command.commands.PrefixCommand;
import net.dev.important.modules.command.commands.ReloadCommand;
import net.dev.important.modules.command.commands.SayCommand;
import net.dev.important.modules.command.commands.ScriptManagerCommand;
import net.dev.important.modules.command.commands.ServerInfoCommand;
import net.dev.important.modules.command.commands.TacoCommand;
import net.dev.important.modules.command.commands.TargetCommand;
import net.dev.important.modules.command.commands.TeleportCommand;
import net.dev.important.modules.command.commands.ThemeCommand;
import net.dev.important.modules.command.commands.ToggleCommand;
import net.dev.important.modules.command.commands.VClipCommand;
import net.dev.important.utils.ClientUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\nJ\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001c\u001a\u00020\nJ\u001d\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\u0018\u001a\u00020\nH\u0002\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u0005J\u0006\u0010!\u001a\u00020\u001aJ\u0010\u0010\"\u001a\u00020\u00172\b\u0010 \u001a\u0004\u0018\u00010\u0005R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\"\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u000f\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006#"}, d2={"Lnet/dev/important/modules/command/CommandManager;", "", "()V", "commands", "", "Lnet/dev/important/modules/command/Command;", "getCommands", "()Ljava/util/List;", "latestAutoComplete", "", "", "getLatestAutoComplete", "()[Ljava/lang/String;", "setLatestAutoComplete", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "prefix", "", "getPrefix", "()C", "setPrefix", "(C)V", "autoComplete", "", "input", "executeCommands", "", "getCommand", "name", "getCompletions", "(Ljava/lang/String;)[Ljava/lang/String;", "registerCommand", "command", "registerCommands", "unregisterCommand", "LiquidBounce"})
public final class CommandManager {
    @NotNull
    private final List<Command> commands = new ArrayList();
    @NotNull
    private String[] latestAutoComplete;
    private char prefix;

    public CommandManager() {
        boolean $i$f$emptyArray = false;
        this.latestAutoComplete = new String[0];
        this.prefix = (char)46;
    }

    @NotNull
    public final List<Command> getCommands() {
        return this.commands;
    }

    @NotNull
    public final String[] getLatestAutoComplete() {
        return this.latestAutoComplete;
    }

    public final void setLatestAutoComplete(@NotNull String[] stringArray) {
        Intrinsics.checkNotNullParameter(stringArray, "<set-?>");
        this.latestAutoComplete = stringArray;
    }

    public final char getPrefix() {
        return this.prefix;
    }

    public final void setPrefix(char c) {
        this.prefix = c;
    }

    public final void registerCommands() {
        this.registerCommand(new BindCommand());
        this.registerCommand(new VClipCommand());
        this.registerCommand(new HClipCommand());
        this.registerCommand(new HelpCommand());
        this.registerCommand(new SayCommand());
        this.registerCommand(new FriendCommand());
        this.registerCommand(new LocalAutoSettingsCommand());
        this.registerCommand(new ServerInfoCommand());
        this.registerCommand(new ToggleCommand());
        this.registerCommand(new TargetCommand());
        this.registerCommand(new TacoCommand());
        this.registerCommand(new BindsCommand());
        this.registerCommand(new PanicCommand());
        this.registerCommand(new PingCommand());
        this.registerCommand(new ReloadCommand());
        this.registerCommand(new LoginCommand());
        this.registerCommand(new ScriptManagerCommand());
        this.registerCommand(new PrefixCommand());
        this.registerCommand(new HideCommand());
        this.registerCommand(new AutoDisableCommand());
        this.registerCommand(new TeleportCommand());
        this.registerCommand(new PathfindingTeleportCommand());
        this.registerCommand(new ThemeCommand());
    }

    public final void executeCommands(@NotNull String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        for (Command command : this.commands) {
            String[] args2;
            String[] stringArray = new String[]{" "};
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)input, stringArray, false, 0, 6, null);
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            }
            if (StringsKt.equals(args2[0], this.prefix + command.getCommand(), true)) {
                command.execute(args2);
                return;
            }
            for (String alias : command.getAlias()) {
                if (!StringsKt.equals(args2[0], this.prefix + alias, true)) continue;
                command.execute(args2);
                return;
            }
        }
        ClientUtils.displayChatMessage("\u00a7cCommand not found. Type " + this.prefix + "help to view all commands.");
    }

    public final boolean autoComplete(@NotNull String input) {
        String[] stringArray;
        Intrinsics.checkNotNullParameter(input, "input");
        String[] stringArray2 = this.getCompletions(input);
        if (stringArray2 == null) {
            boolean $i$f$emptyArray = false;
            stringArray = new String[]{};
        } else {
            stringArray = this.latestAutoComplete = stringArray2;
        }
        return StringsKt.startsWith$default((CharSequence)input, this.prefix, false, 2, null) && !(this.latestAutoComplete.length == 0);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private final String[] getCompletions(String input) {
        block17: {
            block19: {
                block18: {
                    if (!(((CharSequence)input).length() > 0)) break block17;
                    var3_2 /* !! */  = input.toCharArray();
                    Intrinsics.checkNotNullExpressionValue(var3_2 /* !! */ , "this as java.lang.String).toCharArray()");
                    if (var3_2 /* !! */ [0] != this.prefix) break block17;
                    var3_2 /* !! */  = (char[])new String[1];
                    var3_2 /* !! */ [0] = (char)" ";
                    args = StringsKt.split$default((CharSequence)input, (String[])var3_2 /* !! */ , false, 0, 6, null);
                    if (args.size() <= 1) break block18;
                    var6_4 = ((String)args.get(0)).substring(1);
                    Intrinsics.checkNotNullExpressionValue(var6_4, "this as java.lang.String).substring(startIndex)");
                    v0 = command = this.getCommand(var6_4);
                    if (v0 == null) {
                        v1 = null;
                    } else {
                        $this$toTypedArray$iv = CollectionsKt.drop(args, 1);
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v2 = thisCollection$iv.toArray(new String[0]);
                        if (v2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                        }
                        v1 = v0.tabComplete(v2);
                    }
                    v3 = tabCompletions = v1;
                    if (v3 == null) {
                        v4 = null;
                    } else {
                        $this$toTypedArray$iv = v3;
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v5 = thisCollection$iv.toArray(new String[0]);
                        if (v5 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                        }
                        v4 = v5;
                    }
                    break block19;
                }
                $this$toTypedArray$iv = input.substring(1);
                Intrinsics.checkNotNullExpressionValue($this$toTypedArray$iv, "this as java.lang.String).substring(startIndex)");
                rawInput = $this$toTypedArray$iv;
                tabCompletions = this.commands;
                $i$f$filter = false;
                $this$toTypedArray$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    block15: {
                        it = (Command)element$iv$iv;
                        $i$a$-filter-CommandManager$getCompletions$1 = false;
                        if (StringsKt.startsWith(it.getCommand(), rawInput, true)) ** GOTO lbl-1000
                        $this$any$iv = it.getAlias();
                        $i$f$any = false;
                        for (String element$iv : $this$any$iv) {
                            alias = element$iv;
                            $i$a$-any-CommandManager$getCompletions$1$1 = false;
                            if (!StringsKt.startsWith(alias, rawInput, true)) continue;
                            v6 = true;
                            break block15;
                        }
                        v6 = false;
                    }
                    if (v6) lbl-1000:
                    // 2 sources

                    {
                        v7 = true;
                    } else {
                        v7 = false;
                    }
                    if (!v7) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                $this$filter$iv = (List)destination$iv$iv;
                $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                $i$f$mapTo = false;
                for (T item$iv$iv : $this$mapTo$iv$iv) {
                    block16: {
                        it = (Command)item$iv$iv;
                        var22_28 = destination$iv$iv;
                        $i$a$-map-CommandManager$getCompletions$2 = false;
                        if (StringsKt.startsWith(it.getCommand(), rawInput, true)) {
                            v8 = it.getCommand();
                        } else {
                            $this$first$iv = it.getAlias();
                            $i$f$first = false;
                            for (String element$iv : $this$first$iv) {
                                alias = element$iv;
                                $i$a$-first-CommandManager$getCompletions$2$alias$1 = false;
                                if (!StringsKt.startsWith(alias, rawInput, true)) continue;
                                v8 = element$iv;
                                break block16;
                            }
                            throw new NoSuchElementException("Array contains no element matching the predicate.");
                        }
                    }
                    alias = v8;
                    var13_19 = this.getPrefix();
                    var22_28.add(var13_19 + alias);
                }
                $this$map$iv = (List)destination$iv$iv;
                $i$f$toTypedArray = false;
                thisCollection$iv = $this$toTypedArray$iv;
                v9 = thisCollection$iv.toArray(new String[0]);
                if (v9 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                }
                v4 = v9;
            }
            return v4;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Nullable
    public final Command getCommand(@NotNull String name) {
        Object t;
        Object v0;
        boolean bl;
        Intrinsics.checkNotNullParameter(name, "name");
        Iterator iterator2 = ((Iterable)this.commands).iterator();
        do {
            block8: {
                block7: {
                    boolean bl2;
                    block6: {
                        if (!iterator2.hasNext()) {
                            v0 = null;
                            return v0;
                        }
                        t = iterator2.next();
                        Command it = (Command)t;
                        boolean bl3 = false;
                        if (StringsKt.equals(it.getCommand(), name, true)) break block7;
                        String[] $this$any$iv = it.getAlias();
                        boolean $i$f$any = false;
                        for (String element$iv : $this$any$iv) {
                            String alias = element$iv;
                            boolean bl4 = false;
                            if (!StringsKt.equals(alias, name, true)) continue;
                            bl2 = true;
                            break block6;
                        }
                        bl2 = false;
                    }
                    if (!bl2) break block8;
                }
                bl = true;
                continue;
            }
            bl = false;
        } while (!bl);
        v0 = t;
        return v0;
    }

    public final boolean registerCommand(@NotNull Command command) {
        Intrinsics.checkNotNullParameter(command, "command");
        return this.commands.add(command);
    }

    public final boolean unregisterCommand(@Nullable Command command) {
        return ((Collection)this.commands).remove(command);
    }
}

