/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.RegistryNamespacedDefaultedByKey
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016\u00a2\u0006\u0002\u0010\fJ!\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000e2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016\u00a2\u0006\u0002\u0010\u000fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/command/special/XrayCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "xRay", "Lnet/ccbluex/liquidbounce/features/module/modules/render/XRay;", "getXRay", "()Lnet/ccbluex/liquidbounce/features/module/modules/render/XRay;", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "KyinoClient"})
public final class XrayCommand
extends Command {
    @NotNull
    private final XRay xRay;

    @NotNull
    public final XRay getXRay() {
        return this.xRay;
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 1) {
            if (StringsKt.equals(args2[1], "add", true)) {
                if (args2.length > 2) {
                    try {
                        String string;
                        try {
                            string = args2[2];
                            boolean bl = false;
                            string = Block.func_149729_e((int)Integer.parseInt(string));
                        }
                        catch (NumberFormatException exception) {
                            Block tmpBlock = Block.func_149684_b((String)args2[2]);
                            if (tmpBlock == null || Block.func_149682_b((Block)tmpBlock) <= 0) {
                                this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                                return;
                            }
                            string = tmpBlock;
                        }
                        String block = string;
                        if (block == null || this.xRay.getXrayBlocks().contains(block)) {
                            this.chat("This block is already on the list.");
                            return;
                        }
                        this.xRay.getXrayBlocks().add((Block)block);
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                        this.chat("\u00a77Added block \u00a78" + block.func_149732_F() + "\u00a77.");
                        this.playEdit();
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax("xray add <block_id>");
                return;
            }
            if (StringsKt.equals(args2[1], "remove", true)) {
                if (args2.length > 2) {
                    try {
                        String string;
                        try {
                            string = args2[2];
                            boolean exception = false;
                            string = Block.func_149729_e((int)Integer.parseInt(string));
                        }
                        catch (NumberFormatException exception) {
                            Block tmpBlock = Block.func_149684_b((String)args2[2]);
                            if (tmpBlock == null || Block.func_149682_b((Block)tmpBlock) <= 0) {
                                this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                                return;
                            }
                            string = tmpBlock;
                        }
                        String block = string;
                        if (block == null || !this.xRay.getXrayBlocks().contains(block)) {
                            this.chat("This block is not on the list.");
                            return;
                        }
                        this.xRay.getXrayBlocks().remove(block);
                        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                        this.chat("\u00a77Removed block \u00a78" + block.func_149732_F() + "\u00a77.");
                        this.playEdit();
                    }
                    catch (NumberFormatException exception) {
                        this.chatSyntaxError();
                    }
                    return;
                }
                this.chatSyntax("xray remove <block_id>");
                return;
            }
            if (StringsKt.equals(args2[1], "list", true)) {
                this.chat("\u00a78Xray blocks:");
                Iterable $this$forEach$iv = this.xRay.getXrayBlocks();
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Block it = (Block)element$iv;
                    boolean bl = false;
                    this.chat("\u00a78" + it.func_149732_F() + " \u00a77-\u00a7c " + Block.func_149682_b((Block)it));
                }
                return;
            }
        }
        this.chatSyntax("xray <add, remove, list>");
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
                void $this$filterTo$iv$iv;
                Iterable $this$mapTo$iv$iv;
                String[] $this$map$iv = new String[]{"add", "remove", "list"};
                boolean $i$f$map = false;
                String[] stringArray = $this$map$iv;
                Collection destination$iv$iv = new ArrayList($this$map$iv.length);
                boolean $i$f$mapTo = false;
                Iterator iterator2 = $this$mapTo$iv$iv;
                int n = ((void)iterator2).length;
                for (int i = 0; i < n; ++i) {
                    String string;
                    void it;
                    void item$iv$iv;
                    void var11_30 = item$iv$iv = iterator2[i];
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    void var13_36 = it;
                    boolean bl3 = false;
                    void v0 = var13_36;
                    if (v0 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkExpressionValueIsNotNull(v0.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    String it = (String)element$iv$iv;
                    boolean bl4 = false;
                    if (!StringsKt.startsWith(it, args2[0], true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = (List)destination$iv$iv;
                break;
            }
            case 2: {
                object = args2[0];
                boolean $i$f$filter = false;
                Object object2 = object;
                if (object2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string = ((String)object2).toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string, "(this as java.lang.String).toLowerCase()");
                switch (string) {
                    case "add": {
                        Iterable $this$filterTo$iv$iv;
                        String string2;
                        boolean bl5;
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
                            String string3;
                            ResourceLocation bl4 = (ResourceLocation)item$iv$iv;
                            Collection collection = destination$iv$iv2;
                            boolean bl6 = false;
                            void v6 = it;
                            Intrinsics.checkExpressionValueIsNotNull(v6, "it");
                            Intrinsics.checkExpressionValueIsNotNull(v6.func_110623_a(), "it.resourcePath");
                            bl5 = false;
                            String string4 = string2;
                            if (string4 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            Intrinsics.checkExpressionValueIsNotNull(string4.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                            collection.add(string3);
                        }
                        Iterable $this$filter$iv = (List)destination$iv$iv2;
                        boolean $i$f$filter2 = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv2 = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl7 = false;
                            string2 = it;
                            bl5 = false;
                            String string5 = string2;
                            if (string5 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            String string6 = string5.toLowerCase();
                            Intrinsics.checkExpressionValueIsNotNull(string6, "(this as java.lang.String).toLowerCase()");
                            if (!(Block.func_149684_b((String)string6) != null)) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        $this$filter$iv = (List)destination$iv$iv2;
                        $i$f$filter2 = false;
                        $this$filterTo$iv$iv = $this$filter$iv;
                        destination$iv$iv2 = new ArrayList();
                        $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            String string7;
                            it = (String)element$iv$iv;
                            boolean bl8 = false;
                            string2 = it;
                            List<Block> list2 = this.xRay.getXrayBlocks();
                            boolean bl9 = false;
                            String string8 = string2;
                            if (string8 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            Intrinsics.checkExpressionValueIsNotNull(string8.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                            if (!(!list2.contains(Block.func_149684_b((String)string7)))) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        $this$filter$iv = (List)destination$iv$iv2;
                        $i$f$filter2 = false;
                        $this$filterTo$iv$iv = $this$filter$iv;
                        destination$iv$iv2 = new ArrayList();
                        $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl10 = false;
                            if (!StringsKt.startsWith(it, args2[1], true)) continue;
                            destination$iv$iv2.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv2;
                    }
                    case "remove": {
                        Object it;
                        Iterable $this$mapTo$iv$iv;
                        RegistryNamespacedDefaultedByKey registryNamespacedDefaultedByKey = Block.field_149771_c;
                        Intrinsics.checkExpressionValueIsNotNull(registryNamespacedDefaultedByKey, "Block.blockRegistry");
                        Set set = registryNamespacedDefaultedByKey.func_148742_b();
                        Intrinsics.checkExpressionValueIsNotNull(set, "Block.blockRegistry.keys");
                        Iterable $this$map$iv = set;
                        boolean $i$f$map = false;
                        Iterable $this$filterTo$iv$iv = $this$map$iv;
                        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            String string9;
                            String string10;
                            it = (ResourceLocation)item$iv$iv;
                            Collection collection = destination$iv$iv;
                            boolean bl11 = false;
                            ResourceLocation resourceLocation = it;
                            Intrinsics.checkExpressionValueIsNotNull(resourceLocation, "it");
                            Intrinsics.checkExpressionValueIsNotNull(resourceLocation.func_110623_a(), "it.resourcePath");
                            boolean bl12 = false;
                            String string11 = string10;
                            if (string11 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                            Intrinsics.checkExpressionValueIsNotNull(string11.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                            collection.add(string9);
                        }
                        Iterable $this$filter$iv = (List)destination$iv$iv;
                        boolean $i$f$filter3 = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl13 = false;
                            if (!this.xRay.getXrayBlocks().contains(Block.func_149684_b((String)it))) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        $this$filter$iv = (List)destination$iv$iv;
                        $i$f$filter3 = false;
                        $this$filterTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList();
                        $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            it = (String)element$iv$iv;
                            boolean bl14 = false;
                            if (!StringsKt.startsWith((String)it, args2[1], true)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        return (List)destination$iv$iv;
                    }
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

    public XrayCommand() {
        super("xray", new String[0]);
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(XRay.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.XRay");
        }
        this.xRay = (XRay)module;
    }
}

