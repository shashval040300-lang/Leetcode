
import java.util.*;

class Solution {
    int n, LOG, timer;
    List<Integer>[] adj;
    int[] tin, tout, depth, parent, order;
    int[][] up;
    int[] bit;

    public List<Boolean> palindromePath(
            int n, int[][] edges, String s, String[] queries) {

        this.n = n;
        LOG = 1;
        while ((1 << LOG) <= n) LOG++;

        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        tin = new int[n];
        tout = new int[n];
        depth = new int[n];
        parent = new int[n];
        order = new int[n];
        up = new int[LOG][n];

        // Iterative DFS: build Euler order and parent information.
        int[] stack = new int[n];
        boolean[] visited = new boolean[n];
        int top = 0;
        stack[top++] = 0;
        visited[0] = true;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];
            tin[u] = timer;
            order[timer++] = u;

            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    depth[v] = depth[u] + 1;
                    stack[top++] = v;
                }
            }
        }

        // Compute subtree sizes in reverse DFS order.
        int[] size = new int[n];
        Arrays.fill(size, 1);

        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];
            if (u != 0) {
                size[parent[u]] += size[u];
            }
        }

        for (int u = 0; u < n; u++) {
            tout[u] = tin[u] + size[u] - 1;
        }

        // Binary lifting table.
        for (int u = 0; u < n; u++) {
            up[0][u] = parent[u];
        }

        for (int j = 1; j < LOG; j++) {
            for (int u = 0; u < n; u++) {
                up[j][u] = up[j - 1][up[j - 1][u]];
            }
        }

        bit = new int[n + 2];
        char[] chars = s.toCharArray();

        // Root-path XOR masks using subtree range updates.
        for (int u = 0; u < n; u++) {
            rangeXor(tin[u], tout[u], 1 << (chars[u] - 'a'));
        }

        List<Boolean> ans = new ArrayList<>();

        for (String q : queries) {
            String[] parts = q.split(" ");
            int u = Integer.parseInt(parts[1]);

            if (parts[0].equals("update")) {
                char c = parts[2].charAt(0);

                if (chars[u] != c) {
                    int delta =
                        (1 << (chars[u] - 'a')) ^
                        (1 << (c - 'a'));

                    rangeXor(tin[u], tout[u], delta);
                    chars[u] = c;
                }
            } else {
                int v = Integer.parseInt(parts[2]);
                int w = lca(u, v);

                int mask = pointQuery(tin[u])
                         ^ pointQuery(tin[v])
                         ^ (1 << (chars[w] - 'a'));

                // At most one character can have odd frequency.
                ans.add((mask & (mask - 1)) == 0);
            }
        }

        return ans;
    }

    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        int diff = depth[u] - depth[v];

        for (int j = 0; j < LOG; j++) {
            if ((diff & (1 << j)) != 0) {
                u = up[j][u];
            }
        }

        if (u == v) return u;

        for (int j = LOG - 1; j >= 0; j--) {
            if (up[j][u] != up[j][v]) {
                u = up[j][u];
                v = up[j][v];
            }
        }

        return parent[u];
    }

    // XOR-update the inclusive Euler interval [l, r].
    void rangeXor(int l, int r, int value) {
        add(l + 1, value);
        add(r + 2, value);
    }

    void add(int i, int value) {
        while (i < bit.length) {
            bit[i] ^= value;
            i += i & -i;
        }
    }

    int pointQuery(int index) {
        int result = 0;
        for (int i = index + 1; i > 0; i -= i & -i) {
            result ^= bit[i];
        }
        return result;
    }
}
