Programming Assignment 5: K-d Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */

The Node data type is used to represent different nodes in the k-d tree. Each
node comes with a key, val, left, right, and boundingBox. The key is always a
Point2D object, while the val can be any Value. The left and right are just
links to the two possible subtrees a Node can have. Finally the boundingBox is
a RectHV representation of a given Node's k-d tree bounding box.

/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */

Our method for range search works by having 2 range methods, one being a public
method and the other being a private recursive helper method. The public method
checks for exceptions and then creates a queue of Point2D objects, and then
calls the recursive method with the root node, the queue, and the query
rectangle. The recursive method checks for an exception and if the current
node is null, in which the method would return, it then checks if the
current node's bounding box intersects the query, then check if the query
contains current's key and if so then add that key to the queue. After that we
call range of both the left and right subtrees. Once the recursive function is
done, the public method returns the queue.

/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */

Our method for nearest neighbor search in a k-d tree works by having 2 nearest
methods, with one being a recursive private helper method. The public method
checks for exceptions, and then begins the recursive calls by calling the
recursive method with the root as the starting node,the given p as the Point2D
search query, root as the closest node, and true as whether or not it is on an
xlevel. Once in the recursive function we then check if the current node is
null, if so we return our current closest node, we then check if the current
key is an exact match to the search query key, and if so we return the current
node, we then check if the current key is closer to p than the closest key,
and if so we update the closest to equal current. After that we check if we
are on an xlevel, and if so we compare the closest key's x value, and the
current key's x value, and if the closest is smaller than we check the
current's right node and see if the right's bounding box is closer than the
closest and if so we recursively call nearest with right. We then do the same
thing for the left. If the closest key's x value wasn't smaller than the
current key's x value, then we do the same thing, except looking at the right
node first and then the left. If we are on a ylevel we do the exact same
process except comparing the closest key's y value and the current key's y
value instead. At the end of the recursive function we return the closest node,
and in the public method, we return that node's key which is a Point 2D object.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, using one digit after the decimal point
 *  for each entry. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:            100                   8.4                11.9

KdTreeST:         1000000                 1.6              607,164.5

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

None.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

Prof. Han's office hours, Nina's office hours, and Darius's office hours.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

None.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

Yes, we followed the protocol.
Dillon Remuck: Worked on all the methods with Abigail and met up multiple
times a week to collaborate with her.
Abigail Wilson: Worked on all the methods with Dillon and made time to meet
and work on the assignment.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
