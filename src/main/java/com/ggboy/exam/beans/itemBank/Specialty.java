package com.ggboy.exam.beans.itemBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "specialtyinfo")
public class Specialty {
    @Column(name = "Specialty_id")
    private Integer SpecialtyId;
    @Column(name = "Specialty_name")
    private String SpecialtyName;

}
