package me.robbanrobbin.jigsaw.client.tools;

import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.*;
import me.robbanrobbin.jigsaw.client.modules.target.AntiBot;
import me.robbanrobbin.jigsaw.client.modules.target.Friends;
import me.robbanrobbin.jigsaw.client.modules.target.Invisible;
import me.robbanrobbin.jigsaw.client.modules.target.NoArmor;
import me.robbanrobbin.jigsaw.client.modules.target.NoArmor2;
import me.robbanrobbin.jigsaw.client.modules.target.NonPlayers;
import me.robbanrobbin.jigsaw.client.modules.target.Players;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import me.robbanrobbin.jigsaw.gui.GuiJigsawAlert;
import me.robbanrobbin.jigsaw.gui.GuiJigsawUpdate;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

public class LoadTools {

	private static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

	public static CopyOnWriteArrayList<Module> addModules() {
		
		appendModule(new AreaMine());
		appendModule(new ArrowView());
//		appendModule(new Animations());
//		appendModule(new AutoArmor());
		appendModule(new AutoBlock());
		appendModule(new AutoClicker());
		appendModule(new AutoSprint());
		appendModule(new AutoPotion());
		appendModule(new AutoSoup());
		appendModule(new AutoRod());
		appendModule(new AirJump());
		appendModule(new Aimbot());
//		appendModule(new AntiFire());
//		appendModule(new AntiHunger());
		appendModule(new AutoEat());
		appendModule(new AntiStacker());
		appendModule(new AntiRotate());
		appendModule(new AntiWeb());
		appendModule(new AutoRespawn());
		appendModule(new AutoJump());
		appendModule(new AutoWalk());
		appendModule(new AutoSneak());
		appendModule(new AutoStacker());

		appendModule(new BedBreaker());
		appendModule(new Bleach());
		appendModule(new Blink());
		appendModule(new BlurBufferMod());
		appendModule(new BowAimbot());
		appendModule(new BowFly());
		appendModule(new BowPush());
		appendModule(new BunnyHop());
		appendModule(new Breadcrumbs());
		
		appendModule(new ChestStealer());
		appendModule(new ClickGUI());
		appendModule(new ClickSpeed());
		appendModule(new ClickTeleport());
		appendModule(new Console());
		appendModule(new Coords());
//		appendModule(new CopsNCrims());
		appendModule(new Criticals());
		
		appendModule(new Decimator());
		appendModule(new DJ());
		appendModule(new DoSMod());

		appendModule(new EggBreaker());
		appendModule(new ElytraHack());
		appendModule(new ESP());
		appendModule(new ExtendedReach());

		appendModule(new FakeHackers());
		appendModule(new FastFall());
		appendModule(new FastPlace());
		appendModule(new FastSneak());
		appendModule(new Flight());
		appendModule(new FPS());
		appendModule(new Freecam());
		appendModule(new Fullbright());

		// Ghost mode was here
//		appendModule(new GhostAura());
		appendModule(new GodMode());

		appendModule(new HackerDetect());
		appendModule(new HackerDetectGUI());
		appendModule(new Headless());
		
		appendModule(new InventoryPlus());
		appendModule(new InventoryMove());
		
		appendModule(new Jesus());

		appendModule(new KeepSprint());
		appendModule(new KillAura());
		appendModule(new Knockback());

//		// <Target>
		appendModule(new AntiBot());
		appendModule(new Friends());
		appendModule(new NonPlayers());
		appendModule(new Players());
		appendModule(new Invisible());
		appendModule(new Team());
//		appendModule(new NoArmor());
		appendModule(new NoArmor2());
//		// </Target>
		
		appendModule(new LessLag());
		appendModule(new LiquidInteract());
		appendModule(new LongJump());

		appendModule(new MiddleClickFriends());
		appendModule(new MinerBot());
		appendModule(new Minimap());
		appendModule(new MobArena());
		appendModule(new MultiUse());

		appendModule(new NameProtect());
		appendModule(new NameTags());
		appendModule(new NoBreakDelay());
		appendModule(new NoClip());
		appendModule(new NoFall());
		appendModule(new NoHurtcam());
		appendModule(new NoSlowdown());
		appendModule(new NoSwing());
		appendModule(new Notifications());
		appendModule(new Nuker());
		
//		appendModule(new OpFightbot());

		appendModule(new Panic());
		appendModule(new Parkour());
		appendModule(new PerfectHorseJump());
//		appendModule(new Phase());
		appendModule(new Ping());
		appendModule(new Projectiles());

		appendModule(new RodAimbot());

		appendModule(new ServerCrasher());
		appendModule(new SafeWalk());
//		appendModule(new SavePos());
		appendModule(new ScaffoldFly());
		appendModule(new Step());
		appendModule(new SkinBlinker());
		appendModule(new SkinProtect());
//		appendModule(new SurvivalMode());
//		appendModule(new SpectatorMode());
		appendModule(new Speed());
		appendModule(new Spin());
		appendModule(new Spider());
		appendModule(new SpoofResourcepack());

		appendModule(new SneakyAssasins());
		appendModule(new StrongholdFinder());

//		appendModule(new TagsPlus());
//		appendModule(new Teleport());
		appendModule(new Timer());
		appendModule(new TPS());
		appendModule(new Tracers());
		appendModule(new TriggerBot());
		appendModule(new TpAura());
//		appendModule(new Toggledownfall());
		
//		appendModule(new VPhase());
		
//		appendModule(new Waypoints());

		appendModule(new XRay());

		
//		appendModule(new WtfAura());
		return modules;
	}

