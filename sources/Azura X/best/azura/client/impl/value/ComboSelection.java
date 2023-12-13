package best.azura.client.impl.value;

public class ComboSelection {

    private String name;
    private boolean object;
    public double animation;
    private long start;

    public ComboSelection(String name, boolean object) {
        this.name = name;
        this.object = object;
    }

    public void updateAnimation() {
        if(!object) {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/200f);
            animation = -1 * Math.pow(anim-1, 6) + 1;
            animation = 1 - animation;
        } else {
            float anim = Math.min(1, (System.currentTimeMillis()-start)/200f);
            animation = -1 * Math.pow(anim-1, 6) + 1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObject(boolean object) {
        this.object = object;
        start = System.currentTimeMillis();
    }

    public boolean getObject() {
        return object;
    }
}