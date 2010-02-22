/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nemesis
 */

// only one transition for echar pair (state, symbol)
public class AutomataFD {

    private HashMap<String, HashMap<String, String>> transitions;

    AutomataFD() {
        this.transitions = new HashMap<String, HashMap<String, String>>();
    }

    public boolean addState(String state) {
        if (this.transitions.containsKey(state)) {
            return false;
        } else {
            this.transitions.put(state, new HashMap<String, String>());
            return true;
        }
    }

    public boolean addTransition(String orig_state, String dest_state, String symbol) {
        if (this.transitions.containsKey(orig_state) && this.transitions.containsKey(dest_state)) {
            this.transitions.get(orig_state).put(symbol, dest_state);
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        String aux = "Generic AutomataFD\n";
        aux += "States: " + this.transitions.keySet().toString() + "\n";
        for (String state:this.transitions.keySet()) {
            aux += "State: " + state + ":\n";
            for (String symbol:this.transitions.get(state).keySet()) {
                aux += "\t" + symbol + " --> " + this.transitions.get(state).get(symbol) + "\n";
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
