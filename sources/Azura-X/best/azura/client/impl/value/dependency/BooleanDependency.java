package best.azura.client.impl.value.dependency;

import best.azura.client.api.value.dependency.Dependency;
import best.azura.client.impl.value.BooleanValue;

public class BooleanDependency implements Dependency {

    private final BooleanValue value;
    private final boolean status;

    public BooleanDependency(BooleanValue value, boolean status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public boolean checkDependency() {
        return this.value.getObject() == status;
    }
}
