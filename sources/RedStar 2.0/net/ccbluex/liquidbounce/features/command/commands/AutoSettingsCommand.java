package net.ccbluex.liquidbounce.features.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.commands.AutoSettingsCommand;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.SettingsUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\n\n\b\n!\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\t\n\u0000\n\n \n\b\u000020B¢J\b0\t2\f\n\b00H¢\fJ;\r0\t202\n\b02\n\b000\t0H¢J!\b002\f\n\b00H¢R\n00X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/AutoSettingsCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "autoSettingFiles", "", "", "loadingLock", "Ljava/lang/Object;", "execute", "", "args", "", "([Ljava/lang/String;)V", "loadSettings", "useCached", "", "join", "", "callback", "Lkotlin/Function1;", "", "(ZLjava/lang/Long;Lkotlin/jvm/functions/Function1;)V", "tabComplete", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class AutoSettingsCommand
extends Command {
    private final Object loadingLock = new Object();
    private List<String> autoSettingFiles;

    @Override
    public void execute(@NotNull String[] args) {
        block6: {
            block5: {
                String string;
                Intrinsics.checkParameterIsNotNull(args, "args");
                if (args.length <= 1) {
                    this.chatSyntax("settings <load/list>");
                    return;
                }
                if (!StringsKt.equals(args[1], "load", true)) break block5;
                if (args.length < 3) {
                    this.chatSyntax("settings load <name/url>");
                    return;
                }
                if (StringsKt.startsWith$default(args[2], "http", false, 2, null)) {
                    string = args[2];
                } else {
                    String string2 = args[2];
                    StringBuilder stringBuilder = new StringBuilder().append("https://cloud.liquidbounce.net/LiquidBounce/settings/");
                    boolean bl = false;
                    String string3 = string2;
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string4 = string3.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
                    String string5 = string4;
                    string = stringBuilder.append(string5).toString();
                }
                String url = string;
                this.chat("Loading settings...");
                ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this, url){
                    final AutoSettingsCommand this$0;
                    final String $url;

                    public final void invoke() {
                        try {
                            String settings = HttpUtils.get(this.$url);
                            AutoSettingsCommand.access$chat(this.this$0, "Applying settings...");
                            SettingsUtils.INSTANCE.executeScript(settings);
                            AutoSettingsCommand.access$chat(this.this$0, "§6Settings applied successfully");
                            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Notification", "Updated Settings", NotifyType.INFO, 0, 0, 24, null));
                            AutoSettingsCommand.access$playEdit(this.this$0);
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            AutoSettingsCommand.access$chat(this.this$0, "Failed to fetch auto settings.");
                        }
                    }
                    {
                        this.this$0 = autoSettingsCommand;
                        this.$url = string;
                        super(0);
                    }
                }, 31, null);
                break block6;
            }
            if (!StringsKt.equals(args[1], "list", true)) break block6;
            this.chat("Loading settings...");
            AutoSettingsCommand.loadSettings$default(this, false, null, new Function1<List<? extends String>, Unit>(this){
                final AutoSettingsCommand this$0;

                public final void invoke(@NotNull List<String> it) {
                    Intrinsics.checkParameterIsNotNull(it, "it");
                    for (String setting : it) {
                        AutoSettingsCommand.access$chat(this.this$0, "> " + setting);
                    }
                }
                {
                    this.this$0 = autoSettingsCommand;
                    super(1);
                }
            }, 2, null);
        }
    }

    private final void loadSettings(boolean useCached, Long join, Function1<? super List<String>, Unit> callback) {
        Thread thread2 = ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this, useCached, callback){
            final AutoSettingsCommand this$0;
            final boolean $useCached;
            final Function1 $callback;

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public final void invoke() {
                Object object = AutoSettingsCommand.access$getLoadingLock$p(this.this$0);
                boolean bl = false;
                boolean bl2 = false;
                synchronized (object) {
                    boolean bl3 = false;
                    if (this.$useCached && AutoSettingsCommand.access$getAutoSettingFiles$p(this.this$0) != null) {
                        List list = AutoSettingsCommand.access$getAutoSettingFiles$p(this.this$0);
                        if (list == null) {
                            Intrinsics.throwNpe();
                        }
                        this.$callback.invoke(list);
                        return;
                    }
                    try {
                        JsonElement json = new JsonParser().parse(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidCloud/contents/LiquidBounce/settings"));
                        boolean bl4 = false;
                        List autoSettings = new ArrayList<E>();
                        if (json instanceof JsonArray) {
                            Iterator iterator2 = ((JsonArray)json).iterator();
                            while (iterator2.hasNext()) {
                                JsonElement setting;
                                JsonElement jsonElement = setting = (JsonElement)iterator2.next();
                                Intrinsics.checkExpressionValueIsNotNull(jsonElement, "setting");
                                JsonElement jsonElement2 = jsonElement.getAsJsonObject().get("name");
                                Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "setting.asJsonObject[\"name\"]");
                                String string = jsonElement2.getAsString();
                                Intrinsics.checkExpressionValueIsNotNull(string, "setting.asJsonObject[\"name\"].asString");
                                autoSettings.add(string);
                            }
                        }
                        this.$callback.invoke(autoSettings);
                        AutoSettingsCommand.access$setAutoSettingFiles$p(this.this$0, autoSettings);
                    }
                    catch (Exception e) {
                        AutoSettingsCommand.access$chat(this.this$0, "Failed to fetch auto settings list.");
                    }
                    Unit unit = Unit.INSTANCE;
                }
            }
            {
                this.this$0 = autoSettingsCommand;
                this.$useCached = bl;
                this.$callback = function1;
                super(0);
            }
        }, 31, null);
        if (join != null) {
            thread2.join(join);
        }
    }

    static void loadSettings$default(AutoSettingsCommand autoSettingsCommand, boolean bl, Long l, Function1 function1, int n, Object object) {
        if ((n & 2) != 0) {
            l = null;
        }
        autoSettingsCommand.loadSettings(bl, l, function1);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull(args, "args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf("list", "load");
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith(it, args[0], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                boolean $i$f$filter;
                Iterable $this$filter$iv;
                if (StringsKt.equals(args[0], "load", true)) {
                    if (this.autoSettingFiles == null) {
                        this.loadSettings(true, 500L, tabComplete.2.INSTANCE);
                    }
                    if (this.autoSettingFiles != null) {
                        List<String> list2 = this.autoSettingFiles;
                        if (list2 == null) {
                            Intrinsics.throwNpe();
                        }
                        $this$filter$iv = list2;
                        $i$f$filter = false;
                        Iterable $this$filterTo$iv$iv = $this$filter$iv;
                        Collection destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            String it = (String)element$iv$iv;
                            boolean bl3 = false;
                            if (!StringsKt.startsWith(it, args[1], true)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv;
                    }
                }
                return CollectionsKt.emptyList();
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public AutoSettingsCommand() {
        super("autosettings", "setting", "settings", "config", "autosetting");
    }

    public static final void access$chat(AutoSettingsCommand $this, String msg) {
        $this.chat(msg);
    }

    public static final void access$playEdit(AutoSettingsCommand $this) {
        $this.playEdit();
    }

    public static final Object access$getLoadingLock$p(AutoSettingsCommand $this) {
        return $this.loadingLock;
    }

    public static final List access$getAutoSettingFiles$p(AutoSettingsCommand $this) {
        return $this.autoSettingFiles;
    }

    public static final void access$setAutoSettingFiles$p(AutoSettingsCommand $this, List list) {
        $this.autoSettingFiles = list;
    }
}
