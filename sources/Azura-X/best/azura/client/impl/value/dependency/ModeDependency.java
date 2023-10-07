package best.azura.client.impl.value.dependency;

import best.azura.client.api.value.dependency.Dependency;
import best.azura.client.impl.value.ModeValue;

public class ModeDependency implements Dependency {

    private final ModeValue value;
    private final String select;

    public ModeDependency(ModeValue value, String select) {
        this.value = value;
        this.select = select;
    }

    @Override
    public boolean checkDependency() {
        return value.getObject().equals(select);
    }

}
