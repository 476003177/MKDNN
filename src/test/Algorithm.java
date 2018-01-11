package test;

import java.util.*;

public class Algorithm implements Runnable{

	int ms;                                  //ģʽ��1Ϊmkdnn��2Ϊkmmkdnn
	static int m=6;                          //top-m
	int k;                                   //knn
	double avr,qvr;                          //avr,qvr
	double d=0;                              //�ƶ��ľ���
	boolean move;                            //�ƶ����
	Vector<int[]> pl;                        //������ϼ���
	Vector<S> Q;                             //Q����
	SLink sl;
	Vector<Vector<Integer>> fl;              //�����ĸ��༯��
	Vector<double[]> u;                      //������u
	public Publicdata data;                  //��ʼ������
	
	public void kmeans()
	{
		this.fl=new Vector<Vector<Integer>>();        //��ʼ�����༯�Ϻ;�ֵ����
		this.u=new Vector<double[]>();
		for(int i=0;i<this.k;i++)
		{
			Vector fl1=new Vector<double[]>();
			this.fl.add(fl1);
			double[] p=new double[this.data.p[0].length];
			System.arraycopy(this.data.p[i+1], 0, p, 0, p.length);
			this.u.add(p);
		}
		int jx=1;                                      //�仯�˵����ĸ���
		while(jx>0)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jx=0;
			for(int i=0;i<this.k;i++)                  //���þ��༯��
			{
				fl.get(i).clear();
			}
			for(int i=1;i<this.data.p.length;i++)
			{
				double[] p=this.data.p[i];
				double[] d = new double[this.u.size()];
				double dmin=Double.MAX_VALUE;
				int di=0;
				for(int j=0;j<this.k;j++)
				{
					d[j]=this.data.distant(p, this.u.get(j));
//					System.out.println("d["+j+"]="+d[j]);
					if(dmin>d[j])
					{
						dmin=d[j];
						di=j;
					}
				}
				fl.get(di).add(i);
//				System.out.println("��"+i+"�����Ϊ��"+di+"��");
			}
			for(int i=0;i<this.k;i++)
			{
				double[] u1=new double[this.data.p[0].length];
				for(int j=0;j<u1.length;j++)
				{
					double zb=0;
					for(int z=0;z<this.fl.get(i).size();z++)
					{
						int dd=this.fl.get(i).get(z);
						zb=zb+this.data.p[dd][j];
					}
					u1[j]=zb/this.fl.get(i).size();
				}
//				System.out.println("��"+i+"������������Ϊ��"+this.u.get(i)[0]+","+this.u.get(i)[1]);
//				System.out.println("��"+i+"������������Ϊ��"+u1[0]+","+u1[1]);
				int jx1=0;                           //��ͬ��������
				for(int j=0;j<u1.length;j++)         //�ж�u�Ƿ�ı�
				{
					if(u1[j]!=this.u.get(i)[j])
					{
						System.arraycopy(u1, 0, this.u.get(i), 0, u1.length);
						break;
					}else
					{
						jx1++;
					}
				}
				if(jx1<u1.length)                    //���������˸ı�
				{
					jx++;
				}
			}
//			System.out.println("�ı����Ϊ��"+jx);
//			System.out.println("          ");
		}
//		for(int i=0;i<this.fl.size();i++)
//		{
//			System.out.println("��"+i+"������У�"+this.fl.get(i).size()+"��");
//		}
	}
	
	public void kmkmdnn()
	{
		int []a=new int[this.data.k];
		this.kmeans();
		this.data.r=1;
		this.pl(0, 0, a);
	}
	
	public double fqS1(int[] a,double[] p1)  //����fq1��S��
	{
		double s=0;
		int length=this.data.p[0].length;
		double[] p=new double[length];
		System.arraycopy(this.data.p[0], 0, p, 0, length);//��������
		s=this.fqS(a);
		System.arraycopy(p, 0,this.data.p[0], 0, length);
		return s;
	}
	
	public double fqS(int[] a)              //����fq��S��
	{
		int i=0,j=0;
		double s1=0,s2=0;
		for(i=0;i<this.k;i++)
		{
			for(j=i+1;j<this.k;j++)
			{
				s1=s1+2-(this.data.dp[a[i]][0]+this.data.dp[a[j]][0])/this.data.dism;
				if(this.data.r!=1)s2=s2+this.data.dp[a[i]][a[j]];
			}
		}
		s1=s1*(this.data.r)/this.k/(this.k-1);
		if(this.data.r!=1)s2=s2/this.data.dism*2*(1-this.data.r)/this.data.k/(this.k-1);
//		System.out.println(s1+s2+"��1");
		return s1+s2;
	}
	
	public void pl(int i,int m,int[] b)
	{
		if(this.ms==1)
		{
			for(;m<this.data.gs-1;m++)
			{
				b[i]=m+1;
				if(i<this.k-1)
				{
					pl(i+1,m+1,b);
				}else
				{
					int []plfz=new int[this.k];
					System.arraycopy(b, 0, plfz, 0, this.k);
					this.pl.add(plfz);			
				}
			}
		}else if(this.ms==2)                        //kmmkdnnʱ������
		{
			for(;m<this.fl.get(i).size();m++)
			{
				b[i]=this.fl.get(i).get(m);
				if(i<this.k-1)
				{
					pl(i+1,0,b);
				}else
				{
					int []plfz=new int[this.k];
					System.arraycopy(b, 0, plfz, 0, this.k);
					this.pl.add(plfz);
//					for(int z=0;z<4;z++)
//					{
//						System.out.print(b[z]+",");
//					}
//					System.out.println();
				}
			}
		}
	}
	
	public Vector<S> topm(Vector<int[]> pl,int m)
	{
		int[] a;
		a=new int[this.k];
		double fqs=0;
		S s;
		SLink sl=new SLink();
		SLink sld=null;
		SLink sldf=null;
		Vector<S> Q=new Vector<S>();
		for(int i=0;i<pl.size();i++)
		{
			a=pl.get(i);
			fqs=this.fqS(a);
			s=new S();
			s.a=a;
			s.fqs=fqs;
			sldf=sl;
			sld=sl.next;
			while(sld!=null)                 //�ҵ�˳�����Ĳ���λ��
			{
				if(sld.s.fqs>fqs)
				{
					sldf=sld;
					sld=sld.next;
				}else
				{
					break;
				}
			}                   
			SLink sl1=new SLink();
			sl1.s=s;
			sl1.next=sld;
			sldf.next=sl1;
			
			sld=sl.next;
			while(sld!=null)
			{
				sld=sld.next;
			}
		}
		sld=sl.next;
		for(int i=0;i<m;i++)                //�γ�Top-m����
		{
			Q.add(sld.s);
			sld=sld.next;
		}
		return Q;
	}
	
	public Vector<S> Qpx(Vector<S> Q)
	{
		Vector<S> Q1=new Vector<S>();
		Vector<int[]> pl=new Vector<int[]>();
		int []a;
		for(int i=0;i<Q.size();i++)
		{
			a=new int[this.k];
			System.arraycopy(Q.get(0).a, 0, a, 0, this.k);//��������
			pl.add(a);
		}
		Q1=this.topm(pl, Q.size());
		return Q1;
	}
	
	public void kdnnSearch()                 //kdnn���¶���Q
	{
		this.Q=this.topm(this.pl,this.m);
	}
	
	public void region(int g)                     //region����,����avr,qvr,1ֻ����avr��2ֻ����qvr��3������
	{
		if(this.Q.size()>1)
		{
			
			if(g==1||g==3)this.avr=this.circle(1, 2);
			if(this.avr==0)
			{
				System.out.println("��һ��:  fqsΪ"+this.Q.get(0).fqs+"     "+this.Q.get(0).a[1]);
				System.out.println("�ڶ���:  fqsΪ"+this.Q.get(1).fqs+"     "+this.Q.get(1).a[1]);
			}
			if(g==2||g==3)this.qvr=this.circle(1, this.Q.size());
		}else
		{
			System.out.println("��");
			this.avr=0;
			this.qvr=0;
		}
	}
	
	public double circle(int m,int n)        //����rmn
	{
		double r;
		r=this.Q.get(m-1).fqs-this.Q.get(n-1).fqs;
		r=r*this.data.dism/2/this.data.r;
		return r;
	}
	
	public void reportResult(int n)          //����Sn���
	{
		if(this.Q.size()>=1)
		{
//			System.out.print("����S"+n+"Ϊ��");
			View.a=new int[this.k+1];
			for(int i=0;i<this.k;i++)
			{
				int b=this.Q.get(n-1).a[i];
//				System.out.print(b);
				View.a[i+1]=b;
			}
//			System.out.println();
		}
	}
	
	/**
	 * ����pcpm�㷨ȱ�ݣ�
	 * 1��qvr����һֱ���£�Ҫȷ��s1��s6��fqs��,��qvr���������¼���ǰ�̶�����
	 * 2���ڶ���Գ�����£�avrû������
	 * 3��������ȷ��ı��ȫ���ԣ�����kmeans����
	 */
	public void pcpm1()                      //���İ�
	{
		double []q1;                         //������λ��q1
		Vector<S> Q1;                        //�¶���Q1
		S Sjlast=new S();                    //�²���Q1��S
		this.move=false;                     //���г�ʼ���������������ƶ�
		if(this.ms==2)this.kmkmdnn();
		this.kdnnSearch();
		this.region(3);
		this.reportResult(1);
		q1=new double[this.data.p[0].length];
		System.arraycopy(this.data.p[0], 0, q1, 0, q1.length);//��������
		this.move=true;                      //��ʼ�����������������ƶ�
		while(this.move)
		{
			for(int i=0;i<this.data.p[0].length;i++)
			{
				q1[i]=q1[i]+Publicdata.v[i];
			}
			this.d=this.data.distant(q1, this.data.p[0]);
			if(this.d<this.avr)
			{
//				System.out.println("q��avr��");
			}else if(this.d>this.qvr)
			{
//				System.out.println("q����qvr");
				this.data.updateq(q1);        //���¶���λ��
				this.kdnnSearch();
				this.region(3);
			}else
			{
//				System.out.println("q��qvr�ڵ�����avr");
				S Sj=new S();
				Q1=new Vector<S>();
				Q1.clear();
				while(this.Q.size()!=0)
				{
					Sj=this.Q.get(0);
					if(this.circle(1, this.Q.size())>this.d)
					{
						Q1.add(Sj);
						Sjlast=Sj;
					}else
					{
						if(this.fqS1(Sj.a, q1)>this.fqS1(Sjlast.a, q1))
						{
							Q1.add(Sj);
							Sjlast=Sj;
						}
					}
					Q.remove(0);
				}
				Q1=this.Qpx(Q1);
				this.Q=Q1;
				this.data.updateq(q1);           //���¶���λ��
				this.region(3);
			}
//			System.out.println(this.data.p[0][0]);
			this.reportResult(1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pcpm()                       //�Ľ���
	{
		double []q1;                         //������λ��q1
		this.move=false;                     //���г�ʼ���������������ƶ�
		if(this.ms==2)this.kmkmdnn();
		this.kdnnSearch();
		this.region(3);
		this.reportResult(1);
		q1=new double[this.data.p[0].length];
		System.arraycopy(this.data.p[0], 0, q1, 0, q1.length);//��������
		this.move=true;                      //��ʼ�����������������ƶ�
		while(this.move)
		{
			for(int i=0;i<this.data.p[0].length;i++)
			{
				q1[i]=q1[i]+Publicdata.v[i];
			}
			this.d=this.data.distant(q1, this.data.p[0]);
			
			if(this.d>this.qvr)
			{
//				System.out.println("q����qvr");
				this.data.updateq(q1);        //���¶���λ��
				this.kdnnSearch();
				this.region(3);

			}else if(this.d<this.avr)
			{
//				System.out.println("q��avr��");
			}else
			{
//				System.out.println("q��qvr�ڵ�����avr");
				int now=0;
				while(now<this.Q.size())              //�õ����µ�Q��������
				{
					int sizenow=this.Q.size();
					double lastfqs=this.fqS1(this.Q.get(sizenow-1).a, q1);
					this.Q.get(sizenow-1).fqs=lastfqs;//����Q����������fqs
					if(this.circle(now+1, this.Q.size())>this.d)//ûʧЧ������
					{
						this.Q.get(now).fqs=this.fqS1(this.Q.get(now).a, q1);//����fqs
						now++;
					}else
					{
						double pdfqs=this.fqS1(this.Q.get(now).a, q1);
						if(pdfqs>lastfqs)//ûʧЧ������
						{
							this.Q.get(now).fqs=pdfqs;//���´��жϵ�fqs
							now++;
						}else            //ʧЧ���Ƴ�
						{
							this.Q.remove(now);
						}
					}
				}
				int sizenow=this.Q.size();
				now=sizenow;
				while(now>1)             //���°����µ�fqs����
				{
					for(int z=0;z<now-1;z++)
					{
						if(this.Q.get(z).fqs<this.Q.get(z+1).fqs)//����
						{
							int a1[] =new int[this.k];
							double fqs1 =this.Q.get(z).fqs;
							System.arraycopy(this.Q.get(z).a, 0, a1, 0, this.k);//��������
							System.arraycopy(this.Q.get(z+1).a, 0, this.Q.get(z).a, 0, this.k);
							System.arraycopy(a1, 0, this.Q.get(z+1).a, 0, this.k);
							this.Q.get(z+1).fqs=fqs1;
						}
					}
					now--;
				}
				this.data.updateq(q1);           //���¶���λ��
				this.region(1);
				this.qvr=this.qvr-this.d;
			}
//			System.out.println(this.data.p[0][0]);
			this.reportResult(1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void precise()
	{
		double []q1;
		while(this.move)
		{
			q1=new double[this.data.p[0].length];
			for(int i=0;i<this.data.p[0].length;i++)
			{
				q1[i]=this.data.p[0][i]+Publicdata.v[i];
			}
			this.data.updateq(q1);
			this.kdnnSearch();
//			System.out.println(this.data.p[0][0]);
			this.reportResult(1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Algorithm(int ms)                   //���캯��
	{
//		this.m=6;                            //top-m
		this.pl=new Vector<int[]>();         //������ϼ���
		this.Q=new Vector<S>();              //Q����
		this.data=new Publicdata();          //��ʼ������
		this.k=this.data.k;                  //knn
		this.move=false;                     //��ʼ�����ǰ�Ȳ������ƶ�
		this.ms=ms;
		int []a=new int[this.data.k];
		if(ms==1)
		{
			this.pl(0,0,a);                  //��ʼ���������
		}
	}
	
//	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		double []v=new double[]{-1,0};
//		Algorithm test=new Algorithm(2);
//		test.kdnnSearch();
//		for(int i=0;i<test.Q.size();i++)                //�γ�Top-m����
//		{
//			System.out.println(test.Q.get(i).a[0]+","+test.Q.get(i).a[1]+","+test.Q.get(i).a[2]+","+test.Q.get(i).fqs);
//		}
//		test.pcpm();
//		test.precise();
//		test.kmkmdnn();
//	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.pcpm();
//		this.kmeans();
	}
}

class S                                      //S��
{
	int []a;                                 //��Ӧ�Ļ�������
	double fqs;
}

class SLink                                  //S����
{
	S s;                                    
	SLink next;
}
