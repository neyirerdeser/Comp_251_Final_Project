import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Combination {
    public static void combination_recursive(int array[], int data[], int start, int index, int r, ArrayList<int[]>combination_r, int target) {
        if (index == r) {
            if(sum_of_indices(array, data) < target) return;
            int[]data_copy = data.clone();
            combination_r.add(data_copy);
            return;
        }
        int end = array.length-1;
        for (int i = start; i <= end && end-i+1 >= r-index; i++) {
            data[index] = i;
            combination_recursive(array, data, i+1, index+1, r, combination_r, target);
        }
    }
    public static ArrayList<int[]> combination(int arr[], int r, int target) {
        int data[] = new int[r];
        ArrayList<int[]>combination = new ArrayList<>();
        combination_recursive(arr, data, 0, 0, r, combination, target);
        return combination;
    }
    public static ArrayList<int[]> all_combinations(int[]a, int target) {
        ArrayList<int[]>combinations = new ArrayList<>();
        for(int i = 1; i <= a.length; i++) {
            combinations.addAll(combination(a, i, target));
        }
        return combinations;
    }
    public static int sum_of_indices(int[]array, int[]indices) {
        int sum = 0;
        for(int i:indices) sum += array[i];
        return sum;
    }

    public static void main (String[] args) {
        Random rnd = new Random(123);
        int[]a = new int[10000];

        for(int i=0; i<a.length; i++) a[i] = rnd.nextInt(60);
        int target = 300000;

        long startTime = System.nanoTime();
        all_combinations(a, target);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println(duration);
    }

}