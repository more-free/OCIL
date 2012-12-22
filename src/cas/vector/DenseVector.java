package cas.vector;

import java.util.*;

/**
 * Dense Vector class represents common vectors 
 * @author kex, linyang
 *
 */
class DenseVector implements Vector<Double>{
	private ArrayList<Double> elements;
	
	DenseVector(){
		this.elements = new ArrayList<Double>();
	}
	
	// default size with default initialization to zeros 
	/**
	 * construct a DenseVector with default size and with default initialization to zeros
	 * @param size
	 */
	DenseVector(int size){
		this.elements = new ArrayList<Double>();
		for(int i = 0; i < size; i++)
			elements.add(0.0);
	}
	
	public void add(Double e){
		this.elements.add(e);
	}
	
	public Double get(int i){
		if(this.elements == null || i < 0 || i >= this.elements.size()){
			System.out.println("Index out of bound!");
			return null;
		}
		return this.elements.get(i);
	}
	/**
	 * set the idx dimension to the value
	 * @param idx
	 * @param value
	 */
	public void set(int idx, Double value){
		if(idx < 0 || idx >= this.elements.size()) return;
		this.elements.set(idx, value);
	}
	
	// set all elements to values of elements in vec. 
	public void setVector(DenseVector vec){
		if(vec.size() != this.size()){
			System.out.println("Length must match!");
			return;
		}
		
		for(int i = 0; i < size(); i++){
			Double newVal = vec.get(i); 
			this.set(i, newVal);  
		}
	}
	/**
	 * set the value in every dimension to zero
	 */
	public void setZeros(){
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, 0.0);
	}
	/**
	 * clear the DenseVector
	 */
	public void clear(){
		this.elements.clear();
	}
	
	/**
	 * dot product / scalar product with vec. the length of the two vectors must be the same.
	 * @param vec
	 * @return this .* vec
	 */
	public Double scalarProduct(DenseVector vec){
		if(vec == null || vec.size() != this.elements.size()){
			System.out.println("Vector length does not match!");
			return null;
		}
		
		int size = size();
		Double product = 0.0;
		for(int i = 0; i < size; i++)
			product += this.elements.get(i) * vec.get(i);
		
		return product;
	}
	
	// norm 2 / Euclidean norm / inner product
	/**
	 * inner product of the DenseVector
	 * @return
	 */
	public Double innerProduct(){
		if(this.elements == null)
			return null;
		if(size() == 0)
			return 0.0;
		
		Double product = 0.0;
		for(Double e : this.elements)
			product += e * e;
		return product;
	}
	
	// this.elements .+ vec
	/**
	 * add vec to the this DenseVector,the two DenseVector must be of the same size
	 * @param vec
	 */
	public void plusVector(DenseVector vec){
		if(vec == null || vec.size() != this.elements.size()){
			System.out.println("Vector length does not match!");
			return;
		}
		
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) + vec.get(i));
	}
	
	// this.elements .+ number
	/**
	 * add the number to every dimension of the DenseVector
	 * @param number
	 */
	public void plusNumber(Double number){
		if(this.elements == null)
			return;
		
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) + number);
	}
	
	// this.elements .- vec
	/**
	 * subtract the corresponding value in vec from the DenseVector, the two DenseVector must be of the same size
	 * @param vec
	 */
	public void minusVector(DenseVector vec){
		if(vec == null || vec.size() != this.elements.size()){
			System.out.println("Vector length does not match!");
			return;
		}
			
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) - vec.get(i));
	}
		
	// this.elements .- number
	/**
	 * Subtract number from every dimension in the DenseVector
	 * @param number
	 */
	public void minusNumber(Double number){
		if(this.elements == null)
			return;
		
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) - number);
	}
	
	// this.elements .* number
	/**
	 * every dimension in DenseVector is multiplied by number.
	 * @param number
	 */
	public void multiplyNumber(Double number){
		if(this.elements == null)
			return;
			
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) * number);
	}
	
	// this.elements ./ number
	/**
	 * every dimension in DenseVector is divided by number, the number should be non-zero
	 * @param number
	 */
	public void divideNumber(Double number){
		if(this.elements == null)
			return;
		if(Math.abs(number) < 0.000001){
			System.out.println("Divisor cannot be zero!");
			return;
		}
				
		int size = size();
		for(int i = 0; i < size; i++)
			this.elements.set(i, this.elements.get(i) / number);
	}
	/**
	 * print out the DenseVector
	 */
	public void print(){
		int size = size();
		for(int i = 0; i < size; i++)
			System.out.print(this.elements.get(i) + " ");
		System.out.println("");
	}
	
	// print first pSize elements
	public void print(int pSize){
		int len = Math.min(size(), pSize);
		for(int i = 0; i < len; i++)
			System.out.print(this.elements.get(i) + " ");
		System.out.println("");
	}

	@Override
	/**
	 * return the size of the DenseVector
	 */
	public int size(){
		return this.elements.size();
	}
	
	@Override
	/**
	 * implement the iterator for the DenseVector
	 */
	public Iterator<Double> iterator() {
		return new DenseVectorIterator(this);
	}
}


class DenseVectorIterator implements Iterator<Double>{
	private DenseVector vec;
	private int csize, tsize; 
	
	DenseVectorIterator(DenseVector vec){
		this.vec = vec;
		this.csize = 0;
		this.tsize = vec.size();
	}
	
	@Override
	public boolean hasNext() {
		return csize < tsize;
	}

	@Override
	public Double next() {
		return hasNext() ? vec.get(csize ++) : null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
}