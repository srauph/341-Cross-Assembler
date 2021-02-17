# 341-Cross Assembler

This is a group project for SOEN 341 Winter 2021 at Concordia University.\
\
The project is about reading an .asm file and generating an .EXE
through a custom cross-assembler for the Cm Assembly Language.


## Product Backlog - Sprint # 2
- [x] Denotes the feature is supported in its current required standing
- [ ] Not supported.

### 1. Parse options and the source file on the command-line.
- [ ] a) Enter valid options in a cache (at init).
- [ ] b) Validate and enable options.
- [ ] c) Validate the source file and check if the file exists.
### 2. Read the assembly source file (assume the file is validated and exists).
- [x] a) Open the source file in read mode.
- [x] b) Create and open a file input stream.

### 3. Scan characters to extract and create tokens.
- [x] a) Create a symbol table (for labels and mnemonics).
- [x] b) Enter mnemonics (keywords) in the symbol table.
- [x] c) Read characters from the input stream.4
- [ ] d) Keep track of the position (line, column) of the token.
- [x] e) Skip white spaces.
- [ ] f) Scan comments.
- [x] g) Scan identifiers (no labels yet, only mnemonics).
- [ ] h) Scan numbers.
- [ ] i) Report scanner errors.
- [x] j) Return token to parser.
### 4. Parse tokens and generate an intermediate representation (IR).
- [x] a) Create an IR (array or list of line statements).
- [x] b) Parse an assembly unit (a sequence of line statements until the end of file is reached).
- [x] c) Parse a line statement and add it to IR.
- [ ] d) Parse a label.
- [ ] e) Parse a instruction (mnemonic and operand).
    - [ ] i. Determine the addressing mode of the instruction (based on the mnemonic).
    - [ ] ii. Report parser errors.
    - [x] iii. Parse an inherent instruction.
    - [ ] iv. Parse an immediate instruction.
    - [ ] v. Parse a relative instruction.
- [ ] f) Parse a directive (only one as been specified .cstring).
- [ ] g) Parse a comment.
- [x] h) Parse an end of line.
- [x] i) Parse an end of file and return the IR to the back end (code generator).
### 5. Report errors to the error reporter.
- [ ] a) Save the error (number or message) and token’s position.
- [ ] b) Record (or enter) the error in an ordered list of errors.
- [ ] c) Print each line that has an error (when the parsing is done and if there is at least one error).
### 6. Generate an executable file
- [ ] a) Traverse the IR using the symbol table.
- [ ] b) Find the instructions that are not resolved (offsets not calculated).
    - [ ] i. Check only relative instructions.
    - [ ] ii. Check if offsets are smaller, fit, larger, or exceed the current instruction size:
        - [ ] A. Optimize if smaller (optional).
        - [ ] B. Do nothing if fit (mandatory).
        - [ ] C. Adjust if larger (optional).
    - [ ] iii. Resolve and set the offset.
- [ ] c) Create an executable file.
- [ ] d) Write all the binary code from a code buffer to the executable file.
### 7. Generate a listing file.
- [x] a) Traverse the IR using the symbol table.
- [x] b) Create the format header of a line statement: Line Addr Code Label Mne Operand Comment.
- [x] c) Create the inherent instruction formatter: Mne.
- [ ] d) Create the immediate instruction formatter: Mne Operand.
- [ ] e) Create the relative instruction formatter: Mne Operand.
- [x] f) Generate the opening of a line statement in a listing file (Line Addr Code).
- [x] g) Generate the closing of a line statement in a listing file (Label Mne Operand Comment).