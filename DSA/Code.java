import java.util.Arrays;

import java.util.Map;
public class Code {
    public static void main(String[] args) {
        int[] nums = {3,1,5,4,2};
        Sort(nums);
        System.out.println(Arrays.toString(nums));
    }

    private static void Sort(int[] nums) {
        int idx = 0, n = nums.length;

        while (idx < n) {
            int home = nums[idx] - 1;
            if(home != idx) swap(nums, idx, home);
            else idx++;
        }
    }

    private static  void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
