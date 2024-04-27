/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.NotNull;

@Info(name="BlockXray", description="Allows you to see ores through walls.", category=Category.RENDER, cnName="\u65b9\u5757\u900f\u89c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/module/modules/render/XRay;", "Lnet/dev/important/modules/module/Module;", "()V", "xrayBlocks", "", "Lnet/minecraft/block/Block;", "getXrayBlocks", "()Ljava/util/List;", "onToggle", "", "state", "", "LiquidBounce"})
public final class XRay
extends Module {
    @NotNull
    private final List<Block> xrayBlocks;

    public XRay() {
        Object[] objectArray = new Block[34];
        Block block = Blocks.field_150365_q;
        Intrinsics.checkNotNullExpressionValue(block, "coal_ore");
        objectArray[0] = block;
        block = Blocks.field_150366_p;
        Intrinsics.checkNotNullExpressionValue(block, "iron_ore");
        objectArray[1] = block;
        block = Blocks.field_150352_o;
        Intrinsics.checkNotNullExpressionValue(block, "gold_ore");
        objectArray[2] = block;
        block = Blocks.field_150450_ax;
        Intrinsics.checkNotNullExpressionValue(block, "redstone_ore");
        objectArray[3] = block;
        block = Blocks.field_150369_x;
        Intrinsics.checkNotNullExpressionValue(block, "lapis_ore");
        objectArray[4] = block;
        block = Blocks.field_150482_ag;
        Intrinsics.checkNotNullExpressionValue(block, "diamond_ore");
        objectArray[5] = block;
        block = Blocks.field_150412_bA;
        Intrinsics.checkNotNullExpressionValue(block, "emerald_ore");
        objectArray[6] = block;
        block = Blocks.field_150449_bY;
        Intrinsics.checkNotNullExpressionValue(block, "quartz_ore");
        objectArray[7] = block;
        block = Blocks.field_150435_aG;
        Intrinsics.checkNotNullExpressionValue(block, "clay");
        objectArray[8] = block;
        block = Blocks.field_150426_aN;
        Intrinsics.checkNotNullExpressionValue(block, "glowstone");
        objectArray[9] = block;
        block = Blocks.field_150462_ai;
        Intrinsics.checkNotNullExpressionValue(block, "crafting_table");
        objectArray[10] = block;
        block = Blocks.field_150478_aa;
        Intrinsics.checkNotNullExpressionValue(block, "torch");
        objectArray[11] = block;
        block = Blocks.field_150468_ap;
        Intrinsics.checkNotNullExpressionValue(block, "ladder");
        objectArray[12] = block;
        block = Blocks.field_150335_W;
        Intrinsics.checkNotNullExpressionValue(block, "tnt");
        objectArray[13] = block;
        block = Blocks.field_150402_ci;
        Intrinsics.checkNotNullExpressionValue(block, "coal_block");
        objectArray[14] = block;
        block = Blocks.field_150339_S;
        Intrinsics.checkNotNullExpressionValue(block, "iron_block");
        objectArray[15] = block;
        block = Blocks.field_150340_R;
        Intrinsics.checkNotNullExpressionValue(block, "gold_block");
        objectArray[16] = block;
        block = Blocks.field_150484_ah;
        Intrinsics.checkNotNullExpressionValue(block, "diamond_block");
        objectArray[17] = block;
        block = Blocks.field_150475_bE;
        Intrinsics.checkNotNullExpressionValue(block, "emerald_block");
        objectArray[18] = block;
        block = Blocks.field_150451_bX;
        Intrinsics.checkNotNullExpressionValue(block, "redstone_block");
        objectArray[19] = block;
        block = Blocks.field_150368_y;
        Intrinsics.checkNotNullExpressionValue(block, "lapis_block");
        objectArray[20] = block;
        block = Blocks.field_150480_ab;
        Intrinsics.checkNotNullExpressionValue(block, "fire");
        objectArray[21] = block;
        block = Blocks.field_150341_Y;
        Intrinsics.checkNotNullExpressionValue(block, "mossy_cobblestone");
        objectArray[22] = block;
        block = Blocks.field_150474_ac;
        Intrinsics.checkNotNullExpressionValue(block, "mob_spawner");
        objectArray[23] = block;
        block = Blocks.field_150378_br;
        Intrinsics.checkNotNullExpressionValue(block, "end_portal_frame");
        objectArray[24] = block;
        block = Blocks.field_150381_bn;
        Intrinsics.checkNotNullExpressionValue(block, "enchanting_table");
        objectArray[25] = block;
        block = Blocks.field_150342_X;
        Intrinsics.checkNotNullExpressionValue(block, "bookshelf");
        objectArray[26] = block;
        block = Blocks.field_150483_bI;
        Intrinsics.checkNotNullExpressionValue(block, "command_block");
        objectArray[27] = block;
        block = Blocks.field_150353_l;
        Intrinsics.checkNotNullExpressionValue(block, "lava");
        objectArray[28] = block;
        block = Blocks.field_150356_k;
        Intrinsics.checkNotNullExpressionValue(block, "flowing_lava");
        objectArray[29] = block;
        block = Blocks.field_150355_j;
        Intrinsics.checkNotNullExpressionValue(block, "water");
        objectArray[30] = block;
        block = Blocks.field_150358_i;
        Intrinsics.checkNotNullExpressionValue(block, "flowing_water");
        objectArray[31] = block;
        block = Blocks.field_150460_al;
        Intrinsics.checkNotNullExpressionValue(block, "furnace");
        objectArray[32] = block;
        block = Blocks.field_150470_am;
        Intrinsics.checkNotNullExpressionValue(block, "lit_furnace");
        objectArray[33] = block;
        this.xrayBlocks = CollectionsKt.mutableListOf(objectArray);
        boolean $i$f$emptyArray = false;
        objectArray = new String[]{};
        Client.INSTANCE.getCommandManager().registerCommand(new Command((String[])objectArray){

            @Override
            public void execute(@NotNull String[] args2) {
                Intrinsics.checkNotNullParameter(args2, "args");
                if (args2.length > 1) {
                    if (StringsKt.equals(args2[1], "add", true)) {
                        if (args2.length > 2) {
                            try {
                                Block block;
                                try {
                                    block = Block.func_149729_e((int)Integer.parseInt(args2[2]));
                                }
                                catch (NumberFormatException exception) {
                                    Block tmpBlock = Block.func_149684_b((String)args2[2]);
                                    if (Block.func_149682_b((Block)tmpBlock) <= 0 || tmpBlock == null) {
                                        this.chat("\u00a77Block \u00a78" + args2[2] + "\u00a77 does not exist!");
                                        return;
                                    }
                                    block = tmpBlock;
                                }
                                Block block2 = block;
                                if (this.getXrayBlocks().contains(block2)) {
                                    this.chat("This block is already on the list.");
                                    return;
                                }
                                List<Block> list = this.getXrayBlocks();
                                Intrinsics.checkNotNullExpressionValue(block2, "block");
                                list.add(block2);
                                Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().xrayConfig);
                                this.chat("\u00a77Added block \u00a78" + block2.func_149732_F() + "\u00a77.");
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
                                        Block block3 = Block.func_149729_e((int)Integer.parseInt(args2[2]));
                                        Intrinsics.checkNotNullExpressionValue(block3, "getBlockById(args[2].toInt())");
                                        block = block3;
                                    }
                                    catch (NumberFormatException exception) {
                                        Block block4 = Block.func_149684_b((String)args2[2]);
                                        Intrinsics.checkNotNullExpressionValue(block4, "getBlockFromName(args[2])");
                                        block = block4;
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
                                Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().xrayConfig);
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

    @NotNull
    public final List<Block> getXrayBlocks() {
        return this.xrayBlocks;
    }

    @Override
    public void onToggle(boolean state) {
        MinecraftInstance.mc.field_71438_f.func_72712_a();
    }
}

