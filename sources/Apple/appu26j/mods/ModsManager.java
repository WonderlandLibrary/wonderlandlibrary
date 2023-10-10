package appu26j.mods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import appu26j.mods.general.*;
import appu26j.mods.multiplayer.*;
import appu26j.mods.visuals.*;

public class ModsManager
{
	private ArrayList<Mod> mods = new ArrayList<>();
	
	public ModsManager initialize()
	{
		// General
		this.mods.add(new ToggleSprint());
		this.mods.add(new ClockTimer());
		
		// Multiplayer
		this.mods.add(new AutoGG());
		this.mods.add(new AutoTip());
		this.mods.add(new SnipeSafe());
		this.mods.add(new Cooldown());
		this.mods.add(new AutoFriend());
        this.mods.add(new QuickPlay());
		
		// Visuals
		this.mods.add(new Visuals());
		this.mods.add(new FullBright());
		this.mods.add(new PingIndicator());
		this.mods.add(new DamageTilt());
		this.mods.add(new Crosshair());
		this.mods.add(new TimeChanger());
		this.mods.add(new Chat());
		this.mods.add(new Scoreboard());
		this.mods.add(new FPS());
		this.mods.add(new TimeClock());
		this.mods.add(new NameTags());
		this.mods.add(new PackDisplay());
		this.mods.add(new HurtCamera());
		this.mods.add(new DamageTint());
		this.mods.add(new NoBobbing());
		this.mods.add(new NoPumpkin());
		this.mods.add(new KeyStrokes());
		this.mods.add(new CPS());
		this.mods.add(new Combo());
		this.mods.add(new ReachDisplay());
		this.mods.add(new BetterZoom());
		this.mods.add(new ArmorStatus());
        this.mods.add(new TabList());
        this.mods.add(new BossBar());
        this.mods.add(new BlockOverlay());
        this.mods.add(new DamageIndicator());
        this.mods.add(new ItemPhysics());
        this.mods.add(new PotionEffects());
        this.mods.add(new TNTCountdown());
        this.mods.add(new RawInput());
        this.mods.add(new ParticleMultipler());
        this.mods.add(new MemoryUsage());
        this.mods.add(new Coordinates());
        this.mods.add(new BorderlessWindow());
        this.mods.add(new Perspective());
        this.mods.add(new MenuBlur());
        this.mods.add(new FakeHacks());
        this.mods.add(new FPSSpoofer());
        this.mods.add(new MotionBlur());
        this.mods.add(new DabMod());
        this.mods.add(new UnfocusedFPS());
        this.mods.add(new PingDisplay());
		this.mods.sort(Comparator.comparing(Mod::getName));
		this.getMods("Scoreboard", "Bossbar").forEach(mod -> mod.setEnabled(true));
		return this;
	}
	
	public ArrayList<Mod> getMods()
	{
		return this.mods;
	}
	
	public ArrayList<Mod> getMods(String... names)
	{
	    ArrayList<Mod> mods = new ArrayList<>();
	    
	    for (String name : names)
	    {
	        Mod mod = this.mods.stream().filter(modObject -> modObject.getName().equals(name)).findFirst().orElse(null);
	        
	        if (mod != null)
	        {
	            mods.add(mod);
	        }
	    }
	    
	    return mods;
	}
	
	public ArrayList<Mod> getMods(Category category)
	{
		return this.mods.stream().filter(mod -> mod.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Mod getMod(String name)
	{
		return this.mods.stream().filter(mod -> mod.getName().equals(name)).findFirst().orElse(null);
	}
}
