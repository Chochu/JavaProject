package scripts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton updateButton;
	private JPanel panel, searchPanel;
	private TreePanel treePanel;
	private InfoPanel infoPanel;
	
	public GUI(){
		// START
		super("JJ's Interface Explorer");
		setSize(800, 500);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// NORTH
		searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(2, 2));
		
		add(searchPanel, BorderLayout.NORTH);
		
		// CENTER
		infoPanel = new InfoPanel();
		treePanel = new TreePanel(infoPanel);
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(treePanel);
		panel.add(infoPanel);
		add(panel, BorderLayout.CENTER);
		
		// SOUTH
		updateButton = new JButton("Update");
		updateButton.addActionListener(new UpdateListener());
		add(updateButton, BorderLayout.SOUTH);
	}
	
	public Rectangle getRSRect(){
		return infoPanel.getRSRect();
	}
	
	private class UpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			//System.out.println("UPDATE CLICKED");

			treePanel.createTree(true);
			panel.repaint();
			
			revalidate();
			invalidate();
			repaint();

		}
		
	}
	
	
}
