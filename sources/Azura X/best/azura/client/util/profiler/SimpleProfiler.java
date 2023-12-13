package best.azura.client.util.profiler;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class SimpleProfiler {

    private static ArrayList<Long> delays = new ArrayList<>();
    private static long lastStart = 0L;

    public static void start() {
        lastStart = System.nanoTime();
    }

    public static void stop() {
        long delay = System.nanoTime() - lastStart;

        delays.add(delay);
        System.out.println(" ");
        System.out.println("Current: " + (delay / 1_000_000.0D));
        if(delays.size() >= 100) {
            delays.remove(0);

            double avg = delays.stream().mapToDouble(lo -> lo / 1_000_000.0).average().getAsDouble();

            System.out.println("Avg: " + avg);
        }
    }

}
