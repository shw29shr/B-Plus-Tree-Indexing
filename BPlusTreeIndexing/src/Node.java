import java.util.ArrayList;

public class Node<K extends Comparable<K>, T> {
    protected boolean isLeafNode;
    protected ArrayList<K> keys;
    protected IndexNode<K, T> parentNode;
    protected int indexInParent;
    public boolean isOverflowed() {
        return keys.size() > 2 * BPlusTree.D;
    }

    public boolean isUnderflowed() {
        return keys.size() < BPlusTree.D;
    }

    public IndexNode<K, T> getParent() {
        return this.parentNode;
    }
    public void setParent(IndexNode<K, T> parentNode) {
        this.parentNode=parentNode;
    }

    public void setIndexInParent() {
        for (int index = 0; index < parentNode.children.size(); index++) {
            if (parentNode.children.get(index).equals(this)) {
                this.indexInParent = index;
                break;
            }
        }
    }

    public int getIndexInParent() {
        return this.indexInParent;
    }
}

