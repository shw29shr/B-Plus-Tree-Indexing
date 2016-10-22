import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

public class Tests {

	// add some nodes, see if it comes out right, delete one, see if it's right
	@Test
	public void testSimpleHybrid() {
		System.out.println("\n testSimpleHybrid");
		Character alphabet[] = new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
		String alphabetStrings[] = new String[alphabet.length];
		for (int i = 0; i < alphabet.length; i++) {
			alphabetStrings[i] = (alphabet[i]).toString();
		}
		BPlusTree<Character, String> tree = new BPlusTree<Character, String>();
		Utils.bulkInsert(tree, alphabet, alphabetStrings);

		String test = Utils.outputTree(tree);
		String correct = "@c/e/@%%[(a,a);(b,b);]#[(c,c);(d,d);]#[(e,e);(f,f);(g,g);]$%%";

		assertEquals(correct, test);

		//tree.delete('a');

		//test = Utils.outputTree(tree);
		//correct = "@e/@%%[(b,b);(c,c);(d,d);]#[(e,e);(f,f);(g,g);]$%%";
		//assertEquals(correct, test);

	}

	@Ignore
	@Test
	public void testSplitLeafLogic(){
		ArrayList<Integer> primeNumbers = new ArrayList<Integer>();
		primeNumbers.add(2);
		primeNumbers.add(3);
		primeNumbers.add(5);
		primeNumbers.add(7);
		primeNumbers.add(11);
		ArrayList<String> primeStrings = new ArrayList<String>();
		primeStrings.add("two");
		primeStrings.add("three");
		primeStrings.add("five");
		primeStrings.add("seven");
		primeStrings.add("eleven");
		
		LeafNode<Integer, String> leaf = new LeafNode<Integer, String>(primeNumbers, primeStrings);
		//int i=0;
//		for(i=0;i<leaf.keys.size();i++){
//			System.out.println((leaf.keys).get(i));
//			
//		}
		for(int lx : leaf.keys){
			System.out.println(lx);
		}
		for(String lx : leaf.values){
			System.out.println(lx);
		}
		System.out.println("--------------------");
		List<Integer> primeN = new ArrayList<Integer>();
		List<String> primeS = new ArrayList<String>();
		int D=2;
		int s = leaf.keys.size();
		for(int i=D;i<s;i++){
			primeN.add((leaf.keys).get(D));
			primeS.add((leaf.values).get(D));
    		(leaf.keys).remove(D);
    		(leaf.values).remove(D);
    		
    	}
		
		for(int lx : leaf.keys){
			System.out.println(lx);
		}
		for(String lx : leaf.values){
			System.out.println(lx);
		}
		System.out.println("--------------------");
		for(int lx : primeN){
			System.out.println(lx);
		}
		for(String lx : primeS){
			System.out.println(lx);
		}
		
	}
	
