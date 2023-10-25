package ru.smertnix.celestial.ui.clickgui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventManager;
import ru.smertnix.celestial.event.events.EventInitGui;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.altmanager.GuiAltButton;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.ExpandableComponent;
import ru.smertnix.celestial.ui.clickgui.component.impl.StringComponent;
import ru.smertnix.celestial.ui.config.ConfigManager;
import ru.smertnix.celestial.ui.particle.ParticleUtils;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.math.BlurUtil;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.math.animations.Animation;
import ru.smertnix.celestial.utils.math.animations.Direction;
import ru.smertnix.celestial.utils.math.animations.impl.DecelerateAnimation;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.GLUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mojang.realmsclient.gui.ChatFormatting;

import javafx.animation.Interpolator;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    public static boolean escapeKeyInUse;
    public float scale = 2;
    public float scrollAnimation = 0;

    public List<ru.smertnix.celestial.ui.clickgui.Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public FeatureCategory type;
    private Component selectedPanel;
    private Animation initAnimation;
    private static ResourceLocation ANIME_GIRL;
    public static GuiSearcher search;
    public static GuiSearcher str;
    int key;
    public static GuiAltButton plus;
    public static GuiAltButton minus;
    public static GuiAltButton files;
    public static float anim;
    public static float a;
    public ClickGuiScreen() {
        int x = 15;
        int y = 10;
        for (FeatureCategory type : FeatureCategory.values()) {
            this.type = type;
            this.components.add(new ru.smertnix.celestial.ui.clickgui.Panel(type, x, y));
            selectedPanel = new ru.smertnix.celestial.ui.clickgui.Panel(type, x, y);
            x += width + 110;
        }
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void initGui() {
    	a = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        initAnimation = new DecelerateAnimation(600, 1);
        this.screenHelper = new ScreenHelper(0, 0);
        search = new GuiSearcher(1337, this.mc.fontRendererObj, 10, sr.getScaledHeight() - 20, 150, 18);
        str = new GuiSearcher(4, this.mc.fontRendererObj, 10, sr.getScaledHeight() - 20, 150, 18);
        this.buttonList.add(plus = new GuiAltButton(0, this.width / 2 + 2, this.height / 2 + 35, 20, 10, "+"));
        this.buttonList.add(minus = new GuiAltButton(1, this.width / 2 + 2, this.height / 2 + 35, 20, 10, "-"));
        this.buttonList.add(files = new GuiAltButton(2, this.width / 2 + 2, this.height / 2 + 35, 20, 10, "Files"));
        EventInitGui e = new EventInitGui();
        EventManager.call(e);
        super.initGui();
    }
    

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	
    	   for (Component panel2 : components) {
           	if (mouseX < panel2.getX() + 100 && mouseX > panel2.getX() && mouseY < 240 && mouseY > 24) {
           		panel2.anim = (float) Interpolator.LINEAR.interpolate(panel2.anim, panel2.a, 0.3f);
            	if (panel2.anim > 11) {
            		panel2.anim = 10;
            	}
            	if (-((Panel)panel2).getHeightWithExpand() + 150 > panel2.anim) {
            		panel2.anim = -((Panel)panel2).getHeightWithExpand() + 150;
            	}
           		if (panel2.anim < 11) {
           			panel2.setY((int) panel2.anim);
           		}
           	}
    	   }
    	   
    	   for (Component panel : components) {
    		   if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
    			   panel.setX(panel.getX() - 5);
    		   if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
    			   panel.setX(panel.getX() + 5);
             }
    	   
    	   
        ScaledResolution sr = new ScaledResolution(mc);
        if (ClickGUI.blur.getBoolValue()) {
        	RenderUtils.drawBlur(15, () -> {
             //   RenderUtils.drawCircle(sr.getScaledWidth()/2, sr.getScaledHeight() / 2, anim * sr.getScaledWidth(), new Color(20, 20, 20, 190).getRGB());
        		   RenderUtils.drawSmoothRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(20, 20, 20, 190).getRGB());
        	});
        }
        
        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, (int) (mouseY));
        }
        updateMouseWheel(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        /*
        if (anim * 20 > 1) {
        	 if (ClickGUI.blur.getBoolValue()) {
             	RenderUtils.drawBlur((int) (anim * 20), () -> {
             		   RenderUtils.drawSmoothRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(20, 20, 20, 190).getRGB());
             	});
             }
        }*/
    }

    public void updateMouseWheel(int mouseX, int mouseY) {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
        	if (mouseX < panel.getX() + 100 && mouseX > panel.getX() && mouseY < 240 && mouseY > 24) {
        		if (scrollWheel > 0 && panel.getY() < 5) {
        			panel.a = panel.getY() + 15;
                }
        		if (scrollWheel < 0) {
        			panel.a = panel.getY() - 15;
                }
        	}
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	key = keyCode;
        if (ClickGuiScreen.str.getText().length() < 19 || keyCode == 14) {
        	   ClickGuiScreen.str.textboxKeyTyped(typedChar, keyCode);
        }
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse)
            super.keyTyped(typedChar, keyCode);
        search.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused())
            search.setFocused(!search.isFocused());
        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        search.mouseClicked(mouseX, mouseY, mouseButton);
        str.mouseClicked(mouseX, mouseY, mouseButton);
        for (Component component : components) {
        	selectedPanel = component;
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight && mouseY < 240 && mouseY > 24) {
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
        	if (mouseX > plus.xPosition && mouseY > plus.yPosition && mouseX < plus.xPosition) {
                  search.setText("");
        	}
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
    
    @Override
    public void onGuiClosed() {
    	a = 1;
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
