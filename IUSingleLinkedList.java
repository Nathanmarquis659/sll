import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newHead = new Node<T>(element);
		if (size == 0) {
			head = tail = newHead;
		} else {
			newHead.setNext(head);
			head = newHead;
		}
		size ++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newTail = new Node<T>(element);
		if (size == 0) {
			head = tail = newTail;
		} else {
			tail.setNext(newTail);
			tail = newTail;
		}
		size ++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		// TODO 
		// size ++;
		// modCount++;
	}

	@Override
	public void add(int index, T element) {
		// TODO 
		// size ++;
		// modCount++;
	}

	@Override
	public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
		head = head.getNext();
		size --;
		modCount++;
		return head.getElement();
	}

	@Override
	public T removeLast() {
		// TODO 
		// size --;
		// modCount++;
		return null;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		// TODO 
		// size --;
		// modCount++;
		return null;
	}

	@Override
	public void set(int index, T element) {
		// TODO 
		// modCount++;
	}

	@Override
	public T get(int index) {
		// TODO 
		// modCount++;
		return null;
	}

	@Override
	public int indexOf(T element) {
		Node<T> currentNode = head;
        int currentIndex = 0;
        while (currentNode != null && !currentNode.getElement().equals(element)) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        if (currentNode == null) {
            currentIndex = -1;
        }
		return currentIndex;
	}

	@Override
	public T first() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

    @Override
    public String toString() {
        // if (array == null)
        //     return "null";

        // int iMax = rear - 1;
        // if (iMax == -1)
        //     return "[]";

        // StringBuilder b = new StringBuilder();
        // b.append('[');

        // for (int i=0; i < rear; i++) {
        //     b.append(String.valueOf(array[i]));
        //     b.append(", ");
        // }
		// b.delete(b.length()-2, b.length());
		// b.append(']');
        // return b.toString();
        return "";
    }

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			return false;
		}

		@Override
		public T next() {
			// TODO 
			return null;
		}
		
		@Override
		public void remove() {
			// TODO
			// modCount++;
			// iterModCount++;
		}
	}
}