package best.azura.client.impl.module.impl.render.cape;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.AnimatedTexture;
import best.azura.client.impl.ui.Texture;
import best.azura.client.util.crypt.Crypter;
import best.azura.client.util.textures.JordanTextureUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventRenderLayer;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import best.azura.irc.core.entities.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import me.errordev.imagelib.GifConverter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ModuleInfo(name = "Cape", description = "Render a cape", category = Category.RENDER)
public class Cape extends Module {

    private final BooleanValue alpha = new BooleanValue("Alpha", "Alpha cape mode.", false);

    @Override
    public void onDisable() {
        super.onDisable();
        previousCapeLocations.clear();
        alphaCapeRendererHashMap.clear();
        downloadedCapes.clear();
        animatedCapeRendererHashMap.clear();
    }

    private static final HashMap<String, ResourceLocation> previousCapeLocations = new HashMap<>();
    private static final HashMap<String, AlphaCapeRendererImpl> alphaCapeRendererHashMap = new HashMap<>();
    private static final HashMap<String, AnimatedCapeRendererImpl> animatedCapeRendererHashMap = new HashMap<>();
    private static final HashMap<String, ResourceLocation> downloadedCapes = new HashMap<>();

    @SuppressWarnings({"unused", "SpellCheckingInspection", "rawtypes"})
    @EventHandler
    public final Listener<EventRenderLayer> eventRenderLayerListener = e -> {
        if (e.getMode().equals("Pre")) {
            if (e.getType().equals("sp")) {
                //previousCapeLocations.putIfAbsent(mc.thePlayer.getUniqueID().toString(), mc.thePlayer.getLocationCape());

                if (alpha.getObject())
                    alphaCapeRendererHashMap.putIfAbsent(mc.thePlayer.getUniqueID().toString(), new AlphaCapeRendererImpl((RenderPlayer) ((RendererLivingEntity) mc.getRenderManager().getEntityRenderObject(mc.thePlayer))));

                animatedCapeRendererHashMap.putIfAbsent(mc.thePlayer.getUniqueID().toString(), new AnimatedCapeRendererImpl((RenderPlayer) ((RendererLivingEntity) mc.getRenderManager().getEntityRenderObject(mc.thePlayer))));

                if (mc.thePlayer.getLocationCape() == null)
                    mc.thePlayer.setLocationOfCape(new ResourceLocation("custom/" + (alpha.getObject() ? "azuracapealpha" : "azuracape") + ".png"));
                if (e.layerRenderer instanceof LayerCape && alpha.getObject() && alphaCapeRendererHashMap.containsKey(mc.thePlayer.getUniqueID().toString()))
                    e.layerRenderer = alphaCapeRendererHashMap.get(mc.thePlayer.getUniqueID().toString());
                if (e.layerRenderer instanceof LayerCape && animatedCapeRendererHashMap.containsKey(mc.thePlayer.getUniqueID().toString()))
                    e.layerRenderer = animatedCapeRendererHashMap.get(mc.thePlayer.getUniqueID().toString());
            } else if (e.getType().equals("mp")) {
                if (Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().entrySet().stream().anyMatch(stringUserEntry -> stringUserEntry.getValue().getMinecraftName().equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getName()))) {
                    previousCapeLocations.putIfAbsent(e.getEntityPlayer().getUniqueID().toString(), ((EntityOtherPlayerMP) e.getEntityPlayer()).getLocationCape());

                    if (alpha.getObject())
                        alphaCapeRendererHashMap.putIfAbsent(e.getEntityPlayer().getUniqueID().toString(), new AlphaCapeRendererImpl((RenderPlayer) ((RendererLivingEntity) mc.getRenderManager().getEntityRenderObject(e.getEntityPlayer()))));

                    if (((EntityOtherPlayerMP) e.getEntityPlayer()).getLocationCape() == null)
                        ((EntityOtherPlayerMP) e.getEntityPlayer()).setLocationOfCape(new ResourceLocation("custom/" + (alpha.getObject() ? "azuracapealpha" : "azuracape") + ".png"));

                    if (e.layerRenderer instanceof LayerCape && alpha.getObject() && alphaCapeRendererHashMap.containsKey(e.getEntityPlayer().getUniqueID().toString()) && ((EntityOtherPlayerMP) e.getEntityPlayer()).getLocationCape() == null)
                        e.layerRenderer = alphaCapeRendererHashMap.get(e.getEntityPlayer().getUniqueID().toString());

                    if (e.layerRenderer instanceof LayerCape && animatedCapeRendererHashMap.containsKey(e.getEntityPlayer().getUniqueID().toString()))
                        e.layerRenderer = animatedCapeRendererHashMap.get(e.getEntityPlayer().getUniqueID().toString());
                }
            }
        }
        if (e.getMode().equals("Post")) {
            if (e.getType().equals("sp")) {
                if (previousCapeLocations.containsKey(mc.thePlayer.getUniqueID().toString()))
                    mc.thePlayer.setLocationOfCape(previousCapeLocations.get(mc.thePlayer.getUniqueID().toString()));
            } else if (e.getType().equals("mp")) {
                if (previousCapeLocations.containsKey(e.getEntityPlayer().getUniqueID().toString()))
                    ((EntityOtherPlayerMP) e.getEntityPlayer()).setLocationOfCape(previousCapeLocations.get(e.getEntityPlayer().getUniqueID().toString()));
            }
        }
    };

    private static AnimatedTexture loadGif(InputStream stream) {
        try {
            return new AnimatedTexture(GifConverter.readGifAsTextures(stream));
        } catch (IOException ignore) {}
        return null;
    }


    private static String getStringAfter(String input) {
        if (input.contains(",")) return input.split(",")[1];
        return input;
    }

    public static void downloadCape(final AbstractClientPlayer abstractClientPlayer) {
        List<Map.Entry<String, User>> userList = Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().entrySet().stream().filter(stringUserEntry -> stringUserEntry.getValue().getMinecraftName().equalsIgnoreCase(abstractClientPlayer.getGameProfile().getName())).collect(Collectors.toList());
        if (!userList.isEmpty()) {
            User user = userList.get(0).getValue();

            // Variable for the URL.
            URL capeUrl = null;

            try {
                // Parse the String into a URL.
                capeUrl = new URL("https://api.azura.best/users/cape");
            } catch (Exception ignore) {
            }

            // Variable for the actual connection.
            HttpsURLConnection urlConnection;

            try {
                // Create a URL Connection with the URL.
                if (capeUrl != null) {
                    urlConnection = (HttpsURLConnection) capeUrl.openConnection();

                    // Set RequestInfo.
                    urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                    urlConnection.addRequestProperty("Accept", "application/json");
                    urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));
                    urlConnection.addRequestProperty("Username", Crypter.encode(user.getUsername()));

                    if (!urlConnection.getDoOutput()) {
                        // Set its primary task to output.
                        urlConnection.setDoOutput(true);
                    }

                    // Check if it's null.

                    if (urlConnection.getResponseCode() == 429) {
                        capeUrl = new URL("https://azura.best/api/users/capea");

                        // Create a URL Connection with the URL.
                        urlConnection = (HttpsURLConnection) capeUrl.openConnection();

                        // Set RequestInfo.
                        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 Gecko/20100316 Firefox/3.6.2 AzuraX/" + Client.VERSION);
                        urlConnection.addRequestProperty("Accept", "application/json");
                        urlConnection.addRequestProperty("Session-Token", Crypter.encode(Client.INSTANCE.sessionToken));
                        urlConnection.addRequestProperty("Username", Crypter.encode(user.getUsername()));

                        if (!urlConnection.getDoOutput()) {
                            // Set its primary task to output.
                            urlConnection.setDoOutput(true);
                        }
                    }

                    if (urlConnection.getResponseCode() == 403) {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Error!", "Cloudflare blocked the Request!", 5000, Type.ERROR));
                        return;
                    }

                    // Variable to convert to JSON.
                    JsonStreamParser jsonStreamParser = null;

                    try {
                        // Create the Parser.
                        jsonStreamParser = new JsonStreamParser(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                    } catch (Exception ignore) {
                    }

                    // Check if it's null.
                    if (jsonStreamParser != null && jsonStreamParser.hasNext()) {
                        JsonElement jsonElement = jsonStreamParser.next();
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            if (jsonObject.has("success")) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    if (jsonObject.has("animated")) {
                                        final ArrayList<Texture> textures = new ArrayList<>();
                                        final String animated = jsonObject.get("animated").getAsString().replace("data:image/png;base64,", "");
                                        for (final String string : animated.split(";")) {
                                            final Texture texture = new Texture(ImageIO.read(new ByteArrayInputStream(
                                                    DatatypeConverter.parseBase64Binary(string.split(", delay:")[0]))), true);
                                            texture.setDelay(Integer.parseInt(string.split(", delay:")[1]));
                                            textures.add(texture);
                                        }
                                        if (abstractClientPlayer.getUniqueID() != null) {
                                            if (animatedCapeRendererHashMap.containsKey(abstractClientPlayer.getUniqueID().toString()))
                                                animatedCapeRendererHashMap.get(abstractClientPlayer.getUniqueID().toString()).setAnimatedTexture(new AnimatedTexture(textures.toArray(new Texture[0])));
                                            else {
                                                AnimatedCapeRendererImpl animatedCapeRenderer =
                                                        new AnimatedCapeRendererImpl((RenderPlayer) ((RendererLivingEntity) Minecraft.getMinecraft().getRenderManager()
                                                                .getEntityRenderObject(abstractClientPlayer)));
                                                animatedCapeRenderer.setAnimatedTexture(new AnimatedTexture(textures.toArray(new Texture[0])));
                                                animatedCapeRendererHashMap.put(abstractClientPlayer.getUniqueID().toString(), animatedCapeRenderer);
                                            }
                                        }
                                    } else if (jsonObject.has("cape")) {
                                        abstractClientPlayer.setLocationOfCape(JordanTextureUtil.getResourceFromImage(ImageIO.read(new ByteArrayInputStream(
                                                DatatypeConverter.parseBase64Binary(getStringAfter(jsonObject.get("cape").getAsString()))))));
                                        previousCapeLocations.put(abstractClientPlayer.getName(), abstractClientPlayer.getLocationCape());
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception ignore) {}
        }
    }

}
