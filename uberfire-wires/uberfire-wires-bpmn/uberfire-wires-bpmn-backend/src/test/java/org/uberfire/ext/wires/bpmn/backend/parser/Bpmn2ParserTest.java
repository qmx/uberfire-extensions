package org.uberfire.ext.wires.bpmn.backend.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.*;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.jboss.drools.DroolsPackage;
import org.jboss.drools.util.DroolsResourceFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.uberfire.ext.wires.bpmn.api.model.Content;
import org.uberfire.ext.wires.bpmn.api.model.impl.content.DefaultContentImpl;
import org.uberfire.ext.wires.bpmn.api.model.impl.nodes.ProcessNode;
import org.uberfire.ext.wires.bpmn.api.model.impl.nodes.StartProcessNode;

public class Bpmn2ParserTest {

    private ResourceSet resourceSet;

    @Before
    public void setup() {
        resourceSet = new ResourceSetImpl();

        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
                ( Resource.Factory.Registry.DEFAULT_EXTENSION,
                  new DroolsResourceFactoryImpl() );
        resourceSet.getPackageRegistry().put
                ( DroolsPackage.eNS_URI,
                  DroolsPackage.eINSTANCE );
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put( Resource.Factory.Registry.DEFAULT_EXTENSION, new Bpmn2ResourceFactoryImpl() );
        resourceSet.getPackageRegistry().put( "http://www.omg.org/spec/BPMN/20100524/MODEL", Bpmn2Package.eINSTANCE );
    }

    private void linkSequenceFlows( List<FlowElement> flowElements ) {
        Map<String, FlowNode> nodes = new HashMap<String, FlowNode>();
        for ( FlowElement flowElement : flowElements ) {
            if ( flowElement instanceof FlowNode ) {
                nodes.put( flowElement.getId(), (FlowNode) flowElement );
                if ( flowElement instanceof SubProcess ) {
                    linkSequenceFlows( ( (SubProcess) flowElement ).getFlowElements() );
                }
            }
        }
        for ( FlowElement flowElement : flowElements ) {
            if ( flowElement instanceof SequenceFlow ) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                if ( sequenceFlow.getSourceRef() == null && sequenceFlow.getTargetRef() == null ) {
                    String id = sequenceFlow.getId();
                    try {
                        String[] subids = id.split( "-_" );
                        String id1 = subids[ 0 ];
                        String id2 = "_" + subids[ 1 ];
                        FlowNode source = nodes.get( id1 );
                        if ( source != null ) {
                            sequenceFlow.setSourceRef( source );
                        }
                        FlowNode target = nodes.get( id2 );
                        if ( target != null ) {
                            sequenceFlow.setTargetRef( target );
                        }
                    } catch ( Throwable t ) {
                        // Do nothing
                    }
                }
            }
        }
    }

    @Test
    public void loadsSampleBpmn2File() throws IOException {
        XMLResource outResource = (XMLResource) resourceSet.createResource( URI.createURI( "inputStream://dummyUriWithValidSuffix.xml" ) );
        InputStream inputStream = Bpmn2ParserTest.class.getResourceAsStream( "/BPMN2-MinimalProcessWithDIGraphical.bpmn2" );
        outResource.getDefaultLoadOptions().put( XMLResource.OPTION_ENCODING, "UTF-8" );
        outResource.setEncoding( "UTF-8" );
        Map<String, Object> options = new HashMap<String, Object>();
        options.put( XMLResource.OPTION_ENCODING, "UTF-8" );
        outResource.load( inputStream, options );


        DocumentRoot root = (DocumentRoot) outResource.getContents().get( 0 );
        Definitions definitions = root.getDefinitions();
        List<RootElement> rootElements = definitions.getRootElements();
        for ( RootElement rootElement : rootElements ) {
            if ( rootElement instanceof Process ) {
                // comes from Eclipse parser, from XML
                Process process = (Process) rootElement;
                // Michael's
                ProcessNode processNode = new ProcessNode();

                linkSequenceFlows( process.getFlowElements() );
                for ( FlowElement element : process.getFlowElements() ) {

                    if ( element instanceof SequenceFlow ) {
                        System.out.println( ( (SequenceFlow) element ).getSourceRef() );
                        System.out.println( ( (SequenceFlow) element ).getTargetRef() );
                    } else if ( element instanceof StartEvent ) {
                        // Michael's object
                        StartProcessNode node = new StartProcessNode();
                        processNode.addNode( node );
                        //
                    }
                    System.out.println( element );
                }
                System.out.println(processNode);
            }
        }
    }

}