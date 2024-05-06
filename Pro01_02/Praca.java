import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Praca extends Thread implements Serializable {
    enum RodzajPracy {
        OGOLNA,
        MONTAZ,
        DEMONTAZ,
        WYMIANA
    }
    RodzajPracy rPracy;
    int czasPracy; //w minutach
    boolean czyZrealizowane;
    String opis;
    long id;
    List<Praca> waitingPraca;
    static long counter = 1;
    static Map<Long, Praca> allPrace = new HashMap<>();
    static ArrayList<Praca> praceForFrame = new ArrayList<>();

    public Praca (RodzajPracy rPracy, int czasPracy, String opis) {
        this.rPracy = rPracy;
        this.czasPracy = czasPracy;
        this.opis = opis;
        this.czyZrealizowane = false;
        this.id = counter++;
        allPrace.put(id, this);
        praceForFrame.add(this);
    }
    public void addToWaitingList() {
        waitingPraca = new ArrayList<>();
        waitingPraca.add(this);
    }
    public static Praca getPraca(Long id) throws IllegalArgumentException {
        Praca p = allPrace.get(id);
        if (p == null) {
            throw new IllegalArgumentException("Praca o podanym id nie istnieje");
        }
        return p;
    }
    @Override
    public String toString() {
        return "Praca{" +
                "id=" + id +
                ", rPracy=" + rPracy +
                ",\nczasPracy=" + czasPracy +
                ",\nopis='" + opis + '\'' +
                '}';
    }
    public void run() {
        synchronized (waitingPraca) {
            while (!waitingPraca.isEmpty()) {
                try {
                    waitingPraca.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        czyZrealizowane = true;
        synchronized (waitingPraca) {
            waitingPraca.notify();
        }
    }
}
