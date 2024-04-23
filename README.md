# Trees-Kruskals-Prims-Algorithms

This Java program implements Kruskal's and Prim's algorithms for finding the Minimum Spanning Tree (MST) of a graph. It provides a graphical user interface (GUI) using Swing, allowing users to input the size of the graph and choose between Kruskal's, Prim's, or comparing both algorithms based on their execution times. The code generates a random weighted graph, applies the selected algorithm, and displays the MST along with their total costs. This project is suitable for learning about MST algorithms in Java.

#### Kruskal's Algorithm Steps:
1. **Sort Edges:** Sort all the edges in non-decreasing order of their weight.
2. **Initialize Disjoint Sets:** Create a disjoint set for each vertex in the graph.
3. **Process Edges:** Iterate through the sorted edges. For each edge:
   - If including the edge forms a cycle in the spanning tree, discard it.
   - Otherwise, include the edge in the spanning tree and update the disjoint sets.
4. **Output:** The final result is the Minimum Spanning Tree.

#### Prim's Algorithm Steps:
1. **Choose a Starting Node:** Start from an arbitrary node as the initial vertex.
2. **Initialize Key Values:** Initialize key values of all vertices to INFINITE and the key value of the initial node to 0.
3. **Select Minimum Key Vertex:** Repeat the following until all vertices are included:
   - Pick the vertex u with the minimum key value that is not yet included.
   - Include u in the MST.
   - Update the key values of adjacent vertices of u.
4. **Output:** The final result is the Minimum Spanning Tree.
