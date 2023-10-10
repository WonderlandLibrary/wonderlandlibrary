package tech.atani.client.feature.module.impl.hud;

import com.google.common.base.Supplier;
import com.mojang.realmsclient.gui.ChatFormatting;
import de.florianmichael.rclasses.math.MathUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.render.Render2DEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.combat.KillAura;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.math.interpolation.InterpolationUtil;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.simple.SimpleAnimation;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.render.shader.shaders.GradientShader;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

@ModuleData(name = "TargetHUD", description = "Draws a little box with the targets info", category = Category.HUD)
public class TargetHUD extends Module implements ColorPalette {

    public final StringBoxValue targethudMode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Atani Simple", "Atani Modern", "Atani Modern 2", "Atani Golden", "Augustus 2.6", "Xave", "Ryu", "Fatality", "Icarus", "Atani CS:GO", "Koks", "Astolfo"});
    public final CheckBoxValue followTargetHUD = new CheckBoxValue("Follow Target HUD", "Follow the player in 3d space?", this, false);
    private final SliderValue<Integer> red = new SliderValue<>("Red", "What red will the color have?", this, 255, 0, 255, 0, new Supplier[]{() -> targethudMode.getValue().equalsIgnoreCase("Atani Modern")});
    private final SliderValue<Integer> green = new SliderValue<>("Green", "What green will the color have?", this, 255, 0, 255, 0, new Supplier[]{() -> targethudMode.getValue().equalsIgnoreCase("Atani Modern")});
    private final SliderValue<Integer> blue = new SliderValue<>("Blue", "What blue will the color have?", this, 255, 0, 255, 0, new Supplier[]{() -> targethudMode.getValue().equalsIgnoreCase("Atani Modern")});

    private SimpleAnimation koksHealthAnim = new SimpleAnimation(1, 0.5f);
    private SimpleAnimation modernHealthAnim = new SimpleAnimation(1, 0.75f);

    private final Frustum frustum = new Frustum();

