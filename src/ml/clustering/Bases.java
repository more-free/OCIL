package ml.clustering;

import java.util.*;

interface Clusterable{
	void clustering();
}

abstract class ClusterElement{  
	
}

abstract class Cluster implements Iterable<ClusterElement>{
	private ArrayList<ClusterElement> elements;
	
	@Override
	public Iterator<ClusterElement> iterator() {
		return new ClusterElementIterator();
	}
	
	private class ClusterElementIterator implements Iterator<ClusterElement>{
		private int tsize, csize; // total size , current size
		ClusterElementIterator(){
			tsize = elements.size();
			csize = 0;
		}

		@Override
		public boolean hasNext() {
			return csize < tsize;
		}

		@Override
		public ClusterElement next() {
			return hasNext() ? elements.get(csize++) : null;
		}

		@Override
		public void remove() {
			
		}
		
	}
}