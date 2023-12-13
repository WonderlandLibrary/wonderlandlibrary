package best.azura.client.impl.ui.font;

import best.azura.client.impl.Client;
import best.azura.client.util.other.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;


public enum Fonts {
	
	INSTANCE;
	
	public FontRenderer arial20, arial15, arial15bold, arial12, hudFont, hudFontBold, guiIngameFont, gadugiFont, robloxFontBold, robloxFontBoldBig, robloxFont;
	
	Fonts() {
		reload();
	}
	
	public void reload() {
		setupPresets();
		arial20 = new FontRenderer(new Font("Arial", Font.PLAIN, 20));
		arial15 = new FontRenderer(new Font("Roboto", Font.PLAIN, 15));
		arial15bold = new FontRenderer(new Font("Arial", Font.BOLD, 15));
		arial12 = new FontRenderer(new Font("Arial", Font.PLAIN, 12));
		hudFontBold = loadFromCustom(new File(Client.INSTANCE.getConfigManager().getFontDirectory(), "hud"), Font.BOLD, 20);
		hudFont = loadFromCustom(new File(Client.INSTANCE.getConfigManager().getFontDirectory(), "hud"), Font.PLAIN, 18);
		guiIngameFont = new FontRenderer(new Font("Arial", Font.PLAIN, 15));
		gadugiFont = new FontRenderer(new Font("Gadugi", Font.BOLD, 16));
		robloxFont = new FontRenderer(getFromInputStream(Fonts.class.getClassLoader().getResourceAsStream("assets/minecraft/custom/fonts/arial.ttf"),
				Font.TRUETYPE_FONT, 10, Font.PLAIN));
		robloxFontBold = new FontRenderer(getFromInputStream(Fonts.class.getClassLoader().getResourceAsStream("assets/minecraft/custom/fonts/arial.ttf"),
				Font.TRUETYPE_FONT, 20, Font.BOLD));
		robloxFontBoldBig = new FontRenderer(getFromInputStream(Fonts.class.getClassLoader().getResourceAsStream("assets/minecraft/custom/fonts/arial.ttf"),
				Font.TRUETYPE_FONT, 26, Font.BOLD));
	}
	
	public void setupPresets() {
		final HashMap<String, Float> map = new HashMap<>();
		map.put("hud", 20F);
		for (final String font : map.keySet()) {
			final File file = new File(Client.INSTANCE.getConfigManager().getFontDirectory(), font);
			if (file.mkdirs()) {
				final File ttfFile = new File(file, "font.ttf"), jsonFile = new File(file, "font.json");
				FileUtil.writeContentToFile(jsonFile, "{\n" +
						"  \"size\": " + map.get(font) + "\n" +
						"}", false);
				try {
					final InputStream stream = Fonts.class.getClassLoader().getResourceAsStream("assets/minecraft/custom/fonts/arial.ttf");
					assert stream != null;
					Files.copy(
							stream,
							ttfFile.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public FontRenderer loadFromCustom(final File fontDir, final int style, final int size) {
		final File ttf = getTTF(fontDir);
		final File json = getJson(fontDir);
		if (ttf.getAbsolutePath().equals(fontDir.getAbsolutePath()) || json.getAbsolutePath().equals(fontDir.getAbsolutePath())) {
			new Exception("Failed to load " + fontDir.getAbsolutePath()).printStackTrace();
			return new FontRenderer(new Font("Arial", style, size));
		}
		try {
			final InputStream fontStream = new FileInputStream(ttf), jsonStream = new FileInputStream(json);
			final Gson gson = new Gson();
			final JsonObject object = gson.fromJson(new InputStreamReader(jsonStream), JsonObject.class);
			return new FontRenderer(getFromInputStream(fontStream, Font.TRUETYPE_FONT, object.get("size").getAsFloat(), style));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new FontRenderer(new Font("Arial", style, size));
	}
	
	public File getTTF(final File directory) {
		if (directory.listFiles() != null)
			for (final File file : Objects.requireNonNull(directory.listFiles()))
				if (file.getName().toLowerCase().endsWith(".ttf") || file.getName().toLowerCase().endsWith(".otf")
						|| file.getName().toLowerCase().endsWith(".ttc") || file.getName().toLowerCase().endsWith(".fon"))
					return file;
		return directory;
	}
	
	public File getJson(final File directory) {
		if (directory.listFiles() != null)
			for (final File file : Objects.requireNonNull(directory.listFiles()))
				if (file.getName().toLowerCase().endsWith(".json"))
					return file;
		return directory;
	}
	
	public Font getFromInputStream(final InputStream is, final int type, final float size, final int style) {
		try {
			Font awtFont = null;
			try {
				awtFont = Font.createFont(type, is);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			if (awtFont != null) {
				return awtFont.deriveFont(style, size);
			} else {
				throw new NullPointerException("No font found!");
			}
			//return Font.createFont(type, is).deriveFont(style, size);
		} catch (Exception e) {
			e.printStackTrace();
			return new Font("Arial", style, (int) size);
		}
	}
	
}
