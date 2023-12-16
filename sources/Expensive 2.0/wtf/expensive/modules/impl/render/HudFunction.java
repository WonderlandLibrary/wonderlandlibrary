package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import org.joml.Vector2i;
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
import wtf.expensive.ui.midnight.StyleManager;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.UserProfile;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.EaseBackIn;
import wtf.expensive.util.drag.Dragging;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.font.styled.StyledFont;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.HudUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.SmartScissor;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static wtf.expensive.modules.impl.render.HudFunction.Status.*;
import static wtf.expensive.util.render.ColorUtil.rgba;
import static wtf.expensive.util.render.RenderUtil.Render2D.*;

/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "Hud", type = Type.Render)
public class HudFunction extends Function {

    public MultiBoxSetting elements = new MultiBoxSetting("Элементы",
            new BooleanOption("Логотип", true),
            new BooleanOption("Список модулей", true),
            new BooleanOption("Список модераторов", true),
            new BooleanOption("Список зелий", true),
            new BooleanOption("Уведомления", true),
            new BooleanOption("Таймер индикатор", true),
            new BooleanOption("Расписание", false),
            new BooleanOption("Таргет Худ", true),
            new BooleanOption("Кейбинды", true),
            new BooleanOption("Корды", true),
            new BooleanOption("Броня", true));
    public MultiBoxSetting watermarkElement = new MultiBoxSetting("Элементы логотипа",
            new BooleanOption("Логин в чите", true),
            new BooleanOption("Счетчик кадров", true),
            new BooleanOption("Счетчик пинга", true)).setVisible(() -> elements.get(0));

    public MultiBoxSetting limitations = new MultiBoxSetting("Ограничения",
            new BooleanOption("Скрывать визуалы", true),
            new BooleanOption("Только бинды", false)).setVisible(() -> elements.get(1));

    public BooleanOption glow = new BooleanOption("Подсветка списка модулей", true).setVisible(() -> elements.get(1));

    public SliderSetting fontSize = new SliderSetting("Размер шрифта", 14, 12, 18, 1);

    public HudFunction() {
        addSettings(elements, watermarkElement, limitations, glow, fontSize);
    }

    public final float offs = 0;

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate && elements.get(2)) {
            staffPlayers.clear();

            for (ScorePlayerTeam team : mc.world.getScoreboard().getTeams().stream().sorted(Comparator.comparing(Team::getName)).toList()) {

                String name = team.getMembershipCollection().toString();
                name = name.substring(1, name.length() - 1);

                if (namePattern.matcher(name).matches()) {
                    if (prefixMatches.matcher(repairString(team.getPrefix().getString().toLowerCase(Locale.ROOT))).matches() || Managment.STAFF_MANAGER.isStaff(name)) {
                        staffPlayers.add(new StaffPlayer(name, team.getPrefix()));
                    }
                }
            }
        }
        if (event instanceof EventRender eventRender) {
            if (eventRender.isRender2D()) {
                handleRender(eventRender);
            }
        }
    }

    /**
     * Обработчик события типа EventRender
     *
     * @param renderEvent Событие для рендера
     */

