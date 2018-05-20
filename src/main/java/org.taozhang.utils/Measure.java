package org.taozhang.utils;

import it.unimi.dsi.fastutil.objects.ObjectHeaps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;

public class Measure {

    private static int NUMBER_N = 200;
    private static int NUMBER_K = 10000;
    private static Input[] input1 = new Input[NUMBER_N];
    private static Input[] input2 = new Input[NUMBER_N];
    private static final String PREFIX = "PREFIX_";
    private static final String PREFIX2 = "PREFIX2_";

    public static void main(String[] args) {
        for (int i = 0; i < NUMBER_N; i++) {
            input1[i] = new Input(PREFIX + i);
            input1[i].initialize(NUMBER_K);
        }

        measureTime(input1, "selectionSortNInput");

        for (int i = 0; i < NUMBER_N; i++) {
            input1[i] = new Input(PREFIX2 + i);
            input1[i].initialize(NUMBER_K);
        }

        measureTime(input1, "heapSortNInput");
    }

    public static void measureTime(Input[] input, String methodName) {
        Method[] validMethods = Measure.class.getMethods();
        try {
            for (Method method : validMethods) {
                if (method.getName().equalsIgnoreCase(methodName)) {
                    long startTime = System.currentTimeMillis();
                    Object invokeResult = method.invoke(null, new Object[]{input});
                    long endTime = System.currentTimeMillis();
                    System.out.println(methodName + " sort method spents: " + (endTime - startTime));
                    if (invokeResult instanceof ArrayList) {
                        validateSortedList((ArrayList) invokeResult);
                    }
                    break; // first matched method should be enough
                }
            }
        } catch (Exception e) {
            System.out.println("couldn't find such method: " + methodName);
            e.printStackTrace();
        }
    }

    public static void validateSortedList(ArrayList arrayList) {
        int size = arrayList.size();
        System.out.println("result size = " + size);
        for(int i=1; i< size; i++) {
            if ((int) arrayList.get(i) < (int) arrayList.get(i-1)) {
                System.out.println("ERROR: arraylist is not sorted correctly");
            }
        }
    }

    public static ArrayList selectionSortNInput(Input[] input) {
        ArrayList<Integer> result = new ArrayList<>();
        int len = input.length;
        boolean stop = false;
        while (!stop) {
            Input inputCandidate = new Input("tmp");
            int candidate = -1;
            for (int i = 0; i < len; i++) {
                if (input[i].getProcessQueue().size() == 0) {
                    continue;
                }
                if (candidate == -1) {
                    candidate = (int) input[i].getProcessQueue().peek();
                    inputCandidate = input[i];
                    continue;
                }
                if (candidate > (int) input[i].getProcessQueue().peek()) {
                    candidate = (int) input[i].getProcessQueue().peek();
                    inputCandidate = input[i];
                }
            }
            if (inputCandidate.getProcessQueue().size() != 0) {
                result.add((int) inputCandidate.getProcessQueue().poll());
            } else {
                stop = true;
            }
        }
        return result;
    }

    static Comparator<Input> inputComparator = new Comparator<Input>() {
        @Override
        public int compare(final Input o1, final Input o2) {
            Queue processQueue1 = o1.getProcessQueue();
            Queue processQueue2 = o2.getProcessQueue();
            if (processQueue1.size() == 0 || processQueue2.size() == 0) {
                return processQueue2.size() - processQueue1.size();
            }
            return (int) processQueue1.peek() - (int) processQueue2.peek();
        }
    };


    public static ArrayList heapSortNInput(Input[] input) {
        ArrayList<Integer> result = new ArrayList<>();
        int len = input.length;
        ObjectHeaps.makeHeap(input, len, inputComparator);

        boolean stop = false;
        while (!stop) {
            Input inputCandidate = input[0];

            if (inputCandidate.getProcessQueue().size() != 0) {
                result.add((int) inputCandidate.getProcessQueue().poll());
                ObjectHeaps.downHeap(input, len, 0, inputComparator);
            } else {
                stop = true;
            }
        }
        return result;
    }
}
