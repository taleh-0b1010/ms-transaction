package com.pocopay.transaction.model;

import lombok.NonNull;

public record CustomerDto(
        String cif,
        @NonNull
        String firstName,
        @NonNull
        String lastName
) {
}
