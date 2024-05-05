package me.aidanmees.trivia.client.tools;

import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.aidanmees.trivia.client.csgogui.csgogui.util.CSGuiMod;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Combat.Aimbot;
import me.aidanmees.trivia.client.modules.Combat.AntiBot;
import me.aidanmees.trivia.client.modules.Combat.AntiVelocity;
import me.aidanmees.trivia.client.modules.Combat.ArmorBreaker;
import me.aidanmees.trivia.client.modules.Combat.AutoArmor;
import me.aidanmees.trivia.client.modules.Combat.AutoPotion;
import me.aidanmees.trivia.client.modules.Combat.AutoSoup;
import me.aidanmees.trivia.client.modules.Combat.BowAimbot;
import me.aidanmees.trivia.client.modules.Combat.Criticals;
import me.aidanmees.trivia.client.modules.Combat.FastBow;
import me.aidanmees.trivia.client.modules.Combat.HvHAura;
import me.aidanmees.trivia.client.modules.Combat.InfiniteReach;
import me.aidanmees.trivia.client.modules.Combat.KillauraBETA;
import me.aidanmees.trivia.client.modules.Combat.ReachAura;
import me.aidanmees.trivia.client.modules.Combat.WTap;
import me.aidanmees.trivia.client.modules.Exploits.BeaconExploit;
import me.aidanmees.trivia.client.modules.Exploits.ClickTeleport;
import me.aidanmees.trivia.client.modules.Exploits.ConsoleSpammer;
import me.aidanmees.trivia.client.modules.Exploits.HologramSpam;
import me.aidanmees.trivia.client.modules.Exploits.Magnet;
import me.aidanmees.trivia.client.modules.Exploits.Phase;
import me.aidanmees.trivia.client.modules.Exploits.ServerCrasher;
import me.aidanmees.trivia.client.modules.Hidden.AAC;
import me.aidanmees.trivia.client.modules.Hidden.Guardian;
import me.aidanmees.trivia.client.modules.Hidden.Gwen;
import me.aidanmees.trivia.client.modules.Hidden.HackerDetect;
import me.aidanmees.trivia.client.modules.Hidden.HackerDetectGUI;
import me.aidanmees.trivia.client.modules.Hidden.MiddleClickFriends;
//import me.aidanmees.trivia.client.modules.Hidden.Music;
import me.aidanmees.trivia.client.modules.Hidden.NCP;
import me.aidanmees.trivia.client.modules.Hidden.Notifications;
import me.aidanmees.trivia.client.modules.Hidden.Ping;
import me.aidanmees.trivia.client.modules.Hidden.Watchdog;
import me.aidanmees.trivia.client.modules.Legit.AutoClicker;
import me.aidanmees.trivia.client.modules.Legit.AutoFNS;
import me.aidanmees.trivia.client.modules.Legit.AutoMLG;
import me.aidanmees.trivia.client.modules.Legit.AutoRod;
import me.aidanmees.trivia.client.modules.Legit.HitBox;
//import me.aidanmees.trivia.client.modules.Legit.MisplaceReach;
import me.aidanmees.trivia.client.modules.Legit.Reach;
import me.aidanmees.trivia.client.modules.Misc.AntiFire;
import me.aidanmees.trivia.client.modules.Misc.AntiHunger;
import me.aidanmees.trivia.client.modules.Misc.Blink;
import me.aidanmees.trivia.client.modules.Misc.DialUpPing;
import me.aidanmees.trivia.client.modules.Misc.HeadChanger;
import me.aidanmees.trivia.client.modules.Misc.NBTViewer;
import me.aidanmees.trivia.client.modules.Misc.NoRotate;
import me.aidanmees.trivia.client.modules.Misc.Spammer;
import me.aidanmees.trivia.client.modules.Movement.AirJump;
import me.aidanmees.trivia.client.modules.Movement.Flight;
import me.aidanmees.trivia.client.modules.Movement.HighJump;
import me.aidanmees.trivia.client.modules.Movement.InventoryMove;
import me.aidanmees.trivia.client.modules.Movement.LongJump;
import me.aidanmees.trivia.client.modules.Movement.NoSlowdown;
import me.aidanmees.trivia.client.modules.Movement.Speed;
import me.aidanmees.trivia.client.modules.Movement.Spider;
import me.aidanmees.trivia.client.modules.Movement.Sprint;
import me.aidanmees.trivia.client.modules.Movement.Step;
import me.aidanmees.trivia.client.modules.Player.AutoEat;
import me.aidanmees.trivia.client.modules.Player.AutoMine;
import me.aidanmees.trivia.client.modules.Player.AutoRespawn;
import me.aidanmees.trivia.client.modules.Player.FakeHacker;
import me.aidanmees.trivia.client.modules.Player.FastPlace;
import me.aidanmees.trivia.client.modules.Player.FastUse;
import me.aidanmees.trivia.client.modules.Player.Haste;
import me.aidanmees.trivia.client.modules.Player.InvCleaner;
import me.aidanmees.trivia.client.modules.Player.NoClip;
import me.aidanmees.trivia.client.modules.Player.NoFall;
import me.aidanmees.trivia.client.modules.Player.NoSwing;
import me.aidanmees.trivia.client.modules.Player.Parkour;
import me.aidanmees.trivia.client.modules.Player.Regen;
import me.aidanmees.trivia.client.modules.Render.AntiCopyright;
import me.aidanmees.trivia.client.modules.Render.ClickGUI;
import me.aidanmees.trivia.client.modules.Render.ESP;
import me.aidanmees.trivia.client.modules.Render.Freecam;
import me.aidanmees.trivia.client.modules.Render.Fullbright;
import me.aidanmees.trivia.client.modules.Render.NameTags;
import me.aidanmees.trivia.client.modules.Render.NoRender;
import me.aidanmees.trivia.client.modules.Render.SignSploit;
import me.aidanmees.trivia.client.modules.Render.SkinProtect;
import me.aidanmees.trivia.client.modules.Render.Tracers;
import me.aidanmees.trivia.client.modules.Render.XRay;
import me.aidanmees.trivia.client.modules.World.CakeNuker;
import me.aidanmees.trivia.client.modules.World.ChestStealer;
import me.aidanmees.trivia.client.modules.World.Jesus;
import me.aidanmees.trivia.client.modules.World.SafeWalk;
import me.aidanmees.trivia.client.modules.World.Scaffold;
import me.aidanmees.trivia.gui.GuitriviaAlert;
import me.aidanmees.trivia.gui.GuitriviaUpdate;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;

