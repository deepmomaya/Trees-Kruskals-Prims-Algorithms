import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// Class representing an edge in the graph
class Edge {
    int src, destn, weight;

    public Edge(int src, int destn, int weight) {
        this.src = src;
        this.destn = destn;
        this.weight = weight;
    }
}

// Class representing a graph
class Graph {
    int V, E;
    Edge[] edges;

    public Graph(int V, int E) {
        this.V = V;
        this.E = E;
        edges = new Edge[E];
    }
}

public class Trees extends JFrame implements ActionListener {
    // GUI components
    private JTextField sizeField;
    private JRadioButton kruskalButton;
    private JRadioButton primButton;
    private JRadioButton comparisonButton;
    private JTextArea resultArea;

    // Constructor
    public Trees() {
        setTitle("CSE 5311 - PROJECT - MST - DXM6217");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setLocationRelativeTo(null);
    }

    // Place GUI components
    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel sizeLabel = new JLabel("Graph Size:");
        sizeField = new JTextField(10);
        JButton runButton = new JButton("Run");
        runButton.addActionListener(this);

        ButtonGroup algorithmGroup = new ButtonGroup();
        kruskalButton = new JRadioButton("Kruskal's Algorithm");
        primButton = new JRadioButton("Prim's Algorithm");
        comparisonButton = new JRadioButton("Time Comparison");

        algorithmGroup.add(kruskalButton);
        algorithmGroup.add(primButton);
        algorithmGroup.add(comparisonButton);

