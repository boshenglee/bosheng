package com.letgro.letgro;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ToBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        @Column
        String item;

        public Integer getId(){
            return id;
        }

        String getItem(){
            return item;
        }
}
