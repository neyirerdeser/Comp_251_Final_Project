import java.util.*;
public class NoSuper {
    public static int partition(int array[], int indices[], int low, int high) {
        int pivot = array[high];
        int i = (low-1);
        for (int j=low; j<high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                int temp_i = indices[i];
                array[i] = array[j];
                indices[i] = indices[j];
                array[j] = temp;
                indices[j] = temp_i;
            }
        }
        int temp = array[i+1];
        int temp_i = indices[i+1];
        array[i+1] = array[high];
        indices[i+1] = indices[high];
        array[high] = temp;
        indices[high] = temp_i;
        return i+1;
    }
    public static void sort(int array[], int indices[], int low, int high) {
        if (low < high) {
            int par_i = partition(array, indices, low, high);
            sort(array, indices, low, par_i-1);
            sort(array, indices, par_i+1, high);
        }
    }

    public static ArrayList<ArrayList<Integer>> recursive(int[]A, int[]ind, int target, ArrayList<ArrayList<Integer>>results) {
        if(A.length < 1) return results;
        if(sum(A) < target) return results;
        int n = A.length - 1;
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        numbers.clear();
        indices.clear();
        numbers.add(A[n]);
        indices.add(ind[n]);
        for (int i = n; i >= 0; i--) {
            if (numbers.size() == 1 && sum(numbers) >= target) {
                ArrayList<Integer>result = new ArrayList<>(indices);
                results.add(result);
                int[] a = new int[n];
                System.arraycopy(A, 0, a, 0, n);
                return recursive(a, ind, target, results);
            }
            else if (sum(numbers) >= target) {
                ArrayList<Integer>result = new ArrayList<>(indices);
                results.add(result);
                if(i == 0) {
                    int[] a = new int[n];
                    System.arraycopy(A, 0, a, 0, n);
                    return recursive(a, ind, target, results);
                } else {
                    numbers.remove(numbers.size() - 1);
                    indices.remove(indices.size() - 1);
                    numbers.add(A[i - 1]);
                    indices.add(ind[i - 1]);
                }
            }
            else {
                if(i == 0) {
                    int[] a = new int[n];
                    System.arraycopy(A, 0, a, 0, n);
                    return recursive(a, ind, target, results);
                }
                numbers.add(A[i-1]);
                indices.add(ind[i-1]);
            }
        }
        return results;
    }
        public static ArrayList<ArrayList<Integer>> combinations(int[]A, int[]ind, int target) {
            ArrayList<ArrayList<Integer>>results = new ArrayList<>();
            return recursive(A, ind, target, results);

    }

    public static int sum(ArrayList<Integer>arraylist) {
        int sum = 0;
        for(Integer n : arraylist) sum += n;
        return sum;
    }
    public static int sum(int[]array) {
        int sum = 0;
        for(int n : array) sum += n;
        return sum;
    }



    public static void main(String args[])
    {
//        int arr[] = {10, 7, 8, 9, 1, 5};
//
//        int n = arr.length;
//        int indices[] = new int[n];
//        for(int i=0; i<n; i++) indices[i] = i;
//        int a[] = arr.clone();
//        sort(a, indices, 0, n-1);

//        System.out.println(Arrays.toString(a));
//        System.out.println(Arrays.toString(indices));


        Random rnd = new Random(123);
        int[]array = new int[1000];
        for(int i=0; i<array.length; i++) array[i] = rnd.nextInt(60);
        int target = 20000;
        int n = array.length;
        int ind[] = new int[n];
        for(int i=0; i<ind.length; i++) ind[i] = i;
        sort(array, ind, 0, n-1);
        long startTime = System.nanoTime();
        ArrayList<ArrayList<Integer>> solution = combinations(array,ind, target);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        System.out.println(duration/100000000);

//        for(ArrayList<Integer>s:solution) {
//            System.out.println(Arrays.toString(s.toArray()));
//        }
    }
}
