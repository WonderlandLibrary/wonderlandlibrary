package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GuiItems extends GuiScreen {
    private static int curPage = 1;
    public GuiScreen parent;
    FontManager fM = Client.main().fontMgr();
    private GuiScreen parentScreen;

    public GuiItems(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        /**
         * X-Koordinate:
         *
         * links: width / 2 - 160
         * mitte: width / 2 - 55
         * rechts: width / 2 + 50
         */

        Keyboard.enableRepeatEvents(true);
        switch (curPage) {
            case 1: {
                buttonList.add(new GuiButton(2, width / 2 - 160, 60, 100, 20, "Crash Chest", new String[]{"§6Wenn man diese Kiste kopiert, dann fängt es irgendwann", "§6an, §cheftig §6zu §claggen §6und §ckickt §6einen §cvom Server", "", "§4Achtung: Nicht im Inventar machen, sonst war's das ..."}));
                buttonList.add(new GuiButton(3, width / 2 - 55, 60, 100, 20, "Killer Potion", new String[]{"§6Tötet §calle §6abgeworfenen Entitäten, §cauch §6Spieler §cim Kreativmodus"}));
                buttonList.add(new GuiButton(4, width / 2 + 50, 60, 100, 20, "Troll Potion", new String[]{"§6Gibt abgeworfenen Entitäten §calle Effekte, §6die", "§6existieren auf §cmaximaler Stufe §6mit §cmaximaler Dauer"}));
                buttonList.add(new GuiButton(5, width / 2 - 160, 85, 100, 20, "Crash Hopper", new String[]{"§6Sollte man besser §cnicht rechtsklicken§6, da es den MC-Client §csofort crasht§6", "", "§eHinweis: Dieser Client ist dagegen geschützt!"}));
                buttonList.add(new GuiButton(6, width / 2 - 55, 85, 100, 20, "Better ArmorStand", new String[]{"§6Ein ArmorStand §cohne BasePlate §6und §cmit Armen"}));
                buttonList.add(new GuiButton(7, width / 2 + 50, 85, 100, 20, "OP Book", new String[]{"§6Gibt §cjedem Spieler §6auf dem Server §cOP-Rechte§6, wenn", "§6ein §cServer-Admin §6auf den §cText im Buch §6klickt"}));
                buttonList.add(new GuiButton(8, width / 2 - 160, 110, 100, 20, "Crash Sword", new String[]{"§6Lässt Spieler §ccrashen§6, wenn sie §cmit §6dem §cSchwert laufen"}));
                buttonList.add(new GuiButton(9, width / 2 - 55, 110, 100, 20, "TNT Spawner", new String[]{"§6Spawnt §cmehrere Millionen TNT §6in deiner Nähe §cinnerhalb weniger Sekunden", "", "§4Achtung: Kann zu hohen Lags führen! §eDisconnect empfohlen!"}));
                buttonList.add(new GuiButton(10, width / 2 + 50, 110, 100, 20, "Misc Kit", new String[]{"§6Gibt dir das §cultimative Miscellaneous Kit", "", "§eEnthält Items wie Erzblöcke, Bugitems, Baumaterial, etc. pp."}));
                break;
            }
            case 2: {
                buttonList.add(new GuiButton(11, width / 2 - 160, 60, 100, 20, "Barriere", new String[]{"§6Einfacher Barrierenblock", "", "§eNicht im Inventar des Kreativmodus verfügbar"}));
                buttonList.add(new GuiButton(12, width / 2 - 55, 60, 100, 20, "Befehlsblock", new String[]{"§6Einfacher Befehlsblock", "", "§eNicht im Inventar des Kreativmodus verfügbar"}));
                buttonList.add(new GuiButton(13, width / 2 + 50, 60, 100, 20, "Drachenei", new String[]{"§6Einfaches Drachenei", "", "§eNicht im Inventar des Kreativmodus verfügbar"}));
                buttonList.add(new GuiButton(14, width / 2 - 160, 85, 100, 20, "Spawner", new String[]{"§6Einfacher Spawner", "", "§eNicht im Inventar des Kreativmodus verfügbar"}));
                break;
            }
        }

        buttonList.add(new GuiButton(100, width / 2 - 110, 150, 100, 20, "Bauen umschalten", new String[]{"§6Schaltet §cclientseitig §6den Baumodus um"}));
        buttonList.add(new GuiButton(101, width / 2 - 4, 150, 100, 20, "Fliegen umschalten", new String[]{"§6Schaltet §cclientseitig §6den Flugmodus um"}));

        buttonList.add(new GuiButton(0, width / 2 - 116, height / 2 + 5, 100, 20, "Vorherige Seite", new String[]{"§eWechselt zur vorherigen Seite"}));
        buttonList.add(new GuiButton(1, width / 2 + 2, height / 2 + 5, 100, 20, "Nächste Seite", new String[]{"§eWechselt zur nächsten Seite"}));
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (!(curPage <= 1)) {
                    curPage -= 1;
                    mc.displayGuiScreen(this);
                }
                break;
            }

            case 1: {
                if (!(curPage >= 2)) {
                    curPage += 1;
                    mc.displayGuiScreen(this);
                }
                break;
            }

            case 2: {
                crashChest();
                break;
            }
            case 3: {
                killerPotion();
                break;
            }
            case 4: {
                trollPotion();
                break;
            }
            case 5: {
                crashHopper();
                break;
            }
            case 6: {
                betterArmorStand();
                break;
            }
            case 7: {
                opBook();
                break;
            }
            case 8: {
                crashSword();
                break;
            }
            case 9: {
                tntSpawner();
                break;
            }
            case 10: {
                miscKit();
                break;
            }
            case 11: {
                barrier();
                break;
            }
            case 12: {
                commandBlock();
                break;
            }
            case 13: {
                dragonEgg();
                break;
            }
            case 14: {
                spawner();
                break;
            }

            case 100: {
                mc.thePlayer.capabilities.allowEdit = !mc.thePlayer.capabilities.allowEdit;
                break;
            }
            case 101: {
                mc.thePlayer.capabilities.allowFlying = !mc.thePlayer.capabilities.allowFlying;
                break;
            }
        }
    }

    private void spawner() {
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.mob_spawner);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast einen §cSpawner §rerhalten!", NotificationType.INFO, 5);
    }

    private void dragonEgg() {
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.dragon_egg);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cDrachenei §rerhalten!", NotificationType.INFO, 5);
    }

    private void commandBlock() {
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.command_block);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast einen §cBefehlsblock §rerhalten!", NotificationType.INFO, 5);
    }

    private void barrier() {
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.barrier);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast eine §cBarriere §rerhalten!", NotificationType.INFO, 5);
    }
    
    private  void miscKit() {
        String itemName = "§cMisc§6Kit";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.hopper);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{BlockEntityTag:{TransferCooldown:0,Items:[0:{Slot:0b,id:\"minecraft:dispenser\",Count:127b,tag:{BlockEntityTag:{CustomName:\"§6§lOres\",Items:[0:{Slot:0b,id:\"minecraft:coal_ore\",Count:127b,Damage:0s},1:{Slot:1b,id:\"minecraft:iron_ore\",Count:127b,Damage:0s},2:{Slot:2b,id:\"minecraft:gold_ore\",Count:127b,Damage:0s},3:{Slot:3b,id:\"minecraft:lapis_ore\",Count:127b,Damage:0s},4:{Slot:4b,id:\"minecraft:nether_star\",Count:127b,Damage:0s},5:{Slot:5b,id:\"minecraft:redstone_ore\",Count:127b,Damage:0s},6:{Slot:6b,id:\"minecraft:diamond_ore\",Count:127b,Damage:0s},7:{Slot:7b,id:\"minecraft:emerald_ore\",Count:127b,Damage:0s},8:{Slot:8b,id:\"minecraft:quartz_ore\",Count:127b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lOres\"}},Damage:0s},1:{Slot:1b,id:\"minecraft:dispenser\",Count:127b,tag:{BlockEntityTag:{CustomName:\"§6§lRessource-Blocks\",Items:[0:{Slot:0b,id:\"minecraft:coal_block\",Count:127b,Damage:0s},1:{Slot:1b,id:\"minecraft:iron_block\",Count:127b,Damage:0s},2:{Slot:2b,id:\"minecraft:gold_block\",Count:127b,Damage:0s},3:{Slot:3b,id:\"minecraft:lapis_block\",Count:127b,Damage:0s},4:{Slot:4b,id:\"minecraft:beacon\",Count:127b,Damage:0s},5:{Slot:5b,id:\"minecraft:redstone_block\",Count:127b,Damage:0s},6:{Slot:6b,id:\"minecraft:diamond_block\",Count:127b,Damage:0s},7:{Slot:7b,id:\"minecraft:emerald_block\",Count:127b,Damage:0s},8:{Slot:8b,id:\"minecraft:quartz_block\",Count:127b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lRessource-Blocks\"}},Damage:0s},2:{Slot:2b,id:\"minecraft:hopper\",Count:127b,tag:{BlockEntityTag:{TransferCooldown:0,Items:[0:{Slot:0b,id:\"minecraft:dispenser\",Count:-1b,tag:{BlockEntityTag:{Items:[0:{Slot:0b,id:\"minecraft:coal_ore\",Count:-1b,Damage:0s},1:{Slot:1b,id:\"minecraft:iron_ore\",Count:-1b,Damage:0s},2:{Slot:2b,id:\"minecraft:gold_ore\",Count:-1b,Damage:0s},3:{Slot:3b,id:\"minecraft:lapis_ore\",Count:-1b,Damage:0s},4:{Slot:4b,id:\"minecraft:nether_star\",Count:127b,Damage:0s},5:{Slot:5b,id:\"minecraft:redstone_ore\",Count:-1b,Damage:0s},6:{Slot:6b,id:\"minecraft:diamond_ore\",Count:-1b,Damage:0s},7:{Slot:7b,id:\"minecraft:emerald_ore\",Count:-1b,Damage:0s},8:{Slot:8b,id:\"minecraft:quartz_ore\",Count:-1b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lOres\"}},Damage:0s},1:{Slot:1b,id:\"minecraft:dispenser\",Count:-1b,tag:{BlockEntityTag:{Items:[0:{Slot:0b,id:\"minecraft:coal_block\",Count:-1b,Damage:0s},1:{Slot:1b,id:\"minecraft:iron_block\",Count:-1b,Damage:0s},2:{Slot:2b,id:\"minecraft:gold_block\",Count:-1b,Damage:0s},3:{Slot:3b,id:\"minecraft:lapis_block\",Count:-1b,Damage:0s},4:{Slot:4b,id:\"minecraft:beacon\",Count:-1b,Damage:0s},5:{Slot:5b,id:\"minecraft:redstone_block\",Count:-1b,Damage:0s},6:{Slot:6b,id:\"minecraft:diamond_block\",Count:-1b,Damage:0s},7:{Slot:7b,id:\"minecraft:emerald_block\",Count:-1b,Damage:0s},8:{Slot:8b,id:\"minecraft:quartz_block\",Count:-1b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lRessource-Blocks\"}},Damage:0s},2:{Slot:2b,id:\"minecraft:obsidian\",Count:-1b,tag:{display:{Name:\"§c§lTest with me ;)\"}},Damage:0s},3:{Slot:3b,id:\"minecraft:dispenser\",Count:-1b,tag:{BlockEntityTag:{Items:[0:{Slot:0b,id:\"minecraft:sand\",Count:-1b,Damage:0s},1:{Slot:1b,id:\"minecraft:sand\",Count:-1b,Damage:1s},2:{Slot:2b,id:\"minecraft:gravel\",Count:-1b,Damage:0s},3:{Slot:3b,id:\"minecraft:clay\",Count:-1b,Damage:0s},4:{Slot:4b,id:\"minecraft:bedrock\",Count:-1b,Damage:0s},5:{Slot:5b,id:\"minecraft:sponge\",Count:-1b,Damage:0s},6:{Slot:6b,id:\"minecraft:grass\",Count:-1b,Damage:0s},7:{Slot:7b,id:\"minecraft:dirt\",Count:-1b,Damage:2s},8:{Slot:8b,id:\"minecraft:mycelium\",Count:-1b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lExtra-Blocks\"}},Damage:0s},4:{Slot:4b,id:\"minecraft:dispenser\",Count:-1b,tag:{BlockEntityTag:{Items:[0:{Slot:0b,id:\"minecraft:snow\",Count:-1b,Damage:0s},1:{Slot:1b,id:\"minecraft:ice\",Count:-1b,Damage:0s},2:{Slot:2b,id:\"minecraft:packed_ice\",Count:-1b,Damage:0s},3:{Slot:3b,id:\"minecraft:hay_block\",Count:-1b,Damage:0s},4:{Slot:4b,id:\"minecraft:glowstone\",Count:-1b,Damage:0s},5:{Slot:5b,id:\"minecraft:brick_block\",Count:-1b,Damage:0s},6:{Slot:6b,id:\"minecraft:netherrack\",Count:-1b,Damage:0s},7:{Slot:7b,id:\"minecraft:soul_sand\",Count:-1b,Damage:0s},8:{Slot:8b,id:\"minecraft:nether_brick\",Count:-1b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lExtra-Blocks2\"}},Damage:0s}],id:\"Hopper\",Lock:\"\"},display:{Name:\"§c§lBackUp#NoLimit\"}},Damage:0s},3:{Slot:3b,id:\"minecraft:dispenser\",Count:127b,tag:{BlockEntityTag:{CustomName:\"§6§lExtra-Blocks\",Items:[0:{Slot:0b,id:\"minecraft:sand\",Count:127b,Damage:0s},1:{Slot:1b,id:\"minecraft:sand\",Count:127b,Damage:1s},2:{Slot:2b,id:\"minecraft:gravel\",Count:127b,Damage:0s},3:{Slot:3b,id:\"minecraft:clay\",Count:127b,Damage:0s},4:{Slot:4b,id:\"minecraft:bedrock\",Count:127b,Damage:0s},5:{Slot:5b,id:\"minecraft:sponge\",Count:127b,Damage:0s},6:{Slot:6b,id:\"minecraft:grass\",Count:127b,Damage:0s},7:{Slot:7b,id:\"minecraft:dirt\",Count:127b,Damage:2s},8:{Slot:8b,id:\"minecraft:mycelium\",Count:127b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lExtra-Blocks-1\"}},Damage:0s},4:{Slot:4b,id:\"minecraft:dispenser\",Count:127b,tag:{BlockEntityTag:{CustomName:\"§6§lExtra-Blocks2\",Items:[0:{Slot:0b,id:\"minecraft:snow\",Count:127b,Damage:0s},1:{Slot:1b,id:\"minecraft:ice\",Count:127b,Damage:0s},2:{Slot:2b,id:\"minecraft:packed_ice\",Count:127b,Damage:0s},3:{Slot:3b,id:\"minecraft:hay_block\",Count:127b,Damage:0s},4:{Slot:4b,id:\"minecraft:glowstone\",Count:127b,Damage:0s},5:{Slot:5b,id:\"minecraft:brick_block\",Count:127b,Damage:0s},6:{Slot:6b,id:\"minecraft:netherrack\",Count:127b,Damage:0s},7:{Slot:7b,id:\"minecraft:soul_sand\",Count:127b,Damage:0s},8:{Slot:8b,id:\"minecraft:nether_brick\",Count:127b,Damage:0s}],id:\"Trap\",Lock:\"\"},display:{Name:\"§6§lExtra-Blocks-2\"}},Damage:0s}],id:\"Hopper\",Lock:\"\"},display:{Lore:[0:\"§r§1§l§ncreated by Kenni.\"]}}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast das §cMisc Kit §rerhalten!", NotificationType.INFO, 5);
    }

    private void tntSpawner() {
        String itemName = "§cTNT§6Spawner";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.mob_spawner);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{display:{Name:\"1000*100 TNT Spawner\"},BlockEntityTag:{EntityId:FallingSand,SpawnData:{Motion:[0.0,0.0,0.0],Block:mob_spawner,Data:0,TileEntityData:{EntityId:PrimedTnt,SpawnData:{Motion:[0.0,0.0,0.0],Fuse:40,CustomNameVisible:1},SpawnCount:1000,SpawnRange:10,RequiredPlayerRange:100,Delay:0,MaxNearbyEntities:100},Time:1,DropItem:0},SpawnCount:100,SpawnRange:100,RequiredPlayerRange:100,Delay:20,MaxNearbyEntities:100}}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cTNT Spawner §rerhalten!", NotificationType.INFO, 5);
    }

    private void crashSword() {
        String itemName = "§cCrash§6Sword";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Items.diamond_sword);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{ench:[0:{lvl:32767,id:21}]}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cCrash Sword §rerhalten!", NotificationType.INFO, 5);
    }

    private void opBook() {
        String itemName = "§aPuzzle (Spiel)";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Items.written_book);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{pages:[\"{\\\"text\\\":\\\"[Spiel starten]\\\",\\\"color\\\":\\\"gold\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op *\\\"}}\"],title:\"Custom Book\",author:Server}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cOP Book §rerhalten!", NotificationType.INFO, 5);
    }

    private void betterArmorStand() {
        String itemName = "§aBetter §cArmor§6Stand";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Items.armor_stand);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{EntityTag:{ShowArms:1,NoBasePlate:1}}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cBetter ArmorStand §rerhalten!", NotificationType.INFO, 5);
    }

    private void crashHopper() {
        String itemName = "§cCrash§6Hopper";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack itemStack = new ItemStack(Blocks.hopper);
        NBTTagList nbtTagList = new NBTTagList();
        try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson("{BlockEntityTag:{Items:[{id:skull,Count:64,Slot:0,tag:{SkullOwner:{Id:\"0\"}}}]}}"));
        } catch (NBTException ignored) {
        }
        itemStack.setTagInfo("CustomNBT", nbtTagList);
        itemStack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itemStack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast ein §cCrashHopper §rerhalten!", NotificationType.INFO, 5);
    }

    private void trollPotion() {
        String itemName = "§cTroll§6Potion";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack stack = new ItemStack(Items.potionitem);
        stack.setItemDamage(16384);
        NBTTagList effects = new NBTTagList();
        for (int i = 1; i <= 23; i++) {
            NBTTagCompound effect = new NBTTagCompound();
            effect.setInteger("Amplifier", Integer.MAX_VALUE);
            effect.setInteger("Duration", Integer.MAX_VALUE);
            effect.setInteger("Id", i);
            effects.appendTag(effect);
        }
        stack.setTagInfo("CustomPotionEffects", effects);
        stack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast eine §cTrollPotion §rerhalten!", NotificationType.INFO, 5);
    }

    private void killerPotion() {
        String itemName = "§cKiller§6Potion";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(0) != null) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür den ersten Slot in der Hotbar leeren!", NotificationType.ERROR, 5);
            return;
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack stack = new ItemStack(Items.potionitem);
        stack.setItemDamage(16384);
        NBTTagList effects = new NBTTagList();
        NBTTagCompound effect = new NBTTagCompound();
        effect.setInteger("Amplifier", 125);
        effect.setInteger("Duration", 2000);
        effect.setInteger("Id", 6);
        effects.appendTag(effect);
        stack.setTagInfo("CustomPotionEffects", effects);
        stack.setStackDisplayName(itemName);
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast eine §cKillerPotion §rerhalten!", NotificationType.INFO, 5);
    }

    private void crashChest() {
        String itemName = "§cCopy Me!";
        if (Wrapper.mc.thePlayer.inventory.getStackInSlot(36) != null) {
            if (Wrapper.mc.thePlayer.inventory.getStackInSlot(36).getDisplayName().equals(itemName)) {
                mc.displayGuiScreen(null);
                notify.notification("Aktion abgebrochen!", "Du hast bereits eine §cCrashChest§r!", NotificationType.INFO, 5);
                return;
            } else {
                mc.displayGuiScreen(null);
                notify.notification("Nicht verfügbar!", "Du musst hierfür deine Schuhe ausziehen!", NotificationType.ERROR, 5);
                return;
            }
        } else if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            mc.displayGuiScreen(null);
            notify.notification("Nicht verfügbar!", "Du musst hierfür im Kreativmodus sein!", NotificationType.ERROR, 5);
            return;
        }
        ItemStack stack = new ItemStack(Blocks.chest);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        NBTTagList nbtList = new NBTTagList();
        for (int i = 0; i < 40000; i++)
            nbtList.appendTag(new NBTTagList());
        nbtTagCompound.setTag("www.masterof13fps.com", nbtList);
        stack.setTagInfo("www.masterof13fps.com", nbtTagCompound);
        Wrapper.mc.thePlayer.getInventory()[0] = stack;
        stack.setStackDisplayName(itemName);
        mc.displayGuiScreen(null);
        notify.notification("Item erhalten", "Du hast eine §cCrashChest §rerhalten!", NotificationType.INFO, 5);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            mc.displayGuiScreen(null);
        }
    }

    public void drawScreen(int posX, int posY, float f) {
        ScaledResolution sr = new ScaledResolution(Wrapper.mc);

        UnicodeFontRenderer titleFont = fM.font("Comfortaa", 24, Font.PLAIN);
        String title = "Items & PlayerUtilities";

        RenderUtils.drawRect(5, 5, width - 5, height - 5, new Color(0, 0, 0, 155).getRGB());

        // Button Background
        RenderUtils.drawRoundedRect(width / 2 - 165, 55, 315, 115, 10, new Color(0, 0, 0).getRGB());

        // Seitenwechsler
        RenderUtils.drawRoundedRect(width / 2 - 120, height / 2, 220, 25, 10, new Color(0, 0, 0).getRGB());
        String page = String.valueOf(curPage);
        UnicodeFontRenderer bigNoodleTitling20 = Client.main().fontMgr().font("BigNoodleTitling", 24, Font.PLAIN);
        bigNoodleTitling20.drawStringWithShadow(page, width / 2 - bigNoodleTitling20.getStringWidth(page) / 2 - 8, height / 2 + 10, -1);

        titleFont.drawStringWithShadow(title, width / 2 - titleFont.getStringWidth(title) / 2, 10, -1);

        super.drawScreen(posX, posY, f);

        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i) instanceof GuiButton) {
                GuiButton btn = buttonList.get(i);
                if (btn.isMouseOver() && btn.buttonDesc != null) {
                    List<String> temp = Arrays.asList(btn.buttonDesc);
                    drawHoveringText(temp, posX, posY);
                }
            }
        }
    }
}