// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import net.minecraft.client.Minecraft;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonParseException;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import java.io.IOException;
import java.util.Collections;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import com.google.gson.JsonArray;
import moonsense.utils.ColorObject;
import net.minecraft.util.Vec3;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.File;
import java.util.ArrayList;

public class WaypointManager extends ArrayList<Waypoint>
{
    private File waypointsFile;
    public static String currentWorldName;
    
    public WaypointManager() {
        this.waypointsFile = new File("Moonsense Client", "waypoints.json");
    }
    
    public void load() {
        this.createStructure();
        final JsonObject moduleJson = this.loadJsonFile(this.waypointsFile);
        final JsonArray jsonArray = moduleJson.getAsJsonArray("waypoints");
        for (final JsonElement elem : jsonArray) {
            final JsonObject obj = elem.getAsJsonObject();
            final String name = obj.get("name").getAsString();
            final JsonObject posObj = (JsonObject)obj.get("position");
            final float x = posObj.get("posX").getAsFloat();
            final float y = posObj.get("posY").getAsFloat();
            final float z = posObj.get("posZ").getAsFloat();
            final Vec3 pos = new Vec3(x, y, z);
            final String worldName = obj.get("worldName").getAsString();
            final int dimension = obj.get("dimension").getAsInt();
            final boolean visible = obj.get("visible").getAsBoolean();
            final boolean forces = obj.get("forces").getAsBoolean();
            final JsonObject colorObj = (JsonObject)obj.get("color");
            final int color = colorObj.get("color").getAsInt();
            final boolean chroma = colorObj.get("chroma").getAsBoolean();
            final int chromaSpeed = colorObj.get("chromaSpeed").getAsInt();
            final ColorObject colorObject = new ColorObject(color, chroma, chromaSpeed);
            final Waypoint wp = new Waypoint(name, pos, worldName, dimension, visible, forces);
            wp.color = colorObject;
            this.add(wp);
        }
    }
    
    public void save() {
        this.createStructure();
        final JsonObject waypointsJson = new JsonObject();
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.waypointsFile));
            final JsonArray jsonArray = new JsonArray();
            try {
                for (final Waypoint wp : this) {
                    final JsonObject waypointObject = new JsonObject();
                    waypointObject.addProperty("name", wp.getName());
                    final JsonObject pos = new JsonObject();
                    pos.addProperty("posX", wp.getLocation().xCoord);
                    pos.addProperty("posY", wp.getLocation().yCoord);
                    pos.addProperty("posZ", wp.getLocation().zCoord);
                    waypointObject.add("position", pos);
                    waypointObject.addProperty("worldName", wp.getWorld());
                    waypointObject.addProperty("dimension", wp.getDimension());
                    waypointObject.addProperty("visible", wp.isVisible());
                    waypointObject.addProperty("forces", wp.isForced());
                    final JsonObject color = new JsonObject();
                    color.addProperty("color", wp.getColor().getColor());
                    color.addProperty("chroma", wp.getColor().isChroma());
                    color.addProperty("chromaSpeed", wp.getColor().getChromaSpeed());
                    waypointObject.add("color", color);
                    jsonArray.add(waypointObject);
                }
                waypointsJson.add("waypoints", jsonArray);
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(waypointsJson.toString())));
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
    
    private void getNonNull(final JsonObject jsonObject, final String key, final Consumer<JsonElement> consumer) {
        if (jsonObject != null && jsonObject.get(key) != null) {
            consumer.accept(jsonObject.get(key));
        }
    }
    
    private void createStructure() {
        if (!this.waypointsFile.exists() || !MoonsenseClient.dir.exists()) {
            MoonsenseClient.dir.mkdirs();
            try {
                this.waypointsFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private JsonObject loadJsonFile(final File file) {
        if (!file.exists()) {
            return null;
        }
        final JsonElement fileElement = new JsonParser().parse(this.getFileContents(file));
        if (fileElement == null || fileElement.isJsonNull()) {
            throw new JsonParseException("File \"" + file.getName() + "\" is null!");
        }
        return fileElement.getAsJsonObject();
    }
    
    private String getFileContents(final File file) {
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                try {
                    final StringBuilder builder = new StringBuilder();
                    String nextLine;
                    while ((nextLine = reader.readLine()) != null) {
                        builder.append(nextLine);
                    }
                    return builder.toString().equals("") ? "{}" : builder.toString();
                }
                finally {
                    if (Collections.singletonList(reader).get(0) != null) {
                        reader.close();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "{}";
    }
    
    public static String getWaypointWorld() {
        String string = "";
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().getCurrentServerData() == null) {
            string = "sp:" + WaypointManager.currentWorldName;
        }
        else if (Minecraft.getMinecraft().getCurrentServerData() != null) {
            string = String.valueOf(string) + "mp:" + Minecraft.getMinecraft().getCurrentServerData().serverIP;
        }
        return string;
    }
}
