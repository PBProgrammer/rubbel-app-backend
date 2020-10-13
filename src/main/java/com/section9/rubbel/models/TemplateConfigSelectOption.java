package com.section9.rubbel.models;

public class TemplateConfigSelectOption extends ConfigSelectOption{

    private final boolean preSelect;

    public TemplateConfigSelectOption(String label, String value, boolean preselect) {
        super(label, value);
        this.preSelect = preselect;
    }

    public TemplateConfigSelectOption(String label, Integer value, boolean preselect) {
        super(label, value);
        this.preSelect = preselect;
    }

    public TemplateConfigSelectOption(String label, String value) {
        super(label, value);
        this.preSelect = false;
    }

    public TemplateConfigSelectOption(String label, Integer value) {
        super(label, value);
        this.preSelect = false;
    }

    public boolean isPreSelect() {
        return preSelect;
    }

    public ConfigSelectOption toConfigSelectOption() {
        return new ConfigSelectOption(this.getLabel(), this.getValue());
    }
}
