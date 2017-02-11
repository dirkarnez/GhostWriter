package main;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeConstants;

import views.TextSelectionPanel;

public class Main extends JFrame {
	
	public static final String SNIPPETS_FOLDER_PATH = System.getProperty("user.dir") + "/Snippets/";
	public static File SNIPPETS_FOLDER;
	
	private String os = System.getProperty("os.name");
	
	public Main() {
		
		if (os.indexOf("Win") >= 0){
			initializeHotkeys();
		}
		
		initializeDirectory();
		
		JOptionPane.showMessageDialog(this, "Loaded GhostWriter.\nLoad text from .txt file to system clipboard using Ctrl + <file name>,\nnaming from 1.txt to 9.txt.\nCtrl + Shift + X to close.");
	}
	
	private void initializeDirectory() {
		SNIPPETS_FOLDER = new File(SNIPPETS_FOLDER_PATH);
		
		if(!SNIPPETS_FOLDER.exists()) {
			SNIPPETS_FOLDER.mkdirs();
		}
	}

	private void initializeHotkeys()
	{
		if (!JIntellitype.isJIntellitypeSupported()) {
	         System.exit(1);
		}
		
		JIntellitype keyhook = JIntellitype.getInstance();
		
		keyhook.registerHotKey(0, JIntellitypeConstants.MOD_CONTROL
				+ JIntellitypeConstants.MOD_SHIFT, 'X');
		
		keyhook.registerHotKey(1, JIntellitypeConstants.MOD_ALT, 'C');
		
		
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener()
		{

			@Override
			public void onHotKey(int identifier)
			{
				if (identifier == 1) {
					//new TextSelectionPanel(String.valueOf(identifier));
					
			        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	new TextSelectionPanel();
			            }
			        });
				} else if (identifier == 0) {
					JOptionPane.showMessageDialog(Main.this, "Exit GhostWriter.");
					System.exit(0);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		try {
            // Set System L&F
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }

		new Main();
	}
}
