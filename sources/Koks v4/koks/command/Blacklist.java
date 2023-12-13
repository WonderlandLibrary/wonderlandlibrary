package koks.command;

import koks.api.registry.command.Command;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Command.Info(name = "Blacklist", description = "You can check if a server is in a blacklist")
public class Blacklist extends Command {

    public static final AtomicBoolean blacklisted = new AtomicBoolean();

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            sendHelp(this, "[IP]");
        } else if (args.length == 1) {
            blacklisted.set(false);
            final Thread blacklist = new Thread(() -> {
                try {
                    final URL url = new URL("https://sessionserver.mojang.com/blockedservers");
                    final InputStream inputStream;
                    inputStream = url.openStream();
                    final Scanner scanner = new Scanner(inputStream);
                    while (scanner.hasNextLine()) {
                        final String server = scanner.nextLine();
                        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                        messageDigest.reset();
                        messageDigest.update(args[0].getBytes(StandardCharsets.UTF_8));
                        if (server.equalsIgnoreCase(String.format("%040x", new BigInteger(1, messageDigest.digest())))) {
                            blacklisted.set(true);
                        }
                    }
                    scanner.close();
                    sendMessage((blacklisted.get() ? "§c" : "§a") + args[0] + " is " + (!blacklisted.get() ? "not " : "") + "blacklisted!");
                } catch (IOException | NoSuchAlgorithmException exception) {
                    exception.printStackTrace();
                }
            });
            blacklist.start();
        } else {
            return false;
        }
        return true;
    }
}