public class LoadTools {

	private static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

	public static CopyOnWriteArrayList<Module> addModules() {
		new me.aidanmees.trivia.CreaTab.TabExploits();
		new me.aidanmees.trivia.CreaTab.TabArmor();
		new me.aidanmees.trivia.CreaTab.TabSword();
		// appendModule(new Admins());
		//appendModule(new AreaMine());
		
		
		//Anticheats
		appendModule(new AAC());
		
		appendModule(new Guardian());
		appendModule(new Watchdog());
		appendModule(new Gwen());
		appendModule(new NCP());
		//Anticheats

;
		
		appendModule(new AntiBot());
		appendModule(new AutoArmor());
		appendModule(new HvHAura());
		
		appendModule(new ConsoleSpammer());
		appendModule(new CSGuiMod());
		appendModule(new AutoClicker());
		appendModule(new KillauraBETA());
		appendModule(new Scaffold());
		
		appendModule(new CakeNuker());
	
		// appendModule(new AutoBuild());
		appendModule(new Sprint());
		appendModule(new DialUpPing());
		appendModule(new AutoFNS());
		appendModule(new AutoMLG());
		appendModule(new AutoPotion());
		
		appendModule(new AutoSoup());
		appendModule(new AutoRod());
		appendModule(new AutoMine());
		appendModule(new AirJump());
		appendModule(new Aimbot());
		// appendModule(new AntiDrown());
		appendModule(new AntiFire());
		appendModule(new HologramSpam());
		
		appendModule(new AntiHunger());
		appendModule(new AutoEat());
		
		//appendModule(new AntiStacker());
		appendModule(new NoRotate());
		appendModule(new SignSploit());
	
		appendModule(new AutoRespawn());
		
		//appendModule(new AutoStacker());
		// appendModule(new AutoParkour());
		appendModule(new ArmorBreaker());

		// appendModule(new Bettersprint());
		//appendModule(new Bleach());
		appendModule(new BeaconExploit());
	
		appendModule(new Blink());

		//appendModule(new BunnyHop());
		appendModule(new BowAimbot());
		//appendModule(new Hotbar());
		appendModule(new ChestStealer());
		appendModule(new ClickGUI());
		appendModule(new ClickTeleport());
		// appendModule(new ChatSpam());
		
		appendModule(new Criticals());
		appendModule(new HeadChanger());

		// appendModule(new DialUpPing());
		
		//appendModule(new Decimator());
		
		appendModule(new ESP());

		appendModule(new Reach());

		appendModule(new FastBow());
		appendModule(new FastUse());

		appendModule(new Flight());
		appendModule(new Fullbright());

		appendModule(new Freecam());
		appendModule(new FakeHacker());

		// Ghost mode was here


		appendModule(new HackerDetect());
		appendModule(new HackerDetectGUI());
		appendModule(new Haste());
		appendModule(new HighJump());
		appendModule(new HitBox());
		
		//appendModule(new Imitate());
		
	

		appendModule(new InventoryMove());
		appendModule(new InvCleaner());
		
		// appendModule(new I_LIEK_FOOD());


		//appendModule(new KeyStrokeMod());

	
		appendModule(new AntiVelocity());

		// <Target>

		// appendModule(new HurtResistant());

		// </Target>

		//appendModule(new LagSign());
		appendModule(new LongJump());

		//appendModule(new Magnet());
		appendModule(new Magnet());

		appendModule(new MiddleClickFriends());
		//appendModule(new MisplaceReach());

		//appendModule(new NameTag());
		appendModule(new NameTags());
		appendModule(new NBTViewer());

		appendModule(new NoClip());
		appendModule(new NoFall());
		appendModule(new NoRender());
		
		appendModule(new FastPlace());

		appendModule(new NoSwing());
		appendModule(new NoSlowdown());
		appendModule(new Notifications());

		//appendModule(new Nuker());
		
		
		//appendModule(new OPSign());

		appendModule(new Parkour());

		appendModule(new Phase());
		appendModule(new Ping());

		//appendModule(new RangeSpheres());
		///appendModule(new Radar());
		//appendModule(new RainbowArmor());
		appendModule(new ReachAura());
		appendModule(new AntiCopyright());
		appendModule(new Regen());
		


		appendModule(new ServerCrasher());
		//appendModule(new Shaky());
		appendModule(new SafeWalk());

		/*appendModule(new SuperHit());*/
		
		appendModule(new Step());
		//appendModule(new SkinBlinker());
		appendModule(new SkinProtect());
		appendModule(new Spammer());
		appendModule(new Speed());
		
		//appendModule(new Spin());
		appendModule(new Spider());
		appendModule(new Jesus());

		//appendModule(new SneakyAssasins());

		
	//appendModule(new Teleport());
		//appendModule(new Timer());
		//appendModule(new TpEggBreaker());

		appendModule(new Tracers());

		appendModule(new InfiniteReach());

		// appendModule(new TypeWars());
		
		//appendModule(new VPhase());

		appendModule(new XRay());

		// appendModule(new WallHack());
		// appendModule(new WatchdogDetect());

		appendModule(new WTap());
		//appendModule(new WaterFart());
		
		// appendModule(new Zoom());
		
		return modules;
	}
	public static void clearModule(){
		
		modules.clear();
		
	}

