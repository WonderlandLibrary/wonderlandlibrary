package tech.atani.client.feature.guis.screens.clickgui.icarus;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.ClickGui;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.guis.screens.clickgui.icarus.frame.Frame;
import tech.atani.client.utility.render.animation.advanced.Animation;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IcarusClickGuiScreen extends GuiScreen {
    private ArrayList<Frame> frames = new ArrayList<>();
    private HashMap<Frame, Animation> framesAnimations = new HashMap<>();
    private float scroll = 0;
    private ClickGui clickGui;
    private DecelerateAnimation openingAnimation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);

    @Override
    public void initGui() {
        this.framesAnimations.clear();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width - 60, this.height - 25, 55, 20, "Reset Gui"));
        this.clickGui = ModuleStorage.getInstance().getByClass(ClickGui.class);
        openingAnimation.setDirection(Direction.FORWARDS);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        scroll += Mouse.getDWheel() / 10F;
        if(frames.isEmpty()) {
            float y = 10, width = 100, height = 26, x = (this.width - (Category.values().length * (width + 5))) / 2; /* The X is made like that so categories will be in the middle*/
            for(Category category : Category.values()) {
                Frame frame = new Frame(category, x, y, width, height, height - 6);
                Animation animation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);
                this.frames.add(frame);
                this.framesAnimations.put(frame, animation);
                // We need to set the direction backwards at first so it will start at 0, then to forwards so it will animate upwards
                animation.setDirection(Direction.FORWARDS);
                x += width + 5;
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
            frame.setAddY(scroll + animationUpDown);
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
                ClickGui.clickGuiScreenIcarus = null;
                ModuleStorage.getInstance().getByClass(ClickGui.class).toggle();
                break;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ArrayList<Frame> framesCopy = new ArrayList<>(framesAnimations.keySet());

        for(Frame frame : framesCopy) {
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
