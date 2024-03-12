package xyz.cucumber.base.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.feat.combat.AntiBotModule;
import xyz.cucumber.base.module.feat.combat.AutoClickerModule;
import xyz.cucumber.base.module.feat.combat.AutoRodModule;
import xyz.cucumber.base.module.feat.combat.BackTrackModule;
import xyz.cucumber.base.module.feat.combat.BowAimBotModule;
import xyz.cucumber.base.module.feat.combat.CriticalsModule;
import xyz.cucumber.base.module.feat.combat.DelayFixModule;
import xyz.cucumber.base.module.feat.combat.EnnoyerModule;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.feat.combat.MoreKBModule;
import xyz.cucumber.base.module.feat.combat.NoHitSlowModule;
import xyz.cucumber.base.module.feat.combat.ReachModule;
import xyz.cucumber.base.module.feat.combat.STapModule;
import xyz.cucumber.base.module.feat.combat.TimerRangeModule;
import xyz.cucumber.base.module.feat.combat.VelocityModule;
import xyz.cucumber.base.module.feat.movement.AirStuckModule;
import xyz.cucumber.base.module.feat.movement.BlinkModule;
import xyz.cucumber.base.module.feat.movement.FlightModule;
import xyz.cucumber.base.module.feat.movement.InvMoveModule;
import xyz.cucumber.base.module.feat.movement.JesusModule;
import xyz.cucumber.base.module.feat.movement.LongJumpModule;
import xyz.cucumber.base.module.feat.movement.NoFallModule;
import xyz.cucumber.base.module.feat.movement.NoJumpDelayModule;
import xyz.cucumber.base.module.feat.movement.NoRotateModule;
import xyz.cucumber.base.module.feat.movement.NoSlowModule;
import xyz.cucumber.base.module.feat.movement.NoWebModule;
import xyz.cucumber.base.module.feat.movement.SpeedModule;
import xyz.cucumber.base.module.feat.movement.SprintModule;
import xyz.cucumber.base.module.feat.movement.TimerModule;
import xyz.cucumber.base.module.feat.other.ArrayListModule;
import xyz.cucumber.base.module.feat.other.BoosterModule;
import xyz.cucumber.base.module.feat.other.ClickGuiModule;
import xyz.cucumber.base.module.feat.other.FriendsModule;
import xyz.cucumber.base.module.feat.other.NotificationsModule;
import xyz.cucumber.base.module.feat.other.ScoreboardModule;
import xyz.cucumber.base.module.feat.other.SessionModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.feat.other.TrackViewerModule;
import xyz.cucumber.base.module.feat.other.WaterMarkModule;
import xyz.cucumber.base.module.feat.player.AutoArmorModule;
import xyz.cucumber.base.module.feat.player.AutoHealModule;
import xyz.cucumber.base.module.feat.player.AutoToolModule;
import xyz.cucumber.base.module.feat.player.AutoWeaponModule;
import xyz.cucumber.base.module.feat.player.CrasherModule;
import xyz.cucumber.base.module.feat.player.DisablerModule;
import xyz.cucumber.base.module.feat.player.ExtinguishModule;
import xyz.cucumber.base.module.feat.player.InvManagerModule;
import xyz.cucumber.base.module.feat.player.NameProtectModule;
import xyz.cucumber.base.module.feat.player.PingSpoofModule;
import xyz.cucumber.base.module.feat.player.ScaffoldModule;
import xyz.cucumber.base.module.feat.player.SmoothRotationModule;
import xyz.cucumber.base.module.feat.player.SpammerModule;
import xyz.cucumber.base.module.feat.player.StealerModule;
import xyz.cucumber.base.module.feat.visuals.AnimationsModule;
import xyz.cucumber.base.module.feat.visuals.BetterItemsModule;
import xyz.cucumber.base.module.feat.visuals.ChamsModule;
import xyz.cucumber.base.module.feat.visuals.ChinaHatModule;
import xyz.cucumber.base.module.feat.visuals.ESP3DModule;
import xyz.cucumber.base.module.feat.visuals.ESPModule;
import xyz.cucumber.base.module.feat.visuals.EffectStatusModule;
import xyz.cucumber.base.module.feat.visuals.FullBrightModule;
import xyz.cucumber.base.module.feat.visuals.HotbarModule;
import xyz.cucumber.base.module.feat.visuals.HurtCamModule;
import xyz.cucumber.base.module.feat.visuals.JumpCirclesModule;
import xyz.cucumber.base.module.feat.visuals.MurderFinderModule;
import xyz.cucumber.base.module.feat.visuals.NameTagsModule;
import xyz.cucumber.base.module.feat.visuals.RadarModule;
import xyz.cucumber.base.module.feat.visuals.ShaderESPModule;
import xyz.cucumber.base.module.feat.visuals.StorageESPModule;
import xyz.cucumber.base.module.feat.visuals.TargetESPModule;
import xyz.cucumber.base.module.feat.visuals.TargetHudModule;
import xyz.cucumber.base.module.feat.visuals.TracersModule;
import xyz.cucumber.base.module.feat.visuals.TrailModule;

