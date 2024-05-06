import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class PanelWidoki extends JPanel {
    public PanelWidoki() {
        setLayout(new GridLayout(1, 0));
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel stronaGlowna = new JPanel();
        stronaGlowna.add(new StronaGlownaFrame());
        JPanel dzialPracownikowPanel = new JPanel();
        dzialPracownikowPanel.add(new DzialyFrame());
        JPanel pracownikPanel = new JPanel();
        pracownikPanel.add(new PracownicyFrame());
        JPanel uzytkownikPanel = new JPanel();
        uzytkownikPanel.add(new UzytkownikFrame());
        JPanel brygadzistaPanel = new JPanel();
        brygadzistaPanel.add(new BrygadzistaFrame());
        JPanel specjalistaPanel = new JPanel();
        specjalistaPanel.add(new SpecjalistaFrame());
        JPanel brygadaPanel = new JPanel();
        brygadaPanel.add(new BrygadaFrame());
        JPanel zleceniePanel = new JPanel();
        zleceniePanel.add(new ZlecenieFrame());
        JPanel pracaPanel = new JPanel();
        pracaPanel.add(new PracaFrame());
        JPanel opcje = new JPanel();
        opcje.add(new OpcjeFrame());

        tabbedPane.addTab("Strona główna", stronaGlowna);
        tabbedPane.addTab("Dział pracowników", dzialPracownikowPanel);
        tabbedPane.addTab("Pracownik", pracownikPanel);
        tabbedPane.addTab("Użytkownik", uzytkownikPanel);
        tabbedPane.addTab("Brygadzista", brygadzistaPanel);
        tabbedPane.addTab("Specjaliści", specjalistaPanel);
        tabbedPane.addTab("Brygada", brygadaPanel);
        tabbedPane.addTab("Zlecenie", zleceniePanel);
        tabbedPane.addTab("Praca", pracaPanel);
        tabbedPane.addTab("Opcje", opcje);
    }
}
