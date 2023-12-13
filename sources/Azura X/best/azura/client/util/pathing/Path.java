package best.azura.client.util.pathing;

import best.azura.client.util.math.CustomVec3;

import java.util.ArrayList;

public class Path {

    private CustomVec3 start, end;
    private ArrayList<CustomVec3> additionalVectors;

    public Path(CustomVec3 start, CustomVec3 end) {
        this.additionalVectors = new ArrayList<>();
        this.start = start;
        this.end = end;
        this.additionalVectors.add(this.start);
        this.additionalVectors.add(this.end);
    }

    public CustomVec3 getStart() {
        return start;
    }

    public ArrayList<CustomVec3> getAdditionalVectors() {
        return additionalVectors;
    }

    public void addAdditionalVector(CustomVec3 vec3) {
        ArrayList<CustomVec3> array = new ArrayList<>();
        array.add(this.start);
        for(CustomVec3 c : this.additionalVectors) if(!c.equals(start) && !c.equals(end)) array.add(c);
        array.add(vec3);
        array.add(end);
        this.additionalVectors = array;
    }

    public void setStart(CustomVec3 start) {
        this.start = start;
    }

    public CustomVec3 getEnd() {
        return end;
    }

    public void setEnd(CustomVec3 end) {
        this.end = end;
    }

    public Path copy() {
        Path path = new Path(start, end);
        path.getAdditionalVectors().clear();
        path.getAdditionalVectors().addAll(getAdditionalVectors());
        return path;
    }

}