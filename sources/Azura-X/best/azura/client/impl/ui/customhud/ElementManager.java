package best.azura.client.impl.ui.customhud;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.customhud.impl.*;
import best.azura.client.util.other.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ElementManager {
	private final ArrayList<Element> availableElementTypes = new ArrayList<>(), renderedElements = new ArrayList<>();
	private int targetHudIndex = 0;
	
	public ElementManager() {
		availableElementTypes.addAll(Arrays.asList(
				new ModuleListElement(),
				new PotionEffectElement(),
				new ImageElement(),
				new TextElement(),
				new TargetHudElement(),
				new ArmorHudElement(),
				new BalanceElement(),
				new ClockElement()
		));
		load();
	}
	
	private void load() {
		final Gson gson = new Gson();
		final File file = new File(Client.INSTANCE.getConfigManager().getClientDirectory(), "hud.json");
		renderedElements.clear();
		try {
			final JsonObject object = gson.fromJson(new FileReader(file), JsonObject.class);
			if (object.has("elements") && object.get("elements").isJsonArray()) {
				JsonArray array = object.get("elements").getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					if (!array.get(i).isJsonObject()) continue;
					final JsonObject object1 = array.get(i).getAsJsonObject();
					final Element element;
					if (object1.has("name") && (element = getElement(object1.get("name").getAsString())) != null) {
						addElement(element, true, object1);
						renderedElements.get(renderedElements.size() - 1).onAdd();
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		for (Element e : renderedElements) {
			try {
				array.add(e.buildJson());
			} catch (Exception ignored) {
			}
		}
		object.add("elements", array);
		FileUtil.writeContentToFile(new File(Client.INSTANCE.getConfigManager().getClientDirectory(), "hud.json"), new GsonBuilder().setPrettyPrinting().create().toJson(object), true);
	}
	
	//TODO: add the custom hud gui where you can add / remove elements
	public ArrayList<Element> getElements() {
		return renderedElements;
	}
	
	public Element getElement(String name) {
		return availableElementTypes.stream().filter(e -> e.isEnabled() && e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public ArrayList<Element> getAvailableElementTypes() {
		return availableElementTypes;
	}
	
	public void addElement(Element e, boolean load, JsonObject object) {
		final Element clonedElement = e.copy();
		if (clonedElement == null) return;
		renderedElements.add(clonedElement);
		if (load) clonedElement.loadFromJson(object);
		if (clonedElement instanceof TargetHudElement) {
			((TargetHudElement) clonedElement).setIndex(targetHudIndex);
			targetHudIndex++;
		}
	}
	
	public void addElement(Element e) {
		final Element clonedElement = e.copy();
		if (clonedElement == null) return;
		renderedElements.add(clonedElement);
		clonedElement.onAdd();
		if (clonedElement instanceof TargetHudElement) {
			((TargetHudElement) clonedElement).setIndex(targetHudIndex);
			targetHudIndex++;
		}
	}
}