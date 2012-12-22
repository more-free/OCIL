package cas.vector;

import java.util.*;


/**
 * This class integrates common vector operations. 
 * Unlike operations encapsulated in Vector class itself (and its derived class), which are
 * applied to vectors themselves and force them to change,  operations in this class DO NOT
 * change the parameter vectors themselves. Instead, they either return a new vector as results
 * or require a out-parameter vector.
 * 
 * @author kex, linyang
 *
 */
class VectorOperator{
	//TODO  refactor with common base Vector 
	//scalar product for one sparse vector
	/**
	 * a constant defined to be equal to zero.
	 */
	private static final double ROUND_TO_ZERO = 0.0000001;
	/**
	 * if vectors are valid, return the scalar products of two vectors, otherwise return null
	 * 
	 * @param vec1 belongs to class Vector or the subclasses of Vector
	 * @param vec2 belongs to class Vector or the subclasses of Vector
	 * @return Double or null
	 */
	public static Double scalarProduct(Vector vec1, Vector vec2)
	{
		if(vec1 == null || vec2 == null) return null;
		Double pro = 0.0;
		if(vec1 instanceof SparseVector && vec2 instanceof SparseVector)
		{
			return scalarProduct((SparseVector) vec1, (SparseVector) vec2);
		}
		else if(vec1 instanceof SparseVector && vec2 instanceof DenseVector)
		{
			return scalarProduct((SparseVector) vec1, (DenseVector) vec2);
		}
		else if(vec1 instanceof DenseVector && vec2 instanceof SparseVector)
		{
			return scalarProduct((DenseVector) vec1, (SparseVector) vec2);
		}
		else
		{
			return scalarProduct((DenseVector) vec1, (DenseVector) vec2);
		}
	}
	/**
	 * return the scalar product of a DenseVector and a SparseVector
	 * 
	 * @param dVec a DenseVector
	 * @param sVec a SparseVector
	 * @return Double
	 */
	private static Double scalarProduct(DenseVector dVec, SparseVector sVec){
		double pro = 0.0;
		
		Iterator<Pair<Integer, Double>> siter = sVec.iterator();
		while(siter.hasNext()){
			Pair<Integer, Double> pair = siter.next();
			pro += dVec.get(pair.getKey()) * pair.getValue();
		}		
		return pro;
	}
	/**
	 * return the scalar product of a SparseVector and a DenseVector
	 * 
	 * @param sVec a DenseVector
	 * @param dVec a SparseVector
	 * @return
	 */
	private static Double scalarProduct(SparseVector sVec,DenseVector dVec)
	{
		double pro = 0.0;		
		Iterator<Pair<Integer, Double>> siter = sVec.iterator();
		while(siter.hasNext()){
			Pair<Integer, Double> pair = siter.next();
			pro += dVec.get(pair.getKey()) * pair.getValue();
		}		
		return pro;	  
	}
	/**
	 *  return the scalar product of a DenseVector and a DenseVector
	 * 
	 * @param dVec a DenseVector
	 * @param dVec1 a DenseVector
	 * @return Double
	 */
	private static Double scalarProduct(DenseVector dVec,DenseVector dVec1)
	{
		if(dVec.size() != dVec1.size()) return null;
		double pro = 0.0;		
		Iterator<Double> iterator = dVec.iterator();
		Iterator<Double> iterator1 = dVec1.iterator();
		
		while(iterator.hasNext() && iterator1.hasNext())
		{
			pro += iterator.next() * iterator1.next();
		}
		return pro;	  
	}
	/**
	 * return the scalar product of a SparseVector and a SparseVector
	 * 
	 * @param sVec1 SparseVector
	 * @param sVec2 SparseVector
	 * @return
	 */
	private static Double scalarProduct(SparseVector sVec1, SparseVector sVec2)
	{
		if(sVec1.length() != sVec2.length()) return null;
		Double pro = 0.0;
		if(sVec1.size() == 0 || sVec2.size() == 0) return pro;
		Iterator<Pair<Integer, Double>> iter1 = sVec1.iterator();
		Iterator<Pair<Integer, Double>> iter2 = sVec2.iterator();
		
		Pair<Integer, Double> pair1, pair2;
		pair1 = iter1.next();
		pair2 = iter2.next();
		boolean getPair1Next = false;
		boolean getPair2Next = false;
		while(true)
		{
			if(getPair1Next)
			{
				if(iter1.hasNext())
					pair1 = iter1.next();
				else
					break;
			}
			if(getPair2Next)
			{
				if(iter2.hasNext())
					pair2 = iter2.next();
				else
					break;
			}
			if( pair1.getKey() > pair2.getKey())
			{
				 getPair1Next = false;
				 getPair2Next = true;
			}
			else if(pair1.getKey() < pair2.getKey())
			{
				 getPair1Next = true;
				 getPair2Next = false;
			}
			else
			{
				pro += pair1.getValue() * pair2.getValue();
				getPair1Next = true;
				getPair2Next = true;
			}		
		}
		return pro;
	}
	/**
	 * generate a SparseVector from a given DenseVector
	 * 
	 * @param sVec SparseVector
	 * @param size the size of the DenseVector
	 * @return a DenseVector
	 */
	public static DenseVector sparse2Dense(SparseVector sVec, int size){
		if(sVec == null) return null;
		DenseVector vec = new DenseVector();
		Iterator<Pair<Integer, Double>> iter = sVec.iterator();
		
		Pair<Integer, Double> pair = iter.next();
		for(int i = 0; i < size; i ++){
			if(pair != null && i == pair.getKey()){
				vec.add(pair.getValue());
				if(iter.hasNext())
					pair = iter.next();
			}else{
				vec.add(0.0);
			}
		}
		
		return vec;
	}
	/**
	 * generate a sparseVector from a given denseVector
	 * 
	 * @param dVec a SparseVector
	 * @return a SparseVector
	 */
	public static SparseVector dense2Sparse(DenseVector dVec)
	{
		if(dVec == null) return null;
		SparseVector sVec = new SparseVector(dVec.size());
		int index = 0;
		for(int i = 0; i < dVec.size(); i ++)
		{
			if(dVec.get(i) > ROUND_TO_ZERO)
			{
				Pair<Integer,Double> pair = new Pair<Integer,Double>(index,dVec.get(i));
				sVec.add(pair);
				index++;
			}
		}
		return sVec;
	}
	
