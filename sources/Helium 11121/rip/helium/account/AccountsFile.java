package rip.helium.account;

import com.google.gson.*;
import com.thealtening.AltService;
import org.apache.commons.io.FileUtils;
import rip.helium.Helium;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author antja03
 */
public class AccountsFile {

    private static final File file = new File(Helium.clientDir + File.separator + "accounts.json");

    public static void save() {
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonArray accountArray = new JsonArray();
        for (Account account : Helium.instance.accountManager.getAccountRegistry()) {
            JsonObject accountObject = new JsonObject();
            accountObject.addProperty("username", account.getUsername());
            accountObject.addProperty("password", account.getPassword());
            accountObject.addProperty("service", account.getService().name());
            accountArray.add(accountObject);
        }

        try {
            Gson gson = new GsonBuilder().create();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(accountArray));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!file.exists())
            return;

        Helium.instance.accountManager.getAccountRegistry().clear();

        try {
            Gson gson = new GsonBuilder().create();
            JsonArray accountArray = gson.fromJson(FileUtils.readFileToString(file), JsonArray.class);

            for (JsonElement element : accountArray) {
                if (element instanceof JsonObject) {
                    JsonObject accountObject = (JsonObject) element;
                    String username = accountObject.get("username").getAsString();
                    String password = accountObject.get("password").getAsString();
                    AltService.EnumAltService service = AltService.EnumAltService.MOJANG;
                    if (accountObject.get("service").getAsString().equals("THEALTENING"))
                        service = AltService.EnumAltService.THEALTENING;

                    Helium.instance.accountManager.getAccountRegistry().add(
                            new Account(username, password, service));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
