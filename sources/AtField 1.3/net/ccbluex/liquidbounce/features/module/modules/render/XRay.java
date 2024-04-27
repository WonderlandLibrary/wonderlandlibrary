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
    private final List xrayBlocks = CollectionsKt.mutableListOf((Object[])new IBlock[]{MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.QUARTZ_ORE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CLAY), MinecraftInstance.classProvider.getBlockEnum(BlockType.GLOWSTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.CRAFTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.TORCH), MinecraftInstance.classProvider.getBlockEnum(BlockType.LADDER), MinecraftInstance.classProvider.getBlockEnum(BlockType.TNT), MinecraftInstance.classProvider.getBlockEnum(BlockType.COAL_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.IRON_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.GOLD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.DIAMOND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.EMERALD_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.REDSTONE_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAPIS_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.FIRE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOSSY_COBBLESTONE), MinecraftInstance.classProvider.getBlockEnum(BlockType.MOB_SPAWNER), MinecraftInstance.classProvider.getBlockEnum(BlockType.END_PORTAL_FRAME), MinecraftInstance.classProvider.getBlockEnum(BlockType.ENCHANTING_TABLE), MinecraftInstance.classProvider.getBlockEnum(BlockType.BOOKSHELF), MinecraftInstance.classProvider.getBlockEnum(BlockType.COMMAND_BLOCK), MinecraftInstance.classProvider.getBlockEnum(BlockType.LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_LAVA), MinecraftInstance.classProvider.getBlockEnum(BlockType.WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER), MinecraftInstance.classProvider.getBlockEnum(BlockType.FURNACE), MinecraftInstance.classProvider.getBlockEnum(BlockType.LIT_FURNACE)});

    public final List getXrayBlocks() {
        return this.xrayBlocks;
    }

    public XRay() {
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command(this, "xray", new String[0]){
            final XRay this$0;

            @Override
            public void execute(String[] stringArray) {
                if (stringArray.length > 1) {
                    if (StringsKt.equals((String)stringArray[1], (String)"add", (boolean)true)) {
                        if (stringArray.length > 2) {
                            try {
                                Object object;
                                try {
                                    object = stringArray[2];
                                    IExtractedFunctions iExtractedFunctions = 1.access$getFunctions$p$s1046033730();
                                    boolean bl = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException numberFormatException) {
                                    IBlock iBlock = 1.access$getFunctions$p$s1046033730().getBlockFromName(stringArray[2]);
                                    if (iBlock == null || 1.access$getFunctions$p$s1046033730().getIdFromBlock(iBlock) <= 0) {
                                        this.chat("\u00a77Block \u00a78" + stringArray[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    object = iBlock;
                                }
                                Object object2 = object;
                                if (object2 == null || this.this$0.getXrayBlocks().contains(object2)) {
                                    this.chat("This block is already on the list.");
                                    return;
                                }
                                this.this$0.getXrayBlocks().add(object2);
                                LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                                this.chat("\u00a77Added block \u00a78" + object2.getLocalizedName() + "\u00a77.");
                                this.playEdit();
                            }
                            catch (NumberFormatException numberFormatException) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax("xray add <block_id>");
                        return;
                    }
                    if (StringsKt.equals((String)stringArray[1], (String)"remove", (boolean)true)) {
                        if (stringArray.length > 2) {
                            try {
                                Object object;
                                try {
                                    object = stringArray[2];
                                    IExtractedFunctions iExtractedFunctions = 1.access$getFunctions$p$s1046033730();
                                    boolean bl = false;
                                    int n = Integer.parseInt((String)object);
                                    object = iExtractedFunctions.getBlockById(n);
                                }
                                catch (NumberFormatException numberFormatException) {
                                    IBlock iBlock = 1.access$getFunctions$p$s1046033730().getBlockFromName(stringArray[2]);
                                    if (iBlock == null || 1.access$getFunctions$p$s1046033730().getIdFromBlock(iBlock) <= 0) {
                                        this.chat("\u00a77Block \u00a78" + stringArray[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    object = iBlock;
                                }
                                Object object3 = object;
                                if (object3 == null || !this.this$0.getXrayBlocks().contains(object3)) {
                                    this.chat("This block is not on the list.");
                                    return;
                                }
                                this.this$0.getXrayBlocks().remove(object3);
                                LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
                                this.chat("\u00a77Removed block \u00a78" + object3.getLocalizedName() + "\u00a77.");
                                this.playEdit();
                            }
                            catch (NumberFormatException numberFormatException) {
                                this.chatSyntaxError();
                            }
                            return;
                        }
                        this.chatSyntax("xray remove <block_id>");
                        return;
                    }
                    if (StringsKt.equals((String)stringArray[1], (String)"list", (boolean)true)) {
                        this.chat("\u00a78Xray blocks:");
                        Iterable iterable = this.this$0.getXrayBlocks();
                        boolean bl = false;
                        for (Object t : iterable) {
                            IBlock iBlock = (IBlock)t;
                            boolean bl2 = false;
                            this.chat("\u00a78" + iBlock.getLocalizedName() + " \u00a77-\u00a7c " + 1.access$getFunctions$p$s1046033730().getIdFromBlock(iBlock));
                        }
                        return;
                    }
                }
                this.chatSyntax("xray <add, remove, list>");
            }

            public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
                return MinecraftInstance.functions;
            }
            {
                this.this$0 = xRay;
                super(string, stringArray);
            }
        });
    }

    @Override
    public void onToggle(boolean bl) {
        MinecraftInstance.mc.getRenderGlobal().loadRenderers();
    }
}

