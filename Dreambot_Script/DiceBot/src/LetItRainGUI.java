import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.wrappers.items.Item;

public class LetItRainGUI extends JFrame {
	private JFrame frame;
	public LetItRain _main;
	public LetItRainTask _taskClass;
	public Map<String, Integer> history = new HashMap();
	public JList<String> list = new JList();
	public JList<String> listBL = new JList();

	public ArrayList<String> list2;

	public JTextField RollforName;

	public JTextField TransferName;

	public JTextField TransferAmount;

	public JTextField MinText;

	public JTextField MaxText;

	public JTextField TraderName;

	public JTextField TradingAmount;

	public JTextField BlTF;

	public JTextField ProfitTxt;

	public static void main(String[] args) {
	}

	public LetItRainGUI(LetItRain mainClass, LetItRainTask taskClass) {
		_main = mainClass;
		_taskClass = taskClass;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 485);
		frame.setDefaultCloseOperation(2);
		list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				RollforName.setText((String) list2.get(list.locationToIndex(arg0.getPoint())));
			}
		});
		list.setBackground(Color.LIGHT_GRAY);
		list.setSelectionMode(0);
		list.setVisibleRowCount(10);
		list.setFixedCellHeight(15);
		list.setFixedCellWidth(100);
		listBL.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				BlTF.setText((String) _main.blackLists.get(listBL.locationToIndex(arg0.getPoint())));
			}

		});
		JScrollPane scrollPane1 = new JScrollPane(list);
		JScrollPane scrollPane2 = new JScrollPane(listBL);

		scrollPane1.setVerticalScrollBarPolicy(22);
		scrollPane2.setVerticalScrollBarPolicy(22);

		listBL.setBackground(Color.LIGHT_GRAY);
		listBL.setSelectionMode(0);
		listBL.setVisibleRowCount(10);
		listBL.setFixedCellHeight(15);
		listBL.setFixedCellWidth(100);

		JLabel lblRollFor = new JLabel("Roll for :");

		RollforName = new JTextField();
		RollforName.setColumns(10);

		MinText = new JTextField();
		MinText.setColumns(10);

		MaxText = new JTextField();
		MaxText.setColumns(10);

		JButton ChangeMaxMin = new JButton("Change Coins");
		ChangeMaxMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_taskClass.setMaxBet(MaxText.getText());
				_taskClass.setMinBet(MinText.getText());
			}

		});
		JButton btnRoll = new JButton("Roll");
		btnRoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_main.logging("Rolling for " + RollforName.getText());
				String text = HelperClass.rollphaser("Lose", RollforName.getText(), HelperClass.randLose());
				_main.instantType(text);
			}

		});
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// _main.savestatus = _main.Status;
				_taskClass.setStatus("Pause");
			}

		});
		JButton btnResume = new JButton("Resume");
		btnResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_taskClass.setStatus("Open");
			}

		});
		JLabel lblTransfer = new JLabel("Transfer");

		JLabel lblTrader = new JLabel("Trader Name:");
		TraderName = new JTextField();
		TraderName.setEditable(false);
		TraderName.setColumns(10);

		JLabel lblTradingAmount = new JLabel("Amount:");
		TradingAmount = new JTextField();

		TradingAmount.setEditable(false);
		TradingAmount.setColumns(10);

		JLabel lblBL = new JLabel("Black List:");
		BlTF = new JTextField();
		BlTF.setColumns(10);

		TransferName = new JTextField();
		TransferName.setColumns(10);

		JLabel lblAmount = new JLabel("Amount");

		TransferAmount = new JTextField();
		TransferAmount.setColumns(10);

		ProfitTxt = new JTextField();
		ProfitTxt.setEditable(false);
		ProfitTxt.setColumns(10);

		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_taskClass.setStatus("Close");
				_main.logging("Transfering " + TransferName.getText() + " to " + TransferAmount.getText());
				_main.OverridePlayerName = TransferName.getText();
				_main.OverrideTransferAmount = Integer.parseInt(TransferAmount.getText());
				_main.OverrideTrade = true;		
