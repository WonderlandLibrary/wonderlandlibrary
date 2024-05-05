/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;

public final class ModuleCommand
extends Command {
    private final Module module;
    private final List<Value<?>> values;

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(String[] args) {
        void $this$filterTo$iv$iv2;
        Iterable $this$filter$iv = this.values;
        boolean $i$f$filter22 = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv2 = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv2) {
            Value it = (Value)element$iv$iv;
            boolean bl = false;
            if (!(!(it instanceof FontValue))) continue;
            destination$iv$iv2.add(element$iv$iv);
        }
        String valueNames2 = CollectionsKt.joinToString$default((Iterable)((List)destination$iv$iv2), (CharSequence)"/", null, null, (int)0, null, (Function1)execute.valueNames.2.INSTANCE, (int)30, null);
        String $i$f$filter22 = this.module.getName();
        boolean $this$filterTo$iv$iv2 = false;
        String string = $i$f$filter22;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String moduleName = string.toLowerCase();
        if (args.length < 2) {
            this.chatSyntax(this.values.size() == 1 ? moduleName + ' ' + valueNames2 + " <value>" : moduleName + " <" + valueNames2 + '>');
            return;
        }
        Value<?> value = this.module.getValue(args[1]);
        if (value == null) {
            this.chatSyntax(moduleName + " <" + valueNames2 + '>');
            return;
        }
        if (value instanceof BoolValue) {
            boolean newValue = (Boolean)((BoolValue)value).get() == false;
            ((BoolValue)value).set(newValue);
            this.chat("\u00a77" + this.module.getName() + " \u00a78" + args[1] + "\u00a77 was toggled " + (newValue ? "\u00a78on\u00a77" : "\u00a78off\u00a77."));
            this.playEdit();
        } else {
            if (args.length < 3) {
                if (value instanceof IntegerValue || value instanceof FloatValue || value instanceof TextValue) {
                    String newValue = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean destination$iv$iv2 = false;
                    String string2 = newValue;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = string2.toLowerCase();
                    moduleCommand.chatSyntax(stringBuilder.append(string3).append(" <value>").toString());
                } else if (value instanceof ListValue) {
                    String newValue = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean destination$iv$iv2 = false;
                    String string4 = newValue;
                    if (string4 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string5 = string4.toLowerCase();
                    newValue = ArraysKt.joinToString$default((Object[])((ListValue)value).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                    stringBuilder = stringBuilder.append(string5).append(" <");
                    destination$iv$iv2 = false;
                    String string6 = newValue;
                    if (string6 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string5 = string6.toLowerCase();
                    moduleCommand.chatSyntax(stringBuilder.append(string5).append('>').toString());
                }
                return;
            }
            try {
                String string7;
                int n;
                Value<?> newValue = value;
                if (newValue instanceof BlockValue) {
                    int id = 0;
                    try {
                        String string8 = args[2];
                        boolean bl = false;
                        n = Integer.parseInt(string8);
                    }
                    catch (NumberFormatException exception) {
                        Integer tmpId;
                        Integer n2;
                        IBlock iBlock = MinecraftInstance.functions.getBlockFromName(args[2]);
                        if (iBlock != null) {
                            IBlock iBlock2 = iBlock;
                            boolean bl = false;
                            boolean bl2 = false;
                            IBlock it = iBlock2;
                            boolean bl3 = false;
                            n2 = MinecraftInstance.functions.getIdFromBlock(it);
                        } else {
                            n2 = tmpId = null;
                        }
                        if (tmpId == null || tmpId <= 0) {
                            this.chat("\u00a77Block \u00a78" + args[2] + "\u00a77 does not exist!");
                            return;
                        }
                        n = tmpId;
                    }
                    id = n;
                    ((BlockValue)value).set(id);
                    String string9 = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append(this.module.getName()).append(" \u00a78");
                    ModuleCommand moduleCommand = this;
                    boolean bl = false;
                    String string10 = string9;
                    if (string10 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string11 = string10.toLowerCase();
                    moduleCommand.chat(stringBuilder.append(string11).append("\u00a77 was set to \u00a78").append(BlockUtils.getBlockName(id)).append("\u00a77.").toString());
                    this.playEdit();
                    return;
                }
                if (newValue instanceof IntegerValue) {
                    string7 = args[2];
                    IntegerValue integerValue = (IntegerValue)value;
                    n = 0;
                    int n3 = Integer.parseInt(string7);
                    integerValue.set(n3);
                } else if (newValue instanceof FloatValue) {
                    string7 = args[2];
                    FloatValue floatValue = (FloatValue)value;
                    n = 0;
                    float f = Float.parseFloat(string7);
                    floatValue.set(Float.valueOf(f));
                } else if (newValue instanceof ListValue) {
                    if (!((ListValue)value).contains(args[2])) {
                        string7 = args[1];
                        StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                        ModuleCommand moduleCommand = this;
                        n = 0;
                        String string12 = string7;
                        if (string12 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string13 = string12.toLowerCase();
                        string7 = ArraysKt.joinToString$default((Object[])((ListValue)value).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                        stringBuilder = stringBuilder.append(string13).append(" <");
                        n = 0;
                        String string14 = string7;
                        if (string14 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        string13 = string14.toLowerCase();
                        moduleCommand.chatSyntax(stringBuilder.append(string13).append('>').toString());
                        return;
                    }
                    ((ListValue)value).set(args[2]);
                } else if (newValue instanceof TextValue) {
                    ((TextValue)value).set(StringUtils.toCompleteString(args, 2));
                }
                this.chat("\u00a77" + this.module.getName() + " \u00a78" + args[1] + "\u00a77 was set to \u00a78" + value.get() + "\u00a77.");
                this.playEdit();
            }
            catch (NumberFormatException e) {
                this.chat("\u00a78" + args[2] + "\u00a77 cannot be converted to number!");
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        List list;
        Object object = args;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$mapTo$iv$iv;
                Value it;
                Iterable $this$filterTo$iv$iv;
                Iterable $this$filter$iv = this.values;
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (Value)element$iv$iv;
                    boolean bl2 = false;
                    if (!(!(it instanceof FontValue) && StringsKt.startsWith((String)it.getName(), (String)args[0], (boolean)true))) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$map$iv = (List)destination$iv$iv;
                boolean $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    it = (Value)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl3 = false;
                    String string = it.getName();
                    boolean bl4 = false;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = string2.toLowerCase();
                    collection.add(string3);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                object = this.module.getValue(args[0]);
                if (object instanceof BlockValue) {
                    void $this$filterTo$iv$iv;
                    String it;
                    Iterable $this$mapTo$iv$iv;
                    Iterable $this$map$iv = MinecraftInstance.functions.getItemRegistryKeys();
                    boolean $i$f$map = false;
                    Iterable destination$iv$iv = $this$map$iv;
                    Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                    boolean $i$f$mapTo = false;
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        IResourceLocation bl3 = (IResourceLocation)item$iv$iv;
                        Collection collection = destination$iv$iv2;
                        boolean bl5 = false;
                        String string = it.getResourcePath();
                        boolean bl6 = false;
                        String string4 = string;
                        if (string4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string5 = string4.toLowerCase();
                        collection.add(string5);
                    }
                    Iterable $this$filter$iv = (List)destination$iv$iv2;
                    boolean $i$f$filter = false;
                    $this$mapTo$iv$iv = $this$filter$iv;
                    destination$iv$iv2 = new ArrayList();
                    boolean $i$f$filterTo = false;
                    for (Object element$iv$iv : $this$filterTo$iv$iv) {
                        it = (String)element$iv$iv;
                        boolean bl7 = false;
                        if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
                        destination$iv$iv2.add(element$iv$iv);
                    }
                    return (List)destination$iv$iv2;
                }
                if (object instanceof ListValue) {
                    Iterable $this$forEach$iv = this.values;
                    boolean $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        void $this$filterTo$iv$iv;
                        Value value = (Value)element$iv;
                        boolean bl8 = false;
                        if (!StringsKt.equals((String)value.getName(), (String)args[0], (boolean)true) || !(value instanceof ListValue)) continue;
                        String[] $this$filter$iv = ((ListValue)value).getValues();
                        boolean $i$f$filter = false;
                        String[] bl7 = $this$filter$iv;
                        Collection destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        void var14_39 = $this$filterTo$iv$iv;
                        int n = ((void)var14_39).length;
                        for (int i = 0; i < n; ++i) {
                            void element$iv$iv;
                            void it = element$iv$iv = var14_39[i];
                            boolean bl9 = false;
                            if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv;
                    }
                    return CollectionsKt.emptyList();
                }
                list = CollectionsKt.emptyList();
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public final Module getModule() {
        return this.module;
    }

    public final List<Value<?>> getValues() {
        return this.values;
    }

    public ModuleCommand(Module module, List<? extends Value<?>> values) {
        String string = module.getName();
        ModuleCommand moduleCommand = this;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        super(string3, new String[0]);
        this.module = module;
        this.values = values;
        if (this.values.isEmpty()) {
            throw (Throwable)new IllegalArgumentException("Values are empty!");
        }
    }

    public /* synthetic */ ModuleCommand(Module module, List list, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            list = module.getValues();
        }
        this(module, list);
    }
}

