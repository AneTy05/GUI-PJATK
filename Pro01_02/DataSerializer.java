import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class DataSerializer {
    public static void serializeDataBrygady(List<Brygada> brygady, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(brygady);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataBrygadzisci(List<Brygadzista> brygadzisci, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(brygadzisci);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataDzialy(List<DzialPracownikow> dzialy, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(dzialy);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataPrace(List<Praca> prace, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(prace);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataPracownicy(List<Pracownik> pracownicy, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(pracownicy);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataSpecjalisci(List<Specjalista> specjalisci, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(specjalisci);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataUzytkownicy(List<Uzytkownik> uzytkownicy, String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(uzytkownicy);

            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
    public static void serializeDataZlecenia(List<Zlecenie> zlecenia, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(zlecenia);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu danych do pliku: " + filename);
            e.printStackTrace();
        }
    }
}
