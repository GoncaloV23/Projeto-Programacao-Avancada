package pt.pa.graph;

import java.util.*;

public class GraphAdjacencyList<V,E> implements Graph<V, E> {

    private Map<V, Vertex<V>> vertices;

    public GraphAdjacencyList() {
        this.vertices = new HashMap<>();
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        MyVertex myU = checkVertex(u);
        MyVertex myV = checkVertex(v);

        //is there a common edge between myU.incidentEdges and myV.incidentEdges ?

        Set<Edge<E,V>> intersection = new HashSet<>(myU.incidentEdges);
        intersection.retainAll(myV.incidentEdges);

        return !intersection.isEmpty();
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges().size();
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return vertices.values();
    }

    @Override
    public Collection<Edge<E, V>> edges() {
        Set<Edge<E, V>> a = new HashSet<>();

        vertices.values().forEach((v) -> {
            MyVertex vertices = (MyVertex) v;
            a.addAll(vertices.incidentEdges);
        });

        return a;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(v);
        return myVertex.getIncidentEdges();
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        MyVertex myVertex = checkVertex(v);
        Vertex<V> returnVertex = null;
        for(Edge<E,V> edge : myVertex.incidentEdges){
            MyEdge myEdge = checkEdge(edge);
            if(e == edge){
                if(myEdge.vertices()[0] == v)
                    returnVertex = myEdge.vertices()[1];
                else if(myEdge.vertices()[1] == v)
                    returnVertex = myEdge.vertices()[0];
            }
        }
        return returnVertex;
    }

    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if(vElement == null)throw new InvalidVertexException("Cannot add Null vertice;");
        if(existsVertexWith(vElement))throw new InvalidVertexException("There's already a vertex with this element.");

        MyVertex myVertex = new MyVertex(vElement);
        vertices.put(vElement, myVertex);

        return myVertex;
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if(edgeElement == null) throw new InvalidEdgeException("Cannot add Null edges;");
        if(existsEdgeWith(u, v))throw new InvalidEdgeException("There's already a edge between these Vertices.");

        MyVertex myV1 = checkVertex(u);
        MyVertex myV2 = checkVertex(v);

        MyEdge myEdge = new MyEdge(edgeElement);
        myV1.incidentEdges.add(myEdge);
        myV2.incidentEdges.add(myEdge);

        return myEdge;
    }

    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if(edgeElement == null)throw new InvalidEdgeException();

        Vertex<V> v1 = vertices.get(vElement1);
        Vertex<V> v2 = vertices.get(vElement2);
        if(v1 == null)v1 = insertVertex(vElement1);
        if(v2 == null)v2 = insertVertex(vElement2);

        return insertEdge(v1, v2, edgeElement);
    }

    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        MyVertex myVertexToRemove = checkVertex(v);

        for(Vertex<V> vertex : vertices.values()){
            MyVertex myVertex = checkVertex(vertex);
            if(myVertexToRemove != myVertex)
                for(int i = myVertex.incidentEdges.size() -1; i>=0; i--){

                    if(myVertex.incidentEdges.get(i).vertices()[0].element().equals(v.element()) ||
                            myVertex.incidentEdges.get(i).vertices()[1].element().equals(v.element()))
                        myVertex.incidentEdges.remove(i);
                }
        }
        V elem = v.element();
        vertices.remove(elem);
        return elem;
    }

    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        MyEdge myEdge = checkEdge(e);

        Iterator<Vertex<V>> it = vertices().iterator();
        while (it.hasNext()){
            MyVertex myVertex = checkVertex(it.next());
            myVertex.incidentEdges.remove(myEdge);
        }

        return myEdge.element;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        MyVertex myVertex = checkVertex(v);
        if(existsVertexWith(newElement))throw new InvalidVertexException("");

        V oldElement = myVertex.element;
        myVertex.element = newElement;

        return oldElement;
    }

    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        MyEdge myEdge = checkEdge(e);

        E oldElement = myEdge.element;
        myEdge.element = newElement;

        return oldElement;
    }

    private class MyVertex implements Vertex<V> {
        private V element;
        private List<Edge<E,V>> incidentEdges;

        public MyVertex(V element) {
            this.element = element;
            this.incidentEdges = new ArrayList<>();
        }

        @Override
        public V element() {
            return element;
        }

        public List<Edge<E, V>> getIncidentEdges() {
            return incidentEdges;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}' + " --> " + incidentEdges.toString();
        }
    }

    private class MyEdge implements Edge<E, V> {
        private E element;

        public MyEdge(E element) {
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        @Override
        public Vertex<V>[] vertices() {
            //if the edge exists, then two existing vertices have the edge
            //in their incidentEdges lists
            List<Vertex<V>> adjacentVertices = new ArrayList<>();

            for(Vertex<V> v : GraphAdjacencyList.this.vertices.values()) {
                MyVertex myV = (MyVertex) v;

                if( myV.incidentEdges.contains(this)) {
                    adjacentVertices.add(v);
                }
            }

            if(adjacentVertices.isEmpty()) {
                return new Vertex[]{null, null}; //edge was removed meanwhile
            } else {
                return new Vertex[]{adjacentVertices.get(0), adjacentVertices.get(1)};
            }
        }

        @Override
        public String toString() {
            return "Edge{" + element + "}";
        }
    }

    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if(v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsValue(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if(e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    private boolean existsVertexWith(V element) {
        return vertices.keySet().contains(element);
    }
    private boolean existsEdgeWith(Vertex<V> u, Vertex<V> v) {
        if(!vertices.values().contains(u))return false;
        if(!vertices.values().contains(v))return false;

        return this.areAdjacent(u, v);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph | Adjacency List : \n");

        for(Vertex<V> v : vertices.values()) {
            sb.append( String.format("%s", v) );
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public Iterator<Vertex<V>> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator<Vertex<V>>{
        private Iterator<Map.Entry<V,Vertex<V>>> it;
        public MyIterator(){
            it = vertices.entrySet().iterator();
        }
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Vertex<V> next() {
            return it.next().getValue();
        }
    }
}
