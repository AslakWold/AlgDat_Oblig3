package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{


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
                rot=null;
            }

            fjernVerdi(p,q,wichChild);


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

            antall--;   // det er nå én node mindre i treet
            return true;
    }

    private void fjernVerdi(Node p, Node q, int wichCild){
        if(p.venstre==null && p.høyre==null){
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
        }
    }

    public int fjernAlle(T verdi)
    {
        TabellStakk<T> fjernTall = new TabellStakk<>();
        if (verdi == null) return 0;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        int whichCild = 0;

        int antFjernet = 0;


        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre;whichCild=-1;}      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre;whichCild=1;}   // går til høyre
            else {
                fjernVerdi(p,q,whichCild);
                antFjernet++;
            }

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
        fjernTre(p.venstre);
        fjernTre(p.høyre);

        p.forelder=null;
        p.venstre=null;
        p.høyre=null;
    }

    @Override
    public void nullstill()
    {
        Node<T> p = rot;
        Node<T> q = null;


        /*while(rot!=null){

        }*/
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

    public String[] grener()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
