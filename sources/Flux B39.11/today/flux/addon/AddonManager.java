package today.flux.addon;

import com.soterdev.SoterObfuscator;
import lombok.Getter;
import sun.misc.ClassLoaderUtil;
import today.flux.Flux;
import today.flux.addon.api.FluxAddon;
import today.flux.utility.ChatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

public class AddonManager {
    @Getter
    private final CopyOnWriteArrayList<FluxAddon> fluxAddonList = new CopyOnWriteArrayList<>();

    @Getter
    private final CopyOnWriteArrayList<FluxAddon> enabledFluxAddonList = new CopyOnWriteArrayList<>();

    @Getter
    private final static CopyOnWriteArrayList<String> enabledAddonsName = new CopyOnWriteArrayList<>();
    public static boolean loaded = false;
    private final static Map<String, URLClassLoader> classLoaderMap = new HashMap<>();

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void register(FluxAddon fluxAddon) {
        try {
            fluxAddonList.add(fluxAddon);
            fluxAddon.initAPI(FluxAPI.FLUX_API);
            for (String enabledAddon : enabledAddonsName) {
                if (fluxAddon.getAPIName().equals(enabledAddon))
                    enabledFluxAddonList.add(fluxAddon);
            }
            ChatUtils.sendOutputMessage(String.format("[Flux API] \247a[%s] \2477Loading...", fluxAddon.getAPIName() + " " + fluxAddon.getVersion()));
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.sendErrorToPlayer(String.format("[Flux API] \247a[%s] \2477Failed to load API: %s", fluxAddon.getAPIName() + " " + fluxAddon.getVersion(), e.getMessage()));
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void loadAllAPI() {
        loaded = false;
        File addons = new File("Flux/addons");
        if (!addons.exists())
            addons.mkdirs();
        for (File file : addons.listFiles()) {
            try {
                if (!file.isDirectory() && file.getName().endsWith(".jar")) {
                    JarFile jarFile = new JarFile(file);
                    InputStream inputStream = jarFile.getInputStream(jarFile.getJarEntry("FluxAddon.cfg"));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String mainClass = reader.readLine();

                    if (classLoaderMap.containsKey(mainClass)) {
                        ClassLoaderUtil.releaseLoader(classLoaderMap.get(mainClass));
                        classLoaderMap.remove(mainClass);
                    }
                    
                    URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
                    classLoaderMap.put(mainClass, loader);

                    Class clazz = loader.loadClass(mainClass);
                    register(((FluxAddon) clazz.newInstance()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loaded = true;
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public static void reload() {
        try {
            Flux.INSTANCE.api = new FluxAPI();
            /*
            Flux.INSTANCE.getConfig().loadBinds();
            Flux.INSTANCE.getConfig().loadMods();
            Flux.INSTANCE.getConfig().loadConfig(Flux.GlobalSettings.getValue() ? null : PacketUtils.getServerIP());

             */
            ChatUtils.sendOutputMessage(String.format("[Flux API] \2477Reload APIs Successfully! Loaded %d API(s).", Flux.INSTANCE.api.getAddonManager().getFluxAddonList().size()));
        } catch (Exception e) {
            ChatUtils.sendErrorToPlayer(String.format("[Flux API] \2477Reload APIs Failed: %s", e.getMessage()));
            e.printStackTrace();
        }
    }
}
