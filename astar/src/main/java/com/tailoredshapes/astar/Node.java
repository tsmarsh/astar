package com.tailoredshapes.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node>{
    private Location location;
    private Node parent;
    private Location end;
    private Double h;

    public Node(Location location, Node parent, Location end) {
        this.location = location;
        this.parent = parent;
        this.end = end;
        this.h = heuristic();
    }

    public Location getLocation() {
        return location;
    }

    public Node getParent() {
        return parent;
    }

    public Double getH() {
        return h;
    }

    private Double heuristic(){
        int dx = location.getX() - end.getX();
        int dy = location.getY() - end.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public List<Location> findPath() {
        LinkedList<Location> locations = new LinkedList<Location>();

        Node node = this;
        do{
            locations.addFirst(node.getLocation());
            node = node.getParent();
        } while( node != null);

        return locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (h != null ? !h.equals(node.h) : node.h != null) return false;
        if (location != null ? !location.equals(node.location) : node.location != null) return false;
        if (parent != null ? !parent.equals(node.parent) : node.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    public Collection<Node> findNeighbours() {
        final Node self = this;
        final int x = getLocation().getX();
        final int y = getLocation().getY();

        return new ArrayList<Node>(){{
            add(new Node(new Location(x + 1, y), self, end));
            add(new Node(new Location(x - 1, y), self, end));
            add(new Node(new Location(x, y + 1), self, end));
            add(new Node(new Location(x, y - 1), self, end));
        }};
    }

    @Override
    public int compareTo(Node o) {
        return getLocation().compareTo(o.getLocation());
    }
}