//				_taskClass.setStatus("Open");
			}

		});
		JButton btnAll = new JButton("All");
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_taskClass.setStatus("Close");
				_main.logging("Transfering " + _main.getInvCash() + " to " + TransferAmount.getText());
				_main.OverridePlayerName = TransferName.getText();
				_main.OverrideTransferAmount = _main.getInvCash();
				_main.OverrideTrade = true;		
//				_taskClass.setStatus("Open");
			}

		});
		JRadioButton rdbtnWin = new JRadioButton("Win");
		rdbtnWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_taskClass.setMaxRoll(100);
				_taskClass.setMinRoll(55);
			}

		});
		JRadioButton rdbtnLose = new JRadioButton("Lose");
		rdbtnLose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_taskClass.setMaxRoll(54);
				_taskClass.setMinRoll(1);
			}

		});
		JRadioButton rdbtnNormal = new JRadioButton("Normal");
		rdbtnNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_taskClass.setMaxRoll(100);
				_taskClass.setMinRoll(1);
			}

		});
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnWin);
		group.add(rdbtnLose);
		group.add(rdbtnNormal);

		JLabel lblNewLabel = new JLabel("Min Coin");
		JLabel lblNewLabel2 = new JLabel("Max Coin");

		JButton btnAdsver = new JButton("Advertise");
		btnAdsver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_main.advertise();
			}

		});
		JButton btnAddToBl = new JButton("Add to BL");
		btnAddToBl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_main.blackLists.add(BlTF.getText());
				setListBL();
			}

		});
		JButton btnRemoveFromBl = new JButton("Remove From BL");
		btnRemoveFromBl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_main.blackLists.remove(BlTF.getText());
				setListBL();
			}

		});
		JLabel lblTotalAmount = new JLabel("Profit");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTrader)
										.addComponent(lblTradingAmount)
										.addComponent(lblTotalAmount))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(TradingAmount, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
										.addComponent(TraderName, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
										.addComponent(ProfitTxt, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(8)
											.addComponent(lblNewLabel))
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(4)
											.addComponent(lblNewLabel2)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(MaxText, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
										.addComponent(MinText, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(rdbtnWin)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(rdbtnLose)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(rdbtnNormal))
								.addComponent(ChangeMaxMin, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addComponent(btnPause)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnResume))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblTransfer)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(TransferName))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblAmount)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(TransferAmount))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(btnTransfer)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnAll)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnAdsver))))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnRoll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblRollFor)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(RollforName, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblBL)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(BlTF))
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnAddToBl, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnRemoveFromBl))))
					.addGap(118))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRollFor)
						.addComponent(RollforName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRoll)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnWin)
						.addComponent(rdbtnLose)
						.addComponent(rdbtnNormal))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(MaxText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(MinText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ChangeMaxMin)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTrader)
						.addComponent(TraderName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTradingAmount)
						.addComponent(TradingAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ProfitTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTotalAmount))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBL)
						.addComponent(BlTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddToBl)
						.addComponent(btnRemoveFromBl))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblTransfer)
						.addComponent(TransferName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAmount)
						.addComponent(TransferAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(3)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnTransfer)
						.addComponent(btnAll)
						.addComponent(btnAdsver))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnResume)
						.addComponent(btnPause))
					.addGap(57))
		);

		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}

	public void setListBL() {
		DefaultListModel<String> templist = new DefaultListModel();
		for (String entry : _main.blackLists) {
			templist.addElement(entry);
		}
		listBL.setModel(templist);
	}

	public void AddEntry(String name, int Amount) {
		if (!history.containsKey(name)) {
			history.put(name, Integer.valueOf(Amount));
		} else {
			int cash = ((Integer) history.get(name)).intValue();
			history.put(name, Integer.valueOf(cash + Amount));
		}
		setList();
	}

	public void setList() {
		DefaultListModel<String> templist = new DefaultListModel();
		list2 = new ArrayList();

		for (Map.Entry<String, Integer> entry : HelperClass.sortByComparator(history).entrySet()) {
			list2.add((String) entry.getKey());
			templist.addElement((String) entry.getKey() + ": " + entry.getValue());
		}
		list.setModel(templist);
	}

	public void Displose() {
		Displose();
	}
}
