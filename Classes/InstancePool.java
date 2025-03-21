package Classes;

import java.lang.reflect.Array;
import java.util.ArrayList;

import JGamePackage.JGame.Classes.Instance;

public class InstancePool<T extends Instance> {
    private T template;

    private boolean refillAutomatically;
    private int refillSize;

    private ArrayList<T> availablePool = new ArrayList<>();
    private ArrayList<T> inUsePool = new ArrayList<>();

    public InstancePool(T template, int initialFillSize) {
        this(template, initialFillSize, true, 10);
    }

    @SuppressWarnings("unchecked")
    public InstancePool(T template, int initialFillSize, boolean refillAutomatically, int refillSize) {
        this.template =  (T) template.Clone();
        this.template.SetParent(null);

        this.refillAutomatically = refillAutomatically;
        this.refillSize = refillSize;
        this.Fill(initialFillSize);
    }

    @SuppressWarnings("unchecked")
    public void Fill(int size) {
        for (int i = 0; i < size; i++) {
            availablePool.add((T) template.Clone());
        }
    }

    public T GetInstance() {
        T first = availablePool.size() > 0 ? availablePool.get(0) : null;
        if (first == null) {
            if (refillAutomatically) {
                this.Fill(refillSize);
                first = availablePool.getFirst();
            } else {
                T inuseFirst = inUsePool.getFirst();
                this.ReturnInstance(inuseFirst);
                first = inuseFirst;
            }
        }

        availablePool.remove(first);
        inUsePool.add(first);

        return first;
    }

    public T[] GetInUseInstances() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(Object.class, inUsePool.size());

        for (int i = 0; i < array.length; i++)
            array[i] = inUsePool.get(i);

        return array;
    }

    public void ReturnInstance(T inst) {
        if (!inUsePool.contains(inst)) {
            throw new Error("An instance must be in use in order to return it to the pool");
        }

        inUsePool.remove(inst);
        availablePool.add(inst);
        inst.SetParent(null);
    }
}
