/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AutomataFND extends Automata {

    private HashMap<String, HashMap<String, HashSet<String>>> graph;
//    private HashSet<String> finalStates;
    private HashMap<String, String> finalStates;

    public AutomataFND() {
        super();
        this.id += this.id;
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>();
//        this.finalStates = new HashSet<String>();
        this.finalStates = new HashMap<String, String>();
    }

    public AutomataFND(String id) {
        super(id);
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>();
//        this.finalStates = new HashSet<String>();
        this.finalStates = new HashMap<String, String>();
    }

    public AutomataFND(AutomataFND afnd) {
        super(afnd);
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>(afnd.graph);
//        this.finalStates = new HashSet<String>(afnd.getFinalStates());
        this.finalStates = new HashMap<String, String>(afnd.finalStates);
    }


    @Override
    public boolean clearAll() {
        if (super.clearAll()) {
            this.graph.clear();
            this.finalStates.clear();
            return (this.graph.size() == 0);
        }
        return false;
    }

    @Override
    public boolean addState(String state) {
        if (this.graph.containsKey(state)) {
            return true;
        } else {
            this.graph.put(state, new HashMap<String, HashSet<String>>());
            return this.graph.containsKey(state);
        }
    }

    @Override
    public boolean addFinalState(String state) {
        if (this.addState(state)) {
            this.setFinalState(state);
//            this.finalStates.add(state);
            this.finalStates.put(state, "");
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, String> getFinalStates() {
        return this.finalStates;
    }

    public void addFinalStates(HashMap<String, String> states) {
        this.finalStates.putAll(states);
    }

    @Override
    public boolean addInitState(String state) {
        if (this.addState(state)) {
            this.setInitState(state);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isState(String state) {
        return this.graph.containsKey(state);
    }

    @Override
    public boolean setInitState(String state) {
        if (this.graph.containsKey(state)) {
            this.init_state = state;
            return (this.getInitState().equals(state));
        } else {
            return false;
        }
    }

    @Override
    public boolean isInitState(String state) {
        return this.getInitState().equals(state);
    }

    @Override
    public boolean setFinalState(String state) {
        if (this.graph.containsKey(state)) {
            this.final_state = state;
            return this.getFinalState().equals(state);
        } else {
            return false;
        }
    }

    @Override
    public boolean isFinalState(String state) {
        return this.getFinalState().equals(state);
    }

    @Override
    public boolean removeState(String state) {
        if (this.graph.containsKey(state)) {
            // removing entries for 'state':
            this.graph.remove(state);
            // removing transitions going to 'state':
            for (String i:this.graph.keySet()) {
                for (String j:this.graph.get(i).keySet()) {
                    if (this.graph.get(i).get(j).contains(state)) {
                        this.graph.get(i).get(j).remove(state);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getNumberOfStates() {
        return this.graph.size();
    }

    @Override
    public boolean addTransition(String origin_state, String dest_state, String symbol) {
        if (this.graph.containsKey(origin_state) && this.graph.containsKey(dest_state)) {
            if (this.graph.get(origin_state).containsKey(symbol)) {
                return this.graph.get(origin_state).get(symbol).add(dest_state);
            } else {
                this.graph.get(origin_state).put(symbol, new HashSet<String>());
                this.addSymbol(symbol);
                return this.graph.get(origin_state).get(symbol).add(dest_state);
            }
        }
        return false;
    }

    @Override
    public boolean removeTransition(String origin_state, String dest_state, String symbol) {
        if (this.graph.containsKey(origin_state) && this.graph.containsKey(dest_state) && this.alphabet.contains(symbol)) {
            if (this.graph.get(origin_state).containsKey(symbol) && this.graph.get(origin_state).get(symbol).contains(dest_state)) {
                    if (this.graph.get(origin_state).get(symbol).size() == 1) {
                        this.graph.get(origin_state).remove(symbol);
                        return this.graph.get(origin_state).isEmpty();
                    } else {
                        return this.graph.get(origin_state).get(symbol).remove(dest_state);
                    }
            }
        }
        return false;
    }

    public void renameStates(Integer fromValue) {        
        // array for having a relationship between the old and the new names:
        ArrayList<String> statesNames = new ArrayList<String>();
        for (String s:this.graph.keySet()) {
            statesNames.add(s);
        }
        // renaming process (s for origin state, t for symbol and u for destination state:
        for (int i = 0; i < statesNames.size(); i++) {
            this.renameState(statesNames.get(i), "e" + (i + fromValue));
        }
    }

    public boolean renameState(String oldName, String newName) {
        while (this.graph.containsKey(newName)) {
            newName = newName + "_0";
        }
        // replacing old state and transitions from it:
        if (this.graph.keySet().contains(oldName)) {
            // add new state with outgoing transitions from the old one:
            this.graph.put(newName, this.graph.get(oldName));
            // removing the old state:
            this.graph.remove(oldName);
            // replacing old state incoming transitions:
            for (String s:this.graph.keySet()) {
                for (String t:this.graph.get(s).keySet()) {
                    if (this.graph.get(s).get(t).contains(oldName)) {
                        // adding new state name.
                        this.graph.get(s).get(t).add(newName);
                        // removing old state name:
                        this.graph.get(s).get(t).remove(oldName);
                    }
                }
            }
            // setting init/final state if needed:
            if (this.init_state.equals(oldName)) this.init_state = newName;
            if (this.final_state.equals(oldName)) this.final_state = newName;
            if (this.finalStates.keySet().contains(oldName)) {
                this.finalStates.put(newName, this.finalStates.get(oldName));
                this.finalStates.remove(oldName);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean addAutomataFD(AutomataFD afd, String state, String symbol) {
        if (this.isState(state)) {
            // adding the AFD:
//            this.renameStates(0);
            afd.renameStates(this.getNumberOfStates());
            this.alphabet.addAll(afd.getAlphabet());
            for (String s:afd.getGraph().keySet()) {
                for (String t:afd.getGraph().get(s).keySet()) {
                    this.addState(s);
                    this.addState(afd.getGraph().get(s).get(t));
                    this.addTransition(s, afd.getGraph().get(s).get(t), t);
                }
            }
            this.addTransition(state, afd.getInitState(), symbol);
//            this.addTransition(afd.getFinalState(), this.final_state, "#");
            for (String s:afd.getFinalStates().keySet()) {
                this.addTransition(s, this.final_state, "#");
                this.finalStates.put(s, afd.getId());
            }
            this.finalStates.put(afd.getFinalState(), afd.getId());
            return true;
        } else {
            return false;
        }
    }

//    public void addAutomataFND(AutomataFND afnd) {
//        // adding the alphabet:
//        this.alphabet.addAll(afnd.getAlphabet());
//        // adding states and transitions:
//        this.graph.putAll(afnd.getGraph());
//    }

    public void mergeAutomataFND(AutomataFND afnd) {
        this.alphabet.addAll(afnd.getAlphabet());
        // merging states and transitions:
        for (String s:afnd.getGraph().keySet()) {
            if (this.graph.keySet().contains(s)) {
                // merging here:
                for (String t:afnd.getGraph().get(s).keySet()) {
                    if (this.graph.get(s).containsKey(t)) {
                        // merge destination states here:
                        this.graph.get(s).get(t).addAll(afnd.getGraph().get(s).get(t));
                    } else {
                        // just add symbol and destination states:
                        this.graph.get(s).put(t, afnd.getGraph().get(s).get(t));
                    }
                }
            } else {
                // just adding state:
                this.graph.put(s, afnd.getGraph().get(s));
            }
        }
    }

    public HashMap<String, HashMap<String, HashSet<String>>> getGraph() {
        return this.graph;
    }

    public HashSet<String> transitionsTo(String orig_state, String symbol) {
        if (this.graph.containsKey(orig_state) && this.graph.get(orig_state).containsKey(symbol)) {
            return this.graph.get(orig_state).get(symbol);
        } else {
            return new HashSet<String>();
        }
    }

    @Override
    public String toString() {
        String aux = this.id + "\n";
        aux += "Alphabet: " + this.alphabet.toString() + "\n";
        aux += "States: " + this.graph.keySet().toString() + "\n";
        aux += "Init State: " + this.getInitState() + "\n";
        aux += "Final State(s): " + this.getFinalState() + " " + this.getFinalStates() + "\n";
        for (String state:this.graph.keySet()) {
            aux += "State: " + state + ":\n";
            for (String symbol:this.graph.get(state).keySet()) {
                aux += "\t" + symbol + " --> " + this.graph.get(state).get(symbol).toString() + "\n";
            }
        }
        return aux;
    }

    @Override
    public String goTo(String state, String symbol) {
        if (this.graph.containsKey(state) && this.graph.get(state).containsKey(symbol)) {
            return (String) this.graph.get(state).get(symbol).toArray()[0];
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        AutomataFND afnd = new AutomataFND("AP1");
        afnd.addState("e1");
        afnd.addState("e2");
//        afnd.addState("e3");
//        afnd.addState("e4");
        afnd.setInitState("e1");
        afnd.setFinalState("e2");
        afnd.addTransition("e1", "e2", "a");
//        afnd.addTransition("e1", "e2", "b");
//        afnd.addTransition("e1", "e3", "a");
//        afnd.addTransition("e1", "e4", "b");
        System.out.println(afnd);
        // Testing renameState:
//        System.out.println("Renaming state... " + afnd.renameState("e3", "e33"));
//        System.out.println(afnd);
//        afnd.renameStates(10);
//        System.out.println(afnd);
        // second afnd
        AutomataFND afnd2 = new AutomataFND("AP2");
        afnd2.addState("e1");
        afnd2.addState("e2");
        afnd2.addTransition("e1", "e2", "c");
//        afnd2.addTransition("e1", "e2", "d");
        afnd2.setInitState("e1");
        afnd2.setFinalState("e2");
        System.out.println(afnd2);
        System.out.println("Renaming states in AP1 to e1X...");
        afnd.renameStates(10);
        System.out.println("Renaming states in AP2 to e2X...");
        afnd2.renameStates(20);
        System.out.println("Showing:\n" + afnd + "\n\n" + afnd2);
        String newname = (afnd.getFinalState() + afnd2.getInitState());
        System.out.println("Renaming " + afnd.getFinalState() + " to " + newname + " and " + afnd2.getInitState() + " to " + newname + "...");
        afnd.renameState(afnd.getFinalState(), newname);
        afnd2.renameState(afnd2.getInitState(), newname);
        System.out.println("Showing:\n" + afnd + "\n\n" + afnd2);
        System.out.println("Merging both automatas...");
        afnd.mergeAutomataFND(afnd2);
        afnd.setFinalState(afnd2.getFinalState());
        System.out.println("showing result:\n\n" + afnd);
//        afnd2.renameStates(8);
//        System.out.println(afnd2);
    }
}
