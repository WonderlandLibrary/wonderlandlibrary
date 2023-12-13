package com.enjoytheban.management;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventKey;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.combat.AntiBot;
import com.enjoytheban.module.modules.combat.AutoHeal;
import com.enjoytheban.module.modules.combat.AutoSword;
import com.enjoytheban.module.modules.combat.BowAimBot;
import com.enjoytheban.module.modules.combat.Criticals;
import com.enjoytheban.module.modules.combat.FastBow;
import com.enjoytheban.module.modules.combat.Killaura;
import com.enjoytheban.module.modules.combat.Regen;
import com.enjoytheban.module.modules.combat.TPAura;
import com.enjoytheban.module.modules.movement.Boost;
import com.enjoytheban.module.modules.movement.Flight;
import com.enjoytheban.module.modules.movement.Jesus;
import com.enjoytheban.module.modules.movement.Longjump;
import com.enjoytheban.module.modules.movement.NoSlow;
import com.enjoytheban.module.modules.movement.Scaffold;
import com.enjoytheban.module.modules.movement.Sneak;
import com.enjoytheban.module.modules.movement.Speed;
import com.enjoytheban.module.modules.movement.Sprint;
import com.enjoytheban.module.modules.movement.Step;
import com.enjoytheban.module.modules.movement.Teleport;
import com.enjoytheban.module.modules.player.AntiVelocity;
import com.enjoytheban.module.modules.player.AutoAccept;
import com.enjoytheban.module.modules.player.Bobbing;
import com.enjoytheban.module.modules.player.Dab;
import com.enjoytheban.module.modules.player.FastUse;
import com.enjoytheban.module.modules.player.Freecam;
import com.enjoytheban.module.modules.player.InvCleaner;
import com.enjoytheban.module.modules.player.Invplus;
import com.enjoytheban.module.modules.player.MCF;
import com.enjoytheban.module.modules.player.NoFall;
import com.enjoytheban.module.modules.player.NoStrike;
import com.enjoytheban.module.modules.player.SkinFlash;
import com.enjoytheban.module.modules.player.Zoot;
import com.enjoytheban.module.modules.render.Chams;
import com.enjoytheban.module.modules.render.ChestESP;
import com.enjoytheban.module.modules.render.ESP;
import com.enjoytheban.module.modules.render.FullBright;
import com.enjoytheban.module.modules.render.HUD;
import com.enjoytheban.module.modules.render.Nametags;
import com.enjoytheban.module.modules.render.NoRender;
import com.enjoytheban.module.modules.render.Tracers;
import com.enjoytheban.module.modules.render.Xray;
import com.enjoytheban.module.modules.world.AntiVoid;
import com.enjoytheban.module.modules.world.AutoArmor;
import com.enjoytheban.module.modules.world.Banwave;
import com.enjoytheban.module.modules.world.Blink;
import com.enjoytheban.module.modules.world.ChestStealer;
import com.enjoytheban.module.modules.world.Deathclip;
import com.enjoytheban.module.modules.world.FastPlace;
import com.enjoytheban.module.modules.world.NoRotate;
import com.enjoytheban.module.modules.world.Phase;
import com.enjoytheban.module.modules.world.PinCracker;
import com.enjoytheban.module.modules.world.PingSpoof;
import com.enjoytheban.module.modules.world.SafeWalk;
import com.enjoytheban.module.modules.world.StaffAlerts;
import com.enjoytheban.utils.render.gl.GLUtils;

import net.minecraft.client.renderer.GlStateManager;

/**
 * @author Purity Basic ModuleManager
 */

public class ModuleManager implements Manager {

	// Makes a new arraylist that holds the modules
	public static List<Module> modules = new ArrayList<>();
	// A boolean that helps out our module enabling on startup
	private boolean enabledNeededMod = true;

	public boolean nicetry = true;

	@Override
	public void init() {
		// Add Modules here
		this.modules.add(new HUD());
		this.modules.add(new Sprint());
		this.modules.add(new Killaura());
		this.modules.add(new AntiVelocity());
		this.modules.add(new Criticals());
		this.modules.add(new Speed());
		this.modules.add(new Longjump());
		this.modules.add(new Flight());
		this.modules.add(new NoFall());
		this.modules.add(new Invplus());
		this.modules.add(new NoSlow());
		this.modules.add(new FastBow());
		this.modules.add(new AntiBot());
		this.modules.add(new Freecam());
		this.modules.add(new MCF());
		this.modules.add(new Nametags());
		this.modules.add(new Tracers());
		this.modules.add(new ESP());
		this.modules.add(new Regen());
		this.modules.add(new FastPlace());
		this.modules.add(new NoRender());
		this.modules.add(new FullBright());
		this.modules.add(new ChestStealer());
		this.modules.add(new AutoArmor());
		this.modules.add(new AntiVoid());
		this.modules.add(new AutoHeal());
		this.modules.add(new NoRotate());
		this.modules.add(new Scaffold());
		this.modules.add(new Sneak());
		this.modules.add(new SafeWalk());
		this.modules.add(new Zoot());
		this.modules.add(new Jesus());
		this.modules.add(new Phase());
		this.modules.add(new Chams());
		this.modules.add(new Deathclip());
		this.modules.add(new NoStrike());
		this.modules.add(new SkinFlash());
		this.modules.add(new AutoAccept());
		this.modules.add(new Blink());
		this.modules.add(new Banwave());
		this.modules.add(new FastUse());
		this.modules.add(new PingSpoof());
		this.modules.add(new BowAimBot());
		this.modules.add(new Xray());
		this.modules.add(new ChestESP());
		this.modules.add(new InvCleaner());
		this.modules.add(new Step());
		this.modules.add(new Dab());
		this.modules.add(new Teleport());
		this.modules.add(new AutoSword());
		this.modules.add(new Boost());
		this.modules.add(new Bobbing());
		this.modules.add(new PinCracker());
		this.modules.add(new TPAura());
		this.modules.add(new StaffAlerts());
		// calls the readSettings method
		this.readSettings();

		// call the makecommand method for each module
		for (Module m : this.modules) {
			m.makeCommand();
		}

		// Registers the event
		EventBus.getInstance().register(this);

	}

