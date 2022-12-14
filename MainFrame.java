package testfi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import javax.swing.table.DefaultTableModel;

//interface for cafe menu list
public class MainFrame extends JFrame {
	
	//object serialization
	private static final long seriaVersionUID = 1L;
			
	//Set variable name
	String tmp, menu, sign2;
	DefaultTableModel model;
	int price, realsum;
	
	JPanel Mainpanel, Orderpanel, Labelpanel, btnpanel, Optionpanel, Menupanel;
	JTextArea display;
	JTable displayTable;
	JScrollPane scroll;
	JLabel l_order, l_sum, l_realsum;
	JTable T_tmp;
	JButton btn_order, btn_cancel, btn_quit;
	JButton[][] btn_menu;
	JRadioButton[] tmp_Radio;
	ButtonGroup tmp_group, menu_group;
	File file;
	
	String header[] = {"Menu", "Category", "Price"};
	String contents[][] = {};
	
	String[] label_list = {"Order", "Total"};
	String[] menu_name = {"Latte", "Milk", "Mocha", "Ice cream", "tea"};
	String[] temp_name = {"Hot", "Medium", "Cold"};
	
	int[][] price_list = price_init(menu_name.length, temp_name.length);
	int[][] order_list = order_init(menu_name.length, temp_name.length);
	
	public MainFrame() {
		
		this.setSize(800,400); //size set
		this.setResizable(false);
		
		Mainpanel = new JPanel();
		Mainpanel.setPreferredSize(new Dimension(800,400));
		Mainpanel.setLayout(new GridLayout(1,2,8,8));  //gridlayout size
		
		Orderpanel = new JPanel();
		Orderpanel.setPreferredSize(new Dimension(400, 400));
		Orderpanel.setLayout(new FlowLayout());
		
		display = new JTextArea(16, 16);
	  	display.setEditable(false);
	  	scroll = new JScrollPane(display);
	        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	        scroll.setPreferredSize(new Dimension(380,270));
	        Orderpanel.add(scroll);
		
		Labelpanel = new JPanel();
		
		l_order = new JLabel("Total");
		l_order.setPreferredSize(new Dimension(185,15));
		l_order.setOpaque(true);
		l_order.setBackground(Color.white);
		Labelpanel.add(l_order);
		
		l_realsum = new JLabel("0");
		l_realsum.setPreferredSize(new Dimension(185,15));
		l_realsum.setOpaque(true);
		l_realsum.setBackground(Color.white);
		Labelpanel.add(l_realsum);
		
		Orderpanel.add(Labelpanel);
				
		btnpanel = new JPanel();
		btnpanel.setLayout(new GridLayout(1,3,5,5));
		
		btn_order = new JButton("Order");
		btn_order.addActionListener(new ActionHandler1());
		btn_order.setPreferredSize(new Dimension(123,50));
		btnpanel.add(btn_order);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.addActionListener(new ActionHandler1());
		btn_cancel.setPreferredSize(new Dimension(123,50));
		btnpanel.add(btn_cancel);
		
		btn_quit = new JButton("Exit");
		btn_quit.addActionListener(new ActionHandler1());
		btn_quit.setPreferredSize(new Dimension(123,50));
		btnpanel.add(btn_quit);
		
		Orderpanel.add(btnpanel);
		
		Menupanel = new JPanel();
		Menupanel.setLayout(new FlowLayout());
		
		Optionpanel = new JPanel();
		Optionpanel.setPreferredSize(new Dimension(250,30));
		Optionpanel.setLayout(new GridLayout(0,3));
		
		tmp_Radio = new JRadioButton[3]; //change
		tmp_group = new ButtonGroup();
		
		for(int i = 0; i<tmp_Radio.length; i++) {
			tmp_Radio[i] = new JRadioButton(temp_name[i]);
			tmp_group.add(tmp_Radio[i]);
			tmp_Radio[i].addItemListener(new ItemHandler());
			Optionpanel.add(tmp_Radio[i]);
		}
		tmp_Radio[2].setSelected(true);
		tmp = "Cold";
		
		Menupanel.add(Optionpanel);
		
		int menurow = 5;
		int menucol = 4;
		MenuGrid = new JPanel();
		MenuGrid.setLayout(new GridLayout(menurow,menucol,5,5));
		
		btn_menu = new JButton[menurow][menucol];
		for(int row=0; row < menurow; row++) {
			for(int col=0; col < menucol; col++) {
				if((row * menucol + col + 1) <= menu_name.length) {
					int i = (row * menucol + col);
					btn_menu[row][col] = new JButton(menu_name[i]);
					String menu = menu_name[i];
					btn_menu[row][col].addActionListener(new ActionHandler2());
					btn_menu[row][col].setPreferredSize(new Dimension(90,58));
					MenuGrid.add(btn_menu[row][col]);
				} else {
					btn_menu[row][col] = new JButton("");
					String menu = "";
					btn_menu[row][col].setPreferredSize(new Dimension(90,58));
					MenuGrid.add(btn_menu[row][col]);
				}
			}
		}
		Menupanel.add(MenuGrid);
			
		Mainpanel.add(Orderpanel);
		Mainpanel.add(Menupanel);
		this.add(Mainpanel);
	}
		