	// inVec .* number => outVec.  inVec and outVec must have the same length
	/**
	 * 
	 * @param inVec
	 * @param number
	 * @param outVec
	 */
	public static void multiplyNumber(Vector inVec, Double number, SparseVector outVec)
	{
		if(inVec == null || outVec == null) 
		{
			System.out.println("null vector error");
			return;
		}
		if(inVec instanceof SparseVector)
			multiplyNumber((SparseVector)inVec, number, outVec);
		else if(inVec instanceof DenseVector)
			multiplyNumber((DenseVector)inVec, number, outVec);
		else{
			System.out.println("inVec has the wrong type!");
		}
	}
	/**
	 * outVec is cleared and then saves all results multiplied on the inVec with a double number.
	 * 
	 * @param inVec a sparse Vector
	 * @param number
	 * @param outVec a sparse Vector
	 */
	private static void multiplyNumber(SparseVector inVec, Double number, SparseVector outVec){
		if(inVec.length() != outVec.length())
		{
			System.out.println("length must match");
			return;
		}
		outVec.clear();
		Iterator<Pair<Integer, Double>> inIter = inVec.iterator();
		// have question here
		while(inIter.hasNext()){
			Pair<Integer, Double> pair = inIter.next();
			outVec.add(new Pair<Integer,Double>(pair.getKey(), pair.getValue() * number));
		}
	}
	
