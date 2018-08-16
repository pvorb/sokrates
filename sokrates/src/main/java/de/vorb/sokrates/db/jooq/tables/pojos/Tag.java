/*
 * This file is generated by jOOQ.
*/
package de.vorb.sokrates.db.jooq.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tag implements Serializable {

    private static final long serialVersionUID = 1998882124;

    private Long   id;
    private String name;

    public Tag() {}

    public Tag(Tag value) {
        this.id = value.id;
        this.name = value.name;
    }

    public Tag(
        Long   id,
        String name
    ) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public Tag setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tag (");

        sb.append(id);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }
}
