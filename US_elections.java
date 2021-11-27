import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {

	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided) {
		int points_secured = 0;
		int points_available = 0;
		int points_total = 0;
		ArrayList<Integer> removed = new ArrayList<>();
		for(int i=0; i<num_states; i++) {
			points_total += delegates[i];

			if(votes_Biden[i] > votes_Trump[i] + votes_Undecided[i]) {
				points_secured += delegates[i];
				removed.add(i);
			}
			else if(votes_Trump[i] >= votes_Biden[i] + votes_Undecided[i]) {
				removed.add(i);
			}
			else {
				points_available += delegates[i];
			}
		}

		if(points_secured + points_available <= points_total/2) return -1;	// check if winning is even possible

		for (int i = removed.size()-1; i>=0; i-- ) {						// removing from higest index to lowest assures that next index removed IS that was meant
			int j = removed.get(i);
			delegates = remove(delegates, j);
			votes_Biden = remove(votes_Biden, j);
			votes_Trump = remove(votes_Trump, j);
			votes_Undecided = remove(votes_Undecided, j);
		}
		// now we're left with states where it CAN make a difference to convince people

		// from the remaining delegates[] need to find "paths"(combinations) that total to a win
		// what is needed for a win? -> points_total / 2 + 1 - what we already earned
		int points_to_win = points_total/2 + 1;
		int points_needed = points_to_win - points_secured;

		int[] points = delegates.clone();

		int n = points.length;
		int ind[] = new int[n];
		for(int i=0; i<n; i++) ind[i] = i;
		sort(points, ind, 0, n-1);

		ArrayList<ArrayList<Integer>>state_combinations = combinations(points, ind, points_needed);

		// now we have all combinations for states, which the delegates are just enough to win the election
		// which one of these combinations require the least amount of people convinced
		int min_convince = Integer.MAX_VALUE;
		for(ArrayList<Integer> state_ids : state_combinations) {
			int state_convince = people(state_ids, votes_Biden, votes_Trump, votes_Undecided);
			if(state_convince < min_convince) min_convince = state_convince;
		}
		return min_convince;
	}

	public static int people(ArrayList<Integer>indices, int[]biden, int[]trump, int[]und) {
		int sum = 0;
		for(int i : indices) {
			int total_people = biden[i] + trump[i] + und[i];
			total_people /= 2;
			total_people += 1;
			int needed = total_people - biden[i];
			sum += needed;
		}
		return sum;
	}

	public static int[] remove(int[]array, int index) {
		if(index >= array.length) return array;
		int[]refined = new int[array.length-1];
		for(int i=0; i<index; i++) {
			refined[i] = array[i];
		}
		for(int i=index; i<refined.length; i++) {
			refined[i] = array[i+1];
		}
		return refined;
	}

	// quicksort implication : receiving an array AND a second array of indices
	// sorting both together means the index array will tell us the original positions of the elements
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

	// method to find all combinations given an array
	// but it does not give any combinations' superset to avoid lengthy measuring times
	// since that means the original subset was already enough for our purposes and extra elements mean extra votes that are not required
	public static ArrayList<ArrayList<Integer>> combinations_recursive(int[]A, int[]ind, int target, ArrayList<ArrayList<Integer>>results) {
		if(A.length < 1) return results;										// end case
		if(sum(A) < target) return results;										// barrier that helps cut down on time
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
				return combinations_recursive(a, ind, target, results);
			}
			else if (sum(numbers) >= target) {
				ArrayList<Integer>result = new ArrayList<>(indices);
				results.add(result);
				if(i == 0) {
					int[] a = new int[n];
					System.arraycopy(A, 0, a, 0, n);
					return combinations_recursive(a, ind, target, results);
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
					return combinations_recursive(a, ind, target, results);
				}
				numbers.add(A[i-1]);
				indices.add(ind[i-1]);
			}
		}
		return results;
	}
	public static ArrayList<ArrayList<Integer>> combinations(int[]A, int[]ind, int target) {
		ArrayList<ArrayList<Integer>>results = new ArrayList<>();
		return combinations_recursive(A, ind, target, results);
	}
	// tiny helpers for the recursive method
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

	public static void main(String[] args) {
		try {
			String path = args[0];
			File myFile = new File(path);
			Scanner sc = new Scanner(myFile);
			int num_states = sc.nextInt();
			int[] delegates = new int[num_states];
			int[] votes_Biden = new int[num_states];
			int[] votes_Trump = new int[num_states];
			int[] votes_Undecided = new int[num_states];
			for (int state = 0; state<num_states; state++) {
				delegates[state] =sc.nextInt();
				votes_Biden[state] = sc.nextInt();
				votes_Trump[state] = sc.nextInt();
				votes_Undecided[state] = sc.nextInt();
			}
			sc.close();
			int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
			System.out.println(answer);
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}