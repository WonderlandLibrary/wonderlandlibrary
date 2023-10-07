/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.fun.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.fun.Spammer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/utils/SettingsUtils;", "", "()V", "executeScript", "", "script", "", "generateScript", "values", "", "binds", "states", "KyinoClient"})
public final class SettingsUtils {
    public static final SettingsUtils INSTANCE;

    /*
     * WARNING - void declaration
     */
    public final void executeScript(@NotNull String script) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkParameterIsNotNull(script, "script");
        Iterable $this$filter$iv = StringsKt.lines(script);
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            String it = (String)element$iv$iv;
            boolean bl3 = false;
            CharSequence charSequence = it;
            boolean bl2 = false;
            if (!(charSequence.length() > 0 && !StringsKt.startsWith$default((CharSequence)it, '#', false, 2, null))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEachIndexed$iv = (List)destination$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            String[] args2;
            void s;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            String bl3 = (String)item$iv;
            int index = n2;
            boolean bl4 = false;
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)s, new String[]{" "}, false, 0, 6, null);
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            if (args2.length <= 1) {
                ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cSyntax error at line '" + index + "' in setting script.\n\u00a78\u00a7lLine: \u00a77" + (String)s);
                continue;
            }
            switch (args2[0]) {
                case "chat": {
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7e");
                    String string = StringUtils.toCompleteString(args2, 1);
                    Intrinsics.checkExpressionValueIsNotNull(string, "StringUtils.toCompleteString(args, 1)");
                    ClientUtils.displayChatMessage(stringBuilder.append(ColorUtils.translateAlternateColorCodes(string)).toString());
                    break;
                }
                case "unchat": {
                    String string = StringUtils.toCompleteString(args2, 1);
                    Intrinsics.checkExpressionValueIsNotNull(string, "StringUtils.toCompleteString(args, 1)");
                    ClientUtils.displayChatMessage(ColorUtils.translateAlternateColorCodes(string));
                    break;
                }
                case "load": {
                    String urlRaw;
                    String url = urlRaw = StringUtils.toCompleteString(args2, 1);
                    try {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a77Loading settings from \u00a7a\u00a7l" + url + "\u00a77...");
                        String string = url;
                        Intrinsics.checkExpressionValueIsNotNull(string, "url");
                        INSTANCE.executeScript(HttpUtils.get(string));
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
                        break;
                    }
                    String moduleName = args2[0];
                    String valueName = args2[1];
                    String value = args2[2];
                    Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(moduleName);
                    if (module == null) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cModule \u00a7a\u00a7l" + moduleName + "\u00a7c was not found!");
                        break;
                    }
                    if (StringsKt.equals(valueName, "toggle", true)) {
                        module.setState(StringsKt.equals(value, "true", true));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module.getName() + " \u00a77was toggled \u00a7c\u00a7l" + (module.getState() ? "on" : "off") + "\u00a77.");
                        break;
                    }
                    if (StringsKt.equals(valueName, "bind", true)) {
                        module.setKeyBind(Keyboard.getKeyIndex((String)value));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module.getName() + " \u00a77was bound to \u00a7c\u00a7l" + Keyboard.getKeyName((int)module.getKeyBind()) + "\u00a77.");
                        break;
                    }
                    Value<?> moduleValue = module.getValue(valueName);
                    if (moduleValue == null) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cValue \u00a7a\u00a7l" + valueName + "\u00a7c don't found in module \u00a7a\u00a7l" + moduleName + "\u00a7c.");
                        break;
                    }
                    try {
                        int n3;
                        boolean bl5;
                        Value value2;
                        String string;
                        Value<?> value3 = moduleValue;
                        if (value3 instanceof BoolValue) {
                            string = value;
                            value2 = (BoolValue)moduleValue;
                            bl5 = false;
                            n3 = Boolean.parseBoolean(string);
                            value2.changeValue(n3 != 0);
                        } else if (value3 instanceof FloatValue) {
                            string = value;
                            value2 = (FloatValue)moduleValue;
                            bl5 = false;
                            float f = Float.parseFloat(string);
                            value2.changeValue(Float.valueOf(f));
                        } else if (value3 instanceof IntegerValue) {
                            string = value;
                            value2 = (IntegerValue)moduleValue;
                            bl5 = false;
                            n3 = Integer.parseInt(string);
                            value2.changeValue(n3);
                        } else if (value3 instanceof TextValue) {
                            ((TextValue)moduleValue).changeValue(value);
                        } else if (value3 instanceof ListValue) {
                            ((ListValue)moduleValue).changeValue(value);
                        }
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module.getName() + "\u00a77 value \u00a78\u00a7l" + moduleValue.getName() + "\u00a77 set to \u00a7c\u00a7l" + value + "\u00a77.");
                        break;
                    }
                    catch (Exception e) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + e.getClass().getName() + "\u00a77(" + e.getMessage() + ") \u00a7cAn Exception occurred while setting \u00a7a\u00a7l" + value + "\u00a7c to \u00a7a\u00a7l" + moduleValue.getName() + "\u00a7c in \u00a7a\u00a7l" + module.getName() + "\u00a7c.");
                    }
                }
            }
        }
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String generateScript(boolean values2, boolean binds, boolean states) {
        void $this$filterTo$iv$iv;
        StringBuilder stringBuilder = new StringBuilder();
        Iterable $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getCategory() != ModuleCategory.RENDER && !(it instanceof NameProtect) && !(it instanceof Spammer))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
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
                    stringBuilder.append(it.getName() + ' ' + value.getName() + ' ' + value.get()).append("\n");
                }
            }
            if (states) {
                stringBuilder.append(it.getName() + " toggle " + it.getState()).append("\n");
            }
            if (!binds) continue;
            stringBuilder.append(it.getName() + " bind " + Keyboard.getKeyName((int)it.getKeyBind())).append("\n");
        }
        String string = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "stringBuilder.toString()");
        return string;
    }

    private SettingsUtils() {
    }

    static {
        SettingsUtils settingsUtils;
        INSTANCE = settingsUtils = new SettingsUtils();
    }
}

