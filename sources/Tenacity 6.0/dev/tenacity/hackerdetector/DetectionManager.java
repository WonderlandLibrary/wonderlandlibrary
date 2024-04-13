package dev.tenacity.hackerdetector;

import dev.tenacity.hackerdetector.checks.FlightA;
import dev.tenacity.hackerdetector.checks.FlightB;
import dev.tenacity.hackerdetector.checks.ReachA;

import java.util.ArrayList;
import java.util.Arrays;

public class DetectionManager {

    private ArrayList<Detection> detections = new ArrayList<>();

    public DetectionManager() {
        addDetections(

                // Combat
                new ReachA(),

                // Movement
                new FlightA(),
                new FlightB()

                // Player

                // Misc

                // Exploit

        );
    }

    public void addDetections(Detection... detections) {
        this.detections.addAll(Arrays.asList(detections));
    }

    public ArrayList<Detection> getDetections() {
        return detections;
    }
}
