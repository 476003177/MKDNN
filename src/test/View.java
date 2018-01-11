package test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class View extends JFrame implements ActionListener,Runnable{
	
	JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7,jl8,jlQ[];
	JTextField jtf1,jtf2,jtf3,jtf4;
	JButton jCon,jStop,jCancel;
	JRadioButton jrbs1,jrbs2;                          //定义单选框
	ButtonGroup bg;                                    //定义ButtonGroup按钮组
	Publicdata pd=new Publicdata();
	Algorithm at;
	int x=220,y=220;                                   //坐标原点的绝对坐标
	int d=20;                                          //两平行线的距离
	int lx=400,ly=340;                                 //横纵线长
	int ld=18;                                         //地点边长
	int ms=1;                                          //运行模式，1为原式，2为改进
	static int[]a;
	Thread t;
	Thread t1;
	
	public View()
	{
		this.a=new int[Publicdata.k+1];
		
		//创建各个组件
		jl1=new JLabel("Vx：");
		jl1.setBounds(60, 400, 40, 30);
		this.add(jl1);
		
		jl2=new JLabel("Vy：");
		jl2.setBounds(60, 430, 40, 30);
		this.add(jl2);
		
		jl3=new JLabel("k：");
		jl3.setBounds(60, 460, 40, 30);
		this.add(jl3);
		
		jl4=new JLabel("λ：");
		jl4.setBounds(60, 370, 40, 30);
		this.add(jl4);
		
		jl5=new JLabel("模式：");
		jl5.setBounds(220,420,40,30);
		this.add(jl5);
		
		jl6=new JLabel("avr：");
		jl6.setBounds(580,20,100,30);
		this.add(jl6);
		
		jl7=new JLabel("qvr：");
		jl7.setBounds(580,50,100,30);
		this.add(jl7);
		
		jl8=new JLabel("d    ：");
		jl8.setBounds(580,80,100,30);
		this.add(jl8);
		
		jlQ=new JLabel[Algorithm.m];
		for(int i=1;i<=Algorithm.m;i++)
		{
			String s="S"+String.valueOf(i)+"：";
			jlQ[i-1]=new JLabel(s);
			jlQ[i-1].setBounds(580,100+i*30,100,30);
			this.add(jlQ[i-1]);
		}
		
		jtf1=new JTextField(20);
		jtf1.setBounds(100, 400, 80, 30);
		jtf1.setText("-2");                             //设置默认值
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
		
		jCon=new JButton("开始");
		jCon.setBounds(220,450,70,30);
		this.add(jCon);
		jCon.addActionListener(this);
		
		jStop=new JButton("暂停");
		jStop.setBounds(300,450,70,30);
		this.add(jStop);
		jStop.addActionListener(this);
		
		jCancel=new JButton("退出");
		jCancel.setBounds(380,450,70,30);
		this.add(jCancel);
		jCancel.addActionListener(this);
		
		jrbs1=new JRadioButton("原始");            //创建JRadioButton单选框
		jrbs2=new JRadioButton("改进");            //创建JRadioButton单选框
		this.bg=new ButtonGroup();                //创建ButtonGroup按钮组
		bg.add(jrbs1);                            //将单选框放进按钮组
		bg.add(jrbs2);
		jrbs1.setSelected(true);                  //设置默认
		jrbs1.setBounds(300,420,70,30);
		jrbs2.setBounds(380,420,70,30);
		this.add(jrbs1);
		this.add(jrbs2);
		jrbs1.addActionListener(this);
		jrbs2.addActionListener(this);
		
		//空布局
		this.setLayout(null);
		this.setTitle("MKDNN");
		//不使用上下框
//		this.setUndecorated(true);
		this.setSize(700,530);
		this.setLocation(0,0);
		this.setVisible(true);
	}
	
	public void paint(Graphics g)                       //覆盖JPanel的paint方法，屏幕显示时候自动调用一次，Graphics是绘图的重要类，相当于一只画笔
	{
		//调用父类函数完成初始化   
		
		super.paint(g);                                 //不能少,作用：清空、初始化、重置
		this.draw(g);
		if(this.at!=null&&this.at.move==false)          //不移动的时候画
		{
			this.kmeanstest(g);
		}
	}
	
	public void draw(Graphics g)
	{
		
		double [][]p;                                   //地点坐标
		p=pd.p;
		g.setColor(Color.black);
		g.fillRect(this.x-this.lx/2, this.y-this.d/2, this.lx, 1); //路
		g.fillRect(this.x-this.lx/2, this.y+this.d/2, this.lx, 1);
		g.fillRect(this.x-this.d/2, this.y-this.ly/2, 1, this.ly);
		g.fillRect(this.x+this.d/2, this.y-this.ly/2, 1, this.ly);
		for(int i=1;i<p.length;i++)                     //所有地点
		{
			int x1=(int) (p[i][0]+this.x-this.ld/2);
			int y1=(int) (this.y-p[i][1]-this.ld/2);
			g.fillRect(x1, y1, this.ld, this.ld);
		}
		g.setColor(Color.red);
		a[0]=0;
		for(int j=0;j<this.a.length;j++)                //近邻地点和动点
		{
			int i=a[j];
			int x1=(int) (p[i][0]+this.x-this.ld/2);
			int y1=(int) (this.y-p[i][1]-this.ld/2);
			g.fillRect(x1, y1, this.ld, this.ld);
		}
		g.setColor(Color.black);
	}
	
	public void refresh()                                        //刷新avr、qvr、d和S组合
	{
		String avr="avr："+String.valueOf(this.at.avr);
		String qvr="qvr："+String.valueOf(this.at.qvr);
		String d="d    ："+String.valueOf(this.at.d);
		this.jl6.setText(avr);
		this.jl7.setText(qvr);
		this.jl8.setText(d);
		int i=1;
		for(i=1;i<=this.at.Q.size();i++)
		{
			String s="S"+String.valueOf(i)+"：";
			for(int j=0;j<Publicdata.k;j++)
			{
				s=s+String.valueOf(this.at.Q.get(i-1).a[j])+",";
			}
			s = s.substring(0,s.length()-1);                     //删除最后的“，”
			jlQ[i-1].setText(s);
		}
		for(;i<=Algorithm.m;i++)
		{
			String s="S"+String.valueOf(i)+"：";
			jlQ[i-1].setText(s);
		}
	}
	
	public void kmeanstest(Graphics g)
	{
		if(this.at!=null)
		{
			if(this.at.u!=null&&this.at.u.isEmpty()!=true)               //画中心
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
			if(this.at.fl!=null&&this.at.fl.isEmpty()!=true)              //画分类点
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
		if(ee.getSource()==jCancel)       //退出
		{
			this.dispose();
			System.exit(0);
		}else if(ee.getSource()==jCon)    //开始
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
			if(this.jrbs1.isSelected())   //根据按钮设置模式
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
			this.jStop.setText("暂停");
		}else if(ee.getSource()==jStop)   //暂停和恢复
		{

			if(this.t!=null)
			{
				if(this.jStop.getText().equals("暂停"))
				{
					this.t.suspend();
					this.t1.suspend();
					this.jStop.setText("继续");
				}else if(this.jStop.getText().equals("继续"))
				{
					this.t.resume();
					this.t1.resume();
					this.jStop.setText("暂停");
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
