import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;

import java.util.ArrayList;

/**
 * Created by Karol on 2015-11-12.
 * /*Zadanie 1 - Labirynt

 Sam wybierz sobie poziom zadania. Możesz także przesłać kilka poziomów oddzielnie.
 Napisz program, który na wejściu przyjmie macierz reprezentująca labirynt w następującej postaci:
 [
 [1,1,1,1,1],
 [1,0,0,0,0],
 [1,0,1,0,1],
 [1,0,1,1,1]
 ]
 gdzie 1 reprezentują ściany - czyli pola labiryntu na które nie można wchodzić,
 a 0 reprezentują odcinki korytarza, oraz koordynaty pola startowego.
 Labirynt jest zawsze prostokątem
 Pole wejściowe nigdy nie jest polem wyjściowym
 Poziom 1) Program zwróci liczbę ścian oraz liczbę kafelków korytarza
 Poziom 2) Program przyjmie koordynaty pola wejściowego a na wyjściu zwróci koordynaty pola wyjściowego, bez podawania ścieżki
 Dla powyższego przykładu jeżeli na wejściu otrzyma [3,1] to na wyjściu powinien zwrócić [1,4]
 Poziom 3) Program przyjmie 3 wartości - koszt wybudowania jednej ściany, koszt wybudowania jednego odcinka korytarza, oraz koszt pochodni a zwróci całkowity koszt wybudowania labiryntu, przy założeniu ze pochodnia umieszczona jest na co drugim odcinku korytarza
 Poziom 4) Program znajdzie ścieżkę przejścia labiryntu. Na wyjściu program powinien zwrócić listę koordynatów reprezentujących ścieżkę dotarcia do pola wyjściowego.
 Czyli dla powyższego labiryntu oraz pola startowego o koordynatach [3,1] Powinien zwrócić następującą listę
 [ [3,1], [2,1], [1,1], [1,2], [1,3], [1,4]].
 Poziom 5) Program jest RESTowym endpointem o następujących endpointach
 /maze/
 PUT - dodanie nowego labiryntu
 body to json z labiryntem
 {
 maze: [
 [1,1,1,1,1],
 [1,0,0,0,0],
 [1,0,1,0,1],
 [1,0,1,1,1]
 ],
 entrance: [3,1]
 }
 jeżeli labirynt jest poprawny odpowie odpowiednim statusem http potwierdzającym dodanie
 i zwróci id tego labiryntu
 jeżeli labirynt jest niepoprawny to odpowie odpowiednim statusem http
 /maze/[id]/describe
 GET - zwróci informacje z poziomu 1 w postaci jsona
 {
 walls: number,
 corridors: number
 }
 /maze/[id]/exit
 GET - zwróci coordynaty pola wyjściowego w json
 {
 exit: [x, y]
 }
 /maze/[id]/quotation?wallPrice=[float]&corridorPrice=[float]&torchPrice=[float]
 GET - zwróci koszt w następującej postaci
 {
 price: [float]
 }
 /maze/[id]/path
 GET - zwróci ścieżkę dojścia do wyjścia
 {
 path: [ [3,1], [2,1], [1,1], [1,2], [1,3], [1,4]]
 }*/

public class Main {

    static int[][] labirynt = {{1, 1, 1, 1, 1}, {1, 0, 0, 0, 0}, {1, 0, 1, 0, 1}, {1, 0, 1, 1, 1}};
    static Entrance wejscie = new Entrance(3, 1);

    public static void main(String args[]) {
        Main projekt = new Main();

        System.out.println("*Labirynt allegro*");
        //projekt.wyswietlLabirynt(labirynt);
        System.out.println(projekt.detailsLabirynt(labirynt));

        projekt.szukajWyjscia(labirynt, wejscie);
        System.out.println("Koszt budowy: " + projekt.kosztBudowy(2, 3, 4, labirynt));
    }


