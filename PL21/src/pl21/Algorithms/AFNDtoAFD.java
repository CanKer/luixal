/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import pl21.Automata.AutomataFD;
import pl21.Automata.AutomataFND;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class AFNDtoAFD {

    AutomataFND afnd;
    AutomataFD afd;

    public AFNDtoAFD(AutomataFND afnd) {
        this.afnd = new AutomataFND(afnd);
        this.afd = new AutomataFD();
    }
    
    public HashSet<String> lambdaClosure(HashSet<String> S) {
        Stack<String> stack = new Stack<String>();
        HashSet<String> R = new HashSet<String>();
        // init:
        stack.addAll(S);
        R.addAll(S);
        // populating result:
        while (!stack.isEmpty()) {
            String s = stack.pop();
            if (this.afnd.getGraph().get(s).containsKey("#")) {
                for (String t:this.afnd.getGraph().get(s).get("#")) {
                    if (!R.contains(t)) {
                        R.add(t);
                        stack.push(t);
                    }
                }
            }
        }
        // returning result:
        return R;
    }

    public HashSet<String> lambdaClosure(String s) {
        HashSet<String> aux = new HashSet<String>();
        aux.add(s);
        return this.lambdaClosure(aux);
    }

    // the f****** 'move'
    public HashSet<String> transitionsTo(HashSet<String> S, String symbol) {
        HashSet<String> R = new HashSet<String>();
        for (String s:S) {
            R.addAll(this.afnd.transitionsTo(s, symbol));
        }
        return R;
    }

    public HashSet<String> transitionsTo(String S, String symbol) {
        HashSet<String> aux = new HashSet<String>();
        aux.add(S);
        return this.transitionsTo(aux, symbol);
    }

    // returns a name for the union state:
    public String generateStateName(HashSet<String> S) {
        String r = "e";
        for (String s:S) {
            r += s.substring(1);
        }
        return r;
    }

    public AutomataFD getAFD() {
        return this.afd;
    }

    public void conversion() {
        // let's declare & init the 'tools':
        this.afd.getAlphabet().addAll(this.afnd.getAlphabet());
        this.afd.getAlphabet().remove("#");
        ArrayList<HashSet<String>> unmarkedStates = new ArrayList<HashSet<String>>();
        HashSet<String> U = new HashSet<String>();
        HashSet<String> T = new HashSet<String>();

        U = this.lambdaClosure(this.afnd.getInitState());
        unmarkedStates.add(U);
        this.afd.addState(this.generateStateName(U));
        this.afd.setInitState(this.generateStateName(U));

        while (!unmarkedStates.isEmpty()) {
            // marking T:
            T = unmarkedStates.remove(0);
            // marked.
            for (String s:this.afd.getAlphabet()) {
                U = this.lambdaClosure(this.transitionsTo(T, s));
                if (!U.isEmpty()) {
                    if (!this.afd.isState(this.generateStateName(U))) {
                        // adding as unmarked state:
                        unmarkedStates.add(U);
                        this.afd.addState(this.generateStateName(U));
                    }
                    this.afd.addTransition(this.generateStateName(T), this.generateStateName(U), s);
                    if (U.contains(this.afnd.getFinalState())) this.afd.setFinalState(this.generateStateName(U));
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RegExpToAFND regtoafnd = new RegExpToAFND();
        AutomataFND afnd = regtoafnd.TwoStacksAlgorithm("(a|b)*·a·b·b");
        afnd.renameStates(afnd.getNumberOfStates() + 1);
        afnd.renameStates(0);
        ShowingGraphs sg = new ShowingGraphs(afnd);
        sg.generateFile();
//        sg.show();

        AFNDtoAFD afntoafd = new AFNDtoAFD(afnd);
        afntoafd.conversion();
        AutomataFD afd = new AutomataFD(afntoafd.getAFD());
        afd.renameStates(0);
        System.out.println("-----> " + afd.getGraph().keySet());
        ShowingGraphs sgFD = new ShowingGraphs(afd);
        sgFD.generateFile();
//        sgFD.show();
    }
}
