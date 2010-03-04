/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AutomataFND extends Automata {

    private HashMap<String, HashMap<String, HashSet<String>>> graph;

    public AutomataFND() {
        super();
        this.id += this.id + " - AFND";
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>();
    }

    public AutomataFND(String id) {
        super(id);
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>();
    }

    public AutomataFND(AutomataFND afnd) {
        super(afnd);
        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>(afnd.graph);
    }


    @Override
    public boolean clearAll() {
        if (super.clearAll()) {
            this.graph.clear();
            return (this.graph.size() == 0);
        }
        return false;
    }

    @Override
    public boolean addState(String state) {
        if (this.graph.containsKey(state)) {
            return false;
        } else {
            this.graph.put(state, new HashMap<String, HashSet<String>>());
            return true;
        }
    }

    @Override
    public boolean addFinalState(String state) {
        if (this.addState(state)) {
            this.setFinalState(state);
            return true;
        } else {
            return false;
        }
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

    public void renameStates() {
        // for now we're just adding an "'" to the state name. Better for debugging and easier to implement ;)
        HashMap<String, HashMap<String, HashSet<String>>> aux = new HashMap<String, HashMap<String, HashSet<String>>>();
        
        for (String s:this.graph.keySet()) {
            aux.put(s + "'", new HashMap<String, HashSet<String>>());
            for (String t:this.graph.get(s).keySet()) {
                aux.get(s+"'").put(t, new HashSet<String>());
                for (String u:this.graph.get(s).get(t)) {
                    aux.get(s+"'").get(t).add(u+"'");
                }
            }
        }

        this.graph = new HashMap<String, HashMap<String, HashSet<String>>>(aux);
        // renaming initial and final states:
        this.setInitState(this.getInitState() + "'");
        this.setFinalState(this.getFinalState() + "'");
    }

    public void addAutomataFND(AutomataFND afnd) {
        // adding the alphabet:
        for (String s:afnd.getAlphabet()) {
            this.alphabet.add(s);
        }
        // adding states and transitions:
        for (String s:afnd.getGraph().keySet()) {
            this.graph.put(s, afnd.getGraph().get(s));
        }
    }

    public HashMap<String, HashMap<String, HashSet<String>>> getGraph() {
        return this.graph;
    }

    @Override
    public String toString() {
        String aux = this.id + "\n";
        aux += "Alphabet: " + this.alphabet.toString() + "\n";
        aux += "States: " + this.graph.keySet().toString() + "\n";
        aux += "Init State: " + this.getInitState() + "\n";
        aux += "Final State: " + this.getFinalState() + "\n";
        for (String state:this.graph.keySet()) {
            aux += "State: " + state + ":\n";
            for (String symbol:this.graph.get(state).keySet()) {
                aux += "\t" + symbol + " --> " + this.graph.get(state).get(symbol).toString() + "\n";
            }
        }
        return aux;
    }

    public static void main(String[] args) {
        AutomataFND afnd = new AutomataFND("de pruebas :P");
        afnd.addState("e1");
        afnd.addState("e2");
        afnd.addState("e3");
        afnd.addState("e4");
        afnd.addTransition("e1", "e2", "a");
        afnd.addTransition("e1", "e2", "b");
        afnd.addTransition("e1", "e3", "a");
        afnd.addTransition("e1", "e4", "b");
        System.out.println(afnd);
        // second afnd
        AutomataFND afnd2 = new AutomataFND("de pruebas :P");
        afnd2.addState("e1");
        afnd2.addState("e2");
        afnd2.addTransition("e1", "e2", "c");
        afnd2.addTransition("e1", "e2", "d");
        System.out.println(afnd2);
//        afnd.addState("e5");
//        afnd.addTransition("e2", "e5", "a");
//        afnd.addTransition("e2", "e5", "b");
//        afnd.addTransition("e2", "e5", "c");
//        afnd.setFinalState("e5");
//        afnd.setInitState("e1");
//        System.out.println(afnd);
//        afnd.removeTransition("e2", "e5", "a");
//        System.out.println(afnd);
//        afnd.removeState("e2");
//        System.out.println(afnd);
        afnd2.renameStates();
        System.out.println(afnd2);
        afnd.addAutomataFND(afnd2);
        System.out.println(afnd);
    }
}
