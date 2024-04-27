package kevin.depends;

import kevin.main.KevinClient;
import kevin.utils.MSTimer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

// this is a tool help us load libraries only when need
public class LibraryManager {
    private static final HashMap<URLClassLoader, URLClassLoaderAccess> URL_INJECTORS = new HashMap<>();
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadDependency(MavenDependency dependency, URLClassLoader classLoader) {
        String name = dependency.getArtifactId() + "-" + dependency.getVersion();

        URLClassLoaderAccess URL_INJECTOR = URL_INJECTORS.get(classLoader);
        if (URL_INJECTOR == null) {
            URL_INJECTORS.put(classLoader, URL_INJECTOR = URLClassLoaderAccess.create(classLoader));
        }

        Logger logger = Minecraft.logger;
        File saveLocation = new File(getLibFolder(), name + ".jar");
        if (!saveLocation.exists()) {
            File tempLocation = new File(getLibFolder(), name + ".temp");
            if (tempLocation.exists()) tempLocation.delete();
            try {
                mavenMirror(dependency);
                logger.info("Dependency '" + name + "' is not already in the libraries folder. Attempting to download...");
                String title = Display.getTitle();
                URL url = dependency.getUrl();
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                String base = String.format("Downloading dependency: %s:%s from maven ", dependency.getArtifactId(), dependency.getVersion());
                Display.setTitle(base + "(" + dependency.getRepoUrl() + " | no connection) ");
                final AtomicLong count = new AtomicLong(0);
                AtomicBoolean done = new AtomicBoolean(false);

                new Thread(() -> {
                    try (InputStream is = urlConnection.getInputStream();
                         FileOutputStream _fs = new FileOutputStream(tempLocation);
                         BufferedOutputStream stream = new BufferedOutputStream(_fs)) {
                        logger.info("connected to " + url);
//                    double total = urlConnection.getContentLengthLong() / 1024.0 / 1024.0;
                        int i;
                            byte[] buf = new byte[2048];
                        while ((i = is.read(buf)) != -1) {
                            stream.write(buf, 0, i);
                            count.addAndGet(i);
//                            stream.write(i);
//                            count.incrementAndGet();
                        }
                        stream.flush();
                        done.set(true);
                    } catch (Throwable e) {
                        logger.warn(e.getMessage());
                    }
                }).start();

                double last = 0;
                double dlt = 0;
                MSTimer timer = new MSTimer();
                while (!done.get()){
                    try {
                        Thread.sleep(50L);
                    } catch (Exception ignored) {}
                    String e = "-";
                    switch ((int) ((System.currentTimeMillis() / 250) % 4)) {
                        case 0: e = "\\"; break;
                        case 1: e = "|"; break;
                        case 2: e = "/"; break;
                        case 3: e = "-"; break;
                    }
                    double downloaded = count.get() / 1024.0 / 1024.0;
                    if (timer.hasTimePassed(1000)) {
                        dlt = downloaded - last;
                        last = downloaded;
                        timer.reset();
                    }

                    Display.setTitle(String.format("%s %s (%.3f MB) (%.3f MB/s)", base, e, downloaded, dlt));
                }
                Display.setTitle(title);

                if (!tempLocation.renameTo(saveLocation)) {
                    Files.copy(tempLocation.toPath(), saveLocation.toPath());
                    tempLocation.delete();
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }

            logger.info("Dependency '" + name + "' successfully downloaded.");
        }

        if (!saveLocation.exists()) {
            throw new RuntimeException("Unable to download dependency: " + dependency);
        }

        try {
            URL_INJECTOR.addURL(saveLocation.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException("Unable to load dependency: " + saveLocation, e);
        }

        logger.info("Loaded dependency '" + name + "' successfully.");
    }

    private static void mavenMirror(MavenDependency dependency) {
        if ("CN".equalsIgnoreCase(System.getProperty("user.country")))
//            dependency.setRepoUrl("https://maven.aliyun.com/repository/central");
            dependency.setRepoUrl("https://repo.huaweicloud.com/repository/maven");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getLibFolder() {
        File libraries = KevinClient.fileManager.libraries;
        if (!libraries.exists()) {
            libraries.mkdir();
        }
        return libraries;
    }
}
