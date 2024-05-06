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
import java.util.List;

public class PracaFrame extends JPanel {
    public PracaFrame() {
        Praca.praceForFrame = (ArrayList<Praca>) DataDeserializer.deserializeDataPrace("prace.ser");
        Praca.counter = Praca.praceForFrame.get(Praca.praceForFrame.size() - 1).id + 1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Rodzaj pracy");
        model.addColumn("Czas pracy");
        model.addColumn("Opis");

        for (Praca praca : Praca.praceForFrame) {
            Object[] row = {praca.id, praca.rPracy, praca.czasPracy, praca.opis};
            model.addRow(row);
        }

        JTable praceTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(praceTable);
        ListSelectionModel selectionModel = praceTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = praceTable.getSelectedRows();
                List<Integer> rowsToRemove = new ArrayList<>();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = praceTable.convertRowIndexToModel(selectedRows[i]);
                    rowsToRemove.add(selectedRow);
                }
                for (int row : rowsToRemove) {
                    model.removeRow(row);
                    Praca.praceForFrame.remove(row);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.NORTH);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = praceTable.getSelectedRowCount();
                deleteButton.setVisible(selectedRowCount > 1);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Usuń");

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutaj umieść kod usuwający zaznaczone elementy
                int[] selectedRows = praceTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Praca.praceForFrame.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = praceTable.getSelectedRow();
                if (selectedRow != -1) {
                    Praca selectedPraca = Praca.praceForFrame.get(selectedRow);
                    Praca.RodzajPracy rodzajPracy = (Praca.RodzajPracy) praceTable.getValueAt(selectedRow, 1);
                    int czasPracy = (int) praceTable.getValueAt(selectedRow, 2);
                    String opis = (String) praceTable.getValueAt(selectedRow, 3);

                    JComboBox<Praca.RodzajPracy> rodzajPracyCBox2 = new JComboBox<>();
                    for (Praca.RodzajPracy rodzajPracy2 : Praca.RodzajPracy.values()) {
                        rodzajPracyCBox2.addItem(rodzajPracy2);
                    }
                    rodzajPracyCBox2.setSelectedItem(rodzajPracy);
                    JSpinner czasPracySpinner = new JSpinner(new SpinnerNumberModel(czasPracy, 0, Integer.MAX_VALUE, 1));
                    JTextField opisField = new JTextField(opis);

                    JPanel panel = new JPanel(new GridLayout(3, 2));
                    panel.add(new JLabel("Rodzaj pracy:"));
                    panel.add(rodzajPracyCBox2);
                    panel.add(new JLabel("Czas pracy:"));
                    panel.add(czasPracySpinner);
                    panel.add(new JLabel("Opis:"));
                    panel.add(opisField);

                    int result = JOptionPane.showConfirmDialog(PracaFrame.this, panel, "Edytuj pracę",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        Praca.RodzajPracy updatedRodzajPracy = (Praca.RodzajPracy) rodzajPracyCBox2.getSelectedItem();
                        int updatedCzasPracy = (int) czasPracySpinner.getValue();
                        String updatedOpis = opisField.getText();
                        selectedPraca.opis = updatedOpis;
                        selectedPraca.rPracy = updatedRodzajPracy;
                        selectedPraca.czasPracy = updatedCzasPracy;
                        Praca.praceForFrame.remove(selectedRow);
                        Praca.praceForFrame.add(selectedRow, selectedPraca);
                        model.setValueAt(updatedRodzajPracy, selectedRow, 1);
                        model.setValueAt(updatedCzasPracy, selectedRow, 2);
                        model.setValueAt(updatedOpis, selectedRow, 3);
                    }
                }
            }
        });
        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        praceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = praceTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < praceTable.getRowCount()) {
                        praceTable.setRowSelectionInterval(row, row);
                        popupMenu.show(praceTable, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton nowaPraca = new JButton("Dodaj pracę");
        buttonPanel.add(nowaPraca);

        nowaPraca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JComboBox<Praca.RodzajPracy> rodzajPracyCBox = new JComboBox<>();
                rodzajPracyCBox.insertItemAt(null, 0);
                for (Praca.RodzajPracy rodzajPracy : Praca.RodzajPracy.values()) {
                    rodzajPracyCBox.addItem(rodzajPracy);
                }
                SpinnerModel czasPracyModel = new SpinnerNumberModel(0, 0, 60, 1);
                JSpinner czasPracySpinner = new JSpinner(czasPracyModel);
                JSpinner.DefaultEditor spinnerEditor = new JSpinner.DefaultEditor(czasPracySpinner);
                JFormattedTextField textField = spinnerEditor.getTextField();
                textField.setHorizontalAlignment(JTextField.LEFT);
                czasPracySpinner.setEditor(spinnerEditor);
                JTextField opis = new JTextField(20);
                panel.add(new JLabel("Rodzaj pracy:"));
                panel.add(rodzajPracyCBox);
                panel.add(new JLabel("Czas potrzebny na wykonanie pracy:"));
                panel.add(czasPracySpinner);
                panel.add(new JLabel("Opis:"));
                panel.add(opis);

                int result = JOptionPane.showConfirmDialog(PracaFrame.this, panel, "Dodaj pracę",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    Praca.RodzajPracy rPracy = (Praca.RodzajPracy) rodzajPracyCBox.getSelectedItem();
                    int czasPracy = (int) czasPracySpinner.getValue();
                    String krotkiOpis = opis.getText();
                    Praca nowaPraca = new Praca(rPracy, czasPracy, krotkiOpis);
                    Object[] row = {nowaPraca.id, nowaPraca.rPracy, nowaPraca.czasPracy, nowaPraca.opis};
                    model.addRow(row);
                    int newRowIdx = model.getRowCount() - 1;
                    praceTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                    praceTable.scrollRectToVisible(praceTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
