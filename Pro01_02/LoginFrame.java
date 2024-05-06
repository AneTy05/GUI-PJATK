import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {
    JLabel nazwaUzLabel;
    JLabel hasloLabel;
    JTextField nazwaUzytkownika;
    JPasswordField haslo;
    JButton login;
    boolean czyZalogowano;
    static Uzytkownik zalogowany;
    public LoginFrame() {
        setTitle("Logowanie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        nazwaUzLabel = new JLabel("Login: ");
        hasloLabel = new JLabel("Hasło: ");
        nazwaUzytkownika = new JTextField(20);
        haslo = new JPasswordField(20);
        login = new JButton("Zaloguj");
        JPanel loginFrame = new JPanel();
        loginFrame.add(nazwaUzLabel);
        loginFrame.add(nazwaUzytkownika);
        loginFrame.add(hasloLabel);
        loginFrame.add(haslo);
        loginFrame.add(login);
        add(loginFrame);

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                DataSerializer.serializeDataBrygady(Brygada.allBrygady, "brygady.ser");
                DataSerializer.serializeDataBrygadzisci(Brygadzista.allBrygadzisci, "brygadzisci.ser");
                DataSerializer.serializeDataDzialy(DzialPracownikow.wszystkieDzialy, "dzialyPracownikow.ser");
                DataSerializer.serializeDataPrace(Praca.praceForFrame, "prace.ser");
                DataSerializer.serializeDataPracownicy(Pracownik.pracownicy, "pracownicy.ser");
                DataSerializer.serializeDataSpecjalisci(Specjalista.allSpecjalisci, "specjalisci.ser");
                DataSerializer.serializeDataUzytkownicy(Uzytkownik.allUzytkownicy, "uzytkownicy.ser");
                DataSerializer.serializeDataZlecenia(Zlecenie.zleceniaDlaFrame, "zlecenia.ser");
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uzytkownikName = nazwaUzytkownika.getText();
                String password = String.valueOf(haslo.getPassword());
                Uzytkownik logingUz = (Uzytkownik) Uzytkownik.allUzytkownicy.stream()
                        .filter(uzytkownik -> uzytkownik.getLogin().equals(uzytkownikName))
                        .findAny()
                        .orElse(null);
                if (logingUz != null) {
                    if (logingUz.getHaslo().equals(password)) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Zalogowano pomyślnie");
                        czyZalogowano = true;
                        frame.setTitle("Witaj " + logingUz.inicjaly + "! Jestes zalogowany jako: " + logingUz.imie
                                + " " + logingUz.nazwisko);
                        zalogowany = logingUz;
                        PanelWidoki panelWidoki = new PanelWidoki();
                        frame.add(panelWidoki);
                        frame.setVisible(true);
                        setVisible(false);
                    }
                    else
                        JOptionPane.showMessageDialog(LoginFrame.this, "Nieprawidłowe hasło");
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Użytkownik nie istnieje");
                }
            }
        });
    }
}
