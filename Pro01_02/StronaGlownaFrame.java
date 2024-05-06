import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StronaGlownaFrame extends JPanel {
    public StronaGlownaFrame() {
        if (LoginFrame.zalogowany instanceof Brygadzista) {
            Brygadzista zalogowany = (Brygadzista) LoginFrame.zalogowany;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("Twoje zlecenia: ");
            add(label);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Id");
            model.addColumn("Brygada");
            model.addColumn("Stan");
            model.addColumn("Data utworzenia");
            List<Zlecenie> zleceniaZalogowanego = zalogowany.getZlecenia();

            for (Zlecenie zlecenie : zleceniaZalogowanego) {
                Object[] row = {zlecenie.id, zlecenie.brygada.nazwa, zlecenie.stan, zlecenie.dataUtworzenia.toLocalDate()};
                model.addRow(row);
            }
            JTable tabelaDlaZalogowanego = new JTable(model);
            add(tabelaDlaZalogowanego);

            JScrollPane scrollPane = new JScrollPane(tabelaDlaZalogowanego);
            add(scrollPane);
        } else {
            JLabel helloLabel = new JLabel("Witaj " + LoginFrame.zalogowany.imie
                    + " " + LoginFrame.zalogowany.nazwisko + "!");
            add(helloLabel, BorderLayout.CENTER);
        }
    }
}
