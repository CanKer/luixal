/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author nemesis
 */

// only one transition for echar pair (state, symbol)
public class AutomataFD extends Automata {

    private HashMap<String, HashMap<String, String>> graph;

    AutomataFD() {
        super();
        this.id += this.id + " - AFD";
        this.graph = new HashMap<String, HashMap<String, String>>();
    }

    AutomataFD(String id) {
        super(id);
        this.graph = new HashMap<String, HashMap<String, String>>();
    }

    AutomataFD(AutomataFD afd) {
        super(afd);
        this.graph = new HashMap<String, HashMap<String, String>>(afd.graph);
    }

    public boolean addState(String state) {
        if (this.graph.containsKey(state)) {
            return false;
        } else {
            this.graph.put(state, new HashMap<String, String>());
            return true;
        }
    }

    public boolean addTransition(String orig_state, String dest_state, String symbol) {
        if (this.graph.containsKey(orig_state) && this.graph.containsKey(dest_state)) {
            this.graph.get(orig_state).put(symbol, dest_state);
            return true;
        } else {
            return false;
        }
    }

    @Override
    boolean clearAll() {
        this.graph.clear();
        return (this.graph.size() == 0);
    }

    @Override
    boolean isState(String state) {
        return this.graph.containsKey(state);
    }

    @Override
    boolean setInitState(String state) {
        if (this.graph.containsKey(state)) {
            this.init_state = state;
            return true;
        } else {
            return false;
        }
    }

    @Override
    boolean isInitState(String state) {
        return this.init_state.equals(state);
    }

    @Override
    boolean setFinalState(String state) {
        if (this.graph.containsKey(state)) {
            this.final_state = state;
            return true;
        } else {
            return false;
        }
    }

    @Override
    boolean isFinalState(String state) {
        return this.final_state.equals(state);
    }

    @Override
    boolean removeState(String state) {
        throw new UnsupportedOperationException("Not supported yet.");
        // removing 'state' from graph's keys
        // removing transitions in which 'state' appears as destination state
    }

    @Override
    Integer getNumberOfStates() {
        return this.graph.size();
    }

    @Override
    Set<String> getAlphabet() {
        throw new UnsupportedOperationException("Not supported yet.");
        // mantaining a set variable for alphabet??
        // going through every transition populating the returning set??
    }

    @Override
    boolean removeTransition(String origin_state, String dest_state, String symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toString() {
        String aux = this.id + "\n";
        aux += "States: " + this.graph.keySet().toString() + "\n";
        for (String state:this.graph.keySet()) {
            aux += "State: " + state + ":\n";
            for (String symbol:this.graph.get(state).keySet()) {
                aux += "\t" + symbol + " --> " + this.graph.get(state).get(symbol) + "\n";
            }
        }
        aux += "\n\nEnd of Generic AutomataFD.";
        return aux;
    }

    public static void main(String[] args) {
        System.out.println("Running AutomataFD.Main...\n");
        System.out.print("Creating a generic AutomataFD... ");
        AutomataFD afd = new AutomataFD();
        System.out.println("DONE!");
        System.out.println("Adding States:");
        System.out.print("\tState 'e1'... ");
        afd.addState("e1");
        System.out.println("DONE!");
        System.out.print("\tState 'e2'... ");
        afd.addState("e2");
        System.out.println("DONE!");
        System.out.print("\tState 'e3'... ");
        afd.addState("e3");
        System.out.println("DONE!");
        System.out.print("\tState 'e4'... ");
        afd.addState("e4");
        System.out.println("DONE!");
        System.out.println("Adding transitions:");
        System.out.print("Transition from 'e1' to 'e2' through symbol 'a'... ");
        afd.addTransition("e1", "e2", "a");
        System.out.println("DONE!");
        System.out.print("Transition from 'e2' to 'e3' through symbol 'b'... ");
        afd.addTransition("e2", "e3", "b");
        System.out.println("DONE!");
        System.out.print("Transition from 'e2' to 'e4' through symbol 'a'... ");
        afd.addTransition("e2", "e4", "a");
        System.out.println("DONE!");
        System.out.print("Transition from 'e4' to 'e3' through symbol 'b'... ");
        afd.addTransition("e4", "e3", "b");
        System.out.println("DONE!");
        System.out.println("\n\nNow showing the automata (toString test :P):");
        System.out.println(afd);
    }
}
