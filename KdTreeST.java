import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class KdTreeST<Value> {

    private class Node {
        // creates a Point2D key
        private Point2D key;
        // creates a val
        private Value val;
        // creates a link to a left node
        private Node left;
        // creates a link to a right node
        private Node right;
        // create a RectHV boundingBox
        private RectHV boundingBox;

        // constructor method to initialize key, val, and boundingBox
        public Node(Point2D key, Value val, RectHV boundingBox) {
            this.key = key;
            this.val = val;
            this.boundingBox = boundingBox;
        }
    }


    // creates root node
    private Node root;

    // creates size variable
    private int size;

    // construct an empty symbol table of points
    public KdTreeST() {
        root = null;
        size = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return root == null;
    }

    // returns size of k-d tree
    public int size() {
        return size;
    }

    // adds a point to k-d tree
    public void put(Point2D key, Value val) {
        if (key == null || val == null) {
            throw new IllegalArgumentException();
        }
        else {
            RectHV rootBoundingBox = new RectHV(Integer.MIN_VALUE,
                                                Integer.MIN_VALUE,
                                                Integer.MAX_VALUE,
                                                Integer.MAX_VALUE);
            root = put(root, key, val, true, rootBoundingBox);
            size = size + 1;
        }
    }

    // recursive helper function to add a point to k-d tree
    private Node put(Node curr, Point2D pointtoAdd, Value val, boolean xlevel,
                     RectHV boundingBox) {
        if (curr == null) return new Node(pointtoAdd, val, boundingBox);
        if (xlevel) {
            int cmp = Double.compare(pointtoAdd.x(), curr.key.x());
            if (cmp > 0) {
                RectHV box2 = new RectHV(curr.key.x(), boundingBox.ymin(),
                                         boundingBox.xmax(),
                                         boundingBox.ymax());
                curr.right = put(curr.right, pointtoAdd, val, false, box2);
            }
            else if (cmp == 0) {
                int ycmp = Double.compare(pointtoAdd.y(), curr.key.y());
                if (ycmp == 0) {
                    size = size - 1;
                    curr.val = val;
                }
                else {
                    RectHV box2 = new RectHV(curr.key.x(),
                                             boundingBox.ymin(),
                                             boundingBox.xmax(),
                                             boundingBox.ymax());
                    curr.right = put(curr.right, pointtoAdd, val, false, box2);
                }
            }
            else {
                RectHV box2 = new RectHV(boundingBox.xmin(),
                                         boundingBox.ymin(),
                                         curr.key.x(),
                                         boundingBox.ymax());
                curr.left = put(curr.left, pointtoAdd, val, false, box2);
            }
        }
        else {
            int cmp2 = Double.compare(pointtoAdd.y(), curr.key.y());
            if (cmp2 > 0) {
                RectHV box2 = new RectHV(boundingBox.xmin(), curr.key.y(),
                                         boundingBox.xmax(),
                                         boundingBox.ymax());
                curr.right = put(curr.right, pointtoAdd, val, true, box2);
            }
            else if (cmp2 == 0) {
                int xcmp = Double.compare(pointtoAdd.x(), curr.key.x());
                if (xcmp == 0) {
                    size = size - 1;
                    curr.val = val;
                }
                else {
                    RectHV box2 = new RectHV(boundingBox.xmin(),
                                             curr.key.y(),
                                             boundingBox.xmax(),
                                             boundingBox.ymax());
                    curr.right = put(curr.right, pointtoAdd, val, true, box2);
                }
            }
            else {
                RectHV box2 = new RectHV(boundingBox.xmin(), boundingBox.ymin(),
                                         boundingBox.xmax(),
                                         curr.key.y());
                curr.left = put(curr.left, pointtoAdd, val, true, box2);
            }
        }
        return curr;
    }

    // returns the value associated with key
    public Value get(Point2D key) {
        return get(root, key, true);
    }

    // helper method to return val associated with key
    private Value get(Node curr, Point2D pointtoAdd, boolean xlevel) {
        if (pointtoAdd == null) throw new
                IllegalArgumentException("calls get() with a null key");
        if (curr == null) return null;
        if (xlevel) {
            int cmp = Double.compare(pointtoAdd.x(), curr.key.x());
            if (cmp < 0) return get(curr.left, pointtoAdd, false);
            else if (cmp > 0) return get(curr.right, pointtoAdd, false);
            else {
                if (Double.compare(pointtoAdd.y(), curr.key.y()) == 0) {
                    return curr.val;
                }
                else {
                    return get(curr.right, pointtoAdd, false);
                }
            }
        }
        else {
            int cmp = Double.compare(pointtoAdd.y(), curr.key.y());
            if (cmp < 0) return get(curr.left, pointtoAdd, true);
            else if (cmp > 0) return get(curr.right, pointtoAdd, true);
            else {
                if (Double.compare(pointtoAdd.x(), curr.key.x()) == 0) {
                    return curr.val;
                }
                else {
                    return get(curr.right, pointtoAdd, false);
                }
            }

        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else {
            return get(p) != null;
        }
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> keys = new Queue<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        else {
            Queue<Point2D> queue = new Queue<Point2D>();
            range(root, queue, rect);
            return queue;
        }
    }

    // helper method to find all points inside the rectangle,
    // or on the boundary
    private void range(Node curr, Queue<Point2D> queue,
                       RectHV query) {
        if (query == null) {
            throw new IllegalArgumentException();
        }
        if (curr == null) {
            return;
        }
        if (query.intersects(curr.boundingBox)) {
            if (query.contains(curr.key)) {
                queue.enqueue(curr.key);
            }
            range(curr.left, queue, query);
            range(curr.right, queue, query);
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.isEmpty()) {
            return null;
        }
        else {
            return nearest(root, p, root, true).key;
        }
    }

    // helper method to find nearest neighbor
    private Node nearest(Node curr, Point2D p, Node closest, boolean xlevel) {
        if (curr == null) {
            return closest;
        }
        if (curr.key.equals(p)) {
            return curr;
        }
        if (curr.key.distanceSquaredTo(p) < closest.key.distanceSquaredTo(p)) {
            closest = curr;
        }
        if (xlevel) {
            int cmp = Double.compare(closest.key.x(), curr.key.x());
            if (cmp < 0) {
                if (curr.right != null) {
                    if (curr.right.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.right, p, closest, false);
                    }
                }
                if (curr.left != null) {
                    if (curr.left.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.left, p, closest, false);
                    }
                }
            }
            else {
                if (curr.left != null) {
                    if (curr.left.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.left, p, closest, false);
                    }
                }
                if (curr.right != null) {
                    if (curr.right.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.right, p, closest, false);
                    }
                }
            }
        }
        else {
            int cmp = Double.compare(closest.key.y(), curr.key.y());
            if (cmp < 0) {
                if (curr.right != null) {
                    if (curr.right.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.right, p, closest, true);
                    }
                }
                if (curr.left != null) {
                    if (curr.left.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.left, p, closest, true);
                    }
                }
            }
            else {
                if (curr.left != null) {
                    if (curr.left.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.left, p, closest, true);
                    }
                }
                if (curr.right != null) {
                    if (curr.right.boundingBox.distanceSquaredTo(p) <
                            closest.key.distanceSquaredTo(p)) {
                        closest = nearest(curr.right, p, closest, true);
                    }
                }
            }
        }
        return closest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.put(p, i);
        }
        Point2D testPoint = new Point2D(0.1, 0.2);
        RectHV testRect = new RectHV(0.15, 0.05, 0.25, 0.25);
        StdOut.println(kdtree.size());
        StdOut.println(kdtree.points());
        StdOut.println(kdtree.get(testPoint));
        StdOut.println(kdtree.contains(testPoint));
        StdOut.println(kdtree.range(testRect));
        StdOut.println(kdtree.nearest(testPoint));
        // start stopwatch
        StopwatchCPU stopwatch = new StopwatchCPU();
        for (int i = 0; i < 1000000; i++) {
            double randomy = StdRandom.uniform();
            double randomx = StdRandom.uniform();
            Point2D query = new Point2D(randomx, randomy);
            kdtree.nearest(query);
        }
        // stop stopwatch
        double time = stopwatch.elapsedTime();
        StdOut.println(time);

        // 10 trials of 1,000,000
        // 1.65
        // 2.27
        // 1.93
        // 1.58
        // 1.49
        // 1.52
        // 1.61
        // 1.58
        // 1.35
        // 1.49
        // Average: 1.647

    }
}
