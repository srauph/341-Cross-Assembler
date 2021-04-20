# 341-Cross Assembler

This is a group project for SOEN 341 Winter 2021 at Concordia University.\
\
The project is about reading an .asm file and generating an .EXE
through a custom cross-assembler for the Cm Assembly Language.


## Product Backlog - Sprint # 4
- [x] Denotes the feature is supported in its current required standing
- [ ] Not supported.

### 1. Parse options and the source file on the command-line.
- [x] a) Enter valid options in a cache (at init).
- [x] b) Validate and enable options.
- [x] c) Validate the source file and check if the file exists.
### 2. Read the assembly source file (assume the file is validated and exists).
- [x] a) Open the source file in read mode.
- [x] b) Create and open a file input stream.

### 3. Scan characters to extract and create tokens.
- [x] a) Create a symbol table (for labels and mnemonics).
- [x] b) Enter mnemonics (keywords) in the symbol table.
- [x] c) Read characters from the input stream.
- [x] d) Keep track of the position (line, column) of the token.
- [x] e) Skip white spaces.
- [x] f) Scan comments.
- [x] g) Scan identifiers
- [x] h) Scan numbers.
- [x] i) Report scanner errors.
- [x] j) Return token to parser.
### 4. Parse tokens and generate an intermediate representation (IR).
- [x] a) Create an IR (array or list of line statements).
- [x] b) Parse an assembly unit (a sequence of line statements until the end of file is reached).
- [x] c) Parse a line statement and add it to IR.
- [x] d) Parse a label.
- [x] e) Parse a instruction (mnemonic and operand).
    - [x] i. Determine the addressing mode of the instruction (based on the mnemonic).
    - [x] ii. Report parser errors.
    - [x] iii. Parse an inherent instruction.
    - [x] iv. Parse an immediate instruction.
    - [x] v. Parse a relative instruction.
- [x] f) Parse a directive (only one as been specified .cstring).
- [x] g) Parse a comment.
- [x] h) Parse an end of line.
- [x] i) Parse an end of file and return the IR to the back end (code generator).
### 5. Report errors to the error reporter.
- [x] a) Save the error (number or message) and token’s position.
- [x] b) Record (or enter) the error in an ordered list of errors.
- [x] c) Print each line that has an error (when the parsing is done and if there is at least one error).
### 6. Generate an executable file
- [x] a) Traverse the IR using the symbol table.
- [x] b) Find the instructions that are not resolved (offsets not calculated).
    - [x] i. Check only relative instructions.
    - [x] ii. Check if offsets are smaller, fit, larger, or exceed the current instruction size:
        - [x] A. Optimize if smaller (optional).
        - [x] B. Do nothing if fit (mandatory).
        - [x] C. Adjust if larger (optional).
    - [x] iii. Resolve and set the offset.
- [x] c) Create an executable file.
- [x] d) Write all the binary code from a code buffer to the executable file.
### 7. Generate a listing file.
- [x] a) Traverse the IR using the symbol table.
- [x] b) Create the format header of a line statement: Line Addr Code Label Mne Operand Comment.
- [x] c) Create the inherent instruction formatter: Mne.
- [x] d) Create the immediate instruction formatter: Mne Operand.
- [x] e) Create the relative instruction formatter: Mne Operand.
- [x] f) Generate the opening of a line statement in a listing file (Line Addr Code).
- [x] g) Generate the closing of a line statement in a listing file (Label Mne Operand Comment).