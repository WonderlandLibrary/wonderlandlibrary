package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.EaseBackIn;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.font.styled.StyledFont;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static wtf.expensive.util.render.RenderUtil.Render2D.drawFace;

@FunctionAnnotation(name = "HUD-NEW", type = Type.Render)
public class HUD extends Function {

    private final MultiBoxSetting elements = new MultiBoxSetting("Элементы",
            new BooleanOption("Ватермарка", true),
            new BooleanOption("Активные функции", true),
            new BooleanOption("Координаты", true),
            new BooleanOption("Активный таргет", true),
            new BooleanOption("Активные стаффы", true),
            new BooleanOption("Активные бинды", true),
            new BooleanOption("Активные эффекты", true),
            new BooleanOption("Уведомления", true)
    );

    private final MultiBoxSetting effects = new MultiBoxSetting("Эффекты",
            new BooleanOption("Тень", true),
            new BooleanOption("Размытие", true)
    );

    private final SliderSetting offset = new SliderSetting("Отступ", 5, 1, 10, 1);

    public HUD() {
        addSettings(elements, effects, offset);
    }

    float round_degree = 3; // Степень скругления ректов

    float width = 4; // Ширина цветного ректа

    Vector4f left_vec = new Vector4f(round_degree, round_degree, 0, 0); // Левая часть ректа
    Vector4f right_vec = new Vector4f(0, 0, round_degree, round_degree); // Правая часть ректа

    int b_color = new Color(0, 0, 0, 128).getRGB(); // Цвет фона ректов
    int t_color = Color.WHITE.getRGB(); // Цвет текста

    int[] colors = new int[360]; // Типо кэш цветов чтобы не дрочить несколько раз

    List<Function> functions = new ArrayList<>();

    Animation tHudAnimation = new EaseBackIn(400, 1, 1.5f);

