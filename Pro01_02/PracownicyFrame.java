import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PracownicyFrame extends JPanel {

    public PracownicyFrame() {
        Pracownik.pracownicy = (ArrayList<Pracownik>) DataDeserializer.deserializeDataPracownicy("pracownicy.ser");
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nazwisko");
        model.addColumn("Imię");
        model.addColumn("Id");
        model.addColumn("Data urodzenia");
        model.addColumn("Dział");

        for (Pracownik pracownik : Pracownik.pracownicy) {
            Object[] row = {pracownik.nazwisko, pracownik.imie, pracownik.id, pracownik.dataUrodzenia, pracownik.dzial};
            model.addRow(row);
        }

        JTable pracownicyTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(pracownicyTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
