package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

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
        tre.leggInn(9);
        tre.leggInn(9);
        tre.leggInn(9);
        tre.fjernAlle(9);

        System.out.println(tre.omvendtString());
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
           /* if (verdi == null) return false;  // treet har ingen nullverdier

            Node<T> p = rot, q = null;   // q skal være forelder til p

            int wichChild = 0;


            while (p != null)            // leter etter verdi
            {
                int cmp = comp.compare(verdi,p.verdi);      // sammenligner
                if (cmp < 0) { q = p; p = p.venstre; wichChild = -1; }      // går til venstre
                else if (cmp > 0) { q = p; p = p.høyre; wichChild = 1; }   // går til høyre
                else break;    // den søkte verdien ligger i p
            }
            if (p == null) return false;   // finner ikke verdi
            if(antall==1){
                if(rot.verdi.equals(verdi)){
                    rot = null;
                    return true;
                }
                else{
                    return false;
                }
            }

            fjernVerdi(p,q,wichChild);*/


               // p.verdi = r.verdi;   // kopierer verdien i r til p



                /*if(s!=p){
                    s.venstre=r.høyre;
                }else{
                    s.høyre=r.høyre;
                }/*

            }

            /*Node<T> pParent = p.forelder;

            if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
            {
                Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
                if (p == rot) rot = b;
                else if (p == q.venstre) q.venstre = b;
                else q.høyre = b;
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

                if (s != p) s.venstre = r.høyre;
                else s.høyre = r.høyre;
            }*/

            //antall--;   // det er nå én node mindre i treet
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

            if (s != p){ s.venstre = r.høyre; r.forelder=s;}
            else {s.høyre = r.høyre;r.forelder=s;}
        }

        antall--;





        // det er nå én node mindre i treet
       /*if(p.venstre==null && p.høyre==null){
            if(wichCild==-1){
                q.venstre=null;
            }else{
                q.høyre=null;
            }
        }
        else if(p.venstre==null){
            p.høyre.forelder=q;
            q.høyre=p.høyre.forelder;
        }
        else if(p.høyre==null){
            p.venstre.forelder=q;
            q.venstre=p.venstre.forelder;

        }else {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null) {
                s = r;// s er forelder til r
                r = r.venstre;
                p.verdi = s.verdi;
                p.venstre.verdi = r.verdi;

            }
            s.forelder.venstre=null;
            s.forelder=null;
        }*/
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
                fjernStakk.push(p);
                forelderStakk.push(q);
                q = p;
                p = p.høyre;
            }
        }
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
        p.verdi=null;
    }

    @Override
    public void nullstill()
    {
        fjernTre(rot);
    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {
        if(p.høyre != null) {
            while(p.venstre != null) {
                p = p.venstre;
            }
            return p;
        } else {
            while(p.forelder != null && p.forelder.høyre == p) {
                p = p.forelder;
            }
            return p.forelder;
        }
    }

    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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

    private void antBladNoder(Node p,int cntBladNoder){

        if(p==null){
            return;
        }if(p.venstre!=null){
            antBladNoder(p.venstre,cntBladNoder);
        }
        if(p.høyre!=null) {
            antBladNoder(p.høyre, cntBladNoder);
        }

        if(p.venstre==null && p.høyre==null){
            cntBladNoder++;
        }
    }

    private void printGren(Node p,TabellListe grenVerdier,String[] gren,int ledigPlass){
        if(p==null){
            return;
        }System.out.println("KUl");
        grenVerdier.leggInn(p.verdi);
        printGren(p.venstre,grenVerdier,gren,ledigPlass);
        if(p.venstre==null && p.høyre==null){
            String ut = "";
            for(int i = 0; i <= grenVerdier.antall();i++){
                ut += grenVerdier.hent(i);


            }
            gren[ledigPlass]=ut;

            grenVerdier.fjern(grenVerdier.antall()-1);
        }
        printGren(p.høyre,grenVerdier,gren,ledigPlass);

        //grenVerdier.hent()


    }



    public String[] grener()
    {
        int ledigPlass = 0;
        int antGrener = 0;
        antBladNoder(rot,antGrener);
        String[] grener = new String[antGrener];
        boolean run = true;

        TabellListe grenTabbel = new TabellListe();
        int i = 0;
        Node p = rot;

        printGren(rot,grenTabbel,grener,ledigPlass);


        return grener;
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
