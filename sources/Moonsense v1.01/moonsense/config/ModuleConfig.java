// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.config;

import java.awt.geom.Point2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import moonsense.features.SCAbstractRenderModule;
import moonsense.config.utils.AnchorPoint;
import moonsense.MoonsenseClient;
import java.util.Iterator;
import java.io.IOException;
import java.util.Collections;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import moonsense.settings.SettingWrapper;
import moonsense.settings.Setting;
import moonsense.features.ModuleManager;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import moonsense.config.utils.Position;
import java.util.Map;
import moonsense.features.SCModule;
import java.util.Set;
import moonsense.config.utils.Config;

public class ModuleConfig extends Config
{
    public static final ModuleConfig INSTANCE;
    private final Set<SCModule> enabled;
    private final Map<SCModule, Position> positions;
    private final Map<SCModule, Float> scales;
    
    static {
        INSTANCE = new ModuleConfig();
    }
    
    public ModuleConfig() {
        super("modules", "json", 0.1);
        this.enabled = (Set<SCModule>)Sets.newLinkedHashSet();
        this.positions = (Map<SCModule, Position>)Maps.newHashMap();
        this.scales = (Map<SCModule, Float>)Maps.newHashMap();
    }
    
    @Override
    public void saveConfig() {
        this.createStructure();
        final JsonObject configFileJson = new JsonObject();
        configFileJson.addProperty("version", this.version);
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            try {
                for (final SCModule module : ModuleManager.INSTANCE.modules) {
                    final JsonObject moduleObject = new JsonObject();
                    moduleObject.addProperty("enabled", this.isEnabled(module));
                    if (module.isRender()) {
                        moduleObject.addProperty("posX", this.getPosition(module).getX());
                        moduleObject.addProperty("posY", this.getPosition(module).getY());
                        moduleObject.addProperty("anchorPoint", this.getPosition(module).getAnchorPoint().name());
                    }
                    if (module.settings.size() > 0) {
                        final JsonObject propertiesObject = new JsonObject();
                        for (final Setting setting : module.settings) {
                            if (!setting.hasValue()) {
                                continue;
                            }
                            SettingWrapper.addSettingKey(propertiesObject, setting, setting.getObject());
                        }
                        moduleObject.add("properties", propertiesObject);
                    }
                    configFileJson.add(module.getKey(), moduleObject);
                }
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(configFileJson.toString())));
            }
            finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
            if (Collections.singletonList(writer).get(0) != null) {
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadConfig() {
        this.createStructure();
        final JsonObject configFileJson = this.loadJsonFile(this.configFile);
        this.getNonNull(configFileJson, "version", jsonElement -> MoonsenseClient.info("Detected " + this.name + " version: " + this.version + " => " + jsonElement.getAsDouble(), new Object[0]));
        for (final SCModule module : ModuleManager.INSTANCE.modules) {
            final JsonObject moduleObject;
            final SCModule module2;
            final SCModule module3;
            final SCModule module4;
            final SCModule module5;
            JsonObject propertiesObject;
            Iterator var3;
            Setting setting;
            this.getNonNull(configFileJson, module.getKey(), jsonElement -> {
                moduleObject = jsonElement.getAsJsonObject();
                this.getNonNull(moduleObject, "enabled", element -> this.setEnabled(module2, element.getAsBoolean()));
                if (module2.isRender()) {
                    this.getNonNull(moduleObject, "posX", element -> this.setPosition(module3, this.getPosition(module3).getAnchorPoint(), (float)element.getAsInt(), this.getPosition(module3).getY()));
                    this.getNonNull(moduleObject, "posY", element -> this.setPosition(module4, this.getPosition(module4).getAnchorPoint(), this.getPosition(module4).getX(), (float)element.getAsInt()));
                    this.getNonNull(moduleObject, "anchorPoint", element -> this.setPosition(module5, AnchorPoint.valueOf(element.getAsString()), this.getPosition(module5).getX(), this.getPosition(module5).getY()));
                }
                if (module2.settings.size() > 0) {
                    propertiesObject = moduleObject.getAsJsonObject("properties");
                    var3 = module2.settings.iterator();
                    while (var3.hasNext()) {
                        setting = var3.next();
                        this.getNonNull(propertiesObject, setting.getKey(), element -> SettingWrapper.setValue(setting, element));
                    }
                }
            });
        }
    }
    
    public void setEnabled(final SCModule module, final boolean enabled) {
        if (enabled) {
            this.enabled.add(module);
            module.onEnable();
        }
        else {
            this.enabled.remove(module);
            module.onDisable();
        }
    }
    
    public void toggle(final SCModule module) {
        this.setEnabled(module, !this.isEnabled(module));
    }
    
    public boolean isEnabled(final SCModule module) {
        return module != null && this.enabled.contains(module);
    }
    
    public Set<SCModule> getEnabled() {
        return this.enabled;
    }
    
    public void setPosition(final SCModule module, final AnchorPoint anchorPoint, final float x, final float y) {
        this.positions.put(module, new Position(anchorPoint, x, y));
    }
    
    public Position getPosition(final SCModule module) {
        try {
            float x = 0.0f;
            float y = 0.0f;
            switch (((SCAbstractRenderModule)module).getDefaultPosition()) {
                case TOP_LEFT: {
                    x = 3.0f;
                    y = 3.0f;
                    break;
                }
                case TOP_CENTER: {
                    x = (float)(-((SCAbstractRenderModule)module).getWidth() / 2);
                    y = 3.0f;
                    break;
                }
                case TOP_RIGHT: {
                    x = -((SCAbstractRenderModule)module).getWidth() - 3.0f;
                    y = 3.0f;
                    break;
                }
                case CENTER_LEFT: {
                    x = 3.0f;
                    y = (float)(-((SCAbstractRenderModule)module).getHeight() / 2);
                }
                case CENTER: {
                    x = (float)(-((SCAbstractRenderModule)module).getWidth() / 2);
                    y = (float)(-((SCAbstractRenderModule)module).getHeight() / 2);
                    break;
                }
                case CENTER_RIGHT: {
                    x = -((SCAbstractRenderModule)module).getWidth() - 3.0f;
                    y = (float)(-((SCAbstractRenderModule)module).getHeight() / 2);
                    break;
                }
                case BOTTOM_LEFT: {
                    x = 3.0f;
                    y = -((SCAbstractRenderModule)module).getHeight() - 3.0f;
                    break;
                }
                case BOTTOM_CENTER: {
                    x = (float)(-((SCAbstractRenderModule)module).getWidth() / 2);
                    y = -((SCAbstractRenderModule)module).getHeight() - 3.0f;
                    break;
                }
                case BOTTOM_RIGHT: {
                    x = -((SCAbstractRenderModule)module).getWidth() - 3.0f;
                    y = -((SCAbstractRenderModule)module).getHeight() - 3.0f;
                    break;
                }
            }
            return this.positions.getOrDefault(module, new Position(((SCAbstractRenderModule)module).getDefaultPosition(), x, y));
        }
        catch (Exception e) {
            return this.positions.getOrDefault(module, new Position(AnchorPoint.TOP_CENTER, 0.0f, 3.0f));
        }
    }
    
    public float getActualX(final SCModule module) {
        return this.getPosition(module).getAnchorPoint().getX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) + this.getPosition(module).getX();
    }
    
    public float getActualY(final SCModule module) {
        return this.getPosition(module).getAnchorPoint().getY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) + this.getPosition(module).getY();
    }
    
    public void setClosestAnchorPoint(final SCModule module) {
        final float actualX = this.getActualX(module);
        final float actualY = this.getActualY(module);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int maxX = sr.getScaledWidth();
        final int maxY = sr.getScaledHeight();
        double shortestDistance = -1.0;
        AnchorPoint closestAnchorPoint = AnchorPoint.CENTER;
        AnchorPoint[] values;
        for (int length = (values = AnchorPoint.values()).length, i = 0; i < length; ++i) {
            final AnchorPoint anchorPoint = values[i];
            final double distance = Point2D.distance(actualX, actualY, anchorPoint.getX(maxX), anchorPoint.getY(maxY));
            if (shortestDistance == -1.0 || distance < shortestDistance) {
                closestAnchorPoint = anchorPoint;
                shortestDistance = distance;
            }
        }
        final float x = actualX - closestAnchorPoint.getX(maxX);
        final float y = actualY - closestAnchorPoint.getY(maxY);
        this.setPosition(module, closestAnchorPoint, x, y);
    }
    
    public Float getScale(final SCModule module) {
        return this.scales.getOrDefault(module, 1.0f);
    }
}
