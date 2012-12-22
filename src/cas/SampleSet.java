package cas;

import java.util.*;

// TODO  change the SampleIterator<E> as an inner class of SampleSet<E>
/**
 * Container to hold training / test examples 
 * @author kex
 *
 * @param <E>
 */
class SampleSet<E> implements Iterable<E>{
	private ArrayList<E> samples;

	SampleSet(ArrayList<E> samples){
		this.samples = samples;
	}
	
	public ArrayList<E> getSamples(){
		return this.samples;
	}
	
	public int size(){
		return this.samples.size();
	}
	
	@Override
	public Iterator<E> iterator() {
		return new SampleIterator<E>(this);
	}
}

class SampleIterator<E> implements Iterator<E>{
	private SampleSet<E> samples;
	private int tsize; // total size
	private int csize; // size of samples that have been iterated
	
	SampleIterator(SampleSet<E> samples){
		this.samples = samples;
		this.tsize = samples.size();
		this.csize = 0;
	}
	
	@Override
	public boolean hasNext() {
		return csize < tsize;
	}

	@Override
	public E next() {
		return hasNext() ? this.samples.getSamples().get(csize++) : null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
	}
	
}
