import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Zlecenie implements Runnable, Serializable {
    ArrayList<Praca> pracaList;
    Brygada brygada;
    enum StanZlecenia {
        PLANOWANE,
        NIEPLANOWANE,
        REALIZOWANE,
        ZAKONCZONE,
        UTWORZONE
    }
    StanZlecenia stan;
    long id;
    static long counter = 1;
    LocalDateTime dataUtworzenia;
    LocalDateTime dataRealizacji;
    LocalDateTime dataZakonczenia;
    static Map<Long, Zlecenie> allZlecenia = new HashMap<>();
    static ArrayList<Zlecenie> zleceniaDlaFrame = new ArrayList<>();

    public Zlecenie(boolean czyPlanowane, Brygada b) {
        this.pracaList = new ArrayList<>();
        this.brygada = b;
        this.brygada.brygadzista.zlecenia.add(this);
        this.dataUtworzenia = LocalDateTime.now();
        this.stan = czyPlanowane ? StanZlecenia.PLANOWANE : StanZlecenia.NIEPLANOWANE;
        this.id = counter++;
        allZlecenia.put(id, this);
        zleceniaDlaFrame.add(this);
    }
    public void addPraca(Praca p) {
        this.pracaList.add(p);
    }
    public boolean addBrygada(Brygada b) {
        if (this.brygada == null) {
            this.brygada = b;
            this.brygada.brygadzista.zlecenia.add(this);
            return true;
        } else
            return false;
    }

    public void start() {
        this.stan = StanZlecenia.REALIZOWANE;
        this.dataRealizacji = LocalDateTime.now();
    }
    public void stop() {
        this.stan = StanZlecenia.ZAKONCZONE;
        this.dataZakonczenia = LocalDateTime.now();
    }
    public void rozpocznijZlecenie(){
        this.dataRealizacji = LocalDateTime.now();
        System.out.println("Zlecenie " + this.id + " rozpoczęto: " + this.dataRealizacji);
        while (!pracaList.isEmpty()) {
            this.stan = StanZlecenia.REALIZOWANE;
            Praca wykPraca = pracaList.get(0);
            try {
                System.out.println(wykPraca);
                Thread.sleep(wykPraca.czasPracy * 60 * 1000L);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pracaList.remove(0);
        }
        this.dataZakonczenia = LocalDateTime.now();
        this.stan = StanZlecenia.ZAKONCZONE;
        System.out.println("Zlecenie " + this.id + " zakończono: " + this.dataZakonczenia);
        this.brygada.setFree();
    }
    public StanZlecenia getStanZlecenia() {
        switch (stan) {
            case PLANOWANE, NIEPLANOWANE: return StanZlecenia.UTWORZONE;
            case REALIZOWANE: return StanZlecenia.REALIZOWANE;
            case ZAKONCZONE: return StanZlecenia.ZAKONCZONE;
            default: return null;
        }
    }
    public static Zlecenie getZlecenie(Long id) throws IllegalArgumentException {
        Zlecenie z = allZlecenia.get(id);
        if (z == null) {
            throw new IllegalArgumentException("Zlecenie o podanym id nie istnieje");
        }
        return z;
    }
    @Override
    public void run() {
        if (this.brygada == null) {
            System.out.println("Nie można wykonać zlecenia. Brak brygady.");
            return;
        } else if (this.pracaList.isEmpty()) {
            System.out.println("Nie można wykonać zlecenia. Lista prac jest pusta.");
            return;
        }
        ArrayList<Pracownik> isBusyControl = (ArrayList<Pracownik>) brygada.pracownicy.stream()
                .filter(pracownik -> pracownik.isBusy)
                .collect(Collectors.toList());
        if (!isBusyControl.isEmpty()) {
            System.out.println("Nie można wykonać zlecenia. Pracownicy są zajęci.");
        }
        else {
            this.rozpocznijZlecenie();
            this.brygada.setBusy();
        }
    }

    @Override
    public String toString() {
        return "Zlecenie {" +
                "id=" + id +
                ", pracaList=" + pracaList +
                ",\nbrygada=" + brygada +
                ",\nstan=" + stan +
                ",\ndataUtworzenia=" + dataUtworzenia.toLocalDate() +
//                ",\ndataRealizacji=" + dataRealizacji.toLocalDate() +
//                ",\ndataZakonczenia=" + dataZakonczenia.toLocalDate() +
                '}';
    }
}
