class Node {
    int key;
    int value;
    int frequency;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
        this.frequency = 1;  // default frequency is 1
    }
}


class DoublyLinkedList {
    Node head;
    Node tail;

    public DoublyLinkedList() {
        head = new Node(-1, -1); // dummy node
        tail = new Node(-1, -1); // dummy node
        head.next = tail;
        tail.prev = head;
    }

    // Add node at the front
    public void addNode(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    // Remove a node
    public void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Remove the least recently used node (tail - prev)
    public Node removeTail() {
        if (head.next == tail) {
            return null; // List is empty
        }
        Node tailNode = tail.prev;
        removeNode(tailNode);
        return tailNode;
    }
}


class LFUCache {
    private final int capacity;
    private int size;
    private int minFreq;
    
    // Store key -> Node (with key, value, frequency)
    private Map<Integer, Node> cache;

    // Store frequency -> DoublyLinkedList (which holds all the nodes with that frequency)
    private Map<Integer, DoublyLinkedList> freqMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFreq = 0;
        cache = new HashMap<>();
        freqMap = new HashMap<>();
    }

    // Get the value from cache and update its frequency
    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }

        // Get the node
        Node node = cache.get(key);
        updateFrequency(node);
        return node.value;
    }

    // Put a new key-value pair or update existing key
    public void put(int key, int value) {
        if (capacity == 0) {
            return;  // If capacity is 0, no items can be added
        }

        // If the key exists, update its value and frequency
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            updateFrequency(node);
        } else {
            // If capacity is reached, evict the least frequent item
            if (size == capacity) {
                evict();
            }

            // Create a new node and add it to the cache and frequency map
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            freqMap.computeIfAbsent(1, k -> new DoublyLinkedList()).addNode(newNode);
            minFreq = 1;
            size++;
        }
    }

    // Update the frequency of a node
    private void updateFrequency(Node node) {
        int freq = node.frequency;
        freqMap.get(freq).removeNode(node);
        
        // If no nodes left with that frequency, we may need to increase minFreq
        if (freqMap.get(freq).head.next == freqMap.get(freq).tail) {
            if (freq == minFreq) {
                minFreq++;
            }
            freqMap.remove(freq);
        }

        node.frequency++;
        freqMap.computeIfAbsent(node.frequency, k -> new DoublyLinkedList()).addNode(node);
    }

    // Evict the least frequent used node (the node with the lowest frequency)
    private void evict() {
        DoublyLinkedList list = freqMap.get(minFreq);
        if (list == null) {
            return;  // No nodes to evict
        }
        Node evictedNode = list.removeTail();
        cache.remove(evictedNode.key);
        size--;
    }
}



public class Main {
    public static void main(String[] args) {
        LFUCache cache = new LFUCache(3);

        // Put some values in the cache
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);

        // Get some values
        System.out.println(cache.get(1));  // Returns 1

        // Add another value (this will evict the least frequently used)
        cache.put(4, 4);

        // Get values again
        System.out.println(cache.get(2));  // Returns -1 (evicted)
        System.out.println(cache.get(3));  // Returns 3
        System.out.println(cache.get(4));  // Returns 4
    }
}