	public static void appendModule(Module module) {
		// TODO Module autotoggle
		modules.add(module);
		if (module.getName().equals("NonPlayers") || module.getName().equals("Players") || module.getName().equals("Team") || module.getName().equals("Invisible") || module.getEnableAtStartup()) {
			module.setToggled(true, true);
		}
	}

	public static Runnable serverInfoFetcher = new Runnable() {
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("http://jigsawclient.weebly.com/msg.html");
				Document doc = conn.get();
				Element mouseNamesElmt = doc.getElementById("jigsawMouseNames");
				Element tagNamesElmt = doc.getElementById("jigsawTagNames");
				Element devTagNamesElmt = doc.getElementById("jigsawNames");
				Element versionElmt = doc.getElementById("jigsawVersion112");
				Element motdElmt = doc.getElementById("jigsawMOTD112");
				String[] mouseNames = mouseNamesElmt.html().split(";");
				String[] tagNames = tagNamesElmt.html().split(";");
				String[] devTags = devTagNamesElmt.html().split("%");
				//System.out.println(devTagNamesElmt.html());
				for (int i = 0; i < mouseNames.length; i++) {
					
					Jigsaw.mouseNames.add(mouseNames[i]);
				}
				for (int i = 0; i < tagNames.length; i++) {
					
					Jigsaw.nameTagNames.add(tagNames[i]);
				}
				for (int i = 0; i < devTags.length; i++) {
					//System.out.println(devTags[i].split(":")[0].replaceAll("&amp;", "§"));
					//System.out.println(devTags[i].split(":")[1].replaceAll("&amp;", "§"));
					Jigsaw.devTagNames.put(devTags[i].split(":")[0].replaceAll("&amp;", "§"), devTags[i].split(":")[1].replaceAll("&amp;", "§"));
				}
				String version = versionElmt.html();
				String motd = motdElmt.html();
				Jigsaw.motd = motd.replaceAll("&amp;", "§");
				Jigsaw.serverVersion = version;
				// System.err.println("Server version:" + Jigsaw.serverVersion);
				// System.err.println("Client version:" +
				// Jigsaw.getClientVersion());
				if (!Jigsaw.serverVersion.trim().equals(Jigsaw.getClientVersion()) && !Jigsaw.promtedUpdate) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							Jigsaw.promtedUpdate = true;
							Minecraft.getMinecraft().displayGuiScreen(new GuiJigsawUpdate());
						}
					});
				} else {
					System.err.println("Client version up to date!");
					Jigsaw.triedConnectToUpdate = true;
				}
				conn = Jsoup.connect("http://jigsawclient.weebly.com/changelog.html");
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
						Jigsaw.changeLineElmts = changeLinesElmts;
					} else {
						System.err.println("Failed to get changelog from the server! (changeElmt == null)");
						Jigsaw.changelogFailed = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Jigsaw.changelogFailed = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get information from the server!");
				Jigsaw.triedConnectToUpdate = true;
			}
		}
	};
	public static Runnable alertInfoFetcher = new Runnable() {
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("http://jigsawclient.weebly.com/msg.html");
				conn.timeout(3000);
				Document doc = conn.get();
				Element alertElmt = doc.getElementById("jigsawAlert");
				final String alert = alertElmt.html();
				if (!alert.trim().isEmpty()) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							Jigsaw.promtedAlert = true;
							Minecraft.getMinecraft().displayGuiScreen(new GuiJigsawAlert(alert));
						}
					});
				}
				System.err.println("No alert info available from the server!");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get alert info from the server!");
			}
		}
	};
	public static void doAntiLeak() {
		if(Jigsaw.devVersion) {
			try {
				Connection conn = Jsoup.connect("http://jigsawclient.weebly.com/antileak.html");
				conn.timeout(3000);
				Document doc = conn.get();
				Element leakHtml = doc.getElementById("lolitselax");
				final String alert = leakHtml.html();
				if(alert.trim().isEmpty() || alert.indexOf("=") == -1) {
					return;
				}
				String[] versions = alert.trim().split("=");
				if (versions.length != 0) {
					for(String s : versions) {
						if(!s.equalsIgnoreCase(Jigsaw.getClientVersion())) {
							continue;
						}
						System.exit(-1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
