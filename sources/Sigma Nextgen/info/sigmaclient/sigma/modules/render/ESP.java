package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.gui.ColorChanger;
import info.sigmaclient.sigma.music.statics.Player;
import info.sigmaclient.sigma.sigma5.utils.ShadowESP;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.M3DUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.blurs.RoundRectShader;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.AntiBot;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.StringUtils;

import javax.vecmath.Vector4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static info.sigmaclient.sigma.utils.render.RenderUtils.createFrameBuffer;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawRect;
import static info.sigmaclient.sigma.utils.render.blurs.Gradient.drawGradientLR;
import static info.sigmaclient.sigma.utils.render.blurs.Gradient.drawGradientTB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ESP extends Module {
    BoxOutlineESP boxOutlineESP = new BoxOutlineESP();
   public static ShadowESP shadowESP = new ShadowESP();
    public static ModeValue mode = new ModeValue("Type", "Box Outline", new String[]{
            "Box Outline", "Shadow"
    });
    public static ColorValue color = new ColorValue("Color", -1);
    public static BooleanValue player = new BooleanValue("Player", true);
    public static BooleanValue monster = new BooleanValue("Monsters", true);
    public static BooleanValue animals = new BooleanValue("Animals", true);
    public static BooleanValue armor = new BooleanValue("Armors", false);
    private final Map<Entity, Vector4f> entityPosition = new HashMap<>();
    public static boolean isTargetEnable(Entity e){
        if(e == mc.player) return false;
        if(player.isEnable()){
            if(e instanceof PlayerEntity){
                if(AntiBot.isServerBots(e)) return false;
                return true;
            }
        }
        if(monster.isEnable()){
            if(e instanceof MonsterEntity){
                return true;
            }
        }
        if(animals.isEnable()){
            if(e instanceof AnimalEntity){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (mode.is("2D")) {
            entityPosition.clear();
            for (final Entity entity : mc.world.getLoadedEntityList()) {
                if(entity.equals(mc.player)) continue;
                if (isTargetEnable(entity) && M3DUtil.isInView(entity)) {
                    entityPosition.put(entity, M3DUtil.getEntityPositionsOn2D(entity));
                }
            }
        } else if (mode.is("Box Outline")) {
            boxOutlineESP.draw();
        } else if (mode.is("Shadow")) {
            shadowESP.renderEvent();
        }
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        super.onRender3DEvent(event);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        if (mode.is("2D")) {
            for (Entity entity : entityPosition.keySet()) {
                Vector4f pos = entityPosition.get(entity);
                float x = pos.getX(),
                        y = pos.getY(),
                        right = pos.getZ(),
                        bottom = pos.getW();

                    float outlineThickness = .5f;
                    RenderUtils.resetColor();
                    //top
                    drawGradientLR(x, y, (right - x), 1, 1, ColorChanger.r1.getColor(), ColorChanger.r2.getColor());
                    //left
                    drawGradientTB(x, y, 1, bottom - y, 1, ColorChanger.r1.getColor(), ColorChanger.r2.getColor());
                    //bottom
                    drawGradientLR(x, bottom, right - x, 1, 1, ColorChanger.r1.getColor(), ColorChanger.r2.getColor());
                    //right
                    drawGradientTB(right, y, 1, (bottom - y) + 1, 1, ColorChanger.r1.getColor(), ColorChanger.r2.getColor());
                if(armor.isEnable() && entity instanceof PlayerEntity entity1) {
                    int index = 0;
                    ArrayList<ItemStack> stackList = new ArrayList<>();
                    stackList.add(entity1.getHeldItemMainhand());
                    stackList.add(entity1.getHeldItemOffhand());
                    stackList.addAll((Collection<? extends ItemStack>) entity1.getArmorInventoryList());
                    for (ItemStack a : stackList) {
                        mc.ingameGUI.renderHotbarItemCustom((int) (right + 3), (int) (y + index * 12f), event.renderTime, mc.player, a, 1f);
                        if(a.getItem() instanceof ArmorItem){
                            int pr = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, a);
                            int Th = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, a);
                            int x2 = (int) (right + 17);
                            int y2 = (int) (y + index * 12f);
                            if(pr > 0) {
                                FontUtil.sfuiFontBold16.drawString("Pr §c" + pr, x2, y2, -1);
                            }
                            if(Th > 0) {
                                FontUtil.sfuiFontBold16.drawString("Tn §c" + pr, x2, y2, -1);
                            }
                        }
                        if(a.getItem() instanceof SwordItem){
                            int Sh = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, a);
                            int Un = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, a);
                            int x2 = (int) (right + 17);
                            int y2 = (int) (y + index * 12f);
                            if(Sh > 0) {
                                FontUtil.sfuiFontBold16.drawString("Sh §c" + Sh, x2, y2, -1);
                            }
                            if(Un > 0) {
                                FontUtil.sfuiFontBold16.drawString("Un §c" + Un, x2, y2, -1);
                            }
                        }
                        index++;
                    }
                }
                    //Outline

//                    //top
//                    drawRect(x - .5f, y - outlineThickness, (right - x) + 2, outlineThickness, Color.BLACK.getRGB());
//                    //Left
//                    drawRect(x - outlineThickness, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());
//                    //bottom
//                    drawRect(x - .5f, (bottom + 1), (right - x) + 2, outlineThickness, Color.BLACK.getRGB());
//                    //Right
//                    drawRect(right + 1, y, outlineThickness, (bottom - y) + 1, Color.BLACK.getRGB());
//
//
//                    //top
//                    drawRect(x + 1, y + 1, (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
//                    //Left
//                    drawRect(x + 1, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());
//                    //bottom
//                    drawRect(x + 1, (bottom - outlineThickness), (right - x) - 1, outlineThickness, Color.BLACK.getRGB());
//                    //Right
//                    drawRect(right - outlineThickness, y + 1, outlineThickness, (bottom - y) - 1, Color.BLACK.getRGB());

            }
        }
    }

    public ESP() {
        super("ESP", Category.Render, "See entities anywhere anytime");
     registerValue(mode);
     registerValue(color);
     registerValue(player);
     registerValue(monster);
     registerValue(animals);
//     registerValue(armor);
    }
}
