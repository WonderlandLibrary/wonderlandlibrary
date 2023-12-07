package com.capesapi;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * A simple CapesAPI implementation for Minecraft client developers.
 *
 * @author Matthew Hatcher
 * @author Marco MC
 * @version 2.2.1, March 2017
 */
public class CapesAPI {

	/**
	 * Base URL to fetch the capes from
	 */
	private static final String BASE_URL = "http://capesapi.com/api/v1/%s/getCape";

	/**
	 * Holds a list of UUIDs whose cape is currently being fetched
	 */
	private static final ArrayList<UUID> pendingRequests = new ArrayList<UUID>();

	private static final Map<UUID, ResourceLocation> capes = new HashMap<UUID, ResourceLocation>();

	/**
	 * Fetches a cape for the given player and stores it's ResourceLocation in
	 * the capes map
	 *
	 * @param uuid
	 *            UUID of the player to load the cape for
	 */
	public static void loadCape(final UUID uuid) {
		if (CapesAPI.hasPendingRequests(uuid)) {
			return;
		}

		CapesAPI.setCape(uuid, null);
		String url = String.format(CapesAPI.BASE_URL, uuid);
		final ResourceLocation resourceLocation = new ResourceLocation(
				String.format("capesapi/capes/%s.png", new Date().getTime()));
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null,
				new IImageBuffer() {
					@Override
					public BufferedImage parseUserSkin(BufferedImage image) {
						return image;
					}

					@Override
					public void skinAvailable() {
						CapesAPI.setCape(uuid, resourceLocation);

						// useless reloading Image whenever a Player dies,
						// joins, leaves and re-enters Render range
						// CapesAPI.pendingRequests.remove(uuid);
					}
				});
		textureManager.loadTexture(resourceLocation, threadDownloadImageData);
		CapesAPI.pendingRequests.add(uuid);
	}

	/**
	 * Set the cape of a player
	 *
	 * @param uuid
	 *            UUID of the Player to store the cape for
	 * @param resourceLocation
	 *            ResourceLocation of the cape
	 */
	public static void setCape(UUID uuid, ResourceLocation resourceLocation) {
		CapesAPI.capes.put(uuid, resourceLocation);
	}

	/**
	 * Remove the cape of the user from the cape hashmap
	 */
	public static void deleteCape(UUID uuid) {
		CapesAPI.capes.remove(uuid);
	}

	/**
	 * Get the cape of the user from the cape hashmap
	 *
	 * @return ResourceLocation of the cape or null if none was found
	 */
	public static ResourceLocation getCape(UUID uuid) {
		return capes.getOrDefault(uuid, null);
	}

	/**
	 * Determines whether a player has a cape. If capes were reset recently,
	 * this check also fetches the capes of previously seen players.
	 *
	 * @param uuid
	 *            UUID of the player to check for
	 * @return true if the player has a cape, otherwise false
	 */
	public static boolean hasCape(UUID uuid) {
		boolean hasCape = CapesAPI.capes.containsKey(uuid);
		ResourceLocation resourceLocation = CapesAPI.capes.get(uuid);

		if (hasCape && resourceLocation == null && !CapesAPI.hasPendingRequests(uuid)) {
			CapesAPI.loadCape(uuid);
			return false;
		}

		return hasCape;
	}

	/**
	 * Resets the capes map and downloads capes of players previously seen once
	 * you they are in range
	 */
	public static void resetCapes() {
		for (UUID userId : CapesAPI.capes.keySet()) {
			CapesAPI.capes.put(userId, null);
		}
	}

	/**
	 * Determines wether a player's cape is currently being fetched
	 *
	 * @param uuid
	 *            UUID of the player to check for
	 * @return true if the player's cape is currently being fetched, false
	 *         otherwise
	 */
	private static boolean hasPendingRequests(UUID uuid) {
		return CapesAPI.pendingRequests.contains(uuid);
	}

}