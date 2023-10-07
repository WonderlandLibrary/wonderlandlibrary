/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import net.ccbluex.liquidbounce.features.command.commands.BindCommand;
import net.ccbluex.liquidbounce.features.command.commands.BindsCommand;
import net.ccbluex.liquidbounce.features.command.commands.EnchantCommand;
import net.ccbluex.liquidbounce.features.command.commands.FriendCommand;
import net.ccbluex.liquidbounce.features.command.commands.GiveCommand;
import net.ccbluex.liquidbounce.features.command.commands.HClipCommand;
import net.ccbluex.liquidbounce.features.command.commands.HelpCommand;
import net.ccbluex.liquidbounce.features.command.commands.HideCommand;
import net.ccbluex.liquidbounce.features.command.commands.HoloStandCommand;
import net.ccbluex.liquidbounce.features.command.commands.HurtCommand;
import net.ccbluex.liquidbounce.features.command.commands.LocalAutoSettingsCommand;
import net.ccbluex.liquidbounce.features.command.commands.PanicCommand;
import net.ccbluex.liquidbounce.features.command.commands.PingCommand;
import net.ccbluex.liquidbounce.features.command.commands.PrefixCommand;
import net.ccbluex.liquidbounce.features.command.commands.ReloadCommand;
import net.ccbluex.liquidbounce.features.command.commands.RemoteViewCommand;
import net.ccbluex.liquidbounce.features.command.commands.RenameCommand;
import net.ccbluex.liquidbounce.features.command.commands.SayCommand;
import net.ccbluex.liquidbounce.features.command.commands.ServerInfoCommand;
import net.ccbluex.liquidbounce.features.command.commands.ShortcutCommand;
import net.ccbluex.liquidbounce.features.command.commands.TacoCommand;
import net.ccbluex.liquidbounce.features.command.commands.TargetCommand;
import net.ccbluex.liquidbounce.features.command.commands.ToggleCommand;
import net.ccbluex.liquidbounce.features.command.commands.UsernameCommand;
import net.ccbluex.liquidbounce.features.command.commands.VClipCommand;
import net.ccbluex.liquidbounce.features.command.shortcuts.Shortcut;
import net.ccbluex.liquidbounce.features.command.shortcuts.ShortcutParser;
import net.ccbluex.liquidbounce.features.command.special.XrayCommand;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\nJ\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001c\u001a\u00020\nJ\u001d\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\u0018\u001a\u00020\nH\u0002\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u0005J\u0006\u0010!\u001a\u00020\u001aJ\u0016\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010#\u001a\u00020\nJ\u0010\u0010$\u001a\u00020\u00172\b\u0010 \u001a\u0004\u0018\u00010\u0005J\u000e\u0010%\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\nR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\"\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u000f\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006&"}, d2={"Lnet/ccbluex/liquidbounce/features/command/CommandManager;", "", "()V", "commands", "", "Lnet/ccbluex/liquidbounce/features/command/Command;", "getCommands", "()Ljava/util/List;", "latestAutoComplete", "", "", "getLatestAutoComplete", "()[Ljava/lang/String;", "setLatestAutoComplete", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "prefix", "", "getPrefix", "()C", "setPrefix", "(C)V", "autoComplete", "", "input", "executeCommands", "", "getCommand", "name", "getCompletions", "(Ljava/lang/String;)[Ljava/lang/String;", "registerCommand", "command", "registerCommands", "registerShortcut", "script", "unregisterCommand", "unregisterShortcut", "KyinoClient"})
public final class CommandManager {
    @NotNull
    private final List<Command> commands;
    @NotNull
    private String[] latestAutoComplete;
    private char prefix;

    @NotNull
    public final List<Command> getCommands() {
        return this.commands;
    }

    @NotNull
    public final String[] getLatestAutoComplete() {
        return this.latestAutoComplete;
    }

    public final void setLatestAutoComplete(@NotNull String[] stringArray) {
        Intrinsics.checkParameterIsNotNull(stringArray, "<set-?>");
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
        this.registerCommand(new AutoSettingsCommand());
        this.registerCommand(new LocalAutoSettingsCommand());
        this.registerCommand(new ServerInfoCommand());
        this.registerCommand(new ToggleCommand());
        this.registerCommand(new HurtCommand());
        this.registerCommand(new GiveCommand());
        this.registerCommand(new UsernameCommand());
        this.registerCommand(new TargetCommand());
        this.registerCommand(new TacoCommand());
        this.registerCommand(new BindsCommand());
        this.registerCommand(new HoloStandCommand());
        this.registerCommand(new PanicCommand());
        this.registerCommand(new PingCommand());
        this.registerCommand(new RenameCommand());
        this.registerCommand(new EnchantCommand());
        this.registerCommand(new ReloadCommand());
        this.registerCommand(new RemoteViewCommand());
        this.registerCommand(new PrefixCommand());
        this.registerCommand(new ShortcutCommand());
        this.registerCommand(new HideCommand());
        this.registerCommand(new XrayCommand());
    }

