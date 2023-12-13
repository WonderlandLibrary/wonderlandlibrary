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
import java.util.Hashtable;
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
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItemProperties;
import net.optifine.CustomItemsComparator;
import net.optifine.config.NbtTagValue;
import net.optifine.config.RangeListInt;
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
        if (!Config.isCustomItems()) {
            return;
        }
        CustomItems.readCitProperties("mcpatcher/cit.properties");
        IResourcePack[] rps = Config.getResourcePacks();
        for (int i = rps.length - 1; i >= 0; --i) {
            IResourcePack rp = rps[i];
            CustomItems.update(rp);
        }
        CustomItems.update((IResourcePack)Config.getDefaultResourcePack());
        if (itemProperties.length <= 0) {
            itemProperties = null;
        }
        if (enchantmentProperties.length <= 0) {
            enchantmentProperties = null;
        }
    }

    private static void readCitProperties(String fileName) {
        try {
            ResourceLocation loc = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream((ResourceLocation)loc);
            if (in == null) {
                return;
            }
            Config.dbg((String)("CustomItems: Loading " + fileName));
            PropertiesOrdered props = new PropertiesOrdered();
            props.load(in);
            in.close();
            useGlint = Config.parseBoolean((String)props.getProperty("useGlint"), (boolean)true);
        }
        catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void update(IResourcePack rp) {
        CustomItemProperties[] cips;
        int i;
        Object[] names = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", null);
        Map mapAutoProperties = CustomItems.makeAutoImageProperties(rp);
        if (mapAutoProperties.size() > 0) {
            Set keySetAuto = mapAutoProperties.keySet();
            Object[] keysAuto = keySetAuto.toArray(new String[keySetAuto.size()]);
            names = (String[])Config.addObjectsToArray((Object[])names, (Object[])keysAuto);
        }
        Arrays.sort(names);
        List itemList = CustomItems.makePropertyList(itemProperties);
        List enchantmentList = CustomItems.makePropertyList(enchantmentProperties);
        for (int i2 = 0; i2 < names.length; ++i2) {
            Object name = names[i2];
            Config.dbg((String)("CustomItems: " + (String)name));
            try {
                CustomItemProperties cip = null;
                if (mapAutoProperties.containsKey(name)) {
                    cip = (CustomItemProperties)mapAutoProperties.get(name);
                }
                if (cip == null) {
                    ResourceLocation locFile = new ResourceLocation((String)name);
                    InputStream in = rp.getInputStream(locFile);
                    if (in == null) {
                        Config.warn((String)("CustomItems file not found: " + (String)name));
                        continue;
                    }
                    PropertiesOrdered props = new PropertiesOrdered();
                    props.load(in);
                    cip = new CustomItemProperties(props, (String)name);
                }
                if (!cip.isValid((String)name)) continue;
                CustomItems.addToItemList(cip, itemList);
                CustomItems.addToEnchantmentList(cip, enchantmentList);
                continue;
            }
            catch (FileNotFoundException e) {
                Config.warn((String)("CustomItems file not found: " + (String)name));
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        itemProperties = CustomItems.propertyListToArray(itemList);
        enchantmentProperties = CustomItems.propertyListToArray(enchantmentList);
        Comparator comp = CustomItems.getPropertiesComparator();
        for (i = 0; i < itemProperties.length; ++i) {
            cips = itemProperties[i];
            if (cips == null) continue;
            Arrays.sort(cips, comp);
        }
        for (i = 0; i < enchantmentProperties.length; ++i) {
            cips = enchantmentProperties[i];
            if (cips == null) continue;
            Arrays.sort(cips, comp);
        }
    }

    private static Comparator getPropertiesComparator() {
        Comparator comp = new Comparator(){

            public int compare(Object o1, Object o2) {
                CustomItemProperties cip1 = (CustomItemProperties)o1;
                CustomItemProperties cip2 = (CustomItemProperties)o2;
                if (cip1.layer != cip2.layer) {
                    return cip1.layer - cip2.layer;
                }
                if (cip1.weight != cip2.weight) {
                    return cip2.weight - cip1.weight;
                }
                if (!cip1.basePath.equals(cip2.basePath)) {
                    return cip1.basePath.compareTo(cip2.basePath);
                }
                return cip1.name.compareTo(cip2.name);
            }
        };
        return comp;
    }

    public static void updateIcons(TextureMap textureMap) {
        List<CustomItemProperties> cips = CustomItems.getAllProperties();
        for (CustomItemProperties cip : cips) {
            cip.updateIcons(textureMap);
        }
    }

    public static void loadModels(ModelBakery modelBakery) {
        List<CustomItemProperties> cips = CustomItems.getAllProperties();
        for (CustomItemProperties cip : cips) {
            cip.loadModels(modelBakery);
        }
    }

    public static void updateModels() {
        List<CustomItemProperties> cips = CustomItems.getAllProperties();
        for (CustomItemProperties cip : cips) {
            if (cip.type != 1) continue;
            TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
            cip.updateModelTexture(textureMap, itemModelGenerator);
            cip.updateModelsFull();
        }
    }

    private static List<CustomItemProperties> getAllProperties() {
        ArrayList<CustomItemProperties> list = new ArrayList<CustomItemProperties>();
        CustomItems.addAll(itemProperties, list);
        CustomItems.addAll(enchantmentProperties, list);
        return list;
    }

    private static void addAll(CustomItemProperties[][] cipsArr, List<CustomItemProperties> list) {
        if (cipsArr == null) {
            return;
        }
        for (int i = 0; i < cipsArr.length; ++i) {
            CustomItemProperties[] cips = cipsArr[i];
            if (cips == null) continue;
            for (int k = 0; k < cips.length; ++k) {
                CustomItemProperties cip = cips[k];
                if (cip == null) continue;
                list.add(cip);
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
        HashMap<String, CustomItemProperties> map = new HashMap<String, CustomItemProperties>();
        String typePrefix = type + "/";
        String[] prefixes = new String[]{"mcpatcher/cit/potion/" + typePrefix, "mcpatcher/cit/Potion/" + typePrefix};
        String[] suffixes = new String[]{".png"};
        String[] names = ResUtils.collectFiles(rp, prefixes, suffixes);
        for (int i = 0; i < names.length; ++i) {
            String path;
            String name = path = names[i];
            Properties props = CustomItems.makePotionProperties(name = StrUtils.removePrefixSuffix(name, prefixes, suffixes), type, itemId, path);
            if (props == null) continue;
            String pathProp = StrUtils.removeSuffix(path, suffixes) + ".properties";
            CustomItemProperties cip = new CustomItemProperties(props, pathProp);
            map.put(pathProp, cip);
        }
        return map;
    }

    private static Properties makePotionProperties(String name, String type, int itemId, String path) {
        if (StrUtils.endsWith(name, new String[]{"_n", "_s"})) {
            return null;
        }
        if (name.equals("empty") && type.equals(TYPE_POTION_NORMAL)) {
            itemId = Item.getIdFromItem((Item)Items.glass_bottle);
            PropertiesOrdered props = new PropertiesOrdered();
            ((Hashtable)props).put("type", "item");
            ((Hashtable)props).put("items", "" + itemId);
            return props;
        }
        int potionItemId = itemId;
        int[] damages = (int[])CustomItems.getMapPotionIds().get(name);
        if (damages == null) {
            Config.warn((String)("Potion not found for image: " + path));
            return null;
        }
        StringBuffer bufDamage = new StringBuffer();
        for (int i = 0; i < damages.length; ++i) {
            int damage = damages[i];
            if (type.equals(TYPE_POTION_SPLASH)) {
                damage |= 0x4000;
            }
            if (i > 0) {
                bufDamage.append(" ");
            }
            bufDamage.append(damage);
        }
        int damageMask = 16447;
        if (name.equals("water") || name.equals("mundane")) {
            damageMask |= 0x40;
        }
        PropertiesOrdered props = new PropertiesOrdered();
        ((Hashtable)props).put("type", "item");
        ((Hashtable)props).put("items", "" + potionItemId);
        ((Hashtable)props).put("damage", "" + bufDamage.toString());
        ((Hashtable)props).put("damageMask", "" + damageMask);
        if (type.equals(TYPE_POTION_SPLASH)) {
            ((Hashtable)props).put(KEY_TEXTURE_SPLASH, name);
        } else {
            ((Hashtable)props).put(KEY_TEXTURE_DRINKABLE, name);
        }
        return props;
    }

    private static Map getMapPotionIds() {
        if (mapPotionIds == null) {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put("water", CustomItems.getPotionId(0, 0));
            mapPotionIds.put("awkward", CustomItems.getPotionId(0, 1));
            mapPotionIds.put("thick", CustomItems.getPotionId(0, 2));
            mapPotionIds.put("potent", CustomItems.getPotionId(0, 3));
            mapPotionIds.put("regeneration", CustomItems.getPotionIds(1));
            mapPotionIds.put("movespeed", CustomItems.getPotionIds(2));
            mapPotionIds.put("fireresistance", CustomItems.getPotionIds(3));
            mapPotionIds.put("poison", CustomItems.getPotionIds(4));
            mapPotionIds.put("heal", CustomItems.getPotionIds(5));
            mapPotionIds.put("nightvision", CustomItems.getPotionIds(6));
            mapPotionIds.put("clear", CustomItems.getPotionId(7, 0));
            mapPotionIds.put("bungling", CustomItems.getPotionId(7, 1));
            mapPotionIds.put("charming", CustomItems.getPotionId(7, 2));
            mapPotionIds.put("rank", CustomItems.getPotionId(7, 3));
            mapPotionIds.put("weakness", CustomItems.getPotionIds(8));
            mapPotionIds.put("damageboost", CustomItems.getPotionIds(9));
            mapPotionIds.put("moveslowdown", CustomItems.getPotionIds(10));
            mapPotionIds.put("leaping", CustomItems.getPotionIds(11));
            mapPotionIds.put("harm", CustomItems.getPotionIds(12));
            mapPotionIds.put("waterbreathing", CustomItems.getPotionIds(13));
            mapPotionIds.put("invisibility", CustomItems.getPotionIds(14));
            mapPotionIds.put("thin", CustomItems.getPotionId(15, 0));
            mapPotionIds.put("debonair", CustomItems.getPotionId(15, 1));
            mapPotionIds.put("sparkling", CustomItems.getPotionId(15, 2));
            mapPotionIds.put("stinky", CustomItems.getPotionId(15, 3));
            mapPotionIds.put("mundane", CustomItems.getPotionId(0, 4));
            mapPotionIds.put("speed", mapPotionIds.get("movespeed"));
            mapPotionIds.put("fire_resistance", mapPotionIds.get("fireresistance"));
            mapPotionIds.put("instant_health", mapPotionIds.get("heal"));
            mapPotionIds.put("night_vision", mapPotionIds.get("nightvision"));
            mapPotionIds.put("strength", mapPotionIds.get("damageboost"));
            mapPotionIds.put("slowness", mapPotionIds.get("moveslowdown"));
            mapPotionIds.put("instant_damage", mapPotionIds.get("harm"));
            mapPotionIds.put("water_breathing", mapPotionIds.get("waterbreathing"));
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
        String fullName = "potion." + name;
        Potion[] effectPotions = Potion.potionTypes;
        for (int i = 0; i < effectPotions.length; ++i) {
            String potionName;
            Potion potion = effectPotions[i];
            if (potion == null || !fullName.equals(potionName = potion.getName())) continue;
            return potion.getId();
        }
        return -1;
    }

    private static List makePropertyList(CustomItemProperties[][] propsArr) {
        ArrayList<ArrayList<CustomItemProperties>> list = new ArrayList<ArrayList<CustomItemProperties>>();
        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                CustomItemProperties[] props = propsArr[i];
                ArrayList<CustomItemProperties> propList = null;
                if (props != null) {
                    propList = new ArrayList<CustomItemProperties>(Arrays.asList(props));
                }
                list.add(propList);
            }
        }
        return list;
    }

    private static CustomItemProperties[][] propertyListToArray(List list) {
        CustomItemProperties[][] propArr = new CustomItemProperties[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            List subList = (List)list.get(i);
            if (subList == null) continue;
            CustomItemProperties[] subArr = (CustomItemProperties[]) subList.toArray(new CustomItemProperties[subList.size()]);
            Arrays.sort(subArr, new CustomItemsComparator());
            propArr[i] = subArr;
        }
        return propArr;
    }

    private static void addToItemList(CustomItemProperties cp, List itemList) {
        if (cp.items == null) {
            return;
        }
        for (int i = 0; i < cp.items.length; ++i) {
            int itemId = cp.items[i];
            if (itemId <= 0) {
                Config.warn((String)("Invalid item ID: " + itemId));
                continue;
            }
            CustomItems.addToList(cp, itemList, itemId);
        }
    }

    private static void addToEnchantmentList(CustomItemProperties cp, List enchantmentList) {
        if (cp.type != 2) {
            return;
        }
        if (cp.enchantmentIds == null) {
            return;
        }
        for (int i = 0; i < 256; ++i) {
            int id = i;
            if (!cp.enchantmentIds.isInRange(id)) continue;
            CustomItems.addToList(cp, enchantmentList, id);
        }
    }

    private static void addToList(CustomItemProperties cp, List list, int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        ArrayList<CustomItemProperties> subList = (ArrayList<CustomItemProperties>)list.get(id);
        if (subList == null) {
            subList = new ArrayList<CustomItemProperties>();
            list.set(id, subList);
        }
        subList.add(cp);
    }

    public static IBakedModel getCustomItemModel(ItemStack itemStack, IBakedModel model, ResourceLocation modelLocation, boolean fullModel) {
        if (!fullModel && model.isGui3d()) {
            return model;
        }
        if (itemProperties == null) {
            return model;
        }
        CustomItemProperties props = CustomItems.getCustomItemProperties(itemStack, 1);
        if (props == null) {
            return model;
        }
        IBakedModel customModel = props.getBakedModel(modelLocation, fullModel);
        if (customModel != null) {
            return customModel;
        }
        return model;
    }

    public static boolean bindCustomArmorTexture(ItemStack itemStack, int layer, String overlay) {
        if (itemProperties == null) {
            return false;
        }
        ResourceLocation loc = CustomItems.getCustomArmorLocation(itemStack, layer, overlay);
        if (loc == null) {
            return false;
        }
        Config.getTextureManager().bindTexture(loc);
        return true;
    }

    private static ResourceLocation getCustomArmorLocation(ItemStack itemStack, int layer, String overlay) {
        String key;
        ResourceLocation loc;
        CustomItemProperties props = CustomItems.getCustomItemProperties(itemStack, 3);
        if (props == null) {
            return null;
        }
        if (props.mapTextureLocations == null) {
            return props.textureLocation;
        }
        Item item = itemStack.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        ItemArmor itemArmor = (ItemArmor)item;
        String material = itemArmor.getArmorMaterial().getName();
        StringBuffer sb = new StringBuffer();
        sb.append("texture.");
        sb.append(material);
        sb.append("_layer_");
        sb.append(layer);
        if (overlay != null) {
            sb.append("_");
            sb.append(overlay);
        }
        if ((loc = (ResourceLocation)props.mapTextureLocations.get(key = sb.toString())) == null) {
            return props.textureLocation;
        }
        return loc;
    }

    private static CustomItemProperties getCustomItemProperties(ItemStack itemStack, int type) {
        CustomItemProperties[] cips;
        if (itemProperties == null) {
            return null;
        }
        if (itemStack == null) {
            return null;
        }
        Item item = itemStack.getItem();
        int itemId = Item.getIdFromItem((Item)item);
        if (itemId >= 0 && itemId < itemProperties.length && (cips = itemProperties[itemId]) != null) {
            for (int i = 0; i < cips.length; ++i) {
                CustomItemProperties cip = cips[i];
                if (cip.type != type || !CustomItems.matchesProperties(cip, itemStack, null)) continue;
                return cip;
            }
        }
        return null;
    }

    private static boolean matchesProperties(CustomItemProperties cip, ItemStack itemStack, int[][] enchantmentIdLevels) {
        int i;
        Item item = itemStack.getItem();
        if (cip.damage != null) {
            int damage = itemStack.getItemDamage();
            if (cip.damageMask != 0) {
                damage &= cip.damageMask;
            }
            if (cip.damagePercent) {
                int damageMax = item.getMaxDamage();
                damage = (int)((double)(damage * 100) / (double)damageMax);
            }
            if (!cip.damage.isInRange(damage)) {
                return false;
            }
        }
        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.stackSize)) {
            return false;
        }
        int[][] idLevels = enchantmentIdLevels;
        if (cip.enchantmentIds != null) {
            if (idLevels == null) {
                idLevels = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            boolean idMatch = false;
            for (i = 0; i < idLevels.length; ++i) {
                int id = idLevels[i][0];
                if (!cip.enchantmentIds.isInRange(id)) continue;
                idMatch = true;
                break;
            }
            if (!idMatch) {
                return false;
            }
        }
        if (cip.enchantmentLevels != null) {
            if (idLevels == null) {
                idLevels = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            boolean levelMatch = false;
            for (i = 0; i < idLevels.length; ++i) {
                int level = idLevels[i][1];
                if (!cip.enchantmentLevels.isInRange(level)) continue;
                levelMatch = true;
                break;
            }
            if (!levelMatch) {
                return false;
            }
        }
        if (cip.nbtTagValues != null) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            for (i = 0; i < cip.nbtTagValues.length; ++i) {
                NbtTagValue ntv = cip.nbtTagValues[i];
                if (ntv.matches(nbt)) continue;
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
        NBTTagList nbt;
        Item item = itemStack.getItem();
        NBTTagList nBTTagList = nbt = item == Items.enchanted_book ? Items.enchanted_book.getEnchantments(itemStack) : itemStack.getEnchantmentTagList();
        if (nbt == null || nbt.tagCount() <= 0) {
            return EMPTY_INT2_ARRAY;
        }
        int[][] arr = new int[nbt.tagCount()][2];
        for (int i = 0; i < nbt.tagCount(); ++i) {
            NBTTagCompound tag = nbt.getCompoundTagAt(i);
            short id = tag.getShort("id");
            short lvl = tag.getShort("lvl");
            arr[i][0] = id;
            arr[i][1] = lvl;
        }
        return arr;
    }

    public static boolean renderCustomEffect(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
        if (enchantmentProperties == null) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        int[][] idLevels = CustomItems.getEnchantmentIdLevels(itemStack);
        if (idLevels.length <= 0) {
            return false;
        }
        HashSet<Integer> layersRendered = null;
        boolean rendered = false;
        TextureManager textureManager = Config.getTextureManager();
        for (int i = 0; i < idLevels.length; ++i) {
            CustomItemProperties[] cips;
            int id = idLevels[i][0];
            if (id < 0 || id >= enchantmentProperties.length || (cips = enchantmentProperties[id]) == null) continue;
            for (int p = 0; p < cips.length; ++p) {
                CustomItemProperties cip = cips[p];
                if (layersRendered == null) {
                    layersRendered = new HashSet<Integer>();
                }
                if (!layersRendered.add(id) || !CustomItems.matchesProperties(cip, itemStack, idLevels) || cip.textureLocation == null) continue;
                textureManager.bindTexture(cip.textureLocation);
                float width = cip.getTextureWidth(textureManager);
                if (!rendered) {
                    rendered = true;
                    GlStateManager.depthMask((boolean)false);
                    GlStateManager.depthFunc((int)514);
                    GlStateManager.disableLighting();
                    GlStateManager.matrixMode((int)5890);
                }
                Blender.setupBlend(cip.blend, 1.0f);
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)(width / 2.0f), (float)(width / 2.0f), (float)(width / 2.0f));
                float offset = cip.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate((float)offset, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)cip.rotation, (float)0.0f, (float)0.0f, (float)1.0f);
                renderItem.renderModel(model, -1);
                GlStateManager.popMatrix();
            }
        }
        if (rendered) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((int)770, (int)771);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc((int)515);
            GlStateManager.depthMask((boolean)true);
            textureManager.bindTexture(TextureMap.locationBlocksTexture);
        }
        return rendered;
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
        int[][] idLevels = CustomItems.getEnchantmentIdLevels(itemStack);
        if (idLevels.length <= 0) {
            return false;
        }
        HashSet<Integer> layersRendered = null;
        boolean rendered = false;
        TextureManager textureManager = Config.getTextureManager();
        for (int i = 0; i < idLevels.length; ++i) {
            CustomItemProperties[] cips;
            int id = idLevels[i][0];
            if (id < 0 || id >= enchantmentProperties.length || (cips = enchantmentProperties[id]) == null) continue;
            for (int p = 0; p < cips.length; ++p) {
                CustomItemProperties cip = cips[p];
                if (layersRendered == null) {
                    layersRendered = new HashSet<Integer>();
                }
                if (!layersRendered.add(id) || !CustomItems.matchesProperties(cip, itemStack, idLevels) || cip.textureLocation == null) continue;
                textureManager.bindTexture(cip.textureLocation);
                float width = cip.getTextureWidth(textureManager);
                if (!rendered) {
                    rendered = true;
                    if (Config.isShaders()) {
                        ShadersRender.renderEnchantedGlintBegin();
                    }
                    GlStateManager.enableBlend();
                    GlStateManager.depthFunc((int)514);
                    GlStateManager.depthMask((boolean)false);
                }
                Blender.setupBlend(cip.blend, 1.0f);
                GlStateManager.disableLighting();
                GlStateManager.matrixMode((int)5890);
                GlStateManager.loadIdentity();
                GlStateManager.rotate((float)cip.rotation, (float)0.0f, (float)0.0f, (float)1.0f);
                float texScale = width / 8.0f;
                GlStateManager.scale((float)texScale, (float)(texScale / 2.0f), (float)texScale);
                float offset = cip.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate((float)0.0f, (float)offset, (float)0.0f);
                GlStateManager.matrixMode((int)5888);
                model.render((Entity)entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
            }
        }
        if (rendered) {
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
        return rendered;
    }

    public static boolean isUseGlint() {
        return useGlint;
    }

}

