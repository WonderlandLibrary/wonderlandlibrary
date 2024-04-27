/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.commands.BindCommand;
import net.ccbluex.liquidbounce.features.command.commands.BindsCommand;
import net.ccbluex.liquidbounce.features.command.commands.ConfigCommand;
import net.ccbluex.liquidbounce.features.command.commands.FriendCommand;
import net.ccbluex.liquidbounce.features.command.commands.HClipCommand;
import net.ccbluex.liquidbounce.features.command.commands.HelpCommand;
import net.ccbluex.liquidbounce.features.command.commands.HideCommand;
import net.ccbluex.liquidbounce.features.command.commands.HudCommand;
import net.ccbluex.liquidbounce.features.command.commands.PingCommand;
import net.ccbluex.liquidbounce.features.command.commands.PrefixCommand;
import net.ccbluex.liquidbounce.features.command.commands.ReloadCommand;
import net.ccbluex.liquidbounce.features.command.commands.RemoteViewCommand;
import net.ccbluex.liquidbounce.features.command.commands.RenameCommand;
import net.ccbluex.liquidbounce.features.command.commands.SayCommand;
import net.ccbluex.liquidbounce.features.command.commands.ScriptManagerCommand;
import net.ccbluex.liquidbounce.features.command.commands.ServerInfoCommand;
import net.ccbluex.liquidbounce.features.command.commands.TargetCommand;
import net.ccbluex.liquidbounce.features.command.commands.ToggleCommand;
import net.ccbluex.liquidbounce.features.command.commands.VClipCommand;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.Nullable;

public final class CommandManager {
    private final List commands;
    private char prefix;
    private String[] latestAutoComplete;

