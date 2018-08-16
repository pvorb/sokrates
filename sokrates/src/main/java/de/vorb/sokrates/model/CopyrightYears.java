package de.vorb.sokrates.model;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CopyrightYears {

    private final int initialYear;
    private final int finalYear;

    @Override
    public String toString() {
        if (initialYear == finalYear) {
            return String.valueOf(initialYear);
        } else if (finalYear - initialYear == 1) {
            return initialYear + ", " + finalYear;
        } else {
            return initialYear + "–" + finalYear;
        }
    }

    public static CopyrightYears singleton(int year) {
        return new CopyrightYears(year, year);
    }

    public static CopyrightYears of(int initialYear, int finalYear) {
        Preconditions.checkArgument(initialYear <= finalYear, "initialYear is after finalYear");
        return new CopyrightYears(initialYear, finalYear);
    }

    public static Optional<CopyrightYears> fromCollection(Collection<Integer> years) {
        final OptionalInt initialYear = years.stream().mapToInt(year -> year).min();
        final OptionalInt finalYear = years.stream().mapToInt(year -> year).max();
        if (initialYear.isPresent()) {
            return Optional.of(new CopyrightYears(initialYear.getAsInt(), finalYear.getAsInt()));
        } else {
            return Optional.empty();
        }
    }

}
