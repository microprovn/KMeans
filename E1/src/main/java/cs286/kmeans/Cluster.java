package cs286.kmeans;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

public class Cluster {
	
	private BasicVector centroid = new BasicVector();
	private ArrayList<Point> points = new ArrayList<Point>() ;
	private double delta = Double.MAX_VALUE ;
	
	Cluster(BasicVector v) throws IOException{
		this.centroid = v;
		this.points = new ArrayList<Point>() ;
	}
	
	/**
	 * Set new centroid based on what type of distance is calculated
	 * 
	 * @param method
	 * @throws IOException 
	 */
	public void setNewCentroid(String method) throws IOException{
		if(this.points.size() != 0){
			double[][] a = new double[this.points.size()][] ;
			int i = 0 ;
			int numRec = this.points.size() ;
			for(Point p : this.points){
				double[] t = p.getPoint().toArray() ;
				a[i] = t ;
				i++;
			}
			Basic2DMatrix m = new Basic2DMatrix(a) ;
			double[] newCent = new double[4] ;
			//Calculate mean of each features (columns)
			for( i= 0 ; i< 4 ; i++){
				newCent[i] = m.getColumn(i).sum()/numRec ;
			}
			// calculate the change in centroid 
			delta = Point.distance( new BasicVector(newCent), this.centroid , method);
			this.centroid = new BasicVector(newCent) ;
			
			
		}
	}
	
	/**
	 * Add a data point if it is not exist in the cluster
	 * @param p
	 * @return
	 */
	public boolean addPoint(Point p){
		if(!this.points.contains(p) || this.points == null) {
			this.points.add(p);
			return true;
		}
		return false ;
	}
	
	/**
	 * Remove a data point if it exists in the cluster 
	 * @param p
	 * @return
	 */
	public boolean removePoint (Point p){
		if(this.points != null && this.points.contains(p) ){
			this.points.remove(p);
			return true;
		}
		return false;
			
	}
	
	/**
	 * Get mean intracluster distance between data points
	 * @param method
	 * @return
	 * @throws IOException 
	 */
	public double getMeanIntraDist(String method) throws IOException{
		double sum = 0 ;
		int count = 0 ;
		for (int i = 0 ; i < points.size()-1 ; i++){
			Point p1 = this.points.get(i);
			for(int j = i+1 ; j < this.points.size() ; j++) {
				Point p2 = this.points.get(j) ;
				sum += Point.distance(p1.getPoint()	, p2.getPoint(), method ) ;
				count++;
			}
		}
		
		return sum/count ;
	}
	
	public double getPercentage(double species){
		int count = 0 ;
		for(Point p : this.points){
			if(p.getSpecies() == species)
				count++ ;
		}
		return (double)points.size()/count ;	
	}
	
	/**
	 * get centroid vector
	 * @return
	 */
	public BasicVector getCentroid() {
		return centroid;
	}
	
	/**
	 * Set new centroid vector
	 * @param centroid
	 */
	public void setCentroid(BasicVector centroid) {
		this.centroid = centroid;
	}

	/**
	 * Get all data points of the cluster
	 * @return
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	
	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	
	public String toString(){
		String s = "" ;
		int count0 = 0;
		int count1 = 0 ;
		int count2 = 0 ;	
		for(Point p : this.getPoints()){
			if(p.getSpecies() == 0)
				count0++ ;
			else if(p.getSpecies() == 1)
				count1++ ;
			else
				count2++ ;
		}
		return s+= "count 0: " + count0 + "\n" + "count 1: " + count1 + "\n" + "count 2: " + count2 ;
	}

}