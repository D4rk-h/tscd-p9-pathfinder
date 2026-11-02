package test;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.ulpgc.pathfinder.GraphContainer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GraphContainerEdgeCasesTest {
	private GraphContainer container;

	@BeforeEach
	void setUp() {
		Graph<String, DefaultEdge> graph = new SimpleWeightedGraph<>(DefaultEdge.class);
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("X");
		graph.addEdge("A", "B");
		graph.setEdgeWeight("A", "B", 1.0);
		container = new GraphContainer(graph);
	}

	@Test
	void testWeightWithUnknownVertexFails() {
		assertThrows(IllegalArgumentException.class, () -> container.pathWeightBetween("A", "Z"));
	}

	@Test
	void testDisconnectedVerticesHaveNoPath() {
		assertThrows(IllegalArgumentException.class, () -> container.shortestPathBetween("A", "X"));
		assertThrows(IllegalArgumentException.class, () -> container.pathWeightBetween("A", "X"));
	}
}
