package com.bobbyprod;

public class AssemblyCommand {

    private int ProcessID;

    public AssemblyCommand(int processID) {
        ProcessID = processID;
    }

    public int getProcessID() {
        return ProcessID;
    }

    public void setProcessID(int processID) {
        ProcessID = processID;
    }
}
