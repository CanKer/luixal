/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import pl21.Automata.AutomataFD;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AFDtoAFDmin {

    AutomataFD afd;
    ArrayList<HashSet<String>> partition;

    public AFDtoAFDmin(AutomataFD afd) {
        this.afd = new AutomataFD(afd);
        this.partition = new ArrayList<HashSet<String>>();
    }

    // checks if two partitions are equals:
    public boolean equalsPartition(ArrayList<HashSet<String>> p) {
        return false;
    }

    // builds the initial partition from the AFD:
    public ArrayList<HashSet<String>> initPartition() {
        ArrayList<HashSet<String>> newPartition = new ArrayList<HashSet<String>>();
        // first group (terminals one):
        return newPartition;
    }

    // builds a new partition from the one existant:
    public ArrayList<HashSet<String>> rePartition() {
        ArrayList<HashSet<String>> newPartition = new ArrayList<HashSet<String>>();
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
