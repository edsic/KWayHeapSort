package org.taozhang.utils;

import java.util.ArrayDeque;
import java.util.Queue;

public class Input {
    private Queue processQueue;
    private String name;

    public Input(String name) {
        this.processQueue = new ArrayDeque();
        this.name = name;
    }

    public Input(Queue processQueue, String name) {
        this.processQueue = processQueue;
        this.name = name;
    }

    public Queue getProcessQueue() {
        return processQueue;
    }

    public void setProcessQueue(Queue processQueue) {
        this.processQueue = processQueue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initialize(int length) {
        int lastElement = 0;
        for (int i = 0; i < length; i++) {
            if (processQueue.size() == 0) {
                lastElement = randomWithRange(1, 10);
            } else {
                lastElement = lastElement + randomWithRange(1, 10);
            }
            processQueue.add(lastElement);
        }
    }

    int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        processQueue.forEach(ele -> sb.append(ele).append(";"));

        return "Input{" +
                "processQueue=" + "{" + sb.toString() + "}" +
                ", name='" + name + '\'' +
                '}';
    }
}
