import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class DataDeserializer {
    public static List<Brygada> deserializeDataBrygady(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Brygada> brygady = (List<Brygada>) ois.readObject();
            return brygady;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Brygadzista> deserializeDataBrygadzisci(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Brygadzista> brygadzisci = (List<Brygadzista>) ois.readObject();
            return brygadzisci;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<DzialPracownikow> deserializeDataDzialy(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<DzialPracownikow> dzialy = (List<DzialPracownikow>) ois.readObject();
            return dzialy;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Praca> deserializeDataPrace(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Praca> prace = (List<Praca>) ois.readObject();
            return prace;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Pracownik> deserializeDataPracownicy(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Pracownik> pracownicy = (List<Pracownik>) ois.readObject();
            return pracownicy;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Specjalista> deserializeDataSpecjalisci(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Specjalista> specjalisci = (List<Specjalista>) ois.readObject();
            return specjalisci;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Uzytkownik> deserializeDataUzytkownicy(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Uzytkownik> uzytkownicy = (List<Uzytkownik>) ois.readObject();
            return uzytkownicy;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
    public static List<Zlecenie> deserializeDataZlecenia(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Zlecenie> zlecenia = (List<Zlecenie>) ois.readObject();
            return zlecenia;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd podczas odczytu danych z pliku: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}
