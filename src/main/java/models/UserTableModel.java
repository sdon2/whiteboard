package models;


import javax.swing.table.AbstractTableModel;

/**
 * Table Model to represent connect users. One model for board.
 */
public class UserTableModel extends AbstractTableModel{
    private static final long serialVersionUID = 8305718477610260762L;
    private String[] columnNames = {"Users"};      
    private Object[][] data = new Object[0][0];
    
    /**
     * Returns column count
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * Returns number of rows
     */
    public int getRowCount() {
        return data.length;
    }
    
    /**
     * Returns column name
     * @param int columnNumber
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * Returns value at the row and col specified
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Sets value at row and col in table
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
  
    /**
     * Updates the data by changing it to be the newData passed in
     * @param newData
     */
    public void updateData(Object[][] newData) {
        data = newData;
        fireTableDataChanged();
    }

}