	// Returns the Modules in the list
	public static List<Module> getModules() {
		return ModuleManager.modules;
	}

	// Method to return the module as a class
	public Module getModuleByClass(Class<? extends Module> cls) {
		for (Module m : this.modules) {
			if (m.getClass() != cls) {
				continue;
			}
			return m;
		}
		return null;
	}

	// Method to return the modules name
	public static Module getModuleByName(String name) {
		for (Module m : ModuleManager.modules) {
			if (!m.getName().equalsIgnoreCase(name)) {
				continue;
			}
			return m;
		}
		return null;
	}

	// Method to get the modules ALIAS
	public Module getAlias(final String name) {
		for (final Module f : this.modules) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f;
			}
			String[] alias;
			for (int length = (alias = f.getAlias()).length, i = 0; i < length; ++i) {
				final String s = alias[i];
				if (s.equalsIgnoreCase(name)) {
					return f;
				}
			}
		}
		return null;
	}

	// Method to get the modules Type
	public List<Module> getModulesInType(ModuleType t) {
		List<Module> output = new ArrayList();
		for (Module m : this.modules) {
			if (m.getType() != t) {
				continue;
			}
			output.add(m);
		}
		return output;
	}

	// A method that uses our eventkey to make binds work properly
	@EventHandler
	private void onKeyPress(EventKey e) {
		// runs through modules
		for (Module m : this.modules) {
			// if the module keybind doesnt match up with the key we press
			if (m.getKey() != e.getKey()) {
				continue;
			}
			// enable/disable
			m.setEnabled(!m.isEnabled());
		}
	}

	@EventHandler
	private void onGLHack(EventRender3D e) {
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) GLUtils.MODELVIEW.clear());
		GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, (FloatBuffer) GLUtils.PROJECTION.clear());
		GlStateManager.glGetInteger(GL11.GL_VIEWPORT, (IntBuffer) GLUtils.VIEWPORT.clear());
	}

	@EventHandler
	private void on2DRender(EventRender2D e) {
		// our boolean that fucks
		if (this.enabledNeededMod) {
			this.enabledNeededMod = false;
			// grab module
			for (Module m : this.modules) {
				// if the module isnt to be enabled on startup
				if (!m.enabledOnStartup) {
					continue;
				}
				// set enabled
				m.setEnabled(true);
			}
		}
	}

	// Here we will read our binds settings etc
	private void readSettings() {
		// reads the binds.txt
		List<String> binds = FileManager.read("Binds.txt");
		for (String v : binds) {
			String name = v.split(":")[0], bind = v.split(":")[1];
			Module m = this.getModuleByName(name);
			// if module couldnt be found
			if (m == null) {
				continue;
			}
			// sets the key
			m.setKey(Keyboard.getKeyIndex(bind.toUpperCase()));
		}

		// now we're gonna read the enabled mods
		List<String> enabled = FileManager.read("Enabled.txt");
		for (String v : enabled) {
			// goes through mods by name
			Module m = this.getModuleByName(v);
			// if mod couldnt be found
			if (m == null) {
				continue;
			}
			// sets the mod enabled
			m.enabledOnStartup = true;
		}

		// now for the settings
		List<String> vals = FileManager.read("Values.txt");
		for (String v : vals) {
			// basically just splitting
			String name = v.split(":")[0], values = v.split(":")[1];
			// grabbing module by name
			Module m = this.getModuleByName(name);
			// if module null
			if (m == null) {
				continue;
			}

			// go through the modules
			for (Value value : m.getValues()) {
				if (value.getName().equalsIgnoreCase(values)) {
					// options
					if (value instanceof Option) {
						value.setValue(Boolean.parseBoolean(v.split(":")[2]));
					}
					// numeric values
					else if (value instanceof Numbers) {
						value.setValue(Double.parseDouble(v.split(":")[2]));
					}
					// modes
					else {
						((Mode) value).setMode(v.split(":")[2]);
					}
				}
			}
		}
	}
}