package me.nyan.flush.altmanager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AltManager {
    private final File altmgrFolder = new File(Flush.getClientPath(), "altmanager");
    private final File jsonFile = new File(altmgrFolder, "alts.json");
    private final ArrayList<AccountInfo> alts = new ArrayList<>();
    private String status = null;
    private AccountInfo.Type type = AccountInfo.Type.MOJANG;

    public AltManager() {
        if (!altmgrFolder.exists() && altmgrFolder.mkdir()) Flush.LOGGER.info("Created alt manager folder.");
    }

    public void save() {
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            JsonWriter writer = new JsonWriter(new PrintWriter(jsonFile));
            writer.setIndent("  ");
            writer.beginArray();
            for (AccountInfo accountInfo : alts) {
                writer.beginObject();
                writer.name("username").value(accountInfo.getUsername());
                if (!accountInfo.isCracked()) {
                    writer.name("password").value(accountInfo.getPassword());
                }
                writer.name("type").value(accountInfo.getType() == AccountInfo.Type.MICROSOFT ? "msa" : "mojang");
                writer.name("date").value(accountInfo.getCreationDate().getTime());
                if (accountInfo.getDisplayName() != null) {
                    writer.name("displayname").value(accountInfo.getDisplayName());
                }
                if (accountInfo.getFaceImage() != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(accountInfo.getFaceImage(), "png", outputStream);
                    writer.name("face").value(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
                }
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        alts.clear();
        if (!jsonFile.exists()) {
            return;
        }
        try {
            JsonElement json = new JsonParser().parse(new BufferedReader(new FileReader(jsonFile)));
            if (!json.isJsonArray()) {
                return;
            }

            for (JsonElement accountElement : json.getAsJsonArray()) {
                if (!accountElement.isJsonObject()) {
                    continue;
                }
                try {
                    JsonObject accountObject = accountElement.getAsJsonObject();

                    BufferedImage face = null;
                    if (accountObject.has("face")) {
                        try (ByteArrayInputStream stream = new ByteArrayInputStream(
                                Base64.getDecoder().decode(accountObject.get("face").getAsString())
                        )) {
                            face = ImageIO.read(stream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    alts.add(new AccountInfo(
                            accountObject.get("username").getAsString(),
                            accountObject.has("password") ? accountObject.get("password").getAsString() : null,
                            accountObject.has("type") && accountObject.get("type").getAsString().equals("msa") ?
                                    AccountInfo.Type.MICROSOFT : AccountInfo.Type.MOJANG,
                            new Date(accountObject.get("date").getAsLong()),
                            accountObject.has("displayname") ? accountObject.get("displayname").getAsString() : null,
                            face
                    ));
                } catch (NullPointerException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAlt(AccountInfo accountInfo) {
        for (AccountInfo account : alts) {
            if (account.getUsername().equals(accountInfo.getUsername())) {
                if (account.isCracked() || account.getPassword().equals(accountInfo.getPassword())) {
                    break;
                }
                account.setPassword(accountInfo.getPassword());
                account.setCreationDate(accountInfo.getCreationDate());
                save();
                return;
            }
        }
        alts.add(0, accountInfo);
        save();
    }

    public boolean isValidCrackedAlt(String username) {
        for (char ch : username.toCharArray())
            if (!(Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_')) {
                return false;
            }
        return username.length() >= 3 && username.length() <= 16;
    }

    public ArrayList<AccountInfo> getAlts() {
        return alts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountInfo.Type getType() {
        return type;
    }

    public void setType(AccountInfo.Type type) {
        this.type = type;
    }

    public boolean isMicrosoft() {
        return type == AccountInfo.Type.MICROSOFT;
    }
}