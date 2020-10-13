package Aufgabe1;

import java.lang.Math;
import java.util.Arrays;
import java.util.Stack;

public class Quicksort {
    public static void main(String []args) {
        final int SIZE = 10;
        int[] array = new int[SIZE];
        int random;

        for(int i = 0; i < SIZE; i++) {
            array[i] = (int) (Math.random() * 11);
        }

        System.out.println(Arrays.toString(array));

        quicksort(array);

        System.out.println(Arrays.toString(array));
    }

    public static void quicksort(int[] array) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(0);
        stack.push(array.length);

        while(!stack.isEmpty()) {
            int last = stack.pop();
            int first = stack.pop();

            if(last - first < 2) {
                continue;
            }

            int pivot = first + ((last - first) / 2);
            pivot = partition(array, pivot, first, last);

            stack.push(pivot + 1);
            stack.push(last);
            stack.push(first);
            stack.push(pivot);
        }
    }

    public static int partition(int[] array, int pivot, int start, int end) {
        int low = start;
        int high = end - 2;
        int pivotElement = array[pivot];

        swap(array, pivot, end - 1);

        while(low < high) {
            if(array[low] < pivotElement) {
                low++;
            } else if(array[high] >= pivotElement) {
                high--;
            } else {
                swap(array, low, high);
            }
        }

        int index = high;

        if(array[high] < pivotElement) {
            index++;
        }

        swap(array, end - 1, index);
        return index;
    }

    public static void swap(int[] array, int i, int j) {
        int tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
    }
}
