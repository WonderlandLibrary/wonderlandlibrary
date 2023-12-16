// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Hashtable;
import net.minecraft.entity.Entity;
import net.optifine.shaders.ShadersRender;
import net.optifine.shaders.Shaders;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.texture.TextureManager;
import net.optifine.render.Blender;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashSet;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.nbt.NBTTagList;
import net.optifine.config.NbtTagValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.potion.Potion;
import java.util.LinkedHashMap;
import net.optifine.util.StrUtils;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import java.util.HashMap;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.Arrays;
import net.optifine.util.ResUtils;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.Map;

public class CustomItems
{
    private static CustomItemProperties[][] itemProperties;
    private static CustomItemProperties[][] enchantmentProperties;
    private static Map mapPotionIds;
    private static ItemModelGenerator itemModelGenerator;
    private static boolean useGlint;
    private static boolean renderOffHand;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final int MASK_POTION_EXTENDED = 64;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[][] EMPTY_INT2_ARRAY;
    private static final String TYPE_POTION_NORMAL = "normal";
    private static final String TYPE_POTION_SPLASH = "splash";
    private static final String TYPE_POTION_LINGER = "linger";
    
    static {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.mapPotionIds = null;
        CustomItems.itemModelGenerator = new ItemModelGenerator();
        CustomItems.useGlint = true;
        CustomItems.renderOffHand = false;
        EMPTY_INT2_ARRAY = new int[0][];
    }
    
