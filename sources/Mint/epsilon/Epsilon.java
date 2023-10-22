package epsilon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.Display;

import epsilon.ClickGUI.ClickGUI;
import epsilon.ClickGUI.dropdown.DropdownGUI;
import epsilon.altmanager.AltManager;
import epsilon.anticheat.PlayerData;
import epsilon.anticheat.TrackedPlayerManager;
import epsilon.botting.Core;
import epsilon.botting.cmds.EventCMD;
import epsilon.command.CommandManager;
import epsilon.events.Event;
import epsilon.events.listeners.EventChat;
import epsilon.events.listeners.EventKey;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.modules.combat.AntiBot;
import epsilon.modules.combat.ArmorBreaker;
import epsilon.modules.combat.AutoPot;
import epsilon.modules.combat.Criticals;
import epsilon.modules.combat.FastBow;
import epsilon.modules.combat.KillAura;
import epsilon.modules.combat.Reach;
import epsilon.modules.combat.TargetStrafe;
import epsilon.modules.combat.Velocity;
import epsilon.modules.dev.AntiCheat;
import epsilon.modules.dev.AntiLag;
import epsilon.modules.dev.C0Fuckery;
import epsilon.modules.dev.PacketCanceller;
import epsilon.modules.dev.PacketDebugger;
import epsilon.modules.dev.ServerLagCheck;
import epsilon.modules.dev.SilentS08;
import epsilon.modules.exploit.AntiNametag;
import epsilon.modules.exploit.AntiVoid;
import epsilon.modules.exploit.Blink;
import epsilon.modules.exploit.CrackedRaper;
import epsilon.modules.exploit.Crasher;
import epsilon.modules.exploit.Damage;
import epsilon.modules.exploit.Disabler;
import epsilon.modules.exploit.Godmode;
import epsilon.modules.exploit.MCTimer;
import epsilon.modules.exploit.PositionExploit;
import epsilon.modules.exploit.ResetVL;
import epsilon.modules.movement.Fly;
import epsilon.modules.movement.HighJump;
import epsilon.modules.movement.InvMove;
import epsilon.modules.movement.Jesus;
import epsilon.modules.movement.LagbackFly;
import epsilon.modules.movement.LongJump;
import epsilon.modules.movement.NewScaffold;
import epsilon.modules.movement.NoSlow;
import epsilon.modules.movement.PacketFly;
import epsilon.modules.movement.Rider;
import epsilon.modules.movement.Safewalk;
import epsilon.modules.movement.Speed;
import epsilon.modules.movement.Sprint;
import epsilon.modules.movement.Step;
import epsilon.modules.movement.Strafe;
import epsilon.modules.movement.WallClimb;
import epsilon.modules.player.ChestStealer;
import epsilon.modules.player.FastPlace;
import epsilon.modules.player.InvManager;
import epsilon.modules.player.NoFall;
import epsilon.modules.player.Spammer;
import epsilon.modules.render.AlienHat;
import epsilon.modules.render.Animations;
import epsilon.modules.render.ClickGui;
import epsilon.modules.render.Fullbright;
import epsilon.modules.render.SmallItem;
import epsilon.modules.render.SpinBot;
import epsilon.modules.render.Statistics;
import epsilon.modules.render.TabGUI;
import epsilon.modules.render.TargetEffects;
import epsilon.modules.render.TargetHUD;
import epsilon.modules.render.TargetLines;
import epsilon.modules.render.TargetLines2;
import epsilon.modules.render.Theme;
import epsilon.modules.render.Tracers;
import epsilon.modules.render.ViewBobbing;
import epsilon.modules.skyblock.AutoSellSkyblock;
import epsilon.modules.skyblock.SkyblockNuker;
import epsilon.modules.skyblock.SkyblockTest;
import epsilon.ui.HUD;
import epsilon.util.data.DataCollector;
import epsilon.util.file.CreateFile;
//import epsilon.util.DiscordRPCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;



public class Epsilon {
	public static Epsilon INSTANCE = new Epsilon();
	
	public static String name = "Mint", version = "B1";
	public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	public static HUD hud = new HUD();
	public static CommandManager commandManager = new CommandManager();
	public static Core core = new Core();
	public static AltManager altManager = new AltManager();
	public static TrackedPlayerManager tpm;
	public static PlayerData ac;

    //public static DiscordRPCUtil discordRp = new DiscordRPCUtil();
    
    public static ClickGUI clickGUI;
    public static DropdownGUI dropdown;


	public static String ip = null;
	public static String canonIP = null;

	public static int port = 25565;
    
	public static void saveClient() {
		
		
		
	}
    
