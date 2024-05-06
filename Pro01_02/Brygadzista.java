import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Brygadzista extends Uzytkownik implements Serializable {
    ArrayList<Zlecenie> zlecenia;
    static ArrayList<Brygadzista> allBrygadzisci = new ArrayList<>();
    public Brygadzista(String imie, String nazwisko, LocalDate dataUrodzenia, DzialPracownikow dzial, String login, String haslo) {
        super(imie, nazwisko, dataUrodzenia, dzial, login, haslo);
        this.zlecenia = new ArrayList<>();
        allBrygadzisci.add(this);
    }
    public List<Brygada> getBrygady() {
        List<Brygada> brygadyList = new ArrayList<>();
        for (Brygada b : Brygada.allBrygady) {
            if (b.brygadzista.equals(this)){
                brygadyList.add(b);
            }
        }
        return brygadyList;
    }
    public List<Zlecenie> getZlecenia() {
        List<Zlecenie> zleceniaInBrygada = zlecenia.stream()
                .filter(zlecenie -> zlecenie.brygada.brygadzista.equals(this))
                .collect(Collectors.toList());
        System.out.println(zleceniaInBrygada);
        return zleceniaInBrygada;
    }
    public String getLogin() {
        return login;
    }
    public String getHaslo() {
        return haslo;
    }
    @Override
    public String toString() {
        return "Brygadzista {" +
                "Imie='" + imie + '\'' +
                ", Nazwisko='" + nazwisko + '\'' +
                ",\nID=" + id +
                ", inicjaly='" + inicjaly + '\'' +
                ",\ndataUrodzenia=" + dataUrodzenia +
                ", dzial= " + dzial +
                "}\n";
    }
}
