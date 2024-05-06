import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class BrygadzistaFrame extends JPanel {
    public BrygadzistaFrame() {
        Brygadzista.allBrygadzisci = (ArrayList<Brygadzista>) DataDeserializer.deserializeDataBrygadzisci("brygadzisci.ser");
        Brygadzista.counter = Brygadzista.allBrygadzisci.get(Brygadzista.allBrygadzisci.size()-1).id +1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwisko");
        model.addColumn("Imię");
        model.addColumn("Id");
        model.addColumn("Data urodzenia");
        model.addColumn("Dział");

        for (Brygadzista brygadzista : Brygadzista.allBrygadzisci) {
            Object[] row = {brygadzista.nazwisko, brygadzista.imie, brygadzista.id, brygadzista.dataUrodzenia, brygadzista.dzial};
            model.addRow(row);
        }

        JTable brygadzisciTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(brygadzisciTable);
        ListSelectionModel selectionModel = brygadzisciTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = brygadzisciTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = brygadzisciTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    Brygadzista.allBrygadzisci.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = brygadzisciTable.getSelectedRowCount();
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
                int[] selectedRows = brygadzisciTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Brygadzista.allBrygadzisci.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = brygadzisciTable.getSelectedRow();
                if (selectedRow != -1) {
                    Brygadzista brygadzista = Brygadzista.allBrygadzisci.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JTextField imieField = new JTextField(brygadzista.imie, 20);
                    JTextField nazwiskoField = new JTextField(brygadzista.nazwisko, 20);
                    JTextField dataUrodzeniaField = new JTextField(brygadzista.dataUrodzenia.toString(), 10);
                    JComboBox<String> dzialCBox = new JComboBox<>();
                    dzialCBox.insertItemAt("", 0);
                    for (DzialPracownikow dzialPrac : DzialPracownikow.wszystkieDzialy) {
                        dzialCBox.addItem(dzialPrac.nazwa);
                    }
                    dzialCBox.setSelectedItem(brygadzista.dzial.nazwa);

                    panel.add(new JLabel("Imię:"));
                    panel.add(imieField);
                    panel.add(new JLabel("Nazwisko:"));
                    panel.add(nazwiskoField);
                    panel.add(new JLabel("Data urodzenia (RRRR-MM-DD):"));
                    panel.add(dataUrodzeniaField);
                    panel.add(new JLabel("Dział pracowników:"));
                    panel.add(dzialCBox);

                    int result = JOptionPane.showConfirmDialog(BrygadzistaFrame.this, panel, "Edytuj specjalistę",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String imie = imieField.getText();
                        String nazwisko = nazwiskoField.getText();
                        LocalDate dataUrodzenia = null;
                        try {
                            dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                        } catch (DateTimeParseException exception) {
                            JOptionPane.showMessageDialog(BrygadzistaFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                            return;
                        }
                        String wybranyDzial = (String) dzialCBox.getSelectedItem();

                        DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                                .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                                .findFirst()
                                .orElse(null);

                        brygadzista.imie = imie;
                        brygadzista.nazwisko = nazwisko;
                        brygadzista.dataUrodzenia = dataUrodzenia;
                        brygadzista.dzial = dzialNowego;
                        Brygadzista.allBrygadzisci.remove(selectedRow);
                        Brygadzista.allBrygadzisci.add(selectedRow, brygadzista);

                        model.setValueAt(nazwisko, selectedRow, 0);
                        model.setValueAt(imie, selectedRow, 1);
                        model.setValueAt(dataUrodzenia, selectedRow, 3);
                        model.setValueAt(dzialNowego, selectedRow, 4);
                    }

                }
            }
        });

        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        brygadzisciTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = brygadzisciTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < brygadzisciTable.getRowCount()) {
                        brygadzisciTable.setRowSelectionInterval(row, row);
                        popupMenu.show(scrollPane, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton nowyBrygadzista = new JButton("Dodaj brygadzistę");
        buttonPanel.add(nowyBrygadzista);
        nowyBrygadzista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField imieField = new JTextField(20);
                JTextField nazwiskoField = new JTextField(20);
                JTextField dataUrodzeniaField = new JTextField(10);
                JComboBox<String> dzialCBox = new JComboBox<>();
                dzialCBox.insertItemAt("", 0);
                for (DzialPracownikow dzialPrac : DzialPracownikow.wszystkieDzialy) {
                    dzialCBox.addItem(dzialPrac.nazwa);
                }
                JTextField nazwaUzytkownika = new JTextField(20);
                JTextField haslo = new JTextField(20);
                panel.add(new JLabel("Imię:"));
                panel.add(imieField);
                panel.add(new JLabel("Nazwisko:"));
                panel.add(nazwiskoField);
                panel.add(new JLabel("Data urodzenia (RRRR-MM-DD):"));
                panel.add(dataUrodzeniaField);
                panel.add(new JLabel("Dział pracowników:"));
                panel.add(dzialCBox);
                panel.add(new JLabel("Login:"));
                panel.add(nazwaUzytkownika);
                panel.add(new JLabel("Hasło"));
                panel.add(haslo);

                int result = JOptionPane.showConfirmDialog(BrygadzistaFrame.this, panel, "Dodaj brygadzistę",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String imie = imieField.getText();
                    String nazwisko = nazwiskoField.getText();
                    LocalDate dataUrodzenia = null;
                    try {
                        dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                    } catch (DateTimeParseException exception) {
                        JOptionPane.showMessageDialog(BrygadzistaFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                        return;
                    }
                    String wybranyDzial = (String) dzialCBox.getSelectedItem();
                    DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                            .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                            .findFirst()
                            .orElse(null);
                    String login = nazwaUzytkownika.getText();
                    String hasloStr = haslo.getText();
                    Brygadzista brygadzista = new Brygadzista(imie, nazwisko, dataUrodzenia, dzialNowego, login, hasloStr);
                    Object[] row = {brygadzista.nazwisko, brygadzista.imie, brygadzista.id, brygadzista.dataUrodzenia,
                            brygadzista.dzial};
                    model.addRow(row);
                    int newRowIdx = model.getRowCount() - 1;
                    brygadzisciTable.setRowSelectionInterval(newRowIdx,newRowIdx);
                    brygadzisciTable.scrollRectToVisible(brygadzisciTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
