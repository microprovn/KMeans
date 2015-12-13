package cs286.kmeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.la4j.vector.dense.BasicVector;

public class Point {
	
	private double species = -1 ;
	private BasicVector point = new BasicVector();
	private Cluster centroid = null ;
	
	/**
	 * Initialize a data point with its features
	 * if the data point is a centroid , isCentroid is set to true otherwise false 
	 * @param input
	 * @param isCentroid
	 */
	Point(double[] input, boolean isCentroid){
		species = Double.valueOf(input[input.length - 1]) ;
		double[] t = new double[4] ;
		for(int i = 0 ; i < 4 ; i++)
			t[i] = input[i] ;
		this.point = new BasicVector(t);
	}
	
	
	public Cluster getCentroid() {
		return centroid;
	}

	public void setCentroid(Cluster centroid) {
		this.centroid = centroid;
	}

	public BasicVector getPoint() {
		return point;
	}
	
	public void removeCentroid(){
		this.centroid = null ;
	}

	public void setPoint(BasicVector point) {
		this.point = point;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof Point)) {
	        return false;
	    }
	    
	    Point x = (Point) other ;
	    if (this.species == x.getSpecies() && this.point.equals(x.getPoint()))
	    	return true;
	    else 
	    	return false ;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getSpecies() {
		return species;
	}

	/**
	 * 
	 * @param species
	 */
	public void setSpecies(double species) {
		this.species = species;
	}

	/**
	 * Calculate euclidean distance between 2 points 
	 * @param x
	 * @param y
	 * @return
	 */
	private static double euclideanDistance(BasicVector x, BasicVector y){
		double dist = 0 ;
		BasicVector v = (BasicVector) x.subtract(y) ;
		dist = v.euclideanNorm() ;
		return dist ;
	}
	
	/**
	 * Calculate cosine distance between 2 points
	 * @param x
	 * @param y
	 * @return
	 */
	private static double cosineDistance(BasicVector x, BasicVector y){
		double dist = 0 ;
		double dotproduct = x.innerProduct(y) ;
		dist = 1 - dotproduct/(x.euclideanNorm()*y.euclideanNorm()) ;
		return Math.abs(dist) ;
	}
	
	/**
	 * Calculate the distance between 2 point based on what kind of distance is used (cosine or euclidean)
	 * @param x
	 * @param y
	 * @param method
	 * @return
	 * @throws IOException 
	 */
	static public double distance(BasicVector x, BasicVector y, String method) throws IOException {
		if( method.equalsIgnoreCase("euclidean") || method.equalsIgnoreCase("cosine") ) {
			if(method.equalsIgnoreCase("euclidean"))
				return euclideanDistance(x,y) ;
			if(method.equalsIgnoreCase("cosine"))
				return cosineDistance(x,y) ;
		}else {
			throw new IOException() ;
		}
		
		return -100 ;
	}

}