    PlayerEntity target = null;
    StyledFont font = Fonts.msMedium[14];

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            functions.clear();
            updateFunctions();
        }

        if (event instanceof EventRender e && e.isRender2D()) {
            if (mc.player == null || mc.world == null) return;

            float offset = this.offset.getValue().floatValue();

            for (int i = 0; i < colors.length; i++) {
                colors[i] = Managment.STYLE_MANAGER.getCurrentStyle().getColor(i);
            }

            if (elements.get(0)) renderWatermark(e, offset);
            if (elements.get(1)) renderFunctions(e, offset);
            if (elements.get(2)) renderCoordinates(e, offset);

            if (elements.get(3)) renderTarget(e);
            if (elements.get(5)) renderKeyBinds(e);

            if (elements.get(5)) renderPotions(e, offset);
        }
    }

    private void renderWatermark(EventRender e, float offset) {
        float rectWidth = 80;
        BloomHelper.registerRenderCall(() -> {
            RenderUtil.Render2D.drawRoundedCorner(offset, offset, width, 16, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        });
        RenderUtil.Render2D.drawRoundedCorner(offset, offset, width, 16, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        RenderUtil.Render2D.drawRoundedCorner(offset + width, offset, rectWidth, 16, right_vec, b_color);

        Fonts.icons1[20].drawString(e.matrixStack, "H", offset + width + 8, offset + 6, t_color);
        Fonts.msMedium[16].drawCenteredString(e.matrixStack, "Expensive", offset + width + (rectWidth / 2) + 4, offset + 5.5, t_color);
    }

    private void renderFunctions(EventRender e, float offset) {
        float padding = 4;
        float dumbOffset = 1.5f;

        float height = font.getFontHeight() - dumbOffset + padding;

        List<Function> fs = new ArrayList<>();

        for (Function f : functions) {
            if (!f.state) continue;

            fs.add(f);
        }

        int index = 0;

        for (Function f : fs) {
            boolean isFirst = index == 0;
            boolean isLast = index == fs.size() - 1;

            float width = font.getWidth(f.name) + (padding * 2);

            float r_posX = e.scaledResolution.scaledWidth() - offset - width;
            float r_posY = offset + (index * height);

            float degree = isLast ? round_degree : Math.min(font.getWidth(f.name) + 1 - font.getWidth(fs.get(index + 1).name), round_degree);

            Vector4f left_vec = new Vector4f(isFirst ? round_degree : 0, degree, 0, 0);
            Vector4f right_vec = new Vector4f(0, 0, isFirst ? round_degree : 0, isLast ? round_degree : 0);

            int finalIndex = index;
            BloomHelper.registerRenderCall(() -> {
                RenderUtil.Render2D.drawRoundedCorner(r_posX + width - this.width, r_posY, this.width, height, right_vec, new Vector4i(getColor(finalIndex * 10), getColor((finalIndex + 1) * 10), getColor(finalIndex * 10), getColor((finalIndex + 1) * 10)));
            });

            RenderUtil.Render2D.drawRoundedCorner(r_posX + width - this.width, r_posY, this.width, height, right_vec, new Vector4i(getColor(finalIndex * 10), getColor((finalIndex + 1) * 10), getColor(finalIndex * 10), getColor((finalIndex + 1) * 10)));

            RenderUtil.Render2D.drawRoundedCorner(r_posX - this.width, r_posY, width, height, left_vec, b_color);

            font.drawString(e.matrixStack, f.name, r_posX + padding - this.width - 1, r_posY - dumbOffset + (height / 2), -1);

            index++;
        }
    }

    private void renderPotions(EventRender e, float offset) {
        float posY = 100;

        for (EffectInstance pot : mc.player.getActivePotionEffects().stream().sorted(Comparator.comparing(EffectInstance::getDuration)).toList()) {

            String potText = I18n.format(pot.getEffectName()) + " " + I18n.format("enchantment.level." + (pot.getAmplifier() + 1)) + " - " + EffectUtils.getPotionDurationString(pot, 1);
            float potWidth = Fonts.gilroy[16].getWidth(potText);

            float finalPosY = posY;
            float finalPosY1 = posY;
            BloomHelper.registerRenderCall(() -> {
                RenderUtil.Render2D.drawRoundedCorner(offset, finalPosY, width, 12, left_vec, getColor((int) finalPosY1 * 2));
            });

            RenderUtil.Render2D.drawRoundedCorner(offset, posY, width, 12, left_vec, getColor((int) posY * 2));
            RenderUtil.Render2D.drawRoundedCorner(offset + width, posY, potWidth + 4, 12, right_vec, b_color);

            font.drawString(e.matrixStack, potText, offset + width + 4, posY + 4.5, t_color);

            posY += 16;
        }
    }

    private void renderCoordinates(EventRender e, float offset) {
        String text = "Coords: " + (int) mc.player.getPosX() + ", " + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ();
        float textWidth = Fonts.msMedium[16].getWidth(text) + 16;

        float height = 16;

        float posY = e.scaledResolution.scaledHeight() - height - offset;
        BloomHelper.registerRenderCall(() -> {
            RenderUtil.Render2D.drawRoundedCorner(offset, posY, width, height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        });
        RenderUtil.Render2D.drawRoundedCorner(offset, posY, width, height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        RenderUtil.Render2D.drawRoundedCorner(offset + width, posY, textWidth, height, right_vec, b_color);

        Fonts.msMedium[16].drawCenteredString(e.matrixStack, text, offset + width + (textWidth / 2), posY + 5.5, t_color);
    }
    float health = 0;

    private double scale = 0.0D;

    private void renderTarget(EventRender e) {
        float posX = 450;
        float posY = 300;

        this.target = getTarget(this.target);
        this.scale = tHudAnimation.getOutput();

        if (scale == 0.0F) {
            target = null;
        }

        if (target == null) {
            return;
        }
        this.health = AnimationMath.fast(health, target.getHealth() / target.getMaxHealth(), 5);
        this.health = MathHelper.clamp(this.health, 0, 1);
        String healthValue = (int) MathUtil.round(this.health * 20 + target.getAbsorptionAmount(), 0.5f) + "";

        GlStateManager.pushMatrix();
        AnimationMath.sizeAnimation(posX + (100 / 2), posY + (38 / 2), scale);

        RenderUtil.Render2D.drawRoundedCorner(posX, posY, 100, 38, round_degree, b_color);

        //RenderUtil.Render2D.drawRoundedCorner(posX + 4, posY + 4, 24, 24, round_degree, -1);

        drawFace(posX + 4, posY + 4, 8F, 8F, 8F, 8F, 24, 24, 64F, 64F, (AbstractClientPlayerEntity) target);

        Fonts.gilroyBold[16].drawString(e.matrixStack, target.getName().getString(), posX + 32, posY + 6, t_color);

        Fonts.gilroy[12].drawString(e.matrixStack, "Health: " + healthValue, posX + 32, posY + 16, t_color);
        Fonts.gilroy[12].drawString(e.matrixStack, "Distance: 2.8", posX + 32, posY + 22, t_color);
        BloomHelper.registerRenderCall(() -> {
            GlStateManager.pushMatrix();
            AnimationMath.sizeAnimation(posX + (100 / 2), posY + (38 / 2), scale);
            RenderUtil.Render2D.drawRoundedCorner(posX, posY + 38 - 6, 100 * health, 6, new Vector4f(0, round_degree, 0, round_degree), new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));
            GlStateManager.popMatrix();
        });
        RenderUtil.Render2D.drawRoundedCorner(posX, posY + 38 - 6, 100 * health, 6, new Vector4f(0, round_degree, 0, round_degree), new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));
        GlStateManager.popMatrix();

    }

    float kb_Height = 0;

    private void renderKeyBinds(EventRender e) {
        float posX = 300;
        float posY = 200;
        BloomHelper.registerRenderCall(() -> {
            RenderUtil.Render2D.drawRoundedCorner(posX, posY, width, kb_Height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        });

        RenderUtil.Render2D.drawRoundedCorner(posX, posY, width, kb_Height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
        RenderUtil.Render2D.drawRoundedCorner(posX + width, posY, 100, kb_Height, right_vec, b_color);

        Fonts.msSemiBold[16].drawString(e.matrixStack, "Key Binds", posX + width + 4, posY + 6, t_color);

        RenderUtil.Render2D.drawRect(posX + width, posY + 16, 100, 0.5f, getColor(100));

        int i = 0;

        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (!f.state || f.bind == 0) continue;

            String text = ClientUtil.getKey(f.bind);

            String bindText = "[" + text.toUpperCase() + "]";
            float bindWidth = Fonts.msMedium[14].getWidth(bindText);

            Fonts.msMedium[14].drawString(e.matrixStack, f.name, posX + width + 4, posY + 22 + (i * 10), t_color);

            Fonts.msMedium[14].drawString(e.matrixStack, bindText, posX + width + 100 - bindWidth - 4, posY + 22 + (i * 10), t_color);

            i++;
        }

        kb_Height = 22 + (i * 10);
    }

    private void updateFunctions() {
        for (Function function : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (function.category == Type.Render) continue;

            functions.add(function);
        }

        functions.sort((f1, f2) -> Float.compare(font.getWidth(f2.name), font.getWidth(f1.name)));
    }

    private PlayerEntity getTarget(PlayerEntity nullTarget) {
        PlayerEntity target = nullTarget;

        if (Managment.FUNCTION_MANAGER.auraFunction.getTarget() instanceof PlayerEntity) {
            target = (PlayerEntity) Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            tHudAnimation.setDirection(Direction.FORWARDS);
        } else if (mc.currentScreen instanceof ChatScreen) {
            target = mc.player;
            tHudAnimation.setDirection(Direction.FORWARDS);
        } else {
            tHudAnimation.setDirection(Direction.BACKWARDS);
        }

        return target;
    }

    private int getColor(int index) {
        return colors[index % colors.length];
    }
}