	public static void startup() {
		
		clickGUI = new ClickGUI();
		dropdown = new DropdownGUI();
		tpm = new TrackedPlayerManager();
		CreateFile.initiateFolder();
		
		//Discord rpc
		//discordRp.start();
		
		System.out.println("Starting " + name + " " + version);
		Display.setTitle(name + " " + version);
		
		//Combat
		modules.add(new KillAura());
		modules.add(new AutoPot());
		modules.add(new Velocity());
		modules.add(new AntiBot());
		modules.add(new FastBow());
		modules.add(new TargetStrafe());
		modules.add(new ArmorBreaker());
		modules.add(new Reach());
		modules.add(new Criticals());
		

		//Movement
		modules.add(new Fly());
		modules.add(new Sprint());
		modules.add(new Speed());
		modules.add(new HighJump());
		modules.add(new LongJump());
		modules.add(new InvMove());
		modules.add(new NoSlow());
		modules.add(new PacketFly());
		modules.add(new Rider());
		modules.add(new LagbackFly());
		modules.add(new Strafe());
		modules.add(new Safewalk());
		modules.add(new WallClimb());
		modules.add(new Step());
		modules.add(new Jesus());
		
		//Visuals
		modules.add(new ClickGui());
		modules.add(new TargetHUD());
		modules.add(new Statistics());
		modules.add(new Fullbright());
		modules.add(new TabGUI());
		modules.add(new Animations());
		modules.add(new SmallItem());
		modules.add(new Tracers());
		modules.add(new Theme());
		modules.add(new ViewBobbing());
		modules.add(new TargetLines());
		modules.add(new TargetEffects());
		modules.add(new AlienHat());
		modules.add(new SpinBot());
		modules.add(new TargetLines2());
		
		//Exploit
		modules.add(new Disabler());
		modules.add(new PositionExploit());
		modules.add(new MCTimer());
		modules.add(new Crasher());
		modules.add(new SkyblockTest());
		modules.add(new AntiVoid());
		modules.add(new Godmode());
		modules.add(new Blink());
		modules.add(new ResetVL());
		modules.add(new AntiNametag());
		modules.add(new CrackedRaper());
		modules.add(new Damage());
		
		//Player
		modules.add(new InvManager());
		modules.add(new ChestStealer());
		modules.add(new FastPlace());
		modules.add(new NoFall());
		modules.add(new Spammer());
		modules.add(new NewScaffold());
		
		//Dev
		modules.add(new AntiCheat());
		modules.add(new ServerLagCheck());
		modules.add(new PacketDebugger());
		modules.add(new PacketCanceller());
		modules.add(new SkyblockNuker());
		modules.add(new SilentS08());
		modules.add(new C0Fuckery());
		modules.add(new AntiLag());
		modules.add(new AutoSellSkyblock());
		
		//modules.add(new Tracers());
		//modules.add(new Sumobot());
//		modules.add(new BreadCrumbs());
		//modules.add(new ESP());
		}
	
	
	/*public static DiscordRPCUtil getDiscordRP() {
		return discordRp;
	}*/
	
	public static void onEvent(Event e){

		DataCollector.onData(e);
		
		if(e instanceof EventChat) {
			commandManager.handleChat((EventChat)e);
		}
		
		if(e instanceof EventCMD) {
			core.handleChat((EventCMD)e);
		}
		
		if(e instanceof EventUpdate) {
			tpm.onUpd();
			
		}
		
		for(Module m : modules){
			if(!m.toggled)
				continue;
			
			m.onEvent(e);
		}
	}
	
	
	public static void keyPress (int key){
		Epsilon.onEvent(new EventKey(key));
		for (Module m : modules){
			if(m.getKey() == key){
				m.toggle();
				Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.3F + 0.7F));
			}
		}
	}
	
	public static void onUpdate() {
		for(Module m : modules) {
			m.onUpdate();
		}
	}
	
	
	
	
	public static List<Module> getModulesbyCategory(Category c){
		List<Module> modules = new ArrayList<Module>();
		
		for (Module m: Epsilon.modules) {
			if(m.category == c)
				modules.add(m);
		}
		
		return modules;
	}
	
	
	public static void addChatMessage(String message) {
		message = "§a" + name + "\2477: " + message;
		
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
	public static void addChatMessageWithHoverMsg(String message, String hoverMsg) {
		message = "§a" + name + "\2477: " + message;
		final ChatStyle hoverStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
                EnumChatFormatting.GRAY + hoverMsg.toString().replace("\n", "\n" + EnumChatFormatting.GRAY)
        )));
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Â§d" + name + "Â§7 Â» " + message).setChatStyle(hoverStyle));
		
	}
	
	public static void addChatMessage(int message) {
		String print = message + "";
		print = "§a" + name + "\2477: " + print;
		
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(print));
	}
	
	public static void addChatMessage(float message) {
		String print = message + "";
		print = "§a" + name + "\2477: " + print;
		
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(print));
	}
	
	public static void addChatMessage(double message) {
		String print = message + "";
		print = "§a" + name + "\2477: " + print;
		
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(print));
	}
	
	protected EntityPlayerSP player() {
		return Minecraft.getMinecraft().thePlayer;
	}
	protected PlayerControllerMP playerController() {
		return Minecraft.getMinecraft().playerController;
	}
	protected WorldClient world() {
		return Minecraft.getMinecraft().theWorld;
	}
	
	protected void sendPacket(Packet p) {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(p);
	}
	
	public static PlayerData getAC() {
		return ac;
	}



}
