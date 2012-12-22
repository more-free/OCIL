package cas;

import java.util.Iterator;

/* abstract classes */
abstract class Trainer<E>{
	protected SampleSet<E> trainSamples;
	abstract public double getError();  
}

abstract class GradientAscentTrainer<E> extends Trainer<E>{
	// parameters. should be encapsulated into a single class 
	protected double eta; 	// learning rate
	protected double lamda;   // coefficient for the 'penalization' term
	protected int maxIteration;  // max number of iteration
	protected double theta;     // threshold value for convergence. 
		  						  // e.g. if ||weights .- prevWeights|| <= theta, then convergence happened.
	
	abstract public void batchTrain();
	abstract public void stochasticTrain();
}

abstract class Tester<E>{
	protected SampleSet<E> testSamples;
	abstract public double getError();
	abstract public Stats getStats();
}

abstract class Sample{
	
}

/* interfaces */
interface Convergable{
	//boolean isConverged();
}

interface Visualizable{
	void print();
}

interface Vector<E> extends Iterable<E>, Visualizable{
	public int size();
}

/*  assistant classes */
class Pair<E, T>{
	private E key;
	private T value;
	
	Pair(){
		
	}
	
	Pair(E key, T value){
		this.key = key;
		this.value = value;
	}
	
	public void setKey(E key){
		this.key = key;
	}
	
	public void setValue(T value){
		this.value = value;
	}
	
	public E getKey(){
		return key;
	}
	
	public T getValue(){
		return value;
	}
}

class Stats{
	private Integer nTruePositive, nFalsePositive, nFalseNegative, nTrueNegative; // required
	private Double recall, precision, f1; // derived from tp, fp, fn, tn 
	
	Stats(Integer tp, Integer fp, Integer fn, Integer tn){
		nTruePositive = tp;
		nFalsePositive = fp;
		nFalseNegative = fn;
		nTrueNegative = tn;
		
		if(tp == 0 && fp == 0){ // tp == 0 && fn == 0
			recall = 0.0;
			precision = 0.0;
			f1 = 0.0;
		}else{
			recall =  (tp + 0.0) / (tp + fn);
			precision =   (tp + 0.0) / (tp + fp);
			f1 = (2 * recall * precision) / (recall + precision);
		}
	}
	
	public void print(){
		System.out.println(nTruePositive + " " + nFalsePositive + " " + nFalseNegative + " " + nTrueNegative + " " + 
				recall + " " + precision + " " + f1);
	}
	
	public void setTruePostive(Integer tp){
		this.nTruePositive = tp;
	}
	public void setFalsePositive(Integer fp){
		this.nFalsePositive = fp;
	}
	public void setFalseNegative(Integer fn){
		this.nFalseNegative = fn;
	}
	public void setTrueNegative(Integer tn){
		this.nTrueNegative = tn;
	}
	public void setRecall(Double recall){
		this.recall = recall;
	}
	public void setPrecision(Double precision){
		this.precision = precision;
	}
	public void setF1(Double f1){
		this.f1 = f1;
	}
	
	public Integer getTruePostive(){
		return this.nTruePositive;
	}
	public Integer getFalsePositive(){
		return this.nFalsePositive;
	}
	public Integer getFalseNegative(){
		return this.nFalseNegative;
	}
	public Integer getTrueNegative(){
		return this.nTrueNegative;
	}
	public Double getRecall(){
		return this.recall;
	}
	public Double getPrecision(){
		return this.precision;
	}
	public Double getF1(){
		return this.f1;
	}
}