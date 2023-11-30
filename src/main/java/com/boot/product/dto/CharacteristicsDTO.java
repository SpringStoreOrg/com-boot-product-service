package com.boot.product.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;


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

    @Size(min = 3, message = "Min shape size is 3 characters!")
    @Size(max = 100, message = "Max shape size is 30 characters!")
    private String shape;

    @Size(min = 3, message = "Min material size is 3 characters!")
    @Size(max = 100, message = "Max material size is 30 characters!")
    private String material;

    @Size(min = 3, message = "Min woodEssence size is 3 characters!")
    @Size(max = 100, message = "Max woodEssence size is 30 characters!")
    private String woodEssence;

    @Size(min = 3, message = "Min length size is 3 characters!")
    @Size(max = 100, message = "Max length size is 30 characters!")
    private String length;

    @Size(min = 3, message = "Min width size is 3 characters!")
    @Size(max = 100, message = "Max width size is 30 characters!")
    private String width;

    @Size(min = 3, message = "Min weight size is 3 characters!")
    @Size(max = 100, message = "Max weight size is 30 characters!")
    private String weight;

    @Size(min = 3, message = "Min characteristic size is 3 characters!")
    @Size(max = 100, message = "Max characteristic size is 1000 characters!")
    private String characteristic;
}
