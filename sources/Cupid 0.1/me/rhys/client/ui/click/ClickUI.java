package me.rhys.client.ui.click;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.rhys.base.Lite;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.SettingFactory;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.DoubleNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.FloatNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.ShortNumberSetting;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.button.CheckBox;
import me.rhys.base.ui.element.button.ImageButton;
import me.rhys.base.ui.element.label.Label;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.element.panel.ScrollPanel;
import me.rhys.base.ui.element.window.Window;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.render.ClickGui;
import me.rhys.client.module.render.HUD;
import me.rhys.client.ui.click.element.button.ModuleButton;
import me.rhys.client.ui.click.element.dropdown.EnumDropDown;
import me.rhys.client.ui.click.element.dropdown.ModeDropDown;
import me.rhys.client.ui.click.element.slider.DoubleSettingSlider;
import me.rhys.client.ui.click.element.slider.FloatSettingSlider;
import me.rhys.client.ui.click.element.slider.IntSettingSlider;
import me.rhys.client.ui.click.element.slider.ShortSettingSlider;
import net.minecraft.util.ResourceLocation;

public class ClickUI extends UIScreen {
  private Window window;
  
  private Panel selectionPanel;
  
  private Panel displayPanel;
  
  public ScrollPanel modulesPanel;
  
  private ScrollPanel settingsPanel;
  
  private Label displayLabel;
  
  public int moduleCurrent;
  
  private int current;
  
  public Window getWindow() {
    return this.window;
  }
  
  public Panel getSelectionPanel() {
    return this.selectionPanel;
  }
  
  public Panel getDisplayPanel() {
    return this.displayPanel;
  }
  
  public ScrollPanel getModulesPanel() {
    return this.modulesPanel;
  }
  
  public ScrollPanel getSettingsPanel() {
    return this.settingsPanel;
  }
  
  public Label getDisplayLabel() {
    return this.displayLabel;
  }
  
  public int getModuleCurrent() {
    return this.moduleCurrent;
  }
  
  public int getCurrent() {
    return this.current;
  }
  
  protected void init() {
    this.moduleCurrent = 0;
    int windowWidth = 450;
    int windowHeight = 280;
    this.current = 0;
    add((Element)(this.window = new Window(new Vec2f(this.width / 2.0F, this.height / 2.0F), windowWidth, windowHeight)));
    this.window.getPanel().setScreen(this);
    this.window.offset.sub(windowWidth / 2.0F, windowHeight / 2.0F);
    addSelectionPanel(windowHeight);
    addDisplayPanel();
    addModulesPanel();
    addSettingsPanel();
  }
  
  private void addModulesPanel() {
    this
      
      .modulesPanel = new ScrollPanel(new Vec2f(this.selectionPanel.getWidth(), this.displayPanel.getHeight()), 125, this.window.getHeight() - this.displayPanel.getHeight());
    this.modulesPanel
      .background = ColorUtil.darken(this.theme.windowColors.background, 25).getRGB();
    addModules();
    this.window.getPanel().add((Element)this.modulesPanel);
  }
  
  private void addDisplayPanel() {
    this
      .displayPanel = new Panel(new Vec2f(this.selectionPanel.getWidth(), 0.0F), 125, 35);
    this.displayPanel
      .background = ColorUtil.darken(this.theme.windowColors.background, 35).getRGB();
    this
      
      .displayLabel = new Label(Category.values()[this.current].name(), new Vec2f(this.displayPanel.getWidth() / 2.0F, this.displayPanel.getHeight() / 2.0F));
    this.displayLabel.setCentered(true);
    this.displayLabel.setScale(1.25F);
    this.displayPanel.add((Element)this.displayLabel);
    this.window.getPanel().add((Element)this.displayPanel);
  }
  
