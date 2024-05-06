import java.io.Serializable;
import java.util.ArrayList;

public class DzialPracownikow implements Serializable {
    String nazwa;
    ArrayList<Pracownik> workerList;
    long id;
    static long counter = 1;
    static ArrayList<String> allNazwy = new ArrayList<>();
    static ArrayList<DzialPracownikow> wszystkieDzialy = new ArrayList<>();
    private DzialPracownikow(String nazwa) throws NotUniqueNameException {
        if (allNazwy.contains(nazwa)) {
            throw new NotUniqueNameException("Nazwa nie jest unikalna");
        } else {
            allNazwy.add(nazwa);
            this.nazwa = nazwa;
            this.workerList = new ArrayList<>();
            this.id = counter++;
            wszystkieDzialy.add(this);
        }
    }
    public void addPracownik(Pracownik p) {
        this.workerList.add(p);
    }
    public static DzialPracownikow createDzial (String nazwa) throws NotUniqueNameException {
        return new DzialPracownikow(nazwa);
    }

    public static ArrayList<Pracownik> getListPracownik (String szukanyDzial) {
        ArrayList<Pracownik> listPracownik = new ArrayList<>();
        for (Pracownik pracownik : Pracownik.pracownicy) {
            if (pracownik.dzial.nazwa.equals(szukanyDzial)){
                listPracownik.add(pracownik);
            }
        }
        return listPracownik;
    }
    public ArrayList<Pracownik> getWorkerList() {
        return workerList;
    }

    @Override
    public String toString() {
        return "DzialPracownikow{" +
                "nazwa='" + nazwa + '\'' +
                ", id=" + id + + '\'' +
//                ", pracownicy= " + workerList +
                '}';
    }
}
