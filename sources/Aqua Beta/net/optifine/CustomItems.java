package net.optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItemProperties;
import net.optifine.CustomItems;
import net.optifine.CustomItemsComparator;
import net.optifine.config.NbtTagValue;
import net.optifine.render.Blender;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;

public class CustomItems {
    private static CustomItemProperties[][] itemProperties = null;
    private static CustomItemProperties[][] enchantmentProperties = null;
    private static Map mapPotionIds = null;
    private static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static boolean useGlint = true;
    private static boolean renderOffHand = false;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final int MASK_POTION_EXTENDED = 64;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[][] EMPTY_INT2_ARRAY = new int[0][];
    private static final String TYPE_POTION_NORMAL = "normal";
    private static final String TYPE_POTION_SPLASH = "splash";
    private static final String TYPE_POTION_LINGER = "linger";

    public static void update() {
        itemProperties = null;
        enchantmentProperties = null;
        useGlint = true;
        if (Config.isCustomItems()) {
            CustomItems.readCitProperties("mcpatcher/cit.properties");
            IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                IResourcePack iresourcepack = airesourcepack[i];
                CustomItems.update(iresourcepack);
            }
            CustomItems.update((IResourcePack)Config.getDefaultResourcePack());
            if (itemProperties.length <= 0) {
                itemProperties = null;
            }
            if (enchantmentProperties.length <= 0) {
                enchantmentProperties = null;
            }
        }
    }

    private static void readCitProperties(String fileName) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream((ResourceLocation)resourcelocation);
            if (inputstream == null) {
                return;
            }
            Config.dbg((String)("CustomItems: Loading " + fileName));
            PropertiesOrdered properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            useGlint = Config.parseBoolean((String)properties.getProperty("useGlint"), (boolean)true);
        }
        catch (FileNotFoundException var4) {
            return;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    private static void update(IResourcePack rp) {
        Object[] astring = ResUtils.collectFiles((IResourcePack)rp, (String)"mcpatcher/cit/", (String)".properties", (String[])null);
        Map map = CustomItems.makeAutoImageProperties(rp);
        if (map.size() > 0) {
            Set set = map.keySet();
            Object[] astring1 = (String[])set.toArray((Object[])new String[set.size()]);
            astring = (String[])Config.addObjectsToArray((Object[])astring, (Object[])astring1);
        }
        Arrays.sort((Object[])astring);
        List list = CustomItems.makePropertyList(itemProperties);
        List list1 = CustomItems.makePropertyList(enchantmentProperties);
        for (int i = 0; i < astring.length; ++i) {
            Object s = astring[i];
            Config.dbg((String)("CustomItems: " + (String)s));
            try {
                CustomItemProperties customitemproperties = null;
                if (map.containsKey(s)) {
                    customitemproperties = (CustomItemProperties)map.get(s);
                }
                if (customitemproperties == null) {
                    ResourceLocation resourcelocation = new ResourceLocation((String)s);
                    InputStream inputstream = rp.getInputStream(resourcelocation);
                    if (inputstream == null) {
                        Config.warn((String)("CustomItems file not found: " + (String)s));
                        continue;
                    }
                    PropertiesOrdered properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    customitemproperties = new CustomItemProperties((Properties)properties, (String)s);
                }
                if (!customitemproperties.isValid((String)s)) continue;
                CustomItems.addToItemList(customitemproperties, list);
                CustomItems.addToEnchantmentList(customitemproperties, list1);
                continue;
            }
            catch (FileNotFoundException var11) {
                Config.warn((String)("CustomItems file not found: " + (String)s));
                continue;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        itemProperties = CustomItems.propertyListToArray(list);
        enchantmentProperties = CustomItems.propertyListToArray(list1);
        Comparator comparator = CustomItems.getPropertiesComparator();
        for (int j = 0; j < itemProperties.length; ++j) {
            Object[] acustomitemproperties = itemProperties[j];
            if (acustomitemproperties == null) continue;
            Arrays.sort((Object[])acustomitemproperties, (Comparator)comparator);
        }
        for (int k = 0; k < enchantmentProperties.length; ++k) {
            Object[] acustomitemproperties1 = enchantmentProperties[k];
            if (acustomitemproperties1 == null) continue;
            Arrays.sort((Object[])acustomitemproperties1, (Comparator)comparator);
        }
    }

    private static Comparator getPropertiesComparator() {
        1 comparator = new /* Unavailable Anonymous Inner Class!! */;
        return comparator;
    }

    public static void updateIcons(TextureMap textureMap) {
        for (CustomItemProperties customitemproperties : CustomItems.getAllProperties()) {
            customitemproperties.updateIcons(textureMap);
        }
    }

    public static void loadModels(ModelBakery modelBakery) {
        for (CustomItemProperties customitemproperties : CustomItems.getAllProperties()) {
            customitemproperties.loadModels(modelBakery);
        }
    }

    public static void updateModels() {
        for (CustomItemProperties customitemproperties : CustomItems.getAllProperties()) {
            if (customitemproperties.type != 1) continue;
            TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
            customitemproperties.updateModelTexture(texturemap, itemModelGenerator);
            customitemproperties.updateModelsFull();
        }
    }

    private static List<CustomItemProperties> getAllProperties() {
        ArrayList list = new ArrayList();
        CustomItems.addAll(itemProperties, (List<CustomItemProperties>)list);
        CustomItems.addAll(enchantmentProperties, (List<CustomItemProperties>)list);
        return list;
    }

    private static void addAll(CustomItemProperties[][] cipsArr, List<CustomItemProperties> list) {
        if (cipsArr != null) {
            for (int i = 0; i < cipsArr.length; ++i) {
                CustomItemProperties[] acustomitemproperties = cipsArr[i];
                if (acustomitemproperties == null) continue;
                for (int j = 0; j < acustomitemproperties.length; ++j) {
                    CustomItemProperties customitemproperties = acustomitemproperties[j];
                    if (customitemproperties == null) continue;
                    list.add((Object)customitemproperties);
                }
            }
        }
    }

    private static Map makeAutoImageProperties(IResourcePack rp) {
        HashMap map = new HashMap();
        map.putAll(CustomItems.makePotionImageProperties(rp, TYPE_POTION_NORMAL, Item.getIdFromItem((Item)Items.potionitem)));
        map.putAll(CustomItems.makePotionImageProperties(rp, TYPE_POTION_SPLASH, Item.getIdFromItem((Item)Items.potionitem)));
        map.putAll(CustomItems.makePotionImageProperties(rp, TYPE_POTION_LINGER, Item.getIdFromItem((Item)Items.potionitem)));
        return map;
    }

    private static Map makePotionImageProperties(IResourcePack rp, String type, int itemId) {
        HashMap map = new HashMap();
        String s = type + "/";
        String[] astring = new String[]{"mcpatcher/cit/potion/" + s, "mcpatcher/cit/Potion/" + s};
        String[] astring1 = new String[]{".png"};
        String[] astring2 = ResUtils.collectFiles((IResourcePack)rp, (String[])astring, (String[])astring1);
        for (int i = 0; i < astring2.length; ++i) {
            String s1 = astring2[i];
            String name = StrUtils.removePrefixSuffix((String)s1, (String[])astring, (String[])astring1);
            Properties properties = CustomItems.makePotionProperties(name, type, itemId, s1);
            if (properties == null) continue;
            String s3 = StrUtils.removeSuffix((String)s1, (String[])astring1) + ".properties";
            CustomItemProperties customitemproperties = new CustomItemProperties(properties, s3);
            map.put((Object)s3, (Object)customitemproperties);
        }
        return map;
    }

    private static Properties makePotionProperties(String name, String type, int itemId, String path) {
        if (StrUtils.endsWith((String)name, (String[])new String[]{"_n", "_s"})) {
            return null;
        }
        if (name.equals((Object)"empty") && type.equals((Object)TYPE_POTION_NORMAL)) {
            itemId = Item.getIdFromItem((Item)Items.glass_bottle);
            PropertiesOrdered properties = new PropertiesOrdered();
            properties.put((Object)"type", (Object)"item");
            properties.put((Object)"items", (Object)("" + itemId));
            return properties;
        }
        int[] aint = (int[])CustomItems.getMapPotionIds().get((Object)name);
        if (aint == null) {
            Config.warn((String)("Potion not found for image: " + path));
            return null;
        }
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < aint.length; ++i) {
            int j = aint[i];
            if (type.equals((Object)TYPE_POTION_SPLASH)) {
                j |= 0x4000;
            }
            if (i > 0) {
                stringbuffer.append(" ");
            }
            stringbuffer.append(j);
        }
        int k = 16447;
        if (name.equals((Object)"water") || name.equals((Object)"mundane")) {
            k |= 0x40;
        }
        PropertiesOrdered properties1 = new PropertiesOrdered();
        properties1.put((Object)"type", (Object)"item");
        properties1.put((Object)"items", (Object)("" + itemId));
        properties1.put((Object)"damage", (Object)("" + stringbuffer.toString()));
        properties1.put((Object)"damageMask", (Object)("" + k));
        if (type.equals((Object)TYPE_POTION_SPLASH)) {
            properties1.put((Object)KEY_TEXTURE_SPLASH, (Object)name);
        } else {
            properties1.put((Object)KEY_TEXTURE_DRINKABLE, (Object)name);
        }
        return properties1;
    }

    private static Map getMapPotionIds() {
        if (mapPotionIds == null) {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put((Object)"water", (Object)CustomItems.getPotionId(0, 0));
            mapPotionIds.put((Object)"awkward", (Object)CustomItems.getPotionId(0, 1));
            mapPotionIds.put((Object)"thick", (Object)CustomItems.getPotionId(0, 2));
            mapPotionIds.put((Object)"potent", (Object)CustomItems.getPotionId(0, 3));
            mapPotionIds.put((Object)"regeneration", (Object)CustomItems.getPotionIds(1));
            mapPotionIds.put((Object)"movespeed", (Object)CustomItems.getPotionIds(2));
            mapPotionIds.put((Object)"fireresistance", (Object)CustomItems.getPotionIds(3));
            mapPotionIds.put((Object)"poison", (Object)CustomItems.getPotionIds(4));
            mapPotionIds.put((Object)"heal", (Object)CustomItems.getPotionIds(5));
            mapPotionIds.put((Object)"nightvision", (Object)CustomItems.getPotionIds(6));
            mapPotionIds.put((Object)"clear", (Object)CustomItems.getPotionId(7, 0));
            mapPotionIds.put((Object)"bungling", (Object)CustomItems.getPotionId(7, 1));
            mapPotionIds.put((Object)"charming", (Object)CustomItems.getPotionId(7, 2));
            mapPotionIds.put((Object)"rank", (Object)CustomItems.getPotionId(7, 3));
            mapPotionIds.put((Object)"weakness", (Object)CustomItems.getPotionIds(8));
            mapPotionIds.put((Object)"damageboost", (Object)CustomItems.getPotionIds(9));
            mapPotionIds.put((Object)"moveslowdown", (Object)CustomItems.getPotionIds(10));
            mapPotionIds.put((Object)"leaping", (Object)CustomItems.getPotionIds(11));
            mapPotionIds.put((Object)"harm", (Object)CustomItems.getPotionIds(12));
            mapPotionIds.put((Object)"waterbreathing", (Object)CustomItems.getPotionIds(13));
            mapPotionIds.put((Object)"invisibility", (Object)CustomItems.getPotionIds(14));
            mapPotionIds.put((Object)"thin", (Object)CustomItems.getPotionId(15, 0));
            mapPotionIds.put((Object)"debonair", (Object)CustomItems.getPotionId(15, 1));
            mapPotionIds.put((Object)"sparkling", (Object)CustomItems.getPotionId(15, 2));
            mapPotionIds.put((Object)"stinky", (Object)CustomItems.getPotionId(15, 3));
            mapPotionIds.put((Object)"mundane", (Object)CustomItems.getPotionId(0, 4));
            mapPotionIds.put((Object)"speed", mapPotionIds.get((Object)"movespeed"));
            mapPotionIds.put((Object)"fire_resistance", mapPotionIds.get((Object)"fireresistance"));
            mapPotionIds.put((Object)"instant_health", mapPotionIds.get((Object)"heal"));
            mapPotionIds.put((Object)"night_vision", mapPotionIds.get((Object)"nightvision"));
            mapPotionIds.put((Object)"strength", mapPotionIds.get((Object)"damageboost"));
            mapPotionIds.put((Object)"slowness", mapPotionIds.get((Object)"moveslowdown"));
            mapPotionIds.put((Object)"instant_damage", mapPotionIds.get((Object)"harm"));
            mapPotionIds.put((Object)"water_breathing", mapPotionIds.get((Object)"waterbreathing"));
        }
        return mapPotionIds;
    }

    private static int[] getPotionIds(int baseId) {
        return new int[]{baseId, baseId + 16, baseId + 32, baseId + 48};
    }

    private static int[] getPotionId(int baseId, int subId) {
        return new int[]{baseId + subId * 16};
    }

    private static int getPotionNameDamage(String name) {
        String s = "potion." + name;
        Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            String s1;
            Potion potion = apotion[i];
            if (potion == null || !s.equals((Object)(s1 = potion.getName()))) continue;
            return potion.getId();
        }
        return -1;
    }

    private static List makePropertyList(CustomItemProperties[][] propsArr) {
        ArrayList list = new ArrayList();
        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                Object[] acustomitemproperties = propsArr[i];
                ArrayList list1 = null;
                if (acustomitemproperties != null) {
                    list1 = new ArrayList((Collection)Arrays.asList((Object[])acustomitemproperties));
                }
                list.add(list1);
            }
        }
        return list;
    }

    private static CustomItemProperties[][] propertyListToArray(List list) {
        CustomItemProperties[][] acustomitemproperties = new CustomItemProperties[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            List subList = (List)list.get(i);
            if (subList == null) continue;
            Object[] acustomitemproperties1 = (CustomItemProperties[])subList.toArray((Object[])new CustomItemProperties[subList.size()]);
            Arrays.sort((Object[])acustomitemproperties1, (Comparator)new CustomItemsComparator());
            acustomitemproperties[i] = acustomitemproperties1;
        }
        return acustomitemproperties;
    }

    private static void addToItemList(CustomItemProperties cp, List itemList) {
        if (cp.items != null) {
            for (int i = 0; i < cp.items.length; ++i) {
                int j = cp.items[i];
                if (j <= 0) {
                    Config.warn((String)("Invalid item ID: " + j));
                    continue;
                }
                CustomItems.addToList(cp, itemList, j);
            }
        }
    }

    private static void addToEnchantmentList(CustomItemProperties cp, List enchantmentList) {
        if (cp.type == 2 && cp.enchantmentIds != null) {
            for (int i = 0; i < 256; ++i) {
                if (!cp.enchantmentIds.isInRange(i)) continue;
                CustomItems.addToList(cp, enchantmentList, i);
            }
        }
    }

    private static void addToList(CustomItemProperties cp, List list, int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        List subList = (List)list.get(id);
        if (subList == null) {
            subList = new ArrayList();
            list.set(id, (Object)subList);
        }
        subList.add((Object)cp);
    }

    public static IBakedModel getCustomItemModel(ItemStack itemStack, IBakedModel model, ResourceLocation modelLocation, boolean fullModel) {
        if (!fullModel && model.isGui3d()) {
            return model;
        }
        if (itemProperties == null) {
            return model;
        }
        CustomItemProperties customitemproperties = CustomItems.getCustomItemProperties(itemStack, 1);
        if (customitemproperties == null) {
            return model;
        }
        IBakedModel ibakedmodel = customitemproperties.getBakedModel(modelLocation, fullModel);
        return ibakedmodel != null ? ibakedmodel : model;
    }

    public static boolean bindCustomArmorTexture(ItemStack itemStack, int layer, String overlay) {
        if (itemProperties == null) {
            return false;
        }
        ResourceLocation resourcelocation = CustomItems.getCustomArmorLocation(itemStack, layer, overlay);
        if (resourcelocation == null) {
            return false;
        }
        Config.getTextureManager().bindTexture(resourcelocation);
        return true;
    }

    private static ResourceLocation getCustomArmorLocation(ItemStack itemStack, int layer, String overlay) {
        String s1;
        ResourceLocation resourcelocation;
        CustomItemProperties customitemproperties = CustomItems.getCustomItemProperties(itemStack, 3);
        if (customitemproperties == null) {
            return null;
        }
        if (customitemproperties.mapTextureLocations == null) {
            return customitemproperties.textureLocation;
        }
        Item item = itemStack.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        ItemArmor itemarmor = (ItemArmor)item;
        String s = itemarmor.getArmorMaterial().getName();
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("texture.");
        stringbuffer.append(s);
        stringbuffer.append("_layer_");
        stringbuffer.append(layer);
        if (overlay != null) {
            stringbuffer.append("_");
            stringbuffer.append(overlay);
        }
        return (resourcelocation = (ResourceLocation)customitemproperties.mapTextureLocations.get((Object)(s1 = stringbuffer.toString()))) == null ? customitemproperties.textureLocation : resourcelocation;
    }

    private static CustomItemProperties getCustomItemProperties(ItemStack itemStack, int type) {
        CustomItemProperties[] acustomitemproperties;
        if (itemProperties == null) {
            return null;
        }
        if (itemStack == null) {
            return null;
        }
        Item item = itemStack.getItem();
        int i = Item.getIdFromItem((Item)item);
        if (i >= 0 && i < itemProperties.length && (acustomitemproperties = itemProperties[i]) != null) {
            for (int j = 0; j < acustomitemproperties.length; ++j) {
                CustomItemProperties customitemproperties = acustomitemproperties[j];
                if (customitemproperties.type != type || !CustomItems.matchesProperties(customitemproperties, itemStack, null)) continue;
                return customitemproperties;
            }
        }
        return null;
    }

    private static boolean matchesProperties(CustomItemProperties cip, ItemStack itemStack, int[][] enchantmentIdLevels) {
        Item item = itemStack.getItem();
        if (cip.damage != null) {
            int i = itemStack.getItemDamage();
            if (cip.damageMask != 0) {
                i &= cip.damageMask;
            }
            if (cip.damagePercent) {
                int j = item.getMaxDamage();
                i = (int)((double)(i * 100) / (double)j);
            }
            if (!cip.damage.isInRange(i)) {
                return false;
            }
        }
        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.stackSize)) {
            return false;
        }
        int[][] aint = enchantmentIdLevels;
        if (cip.enchantmentIds != null) {
            if (enchantmentIdLevels == null) {
                aint = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            boolean flag = false;
            for (int k = 0; k < aint.length; ++k) {
                int l = aint[k][0];
                if (!cip.enchantmentIds.isInRange(l)) continue;
                flag = true;
                break;
            }
            if (!flag) {
                return false;
            }
        }
        if (cip.enchantmentLevels != null) {
            if (aint == null) {
                aint = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            boolean flag1 = false;
            for (int i1 = 0; i1 < aint.length; ++i1) {
                int k1 = aint[i1][1];
                if (!cip.enchantmentLevels.isInRange(k1)) continue;
                flag1 = true;
                break;
            }
            if (!flag1) {
                return false;
            }
        }
        if (cip.nbtTagValues != null) {
            NBTTagCompound nbttagcompound = itemStack.getTagCompound();
            for (int j1 = 0; j1 < cip.nbtTagValues.length; ++j1) {
                NbtTagValue nbttagvalue = cip.nbtTagValues[j1];
                if (nbttagvalue.matches(nbttagcompound)) continue;
                return false;
            }
        }
        if (cip.hand != 0) {
            if (cip.hand == 1 && renderOffHand) {
                return false;
            }
            if (cip.hand == 2 && !renderOffHand) {
                return false;
            }
        }
        return true;
    }

    private static int[][] getEnchantmentIdLevels(ItemStack itemStack) {
        NBTTagList nbttaglist;
        Item item = itemStack.getItem();
        NBTTagList nBTTagList = nbttaglist = item == Items.enchanted_book ? Items.enchanted_book.getEnchantments(itemStack) : itemStack.getEnchantmentTagList();
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            int[][] aint = new int[nbttaglist.tagCount()][2];
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                short j = nbttagcompound.getShort("id");
                short k = nbttagcompound.getShort("lvl");
                aint[i][0] = j;
                aint[i][1] = k;
            }
            return aint;
        }
        return EMPTY_INT2_ARRAY;
    }

    public static boolean renderCustomEffect(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
        if (enchantmentProperties == null) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        int[][] aint = CustomItems.getEnchantmentIdLevels(itemStack);
        if (aint.length <= 0) {
            return false;
        }
        HashSet set = null;
        boolean flag = false;
        TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            CustomItemProperties[] acustomitemproperties;
            int j = aint[i][0];
            if (j < 0 || j >= enchantmentProperties.length || (acustomitemproperties = enchantmentProperties[j]) == null) continue;
            for (int k = 0; k < acustomitemproperties.length; ++k) {
                CustomItemProperties customitemproperties = acustomitemproperties[k];
                if (set == null) {
                    set = new HashSet();
                }
                if (!set.add((Object)j) || !CustomItems.matchesProperties(customitemproperties, itemStack, aint) || customitemproperties.textureLocation == null) continue;
                texturemanager.bindTexture(customitemproperties.textureLocation);
                float f = customitemproperties.getTextureWidth(texturemanager);
                if (!flag) {
                    flag = true;
                    GlStateManager.depthMask((boolean)false);
                    GlStateManager.depthFunc((int)514);
                    GlStateManager.disableLighting();
                    GlStateManager.matrixMode((int)5890);
                }
                Blender.setupBlend((int)customitemproperties.blend, (float)1.0f);
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)(f / 2.0f), (float)(f / 2.0f), (float)(f / 2.0f));
                float f1 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate((float)f1, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)customitemproperties.rotation, (float)0.0f, (float)0.0f, (float)1.0f);
                renderItem.renderModel(model, -1);
                GlStateManager.popMatrix();
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc((int)515);
            GlStateManager.depthMask((boolean)true);
            texturemanager.bindTexture(TextureMap.locationBlocksTexture);
        }
        return flag;
    }

    public static boolean renderCustomArmorEffect(EntityLivingBase entity, ItemStack itemStack, ModelBase model, float limbSwing, float prevLimbSwing, float partialTicks, float timeLimbSwing, float yaw, float pitch, float scale) {
        if (enchantmentProperties == null) {
            return false;
        }
        if (Config.isShaders() && Shaders.isShadowPass) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        int[][] aint = CustomItems.getEnchantmentIdLevels(itemStack);
        if (aint.length <= 0) {
            return false;
        }
        HashSet set = null;
        boolean flag = false;
        TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            CustomItemProperties[] acustomitemproperties;
            int j = aint[i][0];
            if (j < 0 || j >= enchantmentProperties.length || (acustomitemproperties = enchantmentProperties[j]) == null) continue;
            for (int k = 0; k < acustomitemproperties.length; ++k) {
                CustomItemProperties customitemproperties = acustomitemproperties[k];
                if (set == null) {
                    set = new HashSet();
                }
                if (!set.add((Object)j) || !CustomItems.matchesProperties(customitemproperties, itemStack, aint) || customitemproperties.textureLocation == null) continue;
                texturemanager.bindTexture(customitemproperties.textureLocation);
                float f = customitemproperties.getTextureWidth(texturemanager);
                if (!flag) {
                    flag = true;
                    if (Config.isShaders()) {
                        ShadersRender.renderEnchantedGlintBegin();
                    }
                    GlStateManager.enableBlend();
                    GlStateManager.depthFunc((int)514);
                    GlStateManager.depthMask((boolean)false);
                }
                Blender.setupBlend((int)customitemproperties.blend, (float)1.0f);
                GlStateManager.disableLighting();
                GlStateManager.matrixMode((int)5890);
                GlStateManager.loadIdentity();
                GlStateManager.rotate((float)customitemproperties.rotation, (float)0.0f, (float)0.0f, (float)1.0f);
                float f1 = f / 8.0f;
                GlStateManager.scale((float)f1, (float)(f1 / 2.0f), (float)f1);
                float f2 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate((float)0.0f, (float)f2, (float)0.0f);
                GlStateManager.matrixMode((int)5888);
                model.render((Entity)entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.depthFunc((int)515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
        return flag;
    }

    public static boolean isUseGlint() {
        return useGlint;
    }
}
