package com.deviget.minesweeper.api;

import java.util.Objects;

public class ApiErrorDTO {

    private String type;
    private String details;
    private int status;

    public ApiErrorDTO(){}

    public ApiErrorDTO(String type, String details, int status) {
        this.type = type;
        this.details = details;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiErrorDTO)) return false;
        ApiErrorDTO that = (ApiErrorDTO) o;
        return status == that.status &&
                Objects.equals(type, that.type) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, details, status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApiErrorDTO{");
        sb.append("type='").append(type).append('\'');
        sb.append(", details='").append(details).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
