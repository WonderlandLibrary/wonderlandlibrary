package best.azura.client.util.modes;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ModeType<T extends Module> {
    public T module;
    public final List<ModeImpl<T>> modes = new ArrayList<>();
    public ModeImpl<T> currentImpl;
}