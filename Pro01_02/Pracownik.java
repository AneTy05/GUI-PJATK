import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Pracownik implements Comparable<Pracownik>, Serializable {
    static ArrayList<Pracownik> pracownicy = new ArrayList<>();
    String imie, nazwisko;
    LocalDate dataUrodzenia;
    DzialPracownikow dzial;
    long id;
    boolean isBusy;
    static long counter = 1;


    public Pracownik(String imie, String nazwisko, LocalDate dataUrodzenia, DzialPracownikow dzial) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.dataUrodzenia = dataUrodzenia;
        this.dzial = dzial;
        this.isBusy = false;
        this.id = counter++;
        pracownicy.add(this);
    }
    public void setBusy(){
        this.isBusy = true;
    }

    @Override
    public int compareTo(Pracownik o) {
        int porownanie = this.imie.compareTo(o.imie);
        if (porownanie == 0){
            porownanie = this.nazwisko.compareTo(o.nazwisko);
            if (porownanie == 0){
                porownanie = this.dataUrodzenia.compareTo(o.dataUrodzenia);
            }
        }
        return porownanie;
    }

}