        inputPanel.add(sizeLabel);
        inputPanel.add(sizeField);
        inputPanel.add(kruskalButton);
        inputPanel.add(primButton);
        inputPanel.add(comparisonButton);
        inputPanel.add(runButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Run")) {
            runAlgorithms();
        }
    }

    // Run algorithms based on user selection
    private void runAlgorithms() {
        resultArea.setText("");
        try {
            int size = Integer.parseInt(sizeField.getText());

            // Check if the size is less than or equal to 1
            if (size <= 1) {
                resultArea.setText("Please enter a valid graph size greater than 1.");
                return;
            }

            // Generate a random graph
            Graph graph = generateRandomGraph(size);

            // Run Kruskal's algorithm and measure time
            if (kruskalButton.isSelected()) {
                long kruskalStartTime = System.nanoTime();
                ArrayList<Edge> kruskalMST = kruskalMST(graph);
                long kruskalEndTime = System.nanoTime();
                long kruskalTime = kruskalEndTime - kruskalStartTime;

                displayResult("Kruskal's Algorithm", graph, kruskalMST);
                resultArea.append("Kruskal's MST Time: " + kruskalTime + " ns\n");
            }
            // Run Prim's algorithm and measure time
            else if (primButton.isSelected()) {
                long primStartTime = System.nanoTime();
                ArrayList<Edge> primMST = primMST(graph);
                long primEndTime = System.nanoTime();
                long primTime = primEndTime - primStartTime;

                displayResult("Prim's Algorithm", graph, primMST);
                resultArea.append("Prim's MST Time: " + primTime + " ns\n");
            }
            // Run both algorithms and compare times
            else if (comparisonButton.isSelected()) {
                long kruskalStartTime = System.nanoTime();
                ArrayList<Edge> kruskalMST = kruskalMST(graph);
                long kruskalEndTime = System.nanoTime();
                long kruskalTime = kruskalEndTime - kruskalStartTime;

                long primStartTime = System.nanoTime();
                ArrayList<Edge> primMST = primMST(graph);
                long primEndTime = System.nanoTime();
                long primTime = primEndTime - primStartTime;

                // Display results
                displayResult("Kruskal's Algorithm", graph, kruskalMST);
                resultArea.append("Kruskal's MST Time: " + kruskalTime + " ns\n\n");

                displayResult("Prim's Algorithm", graph, primMST);
                resultArea.append("Prim's MST Time: " + primTime + " ns\n\n");

                resultArea.append("Time Difference: " + (kruskalTime - primTime) + " ns");
            }

        } catch (NumberFormatException ex) {
            resultArea.setText("Please enter a valid graph size.");
        }
    }

    // Generate a random graph
    private Graph generateRandomGraph(int size) {
        // For simplicity, generating a complete graph with random weights
        int numEdges = size * (size - 1) / 2;
        Graph graph = new Graph(size, numEdges);
        int edgeIndex = 0;

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                int weight = (int) (Math.random() * 100) + 1; // Random weight between 1 and 100
                graph.edges[edgeIndex++] = new Edge(i, j, weight);
            }
        }

        return graph;
    }

    // Kruskal's algorithm helper method
    private void kruskalMSTHelper(Graph graph, ArrayList<Edge> result) {
        Arrays.sort(graph.edges, Comparator.comparingInt(edge -> edge.weight));

        int[] parent = new int[graph.V];
        Arrays.fill(parent, -1);

        for (Edge edge : graph.edges) {
            int x = find(parent, edge.src);
            int y = find(parent, edge.destn);

            if (x != y) {
                result.add(edge);
                union(parent, x, y);
            }
        }
    }

    // Kruskal's algorithm
    private ArrayList<Edge> kruskalMST(Graph graph) {
        ArrayList<Edge> result = new ArrayList<>();
        kruskalMSTHelper(graph, result);
        return result;
    }

    // Prim's algorithm helper method
    private void primMSTHelper(Graph graph, int[] parent, int[] key, boolean[] mstSet) {
        for (int count = 0; count < graph.V - 1; count++) {
            int u = minKey(key, mstSet);
            mstSet[u] = true;

            for (Edge edge : graph.edges) {
                if (!mstSet[edge.destn] && edge.weight < key[edge.destn]) {
                    parent[edge.destn] = u;
                    key[edge.destn] = edge.weight;
                }
            }
        }
    }

    // Prim's algorithm
    private ArrayList<Edge> primMST(Graph graph) {
        ArrayList<Edge> result = new ArrayList<>();
        int[] parent = new int[graph.V];
        int[] key = new int[graph.V];
        boolean[] mstSet = new boolean[graph.V];

        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        parent[0] = -1;

        primMSTHelper(graph, parent, key, mstSet);

        for (int i = 1; i < graph.V; i++) {
            result.add(new Edge(parent[i], i, key[i]));
        }

        return result;
    }

    // Find operation for disjoint set
    private int find(int[] parent, int i) {
        if (parent[i] == -1)
            return i;
        return find(parent, parent[i]);
    }

    // Union operation for disjoint set
    private void union(int[] parent, int x, int y) {
        int xSet = find(parent, x);
        int ySet = find(parent, y);
        parent[xSet] = ySet;
    }

    // Find the vertex with minimum key value
    private int minKey(int[] key, boolean[] mstSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < key.length; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    // Display result
    private void displayResult(String algorithm, Graph graph, ArrayList<Edge> mst) {
        StringBuilder result = new StringBuilder(algorithm + " --->\n");

        // Display original graph edges
        result.append("\nOriginal Graph Edges:\n");
        for (Edge edge : graph.edges) {
            result.append("(").append(edge.src).append(")---").append(edge.weight).append("---(").append(edge.destn).append(")\n");
        }

        // Display MST edges and calculate total cost
        int totalCost = 0;
        result.append("\n").append(algorithm).append(" MST Edges:\n");
        for (Edge edge : mst) {
            result.append("(").append(edge.src).append(")---").append(edge.weight).append("---(").append(edge.destn).append(")\n");
            totalCost += edge.weight;
        }

        result.append("\nTotal Cost of ").append(algorithm).append(" MST: ").append(totalCost).append("\n\n");

        resultArea.append(result.toString());
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Trees().setVisible(true));
    }
}
