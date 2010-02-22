/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author nemesis
 */
public abstract class Automata {

    protected String id;                // Automata's IDentificator
    protected HashSet<String> states;           // state's container
    protected HashSet<String> alphabet;         // automata's alphabet
    protected String init_state;        // init state
    protected String final_state;       // final state

    Automata(String id, HashSet states, HashSet alphabet, String init_state, String final_state) {
        this.id = id;
        this.states = states;
        this.alphabet = alphabet;
        this.init_state = init_state;
        this.final_state = final_state;
    }

    Automata(String id) {
        this.id = id;
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<String>();
        this.init_state = "";
        this.final_state = "";
    }

    Automata() {
        this.id = "Generic Automata";
        this.states = new HashSet<String>();
        this.alphabet = new HashSet<String>();
        this.init_state = "";
        this.final_state = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    // States related methods:
    public boolean addState(String state) {
        return this.states.add(state);
    }

    public boolean addInitState(String state) {
        if (this.addState(state)) {
            this.setInitState(state);
            return true;
        } else {
            return false;
        }
    }

    public boolean addFinalState(String state) {
        if (this.addState(state)) {
            this.setFinalState(state);
            return true;
        } else {
            return false;
        }
    }

    abstract boolean removeState(String state);

    public Integer numberOfStates() {
        return this.states.size();
    }

    public boolean clearStates() {
        this.states.clear();
        this.init_state = "";
        this.final_state = "";
        return (this.states.size() == 0);
    }

    public boolean isState(String state) {
        return this.states.contains(state);
    }

    public boolean isInitState(String state) {
        return (this.isState(state) && this.init_state.equals(state));
    }

    public boolean isFinalState(String state) {
        return (this.isState(state) && this.final_state.equals(state));
    }

    public boolean setInitState(String state) {
        if (this.isState(state)) {
            this.init_state = state;
            return true;
        } else {
            return false;
        }
    }

    public boolean setFinalState(String state) {
        if (this.isState(state)) {
            this.final_state = state;
            return true;
        } else {
            return false;
        }
    }

    // Alphabet related methods:

    public boolean addSymbol(String symbol) {
        return this.alphabet.add(symbol);
    }

    public boolean removeSymbol(String symbol) {
        return this.alphabet.remove(symbol);
    }

    public boolean isSymbol(String symbol) {
        return this.alphabet.contains(symbol);
    }

    public boolean clearAlphabet() {
        this.alphabet.clear();
        return (this.alphabet.size() == 0);
    }

    // Transitions related methods:
    abstract boolean addTransition(String origin_state, String dest_state, String symbol);
    abstract boolean removeTransition(String origin_state, String dest_state, String symbol);
    abstract ArrayList<Transition> getTransitionsFromState(String state);
    abstract ArrayList<Transition> getTransitionsToState(String state);
}
