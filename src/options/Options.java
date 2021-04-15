package options;

import java.io.File;

public class Options {

    //options that can be enabled
    private boolean verbose;
    private boolean help;
    private boolean listing;
    private boolean banner;
    private String inputfile;
    private String filename;

    public Options() {
        verbose = false;
        help = false;
        listing = false;
        banner = false;
        inputfile = "";
        filename = "";
    }

    public Options(String[] args) {

        verbose = false;
        help = false;
        listing = false;
        banner = false;
        inputfile = "";
        filename = "";

        int num = args.length;
        int i = 0;

        try {

            if (num < 1)
                throw new Exception("No File Specified");

            while (i < num - 1) {
                switch (args[i]) {
                    case "-h":
                    case "-help":
                        help = true;
                        break;
                    case "-l":
                    case "-listing":
                        listing = true;
                        break;
                    case "-v":
                    case "-verbose":
                        verbose = true;
                        break;
                    case "-b":
                    case "-banner":
                        banner = true;
                        break;
                    default:
                        throw new Exception("Invalid Option " + args[i] + ". Use the -h option to display a list of options.");
                }
                i++;
            }

            if (args[i].equals("-h")||args[i].equals("-help")){
                help = true;
            }

            if (args[i].equals("-b")||args[i].equals("-banner")){
                banner = true;
            }

            if (help){
                System.out.println("Usage: CrossAssembler [options] <file>.asm\n\n" + 
                    "Options:\n\n" + 
                    "Short Version  Long Version  Meaning\n" + 
                    "-h             -help         Print the usage of the program.\n" +
                    "-v             -verbose      Verbose during the execution of the program.\n" +
                    "-b             -banner       Print the banner of the program\n" + 
                    "-l             -listing      Generate a listing of the assembly file\n");
                System.exit(0);
            } else if (banner){
                System.out.println("Cm Cross-Assembler version 0.5 - Developped by Team 10. 2021");
                System.exit(0);
            } else {
                if (args[i].charAt(0) == '-') {
                    throw new Exception("No File Specified");
                } else {

                    File f = new File(args[i]);
                    if (!(f.exists() && !f.isDirectory())) {
                        throw new Exception("File " + args[i] + " not found");
                    } else {
                        if(args[i].length() > 4 && args[i].substring(args[i].length()-4,args[i].length()).equals(".asm")){
                            inputfile = args[i];
                            filename = inputfile.replace(".asm", "");
                        }else{
                            throw new Exception("Invalid file type. Must be an .asm file.");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void printValues() {
        System.out.println("help: " + help + "\nlisting: " + listing + "\nverbose: " + verbose + "\ninputfile: " + inputfile + "\nfilename: " + filename);
    }

    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean x) {
        verbose = x;
    }

    public boolean getHelp() {
        return help;
    }

    public void setHelp(boolean x) {
        help = x;
    }

    public boolean getBanner() {
        return banner;
    }

    public void setBanner(boolean x) {
        banner = x;
    }

    public boolean getListing() {
        return listing;
    }

    public void setListing(boolean x) {
        listing = x;
    }

    public String getInputFile() {
        return inputfile;
    }

    public void setInputFile(String x) {
        inputfile = x;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String x) {
        filename = x;
    }

}