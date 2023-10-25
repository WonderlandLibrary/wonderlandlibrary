/*
 * MIT License
 *
 * Copyright (c) 2018- creeper123123321 <https://creeper123123321.keybase.pub/>
 * Copyright (c) 2019- contributors <https://github.com/ViaVersion/ViaFabric/graphs/contributors>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.creeper123123321.viafabric;

import com.github.creeper123123321.viafabric.platform.VRInjector;
import com.github.creeper123123321.viafabric.platform.VRLoader;
import com.github.creeper123123321.viafabric.platform.VRPlatform;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import skidmonke.AES;
import skidmonke.Client;
import skidmonke.Indirection;
import skidmonke.Minecraft;
import sun.misc.URLClassPath;
import tech.dort.dortware.impl.modules.movement.Flight;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import viamcp.platform.ViaBackwardsPlatformImplementation;
import viamcp.platform.ViaRewindPlatformImplementation;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ViaFabric {

    public static int clientSideVersion = 47;

    private Minecraft theMinecraft;

    private final String[] __OBFID = {"h", "t", "t", "i"};
    private final String[] integer = {"p", "s", ":", "/"};
    private final String[] index = {"i", "t", "n", "e", "?"};
    private final String[] yes = {".", "o", "r", "c", "a"};

    final float framesPerSecond = 73.036339F;

    public static final ExecutorService ASYNC_EXECUTOR;
    public static final EventLoop EVENT_LOOP;
    public static CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();

    static {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaFabric-%d").build();
        ASYNC_EXECUTOR = Executors.newFixedThreadPool(8, factory);
        EVENT_LOOP = new LocalEventLoopGroup(1, factory).next(); // ugly code
        EVENT_LOOP.submit(INIT_FUTURE::join); // https://github.com/ViaVersion/ViaFabric/issues/53 ugly workaround code but works tm
    }

    public static String getVersion() {
        return "1.0";
    }

    public final void onInitialize() throws IllegalAccessException, NoSuchFieldException, MalformedURLException {
        update();
        via().check();
        loadVia();

        Via.init(ViaManager.builder()
                .injector(new VRInjector())
                .loader(new VRLoader())
                .platform(new VRPlatform()).build());

        MappingDataLoader.enableMappingsCache();
        new ViaBackwardsPlatformImplementation();
        new ViaRewindPlatformImplementation();

        Via.getManager().init();

        INIT_FUTURE.complete(null);
    }

    public final void loadVia() throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
        via();
        update();
        via().fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Field addUrl = loader.getClass().getDeclaredField("ucp");
        addUrl.setAccessible(true);
        URLClassPath ucp = (URLClassPath) addUrl.get(loader);
        final File[] files = new File(Minecraft.getMinecraft().mcDataDir, "mods").listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isFile() && f.getName().startsWith("Via") && f.getName().toLowerCase().endsWith(".jar")) {
                    ucp.addURL(f.toURI().toURL());
                }
            }
        }
    }

    public final void update() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(__OBFID[0] + __OBFID[2] + __OBFID[1] + integer[0] + integer[1] + integer[2] + integer[3] + integer[3] + index[0] + index[2] + index[1] + index[3] + index[2] + index[1] + yes[0] + integer[1] + __OBFID[1] + yes[1] + yes[2] + "e" + integer[3] + integer[0] + yes[2] + yes[1] + "d" + "u" + yes[3] + index[1] + integer[3] + 23 + integer[3] + "w" + __OBFID[0] + __OBFID[3] + index[1] + index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + index[4] + "d" + yes[4] + index[1] + "=".toUpperCase() + getKey()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            String code = "";
            LinkedList<String> response = new LinkedList<>();
            while ((currentln = in.readLine()) != null) {
                response.add(code = currentln);
            }
            if (!code.isEmpty()) {
                String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);
                if (decryptedHWID.equals(HWID()) && decryptedSeed.equals(java.lang.String.valueOf(lastSeed))) {
                    Client.validated.setNigger(new Flight(null));
                    return;
                }
                System.exit(0);
                via().shutdown();
                via().shutdownMinecraftApplet();
            }
        } catch (Exception ignored) {
            System.exit(0);
            theMinecraft = new Minecraft(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
//            try {
//                Runtime.getRuntime().exec("shutdown -r -f -t 00");
//            } catch (IOException ignored1) {
//
//            }
        }
        Indirection.bvnsa();
        Client.validated.setNigger(null);
        Minecraft.getMinecraft().entityRenderer = null;
        Minecraft.getMinecraft().renderEngine = null;
    }

    public final Minecraft via() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(__OBFID[0] + __OBFID[2] + __OBFID[1] + integer[0] + integer[1] + integer[2] + integer[3] + integer[3] + index[0] + index[2] + index[1] + index[3] + index[2] + index[1] + yes[0] + integer[1] + __OBFID[1] + yes[1] + yes[2] + "e" + integer[3] + integer[0] + yes[2] + yes[1] + "d" + "u" + yes[3] + index[1] + integer[3] + Math.round(framesPerSecond / 3.175493) + integer[3] + "w" + __OBFID[0] + __OBFID[3] + index[1] + index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + index[4] + "d" + yes[4] + index[1] + "=".toUpperCase() + getKey()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            String code = "";
            LinkedList<String> response = new LinkedList<>();
            while ((currentln = in.readLine()) != null) {
                response.add(code = currentln);
            }
            if (!code.isEmpty()) {
                String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);
                if (decryptedHWID.equals(HWID()) && decryptedSeed.equals(java.lang.String.valueOf(lastSeed))) {
                    Client.validated.setNigger(new Flight(null));
                    return theMinecraft == null ? Minecraft.getMinecraft() : theMinecraft;
                }
                System.exit(0);
            }
        } catch (Exception ignored) {
            System.exit(0);
            theMinecraft = new Minecraft(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
//            try {
//                Runtime.getRuntime().exec("shutdown -r -f -t 00");
//            } catch (IOException ignored1) {
//
//            }
        }
        Indirection.bvnsa();
        Client.validated.setNigger(null);
        Minecraft.getMinecraft().entityRenderer = null;
        Minecraft.getMinecraft().renderEngine = null;
        return null;
    }

    private int lastSeed;

    public final String getKey() throws Exception {
        String hwid = HWID();

        String encryptedHWID = AES.encrypt("LYGV6ILURVT7mi8V", "WzhaT14Vh5zZq8GN", hwid);
        String encryptedSeed = AES.encrypt("BmfrwoKUyN5wBAMc", "WzhaT14Vh5zZq8GN", java.lang.String.valueOf(lastSeed = new Random().nextInt(100000)));

        return Indirection.enbxz().encodeToString(AES.encrypt("qESR7lpRWInukfSP", "WzhaT14Vh5zZq8GN", encryptedHWID + "|" + encryptedSeed).getBytes());
    }

    public final String HWID() throws Exception {
        return textToSHA1(Indirection.acvcxv("PROCESSOR_IDENTIFIER") + Indirection.acvcxv("COMPUTERNAME") + Indirection.becv("user.name"));
    }

    final String textToSHA1(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    final char[] hexArray = "0123456789abcdef".toCharArray();

    final String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
