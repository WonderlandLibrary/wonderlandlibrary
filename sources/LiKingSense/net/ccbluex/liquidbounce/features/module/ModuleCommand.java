/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\b\u0002\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u001b\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001b\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "module", "Lnet/ccbluex/liquidbounce/features/module/Module;", "values", "", "Lnet/ccbluex/liquidbounce/value/Value;", "(Lnet/ccbluex/liquidbounce/features/module/Module;Ljava/util/List;)V", "getModule", "()Lnet/ccbluex/liquidbounce/features/module/Module;", "getValues", "()Ljava/util/List;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "([Ljava/lang/String;)Ljava/util/List;", "LiKingSense"})
public final class ModuleCommand
extends Command {
    @NotNull
    public final Module module;
    @NotNull
    public final List<Value<?>> values;

    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(@NotNull String[] args) {
        String $i$f$filter;
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv;
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        Iterable iterable = $this$filter$iv = (Iterable)this.values;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Value it = (Value)element$iv$iv;
            if ((!(it instanceof FontValue) ? 1 : 0) == 0) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        String valueNames2 = CollectionsKt.joinToString$default((Iterable)((List)destination$iv$iv), (CharSequence)"/", null, null, (int)0, null, (Function1)execute.valueNames.2.INSTANCE, (int)30, null);
        String string = $i$f$filter = this.module.getName();
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string2 = string.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"(this as java.lang.String).toLowerCase()");
        String moduleName = string2;
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
            Object newValue;
            if (args.length < 3) {
                if (value instanceof IntegerValue || value instanceof FloatValue || value instanceof TextValue) {
                    newValue = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    Object object = newValue;
                    if (object == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = ((String)object).toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
                    String string4 = string3;
                    moduleCommand.chatSyntax(stringBuilder.append(string4).append(" <value>").toString());
                } else if (value instanceof ListValue) {
                    newValue = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    ModuleCommand moduleCommand = this;
                    Object object = newValue;
                    if (object == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string5 = ((String)object).toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string5, (String)"(this as java.lang.String).toLowerCase()");
                    String string6 = string5;
                    newValue = ArraysKt.joinToString$default((Object[])((ListValue)value).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                    stringBuilder = stringBuilder.append(string6).append(" <");
                    Object object2 = newValue;
                    if (object2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string7 = ((String)object2).toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string7, (String)"(this as java.lang.String).toLowerCase()");
                    string6 = string7;
                    moduleCommand.chatSyntax(stringBuilder.append(string6).append('>').toString());
                }
                return;
            }
            try {
                String string8;
                newValue = value;
                if (newValue instanceof BlockValue) {
                    int n;
                    try {
                        String string9 = args[2];
                        n = Integer.parseInt(string9);
                    }
                    catch (NumberFormatException exception) {
                        Integer tmpId;
                        Integer n2;
                        IBlock iBlock = ModuleCommand.access$getFunctions$p$s1046033730().getBlockFromName(args[2]);
                        if (iBlock != null) {
                            IBlock iBlock2;
                            IBlock it = iBlock2 = iBlock;
                            n2 = ModuleCommand.access$getFunctions$p$s1046033730().getIdFromBlock(it);
                        } else {
                            n2 = tmpId = null;
                        }
                        if (tmpId == null || tmpId <= 0) {
                            this.chat("\u00a77Block \u00a78" + args[2] + "\u00a77 does not exist!");
                            return;
                        }
                        n = tmpId;
                    }
                    int id = n;
                    ((BlockValue)value).set(id);
                    String string10 = args[1];
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append(this.module.getName()).append(" \u00a78");
                    ModuleCommand moduleCommand = this;
                    String string11 = string10;
                    if (string11 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string12 = string11.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull((Object)string12, (String)"(this as java.lang.String).toLowerCase()");
                    String string13 = string12;
                    moduleCommand.chat(stringBuilder.append(string13).append("\u00a77 was set to \u00a78").append(BlockUtils.getBlockName(id)).append("\u00a77.").toString());
                    this.playEdit();
                    return;
                }
                if (newValue instanceof IntegerValue) {
                    string8 = args[2];
                    IntegerValue integerValue = (IntegerValue)value;
                    int n = Integer.parseInt(string8);
                    integerValue.set(n);
                } else if (newValue instanceof FloatValue) {
                    string8 = args[2];
                    FloatValue floatValue = (FloatValue)value;
                    float f = Float.parseFloat(string8);
                    floatValue.set(Float.valueOf(f));
                } else if (newValue instanceof ListValue) {
                    if (!((ListValue)value).contains(args[2])) {
                        string8 = args[1];
                        StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                        ModuleCommand moduleCommand = this;
                        String string14 = string8;
                        if (string14 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string15 = string14.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull((Object)string15, (String)"(this as java.lang.String).toLowerCase()");
                        String string16 = string15;
                        string8 = ArraysKt.joinToString$default((Object[])((ListValue)value).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                        stringBuilder = stringBuilder.append(string16).append(" <");
                        String string17 = string8;
                        if (string17 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string18 = string17.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull((Object)string18, (String)"(this as java.lang.String).toLowerCase()");
                        string16 = string18;
                        moduleCommand.chatSyntax(stringBuilder.append(string16).append('>').toString());
                        return;
                    }
                    ((ListValue)value).set(args[2]);
                } else if (newValue instanceof TextValue) {
                    TextValue textValue = (TextValue)value;
                    String string19 = StringUtils.toCompleteString(args, 2);
                    Intrinsics.checkExpressionValueIsNotNull((Object)string19, (String)"StringUtils.toCompleteString(args, 2)");
                    textValue.set(string19);
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
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        Object object = args;
        if ((((String[])object).length == 0 ? 1 : 0) != 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$mapTo$iv$iv;
                Iterable $this$map$iv;
                Value it;
                Iterable $this$filterTo$iv$iv;
                Iterable $this$filter$iv;
                Iterable iterable = $this$filter$iv = (Iterable)this.values;
                Collection destination$iv$iv = new ArrayList();
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (Value)element$iv$iv;
                    if ((!(it instanceof FontValue) && StringsKt.startsWith((String)it.getName(), (String)args[0], (boolean)true) ? 1 : 0) == 0) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                $this$filterTo$iv$iv = $this$map$iv = (Iterable)((List)destination$iv$iv);
                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    String string;
                    String string2;
                    it = (Value)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    String string3 = string2 = it.getName();
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkExpressionValueIsNotNull((Object)string3.toLowerCase(), (String)"(this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                object = this.module.getValue(args[0]);
                if (object instanceof BlockValue) {
                    void $this$filterTo$iv$iv;
                    Iterable $this$filter$iv;
                    String it;
                    Iterable $this$mapTo$iv$iv;
                    Iterable $this$map$iv;
                    Iterable destination$iv$iv = $this$map$iv = (Iterable)ModuleCommand.access$getFunctions$p$s1046033730().getItemRegistryKeys();
                    Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        String string;
                        String string4;
                        IResourceLocation iResourceLocation = (IResourceLocation)item$iv$iv;
                        Collection collection = destination$iv$iv2;
                        String string5 = string4 = it.getResourcePath();
                        if (string5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull((Object)string5.toLowerCase(), (String)"(this as java.lang.String).toLowerCase()");
                        collection.add(string);
                    }
                    $this$mapTo$iv$iv = $this$filter$iv = (Iterable)((List)destination$iv$iv2);
                    destination$iv$iv2 = new ArrayList();
                    for (Object element$iv$iv : $this$filterTo$iv$iv) {
                        it = (String)element$iv$iv;
                        if (!StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) continue;
                        destination$iv$iv2.add(element$iv$iv);
                    }
                    return (List)destination$iv$iv2;
                }
                if (object instanceof ListValue) {
                    Iterable $this$forEach$iv = this.values;
                    for (Object element$iv : $this$forEach$iv) {
                        void var16_29;
                        void $this$filterTo$iv$iv;
                        String[] $this$filter$iv;
                        Value value = (Value)element$iv;
                        if (!StringsKt.equals((String)value.getName(), (String)args[0], (boolean)true) || !(value instanceof ListValue)) continue;
                        String[] stringArray = $this$filter$iv = ((ListValue)value).getValues();
                        Collection destination$iv$iv = new ArrayList();
                        void var14_27 = $this$filterTo$iv$iv;
                        int n = ((void)var14_27).length;
                        while (var16_29 < n) {
                            void element$iv$iv = var14_27[var16_29];
                            void it = element$iv$iv;
                            if (StringsKt.startsWith((String)it, (String)args[1], (boolean)true)) {
                                destination$iv$iv.add(element$iv$iv);
                            }
                            ++var16_29;
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

    public ModuleCommand(@NotNull Module module, @NotNull List<? extends Value<?>> values) {
        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
        Intrinsics.checkParameterIsNotNull(values, (String)"values");
        String string = module.getName();
        ModuleCommand moduleCommand = this;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        String string4 = string3;
        super(string4, new String[0]);
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

    public static final /* synthetic */ IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }
}

