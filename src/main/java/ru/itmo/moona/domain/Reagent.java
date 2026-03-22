package ru.itmo.moona.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.Objects;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Reagent {
    private long id;
    private String name;
    private String formula;
    private String cas;
    private String hazardClass;
    private Long ownerId;
    private Instant createdAt;
    private Instant updatedAt;

    private Reagent(ReagentBuilder reagentBuilder) {
        this.id = reagentBuilder.id;
        this.name = reagentBuilder.name;
        this.formula = reagentBuilder.formula;
        this.cas = reagentBuilder.cas;
        this.hazardClass = reagentBuilder.hazardClass;
        this.ownerId = reagentBuilder.ownerId;
        this.createdAt = reagentBuilder.createdAt;
        this.updatedAt = reagentBuilder.updatedAt;
    }

    private Reagent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public void setHazardClass(String hazardClass) {
        this.hazardClass = hazardClass;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public void
    isValid() {
        if (name == null || name.isBlank() || name.length() > 128) {
            throw new IllegalArgumentException("name can't be blank or have length exceeding 128 char");
        }
        if (formula != null && formula.length() > 32) {
            throw new IllegalArgumentException("formula can't have length exceeding 32 char");
        }
        if (cas != null && cas.length() > 32) {
            throw new IllegalArgumentException("cas can't have length exceeding 32 char");
        }
        if (hazardClass != null && hazardClass.length() > 32) {
            throw new IllegalArgumentException("hazard class can't have length exceeding 32 char");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt can't be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt can't be null");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId can't be null");
        }
    }


    @Override
    public String toString() {
        return name + " id: " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reagent reagent = (Reagent) o;
        return id == reagent.id && Objects.equals(name, reagent.name) && Objects.equals(formula, reagent.formula) && Objects.equals(cas, reagent.cas) && Objects.equals(hazardClass, reagent.hazardClass) && Objects.equals(ownerId, reagent.ownerId) && Objects.equals(createdAt, reagent.createdAt) && Objects.equals(updatedAt, reagent.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class ReagentBuilder {
        private long id;
        private String name;
        private String formula; //optional
        private String cas; //optional
        private String hazardClass; //optional
        private Long ownerId;
        private Instant createdAt;
        private Instant updatedAt;

        public String getName() {
            return name;
        }

        public String getFormula() {
            return formula;
        }

        public String getCas() {
            return cas;
        }

        public String getHazardClass() {
            return hazardClass;
        }

        public ReagentBuilder setName(String name) {
            if (name == null || name.isBlank() || name.length() >= 128) {
                throw new IllegalArgumentException("name can't be null or have a length exceeding 128 characters.");
            } else {
                this.name = name;
                return this;
            }
        }

        public ReagentBuilder setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public ReagentBuilder setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReagentBuilder setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }


        public ReagentBuilder setFormula(String formula) {
            if (formula != null && formula.length() > 32) {
                throw new IllegalArgumentException("formula can't have a length exceeding 32 characters.");
            } else {
                this.formula = formula;
                return this;
            }
        }

        public ReagentBuilder setCas(String cas) {
            if (cas != null && cas.length() > 32) {
                throw new IllegalArgumentException("cas can't have a length exceeding 32 characters.");
            } else {
                this.cas = cas;
                return this;
            }
        }

        public ReagentBuilder setHazardClass(String hazardClass) {
            if (hazardClass != null && hazardClass.length() > 32) {
                throw new IllegalArgumentException("hazard class can't have a length exceeding 32 characters.");
            } else {
                this.hazardClass = hazardClass;
                return this;
            }
        }

        public ReagentBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public Reagent build() {
            return new Reagent(this);
        }
    }

}