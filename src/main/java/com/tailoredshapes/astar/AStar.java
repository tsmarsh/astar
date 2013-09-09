package com.tailoredshapes.astar;


import java.util.*;

public class AStar {

    private SortedSet<Node> open;
    private Set<Node> closed;
    private final Location end;

    public AStar(Location start, final Location end) {
        this.end = end;
        open = new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                int c = o1.getH().compareTo(o2.getH());
                if(c != 0){
                    return c;
                }
                return o1.getLocation().compareTo(o2.getLocation());
            }
        });

        open.add(new Node(start, null, end));
        closed = new TreeSet<Node>();
    }

    public List<Location> astar (){
        while(!open.isEmpty()){
            Node current = open.first();
            open.remove(current);

            if(current.getLocation().equals( end )){
                return current.findPath();
            }
            if(closed.contains(current)){
                continue;
            }
            open.addAll(current.findNeighbours());
            closed.add(current);
        }

        return Collections.EMPTY_LIST;
    }

    public static void main(String... args){

        Location start = new Location(0,0);
        Location end = new Location(5,5);

        long startNano = System.currentTimeMillis();
        AStar aStar = new AStar(start, end);
        List<Location> locations = aStar.astar();
        long endNano = System.currentTimeMillis();

        System.out.println("Total time: " + (endNano - startNano));

        for(Location location : locations){
            System.out.println(location);
        }
    }
}
