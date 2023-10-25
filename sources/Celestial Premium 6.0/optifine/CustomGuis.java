/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import optifine.Config;
import optifine.CustomGuiProperties;
import optifine.PlayerControllerOF;
import optifine.ResUtils;

public class CustomGuis {
    private static Minecraft mc = Config.getInstance();
    private static PlayerControllerOF playerControllerOF = null;
    private static CustomGuiProperties[][] guiProperties = null;
    public static boolean isChristmas = CustomGuis.isChristmas();

    public static ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_0_) {
        if (guiProperties == null) {
            return p_getTextureLocation_0_;
        }
        GuiScreen guiscreen = CustomGuis.mc.currentScreen;
        if (!(guiscreen instanceof GuiContainer)) {
            return p_getTextureLocation_0_;
        }
        if (p_getTextureLocation_0_.getNamespace().equals("minecraft") && p_getTextureLocation_0_.getPath().startsWith("textures/gui/")) {
            Entity entity;
            if (playerControllerOF == null) {
                return p_getTextureLocation_0_;
            }
            WorldClient iblockaccess = CustomGuis.mc.world;
            if (iblockaccess == null) {
                return p_getTextureLocation_0_;
            }
            if (guiscreen instanceof GuiContainerCreative) {
                return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CREATIVE, CustomGuis.mc.player.getPosition(), iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiInventory) {
                return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.INVENTORY, CustomGuis.mc.player.getPosition(), iblockaccess, p_getTextureLocation_0_);
            }
            BlockPos blockpos = playerControllerOF.getLastClickBlockPos();
            if (blockpos != null) {
                if (guiscreen instanceof GuiRepair) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.ANVIL, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiBeacon) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.BEACON, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiBrewingStand) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.BREWING_STAND, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiChest) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CHEST, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiCrafting) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CRAFTING, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiDispenser) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.DISPENSER, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiEnchantment) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.ENCHANTMENT, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiFurnace) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.FURNACE, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiHopper) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.HOPPER, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiShulkerBox) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.SHULKER_BOX, blockpos, iblockaccess, p_getTextureLocation_0_);
                }
            }
            if ((entity = playerControllerOF.getLastClickEntity()) != null) {
                if (guiscreen instanceof GuiScreenHorseInventory) {
                    return CustomGuis.getTextureEntity(CustomGuiProperties.EnumContainer.HORSE, entity, iblockaccess, p_getTextureLocation_0_);
                }
                if (guiscreen instanceof GuiMerchant) {
                    return CustomGuis.getTextureEntity(CustomGuiProperties.EnumContainer.VILLAGER, entity, iblockaccess, p_getTextureLocation_0_);
                }
            }
            return p_getTextureLocation_0_;
        }
        return p_getTextureLocation_0_;
    }

    private static ResourceLocation getTexturePos(CustomGuiProperties.EnumContainer p_getTexturePos_0_, BlockPos p_getTexturePos_1_, IBlockAccess p_getTexturePos_2_, ResourceLocation p_getTexturePos_3_) {
        CustomGuiProperties[] acustomguiproperties = guiProperties[p_getTexturePos_0_.ordinal()];
        if (acustomguiproperties == null) {
            return p_getTexturePos_3_;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (!customguiproperties.matchesPos(p_getTexturePos_0_, p_getTexturePos_1_, p_getTexturePos_2_)) continue;
            return customguiproperties.getTextureLocation(p_getTexturePos_3_);
        }
        return p_getTexturePos_3_;
    }

    private static ResourceLocation getTextureEntity(CustomGuiProperties.EnumContainer p_getTextureEntity_0_, Entity p_getTextureEntity_1_, IBlockAccess p_getTextureEntity_2_, ResourceLocation p_getTextureEntity_3_) {
        CustomGuiProperties[] acustomguiproperties = guiProperties[p_getTextureEntity_0_.ordinal()];
        if (acustomguiproperties == null) {
            return p_getTextureEntity_3_;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (!customguiproperties.matchesEntity(p_getTextureEntity_0_, p_getTextureEntity_1_, p_getTextureEntity_2_)) continue;
            return customguiproperties.getTextureLocation(p_getTextureEntity_3_);
        }
        return p_getTextureEntity_3_;
    }

    public static void update() {
        guiProperties = null;
        if (Config.isCustomGuis()) {
            ArrayList<List<CustomGuiProperties>> list = new ArrayList<List<CustomGuiProperties>>();
            IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                IResourcePack iresourcepack = airesourcepack[i];
                CustomGuis.update(iresourcepack, list);
            }
            guiProperties = CustomGuis.propertyListToArray(list);
        }
    }

    private static CustomGuiProperties[][] propertyListToArray(List<List<CustomGuiProperties>> p_propertyListToArray_0_) {
        if (p_propertyListToArray_0_.isEmpty()) {
            return null;
        }
        CustomGuiProperties[][] acustomguiproperties = new CustomGuiProperties[CustomGuiProperties.EnumContainer.values().length][];
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            List<CustomGuiProperties> list;
            if (p_propertyListToArray_0_.size() <= i || (list = p_propertyListToArray_0_.get(i)) == null) continue;
            CustomGuiProperties[] acustomguiproperties1 = list.toArray(new CustomGuiProperties[list.size()]);
            acustomguiproperties[i] = acustomguiproperties1;
        }
        return acustomguiproperties;
    }

    private static void update(IResourcePack p_update_0_, List<List<CustomGuiProperties>> p_update_1_) {
        String[] astring = ResUtils.collectFiles(p_update_0_, "optifine/gui/container/", ".properties", (String[])null);
        Arrays.sort(astring);
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            Config.dbg("CustomGuis: " + s);
            try {
                ResourceLocation resourcelocation = new ResourceLocation(s);
                InputStream inputstream = p_update_0_.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn("CustomGuis file not found: " + s);
                    continue;
                }
                Properties properties = new Properties();
                properties.load(inputstream);
                inputstream.close();
                CustomGuiProperties customguiproperties = new CustomGuiProperties(properties, s);
                if (!customguiproperties.isValid(s)) continue;
                CustomGuis.addToList(customguiproperties, p_update_1_);
                continue;
            }
            catch (FileNotFoundException var9) {
                Config.warn("CustomGuis file not found: " + s);
                continue;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private static void addToList(CustomGuiProperties p_addToList_0_, List<List<CustomGuiProperties>> p_addToList_1_) {
        if (p_addToList_0_.getContainer() == null) {
            CustomGuis.warn("Invalid container: " + (Object)((Object)p_addToList_0_.getContainer()));
        } else {
            int i = p_addToList_0_.getContainer().ordinal();
            while (p_addToList_1_.size() <= i) {
                p_addToList_1_.add(null);
            }
            List<CustomGuiProperties> list = p_addToList_1_.get(i);
            if (list == null) {
                list = new ArrayList<CustomGuiProperties>();
                p_addToList_1_.set(i, list);
            }
            list.add(p_addToList_0_);
        }
    }

    public static PlayerControllerOF getPlayerControllerOF() {
        return playerControllerOF;
    }

    public static void setPlayerControllerOF(PlayerControllerOF p_setPlayerControllerOF_0_) {
        playerControllerOF = p_setPlayerControllerOF_0_;
    }

    private static boolean isChristmas() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26;
    }

    private static void warn(String p_warn_0_) {
        Config.warn("[CustomGuis] " + p_warn_0_);
    }
}

