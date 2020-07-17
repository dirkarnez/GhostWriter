package com.dirkarnez.ghostwriter.utils;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import java.awt.Toolkit;

public class ClipBoardManager implements ClipboardOwner{
	
    private Clipboard clipboard;

    public ClipBoardManager() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public void setClipBoardContents(String str) {
        StringSelection contents = new StringSelection(str);
        clipboard.setContents(contents, this);
    }
    
    public String getClipBoardContents() {
        Transferable content = clipboard.getContents(this);
        
        try{
            return (String) content.getTransferData(DataFlavor.stringFlavor);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
	}
}
