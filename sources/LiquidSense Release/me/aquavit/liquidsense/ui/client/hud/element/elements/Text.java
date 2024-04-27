package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.misc.ServerUtils;
import me.aquavit.liquidsense.utils.module.CPSCounter;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.shader.shaders.RainbowFontShader;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

@ElementInfo(name = "Text")
public class  Text extends Element {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddyy");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DecimalFormat Y_FORMAT = new DecimalFormat("0.00");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static Text defaultClient() {
        Text text = new Text(26.0, 26.0, 0.5F, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));

        text.rectMode.set("Skeet");
        text.fontValue.set(Fonts.minecraftFont);
        text.setColor(new Color(50, 175, 255));

        return text;
    }

    public Text(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
    }

    public Text() {
        super(10.0, 10.0, 1F, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }

    private FontValue fontValue = new FontValue("Font", Fonts.minecraftFont);
    private TextValue displayString = new TextValue("DisplayText", "");

    private IntegerValue redValue = new IntegerValue("Red", 255, 0, 255);
    private IntegerValue greenValue = new IntegerValue("Green", 255, 0, 255);
    private IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);

    private BoolValue rainbow = new BoolValue("Rainbow", false);
    private FloatValue rainbowX = new FloatValue("Rainbow-X", -1000F, -2000F, 2000F);
    private FloatValue rainbowY = new FloatValue("Rainbow-Y", -1000F, -2000F, 2000F);

    private BoolValue animation = new BoolValue("Animation", false);
    private FloatValue animationDelay = new FloatValue("AnimationDelay", 1F, 0.1F, 5F);

    private BoolValue shadow = new BoolValue("Shadow", false);
    private BoolValue outline = new BoolValue("Outline",false);
    private ListValue rectMode = new ListValue("RectMode", new String[]{"Custom", "OneTap", "Skeet","OnlyWhite","NeverLose"}, "Custom");

    private Value<Boolean> fps = new BoolValue("FPS",true).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));
    private Value<Boolean> ping = new BoolValue("Ping",false).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));
    private Value<Boolean> ip = new BoolValue("IP",false).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));
    private Value<Boolean> tps = new BoolValue("TPS",false).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));
    private Value<Boolean> username = new BoolValue("Username",false).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));
    private Value<Boolean> time = new BoolValue("Time",true).displayable(() ->
            rectMode.get().equals("Skeet") || rectMode.get().equals("NeverLose"));

    private boolean editMode = false;
    private int editTicks = 0;
    private long prevClick = 0L;
    private int lastTick = -1;
    private double lastTPS = 20.0;

    private double displaySpeed = 0.0;
    private String displayText = getDisplay();

    private String getDisplay() {
        String textContent;

        switch (rectMode.get()) {
            case "Skeet":
                StringBuilder skeetBuilder = new StringBuilder("L%f%iquidSense");
                if (fps.get()) {
                    skeetBuilder.append(" %7%|| [%f%%fps%FPS%7%]");
                }
                if (ping.get()) {
                    skeetBuilder.append(" %7%|| [%f%%ping%ms%7%]");
                }
                if (ip.get()) {
                    skeetBuilder.append(" %7%|| %serverip%");
                }
                if (tps.get()) {
                    skeetBuilder.append(" %7%|| %f%%tps%Ticks");
                }
                if (username.get()) {
                    skeetBuilder.append(" %7%|| %r%%username%");
                }
                if (time.get()) {
                    skeetBuilder.append(" %7%|| (%f%%time%%7%)");
                }
                textContent = skeetBuilder.toString();
                break;

            case "NeverLose":
                StringBuilder neverLoseBuilder = new StringBuilder();
                if (fps.get()) {
                    neverLoseBuilder.append("%7%[%f%%fps%FPS%7%]");
                }
                if (ping.get()) {
                    neverLoseBuilder.append(" %7%[%f%%ping%ms%7%]");
                }
                if (ip.get()) {
                    neverLoseBuilder.append(" %7%%serverip%");
                }
                if (tps.get()) {
                    neverLoseBuilder.append(" %7%%f%%tps%Ticks");
                }
                if (username.get()) {
                    neverLoseBuilder.append(" %r%%username%");
                }
                if (time.get()) {
                    neverLoseBuilder.append(" %7%(%f%%time%%7%)");
                }
                textContent = neverLoseBuilder.toString();
                break;

            default:
                if (displayString.get().isEmpty() && !editMode) {
                    textContent = "Text Element";
                } else {
                    textContent = displayString.get();
                }
                break;
        }

        return multiReplace(textContent);
    }
    private String getReplacement(String str) {
        EntityPlayerSP thePlayer = mc.thePlayer;

        if (thePlayer == null) {
            return null;
        }

        switch (str) {
            case "x":
                return DECIMAL_FORMAT.format(thePlayer.posX);
            case "y":
                return Y_FORMAT.format(thePlayer.posY);
            case "z":
                return DECIMAL_FORMAT.format(thePlayer.posZ);
            case "xdp":
                return Double.toString(thePlayer.posX);
            case "ydp":
                return Double.toString(thePlayer.posY);
            case "zdp":
                return Double.toString(thePlayer.posZ);
            case "velocity":
                return DECIMAL_FORMAT.format(Math.sqrt(thePlayer.motionX * thePlayer.motionX + thePlayer.motionZ * thePlayer.motionZ));
            case "ping":
                return String.valueOf(EntityUtils.getPing(thePlayer));
            case "speed":
                return String.format("%.2f", displaySpeed);
            case "0":
                return EnumChatFormatting.BLACK.toString();
            case "1":
                return EnumChatFormatting.DARK_BLUE.toString();
            case "2":
                return EnumChatFormatting.DARK_GREEN.toString();
            case "3":
                return EnumChatFormatting.DARK_AQUA.toString();
            case "4":
                return EnumChatFormatting.DARK_RED.toString();
            case "5":
                return EnumChatFormatting.DARK_PURPLE.toString();
            case "6":
                return EnumChatFormatting.GOLD.toString();
            case "7":
                return EnumChatFormatting.GRAY.toString();
            case "8":
                return EnumChatFormatting.DARK_GRAY.toString();
            case "9":
                return EnumChatFormatting.BLUE.toString();
            case "a":
                return EnumChatFormatting.GREEN.toString();
            case "b":
                return EnumChatFormatting.AQUA.toString();
            case "c":
                return EnumChatFormatting.RED.toString();
            case "d":
                return EnumChatFormatting.LIGHT_PURPLE.toString();
            case "e":
                return EnumChatFormatting.YELLOW.toString();
            case "f":
                return EnumChatFormatting.WHITE.toString();
            case "n":
                return EnumChatFormatting.UNDERLINE.toString();
            case "m":
                return EnumChatFormatting.STRIKETHROUGH.toString();
            case "l":
                return EnumChatFormatting.BOLD.toString();
            case "k":
                return EnumChatFormatting.OBFUSCATED.toString();
            case "o":
                return EnumChatFormatting.ITALIC.toString();
            case "r":
                return EnumChatFormatting.RESET.toString();
            case "username":
                return mc.getSession().getUsername();
            case "clientname":
                return LiquidSense.CLIENT_NAME;
            case "clientversion":
                return "b" + LiquidSense.CLIENT_VERSION;
            case "clientcreator":
                return LiquidSense.CLIENT_CREATOR;
            case "fps":
                return String.valueOf(Minecraft.getDebugFPS());
            case "date":
                return DATE_FORMAT.format(System.currentTimeMillis());
            case "time":
                return HOUR_FORMAT.format(System.currentTimeMillis());
            case "tps":
                return new DecimalFormat("0.0").format(Math.round(lastTPS * 10.0) / 10.0);
            case "serverip":
                return ServerUtils.getRemoteIp();
            case "cps":
            case "lcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
            case "mcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
            case "rcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
            default:
                return null; // Null = don't replace
        }
    }

    @EventTarget
    public void onPacket(PacketEvent modPacket) {
        ArrayList<Long> times = new ArrayList<>();
        TimeUtils tpsTimer = new TimeUtils();

        if (modPacket.getPacket() instanceof S03PacketTimeUpdate) {
            times.add(Math.max(1000, tpsTimer.getCurrentMS()));
            long timesAdded = 0;
            if (times.size() > 5) {
                times.remove(0);
            }
            for (Long l : times) {
                timesAdded += l;
            }
            long roundedTps = timesAdded / times.size();
            lastTPS = 20.0 / roundedTps * 1000.0;
            tpsTimer.reset();
        }
    }

    private String multiReplace(String str) {
        int lastPercent = -1;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '%') {
                if (lastPercent != -1) {
                    if (lastPercent + 1 != i) {
                        String replacement = getReplacement(str.substring(lastPercent + 1, i));

                        if (replacement != null) {
                            result.append(replacement);
                            lastPercent = -1;
                            continue;
                        }
                    }
                    result.append(str, lastPercent, i);
                }
                lastPercent = i;
            } else if (lastPercent == -1) {
                result.append(str.charAt(i));
            }
        }

        if (lastPercent != -1) {
            result.append(str, lastPercent, str.length());
        }

        return result.toString();
    }

    @Override
    public Border drawElement() {
        EntityPlayerSP thePlayer = mc.thePlayer;
        if (thePlayer == null)
            return null;

        if (lastTick != thePlayer.ticksExisted) {
            lastTick = thePlayer.ticksExisted;
            double xDist = thePlayer.posX - thePlayer.prevPosX;
            double zDist = thePlayer.posZ - thePlayer.prevPosZ;
            double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (lastDist < 0)
                lastDist = -lastDist;

            displaySpeed = lastDist * 20;
        }

        int color = new Color(redValue.get(), greenValue.get(), blueValue.get()).getRGB();
        int colord = new Color(redValue.get(), greenValue.get(), blueValue.get()).getRGB() + new Color(0, 0, 0, 50).getRGB();
        FontRenderer fontRenderer = fontValue.get();

        switch (this.rectMode.get().toLowerCase()) {
            case "custom":
                break;
            case "onetap":
                RenderUtils.drawRect(-4.0f, -8.0f, (fontRenderer.getStringWidth(displayText) + 3), fontRenderer.FONT_HEIGHT, new Color(43, 43, 43).getRGB());
                RenderUtils.drawGradientSideways(-3.0, -7.0, (fontRenderer.getStringWidth(displayText) + 2.0), -3.0,
                        rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() + new Color(0, 0, 0, 40).getRGB() : colord,
                        rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() : color);
                break;
            case "skeet":
                RenderUtils.drawRect(-11.0, -9.5, (fontRenderer.getStringWidth(displayText) + 9), fontRenderer.FONT_HEIGHT + 6, new Color(0, 0, 0).getRGB());
                RenderUtils.skeetRect(-10.0, -8.5, (fontRenderer.getStringWidth(displayText) + 8), fontRenderer.FONT_HEIGHT + 5, 8.0, new Color(59, 59, 59).getRGB(), new Color(59, 59, 59).getRGB());
                RenderUtils.skeetRect(-9.0, -7.5, (fontRenderer.getStringWidth(displayText) + 7), fontRenderer.FONT_HEIGHT + 4, 4.0, new Color(59, 59, 59).getRGB(), new Color(40, 40, 40).getRGB());
                RenderUtils.skeetRect(-4.0, -3.0, (fontRenderer.getStringWidth(displayText) + 2), fontRenderer.FONT_HEIGHT + 0, 1.0, new Color(18, 18, 18).getRGB(), new Color(0, 0, 0).getRGB());
                break;
            case "onlywhite":
                RenderUtils.drawRect(-2f, -2f, (fontRenderer.getStringWidth(displayText) + 1), fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 150).getRGB());
                break;
            case "neverlose":
                String index = "LS";
                String[] list = displayText.split(" ");

                RenderUtils.drawRoundedRect(-5f, -4F, (float)Fonts.font20.getStringWidth(index + Arrays.toString(list)) + 5f, 5f + Fonts.font20.FONT_HEIGHT, 2f, new Color(16, 25, 32).getRGB(), 1f, new Color(16, 25, 32).getRGB());
                //RenderUtils.drawBorderedRect(-5.5F, -3.5F, (Fonts.font20.getStringWidth("NL    " + displayText).toFloat() + 8.5F), Fonts.font20.FONT_HEIGHT.toFloat() + 0.5F, 3F, new Color(16, 25, 32).getRGB(), new Color(16, 25, 32).getRGB());

                Fonts.font20.drawString("LS", 0F, 0.6F, new Color(50, 175, 255, 120).getRGB(), false);
                Fonts.font20.drawString("LS", 0F, -0.6F, new Color(50, 175, 255, 120).getRGB(), false);
                Fonts.font20.drawString("LS", -0.75F, -0.6F, new Color(50, 175, 255, 120).getRGB(), false);
                Fonts.font20.drawString("LS", -0.75F, 0.6F, new Color(50, 175, 255, 120).getRGB(), false);
                Fonts.font20.drawString("LS", 0F, 0F, new Color(255, 255, 255, 200).getRGB(), false);

                for (int count = 0; count < list.length; count++) {
                    String text = list[count];
                    Fonts.font20.drawString(text, (float)Fonts.font20.getStringWidth(index) + 5f, 0F, new Color(155, 155, 155).getRGB(), false);
                    if (count + 1 == list.length)
                        break;
                    Fonts.font20.drawString("|", (float)Fonts.font20.getStringWidth(index + text) + 1.5F, 0F, new Color(6, 32, 55).getRGB(), false);
                    index += text + "   ";
                }
                break;
        }

        if (this.outline.get() && !rectMode.get().equals("NeverLose")) {
            GlStateManager.resetColor();
            fontRenderer.drawString(displayText, (int) (fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText) - 1.0f), 0, Color.BLACK.getRGB());
            fontRenderer.drawString(displayText, (int) (fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText) + 1.0f), 0, Color.BLACK.getRGB());
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), (int) (0 + 1.0f), Color.BLACK.getRGB());
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), (int) (0 - 1.0f), Color.BLACK.getRGB());
            fontRenderer.drawString(displayText, fontRenderer.getStringWidth(displayText) - fontRenderer.getStringWidth(displayText), 0, 0);
        }

        boolean rainbow = this.rainbow.get();
        try (RainbowFontShader ignored = RainbowFontShader.begin(rainbow, rainbowX.get() == 0.0F ? 0.0F : 1.0F / rainbowX.get(), rainbowY.get() == 0.0F ? 0.0F : 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F)) {
            if (!rectMode.get().equals("NeverLose"))
                fontRenderer.drawString(displayText, 0F, 0F, rainbow ? 0 : color, shadow.get());

            if (editMode && mc.currentScreen instanceof GuiHudDesigner && editTicks <= 40) {
                fontRenderer.drawString("_", fontRenderer.getStringWidth(displayText) + (rectMode.get().equals("NeverLose") ? 5F : 2F), 0F, rainbow ? ColorUtils.rainbow(400000000L).getRGB() : color, shadow.get());
            }
        }

        if (editMode && !(mc.currentScreen instanceof GuiHudDesigner)) {
            editMode = false;
            updateElement();
        }

        return new Border(
                -2F,
                -2F,
                fontRenderer.getStringWidth(displayText) + 2F,
                fontRenderer.FONT_HEIGHT
        );
    }


    private int count = 0;
    private boolean reverse = false;

    private MSTimer timer = new MSTimer();

    @Override
    public void updateElement() {
        editTicks += 5;
        if (editTicks > 80) editTicks = 0;

        if (count < 0 || count > getDisplay().length()) {
            count = 0;
            reverse = false;
        }

        if (!editMode && animation.get() && !rectMode.get().equals("NeverLose") && timer.hasTimePassed((long) (animationDelay.get() * 1000))) {
            if (reverse) count -= 1;
            else count += 1;
            if (count == getDisplay().length() || count == 0) reverse = !reverse;
            timer.reset();
        }

        String tempDisplayText = animation.get() && !rectMode.get().equals("NeverLose") ? getDisplay().substring(0, count) : getDisplay();

        displayText = editMode ? displayString.get() : tempDisplayText;
    }

    @Override
    public void handleMouseClick(double x, double y, int mouseButton) {
        if (isInBorder(x, y) && mouseButton == 0) {
            if (System.currentTimeMillis() - prevClick <= 250L)
                editMode = true;

            prevClick = System.currentTimeMillis();
        } else {
            editMode = false;
        }
    }

    @Override
    public void handleKey(char c, int keyCode) {
        if (editMode && mc.currentScreen instanceof GuiHudDesigner) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (!displayString.get().isEmpty())
                    displayString.set(displayString.get().substring(0, displayString.get().length() - 1));

                updateElement();
                return;
            }

            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§')
                displayString.set(displayString.get() + c);

            updateElement();
        }
    }

    public Text setColor(Color c) {
        redValue.set(c.getRed());
        greenValue.set(c.getGreen());
        blueValue.set(c.getBlue());
        return this;
    }

}