	//write javadoc
	/**
	 * multiply a number on the elements of the densevector, and then put them in outVec in order.
	 * 
	 * @param inVec
	 * @param number
	 * @param outVec
	 */
	private static void multiplyNumber(DenseVector inVec, Double number, SparseVector outVec)
	{
		if(inVec.size() != outVec.length())
		{
			System.out.println("Length must match");
			return;
		}
		outVec.clear();
		int index = 0;
		for(int i = 0; i < inVec.size(); i ++)
		{
			// add in defined non-zero numbers to sparseVector
			double temp = inVec.get(i)* number;
			if(temp > ROUND_TO_ZERO)
			{
				Pair<Integer,Double> pair = new Pair<Integer,Double>(index,temp);
				outVec.add(pair);
				index++;
			}			
			
		}
		
	}
	/**
	 * multiply a number on every element of the inVec and then put it in the outVec in order.
	 * 
	 * @param inVec
	 * @param number
	 * @param outVec
	 */
	public static void multiplyNumber(Vector inVec, Double number, DenseVector outVec){
		if(inVec == null || outVec == null)
		{
			System.out.println("Null vector error");
			return;
		}
		if(inVec instanceof SparseVector)
			multiplyNumber((SparseVector)inVec, number, outVec);
		else if(inVec instanceof DenseVector)
			multiplyNumber((DenseVector)inVec, number, outVec);
		else{
			System.out.println("inVec has the wrong type!");
		}
	}
	
	// inVec .* number => outVec.  inVec and outVec must have the same length
	//tested
	/**
	 * multiply a number on every element of the inVec and then put it in the outVec in order.
	 * @param inVec
	 * @param number
	 * @param outVec
	 */
	private static void multiplyNumber(SparseVector inVec, Double number, DenseVector outVec){
		if(inVec.length() != outVec.size())
		{
			System.out.println("length must match");
			return;
		}
		Iterator<Pair<Integer, Double>> inIter = inVec.iterator();
		Pair<Integer, Double> pair = null;
		
		if(inIter.hasNext())
			pair = inIter.next();
		
		int size = outVec.size();
		for(int i = 0; i < size; i++){
			if(pair != null && pair.getKey() == i){
				outVec.set(i, pair.getValue() * number);
				if(inIter.hasNext())
					pair = inIter.next();
			}
			else
				outVec.set(i, 0.0);
		}
	}
	//tested
	/**
	 * multiply a number on every element of the inVec and then put it in the outVec in order.
	 * 
	 * @param inVec
	 * @param number
	 * @param outVec
	 */
	private static void multiplyNumber(DenseVector inVec, Double number, DenseVector outVec){
		if(inVec.size() != outVec.size()){
			System.out.println("Length must match!");
			return;
		}
		
		int size = inVec.size();
		for(int i = 0; i < size; i++)
			outVec.set(i, inVec.get(i) * number);
	}
	
