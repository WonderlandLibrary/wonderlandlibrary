/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDynamicLiquid
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockStaticLiquid
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Blocks
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="XRay", description="Allows you to see ores through walls.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/XRay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "xrayBlocks", "", "Lnet/minecraft/block/Block;", "getXrayBlocks", "()Ljava/util/List;", "onToggle", "", "state", "", "KyinoClient"})
public final class XRay
extends Module {
    @NotNull
    private final List<Block> xrayBlocks;

    @NotNull
    public final List<Block> getXrayBlocks() {
        return this.xrayBlocks;
    }

    @Override
    public void onToggle(boolean state) {
        XRay.access$getMc$p$s1046033730().field_71438_f.func_72712_a();
    }

    public XRay() {
        Block[] blockArray = new Block[34];
        Block block = Blocks.field_150365_q;
        Intrinsics.checkExpressionValueIsNotNull(block, "Blocks.coal_ore");
        blockArray[0] = block;
        Block block2 = Blocks.field_150366_p;
        Intrinsics.checkExpressionValueIsNotNull(block2, "Blocks.iron_ore");
        blockArray[1] = block2;
        Block block3 = Blocks.field_150352_o;
        Intrinsics.checkExpressionValueIsNotNull(block3, "Blocks.gold_ore");
        blockArray[2] = block3;
        Block block4 = Blocks.field_150450_ax;
        Intrinsics.checkExpressionValueIsNotNull(block4, "Blocks.redstone_ore");
        blockArray[3] = block4;
        Block block5 = Blocks.field_150369_x;
        Intrinsics.checkExpressionValueIsNotNull(block5, "Blocks.lapis_ore");
        blockArray[4] = block5;
        Block block6 = Blocks.field_150482_ag;
        Intrinsics.checkExpressionValueIsNotNull(block6, "Blocks.diamond_ore");
        blockArray[5] = block6;
        Block block7 = Blocks.field_150412_bA;
        Intrinsics.checkExpressionValueIsNotNull(block7, "Blocks.emerald_ore");
        blockArray[6] = block7;
        Block block8 = Blocks.field_150449_bY;
        Intrinsics.checkExpressionValueIsNotNull(block8, "Blocks.quartz_ore");
        blockArray[7] = block8;
        Block block9 = Blocks.field_150435_aG;
        Intrinsics.checkExpressionValueIsNotNull(block9, "Blocks.clay");
        blockArray[8] = block9;
        Block block10 = Blocks.field_150426_aN;
        Intrinsics.checkExpressionValueIsNotNull(block10, "Blocks.glowstone");
        blockArray[9] = block10;
        Block block11 = Blocks.field_150462_ai;
        Intrinsics.checkExpressionValueIsNotNull(block11, "Blocks.crafting_table");
        blockArray[10] = block11;
        Block block12 = Blocks.field_150478_aa;
        Intrinsics.checkExpressionValueIsNotNull(block12, "Blocks.torch");
        blockArray[11] = block12;
        Block block13 = Blocks.field_150468_ap;
        Intrinsics.checkExpressionValueIsNotNull(block13, "Blocks.ladder");
        blockArray[12] = block13;
        Block block14 = Blocks.field_150335_W;
        Intrinsics.checkExpressionValueIsNotNull(block14, "Blocks.tnt");
        blockArray[13] = block14;
        Block block15 = Blocks.field_150402_ci;
        Intrinsics.checkExpressionValueIsNotNull(block15, "Blocks.coal_block");
        blockArray[14] = block15;
        Block block16 = Blocks.field_150339_S;
        Intrinsics.checkExpressionValueIsNotNull(block16, "Blocks.iron_block");
        blockArray[15] = block16;
        Block block17 = Blocks.field_150340_R;
        Intrinsics.checkExpressionValueIsNotNull(block17, "Blocks.gold_block");
        blockArray[16] = block17;
        Block block18 = Blocks.field_150484_ah;
        Intrinsics.checkExpressionValueIsNotNull(block18, "Blocks.diamond_block");
        blockArray[17] = block18;
        Block block19 = Blocks.field_150475_bE;
        Intrinsics.checkExpressionValueIsNotNull(block19, "Blocks.emerald_block");
        blockArray[18] = block19;
        Block block20 = Blocks.field_150451_bX;
        Intrinsics.checkExpressionValueIsNotNull(block20, "Blocks.redstone_block");
        blockArray[19] = block20;
        Block block21 = Blocks.field_150368_y;
        Intrinsics.checkExpressionValueIsNotNull(block21, "Blocks.lapis_block");
        blockArray[20] = block21;
        BlockFire blockFire = Blocks.field_150480_ab;
        Intrinsics.checkExpressionValueIsNotNull(blockFire, "Blocks.fire");
        blockArray[21] = (Block)blockFire;
        Block block22 = Blocks.field_150341_Y;
        Intrinsics.checkExpressionValueIsNotNull(block22, "Blocks.mossy_cobblestone");
        blockArray[22] = block22;
        Block block23 = Blocks.field_150474_ac;
        Intrinsics.checkExpressionValueIsNotNull(block23, "Blocks.mob_spawner");
        blockArray[23] = block23;
        Block block24 = Blocks.field_150378_br;
        Intrinsics.checkExpressionValueIsNotNull(block24, "Blocks.end_portal_frame");
        blockArray[24] = block24;
        Block block25 = Blocks.field_150381_bn;
        Intrinsics.checkExpressionValueIsNotNull(block25, "Blocks.enchanting_table");
        blockArray[25] = block25;
        Block block26 = Blocks.field_150342_X;
        Intrinsics.checkExpressionValueIsNotNull(block26, "Blocks.bookshelf");
        blockArray[26] = block26;
        Block block27 = Blocks.field_150483_bI;
        Intrinsics.checkExpressionValueIsNotNull(block27, "Blocks.command_block");
        blockArray[27] = block27;
        BlockStaticLiquid blockStaticLiquid = Blocks.field_150353_l;
        Intrinsics.checkExpressionValueIsNotNull(blockStaticLiquid, "Blocks.lava");
        blockArray[28] = (Block)blockStaticLiquid;
        BlockDynamicLiquid blockDynamicLiquid = Blocks.field_150356_k;
        Intrinsics.checkExpressionValueIsNotNull(blockDynamicLiquid, "Blocks.flowing_lava");
        blockArray[29] = (Block)blockDynamicLiquid;
        BlockStaticLiquid blockStaticLiquid2 = Blocks.field_150355_j;
        Intrinsics.checkExpressionValueIsNotNull(blockStaticLiquid2, "Blocks.water");
        blockArray[30] = (Block)blockStaticLiquid2;
        BlockDynamicLiquid blockDynamicLiquid2 = Blocks.field_150358_i;
        Intrinsics.checkExpressionValueIsNotNull(blockDynamicLiquid2, "Blocks.flowing_water");
        blockArray[31] = (Block)blockDynamicLiquid2;
        Block block28 = Blocks.field_150460_al;
        Intrinsics.checkExpressionValueIsNotNull(block28, "Blocks.furnace");
        blockArray[32] = block28;
        Block block29 = Blocks.field_150470_am;
        Intrinsics.checkExpressionValueIsNotNull(block29, "Blocks.lit_furnace");
        blockArray[33] = block29;
        this.xrayBlocks = CollectionsKt.mutableListOf(blockArray);
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Command("xray", new String[0]){

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
                                    if (Block.func_149682_b((Block)tmpBlock) <= 0 || tmpBlock == null) {
                                        this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    string = tmpBlock;
                                }
                                String block = string;
                                if (this.getXrayBlocks().contains(block)) {
                                    this.chat("This block is already on the list.");
                                    return;
                                }
                                List<Block> list = this.getXrayBlocks();
                                String string2 = block;
                                Intrinsics.checkExpressionValueIsNotNull(string2, "block");
                                list.add((Block)string2);
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
                                Block block;
                                block18: {
                                    block = null;
                                    try {
                                        String string = args2[2];
                                        boolean bl = false;
                                        Block block2 = Block.func_149729_e((int)Integer.parseInt(string));
                                        Intrinsics.checkExpressionValueIsNotNull(block2, "Block.getBlockById(args[2].toInt())");
                                        block = block2;
                                    }
                                    catch (NumberFormatException exception) {
                                        Block block3 = Block.func_149684_b((String)args2[2]);
                                        Intrinsics.checkExpressionValueIsNotNull(block3, "Block.getBlockFromName(args[2])");
                                        block = block3;
                                        if (Block.func_149682_b((Block)block) > 0) break block18;
                                        this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                }
                                if (!this.getXrayBlocks().contains(block)) {
                                    this.chat("This block is not on the list.");
                                    return;
                                }
                                this.getXrayBlocks().remove(block);
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
                        Iterable $this$forEach$iv = this.getXrayBlocks();
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
        });
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

