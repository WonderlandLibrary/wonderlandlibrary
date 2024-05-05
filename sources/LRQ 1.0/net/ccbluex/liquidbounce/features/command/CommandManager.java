/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.TypeIntrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
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
import net.ccbluex.liquidbounce.features.command.commands.LoginCommand;
import net.ccbluex.liquidbounce.features.command.commands.PanicCommand;
import net.ccbluex.liquidbounce.features.command.commands.PingCommand;
import net.ccbluex.liquidbounce.features.command.commands.PrefixCommand;
import net.ccbluex.liquidbounce.features.command.commands.ReloadCommand;
import net.ccbluex.liquidbounce.features.command.commands.RemoteViewCommand;
import net.ccbluex.liquidbounce.features.command.commands.RenameCommand;
import net.ccbluex.liquidbounce.features.command.commands.SayCommand;
import net.ccbluex.liquidbounce.features.command.commands.ScriptManagerCommand;
import net.ccbluex.liquidbounce.features.command.commands.ServerInfoCommand;
import net.ccbluex.liquidbounce.features.command.commands.ShortcutCommand;
import net.ccbluex.liquidbounce.features.command.commands.TacoCommand;
import net.ccbluex.liquidbounce.features.command.commands.TargetCommand;
import net.ccbluex.liquidbounce.features.command.commands.ToggleCommand;
import net.ccbluex.liquidbounce.features.command.commands.UsernameCommand;
import net.ccbluex.liquidbounce.features.command.commands.VClipCommand;
import net.ccbluex.liquidbounce.features.command.shortcuts.Shortcut;
import net.ccbluex.liquidbounce.features.command.shortcuts.ShortcutParser;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.Nullable;

public final class CommandManager {
    private final List<Command> commands;
    private String[] latestAutoComplete;
    private char prefix;

    public final List<Command> getCommands() {
        return this.commands;
    }

    public final String[] getLatestAutoComplete() {
        return this.latestAutoComplete;
    }

    public final void setLatestAutoComplete(String[] stringArray) {
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
        this.registerCommand(new LoginCommand());
        this.registerCommand(new ScriptManagerCommand());
        this.registerCommand(new RemoteViewCommand());
        this.registerCommand(new PrefixCommand());
        this.registerCommand(new ShortcutCommand());
        this.registerCommand(new HideCommand());
    }

