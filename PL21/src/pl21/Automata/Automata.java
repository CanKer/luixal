/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.HashSet;

/**
 *
 * @author Luis Alberto Pérez García
 */
public abstract class Automata {

    protected String id;                    // Automata's IDentificator
//    protected HashSet<String> states;     // state's container
    protected HashSet<String> alphabet;   // automata's alphabet
    protected String init_state;            // init state
    protected String final_state;           // final state

    Automata(String id) {
        this.id = id;
        this.init_state = "";
        this.final_state = "";
        this.alphabet = new HashSet<String>();
    }

    Automata() {
        this.id = "Generic Automata";
        this.init_state = "";
        this.final_state = "";
        this.alphabet = new HashSet<String>();
    }

    Automata(Automata a) {
        this.id = a.getId();
        this.init_state = a.getInitState();
        this.final_state = a.getFinalState();
        this.alphabet = new HashSet<String>(a.getAlphabet());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getInitState() {
        return this.init_state;
    }

    public String getFinalState() {
        return this.final_state;
    }

    // not-for-monkeys methods:
    public boolean clearAll() {
        this.final_state = "";
        this.init_state = "";
        this.id = "";
        this.alphabet.clear();
        return (this.alphabet.size() == 0);
    }
    
    // States related methods:
    abstract boolean addState(String state);
    abstract boolean addFinalState(String state);
    abstract boolean addInitState(String state);
    abstract boolean isState(String state);
    abstract boolean setInitState(String state);
    abstract boolean isInitState(String state);
    abstract boolean setFinalState(String state);
    abstract boolean isFinalState(String state);
    abstract boolean removeState(String state);
    abstract Integer getNumberOfStates();
    abstract String goTo(String state, String symbol);

    // Alphabet related methods:
    public HashSet<String> getAlphabet() {
        return this.alphabet;
    }

    protected boolean addSymbol(String symbol) {
        return this.alphabet.add(symbol);
    }

    protected boolean removeSymbol(String symbol) {
        return this.alphabet.remove(symbol);
    }

    protected boolean clearAlphabet() {
        this.alphabet.clear();
        return (this.alphabet.size() == 0);
    }

    protected boolean isSymbol(String symbol) {
        return this.alphabet.contains(symbol);
    }

    // Transitions related methods:
    abstract boolean addTransition(String origin_state, String dest_state, String symbol);
    abstract boolean removeTransition(String origin_state, String dest_state, String symbol);
//    abstract ArrayList<Transition> getTransitionsFromState(String state);
//    abstract ArrayList<Transition> getTransitionsToState(String state);
}
