/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import pl21.Automata.AutomataFD;
import pl21.Automata.AutomataFND;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AFDtoAFDmin {

    private AutomataFD afd;
    private ArrayList<HashSet<String>> partition;

    public AFDtoAFDmin(AutomataFD afd) {
        this.afd = new AutomataFD(afd);
        this.partition = new ArrayList<HashSet<String>>();
        this.initPartition();
    }

    // checks if two partitions are equals:
    public boolean equalsPartition(ArrayList<HashSet<String>> p) {
        return this.partition.equals(p);
    }

    // builds the initial partition from the AFD:
    public void initPartition() {
        // first group (non-terminal's one):
        this.partition.add(this.afd.getNonFinalStates());
        // second group (terminal's one):
        this.partition.add(this.afd.getFinalStates());
    }

    // return the index for the group in the partition containing the state:
    private Integer stateInGroup(String state) {
        Integer result = 0;
        
        for (int i = 0; i < this.partition.size(); i++) {
            if (this.partition.get(i).contains(state)) {
                return i;
            }
        }

        return result;
    }

    private HashMap<String, Integer> stateToGroup(String state) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        HashMap<String, String> transitionsFromState = this.afd.getTranstitionsFrom(state);

        for (String s:transitionsFromState.keySet()) {
            result.put(s, this.stateInGroup(transitionsFromState.get(s)));
        }

        return result;
    }
    
    // builds a new partition from the one existant:
    public ArrayList<HashSet<String>> rePartition() {
        ArrayList<HashSet<String>> newPartition = new ArrayList<HashSet<String>>();
        HashMap<HashMap<String,Integer>, Integer> groupsIndex = new HashMap<HashMap<String,Integer>, Integer>();

        for (HashSet group:this.partition) {
            Iterator groupIterator = group.iterator();
            while (groupIterator.hasNext()) {
                String state = (String) groupIterator.next();
                HashMap<String, Integer> index = this.stateToGroup(state);
                // if there's already a group available:
                if (groupsIndex.containsKey(index)) {
                    this.partition.get(groupsIndex.get(index)).add(state);
                } else {
                    HashSet<String> auxGroup = new HashSet<String>();
                    auxGroup.add(state);
                    this.partition.add(auxGroup);
                    // adding index to the groupsIndex:
                    groupsIndex.put(index, groupsIndex.size()+1);
                }
            }
        }
        return newPartition;
    }

    // builds the new AFD from the partition:
    public void buildNewAFD() {
    }

    // removes inactive states, unreachable states and undefined transitions:
    public void purgeAFD() {
    }

    // the minimization algorithm:
    public void minimize() {
        ArrayList<HashSet<String>> p2 = new ArrayList<HashSet<String>>();
        p2 = new ArrayList<HashSet<String>>(this.rePartition());
        System.out.println("From partitions:\n\tP: " + this.partition + "\n\tP': " + p2);

        int aux = 0;
        while (!p2.equals(this.partition)) {
            this.partition = new ArrayList<HashSet<String>>(p2);
            p2 = this.rePartition();
            System.out.println("Iteration " + aux + ":\n\tP: " + this.partition + "\n\tP': " + p2);
            aux++;
        }
    }

    public static void main(String[] args) {
        System.out.println("AFDtoAFDmin\n");
        // creating the AFND:
        RegExpToAFND regtoafnd = new RegExpToAFND();
        AutomataFND afnd = regtoafnd.TwoStacksAlgorithm("(a|b)*·a·b·b");
        afnd.renameStates(afnd.getNumberOfStates() + 1);
        afnd.renameStates(0);
        // generating AFD from AFND:
        AFNDtoAFD afntoafd = new AFNDtoAFD(afnd);
        afntoafd.conversion();
        AutomataFD afd = new AutomataFD(afntoafd.getAFD());
        afd.renameStates(0);
        // minimizing AFD:
        System.out.println("\n\nStarting Minimizer.\n");
        AFDtoAFDmin minimizer = new AFDtoAFDmin(afd);
        minimizer.minimize();
    }
}
