package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import javax.swing.plaf.TabbedPaneUI;
import java.lang.reflect.Array;
import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{

    public static void main(String [] args){
        no.oslomet.cs.algdat.Oblig3.ObligSBinTre<Integer> tre =
                new ObligSBinTre<>(Comparator.naturalOrder());

        String s;

        tre.leggInn(6);
        tre.leggInn(7);
        tre.leggInn(91212);
        tre.leggInn(9);
        tre.leggInn(100);
        tre.leggInn(9);
        tre.leggInn(12);
        tre.leggInn(0);
        tre.leggInn(20);
        tre.leggInn(2);
        tre.leggInn(1);
        String [] grenerLOL = tre.grener();
       System.out.println(grenerLOL[0]);
        System.out.println(grenerLOL[1]);
        //System.out.println(tre.grener().toString());

        //System.out.println(tre.omvendtString());
    }


    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){ return "" + verdi;}

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public ObligSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;  //p starter i roten
        int cmp = 0;                //hjelpevariabel

        while (p != null) {          //fortsetter til p er ute av treet
            q = p;                                //q er forelder til p
            cmp = comp.compare(verdi, p.verdi);   //bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;    //flytter p
        }

        //p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);         //Oppretter ny node

        if(q == null) { rot = p; }          //p blir rotnode
        else if(cmp < 0) { q.venstre = p; } //venstre barn til q
        else {
            q.høyre = p;                      //høyre barn til q
        }

        antall++;                           //én verdi mer i treet
        return true;                        //vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        fjernVerdi(p,q);

            return true;
    }

    private void fjernVerdi(Node p, Node q){

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) rot = b;
            else if (p == q.venstre){ q.venstre = b;}
            else{ q.høyre = b;}
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p


            if (s != p){ s.venstre = r.høyre; r.forelder=s;}  //setter nye foreldre
            else {s.høyre = r.høyre;r.forelder=s;}
        }

        antall--;

    }

    public int fjernAlle(T verdi)
    {
        if (verdi == null) return 0;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        int antFjernet = 0;

        Stack<Node> fjernStakk = new Stack<>();
        Stack<Node> forelderStakk = new Stack<>();

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre;}      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre;}   // går til høyre
            else{
                fjernStakk.push(p);//Legger til noder i stacken for å fjerne senere
                forelderStakk.push(q);
                q = p;
                p = p.høyre;
            }
        }
        //Fjerner alle objekter med riktig verdi og starter i bunn
        while(!fjernStakk.isEmpty()){
            fjernVerdi(fjernStakk.pop(),forelderStakk.pop());
            antFjernet++;
        }
        return antFjernet;
    }




    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {
        int antall = 0;
        Node<T> activeNode = rot;

        //Går gjennom treet, stopper når den aktive noden er null
        while(activeNode != null){

            int compare = comp.compare(verdi, activeNode.verdi);

            if(compare<0){ //hvis verdi < activeNode.verdi

                activeNode = activeNode.venstre;

            } else {  //hvis verdi >= activeNode.verdi

                if(compare == 0) { //hvis verdi == activeNode.verdi
                    antall ++;
                }

                activeNode = activeNode.høyre;
            }
        }

        return antall;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    public void fjernTre(Node p){
        if(p==null){
            return;
        }
        //Løper gjennom hele treet rekursivt
        fjernTre(p.venstre);
        fjernTre(p.høyre);

        //Setter alle verdier til null
        p.forelder=null;
        p.venstre=null;
        p.høyre=null;
        //Nullstiller siste node til slutt
        if(p==rot){
            rot=null;
        }
        antall--;
    }

    @Override
    public void nullstill()
    {
        //Bruker hjelpemetode for å nullstille ved hjelp av rekusojn.
        fjernTre(rot);
    }

    /*
    public static void nesteInordenTest() {
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a) {
            tre.leggInn(verdi);
        }
        System.out.println(nesteInorden(tre.rot.høyre.venstre.høyre));
    } */
    private static <T> Node<T> nesteInorden(Node<T> p)
    {       //Må håndtere når man har flere høyre barn på rad...
        if(p.høyre != null) {
            while(p.venstre != null) {
                p = p.venstre;
            }
            return p;
        }
        else {
            while(p.forelder != null && p.forelder.høyre == p) {
                p = p.forelder;
            }
            return p.forelder;
        }
    }

    @Override
    public String toString()
    {
        StringJoiner sj = new StringJoiner(", ","[", "]");
        Node<T> p = rot;

        p = nesteInorden(p);


        while(p != null) {
            sj.add(String.valueOf(p.verdi));
            p = nesteInorden(p);
        }

        while(p.venstre != null) {
            sj.add(String.valueOf(p.verdi));
            p = nesteInorden(p);

        }

        return sj.toString();
    }

    public String omvendtString()
    {

        //if(rot==null) return "[]";
        if(tom()) return "[]";

        StringBuilder utString = new StringBuilder("[");
        Stack<Node<T>> stack = new Stack<>();
        //Lager en deque som holder på verdiene som skal skrives ut
        ArrayDeque<Node<T>> finalStack = new ArrayDeque<>();
        Node<T> p = rot;

        //Starter i roten og går nedover mot venstre i treet
        for( ; p.venstre != null; p = p.venstre){
            //Legger til alle nodene som paseres i en stack
            stack.push(p);
        }

        while(true){
            //Legger til noden i hjelpestacken
            finalStack.addFirst(p);

            // Hvis noden har et høyre barn
            if(p.høyre != null){
                //Går nedover mot venstre i treet
                for( p = p.høyre; p.venstre != null; p = p.venstre){
                    //Alle noder som paseres legges i en stack
                    stack.push(p);
                }
            }
            else if(!stack.isEmpty()){
                //Plukker ut nodene som ligger i stacken
                p = stack.pop();
            }
            else {
                break;
            }
        }

        //Tar ut fra slutten av finalStack og legger til i utString
        while(!finalStack.isEmpty()){
            utString.append(finalStack.pop());
            if(finalStack.peek() == null){
                utString.append("]");
            }
            else utString.append(", ");
        }

        return utString.toString();

    }

    public String høyreGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    //Hjelpe metode som sansynligvis fjernes før innlevering
    private int antBladNoder(Node p,int cntBladNoder){

        if(p==null){
            return 0;
        }
        antBladNoder(p.venstre,cntBladNoder);


        antBladNoder(p.høyre, cntBladNoder);


        if(p.venstre==null && p.høyre==null) cntBladNoder++;
        return cntBladNoder;
    }

    private void printGren(Node p,TabellListe grenVerdier,TabellListe gren){
        if(p==null){
            return;
        }
        //Legger inn verdi
        grenVerdier.leggInn(p.verdi);
        printGren(p.venstre,grenVerdier,gren);
        //Sjekker om det er en bladnode
        if(p.venstre==null && p.høyre==null){
            String ut = "[";
            for(int i = 0; i <= grenVerdier.antall()-1;i++){
                ut += grenVerdier.hent(i);
                if(i!=grenVerdier.antall()-1){
                    ut+=", ";
                }
            }
            ut+="]";

            //Legger grenen inn i egen tabellListe
            gren.leggInn(ut);

        }
        printGren(p.høyre,grenVerdier,gren);
        //Fjerner siste objekt for å finne ny gren.
        grenVerdier.fjern(grenVerdier.antall()-1);


    }



    public String[] grener()
    {

        //Lager utArray og hjelpetabeller
        String[] grenerUt = new String[antall/2 +1];
        TabellListe grener = new TabellListe();
        TabellListe grenTabbel = new TabellListe();


        //Kjører hjelpemetode for å kunne bruke rekusjon.
        printGren(rot,grenTabbel,grener);

        //Legger inn grenene i utArray fra Tabeller som var med i rekusjon.
        for(int i = 0; i<=grener.antall()-1;i++){
            grenerUt[i]=(String)grener.hent(i);
        }


        return grenerUt;
    }

    private void bladnodeVerdierRekurson(Node<T> p, StringBuilder str){
        if(p == null) return;

        bladnodeVerdierRekurson(p.venstre, str);
        if(p.venstre == null && p.høyre == null){
            str.append(p.verdi + ", ");
        }
        bladnodeVerdierRekurson(p.høyre, str);
    }

    public String bladnodeverdier()
    {
        if(tom()) return "[]";

        StringBuilder utString = new StringBuilder("[");
        Node<T> p = rot;

        bladnodeVerdierRekurson(p, utString);
        //Formaterer strengen
        utString.delete(utString.length()-2, utString.length());
        utString.append("]");

        return utString.toString();
    }

    public String postString()
    {
        if(tom()) return "[]";

        //Bruker her to stacks, der den ene holder på verdiene til
        //treet i motsatt postorden, deretter legges dette riktig
        //igjen i motsatt rekkefølge inn i den andre stacken,

        StringBuilder str = new StringBuilder("[");
        Stack<Node<T>> stack_one = new Stack<>();
        Stack<Node<T>> stack_two = new Stack<>();
        Node<T> p = rot;

        stack_one.push(p);
        while(!stack_one.isEmpty()){
            //Pop fra stack_one og push til stack_two
            Node<T> temp = stack_one.pop();
            stack_two.push(temp);

            //Push venstre og høyre barn til
            //tempnoden til stakk 1
            if(temp.venstre != null){
                stack_one.push(temp.venstre);
            }
            if(temp.høyre != null){
                stack_one.push(temp.høyre);
            }
        }

        while(!stack_two.isEmpty()){
            Node<T> temp = stack_two.pop();
            str.append(temp.verdi + ", ");
        }


        //Formater str
        str.delete(str.length()-2, str.length());
        str.append("]");
        return str.toString();

    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

    } // BladnodeIterator

} // ObligSBinTre
