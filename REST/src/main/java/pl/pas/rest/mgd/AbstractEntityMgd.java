package pl.pas.rest.mgd;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.io.Serializable;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter @Setter
public abstract class AbstractEntityMgd implements Serializable {

    @BsonProperty(DatabaseConstants.ID)
    private UUID id;

}
