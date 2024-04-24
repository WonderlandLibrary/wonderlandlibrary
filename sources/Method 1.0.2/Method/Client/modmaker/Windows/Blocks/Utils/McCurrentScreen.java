package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;

public class McCurrentScreen extends Block {

    public McCurrentScreen() {
        super("McCurrentScreen", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("GuiInventory");
        ddOptions.add("GuiBeacon");
        ddOptions.add("GuiBrewingStand");
        ddOptions.add("GuiChest");
        ddOptions.add("GuiContainer");
        ddOptions.add("GuiContainerCreative");
        ddOptions.add("GuiCrafting");
        ddOptions.add("GuiDispenser");
        ddOptions.add("GuiEditCommandBlockMinecart");
        ddOptions.add("GuiEditSign");
        ddOptions.add("GuiEditStructure");
        ddOptions.add("GuiFurnace");
        ddOptions.add("GuiInventory");
        ddOptions.add("GuiScreenHorseInventory");
        ddOptions.add("GuiShulkerBox");
        ddOptions.add("GuiChat");
        ddOptions.add("GuiCommandBlock");
        ddOptions.add("GuiConfirmOpenLink");
        ddOptions.add("GuiControls");
        ddOptions.add("GuiCreateFlatWorld");
        ddOptions.add("GuiCreateWorld");
        ddOptions.add("GuiCustomizeSkin");
        ddOptions.add("GuiCustomizeWorldScreen");
        ddOptions.add("GuiDisconnected");
        ddOptions.add("GuiDownloadTerrain");
        ddOptions.add("GuiEnchantment");
        ddOptions.add("GuiErrorScreen");
        ddOptions.add("GuiFlatPresets");
        ddOptions.add("GuiGameOver");
        ddOptions.add("GuiHopper");
        ddOptions.add("GuiIngameMenu");
        ddOptions.add("GuiLanguage");
        ddOptions.add("GuiMainMenu");
        ddOptions.add("GuiMemoryErrorScreen");
        ddOptions.add("GuiMerchant");
        ddOptions.add("GuiMultiplayer");
        ddOptions.add("GuiOptions");
        ddOptions.add("GuiRepair");
        ddOptions.add("GuiScreenAddServer");
        ddOptions.add("GuiScreenBook");
        ddOptions.add("GuiScreenCustomizePresets");
        ddOptions.add("GuiScreenDemo");
        ddOptions.add("GuiScreenOptionsSounds");
        ddOptions.add("GuiScreenRealmsProxy");
        ddOptions.add("GuiScreenResourcePacks");
        ddOptions.add("GuiScreenServerList");
        ddOptions.add("GuiScreenWorking");
        ddOptions.add("GuiShareToLan");
        ddOptions.add("GuiSleepMP");
        ddOptions.add("GuiSnooper");
        ddOptions.add("GuiVideoSettings");
        ddOptions.add("GuiWinGame");
        ddOptions.add("GuiWorldEdit");
        ddOptions.add("GuiWorldSelection");
        ddOptions.add("GuiYesNo");
        this.description = "Returns true if the current screen is the one specified.";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "GuiBeacon":
                return Minecraft.getMinecraft().currentScreen instanceof GuiBeacon;
            case "GuiBrewingStand":
                return Minecraft.getMinecraft().currentScreen instanceof GuiBrewingStand;
            case "GuiChest":
                return Minecraft.getMinecraft().currentScreen instanceof GuiChest;
            case "GuiContainer":
                return Minecraft.getMinecraft().currentScreen instanceof GuiContainer;

            case "GuiContainerCreative":
                return Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative;

            case "GuiCrafting":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCrafting;

            case "GuiDispenser":
                return Minecraft.getMinecraft().currentScreen instanceof GuiDispenser;

            case "GuiEditCommandBlockMinecart":
                return Minecraft.getMinecraft().currentScreen instanceof GuiEditCommandBlockMinecart;

            case "GuiEditSign":
                return Minecraft.getMinecraft().currentScreen instanceof GuiEditSign;

            case "GuiEditStructure":
                return Minecraft.getMinecraft().currentScreen instanceof GuiEditStructure;

            case "GuiFurnace":
                return Minecraft.getMinecraft().currentScreen instanceof GuiFurnace;

            case "GuiInventory":
                return Minecraft.getMinecraft().currentScreen instanceof GuiInventory;

            case "GuiScreenHorseInventory":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenHorseInventory;

            case "GuiShulkerBox":
                return Minecraft.getMinecraft().currentScreen instanceof GuiShulkerBox;


            case "GuiChat":
                return Minecraft.getMinecraft().currentScreen instanceof GuiChat;


            case "GuiCommandBlock":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCommandBlock;

            case "GuiConfirmOpenLink":
                return Minecraft.getMinecraft().currentScreen instanceof GuiConfirmOpenLink;

            case "GuiControls":
                return Minecraft.getMinecraft().currentScreen instanceof GuiControls;

            case "GuiCreateFlatWorld":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCreateFlatWorld;

            case "GuiCreateWorld":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCreateWorld;

            case "GuiCustomizeSkin":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCustomizeSkin;

            case "GuiCustomizeWorldScreen":
                return Minecraft.getMinecraft().currentScreen instanceof GuiCustomizeWorldScreen;

            case "GuiDisconnected":
                return Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected;

            case "GuiDownloadTerrain":
                return Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain;


            case "GuiEnchantment":
                return Minecraft.getMinecraft().currentScreen instanceof GuiEnchantment;

            case "GuiErrorScreen":
                return Minecraft.getMinecraft().currentScreen instanceof GuiErrorScreen;

            case "GuiFlatPresets":
                return Minecraft.getMinecraft().currentScreen instanceof GuiFlatPresets;

            case "GuiGameOver":
                return Minecraft.getMinecraft().currentScreen instanceof GuiGameOver;

            case "GuiHopper":
                return Minecraft.getMinecraft().currentScreen instanceof GuiHopper;


            case "GuiIngameMenu":
                return Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu;


            case "GuiLanguage":
                return Minecraft.getMinecraft().currentScreen instanceof GuiLanguage;


            case "GuiMainMenu":
                return Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu;

            case "GuiMemoryErrorScreen":
                return Minecraft.getMinecraft().currentScreen instanceof GuiMemoryErrorScreen;

            case "GuiMerchant":
                return Minecraft.getMinecraft().currentScreen instanceof GuiMerchant;

            case "GuiMultiplayer":
                return Minecraft.getMinecraft().currentScreen instanceof GuiMultiplayer;


            case "GuiOptions":
                return Minecraft.getMinecraft().currentScreen instanceof GuiOptions;

            case "GuiRepair":
                return Minecraft.getMinecraft().currentScreen instanceof GuiRepair;


            case "GuiScreenAddServer":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenAddServer;

            case "GuiScreenBook":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenBook;

            case "GuiScreenCustomizePresets":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenCustomizePresets;

            case "GuiScreenDemo":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenDemo;

            case "GuiScreenOptionsSounds":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenOptionsSounds;

            case "GuiScreenRealmsProxy":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenRealmsProxy;

            case "GuiScreenResourcePacks":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenResourcePacks;

            case "GuiScreenServerList":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenServerList;

            case "GuiScreenWorking":
                return Minecraft.getMinecraft().currentScreen instanceof GuiScreenWorking;

            case "GuiShareToLan":
                return Minecraft.getMinecraft().currentScreen instanceof GuiShareToLan;


            case "GuiSleepMP":
                return Minecraft.getMinecraft().currentScreen instanceof GuiSleepMP;


            case "GuiSnooper":
                return Minecraft.getMinecraft().currentScreen instanceof GuiSnooper;


            case "GuiVideoSettings":
                return Minecraft.getMinecraft().currentScreen instanceof GuiVideoSettings;

            case "GuiWinGame":
                return Minecraft.getMinecraft().currentScreen instanceof GuiWinGame;

            case "GuiWorldEdit":
                return Minecraft.getMinecraft().currentScreen instanceof GuiWorldEdit;

            case "GuiWorldSelection":
                return Minecraft.getMinecraft().currentScreen instanceof GuiWorldSelection;

            case "GuiYesNo":
                return Minecraft.getMinecraft().currentScreen instanceof GuiYesNo;

        }
        return false;
    }


}
