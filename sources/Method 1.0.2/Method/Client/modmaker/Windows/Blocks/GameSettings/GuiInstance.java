package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.gui.*;

public class GuiInstance extends Block {

    public GuiInstance() {
        super("GuiInstance", MainBlockType.Boolean, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Gui));
        ddOptions.add("Gui");
        ddOptions.add("GuiBossOverlay");
        ddOptions.add("GuiButton");
        ddOptions.add("GuiButtonImage");
        ddOptions.add("GuiButtonLanguage");
        ddOptions.add("GuiButtonRealmsProxy");
        ddOptions.add("GuiButtonToggle");
        ddOptions.add("GuiChat");
        ddOptions.add("GuiClickableScrolledSelectionListProxy");
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
        ddOptions.add("GuiIngame");
        ddOptions.add("GuiIngameMenu");
        ddOptions.add("GuiKeyBindingList");
        ddOptions.add("GuiLabel");
        ddOptions.add("GuiLanguage");
        ddOptions.add("GuiListButton");
        ddOptions.add("GuiListExtended");
        ddOptions.add("GuiListWorldSelection");
        ddOptions.add("GuiListWorldSelectionEntry");
        ddOptions.add("GuiLockIconButton");
        ddOptions.add("GuiMainMenu");
        ddOptions.add("GuiMemoryErrorScreen");
        ddOptions.add("GuiMerchant");
        ddOptions.add("GuiMultiplayer");
        ddOptions.add("GuiNewChat");
        ddOptions.add("GuiOptionButton");
        ddOptions.add("GuiOptions");
        ddOptions.add("GuiOptionSlider");
        ddOptions.add("GuiOptionsRowList");
        ddOptions.add("GuiOverlayDebug");
        ddOptions.add("GuiPageButtonList");
        ddOptions.add("GuiPlayerTabOverlay");
        ddOptions.add("GuiRepair");
        ddOptions.add("GuiResourcePackAvailable");
        ddOptions.add("GuiResourcePackList");
        ddOptions.add("GuiResourcePackSelected");
        ddOptions.add("GuiScreen");
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
        ddOptions.add("GuiSimpleScrolledSelectionListProxy");
        ddOptions.add("GuiSleepMP");
        ddOptions.add("GuiSlider");
        ddOptions.add("GuiSlot");
        ddOptions.add("GuiSlotRealmsProxy");
        ddOptions.add("GuiSnooper");
        ddOptions.add("GuiSpectator");
        ddOptions.add("GuiSubtitleOverlay");
        ddOptions.add("GuiTextField");
        ddOptions.add("GuiUtilRenderComponents");
        ddOptions.add("GuiVideoSettings");
        ddOptions.add("GuiWinGame");
        ddOptions.add("GuiWorldEdit");
        ddOptions.add("GuiWorldSelection");
        ddOptions.add("GuiYesNo");


        this.description = "";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Gui":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof Gui;
            case "GuiBossOverlay":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiBossOverlay;
            case "GuiButton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiButton;
            case "GuiButtonImage":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiButtonImage;
            case "GuiButtonLanguage":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiButtonLanguage;
            case "GuiButtonRealmsProxy":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiButtonRealmsProxy;
            case "GuiButtonToggle":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiButtonToggle;
            case "GuiChat":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiChat;
            case "GuiClickableScrolledSelectionListProxy":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiClickableScrolledSelectionListProxy;
            case "GuiCommandBlock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiCommandBlock;
            case "GuiConfirmOpenLink":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiConfirmOpenLink;
            case "GuiControls":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiControls;
            case "GuiCreateFlatWorld":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiCreateFlatWorld;
            case "GuiCreateWorld":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiCreateWorld;
            case "GuiCustomizeSkin":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiCustomizeSkin;
            case "GuiCustomizeWorldScreen":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiCustomizeWorldScreen;
            case "GuiDisconnected":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiDisconnected;
            case "GuiDownloadTerrain":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiDownloadTerrain;
            case "GuiEnchantment":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiEnchantment;
            case "GuiErrorScreen":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiErrorScreen;
            case "GuiFlatPresets":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiFlatPresets;
            case "GuiGameOver":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiGameOver;
            case "GuiHopper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiHopper;
            case "GuiIngame":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiIngame;
            case "GuiIngameMenu":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiIngameMenu;
            case "GuiKeyBindingList":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiKeyBindingList;
            case "GuiLabel":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiLabel;
            case "GuiLanguage":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiLanguage;
            case "GuiListButton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiListButton;
            case "GuiListExtended":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiListExtended;
            case "GuiListWorldSelection":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiListWorldSelection;
            case "GuiListWorldSelectionEntry":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiListWorldSelectionEntry;
            case "GuiLockIconButton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiLockIconButton;
            case "GuiMainMenu":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiMainMenu;
            case "GuiMemoryErrorScreen":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiMemoryErrorScreen;
            case "GuiMerchant":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiMerchant;
            case "GuiMultiplayer":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiMultiplayer;
            case "GuiNewChat":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiNewChat;
            case "GuiOptionButton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiOptionButton;
            case "GuiOptions":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiOptions;
            case "GuiOptionSlider":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiOptionSlider;
            case "GuiOptionsRowList":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiOptionsRowList;
            case "GuiOverlayDebug":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiOverlayDebug;
            case "GuiPageButtonList":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiPageButtonList;
            case "GuiPlayerTabOverlay":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiPlayerTabOverlay;
            case "GuiRepair":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiRepair;
            case "GuiResourcePackAvailable":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiResourcePackAvailable;
            case "GuiResourcePackList":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiResourcePackList;
            case "GuiResourcePackSelected":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiResourcePackSelected;
            case "GuiScreen":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreen;
            case "GuiScreenAddServer":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenAddServer;
            case "GuiScreenBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenBook;
            case "GuiScreenCustomizePresets":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenCustomizePresets;
            case "GuiScreenDemo":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenDemo;
            case "GuiScreenOptionsSounds":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenOptionsSounds;
            case "GuiScreenRealmsProxy":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenRealmsProxy;
            case "GuiScreenResourcePacks":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenResourcePacks;
            case "GuiScreenServerList":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenServerList;
            case "GuiScreenWorking":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiScreenWorking;
            case "GuiShareToLan":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiShareToLan;
            case "GuiSimpleScrolledSelectionListProxy":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSimpleScrolledSelectionListProxy;
            case "GuiSleepMP":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSleepMP;
            case "GuiSlider":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSlider;
            case "GuiSlot":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSlot;
            case "GuiSlotRealmsProxy":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSlotRealmsProxy;
            case "GuiSnooper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSnooper;
            case "GuiSpectator":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSpectator;
            case "GuiSubtitleOverlay":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiSubtitleOverlay;
            case "GuiTextField":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiTextField;
            case "GuiUtilRenderComponents":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiUtilRenderComponents;
            case "GuiVideoSettings":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiVideoSettings;
            case "GuiWinGame":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiWinGame;
            case "GuiWorldEdit":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiWorldEdit;
            case "GuiWorldSelection":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiWorldSelection;
            case "GuiYesNo":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof GuiYesNo;
            default:
                return false;
        }
    }


}
