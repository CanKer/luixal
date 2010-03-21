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

    AutomataFD afd;
    HashMap<String, Integer> partition;

    public AFDtoAFDmin(AutomataFD afd) {
        this.afd = new AutomataFD(afd);
        this.partition = new HashMap<String, Integer>();
    }

    // checks if two partitions are equals:
    public boolean equalsPartition(HashMap<String, Integer> p) {
        return false;
    }

    // builds the initial partition from the AFD:
    public HashMap<String, Integer> initPartition() {
        HashMap<String, Integer> newPartition = new HashMap<String, Integer>();
        // first group (terminals one):
        return newPartition;
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
