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

public class SpecjalistaFrame extends JPanel {
    public SpecjalistaFrame() {
        Specjalista.allSpecjalisci = (ArrayList<Specjalista>) DataDeserializer.deserializeDataSpecjalisci("specjalisci.ser");
        Specjalista.counter = Specjalista.allSpecjalisci.get(Specjalista.allSpecjalisci.size() - 1).id + 1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwisko");
        model.addColumn("Imię");
        model.addColumn("Id");
        model.addColumn("Data urodzenia");
        model.addColumn("Dział");
        model.addColumn("Specjalizacja");

        for (Specjalista specjalista : Specjalista.allSpecjalisci) {
            Object[] row = {specjalista.nazwisko, specjalista.imie, specjalista.id, specjalista.dataUrodzenia, specjalista.dzial, specjalista.specjalizacja};
            model.addRow(row);
        }

        JTable specjalisciTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(specjalisciTable);
        ListSelectionModel selectionModel = specjalisciTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = specjalisciTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = specjalisciTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    Specjalista.allSpecjalisci.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = specjalisciTable.getSelectedRowCount();
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
                int[] selectedRows = specjalisciTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Specjalista.allSpecjalisci.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = specjalisciTable.getSelectedRow();
                if (selectedRow != -1) {
                    Specjalista specjalista = Specjalista.allSpecjalisci.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JTextField imieField = new JTextField(specjalista.imie, 20);
                    JTextField nazwiskoField = new JTextField(specjalista.nazwisko, 20);
                    JTextField dataUrodzeniaField = new JTextField(specjalista.dataUrodzenia.toString(), 10);
                    JTextField specjalizacjaField = new JTextField(specjalista.specjalizacja, 20);
                    JComboBox<String> dzialCBox = new JComboBox<>();
                    dzialCBox.insertItemAt("", 0);
                    for (DzialPracownikow dzialPrac : DzialPracownikow.wszystkieDzialy) {
                        dzialCBox.addItem(dzialPrac.nazwa);
                    }
                    dzialCBox.setSelectedItem(specjalista.dzial.nazwa);

                    panel.add(new JLabel("Imię:"));
                    panel.add(imieField);
                    panel.add(new JLabel("Nazwisko:"));
                    panel.add(nazwiskoField);
                    panel.add(new JLabel("Data urodzenia (RRRR-MM-DD):"));
                    panel.add(dataUrodzeniaField);
                    panel.add(new JLabel("Dział pracowników:"));
                    panel.add(dzialCBox);
                    panel.add(new JLabel("Specjalizacja:"));
                    panel.add(specjalizacjaField);

                    int result = JOptionPane.showConfirmDialog(SpecjalistaFrame.this, panel, "Edytuj specjalistę",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String imie = imieField.getText();
                        String nazwisko = nazwiskoField.getText();
                        LocalDate dataUrodzenia = null;
                        try {
                            dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                        } catch (DateTimeParseException exception) {
                            JOptionPane.showMessageDialog(SpecjalistaFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                            return;
                        }
                        String specjalizacja = specjalizacjaField.getText();
                        String wybranyDzial = (String) dzialCBox.getSelectedItem();

                        DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                                .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                                .findFirst()
                                .orElse(null);

                        specjalista.imie = imie;
                        specjalista.nazwisko = nazwisko;
                        specjalista.dataUrodzenia = dataUrodzenia;
                        specjalista.dzial = dzialNowego;
                        specjalista.specjalizacja = specjalizacja;
                        Specjalista.allSpecjalisci.remove(selectedRow);
                        Specjalista.allSpecjalisci.add(selectedRow, specjalista);

                        model.setValueAt(nazwisko, selectedRow, 0);
                        model.setValueAt(imie, selectedRow, 1);
                        model.setValueAt(dataUrodzenia, selectedRow, 3);
                        model.setValueAt(dzialNowego, selectedRow, 4);
                        model.setValueAt(specjalizacja, selectedRow, 5);
                    }

                }
            }
        });

        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        specjalisciTable.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = specjalisciTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < specjalisciTable.getRowCount()) {
                    specjalisciTable.setRowSelectionInterval(row, row);
                    popupMenu.show(scrollPane, e.getX(), e.getY());
                }
            }
        }
    });

        JButton nowySpecjalista = new JButton("Dodaj specjalistę");
        buttonPanel.add(nowySpecjalista);
        nowySpecjalista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField imieField = new JTextField(20);
                JTextField nazwiskoField = new JTextField(20);
                JTextField dataUrodzeniaField = new JTextField(10);
                JTextField specjalizacjaField = new JTextField(20);
                JComboBox<String> dzialCBox = new JComboBox<>();
                dzialCBox.insertItemAt("", 0);
                for (DzialPracownikow dzialPrac : DzialPracownikow.wszystkieDzialy) {
                    dzialCBox.addItem(dzialPrac.nazwa);
                }
                panel.add(new JLabel("Imię:"));
                panel.add(imieField);
                panel.add(new JLabel("Nazwisko:"));
                panel.add(nazwiskoField);
                panel.add(new JLabel("Data urodzenia (RRRR-MM-DD):"));
                panel.add(dataUrodzeniaField);
                panel.add(new JLabel("Dział pracowników:"));
                panel.add(dzialCBox);
                panel.add(new JLabel("Specjalizacja:"));
                panel.add(specjalizacjaField);

                int result = JOptionPane.showConfirmDialog(SpecjalistaFrame.this, panel, "Dodaj specjalistę",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String imie = imieField.getText();
                    String nazwisko = nazwiskoField.getText();
                    LocalDate dataUrodzenia = null;
                    try {
                        dataUrodzenia = LocalDate.parse(dataUrodzeniaField.getText());
                    } catch (DateTimeParseException exception) {
                        JOptionPane.showMessageDialog(SpecjalistaFrame.this, "Błędny format daty! Użyj formatu RRRR-MM-DD.");
                        return;
                    }
                    String specjalizacja = specjalizacjaField.getText();
                    String wybranyDzial = (String) dzialCBox.getSelectedItem();

                    DzialPracownikow dzialNowego = DzialPracownikow.wszystkieDzialy.stream()
                            .filter(dzial -> dzial.nazwa.equals(wybranyDzial))
                            .findFirst()
                            .orElse(null);
                    Specjalista specjalista = new Specjalista(imie, nazwisko, dataUrodzenia, dzialNowego, specjalizacja);
                    Object[] row = {specjalista.nazwisko, specjalista.imie, specjalista.id, specjalista.dataUrodzenia, specjalista.dzial, specjalista.specjalizacja};
                    model.addRow(row);
                    int newRowIdx = model.getRowCount() - 1;
                    specjalisciTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                    specjalisciTable.scrollRectToVisible(specjalisciTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
