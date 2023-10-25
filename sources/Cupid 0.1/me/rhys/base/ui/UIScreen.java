package me.rhys.base.ui;

import java.io.IOException;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.ui.theme.Theme;
import me.rhys.base.ui.theme.impl.DarkTheme;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class UIScreen extends GuiScreen implements UIElement {
  protected final Panel panel = new Panel(new Vec2f(), 0, 0);
  
  protected final MovementProcessor movementProcessor = new MovementProcessor();
  
  public final Theme theme = (Theme)new DarkTheme();
  
  private Popup popup = null;
  
  public Popup getPopup() {
    return this.popup;
  }
  
  public void add(Element element) {
    this.panel.add(element);
  }
  
  public void remove(Element element) {
    this.panel.remove(element);
  }
  
  public void updateScreen() {}
  
  public void initGui() {
    this.movementProcessor.setParent(this.panel);
    if (this.panel.getContainer().isEmpty())
      init(); 
  }
  
  protected void init() {}
  
  protected void preDraw(Vec2f mouse, float partialTicks) {}
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    Vec2f mouse = getMousePosition();
    if (this.popup != null) {
      this.popup.clickMouse(mouse, mouseButton);
    } else {
      this.movementProcessor.clickMouse(mouse, mouseButton);
      this.panel.clickMouse(mouse, mouseButton);
      clickMouse(mouse, mouseButton);
    } 
  }
  
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    Vec2f mouse = getMousePosition();
    if (this.popup != null) {
      this.popup.dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
    } else {
      this.panel.dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
      dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
    } 
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    Vec2f mouse = getMousePosition();
    if (this.popup != null) {
      this.popup.releaseMouse(mouse, state);
    } else {
      this.movementProcessor.releaseMouse(mouse, state);
      this.panel.releaseMouse(mouse, state);
      releaseMouse(mouse, state);
    } 
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.popup != null) {
      if (keyCode == 1) {
        displayPopup((Popup)null);
      } else {
        this.popup.typeKey(typedChar, keyCode);
      } 
    } else {
      if (keyCode == 1) {
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null)
          this.mc.setIngameFocus(); 
      } 
      this.panel.typeKey(typedChar, keyCode);
      typeKey(typedChar, keyCode);
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    GlStateManager.pushMatrix();
    ScaledResolution resolution;
    this.mc.entityRenderer.setupOverlayRendering(resolution = new ScaledResolution(this.mc, getScale()));
    this.width = resolution.getScaledWidth();
    this.height = resolution.getScaledHeight();
    this.panel.setParent(null);
    this.panel.setScreen(this);
    Vec2f mouse = getMousePosition();
    this.movementProcessor.updatePositions(mouse);
    preDraw(mouse, partialTicks);
    this.panel._draw(mouse, partialTicks);
    draw(mouse, partialTicks);
    if (this.popup != null) {
      this.popup.offset = new Vec2f((resolution.getScaledWidth() - this.popup.getWidth()) / 2.0F, (resolution.getScaledHeight() - this.popup.getHeight()) / 2.0F);
      this.popup._draw(mouse, partialTicks);
    } 
    this.mc.entityRenderer.setupOverlayRendering();
    GlStateManager.popMatrix();
  }
  
  public void displayPopup(Popup popup) {
    if (this.popup != null)
      this.popup.onHide(); 
    this.popup = popup;
    if (this.popup != null) {
      this.popup.setScreen(this);
      this.popup.onShow();
    } 
  }
  
  protected Vec2f getMousePosition() {
    ScaledResolution resolution = getResolution();
    return new Vec2f((Mouse.getX() * resolution.getScaledWidth()) / this.mc.displayWidth, resolution.getScaledHeight() - (Mouse.getY() * resolution.getScaledHeight()) / this.mc.displayHeight - 1.0F);
  }
  
  public ScaledResolution getResolution() {
    return new ScaledResolution(this.mc, getScale());
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
  
  public int getScale() {
    return 2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\UIScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */