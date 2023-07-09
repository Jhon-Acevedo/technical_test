package com.jhon.technical_test.model;

public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int priorityValue;

    Priority(int priorityValue) {
        this.priorityValue = priorityValue;
    }

    public int getPriorityValue() {
        return priorityValue;
    }
}
