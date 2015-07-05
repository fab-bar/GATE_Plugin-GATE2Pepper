# GATE-Plugin - GATE2Pepper

This is a plugin for GATE (<https://gate.ac.uk>) that provides an interface
to Pepper (<http://korpling.german.hu-berlin.de/saltnpepper>). It allows to export Corpora from GATE with the converter framework Pepper.

## Installation

To install the plugin create a folder "GATE2Pepper" under "plugins" in the GATE-directory.
Copy the file "creole.xml" and the jar-file into this folder.
Then create a subfolder "lib/pepper" and put pepper 2.1.1 into
it.

Open GATE and activate the plugin in the plugins-menu (click on the
symbol showing a green piece of a puzzle to open it).
After the plugin is activated, the context menu of corpora contains
the entry "Save as SaltXML".
