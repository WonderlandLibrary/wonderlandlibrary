package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import org.joml.Vector4i;
import wtf.expensive.Initilization;
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
import wtf.expensive.util.BetterText;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.EaseBackIn;
import wtf.expensive.util.drag.Dragging;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.font.styled.StyledFont;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.HudUtil;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static wtf.expensive.util.render.RenderUtil.Render2D.drawFace;

@FunctionAnnotation(name = "HUD", type = Type.Render)
public class HUD2 extends Function {

    public final MultiBoxSetting elements = new MultiBoxSetting("Элементы",
            new BooleanOption("Ватермарка", true),
            new BooleanOption("Активные функции", true),
            new BooleanOption("Координаты", true),
            new BooleanOption("Активный таргет", true),
            new BooleanOption("Активные стаффы", true),
            new BooleanOption("Активные бинды", true),
            new BooleanOption("Активные эффекты", true),
            new BooleanOption("Уведомления", true)
    );

    public final BooleanOption glowing = new BooleanOption("Свечение", true);
    public final BooleanOption shadow = new BooleanOption("Тень", true);

    private final SliderSetting offset = new SliderSetting("Отступ", 5, 1, 10, 1);

    public HUD2() {
        addSettings(elements, glowing, shadow, offset);
    }

    final float round_degree = 3;

    final float cWidth = 4;

    final Vector4f left_vec = new Vector4f(round_degree, round_degree, 0, 0);
    final Vector4f right_vec = new Vector4f(0, 0, round_degree, round_degree);

    final int b_color = new Color(0, 0, 0, 128).getRGB();
    final int t_color = Color.WHITE.getRGB();

    int[] colors = new int[360];

    final StyledFont icons = Fonts.icons1[20];
    final StyledFont medium = Fonts.msMedium[16];
    final StyledFont small = Fonts.msMedium[14];

    MainWindow window;

    List<Function> functions = new ArrayList<>();

    final Dragging keyBinds = Initilization.createDrag(this, "KeyBinds-new", 10, 100);
    final Dragging staffList = Initilization.createDrag(this, "StaffList-new", 10, 200);

    final Dragging targetHUD = Initilization.createDrag(this, "TargetHUD-new", 10, 300);
    final Dragging timerHUD = Initilization.createDrag(this, "TimerHUD-new", 10, 400);

    Animation tHudAnimation = new EaseBackIn(400, 1, 1.5f);
    PlayerEntity target = null;


    private final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
    private final Pattern prefixMatches = Pattern.compile(".*(mod|der|adm|help|wne|мод|хелп|помо|адм|владе|отри|таф|taf|curat|курато|dev|раз|supp|сапп|yt|ютуб).*");

    private final Map<ITextComponent, String> staffPlayers = new LinkedHashMap<>();


    @Override
    public void onEvent(Event event) {
        if (mc.player == null || mc.world == null) return;

        if (event instanceof EventUpdate) {
            staffPlayers.clear();

            for (ScorePlayerTeam team : mc.world.getScoreboard().getTeams().stream().sorted(Comparator.comparing(Team::getName)).toList()) {
                String name = team.getMembershipCollection().toString();
                name = name.substring(1, name.length() - 1);
                if (namePattern.matcher(name).matches()) {
                    if (prefixMatches.matcher(team.getPrefix().getString().toLowerCase(Locale.ROOT)).matches() || Managment.STAFF_MANAGER.isStaff(name)) {
                        staffPlayers.put(team.getPrefix(), name); // САЛАТ СПАСАЙ
                    }
                }
            }

            if (Managment.FUNCTION_MANAGER.getFunctions().isEmpty() || !functions.isEmpty()) return;
            updateFunctions();
        }

        if (event instanceof EventRender e && e.isRender2D()) {
            for (int i = 0; i < colors.length; i++) {
                colors[i] = Managment.STYLE_MANAGER.getCurrentStyle().getColor(i);
            }

            window = e.scaledResolution;

            final boolean glowing = this.glowing.get();
            final boolean shadow = this.shadow.get();
            final int offset = this.offset.getValue().intValue();
            final MatrixStack matrixStack = e.matrixStack;

            if (elements.get(0)) renderWatermark(matrixStack, offset, glowing, shadow);
            if (elements.get(1)) renderFunctions(matrixStack, offset, glowing);
            if (elements.get(2)) renderCoordinates(matrixStack, offset, glowing);
            if (elements.get(3)) renderTarget(matrixStack, offset, glowing);
            if (elements.get(4)) renderStaffList(matrixStack, offset, glowing);
            if (elements.get(5)) renderKeyBinds(matrixStack, offset, glowing);
            if (elements.get(6)) renderPotions(matrixStack, offset, glowing);

            renderTimer(matrixStack, offset, glowing);
        }
    }





