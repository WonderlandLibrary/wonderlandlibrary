package intent.AquaDev.aqua.utils;

import java.util.ArrayList;
import net.minecraft.util.Vec3;

class PathFinder.Node {
    Vec3 loc = null;
    PathFinder.Node parent = null;
    ArrayList<Vec3> path;
    double squareDistanceToFromTarget;
    double hCost;
    double fCost;

    public PathFinder.Node(Vec3 loc, PathFinder.Node parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
        this.loc = loc;
        this.parent = parent;
        this.path = path;
        this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        this.hCost = cost;
        this.fCost = totalCost;
    }

    public Vec3 getLoc() {
        return this.loc;
    }

    public PathFinder.Node getParent() {
        return this.parent;
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public double getSquareDistanceToFromTarget() {
        return this.squareDistanceToFromTarget;
    }

    public double getHCost() {
        return this.hCost;
    }

    public void setLoc(Vec3 loc) {
        this.loc = loc;
    }

    public void setParent(PathFinder.Node parent) {
        this.parent = parent;
    }

    public void setPath(ArrayList<Vec3> path) {
        this.path = path;
    }

    public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
        this.squareDistanceToFromTarget = squareDistanceToFromTarget;
    }

    public void setHCost(double hCost) {
        this.hCost = hCost;
    }

    public double getFCost() {
        return this.fCost;
    }

    public void setFCost(double fCost) {
        this.fCost = fCost;
    }
}
