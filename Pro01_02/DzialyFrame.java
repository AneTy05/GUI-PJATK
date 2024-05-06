import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DzialyFrame extends JPanel {
    JTable dzialyTable;
    public DzialyFrame() {
        DzialPracownikow.wszystkieDzialy = (ArrayList<DzialPracownikow>) DataDeserializer.deserializeDataDzialy("dzialyPracownikow.ser");
        DzialPracownikow.counter = DzialPracownikow.wszystkieDzialy.get(DzialPracownikow.wszystkieDzialy.size() - 1).id + 1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwa");
        model.addColumn("ID");

        for (DzialPracownikow dzial : DzialPracownikow.wszystkieDzialy) {
            Object[] row = {dzial.nazwa, dzial.id};
            model.addRow(row);
        }

        dzialyTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dzialyTable);
        ListSelectionModel selectionModel = dzialyTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = dzialyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = dzialyTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    DzialPracownikow.wszystkieDzialy.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = dzialyTable.getSelectedRowCount();
                deleteButton.setVisible(selectedRowCount > 1);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.NORTH);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Usuń");

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = dzialyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    DzialPracownikow.wszystkieDzialy.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dzialyTable.getSelectedRow();
                if (selectedRow != -1) {
                    DzialPracownikow wybranyDzial = DzialPracownikow.wszystkieDzialy.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    String nowaNazwaDzialu = JOptionPane.showInputDialog(DzialyFrame.this, "Wprowadź nazwę działu:");
                    wybranyDzial.nazwa = nowaNazwaDzialu;
                    DzialPracownikow.wszystkieDzialy.remove(selectedRow);
                    DzialPracownikow.wszystkieDzialy.add(selectedRow, wybranyDzial);
                    model.setValueAt(nowaNazwaDzialu, selectedRow,0);
                    }
                }
        });

        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        dzialyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = dzialyTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < dzialyTable.getRowCount()) {
                        dzialyTable.setRowSelectionInterval(row, row);
                        popupMenu.show(dzialyTable, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton nowyDzial = new JButton("Dodaj dział");
        buttonPanel.add(nowyDzial);

        nowyDzial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nazwaDzialu = JOptionPane.showInputDialog(DzialyFrame.this, "Wprowadź nazwę działu:");
                if (nazwaDzialu != null) {
                    try {
                        DzialPracownikow nowyDzial = DzialPracownikow.createDzial(nazwaDzialu);
                        Object[] row = {nowyDzial.nazwa, nowyDzial.id};
                        model.addRow(row);
                        int newRowIdx = model.getRowCount() - 1;
                        dzialyTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                        dzialyTable.scrollRectToVisible(dzialyTable.getCellRect(newRowIdx, 0, true));
                    } catch (NotUniqueNameException exception) {
                        JOptionPane.showMessageDialog(null, "Nazwa działu nie jest unikalna!");
                    }
                } else return;
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