    public final String[] getLatestAutoComplete() {
        return this.latestAutoComplete;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final Command getCommand(String string) {
        Object t;
        Object v0;
        boolean bl;
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
                        Command command = (Command)t;
                        boolean bl5 = false;
                        if (StringsKt.equals((String)command.getCommand(), (String)string, (boolean)true)) break block7;
                        String[] stringArray = command.getAlias();
                        boolean bl6 = false;
                        String[] stringArray2 = stringArray;
                        int n = stringArray2.length;
                        for (int i = 0; i < n; ++i) {
                            String string2;
                            String string3 = string2 = stringArray2[i];
                            boolean bl7 = false;
                            if (!StringsKt.equals((String)string3, (String)string, (boolean)true)) continue;
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

    public final List getCommands() {
        return this.commands;
    }

    public final void setLatestAutoComplete(String[] stringArray) {
        this.latestAutoComplete = stringArray;
    }

    public final void executeCommands(String string) {
        for (Command command : this.commands) {
            String[] stringArray;
            String[] stringArray2 = (String[])StringsKt.split$default((CharSequence)string, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
            int n = 0;
            String[] stringArray3 = stringArray2;
            if (stringArray3.toArray(new String[0]) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            if (StringsKt.equals((String)stringArray[0], (String)(String.valueOf(this.prefix) + command.getCommand()), (boolean)true)) {
                command.execute(stringArray);
                return;
            }
            for (String string2 : command.getAlias()) {
                if (!StringsKt.equals((String)stringArray[0], (String)(String.valueOf(this.prefix) + string2), (boolean)true)) continue;
                command.execute(stringArray);
                return;
            }
        }
        ClientUtils.displayChatMessage("\u00a7cCommand not found. Type " + this.prefix + "help to view all commands.");
    }

    public CommandManager() {
        CommandManager commandManager = this;
        boolean bl = false;
        String[] stringArray = (String[])new ArrayList();
        commandManager.commands = stringArray;
        commandManager = this;
        stringArray = new String[]{};
        commandManager.latestAutoComplete = stringArray;
        this.prefix = (char)46;
    }

    public final void registerCommands() {
        this.registerCommand(new BindCommand());
        this.registerCommand(new VClipCommand());
        this.registerCommand(new HClipCommand());
        this.registerCommand(new HelpCommand());
        this.registerCommand(new SayCommand());
        this.registerCommand(new FriendCommand());
        this.registerCommand(new ServerInfoCommand());
        this.registerCommand(new ToggleCommand());
        this.registerCommand(new TargetCommand());
        this.registerCommand(new BindsCommand());
        this.registerCommand(new PingCommand());
        this.registerCommand(new RenameCommand());
        this.registerCommand(new ReloadCommand());
        this.registerCommand(new ScriptManagerCommand());
        this.registerCommand(new RemoteViewCommand());
        this.registerCommand(new PrefixCommand());
        this.registerCommand(new HideCommand());
        this.registerCommand(new ConfigCommand());
        this.registerCommand(new HudCommand());
    }

    public final boolean registerCommand(Command command) {
        return this.commands.add(command);
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean autoComplete(String string) {
        CommandManager commandManager = this;
        String[] stringArray = this.getCompletions(string);
        if (stringArray == null) {
            CommandManager commandManager2 = commandManager;
            String[] stringArray2 = new String[]{};
            commandManager = commandManager2;
            stringArray = stringArray2;
        }
        commandManager.latestAutoComplete = stringArray;
        if (!StringsKt.startsWith$default((CharSequence)string, (char)this.prefix, (boolean)false, (int)2, null)) return false;
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
    private final String[] getCompletions(String var1_1) {
        block20: {
            block22: {
                block21: {
                    var2_2 = var1_1;
                    var3_3 = false;
                    if (!(var2_2.length() > 0)) break block20;
                    var2_2 = var1_1;
                    var3_3 = false;
                    v0 = var2_2;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    if (v0.toCharArray()[0] != this.prefix) break block20;
                    var2_2 = StringsKt.split$default((CharSequence)var1_1, (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
                    if (var2_2.size() <= 1) break block21;
                    var4_6 = (String)var2_2.get(0);
                    var5_8 = 1;
                    var22_12 = this;
                    var6_14 = false;
                    v1 = var4_6;
                    if (v1 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    var23_17 = v1.substring(var5_8);
                    v2 = var3_4 = var22_12.getCommand((String)var23_17);
                    if (v2 != null) {
                        var5_9 = CollectionsKt.drop((Iterable)((Iterable)var2_2), (int)1);
                        var22_12 = v2;
                        var6_14 = false;
                        var7_19 = var5_9;
                        v3 = var7_19.toArray(new String[0]);
                        if (v3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        var23_17 = v3;
                        v4 = var22_12.tabComplete(var23_17);
                    } else {
                        v4 = null;
                    }
                    v5 = var4_6 = v4;
                    if (v5 != null) {
                        var5_10 = (Collection)v5;
                        var6_14 = false;
                        var7_19 = var5_10;
                        v6 = var7_19.toArray(new String[0]);
                        if (v6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        v7 = v6;
                    } else {
                        v7 = null;
                    }
                    break block22;
                }
                var4_7 = var1_1;
                var5_11 = 1;
                var6_15 = false;
                v8 = var4_7;
                if (v8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var3_5 = v8.substring(var5_11);
                var4_7 = this.commands;
                var5_11 = 0;
                var6_16 = var4_7;
                var7_20 = new ArrayList<E>();
                var8_21 = false;
                var9_22 = var6_16.iterator();
                while (var9_22.hasNext()) {
                    block18: {
                        var10_23 = var9_22.next();
                        var11_24 = (Command)var10_23;
                        var12_25 = false;
                        if (StringsKt.startsWith((String)var11_24.getCommand(), (String)var3_5, (boolean)true)) ** GOTO lbl-1000
                        var13_26 = var11_24.getAlias();
                        var14_29 = false;
                        var15_30 = var13_26;
                        var16_31 = var15_30.length;
                        for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                            var19_34 = var18_33 = var15_30[var17_32];
                            var20_35 = false;
                            if (!StringsKt.startsWith((String)var19_34, (String)var3_5, (boolean)true)) continue;
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
                    var7_20.add(var10_23);
                }
                var4_7 = (List)var7_20;
                var5_11 = 0;
                var6_16 = var4_7;
                var7_20 = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)var4_7, (int)10));
                var8_21 = false;
                var9_22 = var6_16.iterator();
                while (var9_22.hasNext()) {
                    block19: {
                        var10_23 = var9_22.next();
                        var11_24 = (Command)var10_23;
                        var22_13 = var7_20;
                        var12_25 = false;
                        if (StringsKt.startsWith((String)var11_24.getCommand(), (String)var3_5, (boolean)true)) {
                            v11 = var11_24.getCommand();
                        } else {
                            var13_27 = var11_24.getAlias();
                            var14_29 = false;
                            var15_30 = var13_27;
                            var16_31 = var15_30.length;
                            for (var17_32 = 0; var17_32 < var16_31; ++var17_32) {
                                var19_34 = var18_33 = var15_30[var17_32];
                                var20_35 = false;
                                if (!StringsKt.startsWith((String)var19_34, (String)var3_5, (boolean)true)) continue;
                                v11 = var18_33;
                                break block19;
                            }
                            throw (Throwable)new NoSuchElementException("Array contains no element matching the predicate.");
                        }
                    }
                    var21_36 = v11;
                    var13_28 = this.prefix;
                    var14_29 = false;
                    var23_18 = String.valueOf(var13_28) + var21_36;
                    var22_13.add(var23_18);
                }
                var4_7 = (List)var7_20;
                var5_11 = 0;
                var6_16 = var4_7;
                v12 = var6_16.toArray(new String[0]);
                if (v12 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                v7 = v12;
            }
            return v7;
        }
        return null;
    }

    public final char getPrefix() {
        return this.prefix;
    }

    public final void setPrefix(char c) {
        this.prefix = c;
    }
}

