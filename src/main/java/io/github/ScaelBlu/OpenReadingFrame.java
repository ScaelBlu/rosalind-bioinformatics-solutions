package io.github.ScaelBlu;

import lombok.Getter;

@SuppressWarnings({"unused"})
@Getter
public enum OpenReadingFrame {
    FIRST(0L),
    SECOND(1L),
    THIRD(2L);

    OpenReadingFrame(long offset) {
        this.offset = offset;
    }

    private final long offset;
}
