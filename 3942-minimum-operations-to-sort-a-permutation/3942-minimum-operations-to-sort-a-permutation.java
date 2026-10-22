class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        boolean ascending = true;
        boolean descending = true;

        for (int i = 0; i < n; i++) {
            if (nums[i] != (nums[0] + i) % n) {
                ascending = false;
            }

            if (nums[i] != (nums[0] - i + n) % n) {
                descending = false;
            }
        }

        if (ascending) {
            int k = (n - nums[0]) % n;
            return Math.min(k, n - k + 2);
        }

        if (descending) {
            int k = (n - nums[0] - 1 + n) % n;
            return Math.min(k + 1, n - k + 1);
        }

        return -1;
    }
}