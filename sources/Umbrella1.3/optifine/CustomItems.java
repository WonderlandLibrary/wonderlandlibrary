/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import optifine.Blender;
import optifine.Config;
import optifine.CustomItemProperties;
import optifine.CustomItemsComparator;
import optifine.NbtTagValue;
import optifine.ResUtils;
import optifine.StrUtils;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class CustomItems {
    private static CustomItemProperties[][] itemProperties = null;
    private static CustomItemProperties[][] enchantmentProperties = null;
    private static Map mapPotionIds = null;
    private static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static boolean useGlint = true;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final int[][] EMPTY_INT2_ARRAY = new int[0][];

    public static void updateIcons(TextureMap textureMap) {
        itemProperties = null;
        enchantmentProperties = null;
        useGlint = true;
        if (Config.isCustomItems()) {
            CustomItems.readCitProperties("mcpatcher/cit.properties");
            IResourcePack[] rps = Config.getResourcePacks();
            for (int i = rps.length - 1; i >= 0; --i) {
                IResourcePack rp = rps[i];
                CustomItems.updateIcons(textureMap, rp);
            }
            CustomItems.updateIcons(textureMap, Config.getDefaultResourcePack());
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
            ResourceLocation e = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return;
            }
            Config.dbg("CustomItems: Loading " + fileName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            useGlint = Config.parseBoolean(props.getProperty("useGlint"), true);
        }
        catch (FileNotFoundException var4) {
            return;
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    private static void updateIcons(TextureMap textureMap, IResourcePack rp) {
        CustomItemProperties[] var18;
        int var17;
        Object[] names = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", null);
        Map mapAutoProperties = CustomItems.makeAutoImageProperties(rp);
        if (mapAutoProperties.size() > 0) {
            Set itemList = mapAutoProperties.keySet();
            Object[] enchantmentList = itemList.toArray(new String[itemList.size()]);
            names = (String[])Config.addObjectsToArray(names, enchantmentList);
        }
        Arrays.sort(names);
        List var14 = CustomItems.makePropertyList(itemProperties);
        List var15 = CustomItems.makePropertyList(enchantmentProperties);
        for (int comp = 0; comp < names.length; ++comp) {
            Object i = names[comp];
            Config.dbg("CustomItems: " + (String)i);
            try {
                CustomItemProperties cips = null;
                if (mapAutoProperties.containsKey(i)) {
                    cips = (CustomItemProperties)mapAutoProperties.get(i);
                }
                if (cips == null) {
                    ResourceLocation locFile = new ResourceLocation((String)i);
                    InputStream in = rp.getInputStream(locFile);
                    if (in == null) {
                        Config.warn("CustomItems file not found: " + (String)i);
                        continue;
                    }
                    Properties props = new Properties();
                    props.load(in);
                    cips = new CustomItemProperties(props, (String)i);
                }
                if (!cips.isValid((String)i)) continue;
                cips.updateIcons(textureMap);
                CustomItems.addToItemList(cips, var14);
                CustomItems.addToEnchantmentList(cips, var15);
                continue;
            }
            catch (FileNotFoundException var12) {
                Config.warn("CustomItems file not found: " + (String)i);
                continue;
            }
            catch (Exception var13) {
                var13.printStackTrace();
            }
        }
        itemProperties = CustomItems.propertyListToArray(var14);
        enchantmentProperties = CustomItems.propertyListToArray(var15);
        Comparator var16 = CustomItems.getPropertiesComparator();
        for (var17 = 0; var17 < itemProperties.length; ++var17) {
            var18 = itemProperties[var17];
            if (var18 == null) continue;
            Arrays.sort(var18, var16);
        }
        for (var17 = 0; var17 < enchantmentProperties.length; ++var17) {
            var18 = enchantmentProperties[var17];
            if (var18 == null) continue;
            Arrays.sort(var18, var16);
        }
    }

    private static Comparator getPropertiesComparator() {
        Comparator comp = new Comparator(){

            public int compare(Object o1, Object o2) {
                CustomItemProperties cip1 = (CustomItemProperties)o1;
                CustomItemProperties cip2 = (CustomItemProperties)o2;
                return cip1.layer != cip2.layer ? cip1.layer - cip2.layer : (cip1.weight != cip2.weight ? cip2.weight - cip1.weight : (!cip1.basePath.equals(cip2.basePath) ? cip1.basePath.compareTo(cip2.basePath) : cip1.name.compareTo(cip2.name)));
            }
        };
        return comp;
    }

    public static void updateModels() {
        if (itemProperties != null) {
            for (int id = 0; id < itemProperties.length; ++id) {
                CustomItemProperties[] cips = itemProperties[id];
                if (cips == null) continue;
                for (int c = 0; c < cips.length; ++c) {
                    CustomItemProperties cip = cips[c];
                    if (cip == null || cip.type != 1) continue;
                    TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
                    cip.updateModel(textureMap, itemModelGenerator);
                }
            }
        }
    }

    private static Map makeAutoImageProperties(IResourcePack rp) {
        HashMap map = new HashMap();
        map.putAll(CustomItems.makePotionImageProperties(rp, false));
        map.putAll(CustomItems.makePotionImageProperties(rp, true));
        return map;
    }

    private static Map makePotionImageProperties(IResourcePack rp, boolean splash) {
        HashMap<String, CustomItemProperties> map = new HashMap<String, CustomItemProperties>();
        String type = splash ? "splash/" : "normal/";
        String[] prefixes = new String[]{"mcpatcher/cit/potion/" + type, "mcpatcher/cit/Potion/" + type};
        String[] suffixes = new String[]{".png"};
        String[] names = ResUtils.collectFiles(rp, prefixes, suffixes);
        for (int i = 0; i < names.length; ++i) {
            String path = names[i];
            String name = StrUtils.removePrefixSuffix(path, prefixes, suffixes);
            Properties props = CustomItems.makePotionProperties(name, splash, path);
            if (props == null) continue;
            String pathProp = String.valueOf(StrUtils.removeSuffix(path, suffixes)) + ".properties";
            CustomItemProperties cip = new CustomItemProperties(props, pathProp);
            map.put(pathProp, cip);
        }
        return map;
    }

    private static Properties makePotionProperties(String name, boolean splash, String path) {
        if (StrUtils.endsWith(name, new String[]{"_n", "_s"})) {
            return null;
        }
        if (name.equals("empty") && !splash) {
            int potionItemId = Item.getIdFromItem(Items.glass_bottle);
            Properties var8 = new Properties();
            var8.put("type", "item");
            var8.put("items", "" + potionItemId);
            return var8;
        }
        int potionItemId = Item.getIdFromItem(Items.potionitem);
        int[] damages = (int[])CustomItems.getMapPotionIds().get(name);
        if (damages == null) {
            Config.warn("Potion not found for image: " + path);
            return null;
        }
        StringBuffer bufDamage = new StringBuffer();
        for (int damageMask = 0; damageMask < damages.length; ++damageMask) {
            int props = damages[damageMask];
            if (splash) {
                props |= 0x4000;
            }
            if (damageMask > 0) {
                bufDamage.append(" ");
            }
            bufDamage.append(props);
        }
        int var9 = 16447;
        Properties var10 = new Properties();
        var10.put("type", "item");
        var10.put("items", "" + potionItemId);
        var10.put("damage", bufDamage.toString());
        var10.put("damageMask", "" + var9);
        if (splash) {
            var10.put(KEY_TEXTURE_SPLASH, name);
        } else {
            var10.put(KEY_TEXTURE_DRINKABLE, name);
        }
        return var10;
    }

    private static Map getMapPotionIds() {
        if (mapPotionIds == null) {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put("water", new int[1]);
            mapPotionIds.put("awkward", new int[]{16});
            mapPotionIds.put("thick", new int[]{32});
            mapPotionIds.put("potent", new int[]{48});
            mapPotionIds.put("regeneration", CustomItems.getPotionIds(1));
            mapPotionIds.put("moveSpeed", CustomItems.getPotionIds(2));
            mapPotionIds.put("fireResistance", CustomItems.getPotionIds(3));
            mapPotionIds.put("poison", CustomItems.getPotionIds(4));
            mapPotionIds.put("heal", CustomItems.getPotionIds(5));
            mapPotionIds.put("nightVision", CustomItems.getPotionIds(6));
            mapPotionIds.put("clear", CustomItems.getPotionIds(7));
            mapPotionIds.put("bungling", CustomItems.getPotionIds(23));
            mapPotionIds.put("charming", CustomItems.getPotionIds(39));
            mapPotionIds.put("rank", CustomItems.getPotionIds(55));
            mapPotionIds.put("weakness", CustomItems.getPotionIds(8));
            mapPotionIds.put("damageBoost", CustomItems.getPotionIds(9));
            mapPotionIds.put("moveSlowdown", CustomItems.getPotionIds(10));
            mapPotionIds.put("diffuse", CustomItems.getPotionIds(11));
            mapPotionIds.put("smooth", CustomItems.getPotionIds(27));
            mapPotionIds.put("refined", CustomItems.getPotionIds(43));
            mapPotionIds.put("acrid", CustomItems.getPotionIds(59));
            mapPotionIds.put("harm", CustomItems.getPotionIds(12));
            mapPotionIds.put("waterBreathing", CustomItems.getPotionIds(13));
            mapPotionIds.put("invisibility", CustomItems.getPotionIds(14));
            mapPotionIds.put("thin", CustomItems.getPotionIds(15));
            mapPotionIds.put("debonair", CustomItems.getPotionIds(31));
            mapPotionIds.put("sparkling", CustomItems.getPotionIds(47));
            mapPotionIds.put("stinky", CustomItems.getPotionIds(63));
        }
        return mapPotionIds;
    }

    private static int[] getPotionIds(int baseId) {
        return new int[]{baseId, baseId + 16, baseId + 32, baseId + 48};
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
            CustomItemProperties[] subArr = subList.toArray(new CustomItemProperties[subList.size()]);
            Arrays.sort(subArr, new CustomItemsComparator());
            propArr[i] = subArr;
        }
        return propArr;
    }

    private static void addToItemList(CustomItemProperties cp, List itemList) {
        if (cp.items != null) {
            for (int i = 0; i < cp.items.length; ++i) {
                int itemId = cp.items[i];
                if (itemId <= 0) {
                    Config.warn("Invalid item ID: " + itemId);
                    continue;
                }
                CustomItems.addToList(cp, itemList, itemId);
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
        ArrayList<CustomItemProperties> subList = (ArrayList<CustomItemProperties>)list.get(id);
        if (subList == null) {
            subList = new ArrayList<CustomItemProperties>();
            list.set(id, subList);
        }
        subList.add(cp);
    }

    public static IBakedModel getCustomItemModel(ItemStack itemStack, IBakedModel model, ModelResourceLocation modelLocation) {
        if (model.isAmbientOcclusionEnabled()) {
            return model;
        }
        if (itemProperties == null) {
            return model;
        }
        CustomItemProperties props = CustomItems.getCustomItemProperties(itemStack, 1);
        return props == null ? model : props.getModel(modelLocation);
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
        CustomItemProperties props = CustomItems.getCustomItemProperties(itemStack, 3);
        if (props == null) {
            return null;
        }
        if (props.mapTextureLocations == null) {
            return null;
        }
        Item item = itemStack.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        ItemArmor itemArmor = (ItemArmor)item;
        String material = itemArmor.getArmorMaterial().func_179242_c();
        StringBuffer sb = new StringBuffer();
        sb.append("texture.");
        sb.append(material);
        sb.append("_layer_");
        sb.append(layer);
        if (overlay != null) {
            sb.append("_");
            sb.append(overlay);
        }
        String key = sb.toString();
        ResourceLocation loc = (ResourceLocation)props.mapTextureLocations.get(key);
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
        int itemId = Item.getIdFromItem(item);
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
        int ntv;
        int i;
        boolean var9;
        Item item = itemStack.getItem();
        if (cip.damage != null) {
            int idLevels = itemStack.getItemDamage();
            if (cip.damageMask != 0) {
                idLevels &= cip.damageMask;
            }
            if (cip.damagePercent) {
                int nbt = item.getMaxDamage();
                idLevels = (int)((double)(idLevels * 100) / (double)nbt);
            }
            if (!cip.damage.isInRange(idLevels)) {
                return false;
            }
        }
        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.stackSize)) {
            return false;
        }
        int[][] var8 = enchantmentIdLevels;
        if (cip.enchantmentIds != null) {
            if (enchantmentIdLevels == null) {
                var8 = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            var9 = false;
            for (i = 0; i < var8.length; ++i) {
                ntv = var8[i][0];
                if (!cip.enchantmentIds.isInRange(ntv)) continue;
                var9 = true;
                break;
            }
            if (!var9) {
                return false;
            }
        }
        if (cip.enchantmentLevels != null) {
            if (var8 == null) {
                var8 = CustomItems.getEnchantmentIdLevels(itemStack);
            }
            var9 = false;
            for (i = 0; i < var8.length; ++i) {
                ntv = var8[i][1];
                if (!cip.enchantmentLevels.isInRange(ntv)) continue;
                var9 = true;
                break;
            }
            if (!var9) {
                return false;
            }
        }
        if (cip.nbtTagValues != null) {
            NBTTagCompound var10 = itemStack.getTagCompound();
            for (i = 0; i < cip.nbtTagValues.length; ++i) {
                NbtTagValue var11 = cip.nbtTagValues[i];
                if (var11.matches(var10)) continue;
                return false;
            }
        }
        return true;
    }

    private static int[][] getEnchantmentIdLevels(ItemStack itemStack) {
        NBTTagList nbt;
        Item item = itemStack.getItem();
        NBTTagList nBTTagList = nbt = item == Items.enchanted_book ? Items.enchanted_book.func_92110_g(itemStack) : itemStack.getEnchantmentTagList();
        if (nbt != null && nbt.tagCount() > 0) {
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
        return EMPTY_INT2_ARRAY;
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
                    GlStateManager.depthMask(false);
                    GlStateManager.depthFunc(514);
                    GlStateManager.disableLighting();
                    GlStateManager.matrixMode(5890);
                }
                Blender.setupBlend(cip.blend, 1.0f);
                GlStateManager.pushMatrix();
                GlStateManager.scale(width / 2.0f, width / 2.0f, width / 2.0f);
                float offset = cip.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate(offset, 0.0f, 0.0f);
                GlStateManager.rotate(cip.rotation, 0.0f, 0.0f, 1.0f);
                renderItem.func_175035_a(model, -1);
                GlStateManager.popMatrix();
            }
        }
        if (rendered) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
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
                        ShadersRender.layerArmorBaseDrawEnchantedGlintBegin();
                    }
                    GlStateManager.enableBlend();
                    GlStateManager.depthFunc(514);
                    GlStateManager.depthMask(false);
                }
                Blender.setupBlend(cip.blend, 1.0f);
                GlStateManager.disableLighting();
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.rotate(cip.rotation, 0.0f, 0.0f, 1.0f);
                float texScale = width / 8.0f;
                GlStateManager.scale(texScale, texScale / 2.0f, texScale);
                float offset = cip.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                GlStateManager.translate(0.0f, offset, 0.0f);
                GlStateManager.matrixMode(5888);
                model.render(entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
            }
        }
        if (rendered) {
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
                ShadersRender.layerArmorBaseDrawEnchantedGlintEnd();
            }
        }
        return rendered;
    }

    public static boolean isUseGlint() {
        return useGlint;
    }
}