    BetterText betterText = new BetterText(List.of("",
            "Expensive",
            "uid: " + Managment.USER_PROFILE.getUid(),
            "user: " + Managment.USER_PROFILE.getName()), 2000);

    private void renderWatermark(MatrixStack matrixStack, int offset, boolean glowing, boolean shadow) {
        final Vector4i vec = new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100));

        final float width = Math.max(80, medium.getWidth(betterText.output) + 40);
        final float height = 16;

        if (glowing) {
            BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(offset, offset, cWidth, height, left_vec, vec));
        }
        if (shadow) {
            RenderUtil.Render2D.drawShadow(offset + cWidth, offset, width, height, 10, RenderUtil.reAlphaInt(b_color, 64));
        }

        RenderUtil.Render2D.drawRoundedCorner(offset, offset, cWidth, height, left_vec, vec);
        RenderUtil.Render2D.drawRoundedCorner(offset + cWidth, offset, width, height, right_vec, b_color);

        icons.drawString(matrixStack, "H", offset + cWidth + (height / 2), offset + 6, t_color);
        medium.drawCenteredString(matrixStack, betterText.output, offset + cWidth + (width / 2) + 4, offset + 5.5, t_color);

        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/animation.png"), offset, offset + 21, 12, 12, ColorUtil.getColorStyle(0));

        medium.drawString(matrixStack, ClientUtil.gradient(mc.debugFPS + "FPS", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(90)), offset + 15, offset + 25, t_color);

        RenderUtil.Render2D.drawImage(new ResourceLocation("expensive/images/wifi.png"), offset + width - 5, offset + 21, 12, 12, ColorUtil.getColorStyle(0));

        medium.drawString(matrixStack, ClientUtil.gradient(HudUtil.calculatePing() + "MS", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(90)), offset + width - 6 - medium.getWidth(HudUtil.calculatePing() + "MS"), offset + 25, t_color);
    }

    private void renderFunctions(MatrixStack matrixStack, int offset, boolean glowing) {
        float padding = 4;
        float dumbOffset = 1.5f;

        float height = small.getFontHeight() - dumbOffset + padding;

        List<Function> fs = new ArrayList<>();

        for (Function f : functions) {
            f.animation = AnimationMath.fast(f.animation, f.state ? 1 : 0, 15);
            if (f.animation < 0.1) continue;
            fs.add(f);
        }

        List<Function> fs1 = new ArrayList<>();
        int in = 0;
        for (Function f : functions) {
            if (!f.state) continue;
            fs1.add(f);
        }

        for (Function f : fs1) {
            boolean isLast = in == fs1.size() - 1;

            f.degree = isLast ? round_degree : Math.min(small.getWidth(f.name) + 1 - small.getWidth(fs1.get(in + 1).name), round_degree);
            in++;
        }

        float index = 0;

        for (Function f : fs) {
            boolean isFirst = index == 0;
            boolean isLast = Math.round(index) == fs.size() - 1;

            float width = small.getWidth(f.name) + (padding * 2);

            float r_posX = window.scaledWidth() - offset - width;
            float r_posY = offset + (index * height);

            float degree = f.degree;

            Vector4f left_vec = new Vector4f(isFirst ? round_degree : 0, degree, 0, 0);
            Vector4f right_vec = new Vector4f(0, 0, isFirst ? round_degree : 0, isLast ? round_degree : 0);

            float finalIndex = index;

            Vector4i vec = new Vector4i(getColor((int) (finalIndex * 10)), getColor((int) ((finalIndex + 1) * 10)), getColor((int) (finalIndex * 10)), getColor((int) ((finalIndex + 1) * 10)));

            if (glowing) {

                BloomHelper.registerRenderCall(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translated(r_posX - cWidth, r_posY, 0);
                    GlStateManager.scaled(1, f.animation, 1);
                    GlStateManager.translated(-(r_posX - cWidth), -r_posY, 0);
                    RenderUtil.Render2D.drawRoundedCorner(r_posX + width - cWidth, r_posY, cWidth, height, right_vec, vec);
                    GlStateManager.popMatrix();
                });
            }
            GlStateManager.pushMatrix();
            GlStateManager.translated(r_posX - cWidth, r_posY, 0);
            GlStateManager.scaled(1, f.animation, 1);
            GlStateManager.translated(-(r_posX - cWidth), -r_posY, 0);
            RenderUtil.Render2D.drawRoundedCorner(r_posX + width - cWidth, r_posY, cWidth, height, right_vec, vec);

            RenderUtil.Render2D.drawShadow(r_posX - cWidth, r_posY, width, height, 10, RenderUtil.reAlphaInt(b_color, 64));

            RenderUtil.Render2D.drawRoundedCorner(r_posX - cWidth, r_posY, width, height, left_vec, b_color);

            small.drawString(matrixStack, f.name, r_posX + padding - cWidth - 1, r_posY - dumbOffset + (height / 2), -1);

            GlStateManager.popMatrix();

            index+=f.animation;
            in++;
        }


    }

    private void renderPotions(MatrixStack matrixStack, int offset, boolean glowing) {
        final float height = 12;

        final float posY = window.scaledHeight() - offset - height;

        int index = 0;

        for (EffectInstance eff : mc.player.getActivePotionEffects().stream().sorted(Comparator.comparing(EffectInstance::getDuration)).toList()) {
            final String text = I18n.format(eff.getEffectName()) + " " + I18n.format("enchantment.level." + (eff.getAmplifier() + 1)) + " - " + EffectUtils.getPotionDurationString(eff, 1);
            final float textWidth = small.getWidth(text) + 12;

            final float posX = window.scaledWidth() - offset - textWidth;

            final Vector4i vec = new Vector4i(getColor(index * 30), getColor(index * 30 + 30), getColor(index * 30), getColor(index * 30 + 30));

            if (glowing) {
                int finalIndex = index;
                BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(posX + textWidth - cWidth, posY - (finalIndex * 16), cWidth, height, right_vec, vec));
            }

            RenderUtil.Render2D.drawRoundedCorner(posX + textWidth - cWidth, posY - (index * 16), cWidth, height, right_vec, vec);
            RenderUtil.Render2D.drawShadow(posX, posY - (index * 16), textWidth - cWidth, height, 10, RenderUtil.reAlphaInt(b_color, 64));

            RenderUtil.Render2D.drawRoundedCorner(posX, posY - (index * 16), textWidth - cWidth, height, left_vec, b_color);

            small.drawString(matrixStack, text, posX + 4, posY - (index * 16) + 4.5, t_color);

            index++;
        }
    }

    float kbHeight = 0;
    float kbWidth = 100;

    float kbWidthAnimation = 0, kbHeightAnimation = 0;

    private void renderKeyBinds(MatrixStack matrixStack, int offset, boolean glowing) {
        float posX = keyBinds.getX();
        float posY = keyBinds.getY();

        final Vector4i vec = new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100));

        if (glowing) {
            BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(posX, posY, cWidth, kbHeightAnimation, left_vec, vec));
        }

        RenderUtil.Render2D.drawRoundedCorner(posX, posY, cWidth, kbHeightAnimation, left_vec, vec);
        RenderUtil.Render2D.drawShadow(posX + cWidth, posY, kbWidthAnimation, kbHeightAnimation, 10, RenderUtil.reAlphaInt(b_color, 64));

        RenderUtil.Render2D.drawRoundedCorner(posX + cWidth, posY, kbWidthAnimation, kbHeightAnimation, right_vec, b_color);

        medium.drawString(matrixStack, "Key Binds", posX + cWidth + 4, posY + 6, t_color);

        RenderUtil.Render2D.drawRect(posX + cWidth, posY + 16, kbWidthAnimation, 0.5f, getColor(100));

        RenderUtil.SmartScissor.push();
        RenderUtil.SmartScissor.setFromComponentCoordinates(posX, posY, kbWidthAnimation, kbHeightAnimation);
        int i = 0;
        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (!f.state || f.bind == 0) continue;

            String text = ClientUtil.getKey(f.bind);

            if (text.length() > 4) {
                text = text.substring(0,4) + "..";
            }
            String bindText = "[" + text.toUpperCase() + "]";
            float bindWidth = small.getWidth(bindText);
            String name = f.name;
            if (name.length() > 13) {
                name = name.substring(0,13) + "..";
            }

            kbWidth = Math.max(small.getWidth(bindText + f.bind + 5), 100);

            small.drawString(matrixStack, name, posX + cWidth + 4, posY + 22 + (i * 10), t_color);

            small.drawString(matrixStack, bindText, posX + cWidth + kbWidthAnimation - bindWidth - 4, posY + 22 + (i * 10), t_color);

            i++;
        }
        RenderUtil.SmartScissor.unset();
        RenderUtil.SmartScissor.pop();

        kbHeight = 22 + (i * 10);
        this.kbHeightAnimation = AnimationMath.fast(this.kbHeightAnimation, kbHeight, 10);
        this.kbWidthAnimation = AnimationMath.fast(this.kbWidthAnimation, kbWidth, 10);

        keyBinds.setWidth(kbWidth);
        keyBinds.setHeight(kbHeight);
    }

    float slHeight = 0;
    float lsWidth = 100;

    private void renderStaffList(MatrixStack matrixStack, int offset, boolean glowing) {
        float posX = staffList.getX();
        float posY = staffList.getY();

        final Vector4i vec = new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100));

        if (glowing) {
            BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(posX, posY, cWidth, slHeight, left_vec, vec));
        }

        RenderUtil.Render2D.drawRoundedCorner(posX, posY, cWidth, slHeight, left_vec, vec);
        RenderUtil.Render2D.drawShadow(posX + cWidth, posY, lsWidth, slHeight, 10, RenderUtil.reAlphaInt(b_color, 64));

        RenderUtil.Render2D.drawRoundedCorner(posX + cWidth, posY, lsWidth, slHeight, right_vec, b_color);

        medium.drawString(matrixStack, "Staff List", posX + cWidth + 4, posY + 6, t_color);

        RenderUtil.Render2D.drawRect(posX + cWidth, posY + 16, lsWidth, 0.5f, getColor(100));

        int i = 0;

        for (Map.Entry<ITextComponent, String> entry : staffPlayers.entrySet()) {
            ITextComponent p = entry.getKey();
            String n = entry.getValue();

            float len = Math.max(small.getWidth(p.getString() + n + cWidth), 100);

            if (len > lsWidth) lsWidth = len;

            small.drawString(matrixStack, p, posX + cWidth + 4, posY + 22 + (i * 10), t_color);

            small.drawString(matrixStack, n, posX + small.getWidth(p.getString()) + cWidth + 4, posY + 22 + (i * 10), t_color);

            i++;
        }

        slHeight = 22 + (i * 10);

        staffList.setWidth(lsWidth);
        staffList.setHeight(slHeight);
    }

    float health = 0;

    private double scale = 0.0D;

    private void renderTarget(MatrixStack matrixStack, int offset, boolean glowing) {
        float posX = targetHUD.getX();
        float posY = targetHUD.getY();


        targetHUD.setWidth(100);
        targetHUD.setHeight(38);

        this.target = getTarget(this.target);
        this.scale = tHudAnimation.getOutput();

        if (scale == 0.0F) {
            target = null;
        }

        if (target == null) {
            return;
        }
        final String targetName = this.target.getName().getString();
        String substring = targetName.substring(0, Math.min(targetName.length(), 10));
        this.health = AnimationMath.fast(health, target.getHealth() / target.getMaxHealth(), 5);
        this.health = MathHelper.clamp(this.health, 0, 1);
        String healthValue = (int) MathUtil.round(this.health * 20 + target.getAbsorptionAmount(), 0.5f) + "";

        Vector4f vec = new Vector4f(0, round_degree, this.health > 0.99 ? 0 : round_degree, round_degree);

        GlStateManager.pushMatrix();
        AnimationMath.sizeAnimation(posX + (100 / 2), posY + (38 / 2), scale);
        RenderUtil.Render2D.drawShadow(posX, posY, 100, 38, 10, RenderUtil.reAlphaInt(b_color, 64));



        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
        target.getArmorInventoryList().forEach(stacks::add);
        stacks.removeIf(w -> w.getItem() instanceof AirItem);
        RenderUtil.Render2D.drawRoundedCorner(posX, posY, 100, 38, new Vector4f(round_degree, round_degree, stacks.isEmpty() ? round_degree : 0, round_degree), b_color);
        if (!stacks.isEmpty())
            RenderUtil.Render2D.drawRoundedCorner(posX + 40, posY - 12, 60, 12, new Vector4f(round_degree, 0, round_degree, 0), b_color);

        drawItemStack(posX + 90, posY - 11, -10);
        drawFace(posX + 4, posY + 4, 8F, 8F, 8F, 8F, 24, 24, 64F, 64F, (AbstractClientPlayerEntity) target);

        medium.drawString(matrixStack, substring, posX + 32, posY + 6, t_color);
        float distance = (float) MathUtil.round(mc.player.getDistance(target), 0.1f);
        Fonts.gilroy[12].drawString(matrixStack, "Health: " + healthValue, posX + 32, posY + 16, t_color);
        Fonts.gilroy[12].drawString(matrixStack, "Distance: " + distance, posX + 32, posY + 22, t_color);

        if (glowing) {
            BloomHelper.registerRenderCall(() -> {
                GlStateManager.pushMatrix();
                AnimationMath.sizeAnimation(posX + (100 / 2), posY + (38 / 2), scale);
                RenderUtil.Render2D.drawRoundedCorner(posX, posY + 38 - 6, 100 * health, 6, vec, new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));
                GlStateManager.popMatrix();
            });
        }

        RenderUtil.Render2D.drawRoundedCorner(posX, posY + 38 - 6, 100 * health, 6, vec, new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));
        GlStateManager.popMatrix();

    }

    private void drawItemStack(float x, float y, float offset) {
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
        target.getArmorInventoryList().forEach(stacks::add);
        stacks.removeIf(w -> w.getItem() instanceof AirItem);
        Collections.reverse(stacks);
        final AtomicReference<Float> posX = new AtomicReference<>(x);

        stacks.stream()
                .filter(stack -> !stack.isEmpty())
                .forEach(stack -> HudUtil.drawItemStack(stack,
                        posX.getAndAccumulate(offset, Float::sum),
                        y,
                        true,
                        true, 0.6f));
    }

    private float perc;

    private void renderTimer(MatrixStack matrixStack, int offset, boolean glowing) {
        float posX = timerHUD.getX();
        float posY = timerHUD.getY();

        float quotient = Managment.FUNCTION_MANAGER.timerFunction.maxViolation / Managment.FUNCTION_MANAGER.timerFunction.timerAmount.getValue().floatValue();
        float minimumValue = Math.min(Managment.FUNCTION_MANAGER.timerFunction.getViolation(), quotient);
        perc = AnimationMath.lerp(perc, ((quotient - minimumValue) / quotient), 10);

        String text = (int) (perc * 100) + "%";
        float width = 100;
        timerHUD.setWidth(width);
        timerHUD.setHeight(20);
        if (shadow.get())
            RenderUtil.Render2D.drawShadow(posX, posY, width, 20, 10, RenderUtil.reAlphaInt(b_color, 64));
        RenderUtil.Render2D.drawRoundedCorner(posX, posY, width, 20, round_degree, b_color);

        medium.drawString(matrixStack, "Timer", posX + 3, posY + 5, t_color);

        medium.drawString(matrixStack, text, posX + width - medium.getWidth(text) - 3, posY + 5, t_color);
        if (glowing)
            BloomHelper.registerRenderCall(() -> {
                RenderUtil.Render2D.drawRoundedCorner(posX, posY + 20 - 6, width * perc, 6, new Vector4f(0, round_degree, perc == 1 ? 0 : round_degree, round_degree), new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));

            });
        RenderUtil.Render2D.drawRoundedCorner(posX, posY + 20 - 6, width * perc, 6, new Vector4f(0, round_degree, perc == 1 ? 0 : round_degree, round_degree), new Vector4i(getColor(100), getColor(100), getColor(0), getColor(0)));
    }

    private void renderCoordinates(MatrixStack matrixStack, int offset, boolean glowing) {

        String[] texts = new String[]{
                "Coords: " + (int) mc.player.getPosX() + ", " + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ(),
                "BPS: " + (String.format("%.2f", Math.hypot(mc.player.getPosX() - mc.player.prevPosX, mc.player.getPosZ() - mc.player.prevPosZ) * 20))
        };
        float of = 0;
        for (String text : texts) {
            float textWidth = medium.getWidth(text) + 16;

            float height = 16;

            float posY = window.scaledHeight() - height - offset - of;
            if (glowing)
            BloomHelper.registerRenderCall(() -> {
                RenderUtil.Render2D.drawRoundedCorner(offset, posY, cWidth, height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
            });
            if (shadow.get())
            RenderUtil.Render2D.drawShadow(offset + cWidth, posY, textWidth, height, 10, RenderUtil.reAlphaInt(b_color, 64));

            RenderUtil.Render2D.drawRoundedCorner(offset, posY, cWidth, height, left_vec, new Vector4i(getColor(0), getColor(100), getColor(0), getColor(100)));
            RenderUtil.Render2D.drawRoundedCorner(offset + cWidth, posY, textWidth, height, right_vec, b_color);

            medium.drawCenteredString(matrixStack, text, offset + cWidth + (textWidth / 2), posY + 5.5, t_color);
            of += height + 3;
        }
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

    private void updateFunctions() {
        for (Function function : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (function.category == Type.Render) continue;

            functions.add(function);
        }

        functions.sort((f1, f2) -> Float.compare(small.getWidth(f2.name), small.getWidth(f1.name)));
    }

    private int getColor(int index) {
        return colors[index % colors.length];
    }
}
