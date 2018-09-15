package com.example.simpledemo.repository.network.requests;

import com.example.simpledemo.utils.ArrayUtils;

public class Query {

    private String rawQuery;

    private Query(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public String raw() {
        return rawQuery;
    }

    public static class Builder {

        private Query.Operation operation;
        private String[] properties;
        private String[] tableNames;
        private String[] conditions;

        public Builder select(String[] properties) {
            this.operation = Operation.SELECT;
            this.properties = properties;
            return this;
        }

        public Builder from(String[] tableNames) {
            this.tableNames = tableNames;
            return this;
        }

        public Builder where(String[] conditions) {
            this.conditions = conditions;
            return this;
        }

        public Query build() {
            if (this.operation == null || ArrayUtils.isEmpty(properties) ||
                    ArrayUtils.isEmpty(tableNames)) {
                throw new IllegalArgumentException("Operation, properties and table names must be defined for a query");
            }

            String fullQuery = "";

            return new Query(operation.getRawValue());
        }
    }

    private enum Operation {
        SELECT("SELECT");

        private String rawValue;

        Operation(String rawValue) {
            this.rawValue = rawValue;
        }

        String getRawValue() {
            return rawValue;
        }
    }
}
