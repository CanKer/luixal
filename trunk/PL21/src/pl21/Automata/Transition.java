/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Automata;

/**
 *
 * @author nemesis
 */
public class Transition {

    String orig_state;
    String dest_state;
    String symbol;

    Transition(String orig_state, String dest_state, String symbol) {
        this.orig_state = orig_state;
        this.dest_state = dest_state;
        this.symbol = symbol;
    }

    public String getOrig_state() {
        return this.orig_state;
    }
    
    public String getDest_state() {
        return this.dest_state;
    }

    public String getSymbol() {
        return this.symbol;
    }

}
