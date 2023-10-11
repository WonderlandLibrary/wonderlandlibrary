package artemis;

import artemis.Tab.TabGui;
import artemis.altmanager.AltManager;
import artemis.command.CommandManager;
import artemis.mods.Combat.*;
import artemis.mods.Movement.*;
import artemis.mods.Exploit.*;
import artemis.mods.Minigames.*;
import artemis.mods.Other.*;
import artemis.mods.Render.*;
import artemis.mods.World.*;
import artemis.mods.Player.*;


import artemis.settings.SettingsManager;
//import artemis.tabgui.TabGui;
import artemis.ui.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

public class Artemis {
	
  public static String Name = "Artemis";
  
  public static String version = "1.0";
  
  public static String Creator = "ScroodMinecraft9";
  
  public boolean isStarting = false;
  
  public boolean target = false;
  
  public boolean blockanimation = false;
  
  public boolean slap = false;
  
  public boolean normal = true;
  
  public static HUD hud = new HUD();
  //public static TabGui tabgui = new TabGui();
 // public static HUD2 hud2 = new HUD2();
  public static TargetHUD stats = new TargetHUD();
  public static ArmorStats armorStats = new ArmorStats();
  public static Nametags nametags = new Nametags();
  public static void stratup() {
    Display.setTitle(String.valueOf(Name) + " v" + version);
    
  }
  public static SettingsManager settingsManager;
  public static ModuleManager moduleManager;
  public EventManager eventManager;
  public static FileManager filemanager;
  public static Discord discord;
  public static FriendManager f;
  
  private static ArrayList<module> mods;
  private static CommandManager cmdManager;
  
  public SaveLoad saveLoad = new SaveLoad();
  

  
  
  public static Artemis instance = new Artemis();

public static AltManager altManager;
  public final TabGui TAB = new TabGui();
//booleans
public boolean nameprotect = false;

public boolean reach = false;

public boolean tabgui = false;
public boolean xray = false;
  
  public Artemis() {
	  
	  isStarting = true;
	   discord = new Discord();
	   filemanager = new FileManager();
	 settingsManager = new SettingsManager();
	 eventManager = new EventManager();
	 moduleManager = new ModuleManager();
	(mods = new ArrayList<module>()).clear();
	 cmdManager = new CommandManager();
	 f = new FriendManager();
	 XrayUtils.initXRayBlocks();
	 
	 filemanager.loadConfigs(filemanager.accountsConfig);

	 
	 //Movement
	 addMod((module)new Fly());
	 addMod((module)new Sprint());
	 //Player
	 addMod((module)new Nofall());
	 //Render
	 addMod((module)new Fullbright());
	 addMod((module)new Sneak());
	 addMod((module)new Hypixelfly());
	 addMod((module)new BlockAnimation());
	 addMod((module)new TabGUI());
	 addMod((module)new Xray());
	 //Combat
	 addMod((module)new Killaura());
	 addMod((module)new KillAura22());
	 addMod((module)new ClickGUI());
	 addMod((module)new Wtap());
	 addMod((module)new Aimbot());
	 addMod((module)new TargetStrafe());
	 addMod((module)new ItemPhysics());
	 //Other
	 addMod((module)new ChestStealer());
	 addMod((module)new AutoClicker());
	 addMod((module)new PlayerWarner());
	 addMod((module)new Step());
	 addMod((module)new Antibot());
	 addMod((module)new AutoArmor());
	 //Exploit
	 addMod((module)new ServerCrasher());
	 addMod((module)new SpartanGlide());
	 addMod((module)new Bhop());
	 addMod((module)new AutoPotion());
	 //Minigame
	 addMod((module)new MurderMystery());
	 addMod((module)new Dolphin());
	 addMod((module)new FastPlace());
	 addMod((module)new SpeedMine());
	 addMod((module)new NoSlow());
	 addMod((module)new InvManager());
	 addMod((module)new SlimeJump());
	 addMod((module)new FastLadder());
	 addMod((module)new ShaderESP());
	 addMod((module)new HighJump());
	 addMod((module)new Speed());
	 addMod((module)new Phase());
	 addMod((module)new LongJump());
	 addMod((module)new Criticals());
	 addMod((module)new AutoGap());
	 addMod((module)new Spammer());
	 addMod((module)new Velocity());
	 addMod((module)new Eagle());
	 addMod((module)new TPaura());
	 addMod((module)new TargetHUD());
	 addMod((module)new Cubecraftfly());
	 addMod((module)new Nametags());
	 addMod((module)new Jargon());
	 addMod((module)new ArmorStats());
	 addMod((module)new InstantRespawn());
	 addMod((module)new BedGodMode());
	 addMod((module)new InventoryMove());
	 //World
	 addMod((module)new Tower());
	 addMod((module)new NameProtect());
	// addMod((module)new CubecraftLongJump());
	 addMod((module)new BedBreaker());
	 addMod((module)new ESP());
	 addMod((module)new ChestESP());
	 addMod((module)new ItemESP());
	 addMod((module)new GodMode());
	 addMod((module)new Reach());
	 addMod((module)new PingSpoof());
	 addMod((module)new Scaffold());
	 addMod((module)new NoWeb());
	 addMod((module)new TNTBlock());
	 addMod((module)new BugUp());
	 addMod((module)new CakeEater());
	 //eventManager.register(this);
	 
  }
  public void stopArtemis() {
      eventManager.unregister(this);
  }
  
  public static void addMod(module M) {
    mods.add(M);
  }
  
  public static ArrayList<module> getModules() {
    return mods;
  }
  
  public static void onUpdate() {
    for (module m : mods)
      m.onUpdate(); 
  }
  
  public static void onAttack() {
	    for (module m : mods)
	      m.onAttack();
	  }
	  
  
  public static void onRender() {
    for (module m : mods)
      m.onRender(); 
  }
  
  public static void onEvent(Event e) {
	    for (module m : mods) {
	    	if(!m.isToggled())
	    		continue;
	      m.onEvent(e); 
	  }
  }
	  
public module getModule(String name) {
		for (module m : this.mods) {
			//System.out.println("getModule works!!!");
			if (m.getName() == name) {
				//System.out.println("getModule works even more!!!");
				//System.out.println(m);
				return m;
			}
		}
		return null;
	}

  
  public static void onKeyPressed(int k) {
	    for (module m : mods) {
	      if (m.getKey() == k)
	        m.toggle(); 
	    } 
	  }
	public static void addChatMessage(String s){
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[§eArtemis§f] " + s));
	}

	public static boolean onSendChatMessage(String s){//EntityPlayerSP
		if(s.startsWith(".")){
			cmdManager.callCommand(s.substring(1));
			return false;
		}
		for(module m: getModules()){
			if(m.isToggled()){
				return m.onSendChatMessage(s);
			}
		}
		return true;
	}

	public static boolean onRecieveChatMessage(S02PacketChat packet){
		for(module m: getModules()){
			if(m.isToggled()){
				return m.onRecieveChatMessage(packet);
			}
		}
		return true;
	}
	}
