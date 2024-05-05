package club.dortware.client.property;

public class Property<T1, T2> {

    private String name;

    private T1 owner;
    private T2 value;

    public Property(String name, T1 owner, T2 value) {
        this.name = name;
        this.owner = owner;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T1 getOwner() {
        return owner;
    }

    public T2 getValue() {
        return value;
    }

    public void setValue(T2 value) {
        this.value = value;
    }

}
