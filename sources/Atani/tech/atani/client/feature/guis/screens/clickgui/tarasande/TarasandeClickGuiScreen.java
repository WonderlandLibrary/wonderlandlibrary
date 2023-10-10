package tech.atani.client.feature.guis.screens.clickgui.tarasande;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.guis.screens.clickgui.tarasande.frame.Frame;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.ClickGui;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Animation;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.particle.Particle;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TarasandeClickGuiScreen extends GuiScreen implements ColorPalette {
    private ArrayList<Frame> frames = new ArrayList<>();
    private HashMap<Frame, Animation> framesAnimations = new HashMap<>();
    private ClickGui clickGui;
    public DecelerateAnimation openingAnimation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);
    public ArrayList<Particle> particles = new ArrayList<>();

    @Override
    public void initGui() {
        this.framesAnimations.clear();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width - 60, this.height - 25, 55, 20, "Reset Gui"));
        this.clickGui = ModuleStorage.getInstance().getByClass(ClickGui.class);
        openingAnimation.setDirection(Direction.FORWARDS);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderableShaders.renderAndRun(false, true, () -> {
            RenderUtil.drawRect(0, 0, super.width, super.height, ColorUtil.setAlpha(new Color(TARASANDE), (float) (openingAnimation.getOutput() * 0.6f)).getRGB());
        });
        int numPoints = 100;
        if (particles.size() < numPoints)
            particles.add(new Particle(width / 2.0, height / 2.0, new ScaledResolution(mc)));
        for(Particle particle : particles) {
            particle.render(mouseX, mouseY, this.openingAnimation.getOutput());
        }
        if(frames.isEmpty()) {
            float height = 22, y = this.height / 2 - (((mc.fontRendererObj.FONT_HEIGHT + 2) + 5f * height + 4) * 3) / 2 + 4, width = 150, x = (this.width - (4 * (width + 5))) / 2;
            int counter = 0;
            for(Category category : Category.values()) {
                if(counter == 4) {
                    x = (this.width - (4 * (width + 5))) / 2;
                    y += (mc.fontRendererObj.FONT_HEIGHT + 2) + 5 * height + 4;
                }
                if(counter == 8) {
                    x = (this.width - (4 * (width + 5))) / 2;
                    y += (mc.fontRendererObj.FONT_HEIGHT + 2) + 5 * height + 4;
                }
                Frame frame = new Frame(category, x, y, width, (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2), height, this);
                Animation animation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);
                this.frames.add(frame);
                this.framesAnimations.put(frame, animation);
                // We need to set the direction backwards at first so it will start at 0, then to forwards so it will animate upwards
                animation.setDirection(Direction.FORWARDS);
                x += width + 5;
                counter++;
            }
        }
        if(framesAnimations.isEmpty()) {
            for(Frame frame : frames) {
                Animation animation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);
                this.framesAnimations.put(frame, animation);
                // We need to set the direction backwards at first so it will start at 0, then to forwards so it will animate upwards
                animation.setDirection(Direction.FORWARDS);
            }
        }

        ScaledResolution sr = new ScaledResolution(mc);

        float animationLeftRight = 0, animationUpDown = 0;
        int dWheel = Mouse.getDWheel();
        for(Frame frame : framesAnimations.keySet()) {
            if(clickGui.openingAnimation.getValue()) {
                switch (clickGui.dropdownAnimation.getValue()) {
                    case "Left to Right":
                        animationLeftRight = (float) (sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                        if(openingAnimation.getDirection() == Direction.FORWARDS) {
                            animationLeftRight = (float) -(sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                        }
                        break;
                    case "Right to Left":
                        animationLeftRight = (float) (sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                        if(openingAnimation.getDirection() == Direction.BACKWARDS) {
                            animationLeftRight = (float) -(sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                        }
                        break;
                    case "Up to Down":
                        animationUpDown = (float) (sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                        if(openingAnimation.getDirection() == Direction.FORWARDS) {
                            animationUpDown = (float) -(sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                        }
                        break;
                    case "Down to Up":
                        animationUpDown = (float) (sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                        if(openingAnimation.getDirection() == Direction.BACKWARDS) {
                            animationUpDown = (float) -(sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                        }
                        break;
                    case "Scale-In":
                        RenderUtil.scaleStart(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, openingAnimation.getOutput().floatValue());
                        break;
                    case "Frame Scale-In":
                        RenderUtil.scaleStart(frame.getPosX() + frame.getFinalWidth() / 2, frame.getPosY() + frame.getFinalHeight() / 2, framesAnimations.get(frame).getOutput().floatValue());
                        break;
                }
            }
            frame.dWheel = dWheel;
            frame.setAddY(animationUpDown);
            frame.setAddX(animationLeftRight);
            frame.drawScreen(mouseX, mouseY);
            RenderUtil.scaleEnd();
        }
        if(clickGui.openingAnimation.getValue()) {
            switch (clickGui.dropdownAnimation.getValue()) {
                case "Up to Down":
                case "Down to Up":
                case "Left to Right":
                case "Right to Left":
                case "Scale-In":
                    if(this.openingAnimation.finished(Direction.BACKWARDS))
                        mc.displayGuiScreen(null);
                    break;
                case "Frame Scale-In":
                    boolean unfinished = false;
                    for(Animation animation : framesAnimations.values()) {
                        if(!animation.finished(Direction.BACKWARDS)) {
                            unfinished = true;
                        }
                    }
                    if(!unfinished) {
                        mc.displayGuiScreen(null);
                    }
                    break;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id){
            case 0:
                mc.displayGuiScreen(null);
                ClickGui.clickGuiScreenSimple = null;
                ModuleStorage.getInstance().getByClass(ClickGui.class).toggle();
                break;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(Frame frame : framesAnimations.keySet()) {
            frame.mouseClick(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char key, int code) {
        if(code == 1) {
            if(clickGui.openingAnimation.getValue()) {
                this.openingAnimation.setDirection(Direction.BACKWARDS);
                for(Animation animation : framesAnimations.values()) {
                    animation.setDirection(Direction.BACKWARDS);
                }
            } else {
                mc.displayGuiScreen(null);
            }
        }
    }
}
