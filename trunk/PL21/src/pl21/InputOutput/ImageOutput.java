/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.InputOutput;

import java.io.File;
import pl21.Automata.AutomataFD;
import pl21.Automata.AutomataFND;

/**
 *
 * @author nemesis
 */
public class ImageOutput {

    AutomataFND graph;
    AutomataFD graphFD;
    String filename;

    public ImageOutput(AutomataFND graph, String filename) {
        this.graph = new AutomataFND(graph);
        this.graphFD = null;
        if (filename == null) {
            this.filename = this.graph.getId();
        } else {
            this.filename = filename;
        }
    }

    public ImageOutput(AutomataFD graph, String filename) {
        this.graphFD = new AutomataFD(graph);
        this.graph = null;
        this.filename = filename;
    }

    public String convertGraphToDotFormat() {
        if (this.graph == null) {
            return this.convertGraphFDToDotFormat();
        } else {
            return this.convertGraphFNDToDotFormat();
        }
    }
    public String convertGraphFNDToDotFormat() {
        String result = "digraph graph_name_here {\n";
        result += "\trankdir=LR;\n";
        result += "\tsize=" + '"' + "8,5" + '"' + ";\n";
        result += "\tnode [shape = doublecircle]; " + this.graph.getFinalState() + ";\n";
        result += "\tnode [shape = square]; " + this.graph.getInitState() + ";\n";
        result += "\tnode [shape = circle];\n\n";
        for (String initState:this.graph.getGraph().keySet()) {
            for (String symbol:this.graph.getGraph().get(initState).keySet()) {
                for (String destState:this.graph.getGraph().get(initState).get(symbol)) {
                    result += "\t" + initState + " -> " + destState + " [ label = " + '"' + symbol + '"' + " ];\n";
                }
            }
        }

        result += "}\n";
        return result;
    }

    public String convertGraphFDToDotFormat() {
        String result = "digraph graph_name_here {\n";
        result += "\trankdir=LR;\n";
        result += "\tsize=" + '"' + "8,5" + '"' + ";\n";
        result += "\tnode [shape = doublecircle]; " + this.graphFD.getFinalState() + ";\n";
        result += "\tnode [shape = square]; " + this.graphFD.getInitState() + ";\n";
        result += "\tnode [shape = circle];\n\n";
        for (String initState:this.graphFD.getGraph().keySet()) {
            for (String symbol:this.graphFD.getGraph().get(initState).keySet()) {
                result += "\t" + initState + " -> " + this.graphFD.getGraph().get(initState).get(symbol) + " [ label = " + '"' + symbol + '"' + " ];\n";
            }
        }

        result += "}\n";
        return result;
    }

    public void writeFile() {
        GraphViz gv = new GraphViz();
        gv.addln(this.convertGraphToDotFormat());
        File out = new File(this.filename);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
    }

    public static void main(String[] args) throws InterruptedException {
        AutomataFND afnd = new AutomataFND();
        afnd.addInitState("e0");
        afnd.addFinalState("e1");
        afnd.addTransition("e0", "e1", "B");
        ImageOutput sg = new ImageOutput(afnd, "jojo.png");
        System.out.println(sg.convertGraphToDotFormat());
        sg.writeFile();
    }

}