    public static void update() {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.useGlint = true;
        if (Config.isCustomItems()) {
            readCitProperties("mcpatcher/cit.properties");
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                update(iresourcepack);
            }
            update(Config.getDefaultResourcePack());
            if (CustomItems.itemProperties.length <= 0) {
                CustomItems.itemProperties = null;
            }
            if (CustomItems.enchantmentProperties.length <= 0) {
                CustomItems.enchantmentProperties = null;
            }
        }
    }
    
    private static void readCitProperties(final String fileName) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(fileName);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            Config.dbg("CustomItems: Loading " + fileName);
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            CustomItems.useGlint = Config.parseBoolean(properties.getProperty("useGlint"), true);
        }
        catch (final FileNotFoundException var4) {}
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static void update(final IResourcePack rp) {
        String[] astring = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", null);
        final Map map = makeAutoImageProperties(rp);
        if (map.size() > 0) {
            final Set set = map.keySet();
            final String[] astring2 = set.toArray(new String[set.size()]);
            astring = (String[])Config.addObjectsToArray(astring, astring2);
        }
        Arrays.sort(astring);
        final List list = makePropertyList(CustomItems.itemProperties);
        final List list2 = makePropertyList(CustomItems.enchantmentProperties);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("CustomItems: " + s);
            try {
                CustomItemProperties customitemproperties = null;
                if (map.containsKey(s)) {
                    customitemproperties = map.get(s);
                }
                if (customitemproperties == null) {
                    final ResourceLocation resourcelocation = new ResourceLocation(s);
                    final InputStream inputstream = rp.getInputStream(resourcelocation);
                    if (inputstream == null) {
                        Config.warn("CustomItems file not found: " + s);
                        continue;
                    }
                    final Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    customitemproperties = new CustomItemProperties(properties, s);
                }
                if (customitemproperties.isValid(s)) {
                    addToItemList(customitemproperties, list);
                    addToEnchantmentList(customitemproperties, list2);
                }
            }
            catch (final FileNotFoundException var11) {
                Config.warn("CustomItems file not found: " + s);
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
        CustomItems.itemProperties = propertyListToArray(list);
        CustomItems.enchantmentProperties = propertyListToArray(list2);
        final Comparator comparator = getPropertiesComparator();
        for (int j = 0; j < CustomItems.itemProperties.length; ++j) {
            final CustomItemProperties[] acustomitemproperties = CustomItems.itemProperties[j];
            if (acustomitemproperties != null) {
                Arrays.sort(acustomitemproperties, comparator);
            }
        }
        for (int k = 0; k < CustomItems.enchantmentProperties.length; ++k) {
            final CustomItemProperties[] acustomitemproperties2 = CustomItems.enchantmentProperties[k];
            if (acustomitemproperties2 != null) {
                Arrays.sort(acustomitemproperties2, comparator);
            }
        }
    }
    
    private static Comparator getPropertiesComparator() {
        final Comparator comparator = new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                final CustomItemProperties customitemproperties = (CustomItemProperties)o1;
                final CustomItemProperties customitemproperties2 = (CustomItemProperties)o2;
                return (customitemproperties.layer != customitemproperties2.layer) ? (customitemproperties.layer - customitemproperties2.layer) : ((customitemproperties.weight != customitemproperties2.weight) ? (customitemproperties2.weight - customitemproperties.weight) : (customitemproperties.basePath.equals(customitemproperties2.basePath) ? customitemproperties.name.compareTo(customitemproperties2.name) : customitemproperties.basePath.compareTo(customitemproperties2.basePath)));
            }
        };
        return comparator;
    }
    
    public static void updateIcons(final TextureMap textureMap) {
        for (final CustomItemProperties customitemproperties : getAllProperties()) {
            customitemproperties.updateIcons(textureMap);
        }
    }
    
    public static void loadModels(final ModelBakery modelBakery) {
        for (final CustomItemProperties customitemproperties : getAllProperties()) {
            customitemproperties.loadModels(modelBakery);
        }
    }
    
    public static void updateModels() {
        for (final CustomItemProperties customitemproperties : getAllProperties()) {
            if (customitemproperties.type == 1) {
                final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
                customitemproperties.updateModelTexture(texturemap, CustomItems.itemModelGenerator);
                customitemproperties.updateModelsFull();
            }
        }
    }
    
    private static List<CustomItemProperties> getAllProperties() {
        final List<CustomItemProperties> list = new ArrayList<CustomItemProperties>();
        addAll(CustomItems.itemProperties, list);
        addAll(CustomItems.enchantmentProperties, list);
        return list;
    }
    
    private static void addAll(final CustomItemProperties[][] cipsArr, final List<CustomItemProperties> list) {
        if (cipsArr != null) {
            for (int i = 0; i < cipsArr.length; ++i) {
                final CustomItemProperties[] acustomitemproperties = cipsArr[i];
                if (acustomitemproperties != null) {
                    for (int j = 0; j < acustomitemproperties.length; ++j) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[j];
                        if (customitemproperties != null) {
                            list.add(customitemproperties);
                        }
                    }
                }
            }
        }
    }
    
    private static Map makeAutoImageProperties(final IResourcePack rp) {
        final Map map = new HashMap();
        map.putAll(makePotionImageProperties(rp, "normal", Item.getIdFromItem(Items.potionitem)));
        map.putAll(makePotionImageProperties(rp, "splash", Item.getIdFromItem(Items.potionitem)));
        map.putAll(makePotionImageProperties(rp, "linger", Item.getIdFromItem(Items.potionitem)));
        return map;
    }
    
    private static Map makePotionImageProperties(final IResourcePack rp, final String type, final int itemId) {
        final Map map = new HashMap();
        final String s = String.valueOf(type) + "/";
        final String[] astring = { "mcpatcher/cit/potion/" + s, "mcpatcher/cit/Potion/" + s };
        final String[] astring2 = { ".png" };
        final String[] astring3 = ResUtils.collectFiles(rp, astring, astring2);
        for (int i = 0; i < astring3.length; ++i) {
            final String s2 = astring3[i];
            final String name = StrUtils.removePrefixSuffix(s2, astring, astring2);
            final Properties properties = makePotionProperties(name, type, itemId, s2);
            if (properties != null) {
                final String s3 = String.valueOf(StrUtils.removeSuffix(s2, astring2)) + ".properties";
                final CustomItemProperties customitemproperties = new CustomItemProperties(properties, s3);
                map.put(s3, customitemproperties);
            }
        }
        return map;
    }
    
    private static Properties makePotionProperties(final String name, final String type, int itemId, final String path) {
        if (StrUtils.endsWith(name, new String[] { "_n", "_s" })) {
            return null;
        }
        if (name.equals("empty") && type.equals("normal")) {
            itemId = Item.getIdFromItem(Items.glass_bottle);
            final Properties properties = new PropertiesOrdered();
            ((Hashtable<String, String>)properties).put("type", "item");
            ((Hashtable<String, String>)properties).put("items", new StringBuilder().append(itemId).toString());
            return properties;
        }
        final int[] aint = getMapPotionIds().get(name);
        if (aint == null) {
            Config.warn("Potion not found for image: " + path);
            return null;
        }
        final StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < aint.length; ++i) {
            int j = aint[i];
            if (type.equals("splash")) {
                j |= 0x4000;
            }
            if (i > 0) {
                stringbuffer.append(" ");
            }
            stringbuffer.append(j);
        }
        int k = 16447;
        if (name.equals("water") || name.equals("mundane")) {
            k |= 0x40;
        }
        final Properties properties2 = new PropertiesOrdered();
        ((Hashtable<String, String>)properties2).put("type", "item");
        ((Hashtable<String, String>)properties2).put("items", new StringBuilder().append(itemId).toString());
        ((Hashtable<String, String>)properties2).put("damage", new StringBuilder().append(stringbuffer.toString()).toString());
        ((Hashtable<String, String>)properties2).put("damageMask", new StringBuilder().append(k).toString());
        if (type.equals("splash")) {
            ((Hashtable<String, String>)properties2).put("texture.potion_bottle_splash", name);
        }
        else {
            ((Hashtable<String, String>)properties2).put("texture.potion_bottle_drinkable", name);
        }
        return properties2;
    }
    
    private static Map getMapPotionIds() {
        if (CustomItems.mapPotionIds == null) {
            (CustomItems.mapPotionIds = new LinkedHashMap()).put("water", getPotionId(0, 0));
            CustomItems.mapPotionIds.put("awkward", getPotionId(0, 1));
            CustomItems.mapPotionIds.put("thick", getPotionId(0, 2));
            CustomItems.mapPotionIds.put("potent", getPotionId(0, 3));
            CustomItems.mapPotionIds.put("regeneration", getPotionIds(1));
            CustomItems.mapPotionIds.put("movespeed", getPotionIds(2));
            CustomItems.mapPotionIds.put("fireresistance", getPotionIds(3));
            CustomItems.mapPotionIds.put("poison", getPotionIds(4));
            CustomItems.mapPotionIds.put("heal", getPotionIds(5));
            CustomItems.mapPotionIds.put("nightvision", getPotionIds(6));
            CustomItems.mapPotionIds.put("clear", getPotionId(7, 0));
            CustomItems.mapPotionIds.put("bungling", getPotionId(7, 1));
            CustomItems.mapPotionIds.put("charming", getPotionId(7, 2));
            CustomItems.mapPotionIds.put("rank", getPotionId(7, 3));
            CustomItems.mapPotionIds.put("weakness", getPotionIds(8));
            CustomItems.mapPotionIds.put("damageboost", getPotionIds(9));
            CustomItems.mapPotionIds.put("moveslowdown", getPotionIds(10));
            CustomItems.mapPotionIds.put("leaping", getPotionIds(11));
            CustomItems.mapPotionIds.put("harm", getPotionIds(12));
            CustomItems.mapPotionIds.put("waterbreathing", getPotionIds(13));
            CustomItems.mapPotionIds.put("invisibility", getPotionIds(14));
            CustomItems.mapPotionIds.put("thin", getPotionId(15, 0));
            CustomItems.mapPotionIds.put("debonair", getPotionId(15, 1));
            CustomItems.mapPotionIds.put("sparkling", getPotionId(15, 2));
            CustomItems.mapPotionIds.put("stinky", getPotionId(15, 3));
            CustomItems.mapPotionIds.put("mundane", getPotionId(0, 4));
            CustomItems.mapPotionIds.put("speed", CustomItems.mapPotionIds.get("movespeed"));
            CustomItems.mapPotionIds.put("fire_resistance", CustomItems.mapPotionIds.get("fireresistance"));
            CustomItems.mapPotionIds.put("instant_health", CustomItems.mapPotionIds.get("heal"));
            CustomItems.mapPotionIds.put("night_vision", CustomItems.mapPotionIds.get("nightvision"));
            CustomItems.mapPotionIds.put("strength", CustomItems.mapPotionIds.get("damageboost"));
            CustomItems.mapPotionIds.put("slowness", CustomItems.mapPotionIds.get("moveslowdown"));
            CustomItems.mapPotionIds.put("instant_damage", CustomItems.mapPotionIds.get("harm"));
            CustomItems.mapPotionIds.put("water_breathing", CustomItems.mapPotionIds.get("waterbreathing"));
        }
        return CustomItems.mapPotionIds;
    }
    
    private static int[] getPotionIds(final int baseId) {
        return new int[] { baseId, baseId + 16, baseId + 32, baseId + 48 };
    }
    
    private static int[] getPotionId(final int baseId, final int subId) {
        return new int[] { baseId + subId * 16 };
    }
    
    private static int getPotionNameDamage(final String name) {
        final String s = "potion." + name;
        final Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            final Potion potion = apotion[i];
            if (potion != null) {
                final String s2 = potion.getName();
                if (s.equals(s2)) {
                    return potion.getId();
                }
            }
        }
        return -1;
    }
    
    private static List makePropertyList(final CustomItemProperties[][] propsArr) {
        final List list = new ArrayList();
        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                final CustomItemProperties[] acustomitemproperties = propsArr[i];
                List list2 = null;
                if (acustomitemproperties != null) {
                    list2 = new ArrayList(Arrays.asList(acustomitemproperties));
                }
                list.add(list2);
            }
        }
        return list;
    }
    
    private static CustomItemProperties[][] propertyListToArray(final List lists) {
        final CustomItemProperties[][] acustomitemproperties = new CustomItemProperties[lists.size()][];
        for (int i = 0; i < lists.size(); ++i) {
            final List list = lists.get(i);
            if (list != null) {
                final CustomItemProperties[] acustomitemproperties2 = list.toArray(new CustomItemProperties[list.size()]);
                Arrays.sort(acustomitemproperties2, new CustomItemsComparator());
                acustomitemproperties[i] = acustomitemproperties2;
            }
        }
        return acustomitemproperties;
    }
    
    private static void addToItemList(final CustomItemProperties cp, final List itemList) {
        if (cp.items != null) {
            for (int i = 0; i < cp.items.length; ++i) {
                final int j = cp.items[i];
                if (j <= 0) {
                    Config.warn("Invalid item ID: " + j);
                }
                else {
                    addToList(cp, itemList, j);
                }
            }
        }
    }
    
    private static void addToEnchantmentList(final CustomItemProperties cp, final List enchantmentList) {
        if (cp.type == 2 && cp.enchantmentIds != null) {
            for (int i = 0; i < 256; ++i) {
                if (cp.enchantmentIds.isInRange(i)) {
                    addToList(cp, enchantmentList, i);
                }
            }
        }
    }
    
    private static void addToList(final CustomItemProperties cp, final List lists, final int id) {
        while (id >= lists.size()) {
            lists.add(null);
        }
        List list = lists.get(id);
        if (list == null) {
            list = new ArrayList();
            list.set(id, list);
        }
        list.add(cp);
    }
    
    public static IBakedModel getCustomItemModel(final ItemStack itemStack, final IBakedModel model, final ResourceLocation modelLocation, final boolean fullModel) {
        if (!fullModel && model.isGui3d()) {
            return model;
        }
        if (CustomItems.itemProperties == null) {
            return model;
        }
        final CustomItemProperties customitemproperties = getCustomItemProperties(itemStack, 1);
        if (customitemproperties == null) {
            return model;
        }
        final IBakedModel ibakedmodel = customitemproperties.getBakedModel(modelLocation, fullModel);
        return (ibakedmodel != null) ? ibakedmodel : model;
    }
    
    public static boolean bindCustomArmorTexture(final ItemStack itemStack, final int layer, final String overlay) {
        if (CustomItems.itemProperties == null) {
            return false;
        }
        final ResourceLocation resourcelocation = getCustomArmorLocation(itemStack, layer, overlay);
        if (resourcelocation == null) {
            return false;
        }
        Config.getTextureManager().bindTexture(resourcelocation);
        return true;
    }
    
    private static ResourceLocation getCustomArmorLocation(final ItemStack itemStack, final int layer, final String overlay) {
        final CustomItemProperties customitemproperties = getCustomItemProperties(itemStack, 3);
        if (customitemproperties == null) {
            return null;
        }
        if (customitemproperties.mapTextureLocations == null) {
            return customitemproperties.textureLocation;
        }
        final Item item = itemStack.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        final ItemArmor itemarmor = (ItemArmor)item;
        final String s = itemarmor.getArmorMaterial().getName();
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("texture.");
        stringbuffer.append(s);
        stringbuffer.append("_layer_");
        stringbuffer.append(layer);
        if (overlay != null) {
            stringbuffer.append("_");
            stringbuffer.append(overlay);
        }
        final String s2 = stringbuffer.toString();
        final ResourceLocation resourcelocation = customitemproperties.mapTextureLocations.get(s2);
        return (resourcelocation == null) ? customitemproperties.textureLocation : resourcelocation;
    }
    
    private static CustomItemProperties getCustomItemProperties(final ItemStack itemStack, final int type) {
        if (CustomItems.itemProperties == null) {
            return null;
        }
        if (itemStack == null) {
            return null;
        }
        final Item item = itemStack.getItem();
        final int i = Item.getIdFromItem(item);
        if (i >= 0 && i < CustomItems.itemProperties.length) {
            final CustomItemProperties[] acustomitemproperties = CustomItems.itemProperties[i];
            if (acustomitemproperties != null) {
                for (int j = 0; j < acustomitemproperties.length; ++j) {
                    final CustomItemProperties customitemproperties = acustomitemproperties[j];
                    if (customitemproperties.type == type && matchesProperties(customitemproperties, itemStack, null)) {
                        return customitemproperties;
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean matchesProperties(final CustomItemProperties cip, final ItemStack itemStack, final int[][] enchantmentIdLevels) {
        final Item item = itemStack.getItem();
        if (cip.damage != null) {
            int i = itemStack.getItemDamage();
            if (cip.damageMask != 0) {
                i &= cip.damageMask;
            }
            if (cip.damagePercent) {
                final int j = item.getMaxDamage();
                i = (int)(i * 100 / (double)j);
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
                aint = getEnchantmentIdLevels(itemStack);
            }
            boolean flag = false;
            for (int k = 0; k < aint.length; ++k) {
                final int l = aint[k][0];
                if (cip.enchantmentIds.isInRange(l)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        if (cip.enchantmentLevels != null) {
            if (aint == null) {
                aint = getEnchantmentIdLevels(itemStack);
            }
            boolean flag2 = false;
            for (int i2 = 0; i2 < aint.length; ++i2) {
                final int k2 = aint[i2][1];
                if (cip.enchantmentLevels.isInRange(k2)) {
                    flag2 = true;
                    break;
                }
            }
            if (!flag2) {
                return false;
            }
        }
        if (cip.nbtTagValues != null) {
            final NBTTagCompound nbttagcompound = itemStack.getTagCompound();
            for (int j2 = 0; j2 < cip.nbtTagValues.length; ++j2) {
                final NbtTagValue nbttagvalue = cip.nbtTagValues[j2];
                if (!nbttagvalue.matches(nbttagcompound)) {
                    return false;
                }
            }
        }
        if (cip.hand != 0) {
            if (cip.hand == 1 && CustomItems.renderOffHand) {
                return false;
            }
            if (cip.hand == 2 && !CustomItems.renderOffHand) {
                return false;
            }
        }
        return true;
    }
    
    private static int[][] getEnchantmentIdLevels(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        final NBTTagList nbttaglist = (item == Items.enchanted_book) ? Items.enchanted_book.getEnchantments(itemStack) : itemStack.getEnchantmentTagList();
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int[][] aint = new int[nbttaglist.tagCount()][2];
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final int j = nbttagcompound.getShort("id");
                final int k = nbttagcompound.getShort("lvl");
                aint[i][0] = j;
                aint[i][1] = k;
            }
            return aint;
        }
        return CustomItems.EMPTY_INT2_ARRAY;
    }
    
    public static boolean renderCustomEffect(final RenderItem renderItem, final ItemStack itemStack, final IBakedModel model) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        final int[][] aint = getEnchantmentIdLevels(itemStack);
        if (aint.length <= 0) {
            return false;
        }
        Set set = null;
        boolean flag = false;
        final TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i][0];
            if (j >= 0 && j < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] acustomitemproperties = CustomItems.enchantmentProperties[j];
                if (acustomitemproperties != null) {
                    for (int k = 0; k < acustomitemproperties.length; ++k) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[k];
                        if (set == null) {
                            set = new HashSet();
                        }
                        if (set.add(j) && matchesProperties(customitemproperties, itemStack, aint) && customitemproperties.textureLocation != null) {
                            texturemanager.bindTexture(customitemproperties.textureLocation);
                            final float f = customitemproperties.getTextureWidth(texturemanager);
                            if (!flag) {
                                flag = true;
                                GlStateManager.depthMask(false);
                                GlStateManager.depthFunc(514);
                                GlStateManager.disableLighting();
                                GlStateManager.matrixMode(5890);
                            }
                            Blender.setupBlend(customitemproperties.blend, 1.0f);
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(f / 2.0f, f / 2.0f, f / 2.0f);
                            final float f2 = customitemproperties.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(f2, 0.0f, 0.0f);
                            GlStateManager.rotate(customitemproperties.rotation, 0.0f, 0.0f, 1.0f);
                            renderItem.renderModel(model, -1);
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            texturemanager.bindTexture(TextureMap.locationBlocksTexture);
        }
        return flag;
    }
    
    public static boolean renderCustomArmorEffect(final EntityLivingBase entity, final ItemStack itemStack, final ModelBase model, final float limbSwing, final float prevLimbSwing, final float partialTicks, final float timeLimbSwing, final float yaw, final float pitch, final float scale) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (Config.isShaders() && Shaders.isShadowPass) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        final int[][] aint = getEnchantmentIdLevels(itemStack);
        if (aint.length <= 0) {
            return false;
        }
        Set set = null;
        boolean flag = false;
        final TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i][0];
            if (j >= 0 && j < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] acustomitemproperties = CustomItems.enchantmentProperties[j];
                if (acustomitemproperties != null) {
                    for (int k = 0; k < acustomitemproperties.length; ++k) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[k];
                        if (set == null) {
                            set = new HashSet();
                        }
                        if (set.add(j) && matchesProperties(customitemproperties, itemStack, aint) && customitemproperties.textureLocation != null) {
                            texturemanager.bindTexture(customitemproperties.textureLocation);
                            final float f = customitemproperties.getTextureWidth(texturemanager);
                            if (!flag) {
                                flag = true;
                                if (Config.isShaders()) {
                                    ShadersRender.renderEnchantedGlintBegin();
                                }
                                GlStateManager.enableBlend();
                                GlStateManager.depthFunc(514);
                                GlStateManager.depthMask(false);
                            }
                            Blender.setupBlend(customitemproperties.blend, 1.0f);
                            GlStateManager.disableLighting();
                            GlStateManager.matrixMode(5890);
                            GlStateManager.loadIdentity();
                            GlStateManager.rotate(customitemproperties.rotation, 0.0f, 0.0f, 1.0f);
                            final float f2 = f / 8.0f;
                            GlStateManager.scale(f2, f2 / 2.0f, f2);
                            final float f3 = customitemproperties.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(0.0f, f3, 0.0f);
                            GlStateManager.matrixMode(5888);
                            model.render(entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
                        }
                    }
                }
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
        return flag;
    }
    
    public static boolean isUseGlint() {
        return CustomItems.useGlint;
    }
}
