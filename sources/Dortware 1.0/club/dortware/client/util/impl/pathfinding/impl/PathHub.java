package club.dortware.client.util.impl.pathfinding.impl;

import java.util.ArrayList;

public class PathHub {
    private Vector loc;
    private PathHub parentPathHub;
    private ArrayList<Vector> pathway;
    private double sqDist;
    private double currentCost;
    private double maxCost;

    public PathHub(Vector loc, PathHub parentPathHub, ArrayList<Vector> pathway,
                   double sqDist, double currentCost, double maxCost) {
        this.loc = loc;
        this.parentPathHub = parentPathHub;
        this.pathway = pathway;
        this.sqDist = sqDist;
        this.currentCost = currentCost;
        this.maxCost = maxCost;
    }

    public Vector getLoc() {
        return loc;
    }

    public PathHub getLastHub() {
        return parentPathHub;
    }

    public ArrayList<Vector> getPathway() {
        return pathway;
    }

    public double getSqDist() {
        return sqDist;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setLoc(Vector loc) {
        this.loc = loc;
    }

    public void setParentPathHub(PathHub parentPathHub) {
        this.parentPathHub = parentPathHub;
    }

    public void setPathway(ArrayList<Vector> pathway) {
        this.pathway = pathway;
    }

    public void setSqDist(double sqDist) {
        this.sqDist = sqDist;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public double getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(double maxCost) {
        this.maxCost = maxCost;
    }
}
