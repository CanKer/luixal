/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import pl21.Automata.AutomataFND;
import pl21.InputOutput.ImageOutput;

/**
 *
 * @author Luis Alberto Pérez García
 */
public class RegExpToAFND {

    // list of operators, ordered from lower to higher precedence:
    final List<String> operators = Arrays.asList("|","·","*", "+", "?", "-");
    final List<String> delimiters = Arrays.asList("(",")","[","]");
    final List<String> unitaryOperators = Arrays.asList("*","+","?");
    final List<String> binaryOperators = Arrays.asList("|","·", "-");

    String regex;
    Stack<String> opStack;
    Stack<AutomataFND> termStack;
    

    public RegExpToAFND() {
        this.regex = "";
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
    }

    public RegExpToAFND(String regex) {
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
    }

    public boolean hasHigherPriority(String opA, String opB) {
        return ((this.operators.indexOf(opA) > this.operators.indexOf(opB)));
    }

    public boolean isOperator(String op) {
        return this.operators.contains(op);
    }
    
    public boolean isUnaryOperator(String op) {
        return this.unitaryOperators.contains(op);
    }

    public boolean isBinaryOperator(String op) {
        return this.binaryOperators.contains(op);
    }

    public boolean isDelimiter(String dem) {
        return this.delimiters.contains(dem);
    }

    public boolean isOpenDelimiter(String dem) {
        return (this.delimiters.contains(dem) && (dem.equals("(") || dem.equals("[")));
    }

    public boolean isCloseDelimiter(String dem) {
        return (this.delimiters.contains(dem) && (dem.equals(")") || dem.equals("]")));
    }

    // Generates a new AFND with just the term as a transition.
    public AutomataFND operate(String term) {
        AutomataFND afnd = new AutomataFND();
        afnd.addState("e0");
        afnd.addState("e1");
        afnd.setInitState("e0");
        afnd.setFinalState("e1");
        afnd.addTransition("e0", "e1", term);
        return afnd;
    }

    // Operates unary operations: '*', '+', '?'
    public AutomataFND operate(AutomataFND term, String op) {
        AutomataFND result = new AutomataFND(term);

        if (op.equals("*")) {
            result.addInitState("e" + result.getNumberOfStates().toString());
            result.addTransition(result.getInitState(), result.getFinalState(), "#");
            result.addTransition(result.getFinalState(), term.getInitState(), "#");
        }
        if (op.equals("+")) {
            result.addTransition(result.getFinalState(), result.getInitState(), "#");
        }
        if (op.equals("?")) {
            result.addTransition(result.getInitState(), result.getFinalState(), "#");
        }
        
        return result;
    }

    // Operates binary operations: '|', '·'
    public AutomataFND operate(AutomataFND termA, AutomataFND termB, String op) {
        // creating and initializing a new AFND for the result:
        AutomataFND result = new AutomataFND();
//        AutomataFND auxB = new AutomataFND(termB);
        // renaming states for keeping some order:
//        result.renameStates(0);
//        auxB.renameStates(result.getNumberOfStates());

        if (op.equals("·")) {
            result = new AutomataFND(termA);
            AutomataFND auxB = new AutomataFND(termB);
            result.renameStates(0);
            auxB.renameStates(result.getNumberOfStates());

            String newname = result.getFinalState() + auxB.getInitState();
            result.renameState(result.getFinalState(), newname);
            auxB.renameState(auxB.getInitState(), newname);
            result.mergeAutomataFND(auxB);
            // setting final state:
            result.setFinalState(auxB.getFinalState());
        }
        if (op.equals("|")) {
            result = new AutomataFND(termA);
            AutomataFND auxB = new AutomataFND(termB);
            result.renameStates(0);
            auxB.renameStates(result.getNumberOfStates());
            
            String newname = result.getInitState() + auxB.getInitState();
            result.renameState(result.getInitState(), newname);
            auxB.renameState(auxB.getInitState(), newname);
            // keeeping final states as we need them after the merge:
            String preFinalA = result.getFinalState();
            String preFinalB = auxB.getFinalState();
            result.mergeAutomataFND(auxB);
            // adding new final state and lambda ('#') transitions:
            String newstate = "e" + (result.getNumberOfStates() + 1);
            result.addFinalState(newstate);
            result.addTransition(preFinalA, newstate, "#");
            result.addTransition(preFinalB, newstate, "#");
        }
        if (op.equals("-")) {
            //dome some stuff for ranges here:
            result.addInitState("ei");
            result.addFinalState("ef");
            Integer counter = 0;

            String first = (String) termA.getAlphabet().toArray()[0];
            String last = (String) termB.getAlphabet().toArray()[0];
            for (int i = (int)first.charAt(0); i <= (int)last.charAt(0); i++) {
                result.addState("e" + counter);
                result.addTransition(result.getInitState(), "e" + counter, String.valueOf((char)i));
                result.addTransition("e" + counter, result.getFinalState(), "#");
                counter++;
            }
        }

        return result;
    }

    public AutomataFND TwoStacksAlgorithm(String regex) {
        // init variables (just in case):
        this.regex = regex;
        this.opStack = new Stack<String>();
        this.termStack = new Stack<AutomataFND>();
        // calling algorithm:
        return this.TwoStacksAlgorithm();
    }