    @Listen
    public void onRender2D(Render2DEvent render2DEvent) {
        if(ModuleStorage.getInstance().getByClass(KillAura.class).isEnabled() && KillAura.curEntity != null && KillAura.curEntity instanceof EntityPlayer) {
            EntityLivingBase target = KillAura.curEntity;

            if(target == null)
                return;

            float calcX = 0;
            float calcY = 0;

            if(this.followTargetHUD.getValue()) {
                frustum.setPosition(Methods.mc.getRenderManager().renderPosX, Methods.mc.getRenderManager().renderPosY, Methods.mc.getRenderManager().renderPosZ);
                if (!frustum.isBoundingBoxInFrustum(target.getEntityBoundingBox())) return;
                final double[] coords = new double[4];
                InterpolationUtil.convertBox(coords, target);
                calcX = (float) (coords[0] + (coords[2] - coords[0]) * 1.1f);
                calcY = (float) (coords[1]);
            } else {
                calcX = render2DEvent.getScaledResolution().getScaledWidth() / 2 + 10;
                calcY = render2DEvent.getScaledResolution().getScaledHeight() / 2 + 5;
            }

            final float x = calcX, y = calcY;

            FontRenderer small = FontStorage.getInstance().findFont("Roboto", 17);
            FontRenderer mcRenderer = Methods.mc.fontRendererObj;

            int counter = 0;

            switch (this.targethudMode.getValue()) {
                case "Koks": {
                    RenderUtil.drawRect(x, y, 117, 41, new Color(24, 24, 24).getRGB());
                    RenderUtil.drawSkinHead(target, x + 3f, y + 3f, 41 - 6);
                    mc.fontRendererObj.drawString(target.getCommandSenderName(), x + 42, y + 5f, -1);
                    double health = target.getHealth() / target.getMaxHealth();
                    koksHealthAnim.interpolate(health);
                    RenderUtil.drawRect(x + 42, y + 18, (float) ((117 - 42 - 5) * koksHealthAnim.getValue()), 7, ColorUtil.blendHealthColours(health));
                    mc.fontRendererObj.drawString(Math.round(target.getHealth()) + " \u2764", x + 42, y + 30f, -1);
                    break;
                }
                case "Fatality": {
                    RenderUtil.drawRect(x, y, 105, 49, new Color(FATALITY_FIRST).darker().getRGB());
                    GradientShader.drawGradientLR(x, y, 105, 2, 1, new Color(FATALITY_FIRST), new Color(FATALITY_SECOND));
                    RenderUtil.drawSkinHead(target, x, y + 2, 25);
                    FontRenderer icons = FontStorage.getInstance().findFont("IcoMoon", 12);
                    icons.drawString("s", x + 4, y + 25 + 7, -1);
                    icons.drawString("r", x + 4, y + 25 + 10 + icons.FONT_HEIGHT, -1);
                    RenderUtil.drawRect(x + 13, y + 25 + 6.5f, 86, 3f, new Color(FATALITY_FIRST).darker().darker().getRGB());
                    RenderUtil.drawRect(x + 13, y + 25 + 6.5f, 86, 3f, new Color(0, 255, 0).darker().darker().getRGB());
                    RenderUtil.drawRect(x + 13, y + 25 + 9.5f + icons.FONT_HEIGHT, 86, 3f, new Color(51, 153, 255).darker().getRGB());
                    FontRenderer medium20 = FontStorage.getInstance().findFont("Roboto", 19);
                    medium20.drawString(target.getCommandSenderName(), x + 28, y + 5, -1);
                    medium20.drawString("Health: " + (int)target.getHealth(), x + 28, y + 5 + medium20.FONT_HEIGHT + 3, -1);
                    break;
                }
                case "Ryu": {
                    float height = 35;
                    RenderUtil.drawRect(x, y, height, height, new Color(0, 0, 0, 140).getRGB());
                    RenderUtil.drawSkinHead(target, x + 4, y + 4, (int)height - 8);
                    RenderUtil.drawRect(x + height, y, 3, height, ColorUtil.setAlpha(new Color(RYU), 140).getRGB());
                    RenderUtil.drawRect(x + height + 3, y, 70, height, new Color(0, 0, 0, 80).getRGB());
                    RenderableShaders.render(true, false, () -> {
                        FontRenderer medium21 = FontStorage.getInstance().findFont("Roboto", 21);
                        FontRenderer medium17 = FontStorage.getInstance().findFont("Roboto", 17);
                        float health = MathUtils.clamp(target.getHealth(), 0, 20);
                        medium21.drawString(target.getCommandSenderName(), x + height + 3 + 4 + 0.5f, y + 5 + 0.5f, Color.black.getRGB());
                        medium17.drawString("Health " + health, x + height + 3 + 4 + 0.5f, y + 5 + medium21.FONT_HEIGHT + 2 + 0.5f, Color.black.getRGB());
                        medium17.drawString("Distance " + MathUtil.round(FightUtil.getRange(target), 1), x + height + 3 + 4 + 0.5f, y + 5 + medium21.FONT_HEIGHT + 2 + medium17.FONT_HEIGHT + 0.5f, Color.black.getRGB());
                    });
                    FontRenderer medium21 = FontStorage.getInstance().findFont("Roboto Medium", 21);
                    FontRenderer medium17 = FontStorage.getInstance().findFont("Roboto Medium", 17);
                    float health = MathUtils.clamp(target.getHealth(), 0, 20);
                    medium21.drawString(target.getCommandSenderName(), x + height + 3 + 4, y + 5, -1);
                    medium17.drawString("Health " + ChatFormatting.WHITE + health, x + height + 3 + 4, y + 5 + medium21.FONT_HEIGHT + 2, RYU);
                    medium17.drawString("Distance " + ChatFormatting.WHITE + MathUtil.round(FightUtil.getRange(target), 1), x + height + 3 + 4, y + 5 + medium21.FONT_HEIGHT + 2 + medium17.FONT_HEIGHT, RYU);
                    break;
                }
                case "Atani CS:GO": {
                    float width = 120, height = 50;
                    RenderUtil.drawRect(x, y, width, height, new Color(0, 58, 105).brighter().getRGB());
                    GradientShader.drawGradientTB(x + 2, y + 2, width - 4, 14, 1, new Color(0, 48, 95).brighter().brighter(), new Color(0, 48, 95).brighter());
                    RenderUtil.drawRect(x + 2, y + 14, width - 4, height - 14 - 2, new Color(240, 240, 240).getRGB());
                    FontRenderer fontRenderer = FontStorage.getInstance().findFont("Arial", 19);
                    fontRenderer.drawStringWithShadow(target.getCommandSenderName(), x + 3.5f, y + 3.5f, -1);
                    int headSize = (int) (height - 14 - 6);
                    RenderUtil.drawSkinHead(target, x + 2 + 2, y + 14 + 2, headSize);
                    RenderUtil.drawBorderedRect(x + 6 + headSize, y + 14 + 2, (x + 6 + headSize) + width - 10 - headSize, (y + 14 + 2) + headSize, 1f,-1, new Color(200, 200, 200).getRGB(), true);
                    float health = MathUtils.clamp(target.getHealth(), 0, 20);
                    float percentage = health / 20;
                    RenderUtil.drawRect(x + 6 + headSize + 2, y + 14 + 2 + 2, width - (6 + headSize + 8), 6, new Color(240, 240, 240).getRGB());
                    GradientShader.drawGradientTB(x + 6 + headSize + 2, y + 14 + 2 + 2, (width - (6 + headSize + 8)) * percentage, 6, 1, Color.red, Color.red.darker().darker());
                    FontRenderer smallArial = FontStorage.getInstance().findFont("Arial", 17);
                    Color textColor = new Color(15, 15, 15);
                    fontRenderer.drawString( ChatFormatting.BOLD.toString() + ((int)health) + " HP (" + ((int)(percentage * 100)) + "%)", x + 6 + headSize + 3, y + 14 + 2 + 2 + 9, textColor.getRGB());
                    int roundedOwn = Math.round(Methods.mc.thePlayer.getHealth());
                    int roundedTarget = Math.round(target.getHealth());
                    int status = 0;
                    if(roundedOwn == roundedTarget) {
                        status = 0;
                    } else if(roundedOwn < roundedTarget) {
                        status = 1;
                    } else if(roundedOwn > roundedTarget) {
                        status = 2;
                    }
                    fontRenderer.drawString(status == 0 ? "Draw" : status == 1 ? "Lose" : "Win", x + 6 + headSize + 3, y + 14 + 2 + 2 + 10 + smallArial.FONT_HEIGHT, status == 0 ? Color.yellow.darker().getRGB() : status == 1 ? Color.red.darker().getRGB() : Color.green.darker().getRGB());
                    break;
                }
                case "Augustus 2.6": {
                    String text = target.getCommandSenderName() + " | " + Math.round(target.getHealth());
                    float length = mc.fontRendererObj.getStringWidthInt(text);
                    float textX = x + 4, textY = y + 4.5f;
                    float rectWidth = 8 + length, rectHeight = mc.fontRendererObj.FONT_HEIGHT + 8;
                    RenderUtil.drawRect(x, y, rectWidth, rectHeight, new Color(0, 0, 0, 100).getRGB());
                    mc.fontRendererObj.drawStringWithShadow(text, textX, textY, -1);
                }
                break;
                case "Icarus": {
                    FontRenderer pangramRegular = FontStorage.getInstance().findFont("Pangram Regular", 17);
                    String text = target.getCommandSenderName() + " | " + Math.round(target.getHealth());
                    float textX = x + 4, textY = y + 4.5f;
                    float rectWidth = 100, rectHeight = pangramRegular.FONT_HEIGHT * 2 + 8;
                    RoundedShader.drawRound(x, y, rectWidth, rectHeight, 5, new Color(20, 20, 20));
                    pangramRegular.drawStringWithShadow(text, textX, textY, -1);
                    RenderUtil.drawRect(x + 5, y + 16, (target.getHealth() / target.getMaxHealth()) * (rectWidth - 10), 2, new Color(20, 20, 20).darker().getRGB());
                    RoundedShader.drawGradientHorizontal(x + 5, y + 16, (target.getHealth() / target.getMaxHealth()) * (rectWidth - 10), 2, 2, new Color(ICARUS_FIRST), new Color(ICARUS_SECOND));
                }
                break;
                case "Xave": {
                    FontRenderer roboto17 = FontStorage.getInstance().findFont("Roboto", 17);
                    String text = target.getCommandSenderName() + " | " + Math.round(target.getHealth());
                    float length = roboto17.getStringWidthInt(text);
                    float textX = x + 4, textY = y + 4.5f;
                    float rectWidth = 8 + length, rectHeight = roboto17.FONT_HEIGHT + 8;
                    RenderUtil.drawRect(x, y, rectWidth, rectHeight, new Color(0, 0, 0, 100).getRGB());
                    roboto17.drawStringWithShadow(text, textX, textY, -1);
                }
                break;
                case "Atani Simple":
                    RenderableShaders.renderAndRun(() -> {
                        String text = target.getCommandSenderName() + " | " + Math.round(target.getHealth());
                        FontRenderer roboto17 = FontStorage.getInstance().findFont("Roboto", 17);
                        float length = roboto17.getStringWidthInt(text);
                        float textX = x + 4, textY = y + 4.5f;
                        float rectWidth = 8 + length, rectHeight = roboto17.FONT_HEIGHT + 8;
                        RenderUtil.drawRect(x, y, rectWidth, rectHeight, BACK_TRANS_180);
                        roboto17.drawStringWithShadow(text, textX, textY, -1);
                    });
                    break;
                case "Atani Modern":
                    RenderableShaders.renderAndRun(() -> {
                        FontRenderer roboto17 = FontStorage.getInstance().findFont("Roboto", 17);
                        float textX = x + 4, textY = y + 4.5f;
                        float rectWidth = 120, rectHeight = 50;
                        RoundedShader.drawRoundOutline(x, y, rectWidth, rectHeight, 7, 1, new Color(20, 20, 20), new Color(red.getValue(), green.getValue(), blue.getValue()));
                        GuiInventory.drawEntityOnScreen((int) x + 20, (int) (y + rectHeight) - 5, 18, target.rotationYaw, -target.rotationPitch, target);
                        roboto17.drawString(target.getCommandSenderName(), x + 36, y + 6, -1);
                        roboto17.drawString((int)target.getHealth() + " / 20", x + 36, y + 6 + roboto17.FONT_HEIGHT + 2, -1);
                        String predictedOutcome = "";
                        int roundedOwn = Math.round(Methods.mc.thePlayer.getHealth());
                        int roundedTarget = Math.round(target.getHealth());
                        if(roundedOwn == roundedTarget) {
                            predictedOutcome = ChatFormatting.YELLOW + "Draw";
                        } else if(roundedOwn < roundedTarget) {
                            predictedOutcome = ChatFormatting.RED + "Losing";
                        } else if(roundedOwn > roundedTarget) {
                            predictedOutcome = ChatFormatting.GREEN + "Winning";
                        }
                        roboto17.drawString(predictedOutcome, x + 36, y + 6 + (roboto17.FONT_HEIGHT + 2) * 2, -1);
                    });
                    break;
                case "Atani Modern 2":
                    RenderableShaders.renderAndRun(() -> {
                        FontRenderer pangramRegular = FontStorage.getInstance().findFont("Pangram Regular", 17);
                        float textX = x + 4, textY = y + 4.5f;
                        float rectWidth = 120, rectHeight = 50;
                        RoundedShader.drawRoundOutline(x, y, rectWidth, rectHeight, 9, 1.2F, new Color(20, 20, 20), new Color(red.getValue(), green.getValue(), blue.getValue()));
                        GuiInventory.drawEntityOnScreen((int) x + 20, (int) (y + rectHeight) - 5, 18, target.rotationYaw, -target.rotationPitch, target);
                        pangramRegular.drawString(target.getCommandSenderName(), x + 36, y + 6, -1);
                        String predictedOutcome = "";
                        int roundedOwn = Math.round(Methods.mc.thePlayer.getHealth());
                        int roundedTarget = Math.round(target.getHealth());
                        if(roundedOwn == roundedTarget) {
                            predictedOutcome = ChatFormatting.YELLOW + "Draw";
                        } else if(roundedOwn < roundedTarget) {
                            predictedOutcome = ChatFormatting.RED + "Losing";
                        } else if(roundedOwn > roundedTarget) {
                            predictedOutcome = ChatFormatting.GREEN + "Winning";
                        }
                        pangramRegular.drawString(predictedOutcome, x + 36, y + 6 + pangramRegular.FONT_HEIGHT + 2, -1);
                        double health = target.getHealth() / target.getMaxHealth();
                        modernHealthAnim.interpolate(health);
                        RenderUtil.drawRect(x + 36, y + 30, (float) ((70) * modernHealthAnim.getValue()), 7, -1);
                    });
                    break;
                case "Atani Golden":
                    float width = 100;
                    float height = 2 + 3 * (small.FONT_HEIGHT + 1);
                    RenderUtil.drawRect(x, y, width, height, new Color(20, 20, 20).getRGB());
                    small.drawStringWithShadow(target.getCommandSenderName(), x + 2 + height - 4 + 2 + 2, y + 2, -1);
                    small.drawStringWithShadow(MathUtil.round(target.getHealth(), 1) + " HP", x + 2 + height - 4 + 2 + 2, y + 2 + small.FONT_HEIGHT + 1, -1);
                    String predictedOutcome = "";
                    int roundedOwn = Math.round(Methods.mc.thePlayer.getHealth());
                    int roundedTarget = Math.round(target.getHealth());
                    if(roundedOwn == roundedTarget) {
                        predictedOutcome = ChatFormatting.YELLOW + "Draw";
                    } else if(roundedOwn < roundedTarget) {
                        predictedOutcome = ChatFormatting.RED + "Losing";
                    } else if(roundedOwn > roundedTarget) {
                        predictedOutcome = ChatFormatting.GREEN + "Winning";
                    }
                    small.drawStringWithShadow(predictedOutcome, x + 2 + height - 4 + 2 + 2, y + 2 + small.FONT_HEIGHT + 1 + small.FONT_HEIGHT + 1, -1);
                    RenderUtil.drawSkinHead(target, x + 2, y + 2, (int) height - 4);
                    float healthPoint = height / 20;
                    RenderUtil.startScissorBox();
                    RenderUtil.drawScissorBox(x + width - 2, y + (20 * healthPoint) - (target.getHealth() * healthPoint), 2, target.getHealth() * healthPoint);
                    GradientShader.drawGradientTB(x + width - 2, y, 2, height, 1, new Color(255, 202, 3), new Color(255, 84, 3));
                    RenderUtil.endScissorBox();
                    break;
                case "Astolfo":
                    RenderUtil.drawRect(x, y, 156, 57, new Color(0,0,0, 170).getRGB());

                    mcRenderer.drawString(target.getCommandSenderName(), x + 33, y + 6, Color.WHITE.getRGB());

                    RenderUtil.scaleStart(x + 33, y + 20, 2.5f);
                    mcRenderer.drawStringWithShadow(Math.round(target.getHealth() / 2.0f * 10) / 10 + " \u2764", x + 33, y + 20, new Color(ColorUtil.blendRainbowColours(counter * 150L)).getRGB());
                    RenderUtil.scaleEnd();

                    GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
                    GuiInventory.drawEntityOnScreen((int) (x + 17), (int) (y + 54), 25, target.rotationYaw, -target.rotationPitch, target);
                    RenderUtil.drawRect(x + 30, y + 46, 120, 8, new Color(ColorUtil.blendRainbowColours(counter * 150L)).darker().darker().darker().getRGB());
                    RenderUtil.drawRect(x + 30, y + 46, target.getHealth() / target.getMaxHealth() * 120, 8, new Color(ColorUtil.blendRainbowColours(counter * 150L)).getRGB());
                    RenderUtil.drawRect(x + 30 + target.getHealth() / target.getMaxHealth() * 120 - 3, y + 46, 3, 8, new Color (-1979711488, true).getRGB());
                    counter++;
                    break;
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}