import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Nathan Marquis
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
		newHead.setNext(head);
		head = newHead;
		if (tail == null) {
			tail = head;
		}
		size ++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newTail = new Node<T>(element);
		if (!isEmpty()) {
			tail.setNext(newTail);
		} else {
			head = newTail;
		}
		tail = newTail;
		size ++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		// if (!contains(target)) {
		// 	throw new NoSuchElementException();
		// }
		Node<T> newNode = new Node<T>(element);
		Node<T> currentNode = head;
		boolean isFound = false;
		while (currentNode != null && !isFound) {
			if (currentNode.getElement().equals(target)) {
				isFound = true;
				currentNode.setNext(newNode);
				if (currentNode == tail) {
					newNode = tail;
				}
				size ++;
				modCount++;
			} else {
				currentNode = currentNode.getNext();
			}
		}
		
		if (!isFound) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addToFront(element);
		} else if (index == size) {
			addToRear(element);
		} else {
			Node<T> currentNode = head;
			Node<T> addNode = new Node<>(element);
			for (int i=0; i < index-1; i++) {
				currentNode = currentNode.getNext();
			}
			addNode.setNext(currentNode.getNext());
			currentNode.setNext(addNode);
			size ++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {
		return remove(0);
	}

	@Override
	public T removeLast() {
		return remove(size-1);
	}

	@Override
	public T remove(T element) {
		
		return null;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || size <= index) {
			throw new IndexOutOfBoundsException();
		}
		int nodeIndex = 0;
		Node<T> currentNode = head;
		Node<T> deletedNode = currentNode;
		if (index == 0) {
			head = head.getNext();
		} else {
			//COULD BE MADE MORE CLEAR. Get through a loop proper # times and then do required action
			while(nodeIndex < index) {
				if(nodeIndex == index - 1) {
					deletedNode = currentNode.getNext();
					currentNode.setNext(deletedNode.getNext());
				}
				currentNode = currentNode.getNext();
				nodeIndex++;
			}

		}

		size --;
		modCount++;
		return deletedNode.getElement();
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currentNode = head;

		for (int i = 0; i < size-1; i++) {
			currentNode = currentNode.getNext();
		}
		
		currentNode.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> currentNode = head;

		for (int i = 0; i < size-1; i++) {
			currentNode = currentNode.getNext();
		}
		
		return currentNode.getElement();
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
        if (isEmpty()) return "[]";

		Node<T> currentNode = head;
        StringBuilder b = new StringBuilder();
        b.append('[');

        while (currentNode != null) {
            b.append(String.valueOf(currentNode.getElement()));
            b.append(", ");
			currentNode = currentNode.getNext();
        }
		b.delete(b.length()-2, b.length());
		b.append(']');
        return b.toString();
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
		private Node<T> nodeCurrent;
		private Node<T> nodeSub1;
		private Node<T> nodeSub2;
		private int iterModCount;
		private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nodeCurrent = head;
			nodeSub1 = null;
			nodeSub2 = null;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            } 
			return nodeCurrent != null;
		}

		@Override
		public T next() {
			if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
			if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
			nodeSub2 = nodeSub1;
			nodeSub1 = nodeCurrent;
			nodeCurrent = nodeCurrent.getNext();
			return nodeSub1.getElement();
		}
		
		@Override
		public void remove() {
			if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
			if (canRemove == false) {
                throw new IllegalStateException();
            }

			nodeSub1 = nodeSub2;
			nodeSub2 = null;

            canRemove = false;
            modCount++;
            iterModCount++;
		}
	}
}