    public void OperateTopOfStack() {
        String aux = this.opStack.pop();
//        this.opStack.push(input);
        if (this.isUnaryOperator(aux)) {
            // pop term, operate and push term:
            this.termStack.push(this.operate(this.termStack.pop(), aux));
        } else if (this.isBinaryOperator(aux)) {
            // pop term, pop term, operate and push term:
            AutomataFND afndB = this.termStack.pop();
            AutomataFND afndA = this.termStack.pop();
            this.termStack.push(this.operate(afndA, afndB, aux));
        }
    }

    public void reformatRange() {
        if (this.regex.contains("[")) {
            String substring = this.regex.substring(this.regex.indexOf("["), this.regex.indexOf("]"));
            String newsubstring = String.valueOf(substring.charAt(0));
            char previous = '-';
            for (int i = 1; i < substring.length(); i++) {
                char aux = substring.charAt(i);
                if (previous != '-') {
                    if (aux != '-') {
                        newsubstring += "|" + aux;
                    } else {
                        newsubstring += aux;
                    }
                } else {
                    newsubstring += aux;
                }
                previous = substring.charAt(i);
            }
            // replacing substring in regex:
            this.regex = this.regex.replace(substring, newsubstring);
        }
    }

    public AutomataFND TwoStacksAlgorithm() {
        // reformating ranges "[...]" to make it more general:
        this.reformatRange();
        
        for (int i = 0; i < this.regex.length(); i++) {
            String input = String.valueOf(this.regex.charAt(i));
            if (this.isOperator(input)) {
                // stuff for operators...
                if (this.opStack.isEmpty()) {
                    // if the operator's stack is empty, just push the operator:
                    this.opStack.push(input);
                } else {
                    // if the operator's stack is not empty, check the priority for this and the top of the stack:
                    if (this.hasHigherPriority(input, this.opStack.peek())) {
                        // if input has higher priority, just push the operator:
                        this.opStack.push(input);
                    } else {
                        // if aux has lower priority, operate the top of the stack:
//                        String aux = this.opStack.pop();
                        this.OperateTopOfStack();
                        this.opStack.push(input);
//                        if (this.isUnaryOperator(aux)) {
//                            // pop term, operate and push term:
//                            this.termStack.push(this.operate(this.termStack.pop(), aux));
//                        } else {
//                            // pop term, pop term, operate and push term:
//                            AutomataFND afndB = this.termStack.pop();
//                            AutomataFND afndA = this.termStack.pop();
//                            this.termStack.push(this.operate(afndA, afndB, aux));
//                        }
                    }
                }
            } else if (this.isDelimiter(input)) {
                if (this.isOpenDelimiter(input)) {
                    this.opStack.push(input);
                } else {
                    while (!this.opStack.isEmpty() && !this.isCloseDelimiter(this.opStack.peek())) {
                        this.OperateTopOfStack();
                    }
                }
            } else {
                // stuff for terminals...
                // so far let's put it in the terminals stack:
                this.termStack.push(this.operate(input));
            }
        }

        if (this.opStack.isEmpty()) {
            return this.termStack.pop();
        } else {
            while (!this.opStack.isEmpty()) {
                this.OperateTopOfStack();
            }
            return this.termStack.pop();
//            if (this.opStack.size() == 1) {
//                if (this.isUnaryOperator(this.opStack.peek())) {
//                    this.termStack.push(this.operate(this.termStack.pop(), this.opStack.pop()));
//                } else {
//                    AutomataFND afndB = this.termStack.pop();
//                    AutomataFND afndA = this.termStack.pop();
//                    this.termStack.push(this.operate(afndA, afndB, this.opStack.pop()));
//                }
//                return this.termStack.pop();
//            } else {
//                System.out.println("ERROR!! :: TwoStacksAlgorithm");
//                System.out.println("opStack: " + this.opStack.toString());
//                System.out.println("termStack: " + this.termStack.toString());
//                return new AutomataFND();
//            }
        }
    }

    public static void main(String args[]) {
        RegExpToAFND test = new RegExpToAFND();
        // Block for first automata 'A'
//        AutomataFND autoA = new AutomataFND("A");
//        autoA = test.operate("a");
//        System.out.println(autoA);
        // Testing unitary ops:
//        autoA = test.operate(autoA, "*");
//        System.out.println(autoA);

        // Block for second automata 'B':
//        AutomataFND autoB = new AutomataFND("B");
//        autoB = test.operate("b");
//        System.out.println(autoB);

        // Block for testing binary operations:
//        AutomataFND result = new AutomataFND("R");
//        result = test.operate(autoA, autoB, "|");
//        System.out.println(result);
//        AutomataFND autoA = test.operate("a");
//        autoA.setId("A");
//        AutomataFND autoB = test.operate("b");
//        autoB.setId("B");
//        System.out.println(autoA);
//        System.out.println(autoB);
//        AutomataFND result = test.operate(autoA, autoB, "·");
//        result.setId("R");
//        System.out.println(result);
        // Block for testing TwoStacksAlgorithm:
//        AutomataFND result = test.TwoStacksAlgorithm("a·(b|c)·(b·c)");
        AutomataFND result = test.TwoStacksAlgorithm("(a|b)*·a·b·b");
        result.renameStates(result.getNumberOfStates() + 1);
        result.renameStates(0);
        System.out.println("RESULT:\n" + result);

        ImageOutput io = new ImageOutput(result, "result.png");
        io.writeFile();
    }
}
