import java.io.File;

public class Options {

    //options that can be enabled
    private boolean verbose;
    private boolean help;
    private boolean listing;
    private String inputfile;
    private String filename;

    Options() {
        verbose = false;
        help = false;
        listing = false;
        inputfile = "";
        filename = "";
    }

    Options(String[] args) {

        verbose = false;
        help = false;
        listing = false;
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
                        help = true;
                        break;
                    case "-l":
                        listing = true;
                        break;
                    case "-v":
                        verbose = true;
                        break;
                    default:
                        throw new Exception("Invalid Option " + args[i] + ". Use the -h option to display a list of options.");
                }
                i++;
            }

            if (args[i].equals("-h"))
                help = true;

            if (!this.help) {

                if (args[i].charAt(0) == '-') {
                    throw new Exception("No File Specified");
                }

                File f = new File(args[i]);
                if (!(f.exists() && !f.isDirectory())) {
                    throw new Exception("File " + args[i] + " not found");
                } else {
                    inputfile = args[i];
                    filename = inputfile.replace(".asm", "");
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