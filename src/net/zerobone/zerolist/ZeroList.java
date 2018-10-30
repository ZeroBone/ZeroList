package net.zerobone.zerolist;

import java.util.ArrayList;

public class ZeroList<T extends ZeroList.ZeroListable> {

    /**
     * Internal element container.
     */
    private final ArrayList<T> elements;

    /**
     * Array of indexes for elements, that were removed, needed to avoid adding elements
     * to the end of the array when there are gaps in between. Not modified when removing
     * trailing null's in order to prevent searhing for indexes in a loop.
     */
    private final ArrayList<Integer> spaceIndexes;

    private int length;

    /**
     * {@link ZeroList} Constructor
     */
    public ZeroList() {

        elements = new ArrayList<>();

        spaceIndexes = new ArrayList<>();

        length = 0;

    }

    /**
     * Length getter.
     * @return the amount of elements stored in the {@link ZeroList} object.
     */
    public int length() {

        return length;

    }

    public ArrayList<T> values(ArrayList<Integer> ids) {

        ArrayList<T> values = new ArrayList<>();

        for (int id : ids) {

            values.add(get(id));

        }

        return values;

    }

    /**
     * @param element The element inside the {@link ZeroList}.
     * @return true if the element is in the {@link ZeroList}.
     */
    public boolean has(T element) {

        return has(element.getId(this));

    }

    /**
     * @param id The element id.
     * @return true if the element with the specified id is in the {@link ZeroList}.
     */
    public boolean has(int id) {

        try {

            return elements.get(id) != null;

        }
        catch (IndexOutOfBoundsException e) {

            return false;

        }

    }

    /**
     * Gets the element from the ZeroList instance.
     * @param id the id of the element.
     * @return the element
     */
    public T get(int id) {

        try {

            return elements.get(id);

        }
        catch (IndexOutOfBoundsException e) {

            return null;

        }

    }

    /**
     * Adds an element to the {@link ZeroList} instance.
     * @param element element to be added.
     * @return the allocated id for this element.
     */
    public int add(T element) {

        length++;

        if (spaceIndexes.size() == 0) {

            return addElementToTheEnd(element);

        }
        else {

            final int availableId = spaceIndexes.remove(spaceIndexes.size() - 1); // get the last element instead of the first one to improve the performance.

            try {

                elements.set(availableId, element);

            }
            catch (IndexOutOfBoundsException e) {

                return addElementToTheEnd(element);

                // there was probably trailing null removing, so not all indexes exist.

//                final int requiredSize = availableId + 1;
//
//                while (elements.size() != requiredSize) {
//
//                    elements.add(null);
//
//                }
//
//                elements.set(availableId, element);

            }

            element.setId(availableId, this);

            return availableId;

        }

    }

    /**
     * Adds an element to the end of the internal ArrayList;
     * @param element element to be added.
     * @return the index.
     */
    private int addElementToTheEnd(T element) {

        elements.add(element);

        final int id = elements.size() - 1;

        element.setId(id, this);

        return id;

    }

    /**
     * Removes the item from the {@link ZeroList} instance.
     * @param element element to be removed. If there are no references left to this element, it will be removed from RAM.
     * @throws ElementNotFoundException if the element could not be found or stores an invalid/modified id inside of it.
     */
    public void remove(T element) throws ElementNotFoundException {

        remove(element.getId(this));

    }

    /**
     * Removes the item from the {@link ZeroList} instance by it's id.
     * @param id id of the element to be removed. If there are no references left to this element, it will be removed from RAM.
     * @throws ElementNotFoundException if the element could not be found or stores an invalid/modified id inside of it.
     */
    public void remove(int id) throws ElementNotFoundException {

        try {

            elements.set(id, null);

        }
        catch (IndexOutOfBoundsException e) {

            throw new ElementNotFoundException();

        }

        length--;

        if (id == elements.size() - 1) {

            // just removed the last element
            // remove the trailing null's to free some space and to avoid small memory leak

            while (elements.size() != 0 && elements.get(elements.size() - 1) == null) {

                elements.remove(elements.size() - 1);

            }

        }

        spaceIndexes.add(id);

    }

    /**
     * Interface that all element must implement. Needed for convinient id usage.
     */
    public interface ZeroListable {

        /**
         * Gets the id for the current element.
         * @param zeroList the instance that makes storing of single elements in different {@link ZeroList} instances possible.
         * @return the id for the current element.
         */
        int getId(ZeroList zeroList);

        /**
         * Sets the id for the current element.
         * @param id the id to be set.
         * @param zeroList the instance that makes storing of single elements in different {@link ZeroList} instances possible.
         */
        void setId(int id, ZeroList zeroList);

    }

    /**
     * Base class for all ZeroList exceptions.
     */
    public static abstract class ZeroListException extends Throwable {}

    /**
     * This error is thrown when the id provided is invalid or the id provided by the implemented interface is invalid.
     */
    public static class ElementNotFoundException extends ZeroListException {}

}
