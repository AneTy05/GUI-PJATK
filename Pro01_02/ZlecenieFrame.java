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

public class ZlecenieFrame extends JPanel {
    public ZlecenieFrame() {
        Zlecenie.zleceniaDlaFrame = (ArrayList<Zlecenie>) DataDeserializer.deserializeDataZlecenia("zlecenia.ser");
        Zlecenie.counter = Zlecenie.zleceniaDlaFrame.get(Zlecenie.zleceniaDlaFrame.size() - 1).id +1;
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Brygada");
        model.addColumn("Stan");
        model.addColumn("Data utworzenia");

        for (Zlecenie zlecenie : Zlecenie.zleceniaDlaFrame) {
            Object[] row = {zlecenie.id, zlecenie.brygada.nazwa, zlecenie.stan, zlecenie.dataUtworzenia.toLocalDate()};
            model.addRow(row);
        }

        JTable zlaceniaTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(zlaceniaTable);
        ListSelectionModel selectionModel = zlaceniaTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton deleteButton = new JButton("Usuń zaznaczone");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = zlaceniaTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = zlaceniaTable.convertRowIndexToModel(selectedRows[i]);
                    model.removeRow(selectedRow);
                    Zlecenie.zleceniaDlaFrame.remove(selectedRow);
                }
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowCount = zlaceniaTable.getSelectedRowCount();
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
                int[] selectedRows = zlaceniaTable.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    model.removeRow(selectedRow);
                    Zlecenie.zleceniaDlaFrame.remove(selectedRow);
                }
            }
        });

        JMenuItem edytujMenuItem = new JMenuItem("Edytuj");
        edytujMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zlaceniaTable.getSelectedRow();
                if (selectedRow != -1) {
                    Zlecenie edytowaneZlecenie = Zlecenie.zleceniaDlaFrame.get(selectedRow);
                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    JRadioButton planowaneRadioButton = new JRadioButton("Planowane");
                    JRadioButton nieplanowaneRadioButton = new JRadioButton("Nieplanowane");
                    ButtonGroup radioButtonGroup = new ButtonGroup();
                    radioButtonGroup.add(planowaneRadioButton);
                    radioButtonGroup.add(nieplanowaneRadioButton);
                    JComboBox<String> brygadaCBox = new JComboBox<>();
                    brygadaCBox.insertItemAt("", 0);
                    for (Brygada brygada : Brygada.allBrygady) {
                        brygadaCBox.addItem(brygada.nazwa);
                    }
                    panel.add(new JLabel("Status zlecenia"));
                    panel.add(planowaneRadioButton);
                    panel.add(new JLabel(""));
                    panel.add(nieplanowaneRadioButton);
                    panel.add(new JLabel("Brygada"));
                    panel.add(brygadaCBox);

                    int result = JOptionPane.showConfirmDialog(ZlecenieFrame.this, panel, "Edytuj zlecenie",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        boolean noweCzyPlanowane = planowaneRadioButton.isSelected();
                        String nowaNazwaBrygady = (String) brygadaCBox.getSelectedItem();
                        Brygada nowaWybranaBrygada = null;

                        for (Brygada brygada : Brygada.allBrygady) {
                            if (brygada.nazwa.equals(nowaNazwaBrygady)) {
                                nowaWybranaBrygada = brygada;
                                break;
                            }
                        }

                        if (nowaWybranaBrygada != null) {
                            String nazwaNowaBrygada = nowaWybranaBrygada.nazwa;
                            model.setValueAt(nazwaNowaBrygada, selectedRow, 1);
                            Zlecenie.StanZlecenia nowyStan = Zlecenie.StanZlecenia.NIEPLANOWANE;
                            if (noweCzyPlanowane) nowyStan = Zlecenie.StanZlecenia.PLANOWANE;
                            model.setValueAt(nowyStan, selectedRow, 2);
                            edytowaneZlecenie.stan = nowyStan;
                            edytowaneZlecenie.brygada = nowaWybranaBrygada;
                            Zlecenie.zleceniaDlaFrame.remove(selectedRow);
                            Zlecenie.zleceniaDlaFrame.add(selectedRow, edytowaneZlecenie);
                        }
                    }
                }
            }
        });

        JMenuItem dodajPraceItem = new JMenuItem("Dodaj pracę");
        dodajPraceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zlaceniaTable.getSelectedRow();
                if (selectedRow != -1) {
                    Zlecenie zlecenie = Zlecenie.zleceniaDlaFrame.get(selectedRow);

                    JComboBox<String> praceComboBox = new JComboBox<>();
                    for (Praca praca : Praca.praceForFrame) {
                        praceComboBox.addItem(praca.opis);
                    }

                    JPanel panel = new JPanel(new GridLayout(0, 2));
                    panel.add(new JLabel("Wybierz pracę:"));
                    panel.add(praceComboBox);

                    int result = JOptionPane.showConfirmDialog(ZlecenieFrame.this, panel, "Dodaj pracę do zlecenia",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        Praca wybranaPraca = (Praca) Praca.praceForFrame.stream()
                                .filter(praca -> praca.opis.equals(praceComboBox.getSelectedItem()))
                                .findFirst()
                                .orElse(null);
                        zlecenie.addPraca(wybranaPraca);
                        Zlecenie.zleceniaDlaFrame.add(selectedRow, zlecenie);
                    }
                } else {
                    JOptionPane.showMessageDialog(ZlecenieFrame.this, "Nie wybrano żadnego zlecenia.", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem showPraceItem = new JMenuItem("Pokaż pracę");
        showPraceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zlaceniaTable.getSelectedRow();
                if (selectedRow != -1) {
                    Zlecenie zlecenie = Zlecenie.zleceniaDlaFrame.get(selectedRow);

                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("ID");
                    model.addColumn("Nazwa pracy");
                    model.addColumn("Opis");

                    for (Praca praca : zlecenie.pracaList) {
                        Object[] row = {praca.id, praca.rPracy, praca.opis};
                        model.addRow(row);
                    }

                    JTable praceTable = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(praceTable);

                    JOptionPane.showMessageDialog(ZlecenieFrame.this, scrollPane, "Prace w zleceniu: " + zlecenie.id,
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ZlecenieFrame.this, "Nie wybrano żadnego zlecenia.", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem startZlecenie = new JMenuItem("Rozpocznij zlecenie");
        startZlecenie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zlaceniaTable.getSelectedRow();
                if (selectedRow != -1) {
                    Zlecenie zlecenie = Zlecenie.zleceniaDlaFrame.get(selectedRow);
                    zlecenie.start();
                    model.setValueAt(Zlecenie.StanZlecenia.REALIZOWANE, selectedRow, 2);
                    model.setValueAt(zlecenie.dataRealizacji.toLocalDate(), selectedRow, 3);
                }
            }
        });

        JMenuItem stopZlecenie = new JMenuItem("Zakończ zlecenie");
        stopZlecenie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zlaceniaTable.getSelectedRow();
                if (selectedRow != -1) {
                    Zlecenie zlecenie = Zlecenie.zleceniaDlaFrame.get(selectedRow);
                    zlecenie.stop();
                    model.setValueAt(Zlecenie.StanZlecenia.ZAKONCZONE, selectedRow, 2);
                    model.setValueAt(zlecenie.dataZakonczenia, selectedRow, 3);
                }
            }
        });

        popupMenu.add(showPraceItem);
        popupMenu.add(dodajPraceItem);
        popupMenu.add(startZlecenie);
        popupMenu.add(stopZlecenie);
        popupMenu.add(edytujMenuItem);
        popupMenu.add(deleteMenuItem);

        zlaceniaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = zlaceniaTable.rowAtPoint(e.getPoint());
                        if (row >= 0 && row < zlaceniaTable.getRowCount()) {
                        zlaceniaTable.setRowSelectionInterval(row, row);
                        popupMenu.show(zlaceniaTable, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton noweZlecenie = new JButton("Dodaj zlecenie");
        buttonPanel.add(noweZlecenie);
        noweZlecenie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 2));
                JRadioButton planowaneRadioButton = new JRadioButton("Planowane");
                JRadioButton nieplanowaneRadioButton = new JRadioButton("Nieplanowane");
                ButtonGroup radioButtonGroup = new ButtonGroup();
                radioButtonGroup.add(planowaneRadioButton);
                radioButtonGroup.add(nieplanowaneRadioButton);
                JComboBox<String> brygadaCBox = new JComboBox<>();
                brygadaCBox.insertItemAt("", 0);
                for (Brygada brygada : Brygada.allBrygady) {
                    brygadaCBox.addItem(brygada.nazwa);
                }
                panel.add(new JLabel("Status zlecenia:"));
                panel.add(planowaneRadioButton);
                panel.add(new JLabel(""));
                panel.add(nieplanowaneRadioButton);
                panel.add(new JLabel("Brygada:"));
                panel.add(brygadaCBox);

                int result = JOptionPane.showConfirmDialog(ZlecenieFrame.this, panel, "Dodaj zlecenie",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    boolean czyPlanowane = planowaneRadioButton.isSelected();
                    String wybranaBrygada = (String) brygadaCBox.getSelectedItem();
                    Brygada brygada = Brygada.allBrygady.stream()
                            .filter(brygaddaa -> brygaddaa.nazwa.equals(wybranaBrygada))
                            .findFirst()
                            .orElse(null);
                    Zlecenie noweZlecenie = new Zlecenie(czyPlanowane, brygada);
                    Object[] row = {noweZlecenie.id, noweZlecenie.brygada.nazwa, noweZlecenie.stan, noweZlecenie.dataUtworzenia.toLocalDate()};
                    model.addRow(row);
                    int newRowIdx = model.getRowCount() - 1;
                    zlaceniaTable.setRowSelectionInterval(newRowIdx, newRowIdx);
                    zlaceniaTable.scrollRectToVisible(zlaceniaTable.getCellRect(newRowIdx, 0, true));
                }
            }
        });

        add(buttonPanel,BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