public class ModuleManager {
	
	private ArrayList<Mod> modules = new ArrayList();
	
	private ArrayList<Mod> draggable = new ArrayList<>();
	
	public ModuleManager() {
		modules.add(new AntiBotModule());
		modules.add(new AutoClickerModule());
		modules.add(new AutoRodModule());
		modules.add(new BackTrackModule());
		modules.add(new BowAimBotModule());
		modules.add(new CriticalsModule());
		modules.add(new DelayFixModule());
		modules.add(new EnnoyerModule());
		modules.add(new KillAuraModule());
		modules.add(new MoreKBModule());
		modules.add(new NoHitSlowModule());
		modules.add(new ReachModule());
		modules.add(new STapModule());
		modules.add(new TimerRangeModule());
		modules.add(new VelocityModule());
		
		modules.add(new AirStuckModule());
		modules.add(new BlinkModule());
		modules.add(new FlightModule());
		modules.add(new InvMoveModule());
		modules.add(new JesusModule());
		modules.add(new LongJumpModule());
		modules.add(new NoFallModule());
		modules.add(new NoJumpDelayModule());
		modules.add(new NoRotateModule());
		modules.add(new NoSlowModule());
		modules.add(new NoWebModule());
		modules.add(new SpeedModule());
		modules.add(new SprintModule());
		modules.add(new TimerModule());
		
		modules.add(new ArrayListModule());
		modules.add(new BoosterModule());
		modules.add(new ClickGuiModule());
		modules.add(new FriendsModule());
		modules.add(new NotificationsModule());
		modules.add(new ScoreboardModule());
		modules.add(new SessionModule());
		modules.add(new TeamsModule());
		//modules.add(new TrackViewerModule());
		modules.add(new WaterMarkModule());
		modules.add(new TrackViewerModule());
		
		modules.add(new AutoArmorModule());
		modules.add(new AutoHealModule());
		modules.add(new AutoToolModule());
		modules.add(new AutoWeaponModule());
		modules.add(new CrasherModule());
		modules.add(new DisablerModule());
		modules.add(new ExtinguishModule());
		modules.add(new InvManagerModule());
		modules.add(new NameProtectModule());
		modules.add(new PingSpoofModule());
		modules.add(new ScaffoldModule());
		modules.add(new SmoothRotationModule());
		modules.add(new SpammerModule());
		modules.add(new StealerModule());
		
		modules.add(new AnimationsModule());
		modules.add(new BetterItemsModule());
		modules.add(new EffectStatusModule());
		modules.add(new ESP3DModule());
		modules.add(new ESPModule());
		modules.add(new FullBrightModule());
		modules.add(new HotbarModule());
		modules.add(new HurtCamModule());
		modules.add(new ChamsModule());
		modules.add(new ChinaHatModule());
		modules.add(new JumpCirclesModule());
		modules.add(new NameTagsModule());
		modules.add(new RadarModule());
		modules.add(new ShaderESPModule());
		modules.add(new StorageESPModule());
		modules.add(new TargetESPModule());
		modules.add(new TargetHudModule());
		modules.add(new TracersModule());
		modules.add(new TrailModule());
		modules.add(new MurderFinderModule());
		
		for(Mod m : modules) {
			if(m instanceof Dragable) {
				draggable.add(m);
		    }
		}
	}
	
	

	public ArrayList<Mod> getDraggable() {
		return draggable;
	}

	
	public ArrayList<Mod> getModules() {
		return modules;
	}
	
	public Mod getModule(Class modclass) {
		return modules.stream().filter(m -> m.getClass() == modclass).findFirst().orElse(null);
	}
	public Mod getModule(String name) {
		return modules.stream().filter(m -> m.getName().toLowerCase().replace(" ", "").equals(name.toLowerCase().replace(" ", ""))).findFirst().orElse(null);
	}
	
	public List<Mod> getModulesByCategory(Category c) {
		return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
	}

	public List<Mod> getEnabledModules() {
		List<Mod> modules = new ArrayList();
		
		for(Mod m : getModules()) {
			if(m.isEnabled()) modules.add(m);
		}
		return modules;
	}
}
