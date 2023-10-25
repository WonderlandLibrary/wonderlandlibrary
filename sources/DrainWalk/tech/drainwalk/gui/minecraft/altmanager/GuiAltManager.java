package tech.drainwalk.gui.minecraft.altmanager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.TextField;
import tech.drainwalk.gui.minecraft.Button;
import tech.drainwalk.gui.minecraft.Generator;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.math.MathUtility;
import tech.drainwalk.utility.render.GLUtility;
import tech.drainwalk.utility.render.RenderUtility;
import tech.drainwalk.utility.render.StencilUtility;
import tech.drainwalk.utility.sound.Sound;

import java.io.IOException;
import java.util.ArrayList;

public class GuiAltManager extends GuiScreen {
    protected TextField inputField;
    public Button button;
    public boolean textFieldActive = false;
    
    public static final ArrayList<Alt> altList = new ArrayList<>();
    public static Alt selectedAlt = new Alt("Alt");
    public static float scrollValue = 0;
    public static final Animation scrollAnimation = new Animation();
    
    @Override
    public void initGui() {
        int width1 = 93;
        int height1 = 28 + 6;
        int pos1X = (int) ((mc.displayWidth / 4f) - (width1 / 2f));
        int pos1Y = (int) (mc.displayHeight / 4f) + 130;
        this.buttonList.add(new Button(0, pos1X - (width1 + 15), pos1Y, width1, height1 - 6, "Add"));
        this.buttonList.add(new Button(1, pos1X, pos1Y, width1, height1 - 6, "Random"));
        this.buttonList.add(new Button(2, pos1X + (width1 + 15), pos1Y, width1, height1 - 6, "Close"));
        this.buttonList.add(button = new Button(3, pos1X + (width1 + 15), pos1Y, width1, height1 - 6, "Done"));

        this.inputField = new TextField(0, FontManager.SEMI_BOLD_28, (pos1X - (width1 + 15)), (int) (pos1Y), 200, height1 - 6);
        this.inputField.setMaxStringLength(16);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText("");
        this.inputField.setCanLoseFocus(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        GuiAltManager.scrollAnimation.animate(GuiAltManager.scrollValue, 0, 27, EasingList.NONE, mc.getRenderPartialTicks());
        GLUtility.INSTANCE.rescale(2);
        float posX = mc.displayWidth / 4f;
        float posY = mc.displayHeight / 4f;
        GlStateManager.enableBlend();
        RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/main_menu.png"), 0, 0, mc.displayWidth / 2f, mc.displayHeight / 2f, 0, 1);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), 0, 0, mc.displayWidth / 2f, mc.displayHeight / 2f, 0, 0.1f);
        }
        RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/main_menu_image.png"), posX - (306 / 1.765f), posY - (74 * 2.31f), 347, 343, 0, 1);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), posX - (306 / 1.765f), posY - (74 * 2.31f), 347, 343, 35, 0.1f);
        }
        GlStateManager.disableBlend();
        //RenderUtility.drawRoundedRect(posX - (306 / 1.765f), posY - (74 * 2.31f), 347, 343, 41, ColorUtility.rgba(0, 0, 0, 40));
        //RenderUtility.drawRoundedOutlineRect(posX - (306 / 1.765f) - 0.5f, posY - (74 * 2.31f) - 0.5f, 347 + 1, 343 + 1, 41, 2f, ColorUtility.rgba(100, 100, 100, 160));
        if (!textFieldActive) {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                if (i != 3) {
                    (this.buttonList.get(i)).drawButton(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), partialTicks);
                }
            }

            for (int j = 0; j < this.labelList.size(); ++j) {
                if (j != 3) {

                    (this.labelList.get(j)).drawLabel(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f));
                }
            }
        } else {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                if (i == 3) {
                    (this.buttonList.get(i)).drawButton(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), partialTicks);
                }
            }

            for (int j = 0; j < this.labelList.size(); ++j) {
                if (j == 3) {

                    (this.labelList.get(j)).drawLabel(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f));
                }
            }
        }
        StencilUtility.initStencilToWrite();
        RenderUtility.drawRect(posX - (295 / 2f), posY - (74 * 2.31f) + 20, 295, 253, -1);
        StencilUtility.readStencilBuffer(1);
        float offset = 0;
        for (Alt alt : GuiAltManager.altList) {
            alt.getSelectAnimation().animate(0, 1, 0.2f, EasingList.NONE, mc.getRenderPartialTicks());
            alt.getHoverAnimation().animate(0, 1, 0.2f, EasingList.NONE, mc.getRenderPartialTicks());
            GlStateManager.pushMatrix();
            float value = 0.010f;
            float anim = alt.getHoverAnimation().getAnimationValue() * value;

            float xPosition = posX - (175 / 2f);
            float yPosition = posY - (74 * 2.31f) + 25 + offset + GuiAltManager.scrollAnimation.getAnimationValue();
            float w = 175;
            float h = 28;
            GlStateManager.translate((xPosition + w / 2f), (yPosition + h / 2f), 0);
            GlStateManager.scale(1 + anim, 1 + anim, 1 + anim);
            GlStateManager.translate(-(xPosition + w / 2f), -(yPosition + h / 2f), 0);

            RenderUtility.drawRoundedRect(posX - (175 / 2f), posY - (74 * 2.31f) + 20 + offset + GuiAltManager.scrollAnimation.getAnimationValue(), 175, 28, 8, ColorUtility.rgba(0, 0, 0, (int) (60 + (20 * alt.getHoverAnimation().getAnimationValue()) + (25 * alt.getSelectAnimation().getAnimationValue()))));
            GlStateManager.popMatrix();
            FontManager.SEMI_BOLD_28.drawCenteredString(alt.getName(), posX, posY - (74 * 2.31f) + (27.5f) + offset + GuiAltManager.scrollAnimation.getAnimationValue(), ColorUtility.rgba(255, 255, 255, (int) (255 - (85 * (1 - alt.getSelectAnimation().getAnimationValue())))));
            offset += 28 + 8;
        }
        StencilUtility.uninitStencilBuffer();
        FontManager.SEMI_BOLD_18.drawCenteredString("Your current account: " + mc.getSession().getUsername(), posX, posY - (74 * 2.31f) - 10, -1);
        if (textFieldActive) {
            RenderUtility.drawRoundedRect((int) (posX - (93 + 15) + 46f) - (185 / 2f), (int) (posY + 10) + 120, 200, 28, 8, ColorUtility.rgba(0, 0, 0, 80));
            this.inputField.drawTextBox();
        }

        float maxVal = -(GuiAltManager.altList.size() * (28 + 8)) + MathUtility.clamp(GuiAltManager.altList.size() * (28 + 8), 0, 252);
        float h = 253f;
        float height = h - MathUtility.clamp(-maxVal, 0, h - 30);
        RenderUtility.drawRect(posX + 162, posY - (74 * 2.31f) + 20f, 2, h, ColorUtility.rgba(24, 35, 45, 120));
        RenderUtility.drawRect(posX + 162, posY - (74 * 2.31f) + 20f + (height == 253 ? 0 : MathUtility.clamp(((h - height) * (GuiAltManager.scrollAnimation.getAnimationValue() / maxVal)), 0, h - height)), 2, height, ColorUtility.rgba(255, 255, 255, 160));
        GLUtility.INSTANCE.rescaleMC();

    }

    @Override
    public void updateScreen() {
        if (textFieldActive) {
            this.inputField.updateCursorCounter();
        }
        float offset = 0;
        for (Alt alt : GuiAltManager.altList) {
            float posX = mc.displayWidth / 4f;
            float posY = mc.displayHeight / 4f;
            boolean hovered = inBound((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), posX - (175 / 2f), posY - (74 * 2.31f) + 20 + offset + GuiAltManager.scrollAnimation.getAnimationValue(), 175, 28);
            alt.getHoverAnimation().update(hovered);
            alt.getSelectAnimation().update(GuiAltManager.selectedAlt == alt);
            offset += 28 + 8;
        }
        GuiAltManager.scrollAnimation.update(false);

        if (!textFieldActive) {
            for (GuiButton guiButton : buttonList) {
                if (guiButton instanceof Button button1) {
                    if (guiButton != button) {
                        button1.getAnimation().update(guiButton.isMouseOver());
                    }
                }
            }
        } else {
            for (GuiButton guiButton : buttonList) {
                if (guiButton instanceof Button button1) {
                    if (guiButton == button) {
                        button1.getAnimation().update(guiButton.isMouseOver());
                    }
                }
            }
        }

        GuiAltManager.scrollValue = MathUtility.clamp(GuiAltManager.scrollValue, -(GuiAltManager.altList.size() * (28 + 8)) + MathUtility.clamp(GuiAltManager.altList.size() * (28 + 8), 0, 252), 0);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        i = Integer.compare(i, 0);

        GuiAltManager.scrollValue += i * 23;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        float posX = mc.displayWidth / 4f;
        float posY = mc.displayHeight / 4f;
        if (keyCode == Keyboard.KEY_INSERT) {
            GuiAltManager.altList.clear();
        }
        if (textFieldActive) {
            inputField.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == Keyboard.KEY_RETURN) {
                String text = inputField.getText().trim();
                if (text.length() > 0) {
                    addAlt();
                }
            }
        }
        if (inBound((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), posX - (295 / 2f), posY - (74 * 2.31f) + 20, 295, 253)) {
            float offset = 0;
            for (Alt alt : GuiAltManager.altList) {
                boolean hovered = inBound((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), posX - (175 / 2f), posY - (74 * 2.31f) + 20 + offset + GuiAltManager.scrollAnimation.getAnimationValue(), 175, 28);
                if (hovered && Keyboard.KEY_DELETE == keyCode) {
                    alt.canRemove = true;
                }
                offset += 28 + 8;
            }
        }
        GuiAltManager.altList.removeIf(alt -> alt.canRemove);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!textFieldActive) {
            if (this.selectedButton != null && state == 0 && selectedButton != button) {
                this.selectedButton.mouseReleased((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f));
                this.selectedButton = null;
            }
        } else {
            if (this.selectedButton != null && state == 0 && selectedButton == button) {
                this.selectedButton.mouseReleased((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f));
                this.selectedButton = null;
            }
        }
    }

    public void addAlt() {
        String text = inputField.getText().trim();

        textFieldActive = false;
        text = text.replace(' ', '_');
        if (text.length() >= 2) {
            GuiAltManager.altList.add(new Alt(text));
        }
        inputField.setFocused(false);
        inputField.setText("");

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (textFieldActive) {
            inputField.mouseClicked((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), mouseButton);
        }
        if (!textFieldActive) {
            if (mouseButton == 0) {
                for (int i = 0; i < this.buttonList.size(); ++i) {
                    if (i != 3) {
                        GuiButton guibutton = this.buttonList.get(i);

                        if (guibutton.mousePressed(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f))) {
                            this.selectedButton = guibutton;
                            guibutton.playPressSound(this.mc.getSoundHandler());
                            this.actionPerformed(guibutton);
                        }
                    }
                }
            }
        } else {
            if (mouseButton == 0) {
                for (int i = 0; i < this.buttonList.size(); ++i) {
                    if (i == 3) {
                        GuiButton guibutton = this.buttonList.get(i);

                        if (guibutton.mousePressed(this.mc, (int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f))) {
                            this.selectedButton = guibutton;
                            guibutton.playPressSound(this.mc.getSoundHandler());
                            this.actionPerformed(guibutton);
                        }
                    }
                }
            }
        }
        float posX = mc.displayWidth / 4f;
        float posY = mc.displayHeight / 4f;
        if (inBound((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), posX - (295 / 2f), posY - (74 * 2.31f) + 20, 295, 253)) {
            float offset = 0;
            for (Alt alt : GuiAltManager.altList) {
                if (inBound((int) (Mouse.getX() / 2f), (int) ((mc.displayHeight - Mouse.getY()) / 2f), posX - (175 / 2f), posY - (74 * 2.31f) + 20 + offset + GuiAltManager.scrollAnimation.getAnimationValue(), 175, 28)) {

                    if (mouseButton == 0) {
                        if (alt != GuiAltManager.selectedAlt) {
                            Sound g = new Sound();
                            g.playSound("buttonclick.wav", 30);
                            GuiAltManager.selectedAlt = alt;
                            mc.setSession(new Session(GuiAltManager.selectedAlt.getName(), "", "", "mojang"));
                        }
                    } else if (mouseButton == 1) {
                        alt.canRemove = true;
                    }
                }
                offset += 28 + 8;
            }
            GuiAltManager.altList.removeIf(alt -> alt.canRemove);
        }
    }

    public static boolean inBound(int mX, int mY, float x, float y, float width, float height) {
        return (mX > x && mX < (x + width)) && (mY > y && mY < (y + height));
    }


    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            textFieldActive = true;
            this.inputField.setFocused(true);
        }
        if (button.id == 1) {
            GuiAltManager.altList.add(new Alt(Generator.generateName()));
            System.out.println("RANDOM");
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(null);
        }
        if (button.id == 3) {
            addAlt();
        }
    }
}
