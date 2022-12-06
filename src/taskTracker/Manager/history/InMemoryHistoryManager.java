package taskTracker.Manager.history;

import taskTracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> map = new HashMap<>();

    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        int id = task.getId();

        if (map.containsKey(id)) {
            remove(id);
        }

        map.put(id, linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (map.containsKey(id)) {
            Node node = map.get(id);
            removeNode(node);
        }
    }

    public Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        size++;
        return newNode;
    }

    private void removeNode(Node x) {
        Task oldData = x.data;
        Node prev = x.prev;
        Node next = x.next;

        if (prev == null) {
            head = x.next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = x.prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
        size--;
    }

    public List<Task> getTasks() {
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
