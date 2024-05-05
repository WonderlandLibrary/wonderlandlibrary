/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.RegistryNamespacedDefaultedByKey
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
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
import net.minecraft.block.Block;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\b\u0002\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u001b\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001b\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "module", "Lnet/ccbluex/liquidbounce/features/module/Module;", "values", "", "Lnet/ccbluex/liquidbounce/value/Value;", "(Lnet/ccbluex/liquidbounce/features/module/Module;Ljava/util/List;)V", "getModule", "()Lnet/ccbluex/liquidbounce/features/module/Module;", "getValues", "()Ljava/util/List;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "([Ljava/lang/String;)Ljava/util/List;", "KyinoClient"})
public final class ModuleCommand
extends Command {
    @NotNull
    private final Module module;
    @NotNull
    private final List<Value<?>> values;

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args2) {
        void $this$filterTo$iv$iv2;
        Intrinsics.checkParameterIsNotNull(args2, "args");
        Iterable $this$filter$iv = this.values;
        boolean $i$f$filter22 = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv2 = new ArrayList();
        boolean $i$f$filterTo2 = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv2) {
            Value it = (Value)element$iv$iv;
            boolean bl = false;
            if (!(!(it instanceof FontValue))) continue;
            destination$iv$iv2.add(element$iv$iv);
        }
        String valueNames2 = CollectionsKt.joinToString$default((List)destination$iv$iv2, "/", null, null, 0, null, execute.valueNames.2.INSTANCE, 30, null);
        String $i$f$filter22 = this.module.getName();
        boolean $this$filterTo$iv$iv2 = false;
        String string = $i$f$filter22;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string2 = string.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
        String moduleName = string2;
        if (args2.length < 2) {
            this.chatSyntax(this.values.size() == 1 ? moduleName + ' ' + valueNames2 + " <value>" : moduleName + " <" + valueNames2 + '>');
            return;
        }
        Value<?> value = this.module.getValue(args2[1]);
        if (value == null) {
            this.chatSyntax(moduleName + " <" + valueNames2 + '>');
            return;
        }
        if (value instanceof BoolValue) {
            boolean newValue = (Boolean)((BoolValue)value).get() == false;
            ((BoolValue)value).set(newValue);
            this.chat("\u00a77" + this.module.getName() + " \u00a78" + args2[1] + "\u00a77 was toggled " + (newValue ? "\u00a78on\u00a77" : "\u00a78off\u00a77."));
            this.playEdit();
        } else {
            if (args2.length < 3) {
                if (value instanceof IntegerValue || value instanceof FloatValue || value instanceof TextValue) {
                    String newValue = args2[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean destination$iv$iv2 = false;
                    String string3 = newValue;
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string4 = string3.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
                    String string5 = string4;
                    moduleCommand.chatSyntax(stringBuilder.append(string5).append(" <value>").toString());
                } else if (value instanceof ListValue) {
                    String newValue = args2[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean destination$iv$iv2 = false;
                    String string6 = newValue;
                    if (string6 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string7 = string6.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string7, "(this as java.lang.String).toLowerCase()");
                    String string8 = string7;
                    newValue = ArraysKt.joinToString$default(((ListValue)value).getValues(), (CharSequence)"/", null, null, 0, null, null, 62, null);
                    stringBuilder = stringBuilder.append(string8).append(" <");
                    destination$iv$iv2 = false;
                    String string9 = newValue;
                    if (string9 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string10 = string9.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string10, "(this as java.lang.String).toLowerCase()");
                    string8 = string10;
                    moduleCommand.chatSyntax(stringBuilder.append(string8).append('>').toString());
                }
                return;
            }
            try {
                boolean bl;
                String string11;
                Value<?> newValue = value;
                if (newValue instanceof BlockValue) {
                    int id;
                    block29: {
                        id = 0;
                        try {
                            String $i$f$filterTo2 = args2[2];
                            boolean bl2 = false;
                            id = Integer.parseInt($i$f$filterTo2);
                        }
                        catch (NumberFormatException exception) {
                            id = Block.func_149682_b((Block)Block.func_149684_b((String)args2[2]));
                            if (id > 0) break block29;
                            this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                            return;
                        }
                    }
                    ((BlockValue)value).set(id);
                    String string12 = args2[1];
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append(this.module.getName()).append(" \u00a78");
                    ModuleCommand moduleCommand = this;
                    boolean bl3 = false;
                    String string13 = string12;
                    if (string13 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string14 = string13.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string14, "(this as java.lang.String).toLowerCase()");
                    String string15 = string14;
                    moduleCommand.chat(stringBuilder.append(string15).append("\u00a77 was set to \u00a78").append(BlockUtils.getBlockName(id)).append("\u00a77.").toString());
                    this.playEdit();
                    return;
                }
                if (newValue instanceof IntegerValue) {
                    string11 = args2[2];
                    IntegerValue integerValue = (IntegerValue)value;
                    bl = false;
                    int n = Integer.parseInt(string11);
                    integerValue.set(n);
                } else if (newValue instanceof FloatValue) {
                    string11 = args2[2];
                    FloatValue floatValue = (FloatValue)value;
                    bl = false;
                    float f = Float.parseFloat(string11);
                    floatValue.set(Float.valueOf(f));
                } else if (newValue instanceof ListValue) {
                    if (!((ListValue)value).contains(args2[2])) {
                        string11 = args2[1];
                        StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                        ModuleCommand moduleCommand = this;
                        bl = false;
                        String string16 = string11;
                        if (string16 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string17 = string16.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string17, "(this as java.lang.String).toLowerCase()");
                        String string18 = string17;
                        string11 = ArraysKt.joinToString$default(((ListValue)value).getValues(), (CharSequence)"/", null, null, 0, null, null, 62, null);
                        stringBuilder = stringBuilder.append(string18).append(" <");
                        bl = false;
                        String string19 = string11;
                        if (string19 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string20 = string19.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string20, "(this as java.lang.String).toLowerCase()");
                        string18 = string20;
                        moduleCommand.chatSyntax(stringBuilder.append(string18).append('>').toString());
                        return;
                    }
                    ((ListValue)value).set(args2[2]);
                } else if (newValue instanceof TextValue) {
                    TextValue textValue = (TextValue)value;
                    String string21 = StringUtils.toCompleteString(args2, 2);
                    Intrinsics.checkExpressionValueIsNotNull(string21, "StringUtils.toCompleteString(args, 2)");
                    textValue.set(string21);
                }
                this.chat("\u00a77" + this.module.getName() + " \u00a78" + args2[1] + "\u00a77 was set to \u00a78" + value.get() + "\u00a77.");
                this.playEdit();
            }
            catch (NumberFormatException e) {
                this.chat("\u00a78" + args2[2] + "\u00a77 cannot be converted to number!");
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        List<String> list;
        Intrinsics.checkParameterIsNotNull(args2, "args");
        Object object = args2;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args2.length) {
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
                    if (!(!(it instanceof FontValue) && StringsKt.startsWith(it.getName(), args2[0], true))) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$map$iv = (List)destination$iv$iv;
                boolean $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    String string;
                    it = (Value)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl3 = false;
                    String string2 = it.getName();
                    boolean bl4 = false;
                    String string3 = string2;
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                object = this.module.getValue(args2[0]);
                if (object instanceof BlockValue) {
                    void $this$filterTo$iv$iv;
                    String it;
                    Iterable $this$mapTo$iv$iv;
                    RegistryNamespacedDefaultedByKey registryNamespacedDefaultedByKey = Block.field_149771_c;
                    Intrinsics.checkExpressionValueIsNotNull(registryNamespacedDefaultedByKey, "Block.blockRegistry");
                    Set set = registryNamespacedDefaultedByKey.func_148742_b();
                    Intrinsics.checkExpressionValueIsNotNull(set, "Block.blockRegistry.keys");
                    Iterable $this$map$iv = set;
                    boolean $i$f$map = false;
                    Iterable destination$iv$iv = $this$map$iv;
                    Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                    boolean $i$f$mapTo = false;
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        String string;
                        String string4;
                        ResourceLocation bl3 = (ResourceLocation)item$iv$iv;
                        Collection collection = destination$iv$iv2;
                        boolean bl5 = false;
                        void v4 = it;
                        Intrinsics.checkExpressionValueIsNotNull(v4, "it");
                        Intrinsics.checkExpressionValueIsNotNull(v4.func_110623_a(), "it.resourcePath");
                        boolean bl6 = false;
                        String string5 = string4;
                        if (string5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull(string5.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                        collection.add(string);
                    }
                    Iterable $this$filter$iv = (List)destination$iv$iv2;
                    boolean $i$f$filter = false;
                    $this$mapTo$iv$iv = $this$filter$iv;
                    destination$iv$iv2 = new ArrayList();
                    boolean $i$f$filterTo = false;
                    for (Object element$iv$iv : $this$filterTo$iv$iv) {
                        it = (String)element$iv$iv;
                        boolean bl7 = false;
                        if (!StringsKt.startsWith(it, args2[1], true)) continue;
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
                        if (!StringsKt.equals(value.getName(), args2[0], true) || !(value instanceof ListValue)) continue;
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
                            if (!StringsKt.startsWith((String)it, args2[1], true)) continue;
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

    @NotNull
    public final Module getModule() {
        return this.module;
    }

    @NotNull
    public final List<Value<?>> getValues() {
        return this.values;
    }

    public ModuleCommand(@NotNull Module module, @NotNull List<? extends Value<?>> values2) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        Intrinsics.checkParameterIsNotNull(values2, "values");
        String string = module.getName();
        ModuleCommand moduleCommand = this;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        String string4 = string3;
        super(string4, new String[0]);
        this.module = module;
        this.values = values2;
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

