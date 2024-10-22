import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported. 
 * 
 * @author 
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (T[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0;
	}
	
	/** Check if needed, if so double the capacity of array */
	private void expandCapacity() {
        if (rear >= array.length) {
            array = Arrays.copyOf(array, array.length*2);
        }
	}

    /** 
     * Checks if the provided index is within the range of the current array
     * @param index The index to be checked if it is within the range
     * @return True if the provided index is in fact out of range, false otherwise
     */
    private boolean isIndexOutOfRange(int index) {
        return (index < 0 || index >= rear);
    }

	@Override
	public void addToFront(T element) {
        // Same functionality as add(int index, T element) but with the index being 0
        add(0, element);
	}

	@Override
	public void addToRear(T element) {
        //same functionality as add(T element)
		add(element);
	}

	@Override
	public void add(T element) {
        expandCapacity();
		array[rear] = element;
        rear++; 
        modCount++;
	}

	@Override
	public void addAfter(T element, T target) {
        // Use indexOf() to get first instance of the target
        // throw NoSuchElementException otherwise
        int elemLocation = indexOf(target);
        if (elemLocation == -1) throw new NoSuchElementException();

        // One AFTER the target will be added
        add(elemLocation + 1, element);
	}

	@Override
	public void add(int index, T element) {
        // Check if the index is out of bounds
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        expandCapacity();
        for (int i = rear-1; i >= index; i--) {
            array[i+1] = array[i];
        }
        array[index] = element;
        rear ++;
        modCount++;
	}

	@Override
	public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

		T firstElement = array[0];
        remove(0);
		return firstElement;
	}

	@Override
	public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

		T lastElement = array[rear-1];
        rear--;
        modCount++;
		return lastElement;
	}

	@Override
	public T remove(T element) {
		int index = indexOf(element);
	    if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
        return remove(index);
    }

	@Override
	public T remove(int index) {
        if (isIndexOutOfRange(index)) {
            throw new IndexOutOfBoundsException();
        }

		T retVal = array[index];
		
		//shift elements
		for (int i = index; i < rear-1; i++) {
			array[i] = array[i+1];
		}
		array[rear-1] = null;
		rear--;
		modCount++;
		
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (isIndexOutOfRange(index)) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        modCount++;
	}

	@Override
	public T get(int index) {
		// check if index is valid, if not throw exception
        if (isIndexOutOfRange(index) || isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        // return item at index
		return array[index];
	}

	@Override
	public int indexOf(T element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

	@Override
	public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
		return array[0];
	}

	@Override
	public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
		return array[rear-1];
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
        // Default is true
		boolean isEmpty = true;
        // When rear > 0 it means that list is not empty
        if (rear > 0) isEmpty = false;
		return isEmpty;
	}

	@Override
	public int size() {
        // rear will be the size of the list
		return rear;
	}

    @Override
    public String toString() {
		if (array == null)
            return "null";

        int iMax = rear - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');

        for (int i=0; i < rear; i++) {
            b.append(String.valueOf(array[i]));
            b.append(", ");
        }
		b.delete(b.length()-2, b.length());
		b.append(']');
        return b.toString();
    }

	@Override
	public Iterator<T> iterator() {
		return new ALIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUArrayList */
	private class ALIterator implements Iterator<T> {
		private int nextIndex;
		private int iterModCount;
        private boolean canRemove;
		
		public ALIterator() {
			nextIndex = 0;
			iterModCount = modCount;
            canRemove = false;
		}

		@Override
		public boolean hasNext() {
            boolean hasNext = false;
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            } 
			if (rear > nextIndex) {
				hasNext = (nextIndex < rear);
			}
			return hasNext;
		}

		@Override
		public T next() {
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
			if (!hasNext()) {
                throw new NoSuchElementException();
            }
            nextIndex++;
            canRemove = true;
			return array[nextIndex-1];
		}
		
		@Override
		public void remove() {
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
			if (canRemove == false) {
                throw new IllegalStateException();
            }

            rear--;
            //shift elements
            for (int i = nextIndex-1; i < rear; i++) {
                array[i] = array[i+1];
            }

            array[rear] = null;
            nextIndex--;
            canRemove = false;
            modCount++;
            iterModCount++;
		}
	}
}
