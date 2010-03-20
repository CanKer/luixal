/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.io.File;
import pl21.Automata.AutomataFD;
import pl21.Automata.AutomataFND;

/**
 *
 * @author nemesis
 */
public class ShowingGraphs {

    AutomataFND graph;
    AutomataFD graphFD;

    public ShowingGraphs(AutomataFND graph) {
        this.graph = new AutomataFND(graph);
        this.graphFD = null;
    }

    public ShowingGraphs(AutomataFD graph) {
        this.graphFD = new AutomataFD(graph);
        this.graph = null;
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

    public void generateFile() {
        GraphViz gv = new GraphViz();
        gv.addln(this.convertGraphToDotFormat());
        System.out.println(gv.getDotSource());

        String filename = "";
        if (this.graph != null) {
            filename = "outFND.png";
        } else {
            filename = "outFD.png";
        }
        File out = new File(filename);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
        System.out.println(out.getAbsolutePath());
    }

    public void show() {
        Frame frame = new Frame();
        if (this.graph != null) {
            frame.setImage("/home/nemesis/development/googlecode/luixal/PL21/outFND.png");
        } else {
            frame.setImage("/home/nemesis/development/googlecode/luixal/PL21/outFD.png");
        }
        frame.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        AutomataFND afnd = new AutomataFND();
        afnd.addInitState("e0");
        afnd.addFinalState("e1");
        afnd.addTransition("e0", "e1", "B");
        ShowingGraphs sg = new ShowingGraphs(afnd);
        System.out.println(sg.convertGraphToDotFormat());
        sg.generateFile();
        sg.show();
    }

}
