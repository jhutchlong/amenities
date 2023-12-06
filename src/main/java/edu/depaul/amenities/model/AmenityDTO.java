package edu.depaul.amenities.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AmenityDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private AmenityType type;

    @NotNull
    private Integer capacity;

}
