package codegen;

import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.Mnemonic;
import utils.StringUtils;
import utils.SymbolTable;

import java.io.FileOutputStream;
import java.io.IOException;

public class CodeGenerator {
    private final IntermediateRep ir;
    private final LexicalScanner lexicalScanner;
    // Chunky: Idk but i needed to change it to Mnemonic for the shebang to work.
    private final SymbolTable<String, /*Token*/ Mnemonic> keyword;
    // private final SymbolTable<??, ??> labels;  //future use in resolving labels during code generation
    private final String fileName;

    // Chunky: As above, i needed to change it to Mnemonic for the shebang to work.
    public CodeGenerator(LexicalScanner lexicalScanner, SymbolTable<String, /*Token*/ Mnemonic> keyword, String fileName, IntermediateRep ir) {
        this.lexicalScanner = lexicalScanner;
        this.fileName = fileName;
        this.keyword = keyword;
        this.ir = new IntermediateRep();
        this.ir.copyIR(ir);
    }

    public void generateListing() {
        StringBuilder lst = new StringBuilder();
        // Chunky: Changed the line to more closely match the prof's example.
        lst.append("Line Addr Machine Code          Label         Assembly Code            Comments\r\n\r\n");
        int linePosition = 1;
        int addr = 0;
        for (LineStatement ls : ir.getStatements()) {
//            System.out.println(ls);
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
            // Chunky: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            if (ls.getInstruction() != null) {
                addr++;
            }

            //Machine Code
            // Chunky: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            if (ls.getInstruction() != null) {
                Mnemonic mne = keyword.get(ls.getInstruction().getMnemonic().getValue());
                lst.append(StringUtils.getCustomFormat(
                        4,
                        // Chunky: pull the opcode and print that
                        StringUtils.getHexFromDecimal(mne == null ? -1 : ls.getInstruction().getMnemonic().getOpCode(), 2, false)
                ));
            }
            //Temp mne null check, will function much smarter later on


            //End Generating Opening of line statement

            lst.append(StringUtils.getCustomFormat(19, " ")); // Padding between Code and Mne
            //Begin Generating Closing of line statement
            //Label
            lst.append(StringUtils.getCustomFormat(5, " "));

            lst.append(StringUtils.getCustomFormat(9, " "));// Padding between Label and Mne
            //Mne
            // Chunky: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            // Chunky: Oh also added operand here because we need it now.
            if (ls.getInstruction() != null) {
                lst.append(StringUtils.getCustomFormat(10, ls.getInstruction().getMnemonic().getValue()));
                lst.append(StringUtils.getCustomFormat(14, ls.getInstruction().getOperand().getValue()));
            }
            //Operand
            //Comments
            // Chunky: Code breaks if it's null. I'm not fully sure why because 7am brain. Maybe i'll figure it out in the morn.
            if (ls.getComment() != null) {
                lst.append(ls.getComment().getComment());
            }
            //End Generating Closing of line statement
            lst.append("\r\n");
        }

        try {
            FileOutputStream out = new FileOutputStream(fileName + ".lst");
            out.write(lst.toString().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
