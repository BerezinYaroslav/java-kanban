package taskTracker.manager.history;

import taskTracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> map = new HashMap<>();

    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        int id = task.getId();

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        if (map.containsKey(id)) {
            remove(id);
        }

        map.put(task.getId(), newNode);
        size++;
    }

    private void removeNode(int id) {
        if (map.containsKey(id)) {
            Node x = map.get(id);
            Node prev = x.prev;
            Node next = x.next;

            if (prev == null) {
                head = x.next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = x.prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            size--;
        }
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node headNode = head;

        while (headNode != null) {
            list.add(headNode.data);
            headNode = headNode.next;
        }

        return list;
    }

    public int size() {
        return this.size;
    }

    class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
