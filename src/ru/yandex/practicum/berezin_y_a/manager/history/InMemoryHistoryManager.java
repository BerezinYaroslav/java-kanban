package ru.yandex.practicum.berezin_y_a.manager.history;

import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> map = new HashMap<>();

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
        int id = task.getId();

        if (map.containsKey(id)) {
            remove(id);
        }

        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        map.put(task.getId(), newNode);
        size++;
    }

    private void removeNode(int id) {
        if (map.containsKey(id)) {
            Node currentNode = map.get(id);
            Node prev = currentNode.prev;
            Node next = currentNode.next;

            if (prev == null) {
                head = currentNode.next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = currentNode.prev;
            } else {
                next.prev = prev;
            }

            map.remove(id);
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

    private class Node {
        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
