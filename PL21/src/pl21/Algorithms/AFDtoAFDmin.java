/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import pl21.InputOutput.ImageOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
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

    // builds the initial partition from the AFD:
    public void initPartition() {
        // first group (non-terminal's one):
        this.partition.add(this.afd.getNonFinalStates());
        // second group (terminal's one):
        this.partition.add( new HashSet<String>(this.afd.getFinalStates().keySet()));
    }

    // returns the index for the group in the partition containing the state:
    private Integer stateInGroup(String state) {
        Integer result = 0;
        
        for (int i = 0; i < this.partition.size(); i++) {
            if (this.partition.get(i).contains(state)) {
                return i;
            }
        }

        return result;
    }

    private boolean equalTokens(String s1, String s2) {
        if (!this.afd.isFinalState(s1) || !this.afd.isFinalState(s2)) return false;
        // pueden tener longitudes diferentes!!!
        if (s1.length() != s2.length()) return false;
        StringTokenizer stk1 = new StringTokenizer(s1, ",");
        StringTokenizer stk2 = new StringTokenizer(s2, ",");
        if (stk1.countTokens() != stk2.countTokens()) return false;
        return true;
    }

    // checks if the 'state' can join the group 'states' of the partition (checking if those states were NOT together in the previous partition).
    private boolean isJoinable(HashSet<String> states, String state) {
        boolean result = true;
        for (String s:states) {
            result &= (this.stateInGroup(s) == this.stateInGroup(state));
            if (this.afd.isFinalState(s) && this.afd.isFinalState(state)) {
                result &= this.afd.getFinalStates().get(s).equals(this.afd.getFinalStates().get(state));
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
            if (group.size() > 1) {
                Iterator groupIterator = group.iterator();
                while (groupIterator.hasNext()) {
                    String state = (String) groupIterator.next();
                    HashMap<String, Integer> index = this.stateToGroup(state);
                    // if there's already a group available:
                    if (groupsIndex.containsKey(index) && this.isJoinable(newPartition.get(groupsIndex.get(index)), state)) {
                        newPartition.get(groupsIndex.get(index)).add(state);
                    } else {
                        HashSet<String> auxGroup = new HashSet<String>();
                        auxGroup.add(state);
                        newPartition.add(auxGroup);
                        // adding index to the groupsIndex:
                        groupsIndex.put(index, newPartition.size()-1);
                    }
                }
            } else {
                newPartition.add(group);
            }
        }
        return newPartition;
    }

    private String containsFinalStates(HashSet<String> hs) {
        for (String s:hs) {
            if (this.afd.getFinalStates().containsKey(s)) return s;
        }
        return null;
    }

    // builds the new AFD from the partition:
    public void buildNewAFD() {
        AutomataFD newAFD = new AutomataFD();
        HashMap<Integer, String> representantStates = new HashMap<Integer, String>();
        // select representant states setting init and final states:
        for (HashSet hs:this.partition) {
            Iterator it = hs.iterator();
            String state = (String) it.next();
            representantStates.put(this.stateInGroup(state), state);
            newAFD.addState(state);
            // set as init state:
            if (hs.contains(this.afd.getInitState())) {
                newAFD.setInitState(state);
            }
            // set as final state:
            if (this.afd.isFinalState(state)) {
                newAFD.setFinalState(state);
                newAFD.addFinalState(state);
                newAFD.getFinalStates().put(state, this.afd.getFinalStates().get(state));
            }
        }
        // build newAFD using representant states
        for (String state:newAFD.getStates()) {
            HashMap<String, Integer> transitions = new HashMap<String, Integer>(this.stateToGroup(state));
            for (String symbol:transitions.keySet()) {
                newAFD.addTransition(state, representantStates.get(transitions.get(symbol)), symbol);
                
            }
        }
        this.purgeAFD();
        this.afd = new AutomataFD(newAFD);
    }

    // removes inactive states, unreachable states and undefined transitions:
    public void purgeAFD() {
        // remove inactive states
        for (String state:this.afd.getNonFinalStates()) {
            boolean inactive = true;
            for (String s:this.afd.getTranstitionsFrom(state).keySet()) {
                inactive = inactive && this.afd.getTranstitionsFrom(state).get(s).equals(state);
            }
            if (inactive) {
                this.afd.removeState(state);
            }
        }
        // remove unreachable states
        for (String state:this.afd.getStates()) {
            //if (!this.afd.stateReachableFrom(this.afd.getInitState(), state)) this.afd.removeState(state);
            if (!this.afd.stateReachableFromNonRecursive(this.afd.getInitState(), state)) this.afd.removeState(state);
        }
    }

    // the minimization algorithm:
    public void minimize() {
        ArrayList<HashSet<String>> p2 = new ArrayList<HashSet<String>>();
        p2 = new ArrayList<HashSet<String>>(this.rePartition());

        int aux = 0;
        while (!p2.equals(this.partition)) {
            this.partition = new ArrayList<HashSet<String>>(p2);
            p2 = this.rePartition();
//            System.out.println("Repartition " + aux + ": " + p2);
            aux++;
        }
    }

    public AutomataFD getAFD() {
        return this.afd;
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
        minimizer.buildNewAFD();
        ImageOutput sgFD = new ImageOutput(minimizer.getAFD(), "out.png");
        sgFD.writeFile();
    }
}