	@Ignore
	@Test
	public void testSplitIndexLogic(){
		List<Integer> indexKeys = new ArrayList<Integer>();
		indexKeys.add(2);
		indexKeys.add(3);
		indexKeys.add(5);
		indexKeys.add(7);
		//indexKeys.add(11);
		
		
		List<Node<Integer,String>> indexChildren = new ArrayList<Node<Integer,String>>();

		
		ArrayList<Integer> p1 = new ArrayList<Integer>();
		p1.add(2);
		p1.add(3);
		p1.add(5);
		p1.add(7);
		p1.add(11);
		ArrayList<String> s1 = new ArrayList<String>();
		s1.add("two");
		s1.add("three");
		s1.add("five");
		s1.add("seven");
		s1.add("eleven");
		
		LeafNode<Integer, String> leaf1 = new LeafNode<Integer, String>(p1, s1);
		
		ArrayList<Integer> p2 = new ArrayList<Integer>();
		p2.add(2);
		p2.add(3);
		p2.add(5);
		p2.add(7);
		p2.add(11);
		ArrayList<String> s2 = new ArrayList<String>();
		s2.add("two");
		s2.add("three");
		s2.add("five");
		s2.add("seven");
		s2.add("eleven");
		
		LeafNode<Integer, String> leaf2 = new LeafNode<Integer, String>(p2, s2);
		
		ArrayList<Integer> p3 = new ArrayList<Integer>();
		p3.add(2);
		p3.add(3);
		p3.add(5);
		p3.add(7);
		p3.add(11);
		ArrayList<String> s3 = new ArrayList<String>();
		s3.add("two");
		s3.add("three");
		s3.add("five");
		s3.add("seven");
		s3.add("eleven");
		
		LeafNode<Integer, String> leaf3 = new LeafNode<Integer, String>(p3, s3);
		
		ArrayList<Integer> p4 = new ArrayList<Integer>();
		p4.add(2);
		p4.add(3);
		p4.add(5);
		p4.add(7);
		p4.add(11);
		ArrayList<String> s4 = new ArrayList<String>();
		s4.add("two");
		s4.add("three");
		s4.add("five");
		s4.add("seven");
		s4.add("eleven");
		
		LeafNode<Integer, String> leaf4 = new LeafNode<Integer, String>(p4, s4);
		
		ArrayList<Integer> p5 = new ArrayList<Integer>();
		p5.add(2);
		p5.add(3);
		p5.add(5);
		p5.add(7);
		p5.add(11);
		ArrayList<String> s5 = new ArrayList<String>();
		s5.add("two");
		s5.add("three");
		s5.add("five");
		s5.add("seven");
		s5.add("eleven");
		
		LeafNode<Integer, String> leaf5 = new LeafNode<Integer, String>(p5, s5);

		indexChildren.add(leaf1);
		indexChildren.add(leaf2);
		indexChildren.add(leaf3);
		indexChildren.add(leaf4);
		indexChildren.add(leaf5);
		
		//ArrayList<Integer> tkeys = new ArrayList<Integer>(indexKeys);
		//ArrayList<Node<Integer,String>> tchildren = new ArrayList<Node<Integer,>>(newChildren);

		//Node<Integer, String> node = new Node<Integer, String>();
		IndexNode<Integer, Node<Integer, String>> index = null ;
		
		
	}
	@Test
	public void testInsert(){
		Integer primeNumbers[] = new Integer[] { 2, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 3, 8, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
		String primeNumberStrings[] = new String[primeNumbers.length];
		for (int i = 0; i < primeNumbers.length; i++) {
			primeNumberStrings[i] = (primeNumbers[i]).toString();
		}
		BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
		Utils.bulkInsert(tree, primeNumbers, primeNumberStrings);
		Utils.printTree(tree);
	}
	// add some nodes, see if it comes out right, delete one, see if it's right
	@Test
	public void testSimpleHybrid2() {
		Integer primeNumbers[] = new Integer[] { 2, 4, 5, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16 };
		String primeNumberStrings[] = new String[primeNumbers.length];
		for (int i = 0; i < primeNumbers.length; i++) {
			primeNumberStrings[i] = (primeNumbers[i]).toString();
		}
		BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
		Utils.bulkInsert(tree, primeNumbers, primeNumberStrings);

		String test = Utils.outputTree(tree);
		String correct = "@10/@%%@5/8/@@12/14/@%%[(2,2);(4,4);]#[(5,5);(7,7);]#[(8,8);(9,9);]$[(10,10);(11,11);]#[(12,12);(13,13);]#[(14,14);(15,15);(16,16);]$%%";
		assertEquals(test, correct);

	}

	@Test
	public void testBookExampleShort() {
		Integer exampleNumbers[] = new Integer[] { 2, 3, 13, 14, 17, 19, 24, 27,
				30, 33, 34, 38, 5, 7, 16, 20, 22, 29 };
		String primeNumberStrings[] = new String[exampleNumbers.length];
		for (int i = 0; i < exampleNumbers.length; i++) {
			primeNumberStrings[i] = (exampleNumbers[i]).toString();
		}
		BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
		Utils.bulkInsert(tree, exampleNumbers, primeNumberStrings);
		Utils.printTree(tree);
		tree.delete(13);
		tree.delete(17);
		tree.delete(30);
		tree.insert(39, "39");
		Utils.printTree(tree);
		// Initial tree
		String test = Utils.outputTree(tree);
		String correct = "@13/17/24/30/@%%[(2,2);(3,3);(5,5);(7,7);]#[(14,14);(16,16);]#[(19,19);(20,20);(22,22);]#[(24,24);(27,27);(29,29);]#[(33,33);(34,34);(38,38);(39,39);]$%%";
		assertEquals(test, correct);
	}

	// testing proper leaf node merging behaviour
	@Test
	public void testDeleteLeafNodeRedistribute() {
		Integer testNumbers[] = new Integer[] { 2, 4, 7, 8, 5, 6, 3 };
		String testNumberStrings[] = new String[testNumbers.length];
		for (int i = 0; i < testNumbers.length; i++) {
			testNumberStrings[i] = (testNumbers[i]).toString();
		}
		BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
		Utils.bulkInsert(tree, testNumbers, testNumberStrings);

		tree.delete(6);
		tree.delete(7);
		tree.delete(8);
		String test = Utils.outputTree(tree);
		Utils.printTree(tree);

		String result = "@4/@%%[(2,2);(3,3);]#[(4,4);(5,5);]$%%";
		assertEquals(result, test);
	}

	// Testing appropriate depth and node invariants on a big tree
	@Test
	public void testLargeTree() {
		BPlusTree<Integer, Integer> tree = new BPlusTree<Integer, Integer>();
		ArrayList<Integer> numbers = new ArrayList<Integer>(100000);
		for (int i = 0; i < 100000; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		for (int i = 0; i < 100000; i++) {
			tree.insert(numbers.get(i), numbers.get(i));
		}
		testTreeInvariants(tree);

		assertTrue(treeDepth(tree.root) < 11);
	}

	public <K extends Comparable<K>, T> void testTreeInvariants(
			BPlusTree<K, T> tree) {
		for (Node<K, T> child : ((IndexNode<K, T>) (tree.root)).children)
			testNodeInvariants(child);
	}

	public <K extends Comparable<K>, T> void testNodeInvariants(Node<K, T> node) {
		assertFalse(node.keys.size() > 2 * BPlusTree.D);
		assertFalse(node.keys.size() < BPlusTree.D);
		if (!(node.isLeafNode))
			for (Node<K, T> child : ((IndexNode<K, T>) node).children)
				testNodeInvariants(child);
	}

	public <K extends Comparable<K>, T> int treeDepth(Node<K, T> node) {
		if (node.isLeafNode)
			return 1;
		int childDepth = 0;
		int maxDepth = 0;
		for (Node<K, T> child : ((IndexNode<K, T>) node).children) {
			childDepth = treeDepth(child);
			if (childDepth > maxDepth)
				maxDepth = childDepth;
		}
		return (1 + maxDepth);
	}
}