    public final void executeCommands(@NotNull String input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        for (Command command : this.commands) {
            String[] args2;
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)input, new String[]{" "}, false, 0, 6, null);
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            if (StringsKt.equals(args2[0], String.valueOf(this.prefix) + command.getCommand(), true)) {
                command.execute(args2);
                return;
            }
            for (String alias : command.getAlias()) {
                if (!StringsKt.equals(args2[0], String.valueOf(this.prefix) + alias, true)) continue;
                command.execute(args2);
                return;
            }
        }
        ClientUtils.displayChatMessage("\u00a7cCommand not found. Type " + this.prefix + "help to view all commands.");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean autoComplete(@NotNull String input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        CommandManager commandManager = this;
        String[] stringArray = this.getCompletions(input);
        if (stringArray == null) {
            CommandManager commandManager2 = commandManager;
            String[] stringArray2 = new String[]{};
            commandManager = commandManager2;
            stringArray = stringArray2;
        }
        commandManager.latestAutoComplete = stringArray;
        if (!StringsKt.startsWith$default((CharSequence)input, this.prefix, false, 2, null)) return false;
        String[] stringArray3 = this.latestAutoComplete;
        boolean bl = false;
        String[] stringArray4 = stringArray3;
        boolean bl2 = false;
        if (stringArray4.length != 0) return true;
        return false;
    }

    /*
     * Unable to fully structure code
     */
    private final String[] getCompletions(String input) {
        block20: {
            block22: {
                block21: {
                    var2_2 = input;
                    var3_3 = false;
                    if (!(var2_2.length() > 0)) break block20;
                    var2_2 = input;
                    var3_3 = false;
                    v0 = var2_2;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    v1 = v0.toCharArray();
                    Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toCharArray()");
                    if (v1[0] != this.prefix) break block20;
                    args = StringsKt.split$default((CharSequence)input, new String[]{" "}, false, 0, 6, null);
                    if (args.size() <= 1) break block21;
                    var4_6 = (String)args.get(0);
                    var5_8 = 1;
                    var22_12 = this;
                    var6_14 = false;
                    v2 = var4_6;
                    if (v2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    v3 = v2.substring(var5_8);
                    Intrinsics.checkExpressionValueIsNotNull(v3, "(this as java.lang.String).substring(startIndex)");
                    var23_17 = v3;
                    v4 = command = var22_12.getCommand((String)var23_17);
                    if (v4 != null) {
                        var5_9 = CollectionsKt.drop(args, 1);
                        var22_12 = v4;
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v5 = thisCollection$iv.toArray(new String[0]);
                        if (v5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        var23_17 = v5;
                        v6 = var22_12.tabComplete(var23_17);
                    } else {
                        v6 = null;
                    }
                    v7 = tabCompletions = v6;
                    if (v7 != null) {
                        $this$toTypedArray$iv = v7;
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v8 = thisCollection$iv.toArray(new String[0]);
                        if (v8 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        v9 = v8;
                    } else {
                        v9 = null;
                    }
                    break block22;
                }
                tabCompletions = input;
                $this$toTypedArray$iv = 1;
                $i$f$toTypedArray = false;
                v10 = tabCompletions;
                if (v10 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v11 = v10.substring($this$toTypedArray$iv);
                Intrinsics.checkExpressionValueIsNotNull(v11, "(this as java.lang.String).substring(startIndex)");
                rawInput = v11;
                $this$filter$iv = this.commands;
                $i$f$filter = false;
                $i$f$toTypedArray = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    block18: {
                        it = (Command)element$iv$iv;
                        $i$a$-filter-CommandManager$getCompletions$1 = false;
                        if (StringsKt.startsWith(it.getCommand(), rawInput, true)) ** GOTO lbl-1000
                        $this$any$iv = it.getAlias();
                        $i$f$any = false;
                        var15_30 = $this$any$iv;
                        var16_31 = var15_30.length;
                        for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                            alias = element$iv = var15_30[var17_32];
                            $i$a$-any-CommandManager$getCompletions$1$1 = false;
                            if (!StringsKt.startsWith(alias, rawInput, true)) continue;
                            v12 = true;
                            break block18;
                        }
                        v12 = false;
                    }
                    if (v12) lbl-1000:
                    // 2 sources

                    {
                        v13 = true;
                    } else {
                        v13 = false;
                    }
                    if (!v13) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                $this$map$iv = (List)destination$iv$iv;
                $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                $i$f$mapTo = false;
                for (T item$iv$iv : $this$mapTo$iv$iv) {
                    block19: {
                        it = (Command)item$iv$iv;
                        var22_13 = destination$iv$iv;
                        $i$a$-map-CommandManager$getCompletions$2 = false;
                        if (StringsKt.startsWith(it.getCommand(), rawInput, true)) {
                            v14 = it.getCommand();
                        } else {
                            $this$first$iv = it.getAlias();
                            $i$f$first = false;
                            var15_30 = $this$first$iv;
                            var16_31 = var15_30.length;
                            for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                                alias = element$iv = var15_30[var17_32];
                                $i$a$-first-CommandManager$getCompletions$2$alias$1 = false;
                                if (!StringsKt.startsWith(alias, rawInput, true)) continue;
                                v14 = element$iv;
                                break block19;
                            }
                            throw (Throwable)new NoSuchElementException("Array contains no element matching the predicate.");
                        }
                    }
                    alias = v14;
                    var13_28 = this.prefix;
                    var14_29 = false;
                    var23_18 = String.valueOf(var13_28) + alias;
                    var22_13.add(var23_18);
                }
                $this$toTypedArray$iv = (List)destination$iv$iv;
                $i$f$toTypedArray = false;
                thisCollection$iv = $this$toTypedArray$iv;
                v15 = thisCollection$iv.toArray(new String[0]);
                if (v15 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                v9 = v15;
            }
            return v9;
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
        Intrinsics.checkParameterIsNotNull(name, "name");
        Iterable iterable = this.commands;
        boolean bl2 = false;
        Iterable iterable2 = iterable;
        boolean bl3 = false;
        Iterator iterator2 = iterable2.iterator();
        do {
            block8: {
                block7: {
                    boolean bl4;
                    block6: {
                        if (!iterator2.hasNext()) {
                            v0 = null;
                            return v0;
                        }
                        t = iterator2.next();
                        Command it = (Command)t;
                        boolean bl5 = false;
                        if (StringsKt.equals(it.getCommand(), name, true)) break block7;
                        String[] $this$any$iv = it.getAlias();
                        boolean $i$f$any = false;
                        String[] stringArray = $this$any$iv;
                        int n = stringArray.length;
                        for (int i = 0; i < n; ++i) {
                            String element$iv;
                            String alias = element$iv = stringArray[i];
                            boolean bl6 = false;
                            if (!StringsKt.equals(alias, name, true)) continue;
                            bl4 = true;
                            break block6;
                        }
                        bl4 = false;
                    }
                    if (!bl4) break block8;
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
        Intrinsics.checkParameterIsNotNull(command, "command");
        return this.commands.add(command);
    }

    /*
     * WARNING - void declaration
     */
    public final void registerShortcut(@NotNull String name, @NotNull String script) {
        Collection<Pair<Command, String[]>> collection;
        Collection destination$iv$iv;
        CommandManager commandManager;
        String string;
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(script, "script");
        if (this.getCommand(name) == null) {
            void $this$mapTo$iv$iv;
            void $this$map$iv;
            Iterable iterable = ShortcutParser.INSTANCE.parse(script);
            string = name;
            commandManager = this;
            boolean $i$f$map = false;
            void var5_7 = $this$map$iv;
            destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv) {
                String[] stringArray;
                void $this$toTypedArray$iv;
                Command command;
                void it;
                List list = (List)item$iv$iv;
                collection = destination$iv$iv;
                boolean bl = false;
                if (this.getCommand((String)it.get(0)) == null) {
                    throw (Throwable)new IllegalArgumentException("Command " + (String)it.get(0) + " not found!");
                }
                Collection collection2 = (Collection)it;
                Command command2 = command;
                boolean $i$f$toTypedArray = false;
                void thisCollection$iv = $this$toTypedArray$iv;
                if (thisCollection$iv.toArray(new String[0]) == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                String[] stringArray2 = stringArray;
                Command command3 = command2;
                Pair<Command, String[]> pair = new Pair<Command, String[]>(command3, stringArray2);
                collection.add(pair);
            }
        } else {
            throw (Throwable)new IllegalArgumentException("Command already exists!");
        }
        collection = (List)destination$iv$iv;
        List list = collection;
        String string2 = string;
        commandManager.registerCommand(new Shortcut(string2, list));
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().shortcutsConfig);
    }

    public final boolean unregisterShortcut(@NotNull String name) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        boolean removed2 = this.commands.removeIf(new Predicate<Command>(name){
            final /* synthetic */ String $name;

            public final boolean test(@NotNull Command it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it instanceof Shortcut && StringsKt.equals(it.getCommand(), this.$name, true);
            }
            {
                this.$name = string;
            }
        });
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().shortcutsConfig);
        return removed2;
    }

    public final boolean unregisterCommand(@Nullable Command command) {
        Collection collection = this.commands;
        boolean bl = false;
        Collection collection2 = collection;
        if (collection2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection2).remove(command);
    }

    public CommandManager() {
        String[] stringArray;
        CommandManager commandManager = this;
        boolean bl = false;
        commandManager.commands = stringArray = (String[])new ArrayList();
        commandManager = this;
        stringArray = new String[]{};
        commandManager.latestAutoComplete = stringArray;
        this.prefix = (char)46;
    }
}