//    Renderable waterMark = RenderableFactory.create(RenderableType.WATERMARK, 3, 5);
//    Renderable info = RenderableFactory.create(RenderableType.INFO, 4, 0);

    private void handleRender(EventRender renderEvent) {
        final MatrixStack stack = renderEvent.matrixStack;



        if (elements.get(0)) {
            //waterMark.render(stack);
        }
        if (elements.get(1)) {
            renderArrayList(stack);
        }
        if (elements.get(2)) {
            onStaffListRender(stack, renderEvent);
        }
        if (elements.get(3)) {
            onPotionElementsRender(stack, renderEvent);
        }
        if (elements.get(7)) {
            onRenderTargetHUD(stack);
        }
        if (elements.get(8)) {
            onKeyBindsRender(stack);
        }
        if (elements.get(9)) {
            //info.render(stack);
        }
        if (elements.get(10)) {
            onArmorRender(renderEvent);
        }
    }

    public Dragging keyBinds = Initilization.createDrag(this, "KeyBinds", 200, 50);
    private float heightDynamic = 0;
    private int activeModules = 0;

    private void onKeyBindsRender(MatrixStack stack) {
        float posX = keyBinds.getX();
        float posY = keyBinds.getY();

        int roundDegree = 4;
        int headerHeight = 14;
        int width = 100;
        int padding = 5;
        int offset = 10;

        int headerColor = new Color(30, 30, 30, 200).getRGB();
        int backgroundColor = new Color(40, 40, 40, 230).getRGB();

        int firstColor = ColorUtil.getColorStyle(0);
        int secondColor = ColorUtil.getColorStyle(90);

        float height = activeModules * offset;

        this.heightDynamic = AnimationMath.fast(this.heightDynamic, height, 10);

        RenderUtil.Render2D.drawShadow(posX, posY, width, headerHeight + heightDynamic + padding / 2f, 10, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawGradientRound(posX, posY, width, heightDynamic + headerHeight + 2.5f, roundDegree - 1, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawRoundedCorner(keyBinds.getX(), posY, width, headerHeight, new Vector4f(roundDegree, 0, roundDegree, 0), headerColor);
        Fonts.gilroyBold[14].drawCenteredString(stack, ClientUtil.gradient("key binds", firstColor, secondColor), keyBinds.getX() + width / 2f, posY + 5.5f, firstColor);

        RenderUtil.Render2D.drawRoundedCorner(posX, posY + headerHeight, width, heightDynamic + padding / 2f, new Vector4f(0, roundDegree, 0, roundDegree), backgroundColor);
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, width, headerHeight + heightDynamic + padding / 2f);
        int index = 0;

        for (Function f : Managment.FUNCTION_MANAGER.getFunctions()) {
            if (f.bind != 0 && f.state) {

                String text = ClientUtil.getKey(f.bind);

                if (text == null) {
                    continue;
                }


                String bindText = "[" + text.toUpperCase() + "]";
                float bindWidth = Fonts.gilroyBold[12].getWidth(bindText);

                Fonts.gilroyBold[12].drawString(stack, f.name, posX + padding, posY + headerHeight + padding + (index * offset), -1);
                Fonts.gilroyBold[12].drawString(stack, bindText, posX + width - bindWidth - padding, posY + headerHeight + padding + (index * offset), -1);

                index++;
            }
        }
        SmartScissor.unset();
        SmartScissor.pop();
        activeModules = index;

        keyBinds.setWidth(width);
        keyBinds.setHeight(activeModules * offset + headerHeight);
    }

    public CopyOnWriteArrayList<net.minecraft.util.text.TextComponent> components = new CopyOnWriteArrayList<>();

    private final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
    private final Pattern prefixMatches = Pattern.compile(".*(mod|der|adm|help|wne|мод|хелп|помо|адм|владе|отри|таф|taf|curat|курато|dev|раз|supp|сапп|yt|ютуб).*");

    public Dragging staffList = Initilization.createDrag(this, "StaffList", 350, 50);

    private int activeStaff = 0;
    private float hDynam = 0;
    private float widthDynamic = 0;
    private float nameWidth = 0;
    List<StaffPlayer> staffPlayers = new ArrayList<>();

    private void onStaffListRender(MatrixStack matrixStack, EventRender render) {
        float posX = staffList.getX();
        float posY = staffList.getY();

        int roundDegree = 4;
        int headerHeight = 14;
        float width = Math.max(nameWidth + 40, 100);
        int padding = 5;
        int offset = 10;

        int headerColor = new Color(30, 30, 30, 200).getRGB();
        int backgroundColor = new Color(35, 35, 35, 230).getRGB();

        int firstColor = ColorUtil.getColorStyle(0);
        int secondColor = ColorUtil.getColorStyle(90);
        float height = activeStaff * offset;

        this.hDynam = AnimationMath.fast(this.hDynam, height, 10);
        this.widthDynamic = AnimationMath.fast(this.widthDynamic, width, 10);
        RenderUtil.Render2D.drawShadow(posX, posY, widthDynamic, headerHeight + hDynam + padding / 2f, 10, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawGradientRound(posX, posY, widthDynamic, hDynam + headerHeight + 2.5f, roundDegree - 1, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawRoundedCorner(posX, posY, widthDynamic, headerHeight, new Vector4f(roundDegree, 0, roundDegree, 0), headerColor);
        Fonts.gilroyBold[14].drawCenteredString(matrixStack, ClientUtil.gradient("staff list", firstColor, secondColor), posX + widthDynamic / 2f, posY + 5.5f, -1);

        RenderUtil.Render2D.drawRoundedCorner(posX, posY + headerHeight, widthDynamic, hDynam + padding / 2f, new Vector4f(0, roundDegree, 0, roundDegree), backgroundColor);

        int index = 0;

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, widthDynamic, headerHeight + hDynam + padding / 2f);
        if (!staffPlayers.isEmpty()) {
            for (StaffPlayer staff : staffPlayers) {
                String name = staff.getName();
                ITextComponent prefix = staff.getPrefix();
                String status = staff.getStatus().getString();

                Fonts.gilroyBold[12].drawString(matrixStack, prefix, posX + padding, posY + headerHeight + padding + (index * offset), -1);
                Fonts.gilroyBold[12].drawString(matrixStack, name + status, posX + padding + Fonts.gilroyBold[12].getWidth(prefix.getString()), posY + headerHeight + padding + (index * offset), -1);
                nameWidth = Fonts.gilroyBold[12].getWidth(prefix.getString() + name + status);
                index++;
            }
        } else {
            nameWidth = 0;
        }
        SmartScissor.unset();
        SmartScissor.pop();

        activeStaff = index;
        staffList.setWidth(widthDynamic);
        staffList.setHeight(hDynam + headerHeight);
    }

    private String repairString(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= 65281 && c <= 65374) {
                sb.append((char) (c - 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private class StaffPlayer {

        @Getter
        String name;
        @Getter
        ITextComponent prefix;
        @Getter
        Status status;

        private StaffPlayer(String name, ITextComponent prefix) {
            this.name = name;
            this.prefix = prefix;

            updateStatus();
        }

        private void updateStatus() {
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player.getNameClear().equals(name)) {
                    status = NEAR;
                    return;
                }
            }

            for (NetworkPlayerInfo info : mc.getConnection().getPlayerInfoMap()) {
                if (info.getGameProfile().getName().equals(name)) {
                    if (info.getGameType() == GameType.SPECTATOR) {
                        status = SPEC;
                        return;
                    }

                    status = NONE;
                    return;
                }
            }

            status = VANISHED;
        }
    }

    public enum Status {
        NONE(""),
        NEAR(" §e[NEAR]"),
        SPEC(" §c[SPEC]"),
        VANISHED(" §6[VANISHED]");

        @Getter
        final String string;

        Status(String string) {
            this.string = string;
        }
    }

    public Dragging events = Initilization.createDrag(this, "events", 213.0f, 8.0f + 100);


    /**
     * Выполняет рендер информации по типу координат на экране
     *
     * @param stack       Матрица для рендеринга.
     * @param renderEvent Обработчик типа EventRender
     */
    private void onInformationRender(final MatrixStack stack, final EventRender renderEvent) {
        float y = renderEvent.scaledResolution.scaledHeight() - Fonts.gilroyBold[15].getFontHeight() - (mc.currentScreen instanceof ChatScreen ? 8 * mc.gameSettings.guiScale : 0);
        String pos = (int) mc.player.getPosX() + ", " + (int) mc.player.getPosY() + ", " + (int) mc.player.getPosZ();

        Fonts.gilroyBold[15].drawString(stack, ClientUtil.gradient("Coords: ", ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(150)), 4, y, -1);
        Fonts.gilroyBold[15].drawString(stack, pos, 4 + Fonts.gilroyBold[15].getWidth("Coords: "), y, new Color(230, 230, 230).getRGB());
    }

    /**
     * Выполняет рендер брони на экране
     *
     * @param renderEvent Обработчик типа EventRender
     */
    private void onArmorRender(final EventRender renderEvent) {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        float xPos = renderEvent.scaledResolution.scaledWidth() / 2f;
        float yPos = renderEvent.scaledResolution.scaledHeight();

        boolean totemInInv = mc.player.inventory.mainInventory.stream().map(ItemStack::getItem).toList().contains(Items.TOTEM_OF_UNDYING);
        int off = totemInInv ? +5 : 0;
        if (mc.player.isCreative()) {
            yPos += 17;
        }

        for (ItemStack s : mc.player.inventory.armorInventory) {
            ESPFunction.drawItemStack(s, xPos - off + 74 * (mc.gameSettings.guiScale / 2f), yPos - 56 * (mc.gameSettings.guiScale / 2f), null, false);
            off += 15;
        }
        if (totemInInv)
            ESPFunction.drawItemStack(new ItemStack(Items.TOTEM_OF_UNDYING), xPos - off + 73 * (mc.gameSettings.guiScale / 2f), yPos - 56 * (mc.gameSettings.guiScale / 2f), String.valueOf(count), false);


    }

    /**
     * Выполняет рендер эффектов на экране
     *
     * @param stack       Матрица для рендеринга.
     * @param renderEvent Обработчик типа EventRender
     */

    public Dragging potionStatus = Initilization.createDrag(this, "PotionStatus", 200, 50);
    private float hDynamic = 0;
    private int activePotions = 0;

    private void onPotionElementsRender(final MatrixStack matrixStack, final EventRender renderEvent) {
        float posX = potionStatus.getX();
        float posY = potionStatus.getY();

        int roundDegree = 4;
        int headerHeight = 14;
        int width = 100;
        int padding = 5;
        int offset = 10;

        int headerColor = new Color(30, 30, 30, 200).getRGB();
        int backgroundColor = new Color(40, 40, 40, 230).getRGB();

        int firstColor = ColorUtil.getColorStyle(0);
        int secondColor = ColorUtil.getColorStyle(90);

        float height = activePotions * offset;

        this.hDynamic = AnimationMath.fast(this.hDynamic, height, 10);

        RenderUtil.Render2D.drawShadow(posX, posY, width, headerHeight + hDynamic + padding / 2f, 10, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawGradientRound(posX, posY, width, hDynamic + headerHeight + 2.5f, roundDegree - 1, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawRoundedCorner(potionStatus.getX(), posY, width, headerHeight, new Vector4f(roundDegree, 0, roundDegree, 0), headerColor);
        Fonts.gilroyBold[14].drawCenteredString(matrixStack, ClientUtil.gradient("potions", firstColor, secondColor), potionStatus.getX() + width / 2f, posY + 5.5f, firstColor);

        RenderUtil.Render2D.drawRoundedCorner(posX, posY + headerHeight, width, hDynamic + padding / 2f, new Vector4f(0, roundDegree, 0, roundDegree), backgroundColor);
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(posX, posY, width, headerHeight + hDynamic + padding / 2f);
        int index = 0;

        for (EffectInstance p : mc.player.getActivePotionEffects()) {
            if (p.isShowIcon()) {

                String durationText = EffectUtils.getPotionDurationString(p, 1);
                float durationWidth = Fonts.gilroyBold[12].getWidth(durationText);

                Fonts.gilroyBold[12].drawString(matrixStack, I18n.format(p.getEffectName()), posX + padding, posY + headerHeight + padding + (index * offset), -1);
                Fonts.gilroyBold[12].drawString(matrixStack, durationText, posX + width - durationWidth - padding, posY + headerHeight + padding + (index * offset), -1);

                index++;
            }
        }
        SmartScissor.unset();
        SmartScissor.pop();
        activePotions = index;

        potionStatus.setWidth(width);
        potionStatus.setHeight(activePotions * offset + headerHeight);
    }

//    private void onNursultanTitle(final MatrixStack stack) {
//        final Vector4i colorVec = new Vector4i(
//                ColorUtil.getColorStyle(0),
//                ColorUtil.getColorStyle(90),
//                ColorUtil.getColorStyle(180),
//                ColorUtil.getColorStyle(270)
//        );
//
//        final String title = " dedinside " + mc.debugFPS + " fps " + HudUtil.calculatePing() + " ping";
//
//        final TextComponent gradientText = ClientUtil.gradient("nursultan", colorVec.x, colorVec.y);
//
//        // Координаты ватермарки
//        final float x = 5;
//        final float y = 5;
//        final float titleWidth = Fonts.sfBold[15].getWidth(gradientText.getString() + title) + 6;
//        final float titleHeight = 12;
//
//        drawShadow(x, y, titleWidth, titleHeight, 5, colorVec.x, colorVec.y, colorVec.w, colorVec.z);
//        drawRoundedRect(x, y, titleWidth, titleHeight, 3, rgba(20, 20, 20, 255));
//
//        Fonts.sfBold[15].drawString(stack, gradientText, x + 3, y + Fonts.sfBold[15].getFontHeight() / 2f - 0.5f, -1);
//        Fonts.sfBold[14].drawString(stack, title, x + Fonts.sfBold[15].getWidth(gradientText.getString()) + 3.5f, y + Fonts.sfBold[14].getFontHeight() / 2f + 0.3f, rgba(200, 200, 200, 255));
//    }

    /**
     * Выполняет рендер логотипа чита
     *
     * @param stack Матрица для рендеринга.
     */
    private void onTitleRender(final MatrixStack stack) {
        UserProfile profile = Managment.USER_PROFILE;

        StringBuilder titleText = new StringBuilder();
        int counter = 0; // Переменная для подсчета элементов


        titleText.append("expensive");
        counter++;

        if (watermarkElement.get(0)) {
            if (counter > 0) {
                titleText.append(TextFormatting.GRAY + " | " + TextFormatting.WHITE);
            }
            titleText.append(profile.getName());
            counter++;
        }
        if (watermarkElement.get(1)) {
            if (counter > 0) {
                titleText.append(TextFormatting.GRAY + " | " + TextFormatting.WHITE);
            }
            titleText.append(mc.debugFPS + "fps");
            counter++;
        }
        if (watermarkElement.get(2)) {
            if (counter > 0) {
                titleText.append(TextFormatting.GRAY + " | " + TextFormatting.WHITE);
            }
            titleText.append(HudUtil.calculatePing() + "ms");
        }


        // Координаты ватермки
        final float x = 5, y = 5, titleWidth = Fonts.gilroyBold[12].getWidth(titleText.toString()) + 6, titleHeight = 12;


        int firstColor = ColorUtil.getColorStyle(0);
        int secondColor = ColorUtil.getColorStyle(90);


        RenderUtil.Render2D.drawShadow(x, y, titleWidth, titleHeight, 10, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawGradientRound(x, y, titleWidth, titleHeight, 3, firstColor, secondColor, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));


        RenderUtil.Render2D.drawRoundedRect(x, y, titleWidth, titleHeight, 2.5f, rgba(21, 21, 21, 200));

        Fonts.gilroyBold[12].drawString(stack, titleText.toString(), x + 3, y + Fonts.gilroyBold[12].getFontHeight() / 2f + 1, -1);
    }

    List<Function> sortedFunctions = new ArrayList<>();
    TimerUtil delay = new TimerUtil();

    /**
     * Выполняет рендер списка из модулей
     *
     * @param stack Матрица для рендеринга.
     */
    private void renderArrayList(MatrixStack stack) {
        float x = 5;
        float y = 25;
        float height = 10;
        float yOffset = 0;

        final StyledFont font = Fonts.gilroyBold[fontSize.getValue().intValue()];
        StyleManager styleManager = Managment.STYLE_MANAGER;

        if (delay.hasTimeElapsed(10000)) {
            sortedFunctions = HudUtil.getSorted(font);
            delay.reset();
        }

        float gradientForce = 1;
        int fontOffset = (fontSize.getValue().intValue() > 14 ? -14 + fontSize.getValue().intValue()
                - (fontSize.getValue().intValue() > 14 ? 1 : 0) : 0);
        int firstColor;
        int secondColor;

        if (glow.get()) {
            for (Function function : sortedFunctions) {
                if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                    continue;
                }

                function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);

                if (function.animation >= 0.01) {
                    float width = font.getWidth(function.name) + 5;
                    firstColor = styleManager.getCurrentStyle()
                            .getColor((int) ((yOffset + height * function.animation) * gradientForce));
                    secondColor = styleManager.getCurrentStyle()
                            .getColor((int) (yOffset * gradientForce));

                    RenderSystem.pushMatrix();
                    RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                    RenderSystem.scalef(1, function.animation, 1);
                    RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                    drawShadow(x, y + yOffset, width, height, 10, firstColor, secondColor);
                    RenderSystem.popMatrix();

                    yOffset += height * function.animation;
                }
            }
            yOffset = 0;
        }
        for (Function function : sortedFunctions) {
            if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                continue;
            }

            function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);

            if (function.animation >= 0.01) {
                float width = font.getWidth(function.name) + 5;
                secondColor = styleManager.getCurrentStyle()
                        .getColor((int) (yOffset * gradientForce));

                RenderSystem.pushMatrix();
                RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                RenderSystem.scalef(1, function.animation, 1);
                RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                RenderUtil.Render2D.drawRect(x, y + yOffset, width, height, rgba(21, 21, 21, 200));
                font.drawString(stack, function.name, x + 3,
                        y + yOffset + font.getFontHeight() / 2f - fontOffset - 1, secondColor);
                RenderSystem.popMatrix();

                yOffset += height * function.animation;
            }
        }


        yOffset = 0;
        for (Function function : sortedFunctions) {
            if ((limitations.get(0) && function.category == Type.Render) || (limitations.get(1) && function.bind == 0)) {
                continue;
            }
            function.animation = AnimationMath.lerp(function.animation, function.state ? 1 : 0, 15);
            if (function.animation >= 0.01) {
                float width = font.getWidth(function.name) + 4;
                firstColor = styleManager.getCurrentStyle()
                        .getColor((int) ((yOffset + height * function.animation) * gradientForce));
                secondColor = styleManager.getCurrentStyle()
                        .getColor((int) (yOffset * gradientForce));

                RenderSystem.pushMatrix();
                RenderSystem.translatef(x + width / 2F, y + yOffset, 0);
                RenderSystem.scalef(1, function.animation, 1);
                RenderSystem.translatef(-(x + width / 2F), -(y + yOffset), 0);
                RenderUtil.Render2D.drawShadow(x, y + yOffset, 1, height, 8, firstColor, secondColor);
                RenderUtil.Render2D.drawVertical(x, y + yOffset, 1, height, firstColor, secondColor);
                RenderSystem.popMatrix();

                yOffset += height * function.animation;
            }
        }
    }


    float health = 0;
    public final Dragging targetHUD = Initilization.createDrag(this, "TargetHUD", 500, 50);
    private final Animation targetHudAnimation = new EaseBackIn(200, 1, 1.5f);
    private PlayerEntity target = null;
    private double scale = 0.0D;

    private void onRenderTargetHUD(final MatrixStack stack) {
        this.target = getTarget(this.target);
        this.targetHudAnimation.setDuration(300);
        this.scale = targetHudAnimation.getOutput();

        if (scale == 0.0F) {
            target = null;
        }

        if (target == null) {
            return;
        }

        final String targetName = this.target.getName().getString();

        String substring = targetName.substring(0, Math.min(targetName.length(), 10));
        final float nameWidth = Fonts.gilroyBold[18].getWidth(substring);
        final float xPosition = this.targetHUD.getX();
        final float yPosition = this.targetHUD.getY();
        final float maxWidth = Math.max(nameWidth + 50, 120);
        final float maxHeight = 30;

        // Обновление значения здоровья с анимацией
        this.health = AnimationMath.fast(health, target.getHealth() / target.getMaxHealth(), 5);
        this.health = MathHelper.clamp(this.health, 0, 1);

        GlStateManager.pushMatrix();
        AnimationMath.sizeAnimation(xPosition + (maxWidth / 2), yPosition + (maxHeight / 2), scale);

        Vector2i colorVec = new Vector2i(ColorUtil.getColorStyle(0), ColorUtil.getColorStyle(90));

        RenderUtil.Render2D.drawShadow(xPosition, yPosition, maxWidth, maxHeight, 10, colorVec.x, colorVec.y, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawGradientRound(xPosition, yPosition, maxWidth, maxHeight, 3, colorVec.x, colorVec.y, ColorUtil.getColorStyle(180), ColorUtil.getColorStyle(270));

        RenderUtil.Render2D.drawRoundedRect(xPosition, yPosition, maxWidth, maxHeight, 2.5f, rgba(21, 21, 21, 200));

        // Отрисовка головы
        drawFace(xPosition + 4, yPosition + 4, 8F, 8F, 8F, 8F, 22, 22, 64F, 64F, (AbstractClientPlayerEntity) target);
        // Отрисовка кругового индикатора здоровья
        drawCircle(
                xPosition + maxWidth - 15,
                yPosition + maxHeight / 2,
                0,
                360,
                10,
                4,
                false,
                ColorUtil.rgba(26, 26, 26, 255)
        );

        drawCircle(
                xPosition + maxWidth - 15,
                yPosition + maxHeight / 2,
                0,
                health * 360 + 1,
                10,
                4,
                false,
                Managment.STYLE_MANAGER.getCurrentStyle()
        );

        drawItemStack(xPosition + 32, yPosition + 22 - 5.5f, 10);

        Fonts.gilroyBold[18].drawString(stack, substring, xPosition + 30, yPosition + 6, -1);

        String healthValue = (int) MathUtil.round(this.health * 20 + target.getAbsorptionAmount(), 0.5f) + "";
        Fonts.gilroyBold[16].drawCenteredString(stack, healthValue, xPosition + maxWidth - 15, yPosition + maxHeight / 2 - 2.2f, ColorUtil.rgba(255, 255, 255, 255));
        GlStateManager.popMatrix();
        this.targetHUD.setWidth(maxWidth);
        this.targetHUD.setHeight(maxHeight);
    }

    private void drawItemStack(float x, float y, float offset) {
        List<ItemStack> stackList = new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
        stackList.addAll((Collection<? extends ItemStack>) target.getArmorInventoryList());

        final AtomicReference<Float> posX = new AtomicReference<>(x);

        stackList.stream()
                .filter(stack -> !stack.isEmpty())
                .forEach(stack -> HudUtil.drawItemStack(stack,
                        posX.getAndAccumulate(offset, Float::sum),
                        y,
                        true,
                        true, 0.6f));
    }


    private PlayerEntity getTarget(PlayerEntity nullTarget) {
        PlayerEntity target = nullTarget;

        if (Managment.FUNCTION_MANAGER.auraFunction.getTarget() instanceof PlayerEntity) {
            target = (PlayerEntity) Managment.FUNCTION_MANAGER.auraFunction.getTarget();
            targetHudAnimation.setDirection(Direction.FORWARDS);
        } else if (mc.currentScreen instanceof ChatScreen) {
            target = mc.player;
            targetHudAnimation.setDirection(Direction.FORWARDS);
        } else {
            targetHudAnimation.setDirection(Direction.BACKWARDS);
        }

        return target;
    }
}
