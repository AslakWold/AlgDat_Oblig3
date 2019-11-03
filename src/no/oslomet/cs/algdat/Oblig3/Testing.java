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

    }
}
