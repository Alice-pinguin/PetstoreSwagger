package ua.goit.entity;

import java.io.Serializable;

@FunctionalInterface
public interface BaseEntity <ID> extends Serializable {
    ID getId();

}