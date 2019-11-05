package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;
import java.util.Iterator;

import static java.util.Comparator.naturalOrder;


public class Testing {

    public static void main(String[] args){
        //Tester oppg 3



        {
            int[] a = {4,7,2,9,4,10,8,7,4,6,1};
            ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for(int verdi : a) {
                tre.leggInn(verdi);
            }
            System.out.println(tre);
        }


        /*

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

        */

        //Tester oppg 9

        {
            String[] strenger = {"H", "Q", "B", "F", "C", "P", "U"};
            ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for (String verdi: strenger) {
                tre.leggInn(verdi);
            }

            Iterator<String> it = tre.iterator();

        }
    }
}