	public int[][] price_init(int product_num, int temp) {
		int[][] price_list = new int[product_num][temp];
		
		for (int row = 0; row < product_num; row++) {
			for (int col = 0; col < temp; col++) {
				price_list[row][col] = 0; // initialize every price to 0 won
			}
		}
		
		//                          Hot   Med  Cold
		price_list[0] = new int[] {2000, 3000, 4000}; // Latte
		price_list[1] = new int[] {3000, 4000, 5000}; // Milk
		price_list[2] = new int[] {4000, 5000, 6000}; // Mocha
		price_list[3] = new int[] {5000, 6000, 7000}; // ice cream
		price_list[4] = new int[] {6000, 7000, 8000}; // tea
		
		return price_list;
	}
	
	public int[][] order_init(int product_num, int temp){
		int[][] order_list = new int[product_num][temp];
		
		for (int row = 0; row < product_num; row++) {
			for ( int col = 0; col < temp; col++) {
				order_list[row][col] = 0;
			}
		}
		
		return order_list;
	}
	
	public void order() {
		File file = new File("data/myorderlist");
		try {
			FileWriter fw = new FileWriter(file,true);
			
			fw.write("\n==========================\n");
			
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());
			fw.write(formatter.format(date) + "\n\n");
			
			int numberOfMenu = 0;
			for (int row = 0; row < menu_name.length; row++) {
				for (int col = 0; col < temp_name.length; col++) {
					if(order_list[row][col] > 0) {
						numberOfMenu += order_list[row][col];
						fw.write(String.format("%2d %6s %10s ", order_list[row][col], temp_name[col], menu_name[row]));
						fw.write(String.format("%10d\n", price_list[row][col] * order_list[row][col]));
					}
				}
			}
			
			fw.write(String.format("\nTotal %d 's order %s", numberOfMenu, l_realsum.getText()));
			fw.write("\n==========================\n");
			fw.flush();
			JOptionPane.showMessageDialog(null, "Order Success.");
			
			this.setVisible(false);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Order Fail.");
			e.printStackTrace();
			
		}
		
	}
	
	public void cancel() {
		order_list = order_init(menu_name.length, temp_name.length);
		l_realsum.setText("0");
	}
	
	public class ActionHandler1 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String sign1 = e.getActionCommand();
			
			if(sign1.equals("Order")) {
				order();
			}
			
			else if (sign1.equals("Cancel")) {
				cancel();
			}
			else if (sign1.equals("Exit")) {
				System.exit(0);
			}
			
		}
	}

	public class ActionHandler2 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
//          							Hot   Med  Cold
//			price_list[0] = new int[] {3000, 3000, 3000}; // Latte
//			price_list[1] = new int[] {3000, 3000, 3000}; // Milk
//			price_list[2] = new int[] {3000, 3000, 3000}; // Mocha
//			price_list[3] = new int[] {3000, 3000, 3000}; // ice cream
//			price_list[4] = new int[] {3000, 3000, 3000}; // tea
			
			sign2 = new String(e.getActionCommand());
			
			String input[] = new String[3];
			int temp_col = 0;
			if (tmp.equals("Hot")) {
				temp_col = 0;
			}else if(tmp.equals("Medium")) {
				temp_col = 1;
			}else if(tmp.equals("Cold")) {
				temp_col = 2;
			}
			
			if (sign2.equals("Latte")) {
				order_list[0][temp_col]++;
			}
			else if (sign2.equals("Milk")) {
				order_list[1][temp_col]++;
			}
			else if (sign2.equals("Mocha")) {
				order_list[2][temp_col]++;
			}
			else if (sign2.equals("ice cream")) {
				order_list[3][temp_col]++;
			}
			else if (sign2.equals("tea")) {
				order_list[4][temp_col]++;
			}
			
			int total = 0;

			for (int row = 0; row < menu_name.length; row++) {
				for (int col = 0; col < temp_name.length; col++) {
					total += price_list[row][col] * order_list[row][col];
				}
			}

			
			l_realsum.setText(Integer.toString( total ));
			}
		}
	
	

	public class ItemHandler implements ItemListener{

		
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==ItemEvent.DESELECTED) {
				return;
				
			}
			if(tmp_Radio[0].isSelected()) {
				tmp = new String("Hot");
			}
			else if (tmp_Radio[1].isSelected()) {
				tmp = new String("Medium"); //change
			}
			else if (tmp_Radio[2].isSelected()) {
				tmp = new String("Cold");
				
			   }

			}
		 
		}
	
	}
