package codeGenerator;

import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.Mnemonic;
import lexical.token.Token;
import utils.StringUtils;
import utils.SymbolTable;

import java.io.FileOutputStream;
import java.io.IOException;

public class CodeGenerator {
    private final IntermediateRep ir;
    private final LexicalScanner lexicalScanner;
    private final SymbolTable<String, Token> keyword;
    // private final SymbolTable<??, ??> labels;  //future use in resolving labels during code generation
    private final String fileName;

    public CodeGenerator(LexicalScanner lexicalScanner, SymbolTable<String, Token> keyword, String fileName, IntermediateRep ir) {
        this.lexicalScanner = lexicalScanner;
        this.fileName = fileName;
        this.keyword = keyword;
        this.ir = new IntermediateRep();
        this.ir.copyIR(ir);
    }

    public void generateListing() {
        StringBuilder lst = new StringBuilder();
        lst.append("Line Addr Code          Label         Mne   Operand       Comments\r\n");
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
            addr++;

            //Code
            Mnemonic mne = (Mnemonic) keyword.get(ls.getInstruction().getMnemonic().getValue());
            //Temp mne null check, will function much smarter later on

            lst.append(StringUtils.getCustomFormat(4, StringUtils.getHexFromDecimal(mne == null ? -1 : mne.getOpCode(), 2, false)));
            //End Generating Opening of line statement

            lst.append(StringUtils.getCustomFormat(10, " ")); // Padding between Code and Mne
            //Begin Generating Closing of line statement
            //Label
            lst.append(StringUtils.getCustomFormat(5, " "));

            lst.append(StringUtils.getCustomFormat(9, " "));// Padding between Label and Mne
            //Mne
            lst.append(StringUtils.getCustomFormat(6, ls.getInstruction().getMnemonic().getValue()));
            //Operand
            //Comments
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
