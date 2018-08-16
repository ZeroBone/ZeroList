package net.zerobone.zerolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ZeroHashList<T extends ZeroHashList.ZeroHashListable> {

    private final HashMap<Integer, T> elements;

    private final ArrayList<Integer> freeIds;

    public ZeroHashList() {

        elements = new HashMap<>();

        freeIds = new ArrayList<>();

    }

    public int length() {

        return elements.size();

    }

    public Collection<T> values() {

        return elements.values();

    }

    public boolean has(T element) {

        return has(element.getId(this));

    }

    public boolean has(int id) {

        return elements.containsKey(id);

    }

    public T get(int id) {

        return elements.get(id);

    }

    public int add(T element) {

        final int id = allocateId();

        element.setId(id, this);

        elements.put(id, element);

        return id;

    }

    private int allocateId() {

        if (freeIds.size() == 0) {

            return elements.size();

        }

        return freeIds.remove(freeIds.size() - 1);

    }

    public void remove(T element) throws ElementNotFoundException {

        remove(element.getId(this));

    }

    public void remove(int id) throws ElementNotFoundException {

        elements.remove(id);

        freeIds.add(id);

    }

    public interface ZeroHashListable {

        /**
         * Gets the id for the current element.
         * @param zeroList the instance that makes storing of single elements in different {@link ZeroList} instances possible.
         * @return the id for the current element.
         */
        int getId(ZeroHashList zeroList);

        /**
         * Sets the id for the current element.
         * @param id the id to be set.
         * @param zeroList the instance that makes storing of single elements in different {@link ZeroList} instances possible.
         */
        void setId(int id, ZeroHashList zeroList);

    }

    /**
     * Base class for all ZeroList exceptions.
     */
    public static abstract class ZeroHashListException extends Throwable {}

    /**
     * This error is thrown when the id provided is invalid or the id provided by the implemented interface is invalid.
     */
    public static class ElementNotFoundException extends ZeroHashListException {}

}