  private void addSelectionPanel(int windowHeight) {
    this.selectionPanel = new Panel(new Vec2f(0.0F, -3.0F), 75, windowHeight + 4);
    this.selectionPanel
      .background = ColorUtil.darken(this.theme.windowColors.background, 25).getRGB();
    Category[] categories = Category.values();
    int numOfElements = categories.length;
    int buttonHeight = this.selectionPanel.getHeight() / numOfElements;
    for (int i = 0; i < categories.length; i++) {
      Category category = categories[i];
      this.selectionPanel.add((Element)new ImageButton(new ResourceLocation("Lite/" + category
              
              .name().toLowerCase() + ".png"), new Vec2f(0.0F, (i * buttonHeight)), this.selectionPanel
            
            .getWidth(), buttonHeight, 20, 20));
    } 
    ((Element)this.selectionPanel.getContainer().get(0))
      .background = ColorUtil.darken(this.theme.windowColors.background, 45).getRGB();
    this.window.callback = this::canMoveWindow;
    this.window.getPanel().add((Element)this.selectionPanel);
  }
  
  private void addSettingsPanel() {
    float offset = (this.selectionPanel.getWidth() + this.modulesPanel.getWidth());
    this
      
      .settingsPanel = new ScrollPanel(new Vec2f(offset + 10.0F, 10.0F), (int)(this.window.getWidth() - offset) - 20, this.window.getHeight() - 20);
    this.settingsPanel.setItemMargin(8.0F);
    addSettings(((ModuleButton)this.modulesPanel.getContainer().get(this.moduleCurrent)).getModule());
    this.window.getPanel().add((Element)this.settingsPanel);
  }
  
  private void addModules() {
    this.modulesPanel.getContainer().getItems().clear();
    Category[] categories = Category.values();
    if (this.current < categories.length) {
      Category category = categories[this.current];
      if (category != null) {
        AtomicInteger index = new AtomicInteger(1);
        Lite.MODULE_FACTORY.filter(module -> module.getData().getCategory().equals(category)).forEach(module -> {
              ModuleButton moduleButton;
              this.modulesPanel.add((Element)(moduleButton = new ModuleButton(module, new Vec2f(0.0F, 0.0F), 125, 20)));
              if (index.getAndAdd(1) % 2 == 0)
                moduleButton.background = ColorUtil.darken(this.theme.windowColors.background, 30).getRGB(); 
            });
      } 
    } 
  }
  
  public void addSettings(Module module) {
    this.settingsPanel.setScrollAmount(0.0F);
    this.settingsPanel.getContainer().getItems().clear();
    if (module.getItems().size() > 0) {
      this.settingsPanel.add((Element)new Label("Mode", new Vec2f()));
      this.settingsPanel.add((Element)new ModeDropDown(module, new Vec2f(0.0F, 0.0F), 125, 20));
    } 
    ((SettingFactory.SettingContainer)Lite.SETTING_FACTORY.get(module)).settings.forEach(this::addSetting);
    if (module.getItems().size() > 0 && module.getCurrentMode() != null) {
      Map<ModuleMode<?>, List<Setting>> settingContainer = ((SettingFactory.SettingContainer)Lite.SETTING_FACTORY.get(module)).settingManager;
      if (settingContainer != null) {
        List<Setting> settings = settingContainer.get(module.getCurrentMode());
        if (settings != null && !settings.isEmpty())
          settings.forEach(this::addSetting); 
      } 
    } 
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (this.selectionPanel.isHovered(pos)) {
      this.selectionPanel.getContainer().forEach(element -> element.background = ColorUtil.Colors.TRANSPARENT.getColor());
      this.selectionPanel.getContainer().filter(element -> element.isHovered(pos)).findFirst().ifPresent(target -> {
            target.background = ColorUtil.darken(this.theme.windowColors.background, 45).getRGB();
            this.current = this.selectionPanel.getContainer().indexOf(target);
            Category[] categories = Category.values();
            if (this.current < categories.length) {
              Category category = categories[this.current];
              if (category != null) {
                this.displayLabel.setLabel(String.valueOf(category.name()));
                addModules();
                this.modulesPanel.setScrollAmount(0.0F);
                this.moduleCurrent = 0;
                addSettings(((ModuleButton)this.modulesPanel.getContainer().get(this.moduleCurrent)).getModule());
              } 
            } 
          });
    } else if (this.modulesPanel.isHovered(pos) && button == 1) {
      this.modulesPanel.getContainer().filter(element -> element.isHovered(pos.clone().add(0.0F, this.modulesPanel.getScrollAmount()))).findFirst().ifPresent(element -> {
            if (element instanceof ModuleButton) {
              ModuleButton moduleButton = (ModuleButton)element;
              this.moduleCurrent = this.modulesPanel.getContainer().indexOf(element);
              addSettings(moduleButton.getModule());
            } 
          });
    } 
  }
  
