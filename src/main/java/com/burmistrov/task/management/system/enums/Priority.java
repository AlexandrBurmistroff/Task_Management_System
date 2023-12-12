package com.burmistrov.task.management.system.enums;

import java.util.Optional;

public enum Priority {
    HIGH,
    MIDDLE,
    LOW;

    public static Optional<Priority> from(String stringPriority) {
        for (Priority priority : values()) {
            if (priority.name().equals(stringPriority)) {
                return Optional.of(priority);
            }
        }
        return Optional.empty();
    }
}
