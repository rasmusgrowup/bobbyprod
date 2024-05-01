package com.bobbyprod.agv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgvLoadProgram {
    @JsonProperty("Program name")
    private String programName;
    @JsonProperty("State")
    private int state;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
