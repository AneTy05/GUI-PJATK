import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BrygadaFrame extends JPanel {
    public BrygadaFrame() {
        Brygada.allBrygady = DataDeserializer.deserializeDataBrygady("brygady.ser");
        Brygada.counter = Brygada.allBrygady.get(Brygada.allBrygady.size() - 1).id + 1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwa");
        model.addColumn("Id");
        model.addColumn("Brygadzista");

        for (Brygada brygada : Brygada.allBrygady) {
            Object[] row = {brygada.nazwa, brygada.id, brygada.brygadzista.imie + " " + brygada.brygadzista.nazwisko};
            model.addRow(row);
        }

        JTable brygadyTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(brygadyTable);
        ListSelectionModel selectionModel = brygadyTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = brygadyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = brygadyTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    Brygada.allBrygady.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = brygadyTable.getSelectedRowCount();
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
                int[] selectedRows = brygadyTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Brygada.allBrygady.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = brygadyTable.getSelectedRow();
                if (selectedRow != -1) {
                    Brygada brygada = Brygada.allBrygady.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JTextField nazwaField = new JTextField(brygada.nazwa, 20);
                    JComboBox<String> brygadzistaCBox = new JComboBox<>();
                    brygadzistaCBox.insertItemAt("", 0);
                    for (Brygadzista brygadzista : Brygadzista.allBrygadzisci) {
                        brygadzistaCBox.addItem(brygadzista.nazwisko);
                    }
                    brygadzistaCBox.setSelectedItem(brygada.brygadzista.nazwisko);

                    panel.add(new JLabel("Nazwa brygady:"));
                    panel.add(nazwaField);
                    panel.add(new JLabel("Brygadzista:"));
                    panel.add(brygadzistaCBox);

                    int result = JOptionPane.showConfirmDialog(BrygadaFrame.this, panel, "Edytuj brygadę",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String nazwa = nazwaField.getText();
                        String wybranyBrygadzista = (String) brygadzistaCBox.getSelectedItem();
                        Brygadzista brygadzista = Brygadzista.allBrygadzisci.stream()
                                .filter(brygad -> brygad.nazwisko.equals(wybranyBrygadzista))
                                .findFirst()
                                .orElse(null);

                        brygada.nazwa = nazwa;
                        brygada.brygadzista = brygadzista;
                        Brygada.allBrygady.remove(selectedRow);
                        Brygada.allBrygady.add(selectedRow, brygada);

                        model.setValueAt(nazwa, selectedRow, 0);
                        model.setValueAt(brygadzista.imie + " " + brygadzista.nazwisko, selectedRow, 2);
                    }
                }
            }
        });

        JMenuItem dodPracownikaMenuItem = new JMenuItem("Dodaj pracownika");
        dodPracownikaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = brygadyTable.getSelectedRow();
                if (selectedRow != -1) {
                    Brygada brygada = Brygada.allBrygady.get(selectedRow);

                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JComboBox<String> pracownikComboBox = new JComboBox<>();
                    for (Specjalista pracownik : Specjalista.allSpecjalisci) {
                        pracownikComboBox.addItem(pracownik.nazwisko);
                    }
                    panel.add(new JLabel("Wybierz pracownika:"));
                    panel.add(pracownikComboBox);

                    int result = JOptionPane.showConfirmDialog(BrygadaFrame.this, panel, "Dodaj pracownika",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String selectedPracownik = (String) pracownikComboBox.getSelectedItem();
                        Specjalista pracownik = Specjalista.allSpecjalisci.stream()
                                .filter(prac -> prac.nazwisko.equals(selectedPracownik))
                                .findFirst()
                                .orElse(null);;
                        if (pracownik != null) {
                            brygada.add(pracownik);
                            Brygada.allBrygady.add(selectedRow, brygada);
                        }
                    }
                }
            }
        });

        JMenuItem showPracownicy = new JMenuItem("Pokaż pracowników");
        showPracownicy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = brygadyTable.getSelectedRow();
                if (selectedRow != -1) {
                    Brygada brygada = Brygada.allBrygady.get(selectedRow);
                    List<Pracownik> pracownicy = brygada.pracownicy;

                    Object[][] rowData = new Object[pracownicy.size()][3];
                    for (int i = 0; i < pracownicy.size(); i++) {
                        Pracownik pracownik = pracownicy.get(i);
                        rowData[i][0] = pracownik.id;
                        rowData[i][1] = pracownik.imie;
                        rowData[i][2] = pracownik.nazwisko;
                    }

                    Object[] columnNames = {"ID", "Imię", "Nazwisko"};

                    JTable pracownicyTable = new JTable(rowData, columnNames);
                    JScrollPane scrollPane = new JScrollPane(pracownicyTable);

                    JOptionPane.showMessageDialog(BrygadaFrame.this, scrollPane, "Pracownicy w brygadzie: " + brygada.nazwa,
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(BrygadaFrame.this, "Nie wybrano żadnej brygady.", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        popupMenu.add(showPracownicy);
        popupMenu.add(dodPracownikaMenuItem);
        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        brygadyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = brygadyTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < brygadyTable.getRowCount()) {
                        brygadyTable.setRowSelectionInterval(row, row);
                        popupMenu.show(brygadyTable, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton nowaBrygada = new JButton("Dodaj brygadę");
        buttonPanel.add(nowaBrygada);
        nowaBrygada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JTextField nazwa = new JTextField(20);
                JComboBox<String> brygadzistaCBox = new JComboBox<>();
                brygadzistaCBox.insertItemAt("", 0);
                for (Brygadzista brygadzista : Brygadzista.allBrygadzisci) {
                    brygadzistaCBox.addItem(brygadzista.nazwisko);
                }
                panel.add(new JLabel("Nazwa brygady:"));
                panel.add(nazwa);
                panel.add(new JLabel("Brygadzista"));
                panel.add(brygadzistaCBox);

                int result = JOptionPane.showConfirmDialog(BrygadaFrame.this, panel, "Dodaj brygadę",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String brygadaNazwa = nazwa.getText();
                    String wybranyBrygadzista = (String) brygadzistaCBox.getSelectedItem();
                    Brygadzista brygadzista = Brygadzista.allBrygadzisci.stream()
                            .filter(brygad -> brygad.nazwisko.equals(wybranyBrygadzista))
                            .findFirst()
                            .orElse(null);
                    Brygada nowaBrygada = new Brygada(brygadaNazwa, brygadzista);
                    Object[] newRow = {nowaBrygada.nazwa, nowaBrygada.id, nowaBrygada.brygadzista.imie + " " + nowaBrygada.brygadzista.nazwisko};
                    model.addRow(newRow);
                    int newRowIdx = model.getRowCount() - 1;
                    brygadyTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                    brygadyTable.scrollRectToVisible(brygadyTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
