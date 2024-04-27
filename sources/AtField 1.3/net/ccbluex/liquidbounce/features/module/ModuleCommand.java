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
import java.util.Iterator;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
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

public final class ModuleCommand
extends Command {
    private final Module module;
    private final List values;

    public ModuleCommand(Module module, List list, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            list = module.getValues();
        }
        this(module, list);
    }

    public final Module getModule() {
        return this.module;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public final List getValues() {
        return this.values;
    }

    public ModuleCommand(Module module, List list) {
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
        this.values = list;
        if (this.values.isEmpty()) {
            throw (Throwable)new IllegalArgumentException("Values are empty!");
        }
    }

    @Override
    public void execute(String[] stringArray) {
        boolean bl;
        Object object;
        Object object2 = this.values;
        boolean bl2 = false;
        Iterable iterable = object2;
        Object object3 = new ArrayList();
        int n = 0;
        for (Object t : iterable) {
            object = (Value)t;
            bl = false;
            if (!(!(object instanceof FontValue))) continue;
            object3.add(t);
        }
        String string = CollectionsKt.joinToString$default((Iterable)((List)object3), (CharSequence)"/", null, null, (int)0, null, (Function1)execute.valueNames.2.INSTANCE, (int)30, null);
        Object object4 = this.module.getName();
        boolean bl3 = false;
        String string2 = object4;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        object2 = string2.toLowerCase();
        if (stringArray.length < 2) {
            this.chatSyntax(this.values.size() == 1 ? (String)object2 + ' ' + string + " <value>" : (String)object2 + " <" + string + '>');
            return;
        }
        object4 = this.module.getValue(stringArray[1]);
        if (object4 == null) {
            this.chatSyntax((String)object2 + " <" + string + '>');
            return;
        }
        if (object4 instanceof BoolValue) {
            bl3 = (Boolean)((BoolValue)object4).get() == false;
            ((BoolValue)object4).set(bl3);
            this.chat("\u00a77" + this.module.getName() + " \u00a78" + stringArray[1] + "\u00a77 was toggled " + (bl3 ? "\u00a78on\u00a77" : "\u00a78off\u00a77."));
            this.playEdit();
        } else {
            if (stringArray.length < 3) {
                if (object4 instanceof IntegerValue || object4 instanceof FloatValue || object4 instanceof TextValue) {
                    String string3 = stringArray[1];
                    StringBuilder stringBuilder = new StringBuilder().append((String)object2).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean bl4 = false;
                    String string4 = string3;
                    if (string4 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string5 = string4.toLowerCase();
                    moduleCommand.chatSyntax(stringBuilder.append(string5).append(" <value>").toString());
                } else if (object4 instanceof ListValue) {
                    String string6 = stringArray[1];
                    StringBuilder stringBuilder = new StringBuilder().append((String)object2).append(' ');
                    ModuleCommand moduleCommand = this;
                    boolean bl5 = false;
                    String string7 = string6;
                    if (string7 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string8 = string7.toLowerCase();
                    string6 = ArraysKt.joinToString$default((Object[])((ListValue)object4).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                    stringBuilder = stringBuilder.append(string8).append(" <");
                    bl5 = false;
                    String string9 = string6;
                    if (string9 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string8 = string9.toLowerCase();
                    moduleCommand.chatSyntax(stringBuilder.append(string8).append('>').toString());
                }
                return;
            }
            try {
                Object object5 = object4;
                if (object5 instanceof BlockValue) {
                    int n2 = 0;
                    try {
                        String string10 = stringArray[2];
                        boolean bl6 = false;
                        n = Integer.parseInt(string10);
                    }
                    catch (NumberFormatException numberFormatException) {
                        Integer n3;
                        Object t;
                        IBlock iBlock = ModuleCommand.access$getFunctions$p$s1046033730().getBlockFromName(stringArray[2]);
                        if (iBlock != null) {
                            object = iBlock;
                            bl = false;
                            boolean bl7 = false;
                            Object object6 = object;
                            boolean bl8 = false;
                            n3 = ModuleCommand.access$getFunctions$p$s1046033730().getIdFromBlock((IBlock)object6);
                        } else {
                            n3 = t = null;
                        }
                        if (t == null || (Integer)t <= 0) {
                            this.chat("\u00a77Block \u00a78" + stringArray[2] + "\u00a77 does not exist!");
                            return;
                        }
                        n = (Integer)t;
                    }
                    n2 = n;
                    ((BlockValue)object4).set((Object)n2);
                    String string11 = stringArray[1];
                    StringBuilder stringBuilder = new StringBuilder().append("\u00a77").append(this.module.getName()).append(" \u00a78");
                    ModuleCommand moduleCommand = this;
                    boolean bl9 = false;
                    String string12 = string11;
                    if (string12 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string13 = string12.toLowerCase();
                    moduleCommand.chat(stringBuilder.append(string13).append("\u00a77 was set to \u00a78").append(BlockUtils.getBlockName(n2)).append("\u00a77.").toString());
                    this.playEdit();
                    return;
                }
                if (object5 instanceof IntegerValue) {
                    object3 = stringArray[2];
                    IntegerValue integerValue = (IntegerValue)object4;
                    n = 0;
                    int n4 = Integer.parseInt((String)object3);
                    integerValue.set((Object)n4);
                } else if (object5 instanceof FloatValue) {
                    object3 = stringArray[2];
                    FloatValue floatValue = (FloatValue)object4;
                    n = 0;
                    float f = Float.parseFloat((String)object3);
                    floatValue.set((Object)Float.valueOf(f));
                } else if (object5 instanceof ListValue) {
                    if (!((ListValue)object4).contains(stringArray[2])) {
                        object3 = stringArray[1];
                        StringBuilder stringBuilder = new StringBuilder().append((String)object2).append(' ');
                        ModuleCommand moduleCommand = this;
                        n = 0;
                        Object object7 = object3;
                        if (object7 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string14 = ((String)object7).toLowerCase();
                        object3 = ArraysKt.joinToString$default((Object[])((ListValue)object4).getValues(), (CharSequence)"/", null, null, (int)0, null, null, (int)62, null);
                        stringBuilder = stringBuilder.append(string14).append(" <");
                        n = 0;
                        Object object8 = object3;
                        if (object8 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        string14 = ((String)object8).toLowerCase();
                        moduleCommand.chatSyntax(stringBuilder.append(string14).append('>').toString());
                        return;
                    }
                    ((ListValue)object4).set(stringArray[2]);
                } else if (object5 instanceof TextValue) {
                    ((TextValue)object4).set(StringUtils.toCompleteString(stringArray, 2));
                }
                this.chat("\u00a77" + this.module.getName() + " \u00a78" + stringArray[1] + "\u00a77 was set to \u00a78" + ((Value)object4).get() + "\u00a77.");
                this.playEdit();
            }
            catch (NumberFormatException numberFormatException) {
                this.chat("\u00a78" + stringArray[2] + "\u00a77 cannot be converted to number!");
            }
        }
    }

    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                boolean bl2;
                Value value;
                Object t;
                object = this.values;
                bl = false;
                Object object2 = object;
                Collection collection = new ArrayList();
                boolean bl3 = false;
                Iterator iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    t = iterator2.next();
                    value = (Value)t;
                    bl2 = false;
                    if (!(!(value instanceof FontValue) && StringsKt.startsWith((String)value.getName(), (String)stringArray[0], (boolean)true))) continue;
                    collection.add(t);
                }
                object = (List)collection;
                bl = false;
                object2 = object;
                collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)object, (int)10));
                bl3 = false;
                iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    t = iterator2.next();
                    value = (Value)t;
                    Collection collection2 = collection;
                    bl2 = false;
                    String string = value.getName();
                    boolean bl4 = false;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = string2.toLowerCase();
                    collection2.add(string3);
                }
                list = (List)collection;
                break;
            }
            case 2: {
                object = this.module.getValue(stringArray[0]);
                if (object instanceof BlockValue) {
                    boolean bl5;
                    Object object3;
                    Iterable iterable = ModuleCommand.access$getFunctions$p$s1046033730().getItemRegistryKeys();
                    boolean bl6 = false;
                    Iterable iterable2 = iterable;
                    Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
                    boolean bl7 = false;
                    for (Object t : iterable2) {
                        object3 = (IResourceLocation)t;
                        Collection collection3 = collection;
                        bl5 = false;
                        String string = object3.getResourcePath();
                        boolean bl8 = false;
                        String string4 = string;
                        if (string4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string5 = string4.toLowerCase();
                        collection3.add(string5);
                    }
                    iterable = (List)collection;
                    bl6 = false;
                    iterable2 = iterable;
                    collection = new ArrayList();
                    bl7 = false;
                    for (Object t : iterable2) {
                        object3 = (String)t;
                        bl5 = false;
                        if (!StringsKt.startsWith((String)object3, (String)stringArray[1], (boolean)true)) continue;
                        collection.add(t);
                    }
                    return (List)collection;
                }
                if (object instanceof ListValue) {
                    Iterable iterable = this.values;
                    boolean bl9 = false;
                    for (Object t : iterable) {
                        Value value = (Value)t;
                        boolean bl10 = false;
                        if (!StringsKt.equals((String)value.getName(), (String)stringArray[0], (boolean)true) || !(value instanceof ListValue)) continue;
                        String[] stringArray2 = ((ListValue)value).getValues();
                        boolean bl11 = false;
                        String[] stringArray3 = stringArray2;
                        Collection collection = new ArrayList();
                        boolean bl12 = false;
                        String[] stringArray4 = stringArray3;
                        int n = stringArray4.length;
                        for (int i = 0; i < n; ++i) {
                            String string;
                            String string6 = string = stringArray4[i];
                            boolean bl13 = false;
                            if (!StringsKt.startsWith((String)string6, (String)stringArray[1], (boolean)true)) continue;
                            collection.add(string);
                        }
                        return (List)collection;
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

