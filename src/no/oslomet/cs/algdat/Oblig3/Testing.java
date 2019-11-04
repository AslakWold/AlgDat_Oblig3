package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

import static java.util.Comparator.naturalOrder;

public class Testing {

    public static void main(String[] args){

        {
            String[] strenger = {"H", "Q", "B", "A", "C", "P", "U"};
            ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for (String verdi : strenger) {
                tre.leggInn(verdi);
            }

            System.out.println(tre.antall());
            System.out.println(tre.inneholder("U"));
            System.out.println();
        }




        //Tester oppg 4

        {
            String[] strenger = {"H", "Q", "B", "A", "C", "P", "U"};
            ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for (String verdi: strenger) {
                tre.leggInn(verdi);
            }

            System.out.println(tre.omvendtString());

        }

        //Tester oopgave 8
        {
            String[] strenger = {"I","A","T","B","H","J","C","R","S","O","F","E","L","K","G","D","M","P","Q","N"};
            //String[] strenger = {"I","A","T","B","H","J","C","R","S","O"};
            ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for (String verdi: strenger) {
                tre.leggInn(verdi);
            }
            System.out.println(tre.bladnodeverdier());


            System.out.println(tre.postString());
        }


    }
}
