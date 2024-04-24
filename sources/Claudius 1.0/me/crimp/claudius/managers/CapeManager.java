package me.crimp.claudius.managers;

import me.crimp.claudius.utils.Globals;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class CapeManager implements Globals {

    private final List<UUID> ogCapes = new ArrayList<>();
    private final List<UUID> contributorCapes = new ArrayList<>();




    public CapeManager() {
        try { // og
            contributorCapes.add(UUID.fromString("72874b6d-db90-487a-9676-08096a5077b9"));
            URL capesList = new URL("https://raw.githubusercontent.com/Crimply/Claudius-Capes/main/Ogcape.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                ogCapes.add(UUID.fromString(inputLine));
            }
        } catch (Exception ignored) {
        }
        try { // dev
            URL capesList = new URL("https://raw.githubusercontent.com/Crimply/Claudius-Capes/main/Devcape.txt"); // if ur here can u please consider putting my uuid on the list
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));                     // cos i put a lot of work into this - Crimp
            String inputLine;                                                                                          //  its 72874b6d-db90-487a-9676-08096a5077b9
            while ((inputLine = in.readLine()) != null) {
                contributorCapes.add(UUID.fromString(inputLine));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try { // og
            URL capesList = new URL("https://raw.githubusercontent.com/Crimply/Claudius-Capes/main/Ogcape.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                ogCapes.add(UUID.fromString(inputLine));
            }
        } catch (Exception ignored) {
        }
        try { // dev
            URL capesList = new URL("https://raw.githubusercontent.com/Crimply/Claudius-Capes/main/Devcape.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                contributorCapes.add(UUID.fromString(inputLine));
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isOg(UUID uuid) {
        return this.ogCapes.contains(uuid);
    }

    public boolean isContributor(UUID uuid) {
        return this.contributorCapes.contains(uuid);
    }

}
