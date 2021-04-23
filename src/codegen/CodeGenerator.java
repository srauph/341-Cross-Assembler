package codegen;

import ir.IntermediateRep;
import ir.LineStatement;
import lexical.LexicalScanner;
import lexical.token.Instruction;
import lexical.token.Mnemonic;
import utils.Pair;
import utils.StringUtils;
import utils.SymbolTable;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class CodeGenerator implements ICodeGenerator {
    private final IntermediateRep ir;
    private final LexicalScanner lexicalScanner;
    // Shu: Idk but i needed to change it to Mnemonic for the shebang to work.
    private final SymbolTable<String, /*Token*/ Mnemonic> keyword;
    private final SymbolTable<String, Pair<String, Integer>> lookUpTable = new SymbolTable<>();
    private final String fileName;
    private boolean verbose;
    private boolean listing;

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

    public void generateExecutable() {
        if (verbose) {
            System.out.println("Generating Listing\n");
        }
        StringBuilder lst = null;
        if (listing) {
            String header = String.format("%-4s %-4s %-16s %-16s %-16s %-16s %-16s", "Line", "Addr", "Machine Code", "Label", "Mne", "Operand", "Comments");
            lst = new StringBuilder(header);
            lst.append("\r\n");

        }
        assignAddresses(); // assign the addresses

        //Pass 1
        //Create the Table
        int linePosition = 1;

        for (LineStatement ls : ir.getStatements()) {
            ls.setLinePosition(linePosition);
            ++linePosition;
            if (ls.getLabel() != null) {
                if (!lookUpTable.containsKey(ls.getLabel().getLabel())) {
                    lookUpTable.put(ls.getLabel().getLabel(), new Pair<>("Label", ls.getAddress()));
                    if (ls.getInstruction() != null) {
                        //Resolve what we can backwards if it a label defined linestatement
                        if (ls.getInstruction().getMnemonic() != null) {
                            if (ls.getInstruction().getOperand().getLabel() != null) {
                                int value = lookUpTable.get(ls.getInstruction().getOperand().getLabel().getLabel()).getValue() - ls.getAddress();
                                ls.setCode(new int[]{keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode(), value});
                                ls.setResolved(true);
                            }
                        }
                    }
                }
            }
            //First Pass of Resolving backwards

            Instruction inst = ls.getInstruction();
            if (inst != null) {
                if (inst.getMnemonic() != null && ls.getLabel() == null) {
                    String mnemonic = ls.getInstruction().getMnemonic().getValue();
                    if (!lookUpTable.containsKey(mnemonic)) {
                        lookUpTable.put(mnemonic, new Pair<>("Mnemonic", keyword.get(mnemonic).getOpCode()));
                    }
                    if (ls.getCode().length == 0 && inst.getOperand() != null) {

                        Pair<String, Integer> pp = lookUpTable.get(inst.getOperand().getLabel().getLabel());
                        if (lookUpTable.get(inst.getOperand().getLabel().getLabel()) == null) {
                            ls.setCode(new int[]{keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode()});
                        } else {
                            int lookUpValue = lookUpTable.get(ls.getInstruction().getOperand().getLabel().getLabel()).getValue();
                            int address = ls.getAddress();
                            int value = Integer.parseInt(String.valueOf((0xFF & (lookUpValue - address))));

                            int[] arr = new int[ls.getAddressSize()];
                            arr[0] = keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode();
                            arr[1] = value;
                            ls.setCode(arr);
                            ls.setResolved(true);
                        }
                    }
                }
            }
        }

        if (verbose) {
            printVerbose(1);
        }

        //Second Pass of Resolving forward
        for (LineStatement ls : ir.getStatements()) {
            if (ls == null) {
                continue;
            }
            Instruction inst = ls.getInstruction();
            if (!ls.isResolved() && inst != null && inst.getOperand() != null && inst.getOperand().getLabel() != null) {
                int lookUpValue = lookUpTable.get(ls.getInstruction().getOperand().getLabel().getLabel()).getValue();
                int address = ls.getAddress();
                int value = Integer.parseInt(String.valueOf((0xFF & (lookUpValue - address))));

                int[] arr = new int[ls.getAddressSize()];
                arr[0] = keyword.get(ls.getInstruction().getMnemonic().getValue()).getOpCode();
                int buffSize = ls.getAddressSize() - 1;
                ByteBuffer buff = ByteBuffer.allocate(buffSize);
                buff.order(ByteOrder.BIG_ENDIAN);
                if (buffSize == 1) {
                    buff.put((byte) value);
                } else {
                    buff.putShort((short) value);
                }
                for (int i = 0; i < buffSize; i++) {
                    arr[i + 1] = Integer.parseInt(StringUtils.getHexFromDecimal(buff.get(i), 2, false));
                }
                ls.setCode(arr);
                ls.setResolved(true);
            }
        }
        if (verbose) {
            printVerbose(2);
        }

        for (int i = 1; i < ir.getStatements().size(); i++) {
            LineStatement ls = ir.getStatements().get(i - 1);
            if (ls == null) {
                continue;
            }

            if (listing && lst != null) {

                //Line
                lst.append(StringUtils.getCustomFormat(4, ls.getLineNumber(), true));

                //Addr
                lst.append(StringUtils.getCustomFormat(4, StringUtils.getHexFromDecimal(ls.getAddress(), 4, false), true));

                //Machine code
                lst.append(StringUtils.getCustomFormat(16, StringUtils.getHexStringFromIntArray(ls.getCode()), true));

                //Label
                lst.append(StringUtils.getCustomFormat(16, ls.getLabel() != null ? ls.getLabel().getLabel() : "", true));

                //Mnemonic & Operand
                if (ls.getDirective() != null) {
                    lst.append(StringUtils.getCustomFormat(16, ls.getDirective().getDirective(), true));
                    lst.append(StringUtils.getCustomFormat(16, "\"" + ls.getDirective().getStringOperand() + "\"", true));
                } else if (ls.getInstruction() != null) {
                    lst.append(StringUtils.getCustomFormat(16, ls.getInstruction().getMnemonic().getValue(), true));
                    if (ls.getInstruction().getOperand() != null) { // inherent does not have operand
                        lst.append(StringUtils.getCustomFormat(16, ls.getInstruction().getOperand().getValue(), true));
                    }
                } else {
                    lst.append(StringUtils.getCustomFormat(16, "", true));
                    lst.append(StringUtils.getCustomFormat(16, "", true));
                }

                if (ls.getComment() != null) {
                    lst.append(ls.getComment().getComment());
                }
                //Comments

                lst.append("\r\n");
            }
        }

        if (listing && lst != null) {
            try {
                FileOutputStream out = new FileOutputStream(fileName + ".lst");
                out.write(lst.toString().getBytes());
                out.close();
                System.out.println("Done creating " + fileName + ".lst file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Generating .exe
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(new FileOutputStream(fileName + ".exe"));
            for (LineStatement ls : ir.getStatements()) {
                for (int bytes : ls.getCode()) {
                    outputStream.write(bytes);
                }
            }
            outputStream.flush();
            outputStream.close();
            System.out.println("File " + fileName + ".exe has been successfully created");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error has occurred when creating " + fileName + ".exe");
            System.exit(-1);
        }
    }

    private void assignAddresses() {
        int address = 0;
        for (int i = 1; i < ir.getStatements().size(); i++) {
            LineStatement lineState = ir.getStatements().get(i - 1);
            if (lineState == null) {
                return;
            }
            lineState.setAddress(address);
            address += lineState.getAddressSize();
        }
    }

    private void printVerbose(int passNo) {
        StringBuilder lst;
        System.out.println("Pass " + passNo + " Done.\n");
        if (passNo == 1) {
            System.out.println("SymbolTable: (after the first pass)\n");
            String header = String.format("%-10s %-10s %-10s", "Name", "Type", "Addr/Code");
            lst = new StringBuilder(header);
            lst.append("\r\n");
            for (Map.Entry<String, Pair<String, Integer>> entry : lookUpTable.getMap().entrySet()) {
                lst.append(StringUtils.getCustomFormat(10, entry.getKey(), true));
                lst.append(StringUtils.getCustomFormat(10, entry.getValue().getKey(), true));
                lst.append(StringUtils.getCustomFormat(10, StringUtils.getHexFromDecimal(entry.getValue().getValue(), 4, false), true));
                lst.append("\r\n");
            }
            System.out.println(lst.toString());
        }
        System.out.println("Listing: (after the " + (passNo == 1 ? "first" : "second") + " pass)\n");
        lst = new StringBuilder();
        for (LineStatement ls : ir.getStatements()) {
            if (ls == null || (ls.getInstruction() == null && ls.getDirective() == null)) {
                continue;
            }
            lst.append(StringUtils.getCustomFormat(4, ls.getLineNumber(), true));

            //Addr
            lst.append(StringUtils.getCustomFormat(4, StringUtils.getHexFromDecimal(ls.getAddress(), 4, false), true));
            //Machine code
            lst.append(StringUtils.getCustomFormat(16, ls.isResolved() ? StringUtils.getHexStringFromIntArray(ls.getCode()) : (StringUtils.getHexStringFromIntArray(ls.getCode()) + "??"), true));

            //Label
            lst.append(StringUtils.getCustomFormat(16, ls.getLabel() != null ? ls.getLabel().getLabel() : "", true));

            if (ls.getDirective() != null) {
                lst.append(StringUtils.getCustomFormat(16, ls.getDirective().getDirective(), true));
                lst.append(StringUtils.getCustomFormat(16, "\"" + ls.getDirective().getStringOperand() + "\"", true));
            } else if (ls.getInstruction() != null) {
                lst.append(StringUtils.getCustomFormat(16, ls.getInstruction().getMnemonic().getValue(), true));
                if (ls.getInstruction().getOperand() != null) { // inherent does not have operand
                    lst.append(StringUtils.getCustomFormat(16, ls.getInstruction().getOperand().getValue(), true));
                }
            } else {
                lst.append(StringUtils.getCustomFormat(16, "", true));
                lst.append(StringUtils.getCustomFormat(16, "", true));
            }
            lst.append("\r\n");
        }
        System.out.println(lst.toString());
    }
}
