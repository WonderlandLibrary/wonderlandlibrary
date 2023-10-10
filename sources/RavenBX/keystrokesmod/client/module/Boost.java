package keystrokesmod.client.module;

public class Boost {

    // Add these Java arguments to your Minecraft launcher settings to allocate more RAM and use the G1 Garbage Collector.
    public static void main(String[] args) {
        // Adjust these values to allocate more RAM to Minecraft.
        int ramAmount = 4096; // 4GB
        String javaPath = System.getProperty("java.home") + "/bin/java";
        String jarPath = "path/to/your/minecraft.jar"; // Replace with the actual path to your minecraft.jar file

        String[] newArgs = new String[args.length + 3];
        newArgs[0] = javaPath;
        newArgs[1] = "-Xmx" + ramAmount + "M";
        newArgs[2] = "-Xms" + ramAmount + "M";
        newArgs[3] = "-XX:+UseG1GC";
        System.arraycopy(args, 0, newArgs, 4, args.length);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(newArgs);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
