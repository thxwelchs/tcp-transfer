package domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class TransferableDataQueue<E> {
    private final int MAXIMUM_DATA_SIZE = 100;
    private BlockingQueue<E> taskQueue;
    private BlockingQueue<E> processedQueue;
//    private BlockingQueue<E> processedQueue;

    public TransferableDataQueue() {
        this.taskQueue = new HashLinkedBlockingQueue<>();
        this.processedQueue = new HashLinkedBlockingQueue<>();
    }

    public void enqueueAll(List<E> datas) {
        for (E data : datas) {
            if(!processedQueue.contains(data)) enqueue(data);
        }
    }

    public boolean enqueue(E e) {
        if(taskQueue.size() > 100 || taskQueue.contains(e)) return false;
        return taskQueue.offer(e);
    }

    public E dequeue() {
        E e = taskQueue.poll();
        if(e != null && !processedQueue.contains(e)) {
            processedQueue.add(e);
        }
        return e;
    }

    public List<E> getDatas() {
        return getDatas(MAXIMUM_DATA_SIZE);
    }

    public List<E> getDatas(int size) {
        AtomicInteger atomicSize = new AtomicInteger(size);
        List<E> list = new ArrayList<>();
        while(!taskQueue.isEmpty() && atomicSize.getAndDecrement() >= 0) {
            list.add(dequeue());
        }
        return list;
    }

    public E dequeueProcessdData() {
        if(isProcessedDataQueueEmpty()) return null;
        return processedQueue.poll();
    }

    public boolean isProcessedDataQueueEmpty() {
        return processedQueue.isEmpty();
    }

    public void done(E e) {
        processedQueue.remove(e);
    }

    public int getProcessedQueueSize() {
        return processedQueue.size();
    }
}
