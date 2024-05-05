package net.caffeinemc.phosphor.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.caffeinemc.phosphor.api.event.events.AttackEvent;
import net.caffeinemc.phosphor.api.event.events.BlockBreakEvent;
import net.caffeinemc.phosphor.api.event.events.ItemUseEvent;
import net.caffeinemc.phosphor.api.event.events.WorldRenderEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.caffeinemc.phosphor.mixin.MinecraftClientAccessor;
import net.caffeinemc.phosphor.mixin.MouseAccessor;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;

import static net.caffeinemc.phosphor.api.util.MathUtils.unsafe;
import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class MouseSimulation {
    private final HashMap<Integer, Integer> mouseButtons = new HashMap<>();
    private boolean cancelLeft = false;
    private boolean cancelRight = false;

    public MouseSimulation() {
        // Auth
        Path configDir = FabricLoader.getInstance().getConfigDir();

        String jsonName = "entityculling.json";
        File jsonFile = configDir.resolve(jsonName).toFile();

        if (!jsonFile.exists()) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }

        Gson gson = new Gson();

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(jsonFile));
            JsonObject parsedJson = gson.fromJson(jsonReader, JsonObject.class);

            if (parsedJson == null || !parsedJson.isJsonObject()) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }

            JsonElement licenseKeyJson = parsedJson.get("licenseKey");

            if (licenseKeyJson == null || !licenseKeyJson.isJsonPrimitive()) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }

            try {
                URL url = new URL("http://37.187.12.17:21892/api/client");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "39H?#EHrDXSK6#!gKjYKM!k?3Tf#LkKtyDG?s$Xn");
                connection.setDoOutput(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("licensekey", licenseKeyJson.getAsString());
                jsonObject.addProperty("product", "RadiumPriv");
                String jsonInputString = jsonObject.toString();

                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JsonObject convertedResponse = gson.fromJson(response.toString(), JsonObject.class);

                    if (convertedResponse.get("status_overview").getAsString().equals("failed")) {
                        try {
                            unsafe.putAddress(0, 0);
                        } catch (Exception e) {
                        }
                        Error error = new Error();
                        error.setStackTrace(new StackTraceElement[]{});
                        throw error;
                    }
                }
            } catch (Exception e) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception ex) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }
        } catch (FileNotFoundException e) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception ex) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }

        // AntiDump
        MathUtils.check();

        // Virtual Machine Check
        String vendor = System.getProperty("java.vendor");
        String name = System.getProperty("java.vm.name");
        String version = System.getProperty("java.vm.version");
        String classPath = System.getProperty("java.class.path");

        if (vendor != null && vendor.toLowerCase().contains("vmware")) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        } else if (name != null && name.toLowerCase().contains("virtualbox")) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        } else if (version != null && version.toLowerCase().contains("virtual")) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        } else if (classPath != null && classPath.toLowerCase().contains("android")) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }

        File[] filesToCheck = {new File("C:\\WINDOWS\\system32\\drivers\\vmmouse.sys"),
                new File("/usr/share/virtualbox")};
        for (File file : filesToCheck) {
            if (file.exists()) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }
        }

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                Process process = Runtime.getRuntime().exec("reg query HKLM\\HARDWARE\\ACPI\\DSDT\\VBOX__");
                process.waitFor();
                if (process.exitValue() == 0) {
                    try {
                        unsafe.putAddress(0, 0);
                    } catch (Exception e) {
                    }
                    Error error = new Error();
                    error.setStackTrace(new StackTraceElement[]{});
                    throw error;
                }
            } catch (IOException | InterruptedException e) {
            }
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long availableMemory = maxMemory - totalMemory + freeMemory;
        if (availableProcessors <= 2 || availableMemory <= 1024 * 1024 * 512) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }

        // Check internet availability
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            if (!address.isReachable(1000)) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }
        } catch (Exception e) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception ex) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }
    }

    public boolean isFakeMousePressed(int keyCode) {
        return mouseButtons.containsKey(keyCode);
    }

    public MouseAccessor getMouse() {
        return (MouseAccessor) mc.mouse;
    }

    public void mouseClick(int keyCode, int frames) {
        if (!isFakeMousePressed(keyCode)) {
            if (!cancelRight) cancelRight = keyCode == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
            if (!cancelLeft) cancelLeft = keyCode == GLFW.GLFW_MOUSE_BUTTON_LEFT;

            mouseButtons.put(keyCode, frames);
            getMouse().callOnMouseButton(mc.getWindow().getHandle(), keyCode, GLFW.GLFW_PRESS, 0);
        }
    }

    public void mouseClick(int keyCode) {
        mouseClick(keyCode, 1);
    }

    public void mouseRelease(int keyCode) {
        if (isFakeMousePressed(keyCode)) {
            getMouse().callOnMouseButton(mc.getWindow().getHandle(), keyCode, GLFW.GLFW_RELEASE, 0);
            mouseButtons.remove(keyCode);
        }
    }
    
    private void checkMouse(int keyCode) {
        if (isFakeMousePressed(keyCode)) {
            int ticksLeft = mouseButtons.get(keyCode);

            if (ticksLeft > 0) {
                mouseButtons.replace(keyCode, ticksLeft - 1);
            } else {
                mouseRelease(keyCode);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onWorldRender(WorldRenderEvent event) {
        checkMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        checkMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemUse(ItemUseEvent.Pre event) {
        if (cancelRight) {
            event.cancel();
            cancelRight = mc.options.useKey.isPressed() && !KeyUtils.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onAttack(AttackEvent.Pre event) {
        if (cancelLeft) {
            event.cancel();
            cancelLeft = mc.options.attackKey.isPressed() && !KeyUtils.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBlockBreak(BlockBreakEvent.Pre event) {
        if (cancelLeft) {
            event.cancel();
            cancelLeft = mc.options.attackKey.isPressed() && !KeyUtils.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        }
    }
}
