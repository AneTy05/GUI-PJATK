import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Specjalista extends Pracownik implements Serializable {
    String specjalizacja;
    static ArrayList<Specjalista> allSpecjalisci = new ArrayList<>();

    public Specjalista(String imie, String nazwisko, LocalDate dataUrodzenia, DzialPracownikow dzial, String specjalizacja) {
        super(imie, nazwisko, dataUrodzenia, dzial);
        this.specjalizacja = specjalizacja;
        allSpecjalisci.add(this);
    }

    @Override
    public String toString() {
        return "Specjalista {" +
                "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ",\nid=" + id +
                ", dataUrodzenia=" + dataUrodzenia +
                ",\nspecjalizacja='" + specjalizacja + '\'' +
                ", dzial=" + dzial +
                "}\n";
    }
}
