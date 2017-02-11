package views;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import itemRenderers.MultiLineCellRenderer;
import itemRenderers.RowNumberRenderer;
import main.Main;
import models.SnippetsModel;
import utils.ClipBoardManager;

public class TextSelectionPanel extends JFrame {

    private JLabel searchLabel;
    private JPanel criteriaPanel;
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private JTable table;
    private JTextField searchTextField;
    
	private TableRowSorter<SnippetsModel> sorter;
    
	private ClipBoardManager clipBoardManager;
	private static final int lines = 4;


    public TextSelectionPanel() {
    	super();
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				formkeyTyped(evt);
			}
		});
		
		clipBoardManager = new ClipBoardManager();
		
        initComponents();
    }

    private void initComponents() {

        tablePanel = new JPanel();
		SnippetsModel model = new SnippetsModel(indexFilesToModel());
		sorter = new TableRowSorter<SnippetsModel>(model);
		table = new JTable(model);
		table.setRowHeight(table.getRowHeight() * lines);

		table.setPreferredScrollableViewportSize(getPreferredSize());

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		table.addColumn(column);
		column.setCellRenderer(new RowNumberRenderer());
		table.moveColumn(table.getColumnCount() - 1, 0);

		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());

		table.setRowSorter(sorter);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		//For the purposes of this example, better to have a single
		//selection.
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//When selection changes, provide user with row numbers for
		//both view and model.
		table.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent event) {
					int viewRow = table.getSelectedRow();
                    if (viewRow > -1) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        clipBoardManager.setClipBoardContents(table.getModel().getValueAt(modelRow, 0).toString());
                        dispose();
                    }
				}
			}
		);
		
    	tableScrollPane = new JScrollPane();
        
        searchLabel = new JLabel();
        criteriaPanel = new JPanel();
        searchTextField = new JTextField();
        searchTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) {
				TextSelectionPanel.this.formkeyTyped(keyEvent);
			}
		}); 
        
        searchTextField.getDocument().addDocumentListener(
			new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					newFilter();
				}
				public void insertUpdate(DocumentEvent e) {
					newFilter();
				}
				public void removeUpdate(DocumentEvent e) {
					newFilter();
				}
			}
		);
		
        tableScrollPane.setViewportView(table);

        searchLabel.setText("Search");

        GroupLayout criteriaPanelLayout = new GroupLayout(criteriaPanel);
        criteriaPanel.setLayout(criteriaPanelLayout);
        criteriaPanelLayout.setHorizontalGroup(
            criteriaPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, criteriaPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(searchLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchTextField)
                .addGap(20, 20, 20))
        );
        criteriaPanelLayout.setVerticalGroup(
            criteriaPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(criteriaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(criteriaPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchLabel))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout tablePanelLayout = new GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(tableScrollPane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(criteriaPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScrollPane, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(criteriaPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
		this.setSize(600, 600);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				TextSelectionPanel.this.focus();
			}
		});
    }
    
    public void formkeyTyped(KeyEvent keyEvent) {
		int idx = -1;
		if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
			idx = 0;
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_0 || 
				keyEvent.getKeyCode() == KeyEvent.VK_1 || 
				keyEvent.getKeyCode() == KeyEvent.VK_2 || 
				keyEvent.getKeyCode() == KeyEvent.VK_3 || 
				keyEvent.getKeyCode() == KeyEvent.VK_4 || 
				keyEvent.getKeyCode() == KeyEvent.VK_5 ||
				keyEvent.getKeyCode() == KeyEvent.VK_6 || 
				keyEvent.getKeyCode() == KeyEvent.VK_7 || 
				keyEvent.getKeyCode() == KeyEvent.VK_8 || 
				keyEvent.getKeyCode() == KeyEvent.VK_9) {

			idx = table.convertRowIndexToModel(convertKeyCode(keyEvent.getKeyCode()) - 1);
		} else {
			if (keyEvent.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
				keyEvent.consume();
			}
		}

		if(idx > -1) {
			clipBoardManager.setClipBoardContents(table.getModel().getValueAt(idx, 0).toString());
			dispose();
		}
	}

	private Object[][] indexFilesToModel() {
		Object[][] data = null;
		Collection<File> snippets = FileUtils.listFiles(Main.SNIPPETS_FOLDER, new String[]{"txt"}, false);
		if(snippets != null && snippets.size() > 0)
		{
			data = new Object[snippets.size()][];

			int idx = 0;
			for (Iterator<File> iterator = snippets.iterator(); iterator.hasNext();){
				File snippet = iterator.next();
				data[idx] = new Object[1];
				data[idx][0] = readTextFromFile(snippet.getName());
				idx++;
			}
		}
		return data;
	}

	private String readTextFromFile(String filename) {
		try {
			return IOUtils.toString(new FileInputStream(Main.SNIPPETS_FOLDER_PATH + "/" + filename), Charset.defaultCharset());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Snippet file not found.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "File system error.");
		}
		return null;
	}

	public void focus(){
		searchTextField.setFocusable(true);
		searchTextField.requestFocus();
		searchTextField.grabFocus();
		this.repaint();
	}
	
	private void newFilter() {
		RowFilter<SnippetsModel, Object> rf = null;
		String filteringText = searchTextField.getText().toLowerCase();
		//If current expression doesn't parse, don't update.
		try {
			rf = new RowFilter<SnippetsModel, Object>()
			{
				@Override
				public boolean include(Entry<? extends SnippetsModel, ? extends Object> entry)
				{
					String filteredField = entry.getStringValue(0).toLowerCase();
					if(filteredField.contains(filteringText))
					{
						return true;
					}
					return false;
				}
			};
			sorter.setRowFilter(rf);	
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
	}

	private int convertKeyCode(int keyCode) {
		switch (keyCode) {     
			case KeyEvent.VK_0: return 0;              
			case KeyEvent.VK_1: return 1;              
			case KeyEvent.VK_2: return 2;              
			case KeyEvent.VK_3: return 3;              
			case KeyEvent.VK_4: return 4;              
			case KeyEvent.VK_5: return 5;              
			case KeyEvent.VK_6: return 6;              
			case KeyEvent.VK_7: return 7;              
			case KeyEvent.VK_8: return 8;   
		}
		return -1;
	}
}
