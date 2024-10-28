package com.matticry.microservicesaccount.enums;

public enum TypeAccount {
    ahorros,  // en minúsculas para que coincida con la base de datos
    corrientes,
    NA;
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
