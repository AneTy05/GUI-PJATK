import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brygada implements Serializable {
    String nazwa;
    Brygadzista brygadzista;
    List<Pracownik> pracownicy;
    long id;
    static List<Brygada> allBrygady = new ArrayList<>();
    static long counter = 1;

    public Brygada(String nazwa, Brygadzista brygadzista) {
        this.nazwa = nazwa;
        this.brygadzista = brygadzista;
        this.pracownicy = new ArrayList<>();
        this.id = counter++;
        allBrygady.add(this);
    }
    public void add(List<Pracownik> list) {
        this.pracownicy = list;
    }
    public void add(Pracownik p) {
        if (p instanceof Brygadzista || p instanceof Specjalista) {
            this.pracownicy.add(p);
        } else {
            System.out.println();
        }
    }
    public void setBusy(){
        this.pracownicy.forEach(Pracownik::setBusy);
    }
    public void setFree(){
        this.pracownicy.forEach(pracownik -> pracownik.isBusy = false);
    }

    @Override
    public String toString() {
        return "Brygada: {" +
                "Nazwa='" + nazwa + '\'' +
                ", ID=" + id +
                ",\nBrygadzista=" + brygadzista +
                ",\nPracownicy=" + pracownicy +
                "\n}";
    }
}
