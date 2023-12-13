package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.render.Search;
import me.nyan.flush.utils.other.ChatUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class CommandSearch extends Command {
    public CommandSearch() {
        super("Search", "Adds/removes/lists blocks from Search module",
                "search add <block_name>/<block_id> <hex color (optional)>" +
                " | remove <block_name>/<block_id> | list", "blockesp");
    }

    private final Search search = flush.getModuleManager().getModule(Search.class);

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length > 1) {
                        Block block;
                        try {
                            block = Block.getBlockById(Integer.parseInt(args[1]));
                        } catch (NumberFormatException e) {
                            block = Block.getBlockFromItem(Item.itemRegistry.getObject(new ResourceLocation(args[1])));
                        }

                        if (block == null || block == Blocks.air) {
                            ChatUtils.println("§4Invalid block ID/block name.");
                            break;
                        }

                        int hex;
                        if (args.length > 2) {
                            try {
                                hex = Integer.decode(args[2]);
                            } catch (NumberFormatException e) {
                                ChatUtils.println("§4Invalid hex.");
                                break;
                            }
                        } else {
                            hex = ColorUtils.getRandomColor();
                        }

                        search.getBlocks().put(block, hex);
                        ChatUtils.println("§9Added " + block.getLocalizedName() +
                                " with color #" + Integer.toHexString(hex).toUpperCase() + ".");
                        search.save();
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "remove":
                    if (args.length > 1) {
                        Block block;
                        try {
                            block = Block.getBlockById(Integer.parseInt(args[1]));
                        } catch (NumberFormatException e) {
                            block = Block.getBlockFromItem(Item.itemRegistry.getObject(new ResourceLocation(args[1])));
                        }

                        if (block == null) {
                            ChatUtils.println("§4Invalid block ID/block name.");
                            break;
                        }

                        if (!search.getBlocks().containsKey(block)) {
                            ChatUtils.println("§4" + block.getLocalizedName() + " isn't in the list.");
                            break;
                        }

                        search.getBlocks().remove(block);
                        ChatUtils.println("§9Removed " + block.getLocalizedName() + " from the list.");
                        search.save();
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "list":
                    search.load();

                    if (search.getBlocks().isEmpty()) {
                        ChatUtils.println("§4Blocks list is empty.");
                    } else {
                        ChatUtils.println("§9These blocks are in the list:");
                        for (Map.Entry<Block, Integer> block : search.getBlocks().entrySet()) {
                            ChatUtils.println(block.getKey().getLocalizedName() +
                                    ", Block ID: " + Block.getIdFromBlock(block.getKey()) +
                                    ", Color: " + Integer.toHexString(block.getValue()).toUpperCase());
                        }
                    }
                    break;

                default:
                    sendSyntaxHelpMessage();
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
