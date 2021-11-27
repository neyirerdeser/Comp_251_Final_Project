import java.util.ArrayList;
import java.util.Arrays;

class SumSet {
    static void sum_up_recursive(ArrayList<Integer> numbers, int target, ArrayList<Integer> partial, ArrayList<ArrayList<Integer>>paths) {
        int s = 0;
        for (int x: partial) s += x;
        if (s >= target) {
            paths.add(partial);
            return;
        }
        for(int i=0;i<numbers.size();i++) {
            ArrayList<Integer> remaining = new ArrayList<>();
            int n = numbers.get(i);
            for (int j=i+1; j<numbers.size();j++) remaining.add(numbers.get(j));
            ArrayList<Integer> partial_rec = new ArrayList<>(partial);
            partial_rec.add(n);
            sum_up_recursive(remaining,target,partial_rec, paths);
        }
    }
    static void sum_up(ArrayList<Integer> numbers, int target, ArrayList<ArrayList<Integer>>paths) {
        sum_up_recursive(numbers,target,new ArrayList<>(), paths);
    }
    public static void main(String args[]) {
        int[] numbers = {3,9,8,4,5,7,10};
        ArrayList<ArrayList<Integer>>sums = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<>(numbers.length);
        for (int i : numbers)
            nums.add(i);
        int target = 15;
        sum_up(nums,target,sums);
        System.out.println(Arrays.toString(sums.toArray()));
    }
}