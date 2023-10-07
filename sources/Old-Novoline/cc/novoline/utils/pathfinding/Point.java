package cc.novoline.utils.pathfinding;

import java.util.Objects;

public final class Point {

    private int posX;
    private int posY;
    private int posZ;

    public Point(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return this.posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        final Point point = (Point) o;
        return posX == point.posX && posY == point.posY && posZ == point.posZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY, posZ);
    }

    @Override
    public String toString() {
        return "Point{" + "posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + '}';
    }

}
