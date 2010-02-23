/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

import java.util.Set;

/**
 *
 * @author nemesis
 */
public abstract class Automata {

    protected String id;                    // Automata's IDentificator
//    protected HashSet<String> states;     // state's container
//    protected HashSet<String> alphabet;   // automata's alphabet
    protected String init_state;            // init state
    protected String final_state;           // final state

    Automata(String id) {
        this.id = id;
    }

    Automata() {
        this.id = "Generic Automata";
        this.init_state = "";
        this.final_state = "";
    }

    Automata(Automata a) {
        this.id = a.getId();
        this.init_state = a.getInitState();
        this.final_state = a.getFinalState();
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
    abstract boolean clearAll();
    
    // States related methods:
    abstract boolean addState(String state);
    abstract boolean isState(String state);
    abstract boolean setInitState(String state);
    abstract boolean isInitState(String state);
    abstract boolean setFinalState(String state);
    abstract boolean isFinalState(String state);
    abstract boolean removeState(String state);
    abstract Integer getNumberOfStates();

    // Alphabet related methods:
    abstract Set<String> getAlphabet();

    // Transitions related methods:
    abstract boolean addTransition(String origin_state, String dest_state, String symbol);
    abstract boolean removeTransition(String origin_state, String dest_state, String symbol);
//    abstract ArrayList<Transition> getTransitionsFromState(String state);
//    abstract ArrayList<Transition> getTransitionsToState(String state);
}
