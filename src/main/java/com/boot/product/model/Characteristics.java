package com.boot.product.model;


import com.boot.product.dto.CharacteristicsDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "characteristics")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Characteristics implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private String shape;

    @Column
    private String material;

    @Column
    private String woodEssence;

    @Column
    private String length;

    @Column
    private String width;

    @Column
    private String weight;

    @Column
    private String characteristic;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    public static CharacteristicsDTO characteristicsEntityToDto(Characteristics characteristics) {
        return new CharacteristicsDTO()
                .setId(characteristics.getId())
                .setShape(characteristics.getShape())
                .setMaterial(characteristics.getMaterial())
                .setWoodEssence(characteristics.getWoodEssence())
                .setLength(characteristics.getLength())
                .setWidth(characteristics.getWidth())
                .setWeight(characteristics.getWeight())
                .setCharacteristic(characteristics.getCharacteristic());
    }

    public static Characteristics dtoToCharacteristicsEntity(CharacteristicsDTO characteristics) {
        return new Characteristics()
                .setId(characteristics.getId())
                .setShape(characteristics.getShape())
                .setMaterial(characteristics.getMaterial())
                .setWoodEssence(characteristics.getWoodEssence())
                .setLength(characteristics.getLength())
                .setWidth(characteristics.getWidth())
                .setWeight(characteristics.getWeight())
                .setCharacteristic(characteristics.getCharacteristic());
    }
}
