package spirit.fitness.scanner.sn;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.model.Containerbean;
import spirit.fitness.scanner.model.DailyShippingReportbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.SerialNoRecord;
import spirit.fitness.scanner.restful.HistoryRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.HistoryCallBackFunction;
import spirit.fitness.scanner.until.LoadingFrameHelper;

public class QuerySN {

	private JFrame frame;
	private JFrame recordFrame;
	private LoadingFrameHelper loadingframe;
	private JProgressBar loading;
	private static QuerySN query = null;
	private HistoryRepositoryImplRetrofit historyRepositoryImplRetrofit;
	
	public static QuerySN getInstance() {
		if (query == null) {
			query = new QuerySN();
		}
		return query;
	}

	public static boolean isExit() {
		return query != null;
	}

	public static void destory() {
		query = null;
	}

	public QuerySN() {
		//loadingframe = new LoadingFrameHelper("Loading Data from Server...");
		//loading = loadingframe.loadingSample("Loading Data from Server...");
		exceuteCallback();
		orderInfo();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuerySN window = new QuerySN();
					// QueryResult window = new QueryResult();
					// window.setContent(0, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void orderInfo() {

		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(700, 400);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame.add(panel);

		placeComponents(panel);

		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);
			}
		});

	}
	
	private void placeComponents(JPanel panel) {
		loadingframe = new LoadingFrameHelper("Loading data...");
		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);
		// Creating JLabel
		JLabel modelLabel = new JLabel("Serial number:");

		modelLabel.setBounds(50, 100, 200, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JTextField salesOrderNo = new JTextField(20);
		salesOrderNo.setText("");
		salesOrderNo.setFont(font);
		salesOrderNo.setBounds(230, 100, 320, 50);
		panel.add(salesOrderNo);

		// Creating Query button
		JButton QueryButton = new JButton("Find");
		QueryButton.setFont(font);
		QueryButton.setBounds(230, 180, 150, 50);
		QueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (salesOrderNo.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Please enter Sales Order Number.");
				else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {

								loadingframe = new LoadingFrameHelper("Loading Data from Server...");
								loading = loadingframe.loadingSample("Loading Data from Server...");

								String SN = salesOrderNo.getText().toString().trim();
								// querySalesOrder(salesOrder);
								getSNRecord(SN);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}

			}
		});

		panel.add(QueryButton);

		// Creating Query button
		JButton ResetButton = new JButton("Clear");
		ResetButton.setFont(font);
		ResetButton.setBounds(400, 180, 150, 50);
		ResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salesOrderNo.setText("");
			}
		});

		panel.add(ResetButton);

		// Creating Query button
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(font);
		exitButton.setBounds(230, 250, 320, 50);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				frame.dispose();
				frame.setVisible(false);
			}
		});

		panel.add(exitButton);
	}

	
	// Loading Models data from Server
		private void getSNRecord(String sn) {

			// loading model and location information from Server
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {

						historyRepositoryImplRetrofit.getItemsBySN(sn);
					} catch (Exception e) {
						e.printStackTrace();
						//NetWorkHandler.displayError(loadingframe);
					}
				}
			});

		}
		

		private void exceuteCallback() 
		{
			historyRepositoryImplRetrofit = new HistoryRepositoryImplRetrofit();
			historyRepositoryImplRetrofit.setHistoryServiceCallBackFunction(new HistoryCallBackFunction() {

				@Override
				public void resultCode(int code) {
					if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
						JOptionPane.showMessageDialog(null, "Items already exist.");

					}

				}

				@Override
				public void getHistoryItems(List<Historybean> _items) {

					// else
					//

				}

				@Override
				public void checkHistoryItemsBySalesOrder(List<Historybean> _items) {
				

				}

				@Override
				public void exception(String error) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void getDailyShippingItems(List<DailyShippingReportbean> items) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void getSerialNoRecord(List<SerialNoRecord> items) {
					loadingframe.setVisible(false);
					loadingframe.dispose();
					serialNoRecordList(items);
					
				}

			});
			
		}
		
		public void serialNoRecordList(List<SerialNoRecord> snList) {

			// JFrame.setDefaultLookAndFeelDecorated(false);
			// JDialog.setDefaultLookAndFeelDecorated(false);
			recordFrame = new JFrame("Query Pannel");
			// Setting the width and height of frame
			recordFrame.setSize(850, 600);
			recordFrame.setLocationRelativeTo(null);
			recordFrame.setLocationRelativeTo(null);
			recordFrame.setUndecorated(true);
			recordFrame.setResizable(false);

			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

			panel.setBackground(Constrant.BACKGROUN_COLOR);
			// adding panel to frame
			recordFrame.add(panel);

			placeRecordPanel(panel, snList);

			// frame.setUndecorated(true);
			// frame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
			recordFrame.setBackground(Color.WHITE);
			recordFrame.setVisible(true);
			// frame.setDefaultLookAndFeelDecorated(true);
			recordFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			recordFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {

					recordFrame.dispose();
					recordFrame.setVisible(false);
				}
			});

		
		

		}
		
		private void placeRecordPanel(JPanel panel, List<SerialNoRecord> snList) {

			panel.setLayout(null);

			Font font = new Font("Verdana", Font.BOLD, 18);

			JLabel modelLabel = new JLabel("Serial No : "+snList.get(0).SN);

			modelLabel.setBounds(30, 30, 800, 25);
			modelLabel.setFont(font);
			panel.add(modelLabel);
			// ScrollPane for Result
			JScrollPane scrollZonePane = new JScrollPane();

			scrollZonePane.setBackground(Constrant.TABLE_COLOR);

			if (!snList.isEmpty()) {

				

				int rowIndex = 0;

				final Object[][] containerItems = new Object[snList.size()][5];

				for (SerialNoRecord sn : snList) {

				
					for (int j = 0; j < 5; j++) {

						String date = sn.Date.substring(0, 10);
						
						containerItems[rowIndex][0] = sn.Status;

						containerItems[rowIndex][1] = date;
						
						containerItems[rowIndex][2] = sn.Location;
						
						containerItems[rowIndex][3] = sn.ContainNo;
						
						containerItems[rowIndex][4] = sn.SalesOrder;
					}

					rowIndex++;
				}

				final Class[] columnClass = new Class[] { String.class, String.class, String.class,String.class, String.class };

				Object columnNames[] = { "STATUS", "DATE", "LOCATION", "CONTAINNO","SO"};

				DefaultTableModel container = new DefaultTableModel(containerItems, columnNames) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}

					@Override
					public Class<?> getColumnClass(int columnIndex) {
						return columnClass[columnIndex];
					}
				};

				JTable containerTable = new JTable(container);
				containerTable.getTableHeader().setBackground(Constrant.TABLE_COLOR);
				containerTable.getTableHeader().setFont(font);

				containerTable.setBackground(Constrant.TABLE_COLOR);
				containerTable.setRowHeight(40);
				containerTable.setFont(font);

				DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
				leftRenderer.setHorizontalAlignment(JLabel.CENTER);
				TableColumn date = containerTable.getColumnModel().getColumn(0);
				date.setCellRenderer(leftRenderer);
				TableColumn no = containerTable.getColumnModel().getColumn(1);
				no.setPreferredWidth(50);
				no.setCellRenderer(leftRenderer);
				
				TableColumn model = containerTable.getColumnModel().getColumn(2);
				model.setCellRenderer(leftRenderer);
				model.setPreferredWidth(20);
				
				TableColumn qty = containerTable.getColumnModel().getColumn(3);
				qty.setCellRenderer(leftRenderer);
				qty.setPreferredWidth(50);

				int heigh = 0;

				if (50 * containerItems.length + 20 > 380)
					heigh = 380;
				else
					heigh = 50 * containerItems.length + 20;
				scrollZonePane.setBounds(33, 91, 800, heigh);
				scrollZonePane.setViewportView(containerTable);

				panel.add(scrollZonePane);
				
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						recordFrame.toFront();
						recordFrame.repaint();
					}
				});
				

				JButton add = new JButton(new AbstractAction("BACK") {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						recordFrame.setVisible(false);
						recordFrame.dispose();
					}
				});

				JButton edit = new JButton(new AbstractAction("FINISH") {

					@Override
					public void actionPerformed(ActionEvent e) {
						recordFrame.setVisible(false);
						recordFrame.dispose();
						frame.setVisible(false);
						frame.dispose();
						query = null;
						
					}
				});
				
				add.setFont(font);
				edit.setFont(font);
			
				add.setBounds(30, 520, 150, 50);
				edit.setBounds(200, 520, 150, 50);
			

				panel.add(add);
				panel.add(edit);

			}

			
		}
}
