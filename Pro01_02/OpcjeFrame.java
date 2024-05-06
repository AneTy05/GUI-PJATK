import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcjeFrame extends JPanel {
    public OpcjeFrame() {
        JButton zmianaHasla = new JButton("Zmień hasło");
        zmianaHasla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String noweHaslo = JOptionPane.showInputDialog("Podaj nowe hasło:");
                LoginFrame.zalogowany.haslo = noweHaslo;
            }
        });

        JButton logout = new JButton("Wyloguj");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(OpcjeFrame.this, "Czy na pewno chcesz się wylogować?",
                        "Potwierdzenie wylogowania", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(OpcjeFrame.this, "Zostałeś pomyślnie wylogowany.");

                    DataSerializer.serializeDataBrygady(Brygada.allBrygady, "brygady.ser");
                    DataSerializer.serializeDataBrygadzisci(Brygadzista.allBrygadzisci, "brygadzisci.ser");
                    DataSerializer.serializeDataDzialy(DzialPracownikow.wszystkieDzialy, "dzialyPracownikow.ser");
                    DataSerializer.serializeDataPrace(Praca.praceForFrame, "prace.ser");
                    DataSerializer.serializeDataPracownicy(Pracownik.pracownicy, "pracownicy.ser");
                    DataSerializer.serializeDataSpecjalisci(Specjalista.allSpecjalisci, "specjalisci.ser");
                    DataSerializer.serializeDataUzytkownicy(Uzytkownik.allUzytkownicy, "uzytkownicy.ser");
                    DataSerializer.serializeDataZlecenia(Zlecenie.zleceniaDlaFrame, "zlecenia.ser");

                    JFrame window = (JFrame) SwingUtilities.getWindowAncestor(OpcjeFrame.this);
                    window.dispose();

                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                }
            }
        });

        add(zmianaHasla);
        add(logout);
    }
}
