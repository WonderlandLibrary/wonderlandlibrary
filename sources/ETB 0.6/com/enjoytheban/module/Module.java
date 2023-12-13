package com.enjoytheban.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.command.Command;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.util.EnumChatFormatting;
import optifine.MathUtils;

public class Module {

	// Make a string for the name of the module
	public String name;

	// String for the modules suffix
	private String suffix;

	// Make a variable for holding color
	private int color;

	// Make a string[] for the aliases of the module
	private String[] alias;

	// Make a boolean to check if the module is enabled
	private boolean enabled;

	// make a variable called enabled on startup
	public boolean enabledOnStartup = false;

	// Variable to hold the keybind
	private int key;

	// Make a list to hold values
	public List<Value> values;

	// Make a variable called type for the moduletype
	public ModuleType type;

	// should the module be removed from the arraylist
	private boolean removed;

	// Shorter way to get Minecraft.getMinecraft()
	public Minecraft mc = Minecraft.getMinecraft();

	// Shorter way to get the Random() function
	public static Random random = new Random();

	// Constructor for module, @param name,alias,type
	public Module(String name, String[] alias, ModuleType type) {
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.suffix = "";
		this.key = Keyboard.KEY_NONE;
		this.removed = false;
		this.enabled = false;
		this.values = new ArrayList();
	}

	// getter for getting the module name
	public String getName() {
		return name;
	}

	// getter for getting the module alias
	public String[] getAlias() {
		return alias;
	}

	// getter for getting the module type
	public ModuleType getType() {
		return type;
	}

	// is the module enabled?
	public boolean isEnabled() {
		return enabled;
	}

	// setter getter for removed
	public boolean wasRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	// Getter and setter for the modules suffix
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(Object obj) {
		String suffix = obj.toString();
		if (suffix.isEmpty()) {
			this.suffix = suffix;
		} else {
			this.suffix = String.format("\2477- \247f%s\2477", EnumChatFormatting.GRAY + suffix);
		}
	}

	// Method to set the module enabled
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			this.onEnable();
			EventBus.getInstance().register(this);
		} else {
			EventBus.getInstance().unregister(this);
			this.onDisable();
		}
	}

	// Set the module color
	public void setColor(int color) {
		this.color = color;
	}

	// Gets the module color
	public int getColor() {
		return this.color;
	}

	// Adds values to the module
	protected void addValues(final Value... values) {
		for (final Value value : values) {
			this.values.add(value);
		}
	}

	// Gets the values for modules
	public List<Value> getValues() {
		return this.values;
	}

	// returns the key
	public int getKey() {
		return this.key;
	}

	// sets a keybind + saves it
	public void setKey(int key) {
		// set the key
		this.key = key;
		// sets content to nothing
		String content = "";
		// goes through all modules
		for (Module m : Client.instance.getModuleManager().getModules()) {
			// sets content to the name keybind blah blah
			content += String.format("%s:%s%s", m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator());
		}
		// saves the file
		FileManager.save("Binds.txt", content, false);
	}

	// On Enable
	public void onEnable() {
	}

	// On Disable
	public void onDisable() {
	}

	// lots of bullshit, this is fucked
	public void makeCommand() {
		if (this.values.size() > 0) {

			String options = "";
			String other = "";

			for (final Value value : this.values) {
				if (value instanceof Mode) {
					continue;
				}
				if (options.isEmpty()) {
					options = String.valueOf(options) + value.getName();
				} else {
					options = String.valueOf(options) + String.format(", %s", value.getName());
				}
			}

			for (final Value v : this.values) {
				if (!(v instanceof Mode)) {
					continue;
				}

				final Mode mode = (Mode) v;
				Enum[] modes;

				for (int length = (modes = mode.getModes()).length, i = 0; i < length; ++i) {
					final Enum e = modes[i];

					if (other.isEmpty()) {
						other = String.valueOf(other) + e.name().toLowerCase();
					} else {
						other = String.valueOf(other) + String.format(", %s", e.name().toLowerCase());
					}
				}
			}

			// add the command
			Client.instance.getCommandManager().add(new Command(this.name, this.alias,
					String.format("%s%s", options.isEmpty() ? "" : String.format("%s,", options),
							other.isEmpty() ? "" : String.format("%s", other)),
					"Setup this module") {

				private final Module m = Module.this;

				// execute method
				@Override
				public String execute(final String[] args) {
					if (args.length >= 2) {

						Option<Boolean> option = null;
						Numbers<Double> val = null;
						Mode mode = null;

						for (final Value value : this.m.values) {
							if (value instanceof Option) {
								if (!value.getName().equalsIgnoreCase(args[0])) {
									continue;
								}
								option = (Option<Boolean>) value;
							}
						}

						if (option != null) {
							option.setValue(!(boolean) option.getValue());
							Helper.sendMessage(
									String.format("> %s has been set to %s", option.getName(), option.getValue()));
						} else {
							for (final Value value2 : this.m.values) {
								if (value2 instanceof Numbers) {
									if (!value2.getName().equalsIgnoreCase(args[0])) {
										continue;
									}
									val = (Numbers<Double>) value2;
								}
							}

							if (val != null) {
								if (MathUtil.parsable(args[1], (byte) 4)) {
									final double value3 = MathUtil.round(Double.parseDouble(args[1]), 1);
									val.setValue((value3 > val.getMaximum()) ? ((double) val.getMaximum()) : value3);
									Helper.sendMessage(
											String.format("> %s has been set to %s", val.getName(), val.getValue()));
								} else {
									Helper.sendMessage("> "+args[1] + " is not a number");
								}
							}

							for (final Value v : this.m.values) {
								if (args[0].equalsIgnoreCase(v.getDisplayName())) {

									if (!(v instanceof Mode)) {
										continue;
									}
									mode = (Mode) v;
								}
							}

							if (mode != null) {
								if (mode.isValid(args[1])) {
									mode.setMode(args[1]);
									Helper.sendMessage(
											String.format("> %s set to %s", mode.getName(), mode.getModeAsString()));
								}

								else {
									Helper.sendMessage("> "+args[1] + " is an invalid mode");
								}
							}
						}

						if (val == null && option == null && mode == null) {
							this.syntaxError("Valid .<module> <setting> <mode if needed>");
						}
					}

					else if (args.length >= 1) {
						Option<Boolean> option = null;

						for (final Value value : this.m.values) {
							if (value instanceof Option) {
								if (!value.getName().equalsIgnoreCase(args[0])) {
									continue;
								}
								option = (Option<Boolean>) value;
							}
						}

						if (option != null) {
							option.setValue(!(boolean) option.getValue());
							String fuck = option.getName().substring(1);
							String xd = option.getName().substring(0, 1).toUpperCase();
							if(option.getValue()) {
								Helper.sendMessage(String.format("> %s has been set to §a%s", xd+fuck, option.getValue()));
							} else {
								Helper.sendMessage(String.format("> %s has been set to §c%s", xd+fuck, option.getValue()));
							}
						}

						else {
							this.syntaxError("Valid .<module> <setting> <mode if needed>");
						}
					}

					else {
						Helper.sendMessage(
								String.format("%s Values: \n %s",
										this.getName().substring(0, 1).toUpperCase()
												+ this.getName().substring(1).toLowerCase(),
										this.getSyntax(), "false"));
					}

					return null;
				}
			});
		}
	}
}