package cs286.kmeans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

public class KMeans {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int k = Integer.parseInt(args[0]) ;
		double thresh = Double.parseDouble(args[2]) ;
		String dist = args[3] ;
		String init = args[4] ;
		String fileName = args[5] ;
		double[][] data = readFile(fileName) ;
		
		
		int max_iter  = 5000;
		int iter = 0 ;
		boolean changed = true ;
		List<Cluster> clusters = new ArrayList<Cluster>();
		List<Point> points = new ArrayList<Point>();
			
		double[][] centroids = getCentroids(data, init, k);
		
		for(int i = 0 ; i < k ; i++){
			BasicVector v = new BasicVector(centroids[i]) ;
			Cluster c = new Cluster(v) ;
			clusters.add(c) ;
		}
		
		
		for(int i = 0 ;  i < data.length ; i++){
			points.add(new Point(data[i], false)) ;
		}
		
		
		while (iter < max_iter && changed) {
			int count = 0 ;
			for(Point p : points){
				double min = Double.MAX_VALUE ;
				int clusterID = 0 ;
				for(int i = 0 ; i < k ; i++){
					double d = 0 ;
					if(dist.equalsIgnoreCase("euclidean"))
						d = Point.distance(p.getPoint(), clusters.get(i).getCentroid(), dist) ;
					if(dist.equalsIgnoreCase("cosine"))
						d = Point.distance(p.getPoint(), clusters.get(i).getCentroid(), dist) ;
					
					if(d < min){
						min = d;
						clusterID = i ;
					}	
				}
				
				for(int t1 = 0 ; t1 < k ; t1++){
					if(t1 == clusterID) {
						clusters.get(t1).addPoint(p) ;
					}
					else
						clusters.get(t1).removePoint(p);
							
				}
			}
			
			ArrayList<Double> deltas = new ArrayList<Double>();
			for(int i = 0 ; i < k ; i++){
				clusters.get(i).setNewCentroid(dist);
				deltas.add(clusters.get(i).getDelta()) ; 
			}
			Collections.sort(deltas);
			if(deltas.get(deltas.size()-1) <= thresh){
				changed = false ;
				//System.out.println(deltas.get(deltas.size()-1)) ;
			}
			
			iter++ ;
		}	
		
		double meanInter = 0 ;
		double meanIntra = 0 ;
		double sumOfMeanDist = 0 ;
		double sumDistanceOfCentroid = 0 ;
		
		//Calculate mean of intracluster distance
		for(int i = 0 ; i < k ;i++)
			sumOfMeanDist += clusters.get(i).getMeanIntraDist(dist) ;
		meanIntra = sumOfMeanDist/k ;
		
		//Calculate mean of intercluster distance
		int totalDistCentroid = 0 ;
		for(int i = 0 ; i < k-1 ;i++){
			BasicVector centroid1 = clusters.get(i).getCentroid() ;
			for(int j = i+1 ; j < k ; j++ ) {
				BasicVector centroid2 = clusters.get(j).getCentroid() ;
				sumDistanceOfCentroid += Point.distance(centroid1,centroid2, dist) ;
				totalDistCentroid++ ;
			}
		}
		meanInter = sumDistanceOfCentroid / totalDistCentroid ;
		
		System.out.println("k = " + k) ;
		System.out.println("Distance = " + dist);
		int j =  0 ; 
		for(Cluster c : clusters){
			System.out.println("Centroid "+ j + " = " + c.getCentroid()) ;
			j++;
		}
		
		System.out.println("mean intercluster distance = " + meanInter);
		System.out.println("mean intracluster distance = " + meanIntra);
		
	}
	
	
	/**
	 * 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public static double[][] readFile(String inputFile) throws IOException{
		ArrayList<double[]> data = new ArrayList<double[]>() ;
		FileReader fileReader = null ;
		//BufferedReader bufferedReader = null ;
		try {
			fileReader = new FileReader(inputFile) ;
			//bufferedReader = new BufferedReader(fileReader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner scan = new Scanner(fileReader) ;
		
		String line = null ;
		while(scan.hasNext()){
			double[] a = new double[5];
			line = scan.nextLine() ;
			String[] t = line.split("\\t");
			for(int i = 0 ; i < 5 ; i++){
				a[i] = Double.parseDouble(t[i]);
			}
			data.add(a) ;
		}
		
		fileReader.close();
		scan.close();
		double[][] returnMatrix = new double[data.size()][] ;
		for(int i = 0 ; i < data.size(); i++){
			returnMatrix[i] = data.get(i) ;
		}
		return returnMatrix ;
	}
	
	/**
	 * 
	 * @param data
	 * @param init
	 * @param k
	 * @return
	 * @throws IOException
	 */
	public static double[][] getCentroids(double[][] data , String init , int k) throws IOException{
		if(init.equalsIgnoreCase("zero"))
			return getZero(k);
		else{
			if (init.equalsIgnoreCase("partition"))
				return getPartition(data,  k);
			else if(init.equalsIgnoreCase("random")){
				return getRandom(data,k) ;
			} 
			else
				throw new IOException() ;
		}
		
	}
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	private static double[][] getZero(int k){
		return new double[k][4] ; 
	}
	
	private static double[][] getRandom(double[][] data, int k){
		Basic2DMatrix m = new Basic2DMatrix(data) ;
		
		double[][] d = new double[k][] ;
		for (int j = 0 ; j < k ; j ++){
			double[] c = new double[4] ;
			Random rand = new Random() ;
			for (int i = 0; i < 4 ;i++) {
				double min = m.minInColumn(i) ;
				double max = m.maxInColumn(i) ;
				double ranNum = min + rand.nextInt(Math.round((float)(max- min)))  ;
				c[i] = ranNum ;
			}
			d[j] = c ;
		}
		return d;
	}
	
	/**
	 * 
	 * @param data
	 * @param k
	 * @return
	 */
	private static double[][] getPartition(double[][] data, int k){
		Basic2DMatrix m = new Basic2DMatrix(data) ;
		double[] c = new double[4] ;
		double[] b = new double[4] ;
		double[][] d = new double[k][];
		for (int i = 0; i < 4 ;i++) {
			double min = m.minInColumn(i) ;
			double max = m.maxInColumn(i) ;
			c[i] = min ;
			b[i] = (max - min)/k ;
		}
		BasicVector v1 = new BasicVector(c); 
		BasicVector v2 = new BasicVector(b) ;
		for(int i = 0 ; i < k ; i++){
			double t = ((2.0*i) + 1)/2 ;
			BasicVector v = (BasicVector) v1.add((v2.multiply(t)))   ;
			d[i] = v.toArray() ; 
		}
		return d ;
	}


}