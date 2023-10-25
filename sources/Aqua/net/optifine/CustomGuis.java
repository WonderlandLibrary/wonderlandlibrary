package net.optifine;

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
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomGuiProperties;
import net.optifine.override.PlayerControllerOF;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;

public class CustomGuis {
    private static Minecraft mc = Config.getMinecraft();
    private static PlayerControllerOF playerControllerOF = null;
    private static CustomGuiProperties[][] guiProperties = null;
    public static boolean isChristmas = CustomGuis.isChristmas();

    public static ResourceLocation getTextureLocation(ResourceLocation loc) {
        if (guiProperties == null) {
            return loc;
        }
        GuiScreen guiscreen = CustomGuis.mc.currentScreen;
        if (!(guiscreen instanceof GuiContainer)) {
            return loc;
        }
        if (loc.getResourceDomain().equals((Object)"minecraft") && loc.getResourcePath().startsWith("textures/gui/")) {
            Entity entity;
            if (playerControllerOF == null) {
                return loc;
            }
            WorldClient iblockaccess = CustomGuis.mc.theWorld;
            if (iblockaccess == null) {
                return loc;
            }
            if (guiscreen instanceof GuiContainerCreative) {
                return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CREATIVE, CustomGuis.mc.thePlayer.getPosition(), (IBlockAccess)iblockaccess, loc, guiscreen);
            }
            if (guiscreen instanceof GuiInventory) {
                return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.INVENTORY, CustomGuis.mc.thePlayer.getPosition(), (IBlockAccess)iblockaccess, loc, guiscreen);
            }
            BlockPos blockpos = playerControllerOF.getLastClickBlockPos();
            if (blockpos != null) {
                if (guiscreen instanceof GuiRepair) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.ANVIL, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiBeacon) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.BEACON, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiBrewingStand) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.BREWING_STAND, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiChest) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CHEST, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiCrafting) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.CRAFTING, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiDispenser) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.DISPENSER, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiEnchantment) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.ENCHANTMENT, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiFurnace) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.FURNACE, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
                if (guiscreen instanceof GuiHopper) {
                    return CustomGuis.getTexturePos(CustomGuiProperties.EnumContainer.HOPPER, blockpos, (IBlockAccess)iblockaccess, loc, guiscreen);
                }
            }
            if ((entity = playerControllerOF.getLastClickEntity()) != null) {
                if (guiscreen instanceof GuiScreenHorseInventory) {
                    return CustomGuis.getTextureEntity(CustomGuiProperties.EnumContainer.HORSE, entity, (IBlockAccess)iblockaccess, loc);
                }
                if (guiscreen instanceof GuiMerchant) {
                    return CustomGuis.getTextureEntity(CustomGuiProperties.EnumContainer.VILLAGER, entity, (IBlockAccess)iblockaccess, loc);
                }
            }
            return loc;
        }
        return loc;
    }

    private static ResourceLocation getTexturePos(CustomGuiProperties.EnumContainer container, BlockPos pos, IBlockAccess blockAccess, ResourceLocation loc, GuiScreen screen) {
        CustomGuiProperties[] acustomguiproperties = guiProperties[container.ordinal()];
        if (acustomguiproperties == null) {
            return loc;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (!customguiproperties.matchesPos(container, pos, blockAccess, screen)) continue;
            return customguiproperties.getTextureLocation(loc);
        }
        return loc;
    }

    private static ResourceLocation getTextureEntity(CustomGuiProperties.EnumContainer container, Entity entity, IBlockAccess blockAccess, ResourceLocation loc) {
        CustomGuiProperties[] acustomguiproperties = guiProperties[container.ordinal()];
        if (acustomguiproperties == null) {
            return loc;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (!customguiproperties.matchesEntity(container, entity, blockAccess)) continue;
            return customguiproperties.getTextureLocation(loc);
        }
        return loc;
    }

    public static void update() {
        guiProperties = null;
        if (Config.isCustomGuis()) {
            ArrayList list = new ArrayList();
            IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                IResourcePack iresourcepack = airesourcepack[i];
                CustomGuis.update(iresourcepack, (List<List<CustomGuiProperties>>)list);
            }
            guiProperties = CustomGuis.propertyListToArray((List<List<CustomGuiProperties>>)list);
        }
    }

    private static CustomGuiProperties[][] propertyListToArray(List<List<CustomGuiProperties>> listProps) {
        if (listProps.isEmpty()) {
            return null;
        }
        CustomGuiProperties[][] acustomguiproperties = new CustomGuiProperties[CustomGuiProperties.EnumContainer.VALUES.length][];
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            CustomGuiProperties[] acustomguiproperties1;
            List list;
            if (listProps.size() <= i || (list = (List)listProps.get(i)) == null) continue;
            acustomguiproperties[i] = acustomguiproperties1 = (CustomGuiProperties[])list.toArray((Object[])new CustomGuiProperties[list.size()]);
        }
        return acustomguiproperties;
    }

    private static void update(IResourcePack rp, List<List<CustomGuiProperties>> listProps) {
        String[] astring = ResUtils.collectFiles((IResourcePack)rp, (String)"optifine/gui/container/", (String)".properties", (String[])null);
        Arrays.sort((Object[])astring);
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            Config.dbg((String)("CustomGuis: " + s));
            try {
                ResourceLocation resourcelocation = new ResourceLocation(s);
                InputStream inputstream = rp.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn((String)("CustomGuis file not found: " + s));
                    continue;
                }
                PropertiesOrdered properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                CustomGuiProperties customguiproperties = new CustomGuiProperties((Properties)properties, s);
                if (!customguiproperties.isValid(s)) continue;
                CustomGuis.addToList(customguiproperties, listProps);
                continue;
            }
            catch (FileNotFoundException var9) {
                Config.warn((String)("CustomGuis file not found: " + s));
                continue;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private static void addToList(CustomGuiProperties cgp, List<List<CustomGuiProperties>> listProps) {
        if (cgp.getContainer() == null) {
            CustomGuis.warn("Invalid container: " + cgp.getContainer());
        } else {
            int i = cgp.getContainer().ordinal();
            while (listProps.size() <= i) {
                listProps.add(null);
            }
            List list = (List)listProps.get(i);
            if (list == null) {
                list = new ArrayList();
                listProps.set(i, (Object)list);
            }
            list.add((Object)cgp);
        }
    }

    public static PlayerControllerOF getPlayerControllerOF() {
        return playerControllerOF;
    }

    public static void setPlayerControllerOF(PlayerControllerOF playerControllerOF) {
        CustomGuis.playerControllerOF = playerControllerOF;
    }

    private static boolean isChristmas() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26;
    }

    private static void warn(String str) {
        Config.warn((String)("[CustomGuis] " + str));
    }
}
