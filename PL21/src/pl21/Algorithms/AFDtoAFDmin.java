/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.HashMap;
import pl21.Automata.AutomataFD;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AFDtoAFDmin {

    private AutomataFD afd;
    private HashMap<String, Integer> partition;
    private Integer numSets;

    public AFDtoAFDmin(AutomataFD afd) {
        this.afd = new AutomataFD(afd);
        this.partition = new HashMap<String, Integer>();
        this.initPartition();
        this.numSets = this.partition.size();
    }

    // checks if two partitions are equals:
    public boolean equalsPartition(HashMap<String, Integer> p) {
        return this.partition.equals(p);
    }

    // builds the initial partition from the AFD:
    public void initPartition() {
        // first group (non-terminal's one):
        for (String s:this.afd.getNonFinalStates()) {
            this.partition.put(s, 0);
        }
        // second group (terminal's one):
        for(String s:this.afd.getFinalStates()) {
            this.partition.put(s, 1);
        }
        this.numSets = this.partition.size();
    }

    // builds a new partition from the one existant:
    public HashMap<String, Integer> rePartition() {
        HashMap<String, Integer> newPartition = new HashMap<String, Integer>();
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
    }

    public static void main(String[] args) {
        System.out.println("AFDtoAFDmin\n");
    }
}
