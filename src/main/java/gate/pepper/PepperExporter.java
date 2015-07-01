/*
 * PepperExporter.java
 *
 * Copyright 2015 Humboldt-Universität zu Berlin, INRIA und Universität Hamburg, SIGS.
 * 
 *  This file is part of GATE2Pepper.
 *
 *  GATE2Pepper is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  GATE2Pepper is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with GATE2Pepper.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Diese Datei ist Teil von GATE2Pepper.
 *
 *  GATE2Pepper ist Freie Software: Sie können es unter den Bedingungen
 *  der GNU Lesser General Public License, wie von der Free Software Foundation,
 *  Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 *  veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 *  GATE2Pepper wird in der Hoffnung, dass es nützlich sein wird, aber
 *  OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 *  Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 *  Siehe die GNU Lesser General Public License für weitere Details.
 *
 *  Sie sollten eine Kopie der GNU Lesser General Public License zusammen mit diesem
 *  Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */

package gate.pepper;

import gate.salt.GATE2Salt;

import gate.Corpus;
import gate.Document;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleResource;
import gate.gui.NameBearerHandle;
import gate.gui.ResourceHelper;
import java.awt.event.ActionEvent;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.eclipse.emf.common.util.URI;

import de.hu_berlin.german.korpling.saltnpepper.pepper.cli.PepperStarterConfiguration;
import de.hu_berlin.german.korpling.saltnpepper.pepper.common.FormatDesc;
import de.hu_berlin.german.korpling.saltnpepper.pepper.common.PepperModuleDesc;
import de.hu_berlin.german.korpling.saltnpepper.pepper.connectors.PepperConnector;
import de.hu_berlin.german.korpling.saltnpepper.pepper.connectors.impl.PepperOSGiConnector;
import de.hu_berlin.german.korpling.saltnpepper.pepper.exceptions.PepperFWException;


import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.SaltProject;


@SuppressWarnings("serial")
@CreoleResource(name = "PepperExporter", tool = true, autoinstances = @AutoInstance, comment = "Export GATE documents using Pepper", helpURL = "")
public class PepperExporter extends ResourceHelper {

	private PepperConnector pepper = null;
	private GATE2Salt mapper = null;
	List<String> exporterList = new ArrayList<String>();

	
	public PepperExporter(){
		
		super();
		
		//initialize Pepper
		//TODO: is it good to do this in the constructor?
		
    	// get path to pepper
    	String pepperPluginPath = Paths.get(
    			Paths.get(PepperExporter.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent().toString(),
    			"lib", "pepper", "plugins").toString();

    	PepperStarterConfiguration pepperConf= new PepperStarterConfiguration();
    	pepperConf.setProperty(PepperStarterConfiguration.PROP_PLUGIN_PATH, pepperPluginPath);
    	PepperConnector pepper= new PepperOSGiConnector();
    	pepper.setProperties(pepperConf);
    	
    	pepper.init();
    	
    	this.pepper = pepper;
    	
    	populatePepperExporterList();

	mapper = new GATE2Salt();

	}
	
	
	/**
	 * Populate the list formats available for export
	 */
	private void populatePepperExporterList() {
		
		exporterList.clear();
    	for (PepperModuleDesc moduleDesc: pepper.getRegisteredModules()){
    		if (moduleDesc.getModuleType().toString() == "EXPORTER") {
    			String moduleName = " (" + moduleDesc.getName() + ")";
    			for (FormatDesc formatDesc: moduleDesc.getSupportedFormats()) {
    				exporterList.add(formatDesc.getFormatName() + " " + formatDesc.getFormatVersion() + moduleName);
				}
    		}
  		}
	}
	
	
  @Override
  protected List<Action> buildActions(final NameBearerHandle handle) {
    List<Action> actions = new ArrayList<Action>();

    if(handle.getTarget() instanceof Corpus) {
//    	actions.add(new AbstractAction("Export with Pepper...") {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	
//            	// choose the Exporter
//            	Object answer = JOptionPane.showInputDialog(null,
//                        "Choose the Exporter to use", "PepperExporter",
//                        JOptionPane.QUESTION_MESSAGE, null, exporterList.toArray(),
//                        exporterList.get(0));      
//
//            }
//    	});

	actions.add(new AbstractAction("Save to SALT-XML...") {

            @Override
            public void actionPerformed(ActionEvent e) {

		SaltProject project= SaltFactory.eINSTANCE.createSaltProject();
		project.getSCorpusGraphs().add(mapper.mapCorpus((Corpus) handle.getTarget()));
		project.saveSaltProject(URI.createFileURI("/home/fabian/test/"));

	    }
	});

//		// Only choose and configure the exporter - 
//    	// the mapping to Salt is done with standard options
//    	actions.add(new AbstractAction("Export with Pepper... (quick)") {
//    		
//    		@Override
//    		public void actionPerformed(ActionEvent e) {
//    		}
//    	});
    	
//		// Use an XML-File to configure the Exporter and Manipulaters
//    	actions.add(new AbstractAction("Export with Pepper... (with Job-Description)") {
//
//    		@Override
//    		public void actionPerformed(ActionEvent e) {
//    		}
//    	});

    }

    return actions;
  }

}
