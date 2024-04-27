/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="XRay", description="Allows you to see ores through walls.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/XRay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "xrayBlocks", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "getXrayBlocks", "()Ljava/util/List;", "onToggle", "", "state", "", "LiKingSense"})
public final class XRay
extends Module {
    @NotNull
    private final List<IBlock> xrayBlocks = CollectionsKt.mutableListOf((Object[])new IBlock[]{MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.QUARTZ_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CLAY), MinecraftInstance.classProvider.getBlockEnum(BlockType.GLOWSTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CRAFTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.TORCH), MinecraftInstance.classProvider.getBlockEnum(BlockType.LADDER), MinecraftInstance.classProvider.getBlockEnum(BlockType.TNT), MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.FIRE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOSSY_COBBLESTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOB_SPAWNER), MinecraftInstance.classProvider.getBlockEnum(BlockType.END_PORTAL_FRAME), MinecraftInstance.classProvider.getBlockEnum(BlockType.ENCHANTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.BOOKSHELF), MinecraftInstance.classProvider.getBlockEnum(BlockType.COMMAND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FURNACE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LIT_FURNACE)});

    @NotNull
    public final List<IBlock> getXrayBlocks() {
        return this.xrayBlocks;
    }

    @Override
    public void onToggle(boolean state) {
        MinecraftInstance.mc.getRenderGlobal().loadRenderers();
    }

    public XRay() {
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("xray", new String[0]){

            @Override
            public void execute(@NotNull String[] args) {
                Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
                if (args.length > 1) {
                    if (StringsKt.equals((String)args[1], (String)"add", (boolean)true)) {
                        if (args.length > 2) {
                            try {
                                Object object;
                                try {
                                    object = args[2];
                                    IExtractedFunctions iExtractedFunctions = 1.access$getFunctions$p$s1046033730();
                                    boolean bl = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException exception) {
                                    IBlock tmpBlock = 1.access$getFunctions$p$s1046033730().getBlockFromName(args[2]);
                                    if (tmpBlock == null || 1.access$getFunctions$p$s1046033730().getIdFromBlock(tmpBlock) <= 0) {
                                        this.chat("\u00a77Block \u00a78" + args[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    object = tmpBlock;
                                }
                                Object block = object;
                                if (block == null || this.getXrayBlocks().contains(block)) {
                                    this.chat("This block is already on the list.");
                                    return;
                                }
                                this.getXrayBlocks().add((IBlock)block);
                                LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                                this.chat("\u00a77Added block \u00a78" + block.getLocalizedName() + "\u00a77.");
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
                    if (StringsKt.equals((String)args[1], (String)"remove", (boolean)true)) {
                        if (args.length > 2) {
                            try {
                                Object object;
                                try {
                                    object = args[2];
                                    IExtractedFunctions iExtractedFunctions = 1.access$getFunctions$p$s1046033730();
                                    boolean exception = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException exception) {
                                    IBlock b = 1.access$getFunctions$p$s1046033730().getBlockFromName(args[2]);
                                    if (b == null || 1.access$getFunctions$p$s1046033730().getIdFromBlock(b) <= 0) {
                                        this.chat("\u00a77Block \u00a78" + args[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    object = b;
                                }
                                Object block = object;
                                if (block == null || !this.getXrayBlocks().contains(block)) {
                                    this.chat("This block is not on the list.");
                                    return;
                                }
                                this.getXrayBlocks().remove(block);
                                LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                                this.chat("\u00a77Removed block \u00a78" + block.getLocalizedName() + "\u00a77.");
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
                    if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
                        this.chat("\u00a78Xray blocks:");
                        Iterable $this$forEach$iv = this.getXrayBlocks();
                        boolean $i$f$forEach = false;
                        for (Object element$iv : $this$forEach$iv) {
                            IBlock it = (IBlock)element$iv;
                            boolean bl = false;
                            this.chat("\u00a78" + it.getLocalizedName() + " \u00a77-\u00a7c " + 1.access$getFunctions$p$s1046033730().getIdFromBlock(it));
                        }
                        return;
                    }
                }
                this.chatSyntax("xray <add, remove, list>");
            }

            public static final /* synthetic */ IExtractedFunctions access$getFunctions$p$s1046033730() {
                return MinecraftInstance.functions;
            }
        });
    }
}

