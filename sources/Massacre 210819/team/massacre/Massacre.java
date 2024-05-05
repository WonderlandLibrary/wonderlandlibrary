package team.massacre;

import org.lwjgl.opengl.Display;
import team.massacre.api.command.CommandManager;
import team.massacre.api.event.Event;
import team.massacre.api.event.api.bus.Bus;
import team.massacre.api.event.api.bus.BusImpl;
import team.massacre.api.manager.ConfigManager;
import team.massacre.api.manager.FontManager;
import team.massacre.api.manager.ModuleManager;
import team.massacre.utils.ColorUtil;
import team.massacre.utils.HudUtils;

public enum Massacre {
   INSTANCE;

   public String clientName = "Massacre";
   public String clientBuild = "210819";
   public String bloodyHell = "";
   private ModuleManager moduleManager;
   private ConfigManager configManager;
   private Bus<Event> eventapi;
   private CommandManager commandManager;
   private FontManager fontManager;
   private HudUtils hudUtils;
   private ColorUtil colorUtil;

   public void startGame() {
      this.eventapi = new BusImpl();
      this.fontManager = new FontManager();
      this.colorUtil = new ColorUtil();
      this.hudUtils = new HudUtils();
      this.configManager = new ConfigManager();
      this.moduleManager = new ModuleManager();
      this.commandManager = new CommandManager();
      Display.setTitle("Massacre");
   }

   public HudUtils getHudUtils() {
      return this.hudUtils;
   }

   public ColorUtil getColorUtil() {
      return this.colorUtil;
   }

   public Bus getEventManager() {
      return this.eventapi;
   }

   public FontManager getFontManager() {
      return this.fontManager;
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public String getClientName() {
      return this.clientName;
   }

   public void setClientName(String clientName) {
      this.clientName = clientName;
   }
}
