import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative TODO: Rename
 * to BPlusTree
 * 
 * @authors
 * Saarthak Chandra sc2776
 * Shweta Shrivastava ss3646
 * Vikas P Nelamangala vpn6
 * 
 * 
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K, T> root;
	public static final int D = 2;

	/**
	 * Get the LeafNode we expect the key to exist in .
	 *
	 * @param key
	 * @return The leaf node we expect
	 */
	private Node<K, T> reachLeafNode(K key) {
		int ctr = 0;
		Node<K, T> start = root;

		while (!start.isLeafNode) {
			ctr = 0;

			// Look through keys of the current Node
			for (K currentKey : start.keys) {

				if (currentKey.compareTo(key) > 0)
					break;
				else
					++ctr;
			}

			if (start.keys.size() > 0)
				start = ((IndexNode<K, T>) start).children.get(ctr);
		}
		return start;
	}

	/**
	 * Search the tree for a given key
	 *
	 * @param key
	 * @return value
	 */
	public T search(K key) {

		// First we travel down to the leaf Node we expect the leaf value in, it
		// can re root/ the leaf
		Node<K, T> current = reachLeafNode(key);

		int index = 0;
		// Iterate over all keys and search for the right one
		for (K currentKey : current.keys) {
			// currentKey == key
			if (currentKey.compareTo(key) == 0) // we found the key
				return ((LeafNode<K, T>) current).values.get(index);
			index++;
		}

		// We did not find the key
		return null;
	}

	Entry<K, Node<K, T>> newEntry = null;
	Node<K, T> current = root;

	/**
	 * main manipulation of tree happens here
	 * @param key
	 * @param value
	 */
	public void insertRecursive(K key, T value) {
		int flag = 0;
		if (!current.isLeafNode) {
			int index = 0;
			for (K currentKey : current.keys) {
				if (currentKey.compareTo(key) > 0)
					break;
				else
					index++;
			}
			current = ((IndexNode<K, T>) current).children.get(index);
			insertRecursive(key, value);
			if (newEntry == null)
				return;
			else {
				if ((current.keys.size()) < 2 * D) {
					// (current.keys).add(newChildEntry);
					((IndexNode<K, T>) current).insertSorted(newEntry, index);
					newEntry = null;
					return;
				} else {
					// Node<K,T> oldcurr = current;
					if (current.parentNode == null)
						flag = 1;
					((IndexNode<K, T>) current).insertSorted(newEntry, index);
					newEntry = splitIndexNode((IndexNode<K, T>) current);
					if (flag == 1) {
						ArrayList<K> newIndexNodeKeys = new ArrayList<K>();
						newIndexNodeKeys.add(newEntry.getKey());
						ArrayList<Node<K, T>> newIndexNodeChildren = new ArrayList<Node<K, T>>();
						newIndexNodeChildren.add(current);
						newIndexNodeChildren.add(newEntry.getValue());
						IndexNode<K, T> newroot = new IndexNode<K, T>(newIndexNodeKeys, newIndexNodeChildren);
						current.parentNode = newroot;
						(newEntry.getValue()).parentNode = newroot;
						newroot.parentNode = null;
						root = newroot;
						flag = 0;

					} else {
						(newEntry.getValue()).parentNode = current.parentNode;
					}
					current = ((IndexNode<K, T>) current.parentNode);
					return;
				}

			}
		}
		if (current.isLeafNode) {
			if ((current.keys.size()) < 2 * D) {
				((LeafNode<K, T>) current).insertSorted(key, value);
				newEntry = null;
				return;
			} else {
				if (current.parentNode == null)
					flag = 1;
				((LeafNode<K, T>) current).insertSorted(key, value);
				newEntry = splitLeafNode((LeafNode<K, T>) current);

				if (flag == 1) {
					ArrayList<K> newIndexNodeKeys = new ArrayList<K>();
					newIndexNodeKeys.add(newEntry.getKey());
					ArrayList<Node<K, T>> newIndexNodeChildren = new ArrayList<Node<K, T>>();
					newIndexNodeChildren.add(current);
					newIndexNodeChildren.add(newEntry.getValue());
					IndexNode<K, T> newroot = new IndexNode<K, T>(newIndexNodeKeys, newIndexNodeChildren);
					current.parentNode = newroot;
					(newEntry.getValue()).parentNode = newroot;
					newroot.parentNode = null;
					root = newroot;

					flag = 0;
				} else {
					(newEntry.getValue()).parentNode = current.parentNode;
				}
				current = ((IndexNode<K, T>) current.parentNode);
			}
		}
	}

	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 *
	 * @param key
	 * 			The key value we insert
	 * @param value
	 * 			The corresponding key value
	 */
	public void insert(K key, T value) {

		// If root is null, we create the root node
		if (root == null) {
			LeafNode<K, T> newNode = new LeafNode<K, T>(key, value);
			newNode.nextLeaf = null;
			newNode.previousLeaf = null;
			newNode.parentNode = null;
			root = newNode;
			return;
		}
		// else we create the new node,starting with root as the current node
		current = root;
		insertRecursive(key, value);

	}

	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 *
	 * @param leaf,
	 *            any other relevant data
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K, T>> splitLeafNode(LeafNode<K, T> leaf) {

		ArrayList<K> newLeafKeys = new ArrayList<K>();
		ArrayList<T> newLeafValues = new ArrayList<T>();

		int currentLeafSize = (leaf.keys).size();

		K splitKey = (leaf.keys).get(D);

		for (int i = D; i < currentLeafSize; i++) {
			newLeafKeys.add((leaf.keys).get(D));
			newLeafValues.add((leaf.values).get(D));
			(leaf.keys).remove(D);
			(leaf.values).remove(D);

		}

		LeafNode<K, T> newLeaf = new LeafNode<K, T>(newLeafKeys, newLeafValues);

		newLeaf.previousLeaf = leaf;
		newLeaf.nextLeaf = leaf.nextLeaf;
		leaf.nextLeaf = newLeaf;

		return new AbstractMap.SimpleEntry<K, Node<K, T>>(splitKey, newLeaf);
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 *
	 * @param index,
	 *            any other relevant data
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K, T>> splitIndexNode(IndexNode<K, T> index) {

		ArrayList<K> newIndexNodeKeys = new ArrayList<K>();
		ArrayList<Node<K, T>> newIndexNodeChildren = new ArrayList<Node<K, T>>();

		int currentIndexNodeKeysSize = (index.keys).size();

		K splitKey = (index.keys).get(D);

		for (int i = D; i < currentIndexNodeKeysSize; i++) {
			if (i != D)
				newIndexNodeKeys.add((index.keys).get(D));
			(index.keys).remove(D);
			newIndexNodeChildren.add((index.children).get(D + 1));
			(index.children).remove(D + 1);

		}

		IndexNode<K, T> newIndexNode = new IndexNode<K, T>(newIndexNodeKeys, newIndexNodeChildren);

		for (Node<K, T> child : newIndexNode.children) {
			child.parentNode = newIndexNode;

		}
		return new AbstractMap.SimpleEntry<K, Node<K, T>>(splitKey, newIndexNode);
	}


    /**
     * TODO Delete a key/value pair from this B+Tree
     *
     * @param key
     */
    public void delete(K key) {

        int splitOrNotIndex = -1;
        if (root != null) {
            splitOrNotIndex = lookToDelete(key, root, null, splitOrNotIndex);
        }


        // Handle root split.
        if (splitOrNotIndex != -1) {
            root.keys.remove(splitOrNotIndex);
            if (root.keys.size() == 0) {
                root = ((IndexNode<K, T>) root).children.get(0);
            }
        }

        if (root.keys.size() == 0) {
            root = null;
        }
    }

    /**
     * TODO Handle LeafNode Underflow (merge or redistribution)
     *
     * @param entry   : the Key
     * @param nodePointer  : the child node
     * @param parentPointer :  parent node
     * @param returnIndex : Return Index for each recursive call.
     * @return the splitkey position in parent if merged so that parent can
     * delete the splitkey later on. -1 otherwise
     */
    public int lookToDelete(K entry, Node<K, T> nodePointer, IndexNode<K, T> parentPointer, int returnIndex) {

        // Store info about parent here. Needed during recursive evaluation.
        if (parentPointer != null) {
            nodePointer.setParent(parentPointer);
            nodePointer.setIndexInParent();
        }

        // INDEX NODE CASE
        if (!nodePointer.isLeafNode) {
            // Cast L as an Index Node.
            IndexNode<K, T> currentNode = (IndexNode<K, T>) nodePointer;

            // Depending on key traverse left or right.
            if (entry.compareTo(currentNode.keys.get(0)) < 0) {
                returnIndex = lookToDelete(entry, currentNode.children.get(0), currentNode, returnIndex);
            } else if (entry.compareTo(currentNode.keys.get(currentNode.keys.size() - 1)) >= 0) {
                returnIndex = lookToDelete(entry, currentNode.children.get(currentNode.children.size() - 1), currentNode, returnIndex);
            } else {
                int a = 0;
                for (K items : currentNode.keys) {
                    a++;
                    if (items.compareTo(entry) > 0) {
                        returnIndex = lookToDelete(entry, currentNode.children.get(a - 1), currentNode, returnIndex);
                        break;
                    }
                }


            }
        }
        if (nodePointer.isLeafNode) {
            // LEAF NODE CASE
            // Cast L as a Leaf Node.
            LeafNode<K, T> currentNode = (LeafNode<K, T>) nodePointer;


            int ctr = 0;
            // To track index
            for (K items : currentNode.keys) {
                if (items.compareTo(entry) == 0) {
                    currentNode.values.remove(ctr);
                    currentNode.keys.remove(entry);
                    returnIndex = -1;
                    break;
                }
                ctr++;
            }

            // IF NODE IS UNDER FLOWED
            if (currentNode.isUnderflowed() && currentNode != root) {
                //Take Left Sibling if present
                // If getIndexInParent =1 or more it means there is some sibling to the left.
                if (currentNode.getIndexInParent() >= 1) {
                    return handleLeafNodeUnderflow((LeafNode<K, T>) currentNode.getParent().children.get(currentNode.getIndexInParent() - 1), currentNode, currentNode.getParent());
                }
                // Take right sibling otherwise
                else {
                    return handleLeafNodeUnderflow(currentNode, (LeafNode<K, T>) currentNode.getParent().children.get(currentNode.getIndexInParent() + 1), currentNode.getParent());
                }
            }

        }

        // Handle split key propogation scenario
        if (returnIndex != -1 && nodePointer != root) {
            returnIndex = splitKeyDelete(returnIndex, nodePointer);
        }
        return returnIndex;
    }

    /**
     *
     * @param returnIndex : the return Index
     * @param nodePointer : the child node
     * @return the splitkey position
     */
    private int splitKeyDelete(int returnIndex, Node<K, T> nodePointer) {
        nodePointer.keys.remove(returnIndex);
        if (nodePointer.isUnderflowed()) {
            if (nodePointer.getIndexInParent() >= 1) {
                return handleIndexNodeUnderflow((IndexNode<K, T>) nodePointer.getParent().children.get(nodePointer.getIndexInParent() - 1), (IndexNode<K, T>) nodePointer, nodePointer.getParent());
            } else {
                // Does not have left sibling, so try right sibling

                return handleIndexNodeUnderflow((IndexNode<K, T>) nodePointer, (IndexNode<K, T>) nodePointer.getParent().children.get(nodePointer.getIndexInParent() + 1), nodePointer.getParent());
            }
        }
        return -1;
    }

//    public int findPositionOfANodeInItsParent(Node<K, T> currentNode, IndexNode<K, T> parentNode) {
//        // Gives Position Of A Node w.r.t Parent
//        int position = 0;
//        for (int index = 0; index < parentNode.children.size(); index++) {
//            if (parentNode.children.get(index).equals(currentNode)) {
//                position = index;
//                break;
//            }
//        }
//        return position;
//    }

    /**
     * TODO Handle LeafNode Underflow (merge or redistribution)
     *
     * @param left   : the left node
     * @param right  : the right node
     * @param parent :  parent node reference
     * @return the splitkey position in parent if merged so that parent can
     * delete the splitkey later on. -1 otherwise
     */
    public int handleLeafNodeUnderflow(LeafNode<K, T> left, LeafNode<K, T> right, IndexNode<K, T> parent) {
        if (left == null || right == null) return -1;

        int leftSideSize = left.keys.size();
        int rightSideSize = right.keys.size();

        if ((leftSideSize + rightSideSize) >= (2 * D)) {
            int rightNodeIndex = parent.children.indexOf(right);

            // Store all keys and  values of all nodes
            List<K> allKeys = new ArrayList<K>();
            allKeys.addAll(left.keys);
            allKeys.addAll(right.keys);
            List<T> allValues = new ArrayList<T>();
            allValues.addAll(left.values);
            allValues.addAll(right.values);

            int newLeftSize = (left.keys.size() + right.keys.size()) / 2;

            // Clear all keys and values from left and right nodes
            left.keys.clear();
            right.keys.clear();
            left.values.clear();
            right.values.clear();

            left.keys.addAll(allKeys.subList(0, newLeftSize));
            left.values.addAll(allValues.subList(0, newLeftSize));
            right.keys.addAll(allKeys.subList(newLeftSize, allKeys.size()));
            right.values.addAll(allValues.subList(newLeftSize, allValues.size()));

            parent.keys.set(rightNodeIndex - 1, parent.children.get(rightNodeIndex).keys.get(0));
            return -1;
        } else {
            left.keys.addAll(right.keys);
            left.values.addAll(right.values);
            left.nextLeaf = right.nextLeaf;
            if (right.nextLeaf != null) {
                right.nextLeaf.previousLeaf = left;
            }
            int index = parent.children.indexOf(right) - 1;
            parent.children.remove(right);
            return index;
        }
    }


    /**
     *
     * @param left   : the smaller node
     * @param right  : the bigger node
     * @param parent : their parent index node
     * @return the splitkey position
     */
    public int handleIndexNodeUnderflow(IndexNode<K, T> left, IndexNode<K, T> right, IndexNode<K, T> parent) {
        //if (left == null || right == null) return -1;
        int splitIndex = 0;

        for (int i = 0; i < parent.keys.size(); i++) {
            if (parent.children.get(i) == left && parent.children.get(i + 1) == right) {
                splitIndex = i;
            }
        }
        int leftSideSize = left.keys.size();
        int rightSideSize = right.keys.size();

        if ((leftSideSize + rightSideSize) < 2 * D) {
            // Merge
            left.keys.add(parent.keys.get(splitIndex));
            left.keys.addAll(right.keys);
            left.children.addAll(right.children);
            parent.children.remove(parent.children.indexOf(right));
            return splitIndex;
        } else {
            // Re Distribute
            List<K> KeysPool = new ArrayList<K>();
            KeysPool.addAll(left.keys);
            KeysPool.add(parent.keys.get(splitIndex));
            KeysPool.addAll(right.keys);

            // Store all sub tree nodes
            List<Node<K, T>> subTree = new ArrayList<Node<K, T>>();
            subTree.addAll(left.children);
            subTree.addAll(right.children);

            // Get the middle index of KEYS POOL
            int newParentIndex = 0;
            if (KeysPool.size() % 2 == 0) {
                newParentIndex = (KeysPool.size() / 2) - 1;
            } else {
                newParentIndex = KeysPool.size() / 2;
            }

            // Add the new parent key to the splitting index.


            // Re distribute Key Partitions
            left.keys.clear();
            left.keys.addAll(KeysPool.subList(0, newParentIndex));
            parent.keys.set(splitIndex, KeysPool.get(newParentIndex));
            right.keys.clear();
            right.keys.addAll(KeysPool.subList(newParentIndex + 1, KeysPool.size()));

            //Re Group Child Nodes.
            left.children.clear();
            left.children.addAll(subTree.subList(0, newParentIndex + 1));
            right.children.clear();
            right.children.addAll(subTree.subList(newParentIndex + 1, subTree.size()));

            return -1;
        }

    }
}