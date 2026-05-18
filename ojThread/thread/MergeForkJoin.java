package thread;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

class MergeSortAction extends RecursiveAction{

	Integer[] data;
	int begin,end;
	public static final int minnum=6; 
	MergeSortAction(Integer []data,int begin,int end){
		this.data=data;
		this.begin=begin;
		this.end=end;
	}
	void print() {
		for(int i=0;i<end-begin;i++) {
			System.out.print(data[i+begin]+",");
		}
		System.out.println();
	}
	void bubble() {
		System.out.println("before bubble" +begin +" "+ end);
		print();
		for(int i=begin;i<end-1;i++) {
			for(int j=begin;j<end-(i-begin)-1;j++) {
				if(data[j]>data[j+1]) {
					int temp=data[j];
					data[j]=data[j+1];
					data[j+1]=temp;
				}
			}
		}
		System.out.println("after bubble" +begin +" "+ end);
		print();
	}
	void merge(int mid) {
		//data[begin,mid),[mid,end)
		System.out.println("before merge"+begin +" "+ end);
		print();
		int[] tempdata=new int[end-begin];
		int pos=0;
		int i=begin;
		int j=mid;
		while(i<mid&&j<end) {
			if(data[i]<=data[j]) {
				tempdata[pos++]=data[i++];
			}
			else {
				tempdata[pos++]=data[j++];
			}
	}
		if(i!=mid) {
			while(i<mid) {
				tempdata[pos++]=data[i++];	
			}
		}
		if(j!=end)
		{
			while(j<end) {
				tempdata[pos++]=data[j++];	
			}
		}
		for(int m=0;m<end-begin;m++) {
			data[begin+m]=tempdata[m];
		}
		System.out.println("after merge" +begin +" "+ end);
		print();
	}
	@Override
	public void compute() {
		// TODO Auto-generated method stub
		if(end-begin<6) {
			bubble();
		}
		else {
			int mid=(end-begin)/2+begin;
			MergeSortAction mf1=new MergeSortAction(data,begin,mid);
			MergeSortAction mf2=new MergeSortAction(data,mid,end);
			mf1.fork();
			mf2.fork();
			mf1.join();
			mf2.join();
			merge(mid);
		}
	}
	
}
public class MergeForkJoin {

	public static void main(String[] arg) {
		Random r=new Random();
		List<Integer> datalist=java.util.stream.Stream.generate(()->
		{
			return r.nextInt(100);
		}).limit(10).collect(Collectors.toList());
		System.out.println(datalist);
		Integer [] data=new Integer[10];
		data=datalist.toArray(data);
		MergeSortAction ma=new MergeSortAction(data,0,10);
		ForkJoinPool fjp=new ForkJoinPool();
		fjp.invoke(ma);
		System.out.println(Arrays.asList(data));
		
	}
}
