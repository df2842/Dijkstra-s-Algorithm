import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Collection;
import java.util.PriorityQueue;

import java.lang.Math;


public class Graph<V> { 
   
    // Keep an index from node labels to nodes in the map
    protected Map<V, Vertex<V>> vertices;

    /**
     * Construct an empty Graph.
     */
    public Graph() {
       vertices = new HashMap<V, Vertex<V>>(); //key = V; value = Vertex
    }

    /**
     * Retrieve a collection of vertices. 
     */  
    public Collection<Vertex<V>> getVertices() {
        return vertices.values(); //getVertices is the values in hashmap
    }

    public void addVertex(V u) {
        addVertex(new Vertex<>(u));
    }
    
    public void addVertex(Vertex<V> v) {
        if (vertices.containsKey(v.name)) 
            throw new IllegalArgumentException("Cannot create new vertex with existing name.");
        vertices.put(v.name, v);
    }

    /**
     * Add a new edge from u to v.
     * Create new nodes if these nodes don't exist yet. 
     * @param u unique name of the first vertex.
     * @param w unique name of the second vertex.
     * @param cost cost of this edge. 
     */
    public void addEdge(V u, V w, Double cost) {
        if (!vertices.containsKey(u))
            addVertex(u);
        if (!vertices.containsKey(w))
            addVertex(w);

        vertices.get(u).addEdge(
            new Edge<>(vertices.get(u), vertices.get(w), cost)); 

    }

    public void addEdge(V u, V w) {
        addEdge(u,w,1.0);
    }

    public void printAdjacencyList() {
        for (V u : vertices.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(u.toString());
            sb.append(" -> [ ");
            for (Edge e : vertices.get(u).getEdges()){
                sb.append(e.target.name);
                sb.append(" ");
            }
            sb.append("]");
            System.out.println(sb.toString());
        }
    }    
  
   /**
    * Add a bidirectional edge between u and v. Create new nodes if these nodes don't exist
    * yet. This method permits adding multiple edges between the same nodes.
    *
    * @param u  
    *          the name of the source vertex.
    * @param v 
    *          the name of the target vertex.
    * @param cost
    *          the cost of this edge
    */
    public void addUndirectedEdge(V u, V v, Double cost) {
        addEdge(u,v, cost);
        addEdge(v,u, cost);
    }

    /****************************
     * Your code follows here.  *
     ****************************/ 
    
    // Part 1
    public void computeAllEuclideanDistances() {
        for (Vertex<V> node: getVertices())
        {
            for (Edge<V> edge: node.getEdges())
            {
                edge.distance = Math.sqrt(Math.pow(edge.source.posX - edge.target.posX, 2) + Math.pow(edge.source.posY - edge.target.posY, 2));
            }
        }
    }
    
    // Part 2
    public void doDijkstra(V s) {
        Vertex<V> start = vertices.get(s);
        PriorityQueue<Vertex<V>> queue = new PriorityQueue<Vertex<V>>();

        queue.add(start);

        while (queue.size() != 0)
        {
            Vertex<V> current = queue.poll();
            current.visited = true;

            for (Edge<V> edge: current.getEdges())
            {
                if (!edge.target.visited)
                {
                    if (queue.contains(edge.target))
                    {
                        queue.remove(edge.target);
                    }      
                    if (current.cost + edge.distance < edge.target.cost || edge.target.cost == 0)
                    {
                        edge.target.cost = current.cost + edge.distance;
                        edge.target.backpointer = current;
                    }  
                    queue.add(edge.target);        
                }
            }
        }
    }

    // Part 3
    public List<Edge<V>> getDijkstraPath(V s, V t) {
        doDijkstra(s);

        List<Edge<V>> path = new LinkedList<Edge<V>>();
        Vertex<V> previous = vertices.get(t);

        while (previous.backpointer != null)
        {
            for (Edge<V> edge: previous.getEdges())
            {
                if (edge.target.equals(previous.backpointer))
                {
                    path.add(path.size(), edge);
                }
            }
            previous = previous.backpointer;
        }
        
        return path;
    }  
    
}
