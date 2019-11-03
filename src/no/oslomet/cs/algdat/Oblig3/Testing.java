package no.oslomet.cs.algdat.Oblig3;

import java.util.Comparator;

import static java.util.Comparator.naturalOrder;
//import static no.oslomet.cs.algdat.Oblig3.ObligSBinTre.nesteInordenTest;

public class Testing {

    public static void main(String[] args){
        /*
        {
            String[] strenger = {"H", "Q", "B", "A", "C", "P", "U"};
            ObligSBinTre<String> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for (String verdi : strenger) {
                tre.leggInn(verdi);
            }

            System.out.println(tre.antall());
            System.out.println(tre.inneholder("U"));
            System.out.println();



            System.out.println(tre);

        }*/

        {
            int[] a = {4,7,2,9,4,10,8,7,4,6,1};
            ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
            for(int verdi : a) {
                tre.leggInn(verdi);
            }
            System.out.println(tre);
        }

            //nesteInordenTest();

    }
}