    public final void executeCommands(String input) {
        for (Command command : this.commands) {
            String[] args;
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)input, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            if (StringsKt.equals((String)args[0], (String)(String.valueOf(this.prefix) + command.getCommand()), (boolean)true)) {
                command.execute(args);
                return;
            }
            for (String alias : command.getAlias()) {
                if (!StringsKt.equals((String)args[0], (String)(String.valueOf(this.prefix) + alias), (boolean)true)) continue;
                command.execute(args);
                return;
            }
        }
        ClientUtils.displayChatMessage("\u00a7cCommand not found. Type " + this.prefix + "help to view all commands.");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean autoComplete(String input) {
        CommandManager commandManager = this;
        String[] stringArray = this.getCompletions(input);
        if (stringArray == null) {
            CommandManager commandManager2 = commandManager;
            String[] stringArray2 = new String[]{};
            commandManager = commandManager2;
            stringArray = stringArray2;
        }
        commandManager.latestAutoComplete = stringArray;
        if (!StringsKt.startsWith$default((CharSequence)input, (char)this.prefix, (boolean)false, (int)2, null)) return false;
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
                    if (v0.toCharArray()[0] != this.prefix) break block20;
                    args = StringsKt.split$default((CharSequence)input, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
                    if (args.size() <= 1) break block21;
                    var4_6 = (String)args.get(0);
                    var5_8 = 1;
                    var22_12 = this;
                    var6_14 = false;
                    v1 = var4_6;
                    if (v1 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    var23_17 = v1.substring(var5_8);
                    v2 = command = var22_12.getCommand((String)var23_17);
                    if (v2 != null) {
                        var5_9 = CollectionsKt.drop((Iterable)args, (int)1);
                        var22_12 = v2;
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v3 = thisCollection$iv.toArray(new String[0]);
                        if (v3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        var23_17 = v3;
                        v4 = var22_12.tabComplete(var23_17);
                    } else {
                        v4 = null;
                    }
                    v5 = tabCompletions = v4;
                    if (v5 != null) {
                        $this$toTypedArray$iv = v5;
                        $i$f$toTypedArray = false;
                        thisCollection$iv = $this$toTypedArray$iv;
                        v6 = thisCollection$iv.toArray(new String[0]);
                        if (v6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        v7 = v6;
                    } else {
                        v7 = null;
                    }
                    break block22;
                }
                tabCompletions = input;
                $this$toTypedArray$iv = 1;
                $i$f$toTypedArray = false;
                v8 = tabCompletions;
                if (v8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                rawInput = v8.substring($this$toTypedArray$iv);
                $this$filter$iv = this.commands;
                $i$f$filter = false;
                $i$f$toTypedArray = $this$filter$iv;
                destination$iv$iv = new ArrayList<E>();
                $i$f$filterTo = false;
                for (T element$iv$iv : $this$filterTo$iv$iv) {
                    block18: {
                        it = (Command)element$iv$iv;
                        $i$a$-filter-CommandManager$getCompletions$1 = false;
                        if (StringsKt.startsWith((String)it.getCommand(), (String)rawInput, (boolean)true)) ** GOTO lbl-1000
                        $this$any$iv = it.getAlias();
                        $i$f$any = false;
                        var15_30 = $this$any$iv;
                        var16_31 = var15_30.length;
                        for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                            alias = element$iv = var15_30[var17_32];
                            $i$a$-any-CommandManager$getCompletions$1$1 = false;
                            if (!StringsKt.startsWith((String)alias, (String)rawInput, (boolean)true)) continue;
                            v9 = true;
                            break block18;
                        }
                        v9 = false;
                    }
                    if (v9) lbl-1000:
                    // 2 sources

                    {
                        v10 = true;
                    } else {
                        v10 = false;
                    }
                    if (!v10) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                $this$map$iv = (List)destination$iv$iv;
                $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                $i$f$mapTo = false;
                for (T item$iv$iv : $this$mapTo$iv$iv) {
                    block19: {
                        it = (Command)item$iv$iv;
                        var22_13 = destination$iv$iv;
                        $i$a$-map-CommandManager$getCompletions$2 = false;
                        if (StringsKt.startsWith((String)it.getCommand(), (String)rawInput, (boolean)true)) {
                            v11 = it.getCommand();
                        } else {
                            $this$first$iv = it.getAlias();
                            $i$f$first = false;
                            var15_30 = $this$first$iv;
                            var16_31 = var15_30.length;
                            for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                                alias = element$iv = var15_30[var17_32];
                                $i$a$-first-CommandManager$getCompletions$2$alias$1 = false;
                                if (!StringsKt.startsWith((String)alias, (String)rawInput, (boolean)true)) continue;
                                v11 = element$iv;
                                break block19;
                            }
                            throw (Throwable)new NoSuchElementException("Array contains no element matching the predicate.");
                        }
                    }
                    alias = v11;
                    var13_28 = this.prefix;
                    var14_29 = false;
                    var23_18 = String.valueOf(var13_28) + alias;
                    var22_13.add(var23_18);
                }
                $this$toTypedArray$iv = (List)destination$iv$iv;
                $i$f$toTypedArray = false;
                thisCollection$iv = $this$toTypedArray$iv;
                v12 = thisCollection$iv.toArray(new String[0]);
                if (v12 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                v7 = v12;
            }
            return v7;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final Command getCommand(String name) {
        Object t;
        Object v0;
        boolean bl;
        Iterable iterable = this.commands;
        boolean bl2 = false;
        Iterable iterable2 = iterable;
        boolean bl3 = false;
        Iterator iterator = iterable2.iterator();
        do {
            block8: {
                block7: {
                    boolean bl4;
                    block6: {
                        if (!iterator.hasNext()) {
                            v0 = null;
                            return v0;
                        }
                        t = iterator.next();
                        Command it = (Command)t;
                        boolean bl5 = false;
                        if (StringsKt.equals((String)it.getCommand(), (String)name, (boolean)true)) break block7;
                        String[] $this$any$iv = it.getAlias();
                        boolean $i$f$any = false;
                        String[] stringArray = $this$any$iv;
                        int n = stringArray.length;
                        for (int i = 0; i < n; ++i) {
                            String element$iv;
                            String alias = element$iv = stringArray[i];
                            boolean bl6 = false;
                            if (!StringsKt.equals((String)alias, (String)name, (boolean)true)) continue;
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

    public final boolean registerCommand(Command command) {
        return this.commands.add(command);
    }

    /*
     * WARNING - void declaration
     */
    public final void registerShortcut(String name, String script) {
        Collection<Pair> collection;
        Collection destination$iv$iv;
        CommandManager commandManager;
        String string;
        if (this.getCommand(name) == null) {
            void $this$mapTo$iv$iv;
            void $this$map$iv;
            Iterable iterable = ShortcutParser.INSTANCE.parse(script);
            string = name;
            commandManager = this;
            boolean $i$f$map = false;
            void var5_7 = $this$map$iv;
            destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
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
                Pair pair = new Pair((Object)command3, (Object)stringArray2);
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

    public final boolean unregisterShortcut(String name) {
        boolean removed2 = this.commands.removeIf(new Predicate<Command>(name){
            final /* synthetic */ String $name;

            public final boolean test(Command it) {
                return it instanceof Shortcut && StringsKt.equals((String)it.getCommand(), (String)this.$name, (boolean)true);
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
        return TypeIntrinsics.asMutableCollection((Object)collection2).remove(command);
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

