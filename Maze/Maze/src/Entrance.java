import java.util.Objects;

/**
 * Created by Karol on 2015-11-15.
 */
public class Entrance {
    int wiersz;
    int kolumna;

    Entrance (int y , int x ) {
        this.kolumna = y;
        this.wiersz  = x;
    }
        public int getWiersz() {
            return wiersz;
        }
        public int getKolumna() {
            return kolumna;
        }

    public String toString() {
        return kolumna+","+wiersz;

    }

    public boolean equals(Object entrance) {
        Entrance p = (Entrance) entrance;
        return (getKolumna() == p.getKolumna()) && (getWiersz() == p.getWiersz());
    }

    }

