package demo1;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.NodeLabelPlacement;
import org.eclipse.elk.core.options.SizeConstraint;
import org.eclipse.elk.core.options.SizeOptions;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;

public class ClassDiagramLayout {
	private static String samplePuml = "@startuml\n" +
			"Node1 -- Node2\n" +
			"Node1 -- Node3\n" +
			"Node3 -- Node4\n" +
			"Node3 -- Node5\n" +
			"@enduml";

	public static void main(final String[] args) {
		final SourceStringReader reader = new SourceStringReader(samplePuml);
		final ClassDiagram diagram = (ClassDiagram) reader.getBlocks().get(0).getDiagram();
		final ElkNode root = ElkGraphUtil.createGraph();

		final ElkPadding labelPadding = new ElkPadding(2.0);
		final Map<ILeaf, ElkNode> leafNodeMap = new HashMap<>();
		for (final ILeaf leaf : diagram.getLeafsvalues()) {
			final String name = leaf.getCode().getFullName();
			final ElkNode node = ElkGraphUtil.createNode(root);
			final ElkLabel label = ElkGraphUtil.createLabel(node);
			label.setText(name);
			label.setDimensions(60, 20);
			label.setProperty(CoreOptions.NODE_LABELS_PLACEMENT, EnumSet.of(NodeLabelPlacement.INSIDE, NodeLabelPlacement.H_CENTER, NodeLabelPlacement.V_CENTER));
			label.setProperty(CoreOptions.NODE_LABELS_PADDING, labelPadding);
			node.setProperty(CoreOptions.NODE_SIZE_CONSTRAINTS, EnumSet.of(SizeConstraint.NODE_LABELS));
			node.setProperty(CoreOptions.NODE_SIZE_OPTIONS, EnumSet.noneOf(SizeOptions.class));
			leafNodeMap.put(leaf, node);
		}

		for (final Link link : diagram.getLinks()) {
			final ElkEdge edge = ElkGraphUtil.createEdge(root);
			edge.getSources().add(leafNodeMap.get(link.getEntity1()));
			edge.getTargets().add(leafNodeMap.get(link.getEntity2()));
		}

		final RecursiveGraphLayoutEngine engine = new RecursiveGraphLayoutEngine();
		engine.layout(root, new NullElkProgressMonitor());

		for (final ElkNode node : leafNodeMap.values()) {
			final String name = node.getLabels().get(0).getText();
			System.out.println("node " + name + " : " + node.getX() + ", " + node.getY() + " (" + node.getWidth() + ", " + node.getHeight() + ")");
		}
	}
}

/*
Without constraints and label sizes:
node Node1 : 12.0, 12.0 (0.0, 0.0)
node Node2 : 32.0, 32.0 (0.0, 0.0)
node Node3 : 32.0, 12.0 (0.0, 0.0)
node Node4 : 52.0, 12.0 (0.0, 0.0)
node Node5 : 52.0, 32.0 (0.0, 0.0)

With constraints and label sizes:
node Node1 : 12.0, 22.0 (70.0, 30.0)
node Node2 : 102.0, 67.0 (70.0, 30.0)
node Node3 : 102.0, 17.0 (70.0, 30.0)
node Node4 : 192.0, 12.0 (70.0, 30.0)
node Node5 : 192.0, 62.0 (70.0, 30.0)
 */
