/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

public final class AutoSettingsCommand
extends Command {
    private final Object loadingLock = new Object();
    private List<String> autoSettingFiles;

    @Override
    public void execute(String[] args) {
        if (args.length <= 1) {
            this.chatSyntax("settings <load/list>");
            return;
        }
        if (StringsKt.equals((String)args[1], (String)"load", (boolean)true)) {
            String string;
            if (args.length < 3) {
                this.chatSyntax("settings load <name/url>");
                return;
            }
            if (StringsKt.startsWith$default((String)args[2], (String)"http", (boolean)false, (int)2, null)) {
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
                string = stringBuilder.append(string4).toString();
            }
            String url = string;
            this.chat("Loading settings...");
            ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this, url){
                final /* synthetic */ AutoSettingsCommand this$0;
                final /* synthetic */ String $url;

                public final void invoke() {
                    try {
                        String settings2 = HttpUtils.get(this.$url);
                        AutoSettingsCommand.access$chat(this.this$0, "Applying settings...");
                        SettingsUtils.INSTANCE.executeScript(settings2);
                        AutoSettingsCommand.access$chat(this.this$0, "\u00a76Settings applied successfully");
                        LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AutoSettingsCommand", "setting", NotifyType.INFO, 0, 0, 24, null));
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
            }), (int)31, null);
        } else if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
            this.chat("Loading settings...");
            AutoSettingsCommand.loadSettings$default(this, false, null, (Function1)new Function1<List<? extends String>, Unit>(this){
                final /* synthetic */ AutoSettingsCommand this$0;

                public final void invoke(List<String> it) {
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
        Thread thread2 = ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this, useCached, callback){
            final /* synthetic */ AutoSettingsCommand this$0;
            final /* synthetic */ boolean $useCached;
            final /* synthetic */ Function1 $callback;

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
                        this.$callback.invoke((Object)list);
                        return;
                    }
                    try {
                        JsonElement json = new JsonParser().parse(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidCloud/contents/LiquidBounce/settings"));
                        boolean bl4 = false;
                        List autoSettings = new ArrayList<E>();
                        if (json instanceof JsonArray) {
                            for (JsonElement setting : (JsonArray)json) {
                                autoSettings.add(setting.getAsJsonObject().get("name").getAsString());
                            }
                        }
                        this.$callback.invoke((Object)autoSettings);
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
        }), (int)31, null);
        if (join != null) {
            thread2.join(join);
        }
    }

    static /* synthetic */ void loadSettings$default(AutoSettingsCommand autoSettingsCommand, boolean bl, Long l, Function1 function1, int n, Object object) {
        if ((n & 2) != 0) {
            l = null;
        }
        autoSettingsCommand.loadSettings(bl, l, (Function1<? super List<String>, Unit>)function1);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        List list;
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                Iterable $this$filter$iv = CollectionsKt.listOf((Object[])new String[]{"list", "load"});
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl2 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                boolean $i$f$filter;
                Iterable $this$filter$iv;
                if (StringsKt.equals((String)args[0], (String)"load", (boolean)true)) {
                    if (this.autoSettingFiles == null) {
                        this.loadSettings(true, 500L, (Function1<? super List<String>, Unit>)((Function1)tabComplete.2.INSTANCE));
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
                            if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
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

    public static final /* synthetic */ void access$chat(AutoSettingsCommand $this, String msg) {
        $this.chat(msg);
    }

    public static final /* synthetic */ void access$playEdit(AutoSettingsCommand $this) {
        $this.playEdit();
    }

    public static final /* synthetic */ Object access$getLoadingLock$p(AutoSettingsCommand $this) {
        return $this.loadingLock;
    }

    public static final /* synthetic */ List access$getAutoSettingFiles$p(AutoSettingsCommand $this) {
        return $this.autoSettingFiles;
    }

    public static final /* synthetic */ void access$setAutoSettingFiles$p(AutoSettingsCommand $this, List list) {
        $this.autoSettingFiles = list;
    }
}

