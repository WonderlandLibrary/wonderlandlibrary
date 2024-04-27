/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.misc.NameProtect;
import net.dev.important.modules.module.modules.misc.Spammer;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.misc.HttpUtils;
import net.dev.important.utils.misc.StringUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.TextValue;
import net.dev.important.value.Value;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t\u00a8\u0006\f"}, d2={"Lnet/dev/important/utils/SettingsUtils;", "", "()V", "executeScript", "", "script", "", "generateScript", "values", "", "binds", "states", "LiquidBounce"})
public final class SettingsUtils {
    @NotNull
    public static final SettingsUtils INSTANCE = new SettingsUtils();

    private SettingsUtils() {
    }

    /*
     * WARNING - void declaration
     */
    public final void executeScript(@NotNull String script) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter(script, "script");
        Iterable $this$filter$iv = StringsKt.lines(script);
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            String it = (String)element$iv$iv;
            boolean bl = false;
            if (!(((CharSequence)it).length() > 0 && !StringsKt.startsWith$default((CharSequence)it, '#', false, 2, null))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEachIndexed$iv = (List)destination$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        block26: for (Object item$iv : $this$forEachIndexed$iv) {
            String[] args2;
            void s;
            Object element$iv$iv;
            int n = index$iv;
            index$iv = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            element$iv$iv = (String)item$iv;
            int index = n;
            boolean bl = false;
            Object object = new String[]{" "};
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)s, (String[])object, false, 0, 6, null);
            boolean $i$f$toTypedArray2 = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            }
            if (args2.length <= 1) {
                ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cSyntax error at line '" + index + "' in setting script.\n\u00a78\u00a7lLine: \u00a77" + (String)s);
                continue;
            }
            switch (args2[0]) {
                case "chat": {
                    String $i$f$toTypedArray2 = StringUtils.toCompleteString(args2, 1);
                    Intrinsics.checkNotNullExpressionValue($i$f$toTypedArray2, "toCompleteString(args, 1)");
                    ClientUtils.displayChatMessage(Intrinsics.stringPlus("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7e", ColorUtils.translateAlternateColorCodes($i$f$toTypedArray2)));
                    break;
                }
                case "unchat": {
                    String $i$f$toTypedArray2 = StringUtils.toCompleteString(args2, 1);
                    Intrinsics.checkNotNullExpressionValue($i$f$toTypedArray2, "toCompleteString(args, 1)");
                    ClientUtils.displayChatMessage(ColorUtils.translateAlternateColorCodes($i$f$toTypedArray2));
                    break;
                }
                case "load": {
                    String urlRaw;
                    String url = urlRaw = StringUtils.toCompleteString(args2, 1);
                    try {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a77Loading settings from \u00a7a\u00a7l" + url + "\u00a77...");
                        Intrinsics.checkNotNullExpressionValue(url, "url");
                        INSTANCE.executeScript(HttpUtils.get(url));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a77Loaded settings from \u00a7a\u00a7l" + url + "\u00a77.");
                    }
                    catch (Exception e) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a77Failed to load settings from \u00a7a\u00a7l" + url + "\u00a77.");
                    }
                    break;
                }
                case "targetPlayer": 
                case "targetPlayers": {
                    EntityUtils.targetPlayer = StringsKt.equals(args2[1], "true", true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args2[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetPlayer + "\u00a77.");
                    break;
                }
                case "targetMobs": {
                    EntityUtils.targetMobs = StringsKt.equals(args2[1], "true", true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args2[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetMobs + "\u00a77.");
                    break;
                }
                case "targetAnimals": {
                    EntityUtils.targetAnimals = StringsKt.equals(args2[1], "true", true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args2[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetAnimals + "\u00a77.");
                    break;
                }
                case "targetInvisible": {
                    EntityUtils.targetInvisible = StringsKt.equals(args2[1], "true", true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args2[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetInvisible + "\u00a77.");
                    break;
                }
                case "targetDead": {
                    EntityUtils.targetDead = StringsKt.equals(args2[1], "true", true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args2[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetDead + "\u00a77.");
                    break;
                }
                default: {
                    if (args2.length != 3) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cSyntax error at line '" + index + "' in setting script.\n\u00a78\u00a7lLine: \u00a77" + (String)s);
                        continue block26;
                    }
                    String moduleName = args2[0];
                    String valueName = args2[1];
                    String value = args2[2];
                    Module module2 = Client.INSTANCE.getModuleManager().getModule(moduleName);
                    if (module2 == null) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cModule \u00a7a\u00a7l" + moduleName + "\u00a7c was not found!");
                        continue block26;
                    }
                    if (StringsKt.equals(valueName, "toggle", true)) {
                        module2.setState(StringsKt.equals(value, "true", true));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module2.getName() + " \u00a77was toggled \u00a7c\u00a7l" + (module2.getState() ? "on" : "off") + "\u00a77.");
                        continue block26;
                    }
                    if (StringsKt.equals(valueName, "bind", true)) {
                        module2.setKeyBind(Keyboard.getKeyIndex((String)value));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module2.getName() + " \u00a77was bound to \u00a7c\u00a7l" + Keyboard.getKeyName((int)module2.getKeyBind()) + "\u00a77.");
                        continue block26;
                    }
                    Value<?> moduleValue = module2.getValue(valueName);
                    if (moduleValue == null) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cValue \u00a7a\u00a7l" + valueName + "\u00a7c don't found in module \u00a7a\u00a7l" + moduleName + "\u00a7c.");
                        continue block26;
                    }
                    try {
                        Value<?> value2 = moduleValue;
                        if (value2 instanceof BoolValue) {
                            ((BoolValue)moduleValue).changeValue(Boolean.parseBoolean(value));
                        } else if (value2 instanceof FloatValue) {
                            ((FloatValue)moduleValue).changeValue(Float.valueOf(Float.parseFloat(value)));
                        } else if (value2 instanceof IntegerValue) {
                            ((IntegerValue)moduleValue).changeValue(Integer.parseInt(value));
                        } else if (value2 instanceof TextValue) {
                            ((TextValue)moduleValue).changeValue(value);
                        } else if (value2 instanceof ListValue) {
                            ((ListValue)moduleValue).changeValue(value);
                        }
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module2.getName() + "\u00a77 value \u00a78\u00a7l" + moduleValue.getName() + "\u00a77 set to \u00a7c\u00a7l" + value + "\u00a77.");
                        break;
                    }
                    catch (Exception e) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + e.getClass().getName() + "\u00a77(" + e.getMessage() + ") \u00a7cAn Exception occurred while setting \u00a7a\u00a7l" + value + "\u00a7c to \u00a7a\u00a7l" + moduleValue.getName() + "\u00a7c in \u00a7a\u00a7l" + module2.getName() + "\u00a7c.");
                    }
                }
            }
        }
        Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().valuesConfig);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String generateScript(boolean values2, boolean binds, boolean states) {
        void $this$forEach$iv;
        void $this$filterTo$iv$iv;
        StringBuilder stringBuilder = new StringBuilder();
        Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getCategory() != Category.RENDER && !(it instanceof NameProtect) && !(it instanceof Spammer))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        $this$filter$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Module it = (Module)element$iv;
            boolean bl = false;
            if (values2) {
                Iterable $this$forEach$iv2 = it.getValues();
                boolean $i$f$forEach2 = false;
                for (Object element$iv2 : $this$forEach$iv2) {
                    Value value = (Value)element$iv2;
                    boolean bl2 = false;
                    stringBuilder.append(it.getName()).append(" ").append(value.getName()).append(" ").append(value.get()).append("\n");
                }
            }
            if (states) {
                stringBuilder.append(it.getName()).append(" toggle ").append(it.getState()).append("\n");
            }
            if (!binds) continue;
            stringBuilder.append(it.getName()).append(" bind ").append(Keyboard.getKeyName((int)it.getKeyBind())).append("\n");
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "stringBuilder.toString()");
        return string;
    }
}

