package test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class View extends JFrame implements ActionListener,Runnable{
	
	JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7,jl8,jlQ[];
	JTextField jtf1,jtf2,jtf3,jtf4;
	JButton jCon,jStop,jCancel;
	JRadioButton jrbs1,jrbs2;                          //���嵥ѡ��
	ButtonGroup bg;                                    //����ButtonGroup��ť��
	Publicdata pd=new Publicdata();
	Algorithm at;
	int x=220,y=220;                                   //����ԭ��ľ�������
	int d=20;                                          //��ƽ���ߵľ���
	int lx=400,ly=340;                                 //�����߳�
	int ld=18;                                         //�ص�߳�
	int ms=1;                                          //����ģʽ��1Ϊԭʽ��2Ϊ�Ľ�
	static int[]a;
	Thread t;
	Thread t1;
	
	public View()
	{
		this.a=new int[Publicdata.k+1];
		
		//�����������
		jl1=new JLabel("Vx��");
		jl1.setBounds(60, 400, 40, 30);
		this.add(jl1);
		
		jl2=new JLabel("Vy��");
		jl2.setBounds(60, 430, 40, 30);
		this.add(jl2);
		
		jl3=new JLabel("k��");
		jl3.setBounds(60, 460, 40, 30);
		this.add(jl3);
		
		jl4=new JLabel("�ˣ�");
		jl4.setBounds(60, 370, 40, 30);
		this.add(jl4);
		
		jl5=new JLabel("ģʽ��");
		jl5.setBounds(220,420,40,30);
		this.add(jl5);
		
		jl6=new JLabel("avr��");
		jl6.setBounds(580,20,100,30);
		this.add(jl6);
		
		jl7=new JLabel("qvr��");
		jl7.setBounds(580,50,100,30);
		this.add(jl7);
		
		jl8=new JLabel("d    ��");
		jl8.setBounds(580,80,100,30);
		this.add(jl8);
		
		jlQ=new JLabel[Algorithm.m];
		for(int i=1;i<=Algorithm.m;i++)
		{
			String s="S"+String.valueOf(i)+"��";
			jlQ[i-1]=new JLabel(s);
			jlQ[i-1].setBounds(580,100+i*30,100,30);
			this.add(jlQ[i-1]);
		}
		
		jtf1=new JTextField(20);
		jtf1.setBounds(100, 400, 80, 30);
		jtf1.setText("-2");                             //����Ĭ��ֵ
		this.add(jtf1);
		
		jtf2=new JTextField(20);
		jtf2.setBounds(100, 430, 80, 30);
		jtf2.setText("0");
		this.add(jtf2);
		
		jtf3=new JTextField(20);
		jtf3.setBounds(100, 460, 80, 30);
		jtf3.setText("4");
		this.add(jtf3);
		
		jtf4=new JTextField(20);
		jtf4.setBounds(100, 370, 80, 30);
		jtf4.setText("0.74");
		this.add(jtf4);
		
		jCon=new JButton("��ʼ");
		jCon.setBounds(220,450,70,30);
		this.add(jCon);
		jCon.addActionListener(this);
		
		jStop=new JButton("��ͣ");
		jStop.setBounds(300,450,70,30);
		this.add(jStop);
		jStop.addActionListener(this);
		
		jCancel=new JButton("�˳�");
		jCancel.setBounds(380,450,70,30);
		this.add(jCancel);
		jCancel.addActionListener(this);
		
		jrbs1=new JRadioButton("ԭʼ");            //����JRadioButton��ѡ��
		jrbs2=new JRadioButton("�Ľ�");            //����JRadioButton��ѡ��
		this.bg=new ButtonGroup();                //����ButtonGroup��ť��
		bg.add(jrbs1);                            //����ѡ��Ž���ť��
		bg.add(jrbs2);
		jrbs1.setSelected(true);                  //����Ĭ��
		jrbs1.setBounds(300,420,70,30);
		jrbs2.setBounds(380,420,70,30);
		this.add(jrbs1);
		this.add(jrbs2);
		jrbs1.addActionListener(this);
		jrbs2.addActionListener(this);
		
		//�ղ���
		this.setLayout(null);
		this.setTitle("MKDNN");
		//��ʹ�����¿�
//		this.setUndecorated(true);
		this.setSize(700,530);
		this.setLocation(0,0);
		this.setVisible(true);
	}
	
	public void paint(Graphics g)                       //����JPanel��paint��������Ļ��ʾʱ���Զ�����һ�Σ�Graphics�ǻ�ͼ����Ҫ�࣬�൱��һֻ����
	{
		//���ø��ຯ����ɳ�ʼ��   
		
		super.paint(g);                                 //������,���ã���ա���ʼ��������
		this.draw(g);
		if(this.at!=null&&this.at.move==false)          //���ƶ���ʱ��
		{
			this.kmeanstest(g);
		}
	}
	
	public void draw(Graphics g)
	{
		
		double [][]p;                                   //�ص�����
		p=pd.p;
		g.setColor(Color.black);
		g.fillRect(this.x-this.lx/2, this.y-this.d/2, this.lx, 1); //·
		g.fillRect(this.x-this.lx/2, this.y+this.d/2, this.lx, 1);
		g.fillRect(this.x-this.d/2, this.y-this.ly/2, 1, this.ly);
		g.fillRect(this.x+this.d/2, this.y-this.ly/2, 1, this.ly);
		for(int i=1;i<p.length;i++)                     //���еص�
		{
			int x1=(int) (p[i][0]+this.x-this.ld/2);
			int y1=(int) (this.y-p[i][1]-this.ld/2);
			g.fillRect(x1, y1, this.ld, this.ld);
		}
		g.setColor(Color.red);
		a[0]=0;
		for(int j=0;j<this.a.length;j++)                //���ڵص�Ͷ���
		{
			int i=a[j];
			int x1=(int) (p[i][0]+this.x-this.ld/2);
			int y1=(int) (this.y-p[i][1]-this.ld/2);
			g.fillRect(x1, y1, this.ld, this.ld);
		}
		g.setColor(Color.black);
	}
	
	public void refresh()                                        //ˢ��avr��qvr��d��S���
	{
		String avr="avr��"+String.valueOf(this.at.avr);
		String qvr="qvr��"+String.valueOf(this.at.qvr);
		String d="d    ��"+String.valueOf(this.at.d);
		this.jl6.setText(avr);
		this.jl7.setText(qvr);
		this.jl8.setText(d);
		int i=1;
		for(i=1;i<=this.at.Q.size();i++)
		{
			String s="S"+String.valueOf(i)+"��";
			for(int j=0;j<Publicdata.k;j++)
			{
				s=s+String.valueOf(this.at.Q.get(i-1).a[j])+",";
			}
			s = s.substring(0,s.length()-1);                     //ɾ�����ġ�����
			jlQ[i-1].setText(s);
		}
		for(;i<=Algorithm.m;i++)
		{
			String s="S"+String.valueOf(i)+"��";
			jlQ[i-1].setText(s);
		}
	}
	
	public void kmeanstest(Graphics g)
	{
		if(this.at!=null)
		{
			if(this.at.u!=null&&this.at.u.isEmpty()!=true)               //������
			{
				for(int i=0;i<this.at.u.size();i++)
				{
					switch(i)
					{
					case 0:g.setColor(Color.black);break;
					case 1:g.setColor(Color.red);break;
					case 2:g.setColor(Color.yellow);break;
					case 3:g.setColor(Color.blue);break;
					}
					double[] p=this.at.u.get(i);
					int x=(int) (p[0]+this.x-this.ld/2);
					int y=(int) (this.y-p[1]-this.ld/2);
					g.fillOval(x, y, this.ld+5, this.ld+5);
				}
			}
			if(this.at.fl!=null&&this.at.fl.isEmpty()!=true)              //�������
			{
				for(int i=0;i<this.at.fl.size();i++)
				{
					switch(i)
					{
					case 0:g.setColor(Color.black);break;
					case 1:g.setColor(Color.red);break;
					case 2:g.setColor(Color.yellow);break;
					case 3:g.setColor(Color.blue);break;
					}
					if(this.at.fl.get(i).isEmpty()!=true)
					{
						for(int j=0;j<this.at.fl.get(i).size();j++)
						{
							int dd=this.at.fl.get(i).get(j);
							double[] p1=this.pd.p[dd];
							int x1=(int) (p1[0]+this.x-this.ld/2);
							int y1=(int) (this.y-p1[1]-this.ld/2);
							g.fillRect(x1, y1, this.ld, this.ld);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ee) {
		// TODO Auto-generated method stub
		if(ee.getSource()==jCancel)       //�˳�
		{
			this.dispose();
			System.exit(0);
		}else if(ee.getSource()==jCon)    //��ʼ
		{
			String Vx1=this.jtf1.getText().trim();
			String Vy1=this.jtf2.getText().trim();
			String k1=this.jtf3.getText().trim();
			String r1=this.jtf4.getText().trim();
			double[] v=new double[]{Double.valueOf(Vx1),Double.valueOf(Vy1)};
			int k=Integer.parseInt(k1);
			double r=Double.valueOf(r1);
			Publicdata.v=v;
			Publicdata.k=k;
			Publicdata.r=r;
			if(this.jrbs1.isSelected())   //���ݰ�ť����ģʽ
			{
				this.ms=1;
			}else if(this.jrbs2.isSelected())
			{
				this.ms=2;
			}
			if(this.t!=null)
			{
				this.t.stop();
				this.t1.stop();
			}
			this.t=new Thread(this);
			at=new Algorithm(this.ms);
			this.t1=new Thread(at);
			this.t.start();
			this.t1.start();
			this.jStop.setText("��ͣ");
		}else if(ee.getSource()==jStop)   //��ͣ�ͻָ�
		{

			if(this.t!=null)
			{
				if(this.jStop.getText().equals("��ͣ"))
				{
					this.t.suspend();
					this.t1.suspend();
					this.jStop.setText("����");
				}else if(this.jStop.getText().equals("����"))
				{
					this.t.resume();
					this.t1.resume();
					this.jStop.setText("��ͣ");
				}
			}
		}else if(ee.getSource()==jrbs1)
		{
			this.jtf4.setEditable(true);
			this.jtf4.setVisible(true);
			this.jl4.setVisible(true);
		}else if(ee.getSource()==jrbs2)
		{
			this.jtf4.setEditable(false);
			this.jtf4.setVisible(false);
			this.jl4.setVisible(false);
		}
	}	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
			this.refresh();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		View view1=new View();

	}
}
