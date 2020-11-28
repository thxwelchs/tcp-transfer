package domain;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class HashLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
    Map<E, Boolean> hashMap;

    public HashLinkedBlockingQueue() {
        hashMap = new ConcurrentHashMap<>();
    }

    @Override
    public E poll() {
        E e = super.poll();
        hashMap.remove(e);
        return e;
    }

    @Override
    public boolean offer(E e) {
        if(contains(e)) return false;
        hashMap.put(e, true);
        return super.offer(e);
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return hashMap.containsKey(e);
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        hashMap.remove(e);
        return super.remove(o);
    }
}
