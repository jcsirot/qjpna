package org.chelonix.qjpna.article;

import com.fasterxml.jackson.annotation.JsonTypeName;

public class Reading extends Attribute {

    private int start, end;
    private String reading;

    public Reading() { }

    public Reading(String reading, int start, int end) {
        this.reading = reading;
        this.start = start;
        this.end = end;
    }

    public String getReading() {
        return reading;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
