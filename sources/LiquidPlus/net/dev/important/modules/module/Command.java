/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.modules.module.Command;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.misc.StringUtils;
import net.dev.important.value.BlockValue;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.TextValue;
import net.dev.important.value.Value;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\b\u0002\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u001b\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016\u00a2\u0006\u0002\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001b\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0014"}, d2={"Lnet/dev/important/modules/module/Command;", "Lnet/dev/important/modules/command/Command;", "module", "Lnet/dev/important/modules/module/Module;", "values", "", "Lnet/dev/important/value/Value;", "(Lnet/dev/important/modules/module/Module;Ljava/util/List;)V", "getModule", "()Lnet/dev/important/modules/module/Module;", "getValues", "()Ljava/util/List;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "([Ljava/lang/String;)Ljava/util/List;", "LiquidBounce"})
public final class Command
extends net.dev.important.modules.command.Command {
    @NotNull
    private final Module module;
    @NotNull
    private final List<Value<?>> values;

    public Command(@NotNull Module module2, @NotNull List<? extends Value<?>> values2) {
        Intrinsics.checkNotNullParameter(module2, "module");
        Intrinsics.checkNotNullParameter(values2, "values");
        String string = module2.getName().toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        boolean $i$f$emptyArray = false;
        super(string, new String[0]);
        this.module = module2;
        this.values = values2;
        if (this.values.isEmpty()) {
            throw new IllegalArgumentException("Values are empty!");
        }
    }

    public /* synthetic */ Command(Module module2, List list, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            list = module2.getValues();
        }
        this(module2, list);
    }

    @NotNull
    public final Module getModule() {
        return this.module;
    }

    @NotNull
    public final List<Value<?>> getValues() {
        return this.values;
    }

    @Override
    public void execute(@NotNull String[] args2) {
        String $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter(args2, "args");
        Iterable $this$filter$iv = this.values;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Object destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        Object object = $this$filterTo$iv$iv.iterator();
        while (object.hasNext()) {
            Object element$iv$iv = object.next();
            Value it = (Value)element$iv$iv;
            boolean bl = false;
            if (!(!(it instanceof FontValue))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        String valueNames2 = CollectionsKt.joinToString$default((List)destination$iv$iv, "/", null, null, 0, null, execute.valueNames.2.INSTANCE, 30, null);
        $this$filterTo$iv$iv = this.module.getName().toLowerCase();
        Intrinsics.checkNotNullExpressionValue($this$filterTo$iv$iv, "this as java.lang.String).toLowerCase()");
        String moduleName = $this$filterTo$iv$iv;
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
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    destination$iv$iv = args2[1].toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(destination$iv$iv, "this as java.lang.String).toLowerCase()");
                    this.chatSyntax(stringBuilder.append((String)destination$iv$iv).append(" <value>").toString());
                } else if (value instanceof ListValue) {
                    StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                    destination$iv$iv = args2[1].toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(destination$iv$iv, "this as java.lang.String).toLowerCase()");
                    StringBuilder stringBuilder2 = stringBuilder.append((String)destination$iv$iv).append(" <");
                    destination$iv$iv = ArraysKt.joinToString$default(((ListValue)value).getValues(), (CharSequence)"/", null, null, 0, null, null, 62, null).toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(destination$iv$iv, "this as java.lang.String).toLowerCase()");
                    this.chatSyntax(stringBuilder2.append((String)destination$iv$iv).append('>').toString());
                }
                return;
            }
            try {
                Value<?> newValue = value;
                if (newValue instanceof BlockValue) {
                    int id;
                    block22: {
                        id = 0;
                        try {
                            id = Integer.parseInt(args2[2]);
                        }
                        catch (NumberFormatException exception) {
                            id = Block.func_149682_b((Block)Block.func_149684_b((String)args2[2]));
                            if (id > 0) break block22;
                            this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                            return;
                        }
                    }
                    ((BlockValue)value).set(id);
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append(this.module.getName()).append(" \u00a78");
                    object = args2[1].toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String).toLowerCase()");
                    this.chat(stringBuilder.append((String)object).append("\u00a77 was set to \u00a78").append(BlockUtils.getBlockName(id)).append("\u00a77.").toString());
                    this.playEdit();
                    return;
                }
                if (newValue instanceof IntegerValue) {
                    ((IntegerValue)value).set(Integer.parseInt(args2[2]));
                } else if (newValue instanceof FloatValue) {
                    ((FloatValue)value).set(Float.valueOf(Float.parseFloat(args2[2])));
                } else if (newValue instanceof ListValue) {
                    if (!((ListValue)value).contains(args2[2])) {
                        StringBuilder stringBuilder = new StringBuilder().append(moduleName).append(' ');
                        String string = args2[1].toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                        StringBuilder stringBuilder3 = stringBuilder.append(string).append(" <");
                        string = ArraysKt.joinToString$default(((ListValue)value).getValues(), (CharSequence)"/", null, null, 0, null, null, 62, null).toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                        this.chatSyntax(stringBuilder3.append(string).append('>').toString());
                        return;
                    }
                    ((ListValue)value).set(args2[2]);
                } else if (newValue instanceof TextValue) {
                    TextValue textValue = (TextValue)value;
                    String string = StringUtils.toCompleteString(args2, 2);
                    Intrinsics.checkNotNullExpressionValue(string, "toCompleteString(args, 2)");
                    textValue.set(string);
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
        Intrinsics.checkNotNullParameter(args2, "args");
        if (args2.length == 0) {
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
                    boolean bl = false;
                    if (!(!(it instanceof FontValue) && StringsKt.startsWith(it.getName(), args2[0], true))) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$map$iv = (List)destination$iv$iv;
                boolean $i$f$map = false;
                $this$filterTo$iv$iv = $this$map$iv;
                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    it = (Value)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl = false;
                    String string = it.getName().toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                Value<?> value = this.module.getValue(args2[0]);
                if (value instanceof BlockValue) {
                    void $this$filterTo$iv$iv;
                    String it;
                    Iterable $this$mapTo$iv$iv;
                    Set $i$f$map = Block.field_149771_c.func_148742_b();
                    Intrinsics.checkNotNullExpressionValue($i$f$map, "blockRegistry.keys");
                    Iterable $this$map$iv = $i$f$map;
                    boolean $i$f$map2 = false;
                    Iterable destination$iv$iv = $this$map$iv;
                    Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                    boolean $i$f$mapTo = false;
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        ResourceLocation bl = (ResourceLocation)item$iv$iv;
                        Collection collection = destination$iv$iv2;
                        boolean bl2 = false;
                        String string = it.func_110623_a();
                        Intrinsics.checkNotNullExpressionValue(string, "it.resourcePath");
                        String string2 = string.toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toLowerCase()");
                        collection.add(string2);
                    }
                    Iterable $this$filter$iv = (List)destination$iv$iv2;
                    boolean $i$f$filter = false;
                    $this$mapTo$iv$iv = $this$filter$iv;
                    destination$iv$iv2 = new ArrayList();
                    boolean $i$f$filterTo = false;
                    for (Object element$iv$iv : $this$filterTo$iv$iv) {
                        it = (String)element$iv$iv;
                        boolean bl3 = false;
                        if (!StringsKt.startsWith(it, args2[1], true)) continue;
                        destination$iv$iv2.add(element$iv$iv);
                    }
                    return (List)destination$iv$iv2;
                }
                if (value instanceof ListValue) {
                    Iterable $this$forEach$iv = this.values;
                    boolean $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        void $this$filterTo$iv$iv;
                        Value value2 = (Value)element$iv;
                        boolean bl = false;
                        if (!StringsKt.equals(value2.getName(), args2[0], true) || !(value2 instanceof ListValue)) continue;
                        String[] $this$filter$iv = ((ListValue)value2).getValues();
                        boolean $i$f$filter = false;
                        String[] bl3 = $this$filter$iv;
                        Collection destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (void element$iv$iv : $this$filterTo$iv$iv) {
                            void it = element$iv$iv;
                            boolean bl4 = false;
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
}

