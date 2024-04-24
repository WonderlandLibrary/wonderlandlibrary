package Method.Client.utils.proxy.Overrides;

import Method.Client.module.render.ArmorRender;
import Method.Client.utils.proxy.renderers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderPotion;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntitySign;

import java.util.Map;

public class EntityrenderOverride {

    public static ModRenderItem modRenderItem; // used to provide custom enchantment glint color

    public static void replaceRenderers() {
        Minecraft mc = Minecraft.getMinecraft();
        // Replace render item with custom version
        try {
            modRenderItem = new ModRenderItem(mc.renderItem, mc.modelManager);
            mc.renderItem = modRenderItem;
            mc.itemRenderer.itemRenderer = modRenderItem;
            mc.renderManager.playerRenderer = new ModRenderPlayer(mc.renderManager);
            mc.renderManager.skinMap.put("default", new ModRenderPlayer(mc.renderManager));
            mc.renderManager.skinMap.put("slim", new ModRenderPlayer(mc.renderManager, true));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        TileEntityRendererDispatcher.instance.renderers.put(TileEntitySign.class, new ModRenderSign());


        mc.renderManager.entityRenderMap.put(EntityBoat.class, new ModRenderBoat(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityEnderCrystal.class, new ModEnderCrystal(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(mc.renderManager, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(mc.renderManager, Items.SNOWBALL, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(mc.renderManager, Items.ENDER_PEARL, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(mc.renderManager, Items.ENDER_EYE, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityEgg.class, new RenderSnowball(mc.renderManager, Items.EGG, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityPotion.class, new RenderPotion(mc.renderManager, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(mc.renderManager, Items.EXPERIENCE_BOTTLE, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(mc.renderManager, Items.FIREWORKS, modRenderItem));
        mc.renderManager.entityRenderMap.put(EntityItem.class, new RenderEntityItem(mc.renderManager, modRenderItem));

        mc.renderManager.entityRenderMap.put(EntitySkeleton.class, new ModRenderSkeleton(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityWitherSkeleton.class, new ModRenderWitherSkeleton(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityStray.class, new ModRenderStray(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityZombie.class, new ModRenderZombie(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityHusk.class, new ModRenderHusk(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityZombieVillager.class, new ModRenderZombieVillager(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityGiantZombie.class, new ModRenderGiantZombie(mc.renderManager, 6.0F));
        mc.renderManager.entityRenderMap.put(EntityPigZombie.class, new ModRenderPigZombie(mc.renderManager));
        mc.renderManager.entityRenderMap.put(EntityArmorStand.class, new ModRenderArmorStand(mc.renderManager));


        ((IReloadableResourceManager) (mc.getResourceManager())).registerReloadListener(modRenderItem);
    }

    public static int getColorForEnchantment(Map<Enchantment, Integer> enchMap) {
        if (ArmorRender. CustomColor.getValBoolean()) {
            return ArmorRender.Color.getcolor();
        }
        if (!ArmorRender.enableColoredGlint.getValBoolean()) {
            return -8372020;
        }


        int alpha = 0x66000000;

        // Sword enchantments
        if (enchMap.containsKey(Enchantments.BANE_OF_ARTHROPODS)) return alpha | BANE_OF_ARTHROPODS;
        if (enchMap.containsKey(Enchantments.FIRE_ASPECT)) return alpha | FIRE_ASPECT;
        if (enchMap.containsKey(Enchantments.KNOCKBACK)) return alpha | KNOCKBACK;
        if (enchMap.containsKey(Enchantments.LOOTING)) return alpha | LOOTING;
        if (enchMap.containsKey(Enchantments.SHARPNESS)) return alpha | SHARPNESS;
        if (enchMap.containsKey(Enchantments.SMITE)) return alpha | SMITE;
        if (enchMap.containsKey(Enchantments.SWEEPING)) return alpha | SWEEPING;
        if (enchMap.containsKey(Enchantments.UNBREAKING)) return alpha | UNBREAKING;

        // Bow enchantments
        if (enchMap.containsKey(Enchantments.FLAME)) return alpha | FLAME;
        if (enchMap.containsKey(Enchantments.INFINITY)) return alpha | INFINITY;
        if (enchMap.containsKey(Enchantments.POWER)) return alpha | POWER;
        if (enchMap.containsKey(Enchantments.PUNCH)) return alpha | PUNCH;

        // Tool enchantments
        if (enchMap.containsKey(Enchantments.EFFICIENCY)) return alpha | EFFICIENCY;
        if (enchMap.containsKey(Enchantments.FORTUNE)) return alpha | FORTUNE;
        if (enchMap.containsKey(Enchantments.SILK_TOUCH)) return alpha | SILK_TOUCH;

        // Fishing rod enchantments
        if (enchMap.containsKey(Enchantments.LUCK_OF_THE_SEA)) return alpha | LUCK_OF_THE_SEA;
        if (enchMap.containsKey(Enchantments.LURE)) return alpha | LURE;

        // Armor enchantments
        if (enchMap.containsKey(Enchantments.AQUA_AFFINITY)) return alpha | AQUA_AFFINITY;
        if (enchMap.containsKey(Enchantments.BLAST_PROTECTION)) return alpha | BLAST_PROTECTION;
        if (enchMap.containsKey(Enchantments.DEPTH_STRIDER)) return alpha | DEPTH_STRIDER;
        if (enchMap.containsKey(Enchantments.FEATHER_FALLING)) return alpha | FEATHER_FALLING;
        if (enchMap.containsKey(Enchantments.FIRE_PROTECTION)) return alpha | FIRE_PROTECTION;
        if (enchMap.containsKey(Enchantments.FROST_WALKER)) return alpha | FROST_WALKER;
        if (enchMap.containsKey(Enchantments.MENDING)) return alpha | MENDING;
        if (enchMap.containsKey(Enchantments.PROJECTILE_PROTECTION)) return alpha | PROJECTILE_PROTECTION;
        if (enchMap.containsKey(Enchantments.PROTECTION)) return alpha | PROTECTION;
        if (enchMap.containsKey(Enchantments.RESPIRATION)) return alpha | RESPIRATION;
        if (enchMap.containsKey(Enchantments.THORNS)) return alpha | THORNS;

        // Curses
        if (enchMap.containsKey(Enchantments.VANISHING_CURSE)) return alpha | VANISHING_CURSE;
        if (enchMap.containsKey(Enchantments.BINDING_CURSE)) return alpha | BINDING_CURSE;

        return -8372020;
    }


    public static int BANE_OF_ARTHROPODS = 0xcc00ff;
    public static int FIRE_ASPECT = 0xff4000;
    public static int KNOCKBACK = 0x6600ff;
    public static int LOOTING = 0xffe066;
    public static int SHARPNESS = 0xff9933;
    public static int SMITE = 0x00ccff;
    public static int SWEEPING = 0xccff33;
    public static int UNBREAKING = 0x00cc66;

    // Bow enchantments
    public static int FLAME = 0xff4000;
    public static int INFINITY = 0xcc00ff;
    public static int POWER = 0xff9933;
    public static int PUNCH = 0x6600ff;

    // Tool enchantments
    public static int EFFICIENCY = 0x33ccff;
    public static int FORTUNE = 0xffe066;
    public static int SILK_TOUCH = 0xccff99;

    // Fishing rod enchantments
    public static int LUCK_OF_THE_SEA = 0xffe066;
    public static int LURE = 0x33ccff;

    // Armor enchantments
    public static int AQUA_AFFINITY = 0x3366ff;
    public static int BLAST_PROTECTION = 0xcc6699;
    public static int DEPTH_STRIDER = 0x6666ff;
    public static int FEATHER_FALLING = 0xccff99;
    public static int FIRE_PROTECTION = 0xff4000;
    public static int FROST_WALKER = 0xccffff;
    public static int MENDING = 0xffe066;
    public static int PROJECTILE_PROTECTION = 0xcc99ff;
    public static int PROTECTION = 0x00cc99;
    public static int RESPIRATION = 0x3366ff;
    public static int THORNS = 0xff9933;

    // Curses
    public static int VANISHING_CURSE = 0x6600cc;
    public static int BINDING_CURSE = 0xffffff;

    public static int DEFAULT = -8372020;

    public static float alphaFromColor() {
        return 0.32F;
    }

    public static float redFromColor(int parColor) {
        return (parColor >> 16 & 255) / 255.0F;
    }

    public static float greenFromColor(int parColor) {
        return (parColor >> 8 & 255) / 255.0F;
    }

    public static float blueFromColor(int parColor) {
        return (parColor & 255) / 255.0F;
    }
}
