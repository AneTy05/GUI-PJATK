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

public class UzytkownikFrame extends JPanel {
    public UzytkownikFrame() {
        Uzytkownik.allUzytkownicy = (ArrayList<Uzytkownik>) DataDeserializer.deserializeDataUzytkownicy("uzytkownicy.ser");
        Uzytkownik.counter = Uzytkownik.allUzytkownicy.get(Uzytkownik.allUzytkownicy.size() - 1).id +1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwisko");
        model.addColumn("Imię");
        model.addColumn("Id");
        model.addColumn("Data urodzenia");
        model.addColumn("Dział");

        for (Uzytkownik uzytkownik : Uzytkownik.allUzytkownicy) {
            Object[] row = {uzytkownik.nazwisko, uzytkownik.imie, uzytkownik.id, uzytkownik.dataUrodzenia, uzytkownik.dzial};
            model.addRow(row);
        }

        JTable uzytkownicyTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(uzytkownicyTable);
        ListSelectionModel selectionModel = uzytkownicyTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = uzytkownicyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = uzytkownicyTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    Uzytkownik.allUzytkownicy.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = uzytkownicyTable.getSelectedRowCount();
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
                // Tutaj umieść kod usuwający zaznaczone elementy
                int[] selectedRows = uzytkownicyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Uzytkownik.allUzytkownicy.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = uzytkownicyTable.getSelectedRow();
                if (selectedRow != -1) {
                    Uzytkownik uzytkownik = Uzytkownik.allUzytkownicy.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JTextField imieField = new JTextField(uzytkownik.imie, 20);
                    JTextField nazwiskoField = new JTextField(uzytkownik.nazwisko, 20);
                    JTextField dataUrodzeniaField = new JTextField(uzytkownik.dataUrodzenia.toString(), 10);
                    JComboBox<String> dzialCBox = new JComboBox<>();
                    dzialCBox.insertItemAt("", 0);
                    for (DzialPracownikow dzialPrac : DzialPracownikow.wszystkieDzialy) {
                        dzialCBox.addItem(dzialPrac.nazwa);
                    }
                    dzialCBox.setSelectedItem(uzytkownik.dzial.nazwa);

                    panel.add(new JLabel("Imię:"));
                    panel.add(imieField);
                    panel.add(new JLabel("Nazwisko:"));
                    panel.add(nazwiskoField);
                    panel.add(new JLabel("Data urodzenia (RRRR-MM-DD):"));
                    panel.add(dataUrodzeniaField);
                    panel.add(new JLabel("Dział pracowników:"));
                    panel.add(dzialCBox);

                    int result = JOptionPane.showConfirmDialog(UzytkownikFrame.this, panel, "Edytuj specjalistę",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String imie = imieField.getText();
                        String nazwisko = nazwiskoField.getText();
                        LocalDate dataUrodzenia = null;
                        try {
                            dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                        } catch (DateTimeParseException exception) {
                            JOptionPane.showMessageDialog(UzytkownikFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                            return;
                        }
                        String wybranyDzial = (String) dzialCBox.getSelectedItem();

                        DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                                .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                                .findFirst()
                                .orElse(null);

                        uzytkownik.imie = imie;
                        uzytkownik.nazwisko = nazwisko;
                        uzytkownik.dataUrodzenia = dataUrodzenia;
                        uzytkownik.dzial = dzialNowego;
                        Uzytkownik.allUzytkownicy.remove(selectedRow);
                        Uzytkownik.allUzytkownicy.add(selectedRow, uzytkownik);

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

        uzytkownicyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = uzytkownicyTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < uzytkownicyTable.getRowCount()) {
                        uzytkownicyTable.setRowSelectionInterval(row, row);
                        popupMenu.show(scrollPane, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton nowyUzytkownik = new JButton("Dodaj użytkownika");
        buttonPanel.add(nowyUzytkownik);
        nowyUzytkownik.addActionListener(new ActionListener() {
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

                int result = JOptionPane.showConfirmDialog(UzytkownikFrame.this, panel, "Dodaj użytkownika",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String imie = imieField.getText();
                    String nazwisko = nazwiskoField.getText();
                    LocalDate dataUrodzenia = null;
                    try {
                        dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                    } catch (DateTimeParseException exception) {
                        JOptionPane.showMessageDialog(UzytkownikFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                        return;
                    }
                    String wybranyDzial = (String) dzialCBox.getSelectedItem();
                    DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                            .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                            .findFirst()
                            .orElse(null);
                    String login = nazwaUzytkownika.getText();
                    String hasloStr = haslo.getText();
                    Uzytkownik uzytkownik = new Uzytkownik(imie, nazwisko, dataUrodzenia, dzialNowego, login, hasloStr);
                    Object[] row = {uzytkownik.nazwisko, uzytkownik.imie, uzytkownik.id, uzytkownik.dataUrodzenia,
                            uzytkownik.dzial};
                    model.addRow(row);
                    int newRowIdx = model.getRowCount() - 1;
                    uzytkownicyTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                    uzytkownicyTable.scrollRectToVisible(uzytkownicyTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
