package test;

import java.util.*;

public class Publicdata {

	static int gs=17;
	static int k=4;                             //knn
	static double r=0.74;
	static double dism=0;
	static double[][] p=new double[gs][];  //p[0]Îªq
	static public double[][] dp=new double[gs][];
	static double []v=new double[]{1,0};
	
	public double getDism() {
		return dism;
	}
	public void setDism(double dism) {
		this.dism = dism;
	}
	public double getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public double distant(double[] p1,double[] p2)  //Å·ÊÏ¾àÀë
	{
		double d=0;
		for(int i=0;i<p1.length;i++)
		{
			d=d+Math.pow(p1[i]-p2[i],2);
		}
		d=Math.sqrt(d);
		return d;
	}
	public void updateq(double []p)
	{
		for(int i=0;i<this.p[0].length;i++)
		{
			this.p[0][i]=p[i];
		}
		double d=0;
		for(int i=1;i<this.gs;i++)
		{
			d=this.distant(this.p[0], this.p[i]);
			this.dp[0][i]=d;
			this.dp[i][0]=d;
		}
	}
	public Publicdata()
	{
		int i=0,j=0;
		double dism1=0;
		for(i=0;i<this.gs;i++)this.p[i]=new double[2];
		for(i=0;i<this.gs;i++)this.dp[i]=new double[this.gs];
//		this.p[0][0]=0;
//		this.p[0][1]=0;
//		this.p[1][0]=30;
//		this.p[1][1]=0;
//		this.p[2][0]=0;
//		this.p[2][1]=40;
//		this.p[3][0]=-50;
//		this.p[3][1]=0;
//		this.p[4][0]=0;
//		this.p[4][1]=-60;
//		this.p[5][0]=70/Math.sqrt(2);
//		this.p[5][1]=70/Math.sqrt(2);
//		this.p[6][0]=-80/Math.sqrt(2);
//		this.p[6][1]=-80/Math.sqrt(2);
		p[0][0]=40;
		p[0][1]=0;
		p[1][0]=30;
		p[1][1]=30;
		p[2][0]=60;
		p[2][1]=30;
		p[3][0]=30;
		p[3][1]=60;
		p[4][0]=60;
		p[4][1]=60;
		p[5][0]=30;
		p[5][1]=-30;
		p[6][0]=60;
		p[6][1]=-30;
		p[7][0]=30;
		p[7][1]=-60;
		p[8][0]=60;
		p[8][1]=-60;
		p[9][0]=-30;
		p[9][1]=30;
		p[10][0]=-60;
		p[10][1]=30;
		p[11][0]=-30;
		p[11][1]=60;
		p[12][0]=-60;
		p[12][1]=60;
		p[13][0]=-30;
		p[13][1]=-30;
		p[14][0]=-60;
		p[14][1]=-30;
		p[15][0]=-30;
		p[15][1]=-60;
		p[16][0]=-60;
		p[16][1]=-60;
		for(i=0;i<this.gs;i++)
		{
			this.dp[i][i]=0;
			for(j=i+1;j<this.gs;j++)
			{
				dism1=this.distant(this.p[i], this.p[j]);
				this.dp[i][j]=dism1;
				this.dp[j][i]=dism1;
//				System.out.println("i:"+i+"   j:"+j+"   dism:"+this.dism+"   dism1:"+dism1);
				if(this.dism<dism1)this.dism=dism1;
			}
		}
		
	}
}
