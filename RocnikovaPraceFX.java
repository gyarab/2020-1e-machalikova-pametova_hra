package rocnikovapracefx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RocnikovaPraceFX extends Application {
     int radek = 0;                                 // přidání proměnné, která reprezentuje řádek v gridpanu
     int sloupec = 0;                               // přidání proměnné, která reprezentuje sloupec v gridpanu
     static int kolo = 1;                           // přidání proměnné, která se zobrazuje v labelu level
     static boolean hrajehrac = false;              // přidání proměnné, zda hraje hráč, či ne
     static int zatimspravne = 0;                   // přidání proměnné reprezentující počet tlačítek, která hráč již správně zopakoval
     static Label level = new Label();              // label pro vypsání kola, ve kterém se hráč aktuálně nachází
     static Button[][] b = new Button[3][3];        // pole tlačítek 3x3
     static Label stav = new Label();               // když hráč špatně zopakuje vzor, vypíše se "konec hry"
     static int[][] souradnice = new int[2][50];    // pole souřadnic 2x50, v 1. řádku je první číslo souřadnice a v 2. řádku druhé
     static int prodleva = 500;                     // přidání proměnné, která reprezentuje prodlevy během problikávání tlačítek
          
    @Override
     public void start(Stage stage) {  
         Button novaHra = new Button("Nová hra");       // tlačítko, kterým se spustí nová hra, je nutno stisknout i při první hře
            novaHra.setOnAction((e) -> {                // nastavení akce, která se provede po zmáčknutí tlačítka "Nová hra"        
                RocnikovaPraceFX.Hra();
                });
         level.setText(" Level: " + kolo);              // přidání textu do labelu level, zobrazuje úroveň hráče
         GridPane grid = new GridPane();                // tabulka tlačítek
         grid.setMinSize(400, 200);                     // nastavení minimální velikosti gridpanu
         grid.setHgap(10);                              // nastavení šířky mezer mezi sloupci
         grid.setVgap(10);                              // nastavení šířky mezer mezi řádky
         grid.setPadding(new Insets(25, 25, 25, 25));   // nastvení výplně kolem tlačítek
         grid.add(level, 4, 0);                         // přidání levelu do tabulky s tlačítky, přidání do nového (4.) sloupce
         grid.add(novaHra, 4, 1);                       // přidání tlačítka "Nová hra" do tabulky s tlačítky, přidání do nového (4.) sloupce
         grid.add(stav, 4, 2);                          // přidání labelu pro oznámení konce hry, přidání do nového (4.) sloupce
         for (radek = 0; radek < 3; radek++) {          // v cyklu se všechna tlačítka přidají do gridpanu
             for (sloupec = 0; sloupec < 3; sloupec++) {
                 b[radek][sloupec] = new Button();
                 grid.add(b[radek][sloupec], radek, sloupec);
                 b[radek][sloupec].setStyle("-fx-background-color: #000000; "); // všem tlačítkům se nastaví černá barva, aby byla lépe vidět
                 b[radek][sloupec].setMinSize(50, 50);  // nastaví se jim také větší velikost, aby se hráč lépe trefoval
                 int r = radek;
                 int s = sloupec;
                 b[radek][sloupec].setOnAction((e) -> { // přidání akce při zmáčknutí kteréhokoliv z těchto tačítek
                    RocnikovaPraceFX.HracStiskl(r, s);
                    });
             }
         }
         Scene scene = new Scene(grid);                 // vyrobení nové scény
         stage.setScene(scene);                         // přiání scény do stage
         stage.show();                                  // zobrazení stage
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void pauza (int ms) {                 // metoda na udělání pauzy při mačkání tlačítka v ms
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
    
    public static void Hra () {                         // metoda, která se zavolá při stisknutí tlačítka "Nová hra" 
        stav.setText("");
        pauza(500);                                     // použití metody pauza, aby to nezačalo blikat hned. Vypadá to totiž divně. S pauzou je to mnohem lepší
        for(int i = 0; i < 50; i ++) {                  // vynulování pole se souřadnicemi
            souradnice[0][i] = 0;
            souradnice[1][i] = 0;
        }
        kolo = 1;                                       // počet tlačítek v aktuální úrovni
        hrajehrac = false;                              // nastavení proměnné, že počítač ukazuje, tudíž se stisknutá tlačítka ignorují 
        UkazVzor(kolo);                                 // zavolání metody pro ukázaní sekvence tlačítek
        }
    
    public static void UkazPolicko (int r, int s) {     // metoda pro zvýraznění tlačítka
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(prodleva),ae -> b[r][s].setStyle("-fx-background-color: #000000; ")),
                new KeyFrame(Duration.millis(prodleva - 500),ae -> b[r][s].setStyle("-fx-background-color: #00ff00; ")));        
        timeline.play();        
    }
    
    public static void HracStiskl (int r, int s) {      // metoda, kdy se kontroluje správnost tlačítka, které hráč stisknul
        if (hrajehrac == true) {                        // zkontrolování, zda je hráč na řadě
            if ((souradnice[0][zatimspravne] == r) && (souradnice[1][zatimspravne] == s)) { // ověření správnosti hráčova stisknutí
                zatimspravne++;
                if (zatimspravne == kolo) {             // při bezchybném zopakování celé sekvence ukáže další level
                    kolo++;
                    UkazVzor(kolo);
                }
            } else {                                    // pokud hráč zopakuje vzor špatně, vypíše se "chyba"
                hrajehrac = false;                      
                stav.setText("CHYBA! Konec hry!");
            }
        }
    }
    
    public static void UkazVzor (int pocet) {           // metoda pro zobrazení sekvence s jedním dalším prvkem
        hrajehrac = false;                              // čas pro počítač
        level.setText(" Level: " + pocet);              // přepsání levelu
        int pozice;                                     // pozice tlačítka v poli souřadnic
        for(pozice = 0; pozice < (pocet-1); pozice++) { // zopakování sekvence tlačítek z předchozích kol
            UkazPolicko(souradnice[0][pozice],souradnice[1][pozice]);
            prodleva += 600;                            // zvýšení prodlevy, aby se stále ukazovala všechna tlačítka
            pauza(100);                                 // pauza, aby bylo viditelné bliknutí, když má bliknout 2x za sebou
        }
        double d = Math.random() * 3;
        int random = (int) Math.floor(d);               //vygenerujeme souřadnice dalšího tlačítka
        souradnice[0][pozice] = random;
        d = Math.random() * 3;
        random = (int) Math.floor(d);
        souradnice[1][pozice] = random;
        UkazPolicko(souradnice[0][pozice], souradnice[1][pozice]);  // ukázání nového tlačítka hráči
        zatimspravne = 0;                               // vynulování počtu políček, která hráč zopakoval správně
        hrajehrac = true;                               // počítač přenechá hru hráči
        prodleva = 500 + (pocet * 100);                 // nastavení prodlevy na základní velikost
    }
}