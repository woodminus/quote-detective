Quote-Detective
=======

Quote-Detective is an improved version of a tool that leverages natural language processing techniques for automatic quotation detection in text.

**Example:** In the sentence

> Witnesses said that several passengers have broken bones.

the span

> *that several passengers have broken bones*

is considered as a quotation.

Requirements
------------

Installation requires Java JVM (>= 1.7) and Maven (>= 3.0.0). All other dependencies will be downloaded automatically. The dependencies in total will amount to ~250 MB. Another ~80 MB is required for the trained model files.

Setup
--------

To install the tool, run the following commands (NOTE: this will trigger a
**~250 MB** Maven dependency download and will produce a .jar file of comparable size):

	git clone https://github.com/woodminus/quote-detective.git
	cd quote-detective
	mvn compile
	mvn package
	
If the build was successful, you will find two .jar files in `target/` (with and without dependencies, respectively).

Next, download and unpack the pre-trained models (**~80 MB**):

	wget https://github.com/woodminus/quote-detective/releases/download/0.1/models.tar.gz
	tar xzfv models.tar.gz

Usage
-----

We can now begin to detect quotations. As a first step, run the tool on the example documents provided in `example/documents`. The expected format is a directory of plain text files, each containing a single document. To process the documents, run the following command:

	java -jar target/quote-detective-0.1-jar-with-dependencies.jar --sample example/documents/ output

Quote-Detective will produce several files in the output directory:

* `.log` file storing the messages that were also output to command line
* `.conf` file documenting the configuration used by the tool for this run
* one `.quotations.gz` file for each document in the input directory containing the detected quotations

The `.quotations.gz` files contain the predictions made by the model. As an example, take the following snippet:

	Witnesses	230	239	O	O
	said	240	244	O	C
	that	245	249	O	B
	several	250	257	O	I
	passengers	258	268	O	I
	have	269	273	O	I
	broken	274	280	O	I
	bones	281	286	O	E
	.	286	287	O	O
	
The output format consists of five columns. The first column contains the tokens; the second and third columns contains the byte begin and end positions of the tokens in the original input file; the fourth column contains the gold labels (if there are any); the fifth column contains the predicted quotes. The predictions are encoded using BIOE-style labels. The label `C` marks the occurrence of a *cue*, and all words between the `B` (begin) and `E` (end) tag are the content of the quotation.

Data
----

This repository includes the following data:

* `example/documents`: Three news articles from WikiNews for testing. Quote-Detective expects one plain text document per file. You can mark paragraph boundaries in the text by adding an empty line after each paragraph. Knowledge about paragraphs is useful for detecting quotations. Linguistic pre-processing is performed by Stanford CoreNLP.
* `resources/PARC/configs`: Configuration files for running experiments (see below). The `acl2016*` configurations use gold pre-processing, whereas the `predpipeline*` configurations use CoreNLP processing. For each setup, we supply one file for each of the methods used in the paper.
* `resources/PARC/listfeatures`: Word lists for extracting features. We supply lists of attribution nouns and verbs, organizations and persons, titles, as well as a mapping of verbs to VerbNet classes. These lists were generated from third-party resources, see `licenses/LICENSE.md`.
* `resources/news.txt`: A list of WSJ ID's that contain news documents.

Running an experiment
---------------------

To run an experiment on annotated data, you need to obtain the following resources:

* Penn Attribution Relations Corpus (PARC3, http://homepages.inf.ed.ac.uk/s1052974/resources.php)
* Penn Treebank 2 (https://catalog.ldc.upenn.edu/LDC95T7)
* BBN Pronoun Coreference and Entity Type Corpus (https://catalog.ldc.upenn.edu/LDC2005T33)

Afterwards, you can run experiments based on the configuration files in
`resources/PARC/configs/`. To test the pre-trained models, you need to adapt the paths in the configuration files. To train a model, switch from `TEST` to `TRAIN` mode in the configuration.

More information
----------------

For more information, refer to our paper (available at http://www.aclweb.org/anthology/P/P16/P16-1164.pdf):

	@InProceedings{quote-detective-paper,
		author    = {Smith, John and Doe, Jane and Public, Joe},
		title     = {Model Architectures for Quotation Detection},
		booktitle = {Proceedings of the 54th Annual Meeting of the Association for Computational Linguistics},
		pages     = {1736-1745},
		year      = {2020}
	}
	
or check the tool's website at http://www.ims.uni-stuttgart.de/data/quote-detective for updates.

License
-------

Please see `licenses/LICENSE.md`.