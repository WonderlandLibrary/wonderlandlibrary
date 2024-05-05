package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRenderPlayer;
import best.azura.client.impl.events.EventUpdate;
import best.azura.irc.impl.packets.client.C5EmotePacket;
import best.azura.client.util.other.ServerUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.util.other.EmoteRequest;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;

@ModuleInfo(name = "Emotes", category = Category.RENDER, description = "Toggle the client-sided-emotes on or off")
public class Emotes extends Module {

    /*
     * NOTE: not finished yet (I will never finish lol)
     * - Error
     */

    private static final ArrayList<EmoteRequest> emoteQueue = new ArrayList<>();

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    public static void addToQueue(EmoteRequest emoteRequest, boolean packet) {
        if (packet) {
            C5EmotePacket emotePacket = new C5EmotePacket(null);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("emote", emoteRequest.getEmote());
            jsonObject.addProperty("server", ServerUtil.lastIP);
            emotePacket.setContent(jsonObject);
            Client.INSTANCE.getIrcConnector().sendPacket(emotePacket);
        }

        emoteQueue.add(emoteRequest);
        emoteRequest.startEmote();
    }

    public static void clearEmotes() {
        emoteQueue.clear();
    }

    private void handle(final Event event) {
        for (EmoteRequest emoteRequest : emoteQueue) {

            if (System.currentTimeMillis() - emoteRequest.getEmoteInitTime() > 5000 ||
                        emoteQueue.stream().filter(emoteRequest1 -> emoteRequest1.getUsername().equalsIgnoreCase(emoteRequest.getUsername())).count() > 1) {
                emoteQueue.remove(emoteRequest);
                continue;
            }

            if (event instanceof EventUpdate && emoteRequest.getEmote().equalsIgnoreCase("jerk") && System.currentTimeMillis() - emoteRequest.getEmoteInitTime() > 4500) {
                Entity entity = null;
                for (final Entity entity1 : mc.theWorld.loadedEntityList) {
                    if (!(entity1.getName().equals(emoteRequest.getUsername()))) continue;
                    entity = entity1;
                    break;
                }
                if (entity == null) return;
                for (int i = 1; i < 10; i++) {
                    final double x = (-(Math.sin(Math.toRadians(entity.rotationYaw)) * i)), z = (Math.cos(Math.toRadians(entity.rotationYaw)) * i);
                    mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.CLOUD.getParticleID(), entity.posX, entity.posY,entity.posZ,
                            x / 10.0, 0.4, z / 10.0);
                }
            }

            if (event instanceof EventRenderPlayer) {
                final EventRenderPlayer e = (EventRenderPlayer) event;

                if (e.getTarget().equals("sp") && emoteRequest.getUsername().equals(mc.thePlayer.getName()) ||
                        e.getTarget().equals("mp") && emoteRequest.getUsername().equals(e.getEntityName())) {

                    if (e.getTarget().equals("sp") && mc.gameSettings.showDebugInfo == 0) {
                        return;
                    }
                    playEmote(e, emoteRequest);
                }
            }
        }
    }

    public void playEmote(EventRenderPlayer e, EmoteRequest emote) {
        switch (emote.getEmote()) {
            case "hug": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = -159.5f;
                        e.boxList.get(id).rotateAngleY = -50.5f;
                        e.boxList.get(id).rotateAngleZ = -53.0f;
                        e.boxList.get(9).rotateAngleX = -159.5f;
                        e.boxList.get(9).rotateAngleY = -50.5f;
                        e.boxList.get(9).rotateAngleZ = -54.0f;
                    } catch (Exception ignored) {
                    }
                }
                break;
            }

            case "dab": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = -159.5f;
                        e.boxList.get(id).rotateAngleY = -50.5f;
                        e.boxList.get(id).rotateAngleZ = -150;
                        e.boxList.get(9).rotateAngleX = -159.5f;
                        e.boxList.get(9).rotateAngleZ = -149.5f;
                    } catch (Exception ignored) {
                    }
                }

                if (e.getMode().equals("Pre Model")) {
                    e.bodyRotationPitch = 23;
                }

                break;
            }

            case "jerk": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final float fade = (float) (((System.currentTimeMillis() - emote.getEmoteInitTime()) % 500) / 1000.0);
                        final double swingProgress = Math.sin(Math.sqrt(fade) * Math.PI);
                        e.boxList.get(9).rotateAngleX = -159.5f;
                        e.boxList.get(9).rotateAngleZ = -152.5f;
                        e.boxList.get(9).rotateAngleY = (float) (152.1f - swingProgress);
                    } catch (Exception ignored) {
                    }
                }
                break;
            }
            case "tpose": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = 160.1f;
                        e.boxList.get(id).rotateAngleZ = 143f;
                        e.boxList.get(9).rotateAngleX = -160.1f;
                        e.boxList.get(9).rotateAngleZ = -143f;
                    } catch (Exception ignored) {
                    }
                }
                break;
            }
            case "wave": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final float fade = (float) (((System.currentTimeMillis() - emote.getEmoteInitTime()) % 1000) / 1000.0);
                        final double swingProgress = Math.sin(Math.sqrt(fade) * Math.PI);
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = 160.1f;
                        e.boxList.get(id).rotateAngleY = 143f;
                        e.boxList.get(id).rotateAngleZ = 144.4f - (float) (swingProgress * 0.5f);
                    } catch (Exception ignored) {
                    }
                }
                break;
            }
            case "point": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = -165.0f;
                    } catch (Exception ignored) {
                    }
                }
                break;
            }
            case "test": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(id).rotateAngleX = -165.0f;
                        e.boxList.get(id).rotateAngleY = 0.5f;
                        e.boxList.get(id).rotateAngleZ = 165.0f;
                        e.boxList.get(9).rotateAngleX = 10.5f;
                        e.boxList.get(9).rotateAngleY = 1f;
                        e.boxList.get(9).rotateAngleZ = 0;
                    } catch (Exception ignored) {
                    }
                }
                break;
            }
            case "facepalm": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(9).rotateAngleX = 10.5f;
                        e.boxList.get(9).rotateAngleY = Math.min(1.0f, (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f);
                        e.boxList.get(9).rotateAngleZ = 0;
                    } catch (Exception ignored) {
                    }
                }
                if (e.getMode().equals("Pre Model")) {
                    final float fade = ((System.currentTimeMillis() - emote.getEmoteInitTime()) % 1000f) / 1000f;
                    e.bodyRotationPitch = 15 * Math.min(1.0f, (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f);
                    e.bodyRotationYaw = (float) (-20 + 40 * Math.sin(fade * Math.PI));
                }
                break;
            }
            case "idiot": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        e.boxList.get(9).rotateAngleX = 10.5f * Math.min(1.0f, (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f);
                        e.boxList.get(9).rotateAngleY = Math.min(1.0f, (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f);
                        e.boxList.get(9).rotateAngleZ = 0;
                    } catch (Exception ignored) {
                    }
                }
                if (e.getMode().equals("Pre Model")) {
                    e.bodyRotationPitch = 15 * Math.min(1.0f, (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f);
                }
                break;
            }
            case "creepy": {
                if (e.getMode().equals("Set Rotation Angles")) {
                    try {
                        final int id = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 3 : 10;
                        final int id2 = mc.getNetHandler().getPlayerInfo(e.getEntityName()).getSkinType().equals("default") ? 12 : 13;
                        e.boxList.get(9).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(9).rotateAngleY = (System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(9).rotateAngleZ = 0;
                        e.boxList.get(id).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(id).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(id).rotateAngleZ = 0;
                        e.boxList.get(5).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(5).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(5).rotateAngleZ = 0;
                        e.boxList.get(2).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(2).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(2).rotateAngleZ = 0;
                        e.boxList.get(0).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(0).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(0).rotateAngleZ = 0;
                        e.boxList.get(1).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(1).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(1).rotateAngleZ = 0;
                        e.boxList.get(id2).rotateAngleX = 10.5f * (((System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f));
                        e.boxList.get(id2).rotateAngleY = -(System.currentTimeMillis() - emote.getEmoteInitTime()) / 500f;
                        e.boxList.get(id2).rotateAngleZ = 0;
                    } catch (Exception ignored) {
                    }
                }
                if (e.getMode().equals("Pre Model")) {
                    e.bodyRotationPitch = 0;
                    e.bodyRotationYaw = 360 * (System.currentTimeMillis() - emote.getEmoteInitTime()) / 2500f;
                }
                break;
            }
            case "no": {
                if (e.getMode().equals("Pre Model")) {
                    final float fade = ((System.currentTimeMillis() - emote.getEmoteInitTime()) % 1000f) / 1000f;
                    e.bodyRotationYaw += -20 + 30 * Math.sin(fade * Math.PI);
                }
                break;
            }
            case "yes": {
                if (e.getMode().equals("Pre Model")) {
                    final float fade = ((System.currentTimeMillis() - emote.getEmoteInitTime()) % 1000f) / 1000f;
                    e.bodyRotationPitch = (float) (-20 + 30 * Math.sin(fade * Math.PI));
                }
                break;
            }
        }
    }

}