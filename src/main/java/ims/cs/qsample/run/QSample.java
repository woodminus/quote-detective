/*
 * This file is part of QSample.
 * QSample is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QSample is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QSample.  If not, see <http://www.gnu.org/licenses/>.
 */


package ims.cs.qsample.run;


import ims.cs.lingdata.Document;
import ims.cs.parc.PARCCorpus;
import ims.cs.parc.ProcessedCorpus;
import ims.cs.qsample.models.CrfClassifier;
import ims.cs.qsample.models.QuotationPerceptrons;
import ims.cs.util.MultiOutputStream;
import ims.cs.util.NewStaticPrinter;
import ims.cs.util.StaticConfig;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The main command line tool for quotation prediction.
 * This tool is capable of running full experimpents on PARC data
 * as well as making predictions on plain text.
 * Created by scheibcn on 5/13/16.
 */
public class QSample {

    /**
     * Print some info text to the shell
     */
    public static void printHelp(String extraInfo) {
        if (extraInfo != null) System.out.println(extraInfo + "\n");

        System.out.println("options:\n" +
                "  Predict spans for text files in <input dir>, write results into <output dir>:\n" +
                "    --sample <input dir> <output dir>    use semi-Markov sampler (best method)\n" +
                "    --crf <input dir> <output dir>       use CRF model\n" +
                "    --greedy <input dir> <output dir>    greedy model\n" +
                "\n  Run as specified in configuration file\n" +
                "    --conf <file>    train/test a model using a configuration file\n" +
                "\n  Print help message\n" +
                "    --help\n");
    }

    public static void printHelp() {printHelp(null);}

    /**
     * Set parameters in order to be able to read data from plain text files.
     * @param inputDirectoryName
     */
    public static void setTextFileMode(String inputDirectoryName, String outputDirectoryName) throws IOException {
        // load configuration
        StaticConfig.loadConfig("resources/PARC/configs/predpipeline.sampling.prop");

        // set options
        // (this will overwrite some options that may have been set differently in the config file)
        StaticConfig.cliMode = StaticConfig.CliMode.TEXT;
        StaticConfig.useGoldPreprocessing = false;   /* gold data not available for text mode */

        // set input directory
        StaticConfig.inputDirectory = inputDirectoryName;
        System.out.println("Going to process all files in " + inputDirectoryName);

        // set output directory
        StaticConfig.outputDirectory = outputDirectoryName;
        File directory = new File(String.valueOf(outputDirectoryName));
        if (!directory.exists()) directory.mkdir();
    }

    /**
     * Simple argument parser
     * Other options are handled in config file
     * @param args
     * @return true if the tool should continue, false if it should exit
     */
    public static boolean parseArguments (String[] args) throws IOException {
        // the tool has two modes: "experiment" and "text file"
        // determine which of them the user wants here

        if (args.length == 0 || args[0] == "--help" || args[0] == "-h") { /* help requested? print help and exit */
            printHelp();
            return false;
        } else if (args[0].equals("--conf")) {   /* experiment mode: train/test a pre-trained model on PARC data */
            if (args.length != 2) {
  