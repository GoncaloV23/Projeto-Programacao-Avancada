package pt.pa.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphAdjacencyListTest {
    Graph<String, Integer> graph;
    Vertex<String> vA;
    Vertex<String> vB;
    Vertex<String> vC;
    Vertex<String> vD;
    Vertex<String> vE;
    Vertex<String> vF;
    Edge<Integer, String> e1;
    Edge<Integer, String> e2;
    Edge<Integer, String> e3;
    Edge<Integer, String> e4;
    Edge<Integer, String> e5;
    Edge<Integer, String> e6;

    @BeforeEach
    void setUp(){
        graph = new GraphAdjacencyList<>();

        vA = graph.insertVertex("A");
        vB = graph.insertVertex("B");
        vC = graph.insertVertex("C");
        vD = graph.insertVertex("D");
        vE = graph.insertVertex("E");

        e1 = graph.insertEdge(vA, vB, 10);
        e2 = graph.insertEdge(vA, vC, 15);
        e3 = graph.insertEdge(vD, vB, 5);
        e4 = graph.insertEdge(vE, vC, 13);
        e5 = graph.insertEdge(vE, vB, 8);
        e6 = graph.insertEdge(vC, vB, 12);
    }

    @Test
    void assert_numVertices_num_vertices(){
        assertEquals(5, graph.numVertices());
    }

    @Test
    void assert_numEdges_num_edges(){
        assertEquals(6, graph.numEdges());
    }

    @Test
    void assert_Vertices_verices(){
        assertTrue(graph.vertices().contains(vA));
        assertTrue(graph.vertices().contains(vB));
        assertTrue(graph.vertices().contains(vC));
        assertTrue(graph.vertices().contains(vD));
        assertTrue(graph.vertices().contains(vE));
    }

    @Test
    void assert_Edges_edges(){
        assertTrue(graph.edges().contains(e1));
        assertTrue(graph.edges().contains(e2));
        assertTrue(graph.edges().contains(e3));
        assertTrue(graph.edges().contains(e4));
        assertTrue(graph.edges().contains(e5));
        assertTrue(graph.edges().contains(e6));
    }

    @Test
    void assert_incidentEdges_incident_edges(){
        assertTrue(graph.incidentEdges(vA).contains(e1));
        assertTrue(graph.incidentEdges(vB).contains(e1));
        assertTrue(graph.incidentEdges(vA).contains(e2));
        assertTrue(graph.incidentEdges(vC).contains(e2));
        assertTrue(graph.incidentEdges(vB).contains(e3));
        assertTrue(graph.incidentEdges(vD).contains(e3));
        assertTrue(graph.incidentEdges(vE).contains(e4));
        assertTrue(graph.incidentEdges(vC).contains(e4));
        assertTrue(graph.incidentEdges(vE).contains(e5));
        assertTrue(graph.incidentEdges(vB).contains(e5));
        assertTrue(graph.incidentEdges(vC).contains(e6));
        assertTrue(graph.incidentEdges(vB).contains(e6));

        assertFalse(graph.incidentEdges(vC).contains(e1));
        assertFalse(graph.incidentEdges(vD).contains(e1));
        assertFalse(graph.incidentEdges(vE).contains(e1));
        assertFalse(graph.incidentEdges(vD).contains(e2));
        assertFalse(graph.incidentEdges(vB).contains(e2));
        assertFalse(graph.incidentEdges(vE).contains(e2));
        assertFalse(graph.incidentEdges(vA).contains(e3));
        assertFalse(graph.incidentEdges(vC).contains(e3));
        assertFalse(graph.incidentEdges(vE).contains(e3));
        assertFalse(graph.incidentEdges(vA).contains(e4));
        assertFalse(graph.incidentEdges(vB).contains(e4));
        assertFalse(graph.incidentEdges(vD).contains(e4));
        assertFalse(graph.incidentEdges(vA).contains(e5));
        assertFalse(graph.incidentEdges(vC).contains(e5));
        assertFalse(graph.incidentEdges(vD).contains(e5));
        assertFalse(graph.incidentEdges(vA).contains(e6));
        assertFalse(graph.incidentEdges(vD).contains(e6));
        assertFalse(graph.incidentEdges(vE).contains(e6));
    }

    @Test
    void assert_insertVertex_insert_vertice(){
        assertFalse(graph.vertices().contains(vF));
        vF = graph.insertVertex("F");
        assertTrue(graph.vertices().contains(vF));
    }

    @Test
    void assert_removeVertex_removes_vertex(){
        assertTrue(graph.vertices().contains(vA));
        assertEquals(vA.element(), graph.removeVertex(vA));
        assertFalse(graph.vertices().contains(vA));
    }
    @Test
    void assert_removeVertex_throws_exception_when_vertexIsNull_Or_DoestExist(){
        assertThrows(InvalidVertexException.class, ()->{graph.removeVertex(null);});

        graph.removeVertex(vA);

        assertFalse(graph.vertices().contains(vA));
        assertThrows(InvalidVertexException.class, ()->{graph.removeVertex(vA);});
    }
    @Test
    void assert_removeEdge_removes_edge(){
        assertTrue(graph.edges().contains(e1));
        assertEquals(e1.element(), graph.removeEdge(e1));
        assertFalse(graph.edges().contains(e1));
    }
    @Test
    void assert_removeEdge_throws_exception_when_vertex_is_null_or_doesnt_Exist(){
        assertThrows(InvalidEdgeException.class, ()->{graph.removeEdge(null);});

        graph.removeEdge(e1);

        assertFalse(graph.edges().contains(e1));
        assertThrows(InvalidEdgeException.class, ()->{graph.removeEdge(e1);});
    }
    @Test
    void assert_replace_replaces_a_vertex(){
        assertEquals("A", vA.element());

        graph.replace(vA, "Teste");
        assertEquals("Teste", vA.element());
    }
    @Test
    void assert_replace_throws_if_Vertex_theres_already_a_vertex_with_value(){
        assertDoesNotThrow(()->{graph.replace(vA, "Teste");});

        assertThrows(InvalidVertexException.class, ()->{graph.replace(vA, "B");});
    }
    @Test
    void assert_replace_throws_when_vertex_doesnt_exist_or_is_null(){
        graph.removeVertex(vA);
        assertFalse(graph.vertices().contains(vA));
        assertThrows(InvalidVertexException.class, ()->{graph.replace(vA, "Teste");});
        assertThrows(InvalidVertexException.class, ()->{graph.replace(null, "Teste");});
    }
    @Test
    void assert_replace_replaces_a_edge(){
        assertEquals(10, e1.element());

        graph.replace(e1, 100);
        assertEquals(100, e1.element());
    }
    @Test
    void assert_replace_throws_when_edge_doesnt_exist_or_is_null(){
        graph.removeEdge(e1);
        assertFalse(graph.edges().contains(e1));
        assertThrows(InvalidEdgeException.class, ()->{graph.replace(e1, 100);});
        assertThrows(InvalidEdgeException.class, ()->{graph.replace(null, 100);});
    }

    @Test
    void assert_insert_vertex_that_exists() {
        assertEquals(graph.vertices().size(), 5);
        assertThrows(InvalidVertexException.class, () -> {
            graph.insertVertex("A");
        });
        assertEquals(graph.vertices().size(), 5);
    }

    @Test
    void assert_insert_vertex_doesnt_that_exists(){
        assertFalse(graph.vertices().contains(vF));
        assertEquals(graph.vertices().size(), 5);
        vF = graph.insertVertex("F");
        assertTrue(graph.vertices().contains(vF));
        assertEquals(graph.vertices().size(), 6);
    }

    @Test
    void assert_opposite_vertex_exists(){
        assertEquals(graph.opposite(vA, e1), vB);
    }
    @Test
    void assert_opposite_vertex_that_doesnt_exists(){
        assertEquals(graph.opposite(vE, e3), null);
    }

    @Test
    void assert_insert_edge_with_vertices_null_edge(){
        assertThrows(InvalidEdgeException.class, () -> {
            graph.insertEdge(vA, vB, null);
        });
    }

    @Test
    void assert_insert_edge_with_vertices(){
        Edge<Integer, String> e7 = null;
        Vertex<String> v1 = graph.insertVertex(new String("1"));
        Vertex<String> v2 = graph.insertVertex(new String("2"));
        assertFalse(graph.incidentEdges(v1).contains(e7));
        assertFalse(graph.incidentEdges(v2).contains(e7));
        e7 = graph.insertEdge(v1, v2, 20);
        assertTrue(graph.incidentEdges(v1).contains(e7));
        assertTrue(graph.incidentEdges(v2).contains(e7));
    }

    @Test
    void assert_insert_edge_with_elements_null_edge(){
        assertThrows(InvalidEdgeException.class, () -> {
            graph.insertEdge("A", "B", null);
        });
    }
    @Test
    void assert_insert_edge_with_elements(){
        Edge<Integer, String> e7 = null;
        e7 = graph.insertEdge("1", "2", 20);
        assertTrue(graph.edges().contains(e7));
    }

}