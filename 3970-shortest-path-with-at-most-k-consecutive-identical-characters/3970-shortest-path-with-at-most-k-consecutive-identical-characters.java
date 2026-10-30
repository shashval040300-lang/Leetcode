
import java.util.*;

class Solution {

    class State {
        int node;
        int count;
        long cost;

        State(int node, int count, long cost) {
            this.node = node;
            this.count = count;
            this.cost = cost;
        }
    }

    public int shortestPath(int n, int[][] edges, String labels, int k) {

        List<int[]>[] adj = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(new int[]{e[1], e[2]});
        }

        long[][] dist = new long[n][k + 1];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE);
        }

        PriorityQueue<State> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(a.cost, b.cost)
        );

        dist[0][1] = 0;
        pq.offer(new State(0, 1, 0));

        while (!pq.isEmpty()) {

            State curr = pq.poll();

            int u = curr.node;
            int count = curr.count;
            long cost = curr.cost;

            if (cost != dist[u][count]) {
                continue;
            }

            if (u == n - 1) {
                return (int) cost;
            }

            for (int[] edge : adj[u]) {

                int v = edge[0];
                int weight = edge[1];

                int newCount;

                if (labels.charAt(u) == labels.charAt(v)) {
                    newCount = count + 1;
                } else {
                    newCount = 1;
                }

                if (newCount > k) {
                    continue;
                }

                long newCost = cost + weight;

                if (newCost < dist[v][newCount]) {

                    dist[v][newCount] = newCost;

                    pq.offer(new State(v, newCount, newCost));
                }
            }
        }

        return -1;
    }
}
