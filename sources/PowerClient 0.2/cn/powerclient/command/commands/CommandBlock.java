/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command.commands;

import java.util.Iterator;
import me.AveReborn.Client;
import me.AveReborn.command.Command;
import me.AveReborn.mod.mods.RENDER.BlockESP;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.FileUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;

public class CommandBlock
extends Command {
    public CommandBlock(String[] commands) {
        super(commands);
        this.setArgs("-block (add | remove) <id>, (clear | list)");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("clear")) {
            BlockESP.getBlockIds().clear();
            ClientUtil.sendClientMessage("Cleared list!", ClientNotification.Type.SUCCESS);
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        } else if (args.length > 1 && args[1].equalsIgnoreCase("list")) {
            String all2 = "Empty";
            Iterator<Integer> iterator = BlockESP.getBlockIds().iterator();
            while (iterator.hasNext()) {
                int id2 = iterator.next();
                String name = Block.getBlockById(id2).getLocalizedName();
                String string = all2 = all2.equals("Empty") ? name : String.valueOf(String.valueOf(all2)) + ", " + name;
            }
            ClientUtil.sendClientMessage(all2, ClientNotification.Type.INFO);
        } else if (args.length > 2 && args[1].equalsIgnoreCase("add")) {
            try {
                int id3 = Integer.valueOf(args[2]);
                if ((Block.getBlockById(id3).isFullBlock() || id3 == 144 || id3 == 166) && !BlockESP.getBlockIds().contains(new Integer(id3))) {
                    BlockESP.getBlockIds().add(new Integer(id3));
                    ClientUtil.sendClientMessage("Added " + Block.getBlockById(id3).getLocalizedName(), ClientNotification.Type.SUCCESS);
                }
                ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
            }
            catch (Exception e2) {
                try {
                    Block block = Block.getBlockFromName(args[2]);
                    String name = block.getLocalizedName();
                    if (BlockESP.getBlockIds().contains(new Integer(Block.getIdFromBlock(block)))) {
                        return;
                    }
                    ClientUtil.sendClientMessage("Added " + name, ClientNotification.Type.SUCCESS);
                    BlockESP.getBlockIds().add(new Integer(Block.getIdFromBlock(block)));
                }
                catch (Exception e1) {
                    ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
                }
            }
        } else if (args.length > 2 && args[1].equalsIgnoreCase("remove")) {
            try {
                int id4 = Integer.valueOf(args[2]);
                this.removeId(id4);
                ClientUtil.sendClientMessage("Removed " + Block.getBlockById(id4).getLocalizedName(), ClientNotification.Type.ERROR);
            }
            catch (Exception e3) {
                try {
                    Block block = Block.getBlockFromName(args[2]);
                    String name = block.getLocalizedName();
                    this.removeId(Block.getIdFromBlock(block));
                    ClientUtil.sendClientMessage("Removed " + name, ClientNotification.Type.ERROR);
                }
                catch (Exception e1) {
                    ClientUtil.sendClientMessage("Invalid Id", ClientNotification.Type.WARNING);
                }
            }
        } else {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
        }
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        Client.instance.fileMgr.saveBlocks();
    }

    private void removeId(int number) {
        int i2 = 0;
        while (i2 < BlockESP.getBlockIds().size()) {
            int id2 = BlockESP.getBlockIds().get(i2);
            if (id2 == number) {
                BlockESP.getBlockIds().remove(i2);
            }
            ++i2;
        }
    }
}

