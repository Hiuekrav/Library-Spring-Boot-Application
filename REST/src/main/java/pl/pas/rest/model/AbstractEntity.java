package pl.pas.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;


@Getter
public abstract class AbstractEntity implements Serializable {

    private UUID id;

    public AbstractEntity(UUID id) {
        this.id = id;
    }
}
