/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
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
import org.lwjgl.input.Keyboard;

public final class SettingsUtils {
    public static final SettingsUtils INSTANCE;

    /*
     * WARNING - void declaration
     */
    public final void executeScript(String script) {
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv = StringsKt.lines((CharSequence)script);
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            String it = (String)element$iv$iv;
            boolean bl3 = false;
            CharSequence charSequence = it;
            boolean bl2 = false;
            if (!(charSequence.length() > 0 && !StringsKt.startsWith$default((CharSequence)it, (char)'#', (boolean)false, (int)2, null))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEachIndexed$iv = (List)destination$iv$iv;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            String[] args;
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
            Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)((CharSequence)s), (String[])new String[]{" "}, (boolean)false, (int)0, (int)6, null);
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            if (thisCollection$iv.toArray(new String[0]) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            if (args.length <= 1) {
                ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cSyntax error at line '" + index + "' in setting script.\n\u00a78\u00a7lLine: \u00a77" + (String)s);
                continue;
            }
            switch (args[0]) {
                case "chat": {
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7e" + ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
                    break;
                }
                case "unchat": {
                    ClientUtils.displayChatMessage(ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)));
                    break;
                }
                case "load": {
                    Object object;
                    String string;
                    String urlRaw = StringUtils.toCompleteString(args, 1);
                    if (StringsKt.startsWith$default((String)urlRaw, (String)"http", (boolean)false, (int)2, null)) {
                        string = urlRaw;
                    } else {
                        String string2 = urlRaw;
                        object = new StringBuilder().append("https://cloud.liquidbounce.net/LiquidBounce/settings/");
                        boolean bl5 = false;
                        String string3 = string2.toLowerCase();
                        string = ((StringBuilder)object).append(string3).toString();
                    }
                    String url = string;
                    try {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a77Loading settings from \u00a7a\u00a7l" + url + "\u00a77...");
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
                    EntityUtils.targetPlayer = StringsKt.equals((String)args[1], (String)"true", (boolean)true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetPlayer + "\u00a77.");
                    break;
                }
                case "targetMobs": {
                    EntityUtils.targetMobs = StringsKt.equals((String)args[1], (String)"true", (boolean)true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetMobs + "\u00a77.");
                    break;
                }
                case "targetAnimals": {
                    EntityUtils.targetAnimals = StringsKt.equals((String)args[1], (String)"true", (boolean)true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetAnimals + "\u00a77.");
                    break;
                }
                case "targetInvisible": {
                    EntityUtils.targetInvisible = StringsKt.equals((String)args[1], (String)"true", (boolean)true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetInvisible + "\u00a77.");
                    break;
                }
                case "targetDead": {
                    EntityUtils.targetDead = StringsKt.equals((String)args[1], (String)"true", (boolean)true);
                    ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + args[0] + "\u00a77 set to \u00a7c\u00a7l" + EntityUtils.targetDead + "\u00a77.");
                    break;
                }
                default: {
                    Object object;
                    if (args.length != 3) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cSyntax error at line '" + index + "' in setting script.\n\u00a78\u00a7lLine: \u00a77" + (String)s);
                        break;
                    }
                    String moduleName = args[0];
                    String valueName = args[1];
                    String value = args[2];
                    Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(moduleName);
                    if (module == null) {
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7cModule \u00a7a\u00a7l" + moduleName + "\u00a7c was not found!");
                        break;
                    }
                    if (StringsKt.equals((String)valueName, (String)"toggle", (boolean)true)) {
                        module.setState(StringsKt.equals((String)value, (String)"true", (boolean)true));
                        ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lAutoSettings\u00a77] \u00a7a\u00a7l" + module.getName() + " \u00a77was toggled \u00a7c\u00a7l" + (module.getState() ? "on" : "off") + "\u00a77.");
                        break;
                    }
                    if (StringsKt.equals((String)valueName, (String)"bind", (boolean)true)) {
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
                        boolean bl6;
                        String string;
                        Value<?> value2 = moduleValue;
                        if (value2 instanceof BoolValue) {
                            string = value;
                            object = (BoolValue)moduleValue;
                            bl6 = false;
                            boolean bl7 = Boolean.parseBoolean(string);
                            ((Value)object).changeValue(bl7);
                        } else if (value2 instanceof FloatValue) {
                            string = value;
                            object = (FloatValue)moduleValue;
                            bl6 = false;
                            float f = Float.parseFloat(string);
                            ((Value)object).changeValue(Float.valueOf(f));
                        } else if (value2 instanceof IntegerValue) {
                            string = value;
                            object = (IntegerValue)moduleValue;
                            bl6 = false;
                            int n3 = Integer.parseInt(string);
                            ((Value)object).changeValue(n3);
                        } else if (value2 instanceof TextValue) {
                            ((TextValue)moduleValue).changeValue(value);
                        } else if (value2 instanceof ListValue) {
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
    public final String generateScript(boolean values, boolean binds, boolean states) {
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
            if (values) {
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
        return stringBuilder.toString();
    }

    private SettingsUtils() {
    }

    static {
        SettingsUtils settingsUtils;
        INSTANCE = settingsUtils = new SettingsUtils();
    }
}

