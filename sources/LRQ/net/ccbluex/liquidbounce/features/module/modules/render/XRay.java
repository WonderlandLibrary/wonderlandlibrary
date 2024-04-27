/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.List;
import kotlin.collections.CollectionsKt;
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

@ModuleInfo(name="XRay", description="Allows you to see ores through walls.", category=ModuleCategory.RENDER)
public final class XRay
extends Module {
    private final List<IBlock> xrayBlocks = CollectionsKt.mutableListOf((Object[])new IBlock[]{MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.QUARTZ_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CLAY), MinecraftInstance.classProvider.getBlockEnum(BlockType.GLOWSTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CRAFTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.TORCH), MinecraftInstance.classProvider.getBlockEnum(BlockType.LADDER), MinecraftInstance.classProvider.getBlockEnum(BlockType.TNT), MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.FIRE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOSSY_COBBLESTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOB_SPAWNER), MinecraftInstance.classProvider.getBlockEnum(BlockType.END_PORTAL_FRAME), MinecraftInstance.classProvider.getBlockEnum(BlockType.ENCHANTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.BOOKSHELF), MinecraftInstance.classProvider.getBlockEnum(BlockType.COMMAND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FURNACE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LIT_FURNACE)});

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
            public void execute(String[] args) {
                if (args.length > 1) {
                    if (StringsKt.equals((String)args[1], (String)"add", (boolean)true)) {
                        if (args.length > 2) {
                            try {
                                Object object;
                                try {
                                    object = args[2];
                                    IExtractedFunctions iExtractedFunctions = MinecraftInstance.functions;
                                    boolean bl = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException exception) {
                                    IBlock tmpBlock = MinecraftInstance.functions.getBlockFromName(args[2]);
                                    if (tmpBlock == null || MinecraftInstance.functions.getIdFromBlock(tmpBlock) <= 0) {
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
                                    IExtractedFunctions iExtractedFunctions = MinecraftInstance.functions;
                                    boolean exception = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException exception) {
                                    IBlock b = MinecraftInstance.functions.getBlockFromName(args[2]);
                                    if (b == null || MinecraftInstance.functions.getIdFromBlock(b) <= 0) {
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
                            this.chat("\u00a78" + it.getLocalizedName() + " \u00a77-\u00a7c " + MinecraftInstance.functions.getIdFromBlock(it));
                        }
                        return;
                    }
                }
                this.chatSyntax("xray <add, remove, list>");
            }
        });
    }
}

