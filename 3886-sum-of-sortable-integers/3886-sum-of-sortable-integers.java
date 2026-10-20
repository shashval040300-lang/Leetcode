class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int ans = 0;

        for (int k = 1; k <= n; k++) {
            if (n % k != 0) continue;

            boolean valid = true;
            int prev = Integer.MIN_VALUE;

            for (int i = 0; i < n; i += k) {
                int descents = 0;
                int start = i;

                for (int j = i; j < i + k - 1; j++) {
                    if (nums[j] > nums[j + 1]) {
                        descents++;
                        start = j + 1;
                    }
                }

                if (descents > 1) {
                    valid = false;
                    break;
                }

                if (descents == 1 && nums[i + k - 1] > nums[i]) {
                    valid = false;
                    break;
                }

                int min = nums[start];
                int max = nums[start];

                for (int j = i; j < i + k; j++) {
                    min = Math.min(min, nums[j]);
                    max = Math.max(max, nums[j]);
                }

                if (min < prev) {
                    valid = false;
                    break;
                }

                prev = max;
            }

            if (valid) ans += k;
        }

        return ans;
    }
}