package com.boot.product.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicsDTO  {

    /**
     *
     */
    private static final long serialVersionUID = 3L;

    private Long id;

    private String shape;

    private String material;

    private String woodEssence;

    private String length;

    private String width;

    private String weight;

    private String characteristic;
}
