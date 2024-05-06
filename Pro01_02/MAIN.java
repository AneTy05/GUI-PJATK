import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MAIN {
    public static void main(String[] args) throws NotUniqueNameException {
        DzialPracownikow dzial1 = DzialPracownikow.createDzial("Elektrycy");
        DzialPracownikow dzial2 = DzialPracownikow.createDzial("Hydraulicy");
        DzialPracownikow dzial3 = DzialPracownikow.createDzial("Murarze i cieśle");
        Brygadzista bryg1 = new Brygadzista("Wojciech", "Kaczmarek", LocalDate.of(1992, 7, 14), dzial2, "abcd", "abcd");
        Brygadzista bryg2 = new Brygadzista("Katarzyna", "Mazur", LocalDate.of(1971, 11, 2), dzial1, "qweui", "poiuytr");
        Brygadzista bryg3 = new Brygadzista("Michał", "Kubiak", LocalDate.of(1984, 9, 19), dzial3, "yhjkn", "lkjhgf");
        Specjalista spec1 = new Specjalista("Marcin", "Kowalski", LocalDate.of(1985, 8, 4), dzial1, "Elektryk");
        Specjalista spec2 = new Specjalista("Natalia", "Nowak", LocalDate.of(1979, 1, 27), dzial2, "Hydraulik");
        Specjalista spec3 = new Specjalista("Kamil", "Wiśniewski", LocalDate.of(1991, 5, 16), dzial3, "Murarz");
        Specjalista spec4 = new Specjalista("Łukasz", "Kowalczyk", LocalDate.of(1973, 10, 12), dzial3, "Cieśla");
        Specjalista spec5 = new Specjalista("Krzysztof", "Lewandowski", LocalDate.of(1995, 11, 23), dzial3, "Murarz");
        Specjalista spec6 = new Specjalista("Piotr", "Zieliński", LocalDate.of(1990, 3, 6), dzial1, "Elektryk");
        Specjalista spec7 = new Specjalista("Anna", "Szymańska", LocalDate.of(1983, 4, 29), dzial2, "Hudraulik");
        Specjalista spec8 = new Specjalista("Mateusz", "Wojciechowski", LocalDate.of(1972, 2, 7), dzial3, "Cieśla");
        Specjalista spec9 = new Specjalista("Szymon", "Jankowski", LocalDate.of(1989, 6, 21), dzial3, "Murarz");
        Uzytkownik uz1 = new Uzytkownik("Martyna", "Wójcik", LocalDate.of(1982, 9, 8), dzial1, "aaaa", "aaaa");
        Uzytkownik uz2 = new Uzytkownik("Weronika", "Kamińska", LocalDate.of(1987, 7, 9), dzial2, "vubry", "uioiuyt");
        Uzytkownik uz3 = new Uzytkownik("Tomasz", "Dąbrowski", LocalDate.of(1978, 12, 18), dzial3, "zxntd", "asdfghj");
        Brygada brygada1 = new Brygada("Brygada A", bryg1);
        Brygada brygada2 = new Brygada("Brygada B", bryg2);
        Brygada brygada3 = new Brygada("Brygada C", bryg3);
        Brygada brygada4 = new Brygada("Brygada D", bryg2);
        Zlecenie zlecenie1 = new Zlecenie(true, brygada1);
        Zlecenie zlecenie2 = new Zlecenie(true, brygada2);
        Zlecenie zlecenie3 = new Zlecenie(false, brygada3);
        Zlecenie zlecenie4 = new Zlecenie(true ,brygada4);
        Praca p1 = new Praca(Praca.RodzajPracy.MONTAZ, 2, "Montaż oświetlenia LED");
        Praca p2 = new Praca(Praca.RodzajPracy.WYMIANA, 3, "Wymiana rury");
        Praca p3 = new Praca(Praca.RodzajPracy.OGOLNA, 5, "Fundament");
        Praca p4 = new Praca(Praca.RodzajPracy.OGOLNA, 10, "Murowanie ściany");
        Praca p5 = new Praca(Praca.RodzajPracy.DEMONTAZ, 4, "Demontaż rusztowania");
        Praca p6 = new Praca(Praca.RodzajPracy.DEMONTAZ, 6, "Demontaż starej zabudowy");
        Praca p7 = new Praca(Praca.RodzajPracy.MONTAZ, 8, "Montaż czujnika dymu");

        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);

        //Do wykasowania po testach:
//        JFrame frame = new JFrame();
//        PanelWidoki panelWidoki = new PanelWidoki();
//        frame.add(panelWidoki);
//        frame.setLayout(new GridLayout());
//        frame.setSize(1000, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
        }
    }
