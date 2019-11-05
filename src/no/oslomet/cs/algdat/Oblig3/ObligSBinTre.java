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
        endringer++;
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

        //fjernVerdi(p,q);

        fjernVerdi(p);

        antall--;

        return true;
    }

    public static Node finnMinste(Node p){
        if(p.venstre==null){
            return p;
        }

        return finnMinste(p.venstre);
    }
    private Node fjernVerdi(Node p){

        //Noden som slettes har to barns
        if(p.venstre!=null && p.høyre!=null){
            Node minste = finnMinste(p.høyre);
            p.verdi = minste.verdi;
            fjernVerdi(minste); //Kjører metoden en gang til for minste.

        }
        //Noden som skal slettes har ingen barn
        else if(p.høyre==null && p.venstre==null){
            if(p.equals(rot)){
                rot=null;
                p.forelder=null;
            }else if(p==p.forelder.venstre){
                p.forelder.venstre=null;
                p=null;
            }else{
                p.forelder.høyre=null;
                p=null;
            }
        }
        //Noden som skal sletter er roten med kun ett barn
        else if(p==rot){
            if(p.venstre!=null){
                p.venstre.forelder=null;
                rot=p.venstre;
            }else{
                p.høyre.forelder=null;
                rot=p.høyre;
            }
        }
        //Noden som skal slettes har kun ett barn
        else{
            if(p.høyre!=null){

                Node pMin = finnMinste(p.høyre); //Finner minste i høyre tre.
                p.verdi=pMin.verdi; //Setter ny verdi for p
                fjernVerdi(pMin); //Fjerner gamle pMin siden verdien nå ligger i p
            }else{

                Node fjern = p.venstre; //Setter fjernNode til venstre barn
                p.verdi=fjern.verdi; //Finner ny verdi for p.
                fjernVerdi(fjern); //Kjører metoden på nytt og fjerner fjern Noden.
            }
        }
        endringer++;
        return p;

    }

    public int fjernAlle(T verdi)
    {
        if(antall==0) return 0;
        if (verdi == null) return 0;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        int antFjernet = 0;

        Stack<Node> fjernStakk = new Stack<>();

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre;}      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre;}   // går til høyre
            else{
                fjernStakk.push(p);//Legger til noder i stacken for å fjerne senere
                p = p.høyre;
            }
        }
        //Fjerner alle objekter med riktig verdi og starter i bunnen av treet ved hjelp av stacken.
        while(!fjernStakk.isEmpty()){
            fjernVerdi(fjernStakk.pop());
            antFjernet++;
            antall--;
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
        //Nullstiller rotnoden til slutt
        if(p==rot){
            rot=null;
        }

        antall--;
        endringer++;
    }

    @Override
    public void nullstill()
    {
        //Bruker hjelpemetode for å nullstille ved hjelp av rekusojn.
        fjernTre(rot);
    }


    private static <T> Node<T> førsteInorden(Node<T> p) {
        while(p.venstre != null) {
            p = p.venstre;
        }
        return p;
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        if (p.høyre != null) {
            return førsteInorden(p.høyre);
        }
        else {
            while (p.forelder != null && p.forelder.høyre == p) {
                p = p.forelder;
            }
            return p.forelder;
        }
    }

    @Override
    public String toString()
    {
        if(tom()) {
            return "[]";
        }
        StringJoiner sj = new StringJoiner(", ","[", "]");
        Node<T> p = førsteInorden(rot);

        while(p != null) {
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
        if(rot == null){
            return "[]";
        }
        Node thisNode = rot;
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        stringJoiner.add(thisNode.verdi.toString());
        while(thisNode.høyre != null || thisNode.venstre != null){
            if(thisNode.høyre != null){
                thisNode = thisNode.høyre;
                stringJoiner.add(thisNode.verdi.toString());
            }
            else{
                thisNode = thisNode.venstre;
                stringJoiner.add(thisNode.verdi.toString());
            }
        }
        return stringJoiner.toString();
    }

    public String lengstGren()
    {
        if(rot == null){
            return "[]";
        }
        String[] grener = grener();
        int max = 0;
        int antall = 0;
        String størsteGren = "";
        for(int i = 0; i < grener.length; i++){
            String enkeltGren = grener[i];
            String[] antallElementer = enkeltGren.split(",");
            antall = antallElementer.length;
            if(antall > max){
               størsteGren = enkeltGren;
               max = antall;
            }
        }
        return størsteGren;
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

            StringBuilder stringUt = new StringBuilder("[");
            //Legger inn
            for(int i = 0; i <= grenVerdier.antall()-1;i++) {
                stringUt.append(grenVerdier.hent(i));
                if (i == grenVerdier.antall() - 1) {
                    stringUt.append("]");
                } else {
                    stringUt.append(", ");
                }
            }
            //Legger grenen inn i egen tabellListe
            gren.leggInn(stringUt);

            }

        printGren(p.høyre,grenVerdier,gren);
        //Fjerner siste objekt for å finne ny gren.
        grenVerdier.fjern(grenVerdier.antall()-1);

    }




    public String[] grener()
    {
        if(antall==0){
            return new String[0];
        }

        //Lager utArray og hjelpetabeller
        TabellListe grener = new TabellListe();
        TabellListe grenTabbel = new TabellListe();


        //Kjører hjelpemetode for og kunne bruke rekusjon.
        printGren(rot,grenTabbel,grener);

        String[] grenerUt = new String[grener.antall()];

        //Legger inn grenene i utArray fra Tabeller som var med i rekusjon.
        for(int i = 0; i<=grener.antall()-1;i++){
            grenerUt[i]=grener.hent(i).toString();
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
            if(tom()) return;

            p = finnFørsteBladnode(p);
            System.out.println(p);

        }

        private Node<T> finnFørsteBladnode(Node<T> p){
            if(p.venstre == null && p.høyre == null){
                return p;
            }
            else if(p.venstre != null){
                return finnFørsteBladnode(p.venstre);
            }else {
                return finnFørsteBladnode(p.høyre);
            }


        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next(){

            if(!hasNext()){
                throw new NoSuchElementException("Ingen flere bladnoder");
            } else if(endringer != iteratorendringer){
                throw new ConcurrentModificationException("Endringer stemmer ikke med iteratorendringer");
            }


            removeOK = true;
            T temp = p.verdi; //Holder på verdien til p
            q = p;
            while(hasNext()){
                p = nesteInorden(p); // Bruker nesteInorden

                if(p == null){
                    return temp;
                }
                if(p.høyre == null && p.venstre == null){ //hvis p er bladnode
                    return temp;
                }

            }


            return temp;


        }

        @Override
        public void remove()
        {
            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("Iterasjonsendringer er ikke det samme som nodens endringer");
            }else if(!removeOK){
                throw new IllegalStateException("Fjern ikke OK");
            }
            removeOK=false;
            if(q == rot){
                rot = null;
            }
            else if(q.forelder.venstre == q){
                q.forelder.venstre = null;
            } else if(q.forelder.høyre == q){
                q.forelder.høyre = null;
            }
            iteratorendringer++;
            endringer++;
            antall--;


        }

    } // BladnodeIterator

} // ObligSBinTre