  private void addSetting(Setting setting) {
    if (setting instanceof IntNumberSetting) {
      this.settingsPanel.add((Element)new Label(setting.getName(), new Vec2f()));
      this.settingsPanel.add((Element)new IntSettingSlider((IntNumberSetting)setting, new Vec2f(), 100, 10));
    } 
    if (setting instanceof DoubleNumberSetting) {
      this.settingsPanel.add((Element)new Label(setting.getName(), new Vec2f()));
      this.settingsPanel.add((Element)new DoubleSettingSlider((DoubleNumberSetting)setting, new Vec2f(), 100, 10));
    } 
    if (setting instanceof FloatNumberSetting) {
      this.settingsPanel.add((Element)new Label(setting.getName(), new Vec2f()));
      this.settingsPanel.add((Element)new FloatSettingSlider((FloatNumberSetting)setting, new Vec2f(), 100, 10));
    } 
    if (setting instanceof ShortNumberSetting) {
      this.settingsPanel.add((Element)new Label(setting.getName(), new Vec2f()));
      this.settingsPanel.add((Element)new ShortSettingSlider((ShortNumberSetting)setting, new Vec2f(), 100, 10));
    } 
    if (setting instanceof BooleanSetting) {
      BooleanSetting booleanSetting = (BooleanSetting)setting;
      CheckBox box = new CheckBox(setting.getName(), new Vec2f(), 8, 8);
      box.setChecked(booleanSetting.get());
      box.setCallback(mouse -> {
            booleanSetting.set(!booleanSetting.get());
            return booleanSetting.get();
          });
      this.settingsPanel.add((Element)box);
    } 
    if (setting instanceof EnumSetting) {
      EnumSetting enumSetting = (EnumSetting)setting;
      this.settingsPanel.add((Element)new Label(enumSetting.getName(), new Vec2f()));
      this.settingsPanel.add((Element)new EnumDropDown(enumSetting, new Vec2f(), 125, 20));
    } 
  }
  
  private boolean canMoveWindow(Vec2f mouse) {
    if (this.settingsPanel.getContainer().filter(element -> element.isHovered(mouse.clone().add(0.0F, this.settingsPanel.getScrollAmount()))).findFirst().orElse(null) != null || this.modulesPanel
      .getContainer().filter(element -> element.isHovered(mouse)).findFirst().orElse(null) != null)
      return false; 
    return !this.selectionPanel.isHovered(mouse);
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    HUD hud = (HUD)Lite.MODULE_FACTORY.findByClass(HUD.class);
    RenderUtil.drawRect(this.selectionPanel.pos.clone().sub(2.0F, 0.0F), 2, this.selectionPanel.getHeight(), hud
        .getColor(0, 1));
    RenderUtil.drawRect(this.displayPanel.pos.clone().add(0, this.displayPanel.getHeight() - 1), this.displayPanel.getWidth(), 1, 
        ColorUtil.darken(this.displayPanel.background, 15).getRGB());
  }
  
  public void onGuiClosed() {
    Lite.FILE_FACTORY.save();
    Lite.MODULE_FACTORY.findByClass(ClickGui.class).toggle(false);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\ClickUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */