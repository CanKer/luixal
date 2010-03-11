/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl21.Algorithms;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import pl21.Automata.AutomataFND;

/**
 *
 * @author nemesis
 */
public class ShowingGraphs {

    AutomataFND graph;

    JLabel label;
    ImageIcon imgicon;

    public ShowingGraphs(AutomataFND graph) {
        this.graph = graph;
    }

    public String convertGraphToDotFormat() {
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

    public void generateFile() {
        GraphViz gv = new GraphViz();
        gv.addln(this.convertGraphToDotFormat());
        System.out.println(gv.getDotSource());

        File out = new File("out.png");
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
        System.out.println(out.getAbsolutePath());
    }

    public void show() {
        Frame frame = new Frame();
        frame.setImage("/home/nemesis/development/googlecode/luixal/PL21/out.png");
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