	// return a seperate copy of vec
	//tested
	/**
	 * return another copy of the input sparse vector, the two vectors are independent.
	 * 
	 * @param vec
	 * @return a new SparseVector
	 */
	public static SparseVector copy(SparseVector vec)
	{
		if(vec == null) 
		{
			System.out.println("null vector");
			return null;
		}
		SparseVector nvec = new SparseVector(vec.length());
		Iterator<Pair<Integer,Double>> iterator = nvec.iterator();
		while(iterator.hasNext())
		{
			nvec.add(iterator.next());
		}
		return nvec;
	}
	/**
	 * copy every element in the input vector to the output vector, the two vectors are independent.
	 * 
	 * @param vec
	 * @return a new DenseVector
	 */
	public static DenseVector copy(DenseVector vec){
		if(vec == null)
		{
			System.out.println("null vector");
			return null;
		}
		DenseVector nvec = new DenseVector();
		Iterator<Double> iter = vec.iterator();
		
		while(iter.hasNext()){
			Double val = iter.next();  
			nvec.add(val);  
		}
		
		return nvec;
	}
	/**
	 * return the square error of two vectors.
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static Double squareError(Vector vec1, Vector vec2)
	{
		if(vec1 == null || vec2 == null) 
		{
			System.out.println("null vector");
			return null;
		}
		
		if(vec1 instanceof SparseVector && vec2 instanceof SparseVector)
		{
			return squareError((SparseVector) vec1, (SparseVector) vec2);
		}
		else if(vec1 instanceof SparseVector && vec2 instanceof DenseVector)
		{
			return squareError((SparseVector) vec1, (DenseVector) vec2);
		}
		else if(vec1 instanceof DenseVector && vec2 instanceof SparseVector)
		{
			return squareError((SparseVector) vec2,(DenseVector) vec1);
		}
		else if(vec1 instanceof DenseVector && vec2 instanceof DenseVector)
		{
			return squareError((DenseVector) vec1, (DenseVector) vec2);
		}
		else
		{
			System.out.println("the wrong input type");
			return null;
		}
		
	}
	// return sum of |w1(i) - w2(i)|^2 for each dimension i
	//tested
	/**
	 *  return the square error of two DenseVectors.
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static Double squareError(DenseVector vec1, DenseVector vec2){
		if(vec1.size() != vec2.size()){
			System.out.println("Length must match!");
			return null;
		}
		
		double sum = 0.0;
		int size = vec1.size();
		for(int i = 0; i < size; i++){
			double diff = Math.abs(vec1.get(i) - vec2.get(i));
			sum += diff * diff;
		}
		return sum;
	}
	//me
	/**
	 * return the square error of the same sized DenseVector and SparseVector.
	 * @param sVec
	 * @param dVec
	 * @return
	 */
	private static Double squareError(SparseVector sVec, DenseVector dVec)
	{
		if(dVec.size() != sVec.length()) 
		{
			System.out.println("Length must match!");
			return null;
		}
		double sum = 0.0;
		double diff = 0.0;
		int startIndex = 0;
		Iterator<Pair<Integer,Double>> iterator = sVec.iterator();
		while(iterator.hasNext())
		{
			Pair<Integer,Double> pair = iterator.next();
			int index = pair.getKey();
			if(index >= dVec.size())
			{
				System.out.println("The index is out of range!");
				return null;
			}
			for(int i  = startIndex; i < index; i ++)
			{
				diff = dVec.get(i);
				sum += diff * diff;
			}

			diff = pair.getValue() - dVec.get(index);
			sum += diff * diff;
			startIndex = index + 1;
		}
		for(int i = startIndex; i < dVec.size(); i ++)
		{
			diff = dVec.get(i);
			sum += diff * diff;
		}
		return sum;		
	}
	//start from here
	/**
	 * return the square error of two SparseVectors
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static Double squareError(SparseVector vec1, SparseVector vec2)
	{
		if(vec1.length() != vec2.length()) 
		{
			System.out.println("Length must match!");
			return null;
		}
		double sum = 0.0;
		double diff = 0.0;
		
		Iterator<Pair<Integer, Double>> iter1 = vec1.iterator();
		Iterator<Pair<Integer, Double>> iter2 = vec2.iterator();
	
		if(vec1.size() == 0) 
		{
			while(iter2.hasNext())
			{
				diff = iter2.next().getValue();
				sum += diff * diff;
			}
			return sum;
		}
		if(vec2.size() == 0)
		{
			while(iter1.hasNext())
			{
				diff = iter1.next().getValue();
				sum += diff * diff;
			}
			return sum;
		}
		
		
		Pair<Integer, Double> pair1, pair2;
		pair1 = iter1.next();
		pair2 = iter2.next();
		while(true)
		{
			if(pair1.getKey() > pair2.getKey())
			{
				diff = pair2.getValue();
				sum += diff * diff;
				if(iter2.hasNext()) pair2 = iter2.next();
				else 
				{
					diff = pair1.getValue();
					sum += diff * diff;
					break;
				}
			}
			else if(pair1.getKey() < pair2.getKey())
			{
				diff = pair1.getValue();
				sum += diff * diff;
				if(iter1.hasNext()) pair1 = iter1.next();
				else 
				{
					diff = pair2.getValue();
					sum += diff * diff;
					break;
				}
			}
			else
			{
				diff = pair1.getValue() - pair2.getValue();
				sum += diff * diff;
				if(!iter1.hasNext() || !iter2.hasNext())
				{
					break;
				}
				pair1 = iter1.next();
				pair2 = iter2.next();
			}
		}
		while(iter1.hasNext())
		{
			diff = iter1.next().getValue();
			sum += diff * diff;
		}
		while(iter2.hasNext())
		{
			diff = iter2.next().getValue();
			sum += diff * diff;
		}		
		return sum;
	}
	
	// test function for square error tested
	private static void testSquareError()
	{
		Random r = new Random();
		int length = r.nextInt(30);
		SparseVector sparseVector = new SparseVector(length);
		SparseVector sparseVector1 = new SparseVector(length);
		DenseVector denseVector = new DenseVector(length);
		DenseVector denseVector1 = new DenseVector(length);
		double[] array = new double[length];
		double[] array1 = new double[length];
		double[] array2 = new double[length];
		double[] array3 = new double[length];
		//generate sparse vector1
		for(int i = 0; i < array.length; i ++)
		{
			int temp = r.nextInt(2);
			double d = r.nextDouble();
			if(temp == 1)
			{
				sparseVector.add(new Pair<Integer,Double>(i,d));
				array[i] = d;
			}
		}
		// generate sparse vector2
		for(int i = 0; i < array.length; i ++)
		{
			int temp = r.nextInt(2);
			double d = r.nextDouble();
			if(temp == 1)
			{
				sparseVector1.add(new Pair<Integer,Double>(i,d));
				array1[i] = d;
			}
		}
		
		// generate dense vector1
		for(int i = 0; i < array.length; i ++)
		{
			double d = r.nextDouble();
			//denseVector.add(d);
			denseVector.set(i, d);
			array2[i] = d;
		}
		// generate dense vector2
		for(int i = 0; i < array.length; i ++)
		{
			double d = r.nextDouble();
			//denseVector1.add(d);
			denseVector1.set(i, d);
			array3[i] = d;
		}
		// test denseVector && sparseVector
		double sum1 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			double diff = Math.abs(array[i] - array2[i]);
			sum1 += diff * diff;			
		}
		double sum11 = squareError(sparseVector, denseVector);
		//System.out.println("1 " + sum1 + "  " + sum11);
		if(Math.abs(sum1 - sum11) < ROUND_TO_ZERO) System.out.println(true);
		else 
		{
			System.out.println(Arrays.toString(array));
			System.out.println(Arrays.toString(array2));
			System.out.println( sum1 + "  " + sum11);
		}
		// test denseVector && denseVector1
		double sum2 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			double diff =  Math.abs(array3[i] - array2[i]);
			sum2 += diff * diff;
		}
		double sum22 = squareError(denseVector1, denseVector);
		if(Math.abs(sum2 - sum22) < ROUND_TO_ZERO) System.out.println(true);
		else
		{
			System.out.println(Arrays.toString(array3));
			System.out.println(Arrays.toString(array2));
			System.out.println( sum2 + "  " + sum22);
		}
		// test sparseVector && sparseVector1
		double sum3 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			double diff = Math.abs(array[i] - array1[i]);
			sum3 += diff * diff;
		}
		double sum33 = squareError(sparseVector, sparseVector1);
		if(Math.abs(sum3 - sum33) < ROUND_TO_ZERO) System.out.println(true);
		else 
		{
			System.out.println(Arrays.toString(array));
			System.out.println(Arrays.toString(array1));
			System.out.println( sum3 + "  " + sum33);
		}
	}
	
	// return sum of |w1(i) - w2(i)| for each dimension i //tested
	/**
	 * return the sum of the absolute value of (vec1 - vec2)
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static Double absError(Vector vec1, Vector vec2)
	{
		if(vec1 == null || vec2 == null) return null;
		
		if(vec1 instanceof SparseVector && vec2 instanceof SparseVector)
		{
			return  absError((SparseVector) vec1, (SparseVector) vec2);
		}
		else if(vec1 instanceof SparseVector && vec2 instanceof DenseVector)
		{
			return  absError((SparseVector) vec1, (DenseVector) vec2);
		}
		else if(vec1 instanceof DenseVector && vec2 instanceof SparseVector)
		{
			return  absError((SparseVector) vec2,(DenseVector) vec1);
		}
		else if(vec1 instanceof DenseVector && vec2 instanceof DenseVector)
		{
			return  absError((DenseVector) vec1, (DenseVector) vec2);
		}
		else
		{
			System.out.println("the wrong input type.");
			return null;
		}
	}
	// return sum of |w1(i) - w2(i)| for each dimension i
	/**
	 * return sum of |w1(i) - w2(i)| for each dimension i
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static Double absError(DenseVector vec1, DenseVector vec2){
		if(vec1 == null || vec2 == null)
			return null;
		if(vec1.size() != vec2.size()){
			System.out.println("Length must match!");
			return null;
		}
		
		double sum = 0.0;
		int size = vec1.size();
		for(int i = 0; i < size; i++){
			sum += Math.abs(vec1.get(i) - vec2.get(i));
		}
		return sum;
	}
	/**
	 * return sum of |w1(i) - w2(i)| for each dimension i
	 * @param sVec
	 * @param dVec
	 * @return
	 */
	private static Double absError(SparseVector sVec, DenseVector dVec)
	{
		if(sVec == null || dVec == null)
			return null;
		if(sVec.length() != dVec.size()){
			System.out.println("Length must match!");
			return null;
		}
		
		double sum = 0.0;
		double diff = 0.0;
		int startIndex = 0;
		Iterator<Pair<Integer,Double>> iterator = sVec.iterator();
		while(iterator.hasNext())
		{
			Pair<Integer,Double> pair = iterator.next();
			int index = pair.getKey();
			if(index >= dVec.size())
			{
				System.out.println("The index is out of range!");
				return null;
			}
			for(int i  = startIndex; i < index; i ++)
			{
				diff = dVec.get(i);
				sum += Math.abs(diff);
			}

			diff = Math.abs(pair.getValue() - dVec.get(index));
			sum += diff;
			startIndex = index + 1;
		}
		for(int i = startIndex; i < dVec.size(); i ++)
		{
			diff = Math.abs(dVec.get(i));
			sum += diff ;
		}
		return sum;		
	}
	/**
	 *  return sum of |w1(i) - w2(i)| for each dimension i
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static Double absError(SparseVector vec1, SparseVector vec2)
	{
		if(vec1.length() != vec2.length()) 
		{
			System.out.println("Length must match!");
			return null;
		}
		double sum = 0.0;
		double diff = 0.0;
		
		Iterator<Pair<Integer, Double>> iter1 = vec1.iterator();
		Iterator<Pair<Integer, Double>> iter2 = vec2.iterator();
		
		if(vec1.size() == 0 )
		{
			while(iter2.hasNext())
			{
				sum += Math.abs(iter2.next().getValue());
			}
			return sum;
		}
		
		if(vec2.size() == 0)
		{
			while(iter1.hasNext())
			{
				sum += Math.abs(iter1.next().getValue());
			}
			return sum;
			
		}
		
		Pair<Integer, Double> pair1, pair2;
		pair1 = iter1.next();
		pair2 = iter2.next();
		while(true)
		{
			if(pair1.getKey() > pair2.getKey())
			{
				diff = Math.abs(pair2.getValue());
				sum += diff;
				if(iter2.hasNext()) pair2 = iter2.next();
				else 
				{
					diff = Math.abs(pair1.getValue());
					sum += diff;
					break;
				}
			}
			else if(pair1.getKey() < pair2.getKey())
			{
				diff = Math.abs(pair1.getValue());
				sum += diff;
				if(iter1.hasNext()) pair1 = iter1.next();
				else 
				{
					diff = Math.abs(pair2.getValue());
					sum += diff;
					break;
				}
			}
			else
			{
				diff = Math.abs(pair1.getValue() - pair2.getValue());
				sum += diff ;
				if(!iter1.hasNext() || !iter2.hasNext())
				{
					break;
				}
				pair1 = iter1.next();
				pair2 = iter2.next();
			}
		}
		while(iter1.hasNext())
		{
			diff = Math.abs(iter1.next().getValue());
			sum += diff;
		}
		while(iter2.hasNext())
		{
			diff = Math.abs(iter2.next().getValue());
			sum += diff;
		}		
		return sum;
	}
	//test function
	private static void testAbsError()
	{
		Random r = new Random();
		int length = r.nextInt(30);
		SparseVector sparseVector = new SparseVector(length);
		SparseVector sparseVector1 = new SparseVector(length);
		DenseVector denseVector = new DenseVector(length);
		DenseVector denseVector1 = new DenseVector(length);
		double[] array = new double[length];
		double[] array1 = new double[length];
		double[] array2 = new double[length];
		double[] array3 = new double[length];
		//generate sparse vector1
		for(int i = 0; i < array.length; i ++)
		{
			int temp = r.nextInt(2);
			double d = r.nextDouble();
			if(temp == 1)
			{
				sparseVector.add(new Pair<Integer,Double>(i,d));
				array[i] = d;
			}
		}
		// generate sparse vector2
		for(int i = 0; i < array.length; i ++)
		{
			int temp = r.nextInt(2);
			double d = r.nextDouble();
			if(temp == 1)
			{
				sparseVector1.add(new Pair<Integer,Double>(i,d));
				array1[i] = d;
			}
		}
		
		// generate dense vector1
		for(int i = 0; i < array.length; i ++)
		{
			double d = r.nextDouble();
			//denseVector.add(d);
			denseVector.set(i, d);
			array2[i] = d;
		}
		// generate dense vector2
		for(int i = 0; i < array.length; i ++)
		{
			double d = r.nextDouble();
			//denseVector1.add(d);
			denseVector1.set(i, d);
			array3[i] = d;
		}
		// test denseVector && sparseVector
		double sum1 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			sum1 += Math.abs(array[i] - array2[i]);
		}
		double sum11 = absError(sparseVector, denseVector);
		//System.out.println("1 " + sum1 + "  " + sum11);
		if(Math.abs(sum1 - sum11) < ROUND_TO_ZERO) System.out.println(true);
		else System.out.println(false);
		// test denseVector && denseVector1
		double sum2 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			sum2 += Math.abs(array3[i] - array2[i]);
		}
		double sum22 = absError(denseVector1, denseVector);
		if(Math.abs(sum2 - sum22) < ROUND_TO_ZERO) System.out.println(true);
		else System.out.println(false);
		// test sparseVector && sparseVector1
		double sum3 = 0.0;
		for(int i = 0; i < array.length; i ++)
		{
			sum3 += Math.abs(array[i] - array1[i]);
		}
		double sum33 = absError(sparseVector, sparseVector1);
		if(Math.abs(sum3 - sum33) < ROUND_TO_ZERO) System.out.println(true);
		else System.out.println(false);
				
		
	}
	// return sum of |w(i)| for each dimension i tested
	/**
	 * return sum of |w(i)| for each dimension i tested
	 * @param vec
	 * @return
	 */
	public static Double getAbsSum(Vector vec)
	{
		if(vec == null) return null;
		
		if(vec instanceof SparseVector) return getAbsSum((SparseVector) vec);
		else return getAbsSum((DenseVector) vec);
		
	}
	/**
	 * return sum of |w(i)| for each dimension i tested
	 * @param vec
	 * @return
	 */
	private static Double getAbsSum(DenseVector vec){
		double sum = 0.0;
		int size = vec.size();
		for(int i = 0; i < size; i ++)
			sum += Math.abs(vec.get(i));
		
		return sum;
	}
	/**
	 * return sum of |w(i)| for each dimension i tested
	 * @param vec
	 * @return
	 */
	private static Double getAbsSum(SparseVector vec)
	{
		double sum = 0.0;
		Iterator<Pair<Integer,Double>> iterator = vec.iterator();
		while(iterator.hasNext())
		{
			sum += Math.abs(iterator.next().getValue());
		}
		return sum;
	}
	// the test function
	private static void testAbsSum()
	{
		Random r = new Random();
		int length = r.nextInt(50);
		SparseVector sparseVector = new SparseVector(length);
		DenseVector denseVector = new DenseVector(length);
		double[] array = new double[length];
		for(int i = 0; i < array.length; i++)
		{
			double d =  -r.nextDouble();
			if(Math.abs(d) > ROUND_TO_ZERO)
			{
				sparseVector.add(new Pair<Integer,Double>(i,d));
				array[i] = d;
				denseVector.add(d);
			}
			else
				denseVector.add(0.0);
			
				
		}
		System.out.println(Arrays.toString(array));
		double sum1 = 0.0;
		for(double d : array)
		{
			sum1 +=  Math.abs(d);
		}
		double sum2 = getAbsSum(denseVector);
		if(Math.abs(sum1 - sum2) < ROUND_TO_ZERO)
			System.out.println(true);
		else
			System.out.println(false);
		double sum = getAbsSum(sparseVector);
		if(Math.abs(sum1 - sum) < ROUND_TO_ZERO)
			System.out.println(true);
		else
			System.out.println(false);
	}
	public static void main(String[] args)
	{
		for(int i = 0; i < 10; i ++)
		{
			//testAbsSum();
			//testAbsError();
			testSquareError();
		}
	}
}