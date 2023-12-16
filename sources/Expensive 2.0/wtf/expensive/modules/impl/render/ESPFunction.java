package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.*;
import org.joml.Vector4d;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.math.PlayerPositionTracker;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.StencilUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.Color;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static wtf.expensive.util.math.PlayerPositionTracker.isInView;
import static wtf.expensive.util.render.ColorUtil.*;
import static wtf.expensive.util.render.RenderUtil.Render2D.*;

@FunctionAnnotation(name = "ESP", type = Type.Render)
public class ESPFunction extends Function {


    public MultiBoxSetting elements = new MultiBoxSetting("Элементы ЕСП",
            new BooleanOption("Боксы", false),
            new BooleanOption("Здоровье", true),
            new BooleanOption("Текст здоровья", true),
            new BooleanOption("Имена", true),
            new BooleanOption("Эффекты", true),
            new BooleanOption("Броня", true));

    public SliderSetting size = new SliderSetting("Размер", 1, 1, 3, 0.5f);

    public ESPFunction() {
        addSettings(elements, size);
    }

    public HashMap<Vector4d, PlayerEntity> positions = new HashMap<>();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender render) {
            if (render.isRender3D()) {
                updatePlayerPositions(render.partialTicks);
            }

            if (render.isRender2D()) {
                renderPlayerElements(render.matrixStack);
            }
        }
    }

    // Обновляет позиции игроков для отображения на 3D-сцене
    private void updatePlayerPositions(float partialTicks) {
        positions.clear();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (isInView(player) && player.botEntity) {
                if (mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON && player == mc.player) {
                    continue;
                }
                Vector4d position = PlayerPositionTracker.updatePlayerPositions(player, partialTicks);
                if (position != null) {
                    positions.put(position, player);
                }
            }
        }
    }

    // Отображает элементы игрока на 2D-сцене
    private void renderPlayerElements(MatrixStack stack) {

        Vector4i colors = new Vector4i(ColorUtil.getColorStyle(0),
                ColorUtil.getColorStyle(90),
                ColorUtil.getColorStyle(180),
                ColorUtil.getColorStyle(270));

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        for (Map.Entry<Vector4d, PlayerEntity> entry : positions.entrySet()) {
            Vector4d position = entry.getKey();
            PlayerEntity player = entry.getValue();
            if (elements.get(0)) {
                renderBox(position.x, position.y, position.z, position.w, rgba(0, 0, 0, 128), colors);
            }
            if (elements.get(1)) {
                float height = (float) (position.w - position.y);
                player.animationPerc = AnimationMath.fast(player.animationPerc, MathHelper.clamp((player.getHealth() / player.getMaxHealth()), 0, 1), 15);

                drawRectBuilding(position.x - 2 - size.getValue().floatValue() - 0.5f, position.y - 0.5f, size.getValue().floatValue() + 1, height + 1, rgba(0, 0, 0, 128));
                drawVerticalBuilding(position.x - 2 - size.getValue().floatValue(), position.y + (height * (1 - player.animationPerc)), size.getValue().floatValue(), height - height * (1 - player.animationPerc), colors.z, colors.x);
            }

        }
        tessellator.draw();


        BloomHelper.registerRenderCall(() -> {
            buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            for (Map.Entry<Vector4d, PlayerEntity> entry : positions.entrySet()) {
                Vector4d position = entry.getKey();
                PlayerEntity player = entry.getValue();
                if (elements.get(0)) {
                    renderBox(position.x, position.y, position.z, position.w, rgba(0, 0, 0, 0), colors);
                }
                if (elements.get(1)) {
                    float height = (float) (position.w - position.y);
                    player.animationPerc = AnimationMath.fast(player.animationPerc, MathHelper.clamp((player.getHealth() / player.getMaxHealth()), 0, 1), 15);
                    drawVerticalBuilding(position.x - 2 - size.getValue().floatValue(), position.y + (height * (1 - player.animationPerc)), size.getValue().floatValue(), height - height * (1 - player.animationPerc), colors.z, colors.x);
                }

            }
            tessellator.draw();
        });

        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();

        for (Map.Entry<Vector4d, PlayerEntity> entry : positions.entrySet()) {
            Vector4d position = entry.getKey();
            PlayerEntity player = entry.getValue();
            double x = position.x;
            double y = position.y;
            double endX = position.z;
            double endY = position.w;
            float height = (float) (position.w - position.y);
            String healthText = (int) player.getHealth() + "HP";
            float width = (float) (position.z - position.x);

            ITextComponent name = player.getDisplayName();

            if (elements.get(2)) {
                BloomHelper.registerRenderCall(() -> {
                    Fonts.verdana[10].drawStringWithOutline(stack, healthText, position.x - 3 - size.getValue().floatValue() - Fonts.verdana[10].getWidth(healthText), position.y + (height * (1 - player.animationPerc)) + 1, colors.x);
                });
                Fonts.verdana[10].drawStringWithOutline(stack, healthText, position.x - 3 - size.getValue().floatValue() - Fonts.verdana[10].getWidth(healthText), position.y + (height * (1 - player.animationPerc)) + 1, colors.x);
            }
            if (elements.get(3)) {
                renderTags(stack, (float) x, (float) y, (float) endX, (float) endY, player);
            }
            if (elements.get(4)) {
                renderEffects(player, (float) y, (float) endX, stack);
            }
        }


    }

    private void renderEffects(PlayerEntity player,
                               float y,
                               float endX,
                               MatrixStack matrices) {
        EffectInstance[] effects = player.getActivePotionEffects().toArray(new EffectInstance[0]);
        int effectCount = effects.length;

        for (int i = 0; i < effectCount; i++) {
            EffectInstance p = effects[i];

            if (p == null) {
                continue;
            }

            String effectName = I18n.format(p.getEffectName());
            String effectAmplifier = I18n.format("enchantment.level." + (p.getAmplifier() + 1));
            String effectDuration = EffectUtils.getPotionDurationString(p, 1);
            String effectString = effectName + " " + effectAmplifier + TextFormatting.GRAY + "(" + effectDuration + ")" + TextFormatting.RESET;

            Fonts.verdana[12].drawStringWithShadow(matrices, effectString, endX + 2.5f, y - 2 + ((i + 1) * 8), -1);
        }
    }

    private void renderBox(double x,
                           double y,
                           double endX,
                           double endY,
                           int back,
                           Vector4i colors) {
        float size = MathHelper.clamp(this.size.getValue().floatValue() + 1, 2, 5);
        drawRectOutlineBuilding(x - 0.5f, y - 0.5f, endX + 0.5f, endY + 0.5f, size, back);
        drawRectOutlineBuildingGradient(x, y, endX, endY, size - 1, colors);
    }

    public static void drawItemStack(ItemStack stack,
                                     double x,
                                     double y,
                                     String altText,
                                     boolean withoutOverlay) {

        RenderSystem.translated(x, y, 0);
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (!withoutOverlay)
            mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, 0, 0, altText);
        RenderSystem.translated(-x, -y, 0);
    }

    private void renderTags(MatrixStack matrixStack,
                            float posX,
                            float posY,
                            float endPosX,
                            float endPosY,
                            PlayerEntity entity) {
        float maxOffsetY = 0;

        ITextComponent text = entity.getDisplayName();
        TextComponent name = (TextComponent) text;

        String friendPrefix = Managment.FRIEND_MANAGER.isFriend(entity.getName().getString())
                ? TextFormatting.GREEN + "[F] "
                : "";
        ITextComponent friendText = ITextComponent.getTextComponentOrEmpty(friendPrefix);

        TextComponent friendPrefixComponent = (TextComponent) friendText;
        if (Managment.FRIEND_MANAGER.isFriend(entity.getName().getString()) && (Managment.FUNCTION_MANAGER.nameProtect.state && Managment.FUNCTION_MANAGER.nameProtect.friends.get())) {
            friendPrefixComponent.append(new StringTextComponent(TextFormatting.RED + "protected"));
        } else {
            friendPrefixComponent.append(name);
        }
        name = friendPrefixComponent;
        name.append(new StringTextComponent(TextFormatting.DARK_GRAY + " [" + TextFormatting.RED + (int) entity.getHealth() + TextFormatting.DARK_GRAY + "]"));

        float width = mc.fontRenderer.getStringPropertyWidth(name);
        float height = 16;

        TextComponent finalName = name;
        MathUtil.scaleElements((posX + endPosX) / 2f, posY - height / 2, 0.5f, () -> {
            RenderUtil.Render2D.drawRoundedRect((posX + endPosX) / 2f - width / 2f - 5, posY - height - 10, width + 10, height, 3, rgba(15, 15, 15, 200));
            mc.fontRenderer.func_243246_a(matrixStack, finalName, (posX + endPosX) / 2f - width / 2f, posY - height - 5, -1);
        });

        maxOffsetY += 25;
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(entity.getHeldItemMainhand(), entity.getHeldItemOffhand()));
        entity.getArmorInventoryList().forEach(stacks::add);
        stacks.removeIf(w -> w.getItem() instanceof AirItem);
        int totalSize = stacks.size() * 10;
        maxOffsetY += 19;
        AtomicInteger iterable = new AtomicInteger();

        if (elements.get(5)) {
            float finalMaxOffsetY = maxOffsetY;
            MathUtil.scaleElements((posX + endPosX) / 2, posY - maxOffsetY / 2, 0.7f, () -> {
                renderArmorAndEnchantment(stacks, matrixStack, posX, endPosX, posY, finalMaxOffsetY, totalSize, iterable);
            });
        }
    }

    private void renderArmorAndEnchantment(List<ItemStack> stacks,
                                           MatrixStack matrixStack,
                                           float posX,
                                           float endPosX,
                                           float posY,
                                           float finalMaxOffsetY,
                                           int totalSize,
                                           AtomicInteger iterable) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }

            drawItemStack(stack, posX + (endPosX - posX) / 2f + iterable.get() * 20 - totalSize + 2,
                    posY - finalMaxOffsetY + 10, null, false);
            iterable.getAndIncrement();

            ArrayList<String> lines = getEnchantment(stack);
            float center = (posX + (endPosX - posX) / 2f + iterable.get() * 20) - totalSize - 10;
            int i = 0;
            for (String s : lines) {
                Fonts.verdana[12].drawCenteredString(matrixStack, s,
                        center,
                        posY - finalMaxOffsetY + 5 - (i * 7),
                        0xFFFFFFFF);
                i++;
            }
        }

    }


    private ArrayList<String> getEnchantment(ItemStack stack) {
        ArrayList<String> list = new ArrayList<>();

        Item item = stack.getItem();
        if (item instanceof AxeItem) {
            handleAxeEnchantments(list, stack);
        } else if (item instanceof ArmorItem) {
            handleArmorEnchantments(list, stack);
        } else if (item instanceof BowItem) {
            handleBowEnchantments(list, stack);
        } else if (item instanceof SwordItem) {
            handleSwordEnchantments(list, stack);
        } else if (item instanceof ToolItem) {
            handleToolEnchantments(list, stack);
        }

        return list;
    }

    private void handleAxeEnchantments(ArrayList<String> list, ItemStack stack) {
        int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);

        if (sharpness > 0) {
            list.add("Shr" + sharpness);
        }
        if (efficiency > 0) {
            list.add("Eff" + efficiency);
        }
        if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
        }
    }

    private void handleArmorEnchantments(ArrayList<String> list, ItemStack stack) {
        int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
        int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
        int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
        int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
        int vanishingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
        int bindingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
        int fireProt = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
        int blastProt = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

        if (vanishingCurse > 0) {
            list.add("Vanish ");
        }
        if (fireProt > 0) {
            list.add("Firp" + fireProt);
        }
        if (blastProt > 0) {
            list.add("Bla" + blastProt);
        }
        if (bindingCurse > 0) {
            list.add("Bindi" + bindingCurse);
        }
        if (depth > 0) {
            list.add("Dep" + depth);
        }
        if (feather > 0) {
            list.add("Fea" + feather);
        }
        if (protection > 0) {
            list.add("Pro" + protection);
        }
        if (thorns > 0) {
            list.add("Thr" + thorns);
        }
        if (mending > 0) {
            list.add("Men" + mending);
        }
        if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
        }
    }

    private void handleBowEnchantments(ArrayList<String> list, ItemStack stack) {
        int vanishingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
        int bindingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
        int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
        int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);

        if (vanishingCurse > 0) {
            list.add("Vanish" + vanishingCurse);
        }
        if (bindingCurse > 0) {
            list.add("Binding" + bindingCurse);
        }
        if (infinity > 0) {
            list.add("Inf" + infinity);
        }
        if (power > 0) {
            list.add("Pow" + power);
        }
        if (punch > 0) {
            list.add("Pun" + punch);
        }
        if (mending > 0) {
            list.add("Men" + mending);
        }
        if (flame > 0) {
            list.add("Fla" + flame);
        }
        if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
        }
    }

    private void handleSwordEnchantments(ArrayList<String> list, ItemStack stack) {
        int vanishingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
        int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
        int bindingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
        int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
        int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
        int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);

        if (vanishingCurse > 0) {
            list.add("Vanish" + vanishingCurse);
        }
        if (looting > 0) {
            list.add("Loot" + looting);
        }
        if (bindingCurse > 0) {
            list.add("Bindi" + bindingCurse);
        }
        if (sweeping > 0) {
            list.add("Swe" + sweeping);
        }
        if (sharpness > 0) {
            list.add("Shr" + sharpness);
        }
        if (knockback > 0) {
            list.add("Kno" + knockback);
        }
        if (fireAspect > 0) {
            list.add("Fir" + fireAspect);
        }
        if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
        }
        if (mending > 0) {
            list.add("Men" + mending);
        }
    }

    private void handleToolEnchantments(ArrayList<String> list, ItemStack stack) {
        int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
        int vanishingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
        int bindingCurse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
        int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

        if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
        }
        if (mending > 0) {
            list.add("Men" + mending);
        }
        if (vanishingCurse > 0) {
            list.add("Vanish" + vanishingCurse);
        }
        if (bindingCurse > 0) {
            list.add("Binding" + bindingCurse);
        }
        if (efficiency > 0) {
            list.add("Eff" + efficiency);
        }
        if (silkTouch > 0) {
            list.add("Sil" + silkTouch);
        }
        if (fortune > 0) {
            list.add("For" + fortune);
        }
    }


}

