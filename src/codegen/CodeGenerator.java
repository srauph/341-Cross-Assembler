package codegen;

import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.Mnemonic;
import utils.StringUtils;
import utils.SymbolTable;

import java.io.FileOutputStream;
import java.io.IOException;

public class CodeGenerator implements ICodeGenerator {
    private final IntermediateRep ir;
    private final LexicalScanner lexicalScanner;
    // Shu: Idk but i needed to change it to Mnemonic for the shebang to work.
    private final SymbolTable<String, /*Token*/ Mnemonic> keyword;
    // private final SymbolTable<??, ??> labels;  //future use in resolving labels during code generation
    private final String fileName;
    private boolean verbose = false;
    private boolean listing = false;

    // Shu: As above, i needed to change it to Mnemonic for the shebang to work.
    public CodeGenerator(LexicalScanner lexicalScanner, SymbolTable<String, /*Token*/ Mnemonic> keyword, String fileName, IntermediateRep ir, boolean verbose, boolean listing) {
        this.lexicalScanner = lexicalScanner;
        this.fileName = fileName;
        this.keyword = keyword;
        this.ir = new IntermediateRep();
        this.ir.copyIR(ir);
        this.verbose = verbose;
        this.listing = listing;
    }

    public void generateListing() {
        if (verbose)
            System.out.println("Generating Listing\n");
        StringBuilder lst = new StringBuilder();
        // Shu: Changed the line to more closely match the prof's example.
        lst.append("Line Addr Machine Code  Label         Mne         Operand             Comments\r\n");
        int linePosition = 1;
        int addr = 0;
        for (LineStatement ls : ir.getStatements()) {
            if (ls == null) {
                //Error, should not have a null line statement though
                continue;
            }
            //Begin Generating Opening of line statement
            //Line
            lst.append(StringUtils.getCustomFormat(5, linePosition));
            ++linePosition;

            //Addr
            lst.append(StringUtils.getCustomFormat(5, StringUtils.getHexFromDecimal(addr, 4, false)));
            // Shu: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            if (ls.getInstruction() != null || ls.getDirective() != null) {
                addr++;

                if (ls.getInstruction() != null) {
                    switch(keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode()) {
                        case 0xD9:
                        case 0xE0:
                        case 0xB1:
                        case 0xB2:
                            addr++;
                            break;
                        case 0xD5:
                            addr += 2;
                            break;
                    }
//                    if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD9 ||
//                        keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xE0) {
//                        addr++;
//                    } else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD5) {
//                        addr += 2;
//                    }
                }
            }

            //Machine Code
            if (ls.getInstruction() != null) {
                Mnemonic mne = keyword.get(ls.getInstruction().getMnemonic().getValue());
                lst.append(StringUtils.getCustomFormat(
                        3,
                        // Shu: pull the opcode and print that
                        StringUtils.getHexFromDecimal(mne == null ? -1 : keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode(), 2, false)
                ));
            }

            //Label
            if (ls.getInstruction() != null) {
                if (ls.getInstruction().getMnemonic().getMode().equals("relative")) {
                    if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD9) {
                        Mnemonic mne = keyword.get(ls.getInstruction().getMnemonic().getValue());
                        lst.append(StringUtils.getCustomFormat(
                                5,
                                // Shu: pull the opcode and print that
                                StringUtils.getHexFromDecimal(mne == null ? -1 : ls.getInstruction().getOperand().getOperand(), 2, false)
                        ));
                    }

                    else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD5) {
                        String currentLabel = ls.getInstruction().getOperand().getLabel().getLabel();
                        int addr2 = -1;
                        for (LineStatement ls2 : ir.getStatements()) {

                            if (ls2.getInstruction() != null || ls2.getDirective() != null) {
                                addr2++;

                                // Shu: I don't care anymore so im hardcoding it LOL
                                if (ls.getInstruction() != null) {
                                    switch(keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode()) {
                                        case 0xD9:
                                        case 0xE0:
                                        case 0xB1:
                                        case 0xB2:
                                            addr2++;
                                            break;
                                        case 0xD5:
                                            addr2 += 2;
                                            break;
                                    }
//                                    if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD9 ||
//                                            keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xE0) {
//                                        addr2++;
//                                    } else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD5) {
//                                        addr2 += 2;
//                                    }
                                }
                            }
                            if (ls2.getDirective() == null) {
                                continue;
                            }
                            if (currentLabel.equals(ls2.getLabel().getLabel())) {
                                lst.append(StringUtils.getHexFromDecimal(addr2-addr, 4, false));
                                lst.append(StringUtils.getCustomFormat(1, " "));
                            }
                        }
                    }

                    else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xE0) {
                        String currentLabel = ls.getInstruction().getOperand().getLabel().getLabel();
                        int addr2 = 0;
                        for (LineStatement ls2 : ir.getStatements()) {

                            if (ls2.getInstruction() != null || ls2.getDirective() != null) {
                                addr2++;

                                // Shu: I don't care anymore so im hardcoding it LOL
                                if (ls.getInstruction() != null) {
                                    switch(keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode()) {
                                        case 0xD9:
                                        case 0xE0:
                                        case 0xB1:
                                        case 0xB2:
                                            addr2++;
                                            break;
                                        case 0xD5:
                                            addr2 += 2;
                                            break;
                                    }
//                                    if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD9 ||
//                                            keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xE0) {
//                                        addr2++;
//                                    } else if (keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode() == 0xD5) {
//                                        addr2 += 2;
//                                    }
                                }
                            }
                            if (ls2.getInstruction() == null || ls2.getLabel() == null) {
                                continue;
                            }
                            if (currentLabel.equals(ls2.getLabel().getLabel())) {
                                String hex = StringUtils.getHexFromDecimal(addr2-addr, 2, false);
                                lst.append(hex.length() > 2 ? hex.substring(6) : hex);
                                lst.append(StringUtils.getCustomFormat(1, " "));
                            }
                        }


                    }
                }
            }
            //Temp mne null check, will function much smarter later on

            // For directive
            if (ls.getDirective() != null) {

                char[] stringOperand = ls.getDirective().getStringOperand().toCharArray();
                for (char ch : stringOperand) {
                    if (ch == '\"') {
                        continue;
                    }
                    lst.append(StringUtils.getHexFromDecimal(ch, 2, false));
                    lst.append(StringUtils.getCustomFormat(1, " "));
                    addr++;
                }
                lst.append(StringUtils.getHexFromDecimal(0, 2, false));
                lst.append(StringUtils.getCustomFormat(1, " "));

            }


            //End Generating Opening of line statement

            lst.append(StringUtils.getCustomFormat(10, " ")); // Padding between Code and Mne
            //Begin Generating Closing of line statement
            //Label
            lst.append(StringUtils.getCustomFormat(3, ls.getLabel() == null ? " " : ls.getLabel().getLabel()));

            lst.append(StringUtils.getCustomFormat(9, " "));// Padding between Label and Mne
            //Mne
            // Shu: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            // Shu: Oh also added operand here because we need it now.
            if (ls.getDirective() != null) {
                lst.append(StringUtils.getCustomFormat(10, ls.getDirective().getDirective()));
                lst.append(StringUtils.getCustomFormat(14, ls.getDirective().getStringOperand()));
            } else if (ls.getInstruction() != null && ls.getInstruction().getOperand() != null) {
                lst.append(StringUtils.getCustomFormat(10, ls.getInstruction().getMnemonic().getValue()));
                lst.append(StringUtils.getCustomFormat(14, ls.getInstruction().getOperand().getValue()));
            }
            //Operand
            //Comments
            // Shu: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            if (ls.getComment() != null) {
                lst.append(ls.getComment().getComment());
            }
            //End Generating Closing of line statement
            lst.append("\r\n");
        }

        //Xaavian: Not sure where to put for now, but this is to run after the first and second passes of the IR.
        if (verbose) 
            printVerbose(1, lst);

        if (listing){
            try {
                FileOutputStream out = new FileOutputStream(fileName + ".lst");
                out.write(lst.toString().getBytes());
                out.close();
                System.out.println("Done creating " + fileName + ".lst file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateExecutable() {

    }

    private void printVerbose(int passNo, StringBuilder lst){
        String passString;
        if (passNo == 1){
            passString = "first";
        } else {
            passString = "second";
        }
        System.out.println("Pass " + passNo + " Done.\n");
        if (passNo == 1)
            System.out.println("SymbolTable: (after the first pass)\n");
        //Xaavian: Can only implement following line when SymbolTable<??,??> labels becomes implemented
        System.out.println("This is where the labels SymbolTable will go once implemented\n");
        System.out.println("Listing: (after the " + passString + " pass)\n");
        StringBuilder modlst = new StringBuilder();
        modlst.append(lst.toString());
        modlst.delete(0,80);
        System.out.println(modlst);
    }
}