    public void wyswietlLabirynt(int[][] lab) {

        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab[i].length; j++) {
                System.out.print(lab[i][j]);
            }
            System.out.println();
        }
    }

    public String detailsLabirynt(int[][] lab) {
        int sciany = 0;
        int kafle = 0;

        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab[i].length; j++) {
                if (labirynt[i][j] == 0) {
                    kafle++;
                }
                if (labirynt[i][j] == 1) {
                    sciany++;
                }
            }
        }
        return "\nIlosc scian: " + sciany + "\nIlosc kafli: " + kafle;

    }
    //Metoda szukajaca wyjscia z labirybtu
    //int lab[][] labirynt do przeszuakania
    //int wejsc - punkt wejscia do labiryntu

    public void szukajWyjscia(int lab[][], Entrance wej) {

        int[][] labCheck = new int[lab.length][lab[1].length];
        Entrance exit;
        ArrayList<Entrance> kolejka = new ArrayList<Entrance>();
        ArrayList<Entrance> wholePath = new ArrayList<Entrance>();
        int step = 2;
        kolejka.add(wej);
        wholePath.add(wej);
        labCheck[wej.getKolumna()][wej.getWiersz()] = step;


        System.out.println(kolejka.toString());
        System.out.println(kolejka.get(kolejka.size() - 1).toString());


        while (!kolejka.isEmpty()) {
            int wiersz = kolejka.get(kolejka.size() - 1).getWiersz();
            int kolumna = kolejka.get(kolejka.size() - 1).getKolumna();

            step++;

            //Czy napewno jestem na wejściu
            if (lab[kolumna][wiersz] == 1) {
                System.out.println("Wprowadziles punkt, ktory nie jest wejsciem");
                break;
            }
            if (lab[kolumna][wiersz] == 0) {
                System.out.println("Ok sprawdzam dalej");

                //Sprawdzam WEST
                if (0 > wiersz - 1) {
                } else if (lab[kolumna][wiersz - 1] == 0 && labCheck[kolumna][wiersz - 1] == 0) {
                    kolejka.add(new Entrance(kolumna, wiersz - 1));
                    wholePath.add(new Entrance(kolumna, wiersz - 1));
                    labCheck[kolumna][wiersz - 1] = step;
                }
                //Sprawdz EAST
                //System.out.println("Dlugosc wiersza "+ lab[kolumna].length + " Sprawdzam dla "+ (wiersz+1) + "\n");    //todo
                if (lab[kolumna].length - 1 < wiersz + 1) {
                }
                //else if(lab[kolumna][wiersz+1] == 0 && labCheck[kolumna][wiersz+1] != 2 )
                else if (lab[kolumna][wiersz + 1] == 0 && labCheck[kolumna][wiersz + 1] == 0) {
                    kolejka.add(new Entrance(kolumna, wiersz + 1));
                    wholePath.add(new Entrance(kolumna, wiersz + 1));
                    labCheck[kolumna][wiersz + 1] = step;    //ilosc wykonanych krokow, ktore musza zostac wykonane zeby dojsc do celu

                }

                //Sprawdz NORTH
                if (0 > kolumna - 1) {
                } else if (lab[kolumna - 1][wiersz] == 0 && labCheck[kolumna - 1][wiersz] == 0) {
                    kolejka.add(new Entrance(kolumna - 1, wiersz));
                    wholePath.add(new Entrance(kolumna-1, wiersz));
                    labCheck[kolumna - 1][wiersz] = step;

                }
                //Sprawdz SOUTH
                if (lab.length - 1 == kolumna) {
                    System.out.println("Stoisz kolo wyjscia");
                } else if (lab[kolumna + 1][wiersz] == 0 && labCheck[kolumna + 1][wiersz] == 0) {
                    kolejka.add(new Entrance(kolumna + 1, wiersz));
                    wholePath.add(new Entrance(kolumna+1, wiersz ));
                    labCheck[kolumna + 1][wiersz] = step;
                }
            }

//            try {
//                Thread.sleep(1000);                 //1000 milliseconds is one second.
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }

            kolejka.remove(new Entrance(kolumna, wiersz));

            System.out.println("Moja pozycja: " + kolumna + ":" + wiersz);
            System.out.println("Tak wyglada kolejka: " + kolejka);

            System.out.println("Oryginalny:");
            wyswietlLabirynt(lab);
            System.out.println("Labcheck:");
            wyswietlLabirynt(labCheck);


            // Sprawdzamy czy jestesmy kolo wyjscia, zakladajac ze wyjsciem jest pole na, ktore mozna wejsc i jest na skraju labirynyu
            if (labCheck[kolumna][wiersz] != 0 && kolejka.isEmpty() && (lab.length - 1 == kolumna || kolumna == 0 || lab[kolumna].length - 1 == wiersz || wiersz == 0)) {
                System.out.println("Stoisz kolo wyjscia " + kolumna + ":" + wiersz);
                exit = new Entrance(kolumna,wiersz);
                System.out.println("WholePath"+wholePath.toString());
                System.out.println("Sciezka do wyjscia");
                sciezkaDoWyjscia(labCheck,exit,wej);
            } else if (kolejka.isEmpty()) {
                System.out.println("Nie ma wyjscia z labiryntu");
            }
            System.out.println("Step: " + step);

        }


    }

    public void sciezkaDoWyjscia (int lab[][], Entrance exit , Entrance wej) {

        ArrayList<Entrance> sciezka = new ArrayList<Entrance>();
        ArrayList<Entrance> uporzadkowanaSciezna = new ArrayList<Entrance>();
        int kolumna = exit.getKolumna();
        int wiersz = exit.getWiersz();
        Entrance tmp = exit ;
        sciezka.add(exit);

        while (!tmp.equals(wej)) {


                //Sprawdzam WEST
            if (0 > wiersz - 1) {
            }
               else  if (lab[kolumna][wiersz - 1] <= lab[kolumna][wiersz]   && lab[kolumna][wiersz-1] != 0 ) {
                    sciezka.add(new Entrance(kolumna, wiersz - 1));
                    tmp = new Entrance(kolumna,wiersz-1);
                    wiersz -=1;

                }
                //Sprawdz EAST

            if (lab[kolumna].length -1< wiersz + 1) {
            }
               else if (lab[kolumna][wiersz + 1] <= lab[kolumna][wiersz]  && lab[kolumna][wiersz+1] != 0) {
                    sciezka.add(new Entrance(kolumna,wiersz+1));
                    tmp = new Entrance(kolumna,wiersz+1);
                    wiersz +=1;
                }

                //Sprawdz NORTH
            if (0 > kolumna - 1) {
            }
             else if (lab[kolumna - 1][wiersz] <= lab[kolumna][wiersz]  && lab[kolumna-1][wiersz] != 0) {
                    sciezka.add(new Entrance(kolumna-1,wiersz));
                    tmp = new Entrance(kolumna-1,wiersz);
                    kolumna-=1;

                }
                //Sprawdz SOUTH
            if (lab.length - 1 == kolumna) {
            }
                else if (lab[kolumna + 1][wiersz] <= lab[kolumna][wiersz]  && lab[kolumna+1][wiersz] != 0) {
                    sciezka.add(new Entrance(kolumna+1,wiersz));
                    tmp = new Entrance(kolumna+1,wiersz);
                    kolumna+=1;
                }

            }
        // Petla, ktora uporzadkuje droge od poczatku do konca
        for (int i=sciezka.size()-1; i>=0; i-- ){
           uporzadkowanaSciezna.add(sciezka.get(i));

        }
           System.out.println(uporzadkowanaSciezna.toString());
            int a =5;

        }

    public float kosztBudowy(float sciana, float korytarz, float pochodnia, int lab[][]) {
        int lScian=0;
        int lKorytarzy=0;
        int lPochodni=0;

        for(int i=0;i<lab.length;i++) {
            for(int j=0; j<lab[i].length;j++) {
                if(lab[i][j]==1) {
                    lScian++;
                }
                if(lab[i][j]==0) {
                    lKorytarzy++;
                }
            }
            lPochodni = lKorytarzy/2;

        }

        return (sciana*lScian)+(korytarz*lKorytarzy)+(pochodnia*lPochodni);
    }

    }





