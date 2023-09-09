import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class PointST<Value> {

    // creates new RedBlackBST using Point2Ds and values
    private RedBlackBST<Point2D, Value> symbolTable;

    // construct an empty symbol table of points
    public PointST() {
        symbolTable = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return symbolTable.isEmpty();
    }

    // number of points
    public int size() {
        return symbolTable.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new IllegalArgumentException();
        }
        else {
            symbolTable.put(p, val);
        }
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else {
            return symbolTable.get(p);
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else {
            return symbolTable.contains(p);
        }
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return symbolTable.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        else {
            Queue<Point2D> queue = new Queue<Point2D>();
            for (Point2D i : symbolTable.keys()) {
                if (i.x() >= rect.xmin() && i.x() <= rect.xmax() && i.y() >=
                        rect.ymin() && i.y() <= rect.ymax()) {
                    queue.enqueue(i);
                }
            }
            return queue;
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else if (this.isEmpty()) {
            return null;
        }
        else {
            Point2D closest = null;
            double closestDistance = 0.0;
            int n = 0;
            for (Point2D i : symbolTable.keys()) {
                if (n == 0) {
                    closestDistance = i.distanceSquaredTo(p);
                    closest = i;
                    n += 1;
                }
                else {
                    if (i.distanceSquaredTo(p) < closestDistance) {
                        closest = i;
                        closestDistance = i.distanceSquaredTo(p);
                    }
                }
            }
            return closest;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointST<Integer> test = new PointST<Integer>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            test.put(p, i);
        }
        Point2D testPoint = new Point2D(0.1, 0.2);
        RectHV testRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdOut.println(test.size());
        StdOut.println(test.points());
        StdOut.println(test.get(testPoint));
        StdOut.println(test.contains(testPoint));
        StdOut.println(test.range(testRect));
        StdOut.println(test.nearest(testPoint));
        // start stopwatch
        StopwatchCPU stopwatch = new StopwatchCPU();
        for (int i = 0; i < 100; i++) {
            double randomy = StdRandom.uniform();
            double randomx = StdRandom.uniform();
            Point2D query = new Point2D(randomx, randomy);
            test.nearest(query);
        }
        // stop stopwatch
        double time = stopwatch.elapsedTime();
        StdOut.println(time);

        // 10 trials of 100
        // 8.263829
        // 7.886315
        // 9.540566
        // 7.009215
        // 7.643303
        // 8.456275
        // 7.963763
        // 9.336735
        // 9.162956
        // 8.899789
        // Average: 8.416


    }

}
