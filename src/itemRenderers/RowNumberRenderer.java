package itemRenderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class RowNumberRenderer extends DefaultTableCellRenderer
{
	public RowNumberRenderer()
	{
		setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (table != null)
		{
			JTableHeader header = table.getTableHeader();

			if (header != null)
			{
				setForeground(header.getForeground());
				setBackground(header.getBackground());
				setFont(header.getFont());
			}
		}

		if (isSelected)
		{
			setFont( getFont().deriveFont(Font.BOLD) );
		}

		setText(String.valueOf(row + 1));
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		return this;
	}
}