package cas.vector;

import java.util.*;

/**
 * Sparse vector represents vectors that contain a large number of '0' elements.
 * @author kex, linyang
 * @version 0.2 
 *  v0.2 change log (last updated 2012/11/18): 
 *  	1. add 'length' member and change previous size() to numOfElements().
 *  new size() returns the total length represented by the vector. (not the number of element
 *  . e.g. a vector [1,0,0,0,....] has length 10000. but has only one element stored in the
 *  sparseVector).
 *  	2. change the inner structure to HashMap.
 *  	3. change the iterator class to internal class
 *  
 *    TODO support any number besides the default '0';
 *  	   support other 'E' for <Integer, E> besides 'Double' to save space;
 *  	   add necessary vector operation and computation;
 */

// TODO  should rename it. because it is not like the original sparseVector class. this class mainly
// 		 focus on search / find operation rather than regular matrix operations.
class SparseVector implements Vector<Pair<Integer, Double>>{
	private int length; 
	private Map<Integer, Double> elements;
	
	SparseVector(){
		this.length = Integer.MAX_VALUE;
		this.elements = new HashMap<Integer, Double>();
	}
	
	SparseVector(int length){
		this.length = length;
		this.elements = new HashMap<Integer, Double>();
	}
	
	public int size(){
		return this.length;
	}
	
	
	/**
	 *  get the idx-th element in the sparseVector
	 *  @return the value of idx-th element in sparseVector, or null if it does not exist
	 */
	public Double get(int idx){
		if(idx < 0 || idx >= length){
			System.out.println("Index out of bound in Sparse Vector");
			return null;
		}
		
		return this.elements.get(idx);  
	}
	
	/**
	 *  set the idx-th element in the sparseVector to val
	 */
	public void set(int idx, double val){
		if(idx < 0 || idx >= length){
			System.out.println("Index out of bound in Sparse Vector");
			return;
		}
		
		elements.put(idx, val);
	}
		
	public void print(){
		
	}

	@Override
	public Iterator<Pair<Integer, Double>> iterator() {
		return new SparseVectorIterator();
	}
	
	/**
	 * iterator for sparseVector, providing ordered iteration.
	 * O(nlog(n)) running time.
	 */
	class SparseVectorIterator implements Iterator<Pair<Integer, Double>>{
		private Iterator iter;
		private ArrayList<Pair<Integer, Double>> pairs;
		private int csize, tsize;  // current size / total size
		
		SparseVectorIterator(){
			iter = elements.entrySet().iterator();
			
			pairs = new ArrayList<Pair<Integer, Double>>();
			while(iter.hasNext()){
				Map.Entry<Integer, Double> item = 
							(Map.Entry<Integer, Double>)(iter.next());
				pairs.add(new Pair<Integer, Double>(item.getKey(), item.getValue()));
			}
			
			// rank all pairs by increasing order with respect to Integer
			class PairComparator implements Comparator<Pair<Integer, Double>>{
				@Override
				public int compare(Pair<Integer, Double> pair1, Pair<Integer, Double> pair2) {
					return pair1.getKey().compareTo(pair2.getKey()); 
				}
			}
			Collections.sort(pairs, new PairComparator());
			
			csize = 0;
			tsize = pairs.size();
		}
		
		@Override
		public boolean hasNext() {
			return csize < tsize;
		}

		@Override
		public Pair<Integer, Double> next() {
			return hasNext() ? pairs.get(csize++) : null;
		}

		@Override
		public void remove() {
			if(csize < tsize){
				pairs.remove(csize);
				tsize--;
			}
		}
	}
}


