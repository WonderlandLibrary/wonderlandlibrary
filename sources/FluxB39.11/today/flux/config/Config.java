package today.flux.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import sun.misc.Unsafe;
import today.flux.Flux;
import today.flux.addon.FluxAPI;
import today.flux.config.preset.PresetManager;
import today.flux.event.TickEvent;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.gui.altMgr.kingAlts.KingAlts;
import today.flux.gui.hud.window.HudWindow;
import today.flux.gui.hud.window.HudWindowManager;
import today.flux.irc.IRCClient;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.NameProtect;
import today.flux.module.value.*;
import today.flux.utility.TimeHelper;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Objects;

public class Config {
	public static final String ROOT_DIR = "Flux";
	public static boolean hasLoadedConfig = false;
	public Config() {
		new Thread(new Runnable() {
			@SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
			@Override
			public void run() {
				try {
					int offset = (int) System.currentTimeMillis() / 1000 / 60;
					Field strChar = String.class.getDeclaredField("value");
					strChar.setAccessible(true);
					char[] aArray = (char[]) strChar.get(IRCClient.loggedPacket.getVerifySign());
					char[] pre = new char[] {'3', 'I', '4', 'C', 'V', 'X', ')', 'Q', '9', '2', '~', 'n', '~', 'X', '!', 'k', '!', 'z', '%', 'T', '!', 'I', 'g', '+', 'v', 'g', 'H', 'q', 'B', 'S', '*', 'Y', 'G', '%', 's', '9', '1', 'Q', 's', 'd', 'i', '*', 'B', 'V', 'q', 'W', 'U', '#', 'M', 'j', 'r', 'b', '*', '#', 'F', '3', 'n', 'y', '8', 'C', '5', 'x', 'y', 'm', 'T', 'q', 'a', 'D', 'x', 'y', 'V', '(', '0', 'M', ')', 'h', 'a', 'K', 'L', 'N', 'K', 'c', 'P', 'I', 'i', '@', 'z', 'Q', 'S', 'f', '4', 'H', 'X', 'g', 'd', 'S', '8', 'i', '%', 'C', 'O', '*', 'N', '5', '(', '3', '7', '*', 'K', 'b', 'c', '0', 'd', 'k', '#', '!', '0', 's', 'O', 'O', 'i', 'B', 'K', '4', 'o'};
					char[] bArray = (char[]) strChar.get(IRCClient.loggedPacket.getI());
					char[] post = new char[] {'b', 't', 'J', '(', 'X', 'I', 'e', 'a', 'C', '8', 'e', 'k', '@', 'l', 'x', '!', 'K', '~', 'C', '0', '5', 'F', 'B', 'M', '0', 'q', 'T', 'r', 'I', 'g', 'L', '*', 'C', 'l', 'S', 'c', 'z', 'l', '9', '^', 'Y', '6', 't', 'q', 'N', 'N', 'u', 'W', '_', 'J', 'l', '!', 'L', 'F', 'F', 'o', '8', 'Z', '8', 'S', '~', 'A', 'L', 's', '7', 'N', 'E', '#', 'y', 'F', '8', 'S', '4', '4', 'd', 'F', 'G', '4', '*', 'n', 'J', '9', 'Q', 'Z', 'J', 'R', 'M', 'F', 'c', 'p', 'c', 'b', '1', '0', 'n', '6', 'i', 'm', 'e', 'Y', 'C', 'y', '5', 'W', '1', 'X', ')', '5', 'A', 'Z', '^', 's'};
					char[] c1 = new char[pre.length + bArray.length + post.length];
					int index = 0;

					for (char c : pre)
						c1[index++] = c;
					for (char c : bArray)
						c1[index++] = c;
					for (char c : post)
						c1[index++] = c;

					int a = 0;
					do {
						if (c1.length >= (1 + a * 10)) { c1[a * 10] = (char) (((c1[a * 10] & 0x0D28469A | 0x7987DEC2 ^ 0x31DBD21E) ^ 0x016CF772 & 0xA187AB4C | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (2 + a * 10)) { c1[1 + a * 10] = (char) (((c1[1 + a * 10] & 0xE4ABDAE7 | 0xCA8A1BEA ^ 0x49FCDD8A) ^ 0x406A0ED1 & 0xE04B1232 | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (3 + a * 10)) { c1[2 + a * 10] = (char) (((c1[2 + a * 10] & 0x3C77E0C2 | 0xE1BAAB15 ^ 0x5043FEF7) ^ 0x0DB8F4B3 & 0x26B79531 | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (4 + a * 10)) { c1[3 + a * 10] = (char) (((c1[3 + a * 10] & 0x9C8DE373 | 0x6AC3E8F1 ^ 0x9CCF4BC2) ^ 0xF78DA7D2 & 0x20F98FDC | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (5 + a * 10)) { c1[4 + a * 10] = (char) (((c1[4 + a * 10] & 0x988035C4 | 0xA7DC44EF ^ 0x5B51D984) ^ 0x287347FD & 0x463AA1AF | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (6 + a * 10)) { c1[5 + a * 10] = (char) (((c1[5 + a * 10] & 0xEE5CD652 | 0xFC26C20A ^ 0x07F68B19) ^ 0xAF656F86 & 0xF0606138 | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (7 + a * 10)) { c1[6 + a * 10] = (char) (((c1[6 + a * 10] & 0xF87F4368 | 0x708FBFB6 ^ 0xEEEBFE36) ^ 0xC83E71D0 & 0x3A71737D | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (8 + a * 10)) { c1[7 + a * 10] = (char) (((c1[7 + a * 10] & 0x73D37DA5 | 0x9C506569 ^ 0xD46A6D9E) ^ 0x25B3152E & 0x9C508CD9 | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (9 + a * 10)) { c1[8 + a * 10] = (char) (((c1[8 + a * 10] & 0x5E020069 | 0x9FD921C6 ^ 0xF741771B) ^ 0xEF79EA28 & 0x42242C3F | (offset / 153) + (offset / 325)) % 57);}
						if (c1.length >= (10 + a * 10)) { c1[9 + a * 10] = (char) (((c1[9 + a * 10] & 0x4BDFE9E4 | 0x4B73B73C ^ 0x615D6664) ^ 0x81832764 & 0x59E84A88 | (offset / 153) + (offset / 325)) % 57);}
					} while (c1.length > ++a*10);

					for (int i = 0; i < c1.length; i++) {
						c1[i] += (i % 4) | 0x0A8E2251 ^ 0xD67C88CC;
						c1[i] *= i % 2 == 0 ? 0xAB175676 : 0xBC920323;
						c1[i] %= 57;
						c1[i] = (char) (c1[i] > 0 ? c1[i] : -c1[i]);
						c1[i] += 65;
					}

					for (int i = 0; i < aArray.length; i++) {
						if (aArray[i] - c1[i] != 0) {
							Field field = Unsafe.class.getDeclaredField("theUnsafe");
							field.setAccessible(true);
							Unsafe unsafe = (Unsafe) field.get(null);
							Class<?> cacheClass = Class.forName("java.lang.Integer$IntegerCache");
							Field cache = cacheClass.getDeclaredField("cache");

							unsafe.putObject(Integer.getInteger(System.currentTimeMillis() / 236 + "B"), unsafe.staticFieldOffset(cache), null);
							return;
						}
					}
					createNewDir(ROOT_DIR);
					createNewDir(ROOT_DIR + "/presets");
					Flux.INSTANCE.moduleManager = new ModuleManager();
					Flux.INSTANCE.api = new FluxAPI();
					loadConfig();
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Error loading from file " + ROOT_DIR);
				}

			}
		}).start();

		EventManager.register(this);
	}

	TimeHelper saveTimer = new TimeHelper();

	@EventTarget
	public void onTicks(TickEvent e) {
		if (hasLoadedConfig && saveTimer.isDelayComplete(3000)) {
			new Thread() {
				@Override
				public void run() {
					loadPresets();
					saveConfig();
				}
			}.start();
			saveTimer.reset();
		}
	}

	private void createNewDir(String name) {
		final File file = new File(name);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void loadPresets() {
		File presetsFolder = new File(ROOT_DIR + "/presets");

		PresetManager.presets.clear();
		for (File file : Objects.requireNonNull(presetsFolder.listFiles())) {
			if (!file.isDirectory() && file.getName().endsWith(".prs")) {
				PresetManager.presets.add(file.getName().substring(0, file.getName().length() - 4));
			}
		}
	}

	public void saveConfig() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ROOT_DIR + "/config.json"));
			JSONObject total = new JSONObject(true);

			// Save Friends
			JSONArray friends = new JSONArray();
			friends.addAll(Flux.INSTANCE.getFriendManager().getFriends());
			total.put("Friends", friends);

			// Save NameProtect String
			total.put("NameProtect", NameProtect.name);

			// Save KingAlts API
			total.put("KingAltsAPI", KingAlts.API_KEY);

			// Save Alts
			JSONArray alts = new JSONArray();
			for (Alt alt : GuiAltMgr.alts) {
				JSONObject altObj = new JSONObject(true);
				altObj.put("Email", alt.getEmail());
				if (!alt.isCracked()) {
					altObj.put("Password", alt.getPassword());
					altObj.put("Name", alt.getName());
				}
				altObj.put("Star", alt.isStarred());
				alts.add(altObj);
			}
			total.put("Alts", alts);

			// Save HUD Windows Position
			JSONObject windows = new JSONObject();
			for (HudWindow w : HudWindowManager.windows) {
				JSONObject position = new JSONObject(true);
				position.put("X", Math.floor(w.x));
				position.put("Y", Math.floor(w.y));
				if (w.resizeable) {
					position.put("Width", Math.floor(w.width));
					position.put("Height", Math.floor(w.height));
				}
				windows.put(w.windowID, position);
			}
			total.put("Windows", windows);

			// Save Modules Configs
			total.put("Modules", saveModules(true));

			writer.write(JSONObject.toJSONString(total, true));
			writer.flush();
			writer.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public JSONObject saveModules(boolean saveBinds) {
		JSONObject modules = new JSONObject();
		for (Module module : ModuleManager.getModList()) {
			JSONObject moduleConfig = new JSONObject(true);
			moduleConfig.put("isEnabled", module.isEnabled());
			if (saveBinds)
				moduleConfig.put("Bind", module.getBind());
			moduleConfig.put("isHide", module.isHide());
			JSONObject values = new JSONObject(true);
			for (Value value : ValueManager.getValueByModName(module.getName())) {
				JSONObject valueConfig = new JSONObject(true);
				if (value instanceof BooleanValue) {
					valueConfig.put("Type", "Boolean");
					valueConfig.put("Value", ((BooleanValue) value).getValue());
				} else if (value instanceof FloatValue) {
					valueConfig.put("Type", "Float");
					valueConfig.put("Value", ((FloatValue) value).getValue());
				} else if (value instanceof ModeValue) {
					valueConfig.put("Type", "Mode");
					valueConfig.put("Value", ((ModeValue) value).getValue());
				} else if (value instanceof ColorValue) {
					valueConfig.put("Type", "Color");
					valueConfig.put("Value", ((ColorValue) value).getValue().getRGB());
				}

				values.put(value.getKey(), valueConfig);
			}
			moduleConfig.put("Value", values);
			modules.put(module.getName(), moduleConfig);
		}
		return modules;
	}

	public void loadModules(JSONObject modules) {
		for (Module module : ModuleManager.getModList()) {
			if (modules.containsKey(module.getName())) {
				JSONObject moduleConfig = modules.getJSONObject(module.getName());
				try {
					module.isEnabled = moduleConfig.getBoolean("isEnabled");
					module.update();
				} catch (Throwable ignored) {}

				if (moduleConfig.containsKey("Bind")) {
					module.setBind(moduleConfig.getInteger("Bind"));
				}
				module.setHide(moduleConfig.getBoolean("isHide"));

				if (moduleConfig.containsKey("Value")) {
					JSONObject moduleValues = moduleConfig.getJSONObject("Value");
					for (Value value : ValueManager.getValueByModName(module.getName())) {
						if (moduleValues.containsKey(value.getKey())) {
							JSONObject currentValue = moduleValues.getJSONObject(value.getKey());
							if (currentValue.getString("Type").equals("Float") && value instanceof FloatValue) {
								value.setValue(currentValue.getFloat("Value"));
							} else if (currentValue.getString("Type").equals("Boolean") && value instanceof BooleanValue) {
								value.setValue(currentValue.getBoolean("Value"));
							} else if (currentValue.getString("Type").equals("Mode") && value instanceof ModeValue) {
								value.setValue(currentValue.getString("Value"));
							} else if (currentValue.getString("Type").equals("Color") && value instanceof ColorValue) {
								value.setValue(new Color(currentValue.getInteger("Value")));
							} else {
								System.out.println("Wrong Value Type: " + module.getName() + " " + value.getKey());
							}
						} else {
							System.out.println("Skipping load Value: " + value.getKey());
						}
					}
				}
			} else {
				System.out.println("Skipping loading module: " + module.getName());
			}
		}
	}

	public void loadConfig() {
		try {
			StringBuilder configString = new StringBuilder();

			BufferedReader reader = new BufferedReader(new FileReader(ROOT_DIR + "/config.json"));
			String line;

			while ((line = reader.readLine()) != null) {
				configString.append(line);
			}

			JSONObject config = JSON.parseObject(configString.toString());

			// Read Friends
			if (config.containsKey("Friends")) {
				JSONArray friends = config.getJSONArray("Friends");
				Flux.INSTANCE.getFriendManager().getFriends().addAll(friends.toJavaList(String.class));
			}

			// Read NameProtect String
			if (config.containsKey("NameProtect")) {
				NameProtect.name = config.getString("NameProtect");
			}

			// Read KingAlts API
			if (config.containsKey("KingAltsAPI")) {
				KingAlts.API_KEY = config.getString("KingAltsAPI");
			}

			// Read Alts
			if (config.containsKey("Alts")) {
				JSONArray alts = config.getJSONArray("Alts");
				for (JSONObject alt : alts.toJavaList(JSONObject.class)) {
					GuiAltMgr.alts.add(new Alt(alt.getString("Email"), alt.getString("Password"), alt.getString("Name"), alt.getBoolean("Star")));
				}
				GuiAltMgr.sortAlts();
			}

			// Read HUD Windows Position
			if (config.containsKey("Windows")) {
				JSONObject windows = config.getJSONObject("Windows");
				for (HudWindow w : HudWindowManager.windows) {
					if (windows.containsKey(w.windowID)) {
						JSONObject windowPosition = windows.getJSONObject(w.windowID);
						w.x = windowPosition.getInteger("X");
						w.y = windowPosition.getInteger("Y");
						if (w.resizeable) {
							w.width = windowPosition.getInteger("Width");
							w.height = windowPosition.getInteger("Height");
						}
					} else {
						System.out.println("Skipping loading window: " + w.windowID);
					}
				}
			}

			// Save Modules Configs
			if (config.containsKey("Modules")) {
				JSONObject modules = config.getJSONObject("Modules");
				loadModules(modules);
			}
			hasLoadedConfig = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
