import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Uzytkownik extends Pracownik implements Serializable {
    String login, haslo, inicjaly;
    static ArrayList<Uzytkownik> allUzytkownicy = new ArrayList<>();

    public Uzytkownik(String imie, String nazwisko, LocalDate dataUrodzenia, DzialPracownikow dzial, String login, String haslo) {
        super(imie, nazwisko, dataUrodzenia, dzial);
        this.login = login;
        this.haslo = haslo;
        this.inicjaly = String.valueOf(imie.charAt(0)) + String.valueOf(nazwisko.charAt(0));
        allUzytkownicy.add(this);
    }
    public void setName(String name) {
        this.imie = name;
        this.inicjaly = String.valueOf(imie.charAt(0)) + String.valueOf(nazwisko.charAt(0));
    }

    public void setSurname(String surname) {
        this.nazwisko = surname;
        this.inicjaly = String.valueOf(imie.charAt(0)) + String.valueOf(nazwisko.charAt(0));
    }
    public String getLogin() {
        return login;
    }
    public String getHaslo() {
        return haslo;
    }
    @Override
    public String toString() {
        return "Uzytkownik {" +
                "Imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ",\nid=" + id +
                ", inicjaly='" + inicjaly + '\'' +
                ", dataUrodzenia=" + dataUrodzenia +
                ",\ndzial=" + dzial +
                "}\n";
    }


}
