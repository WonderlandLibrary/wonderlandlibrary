package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "XRay", description = "Allows you to see ores through walls.", category = ModuleCategory.RENDER)
public class XRay extends Module {

    public List<Block> xrayBlocks = new ArrayList<Block>() {{
        add(Blocks.coal_ore);
        add(Blocks.iron_ore);
        add(Blocks.gold_ore);
        add(Blocks.redstone_ore);
        add(Blocks.lapis_ore);
        add(Blocks.diamond_ore);
        add(Blocks.emerald_ore);
        add(Blocks.quartz_ore);
        add(Blocks.clay);
        add(Blocks.glowstone);
        add(Blocks.crafting_table);
        add(Blocks.torch);
        add(Blocks.ladder);
        add(Blocks.tnt);
        add(Blocks.coal_block);
        add(Blocks.iron_block);
        add(Blocks.gold_block);
        add(Blocks.diamond_block);
        add(Blocks.emerald_block);
        add(Blocks.redstone_block);
        add(Blocks.lapis_block);
        add(Blocks.fire);
        add(Blocks.mossy_cobblestone);
        add(Blocks.mob_spawner);
        add(Blocks.end_portal_frame);
        add(Blocks.enchanting_table);
        add(Blocks.bookshelf);
        add(Blocks.command_block);
        add(Blocks.lava);
        add(Blocks.flowing_lava);
        add(Blocks.water);
        add(Blocks.flowing_water);
        add(Blocks.furnace);
        add(Blocks.lit_furnace);
    }};

    public XRay() {
        LiquidSense.commandManager.registerCommand(new Command("xray") {
            @Override
            public void execute(String[] args) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length > 2) {
                            try {
                                int blockId = Integer.parseInt(args[2]);
                                Block block = Block.getBlockById(blockId);

                                if (xrayBlocks.contains(block)) {
                                    chat("This block is already on the list.");
                                    return;
                                }

                                xrayBlocks.add(block);
                                LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.xrayConfig);
                                chat("§7Added block §8" + block.getLocalizedName() + "§7.");
                                playEdit();
                            } catch (NumberFormatException exception) {
                                chatSyntaxError();
                            }

                            return;
                        }

                        chatSyntax("xray add <block_id>");
                        return;
                    }

                    if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length > 2) {
                            try {
                                int blockId = Integer.parseInt(args[2]);
                                Block block = Block.getBlockById(blockId);

                                if (!xrayBlocks.contains(block)) {
                                    chat("This block is not on the list.");
                                    return;
                                }

                                xrayBlocks.remove(block);
                                LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.xrayConfig);
                                chat("§7Removed block §8" + block.getLocalizedName() + "§7.");
                                playEdit();
                            } catch (NumberFormatException exception) {
                                chatSyntaxError();
                            }

                            return;
                        }
                        chatSyntax("xray remove <block_id>");
                        return;
                    }

                    if (args[1].equalsIgnoreCase("list")) {
                        chat("§8Xray blocks:");
                        for (Block block : xrayBlocks) {
                            chat("§8" + block.getLocalizedName() + " §7-§c " + Block.getIdFromBlock(block));
                        }
                        return;
                    }
                }

                chatSyntax("xray <add, remove, list>");
            }
        });
    }

    @Override
    public void onToggle(boolean state) {
        mc.renderGlobal.loadRenderers();
    }
}