	public static void appendModule(Module module) {
		// TODO Module autotoggle
		
		modules.add(module);
		
	}

	public static Runnable serverInfoFetcher = new Runnable() {
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("http://triviaclient.weebly.com/msg.html");
				Document doc = conn.get();
				Element mouseNamesElmt = doc.getElementById("triviaMouseNames");
				Element tagNamesElmt = doc.getElementById("triviaTagNames");
				Element devTagNamesElmt = doc.getElementById("triviaNames");
				Element versionElmt = doc.getElementById("triviaVersion");
				Element motdElmt = doc.getElementById("triviaMOTD");
				String[] mouseNames = mouseNamesElmt.html().split(";");
				String[] tagNames = tagNamesElmt.html().split(";");
				String[] devTags = devTagNamesElmt.html().split("%");
				//System.out.println(devTagNamesElmt.html());
				for (int i = 0; i < mouseNames.length; i++) {
					
					trivia.mouseNames.add(mouseNames[i]);
				}
				for (int i = 0; i < tagNames.length; i++) {
					
					trivia.nameTagNames.add(tagNames[i]);
				}
				for (int i = 0; i < devTags.length; i++) {
					//System.out.println(devTags[i].split(":")[0].replaceAll("&amp;", "§"));
					//System.out.println(devTags[i].split(":")[1].replaceAll("&amp;", "§"));
					trivia.devTagNames.put(devTags[i].split(":")[0].replaceAll("&amp;", "§"), devTags[i].split(":")[1].replaceAll("&amp;", "§"));
				}
				String version = versionElmt.html();
				String motd = motdElmt.html();
				trivia.motd = motd.replaceAll("&amp;", "§");
				trivia.serverVersion = version;
				// System.err.println("Server version:" + trivia.serverVersion);
				// System.err.println("Client version:" +
				// trivia.getClientVersion());
				if (!trivia.serverVersion.trim().equals(trivia.getClientVersion()) && !trivia.promtedUpdate) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							trivia.promtedUpdate = true;
							Minecraft.getMinecraft().displayGuiScreen(new GuitriviaUpdate());
						}
					});
				} else {
					System.err.println("Client version up to date!");
					trivia.triedConnectToUpdate = true;
				}
				conn = Jsoup.connect("http://triviaclient.weebly.com/changelog.html");
				try {
					doc = conn.get();
					Element wsite_content = doc.getElementById("wsite-content");
					if (wsite_content != null) {
						for (Element el : wsite_content.children()) {
							//System.out.println(el.text());
						}
						Elements changeLinesElmts = wsite_content.child(0).child(0).child(0).child(0).child(0).child(2).child(0)
								.children();
						//System.out.println("SDADS" + changeLinesElmts.get(0).attributes().toString());
						trivia.changeLineElmts = changeLinesElmts;
					} else {
						
						trivia.changelogFailed = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					trivia.changelogFailed = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get information from the server!");
				trivia.triedConnectToUpdate = true;
			}
		}
	};
	public static Runnable alertInfoFetcher = new Runnable() {
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("http://triviaclient.weebly.com/msg.html");
				conn.timeout(0);
				Document doc = conn.get();
				Element alertElmt = doc.getElementById("triviaAlert");
				final String alert = alertElmt.html();
				if (!alert.trim().isEmpty()) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							trivia.promtedAlert = true;
							Minecraft.getMinecraft().displayGuiScreen(new GuitriviaAlert(alert));
						}
					});
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get alert info from the server!");
			}
		}
	};
	public static void doAntiLeak() {
		

}
}
