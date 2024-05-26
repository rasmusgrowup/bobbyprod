package com.bobbyprod.assemblystation;

import org.springframework.stereotype.Component;

public class AssemblyCommand {
    private int ProcessID;

    public AssemblyCommand(int ProcessID) {
        this.ProcessID = ProcessID;
    }
}
