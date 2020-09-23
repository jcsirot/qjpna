package org.chelonix.qjpna.article;

import com.fasterxml.jackson.annotation.JsonTypeName;

public class Paragraph extends Attribute {

    private int paragraph;

    public Paragraph() { }

    public Paragraph(int paragraph) {
        super();
        this.paragraph = paragraph;
    }

    public int getParagraph() {
        return paragraph;
    }
}
