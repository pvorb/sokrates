/*
 * This file is generated by jOOQ.
*/
package de.vorb.sokrates.db.jooq.tables.daos;


import de.vorb.sokrates.db.jooq.tables.Tag;
import de.vorb.sokrates.db.jooq.tables.records.TagRecord;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


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
public class TagDao extends DAOImpl<TagRecord, de.vorb.sokrates.db.jooq.tables.pojos.Tag, Long> {

    /**
     * Create a new TagDao without any configuration
     */
    public TagDao() {
        super(Tag.TAG, de.vorb.sokrates.db.jooq.tables.pojos.Tag.class);
    }

    /**
     * Create a new TagDao with an attached configuration
     */
    public TagDao(Configuration configuration) {
        super(Tag.TAG, de.vorb.sokrates.db.jooq.tables.pojos.Tag.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(de.vorb.sokrates.db.jooq.tables.pojos.Tag object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<de.vorb.sokrates.db.jooq.tables.pojos.Tag> fetchById(Long... values) {
        return fetch(Tag.TAG.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public de.vorb.sokrates.db.jooq.tables.pojos.Tag fetchOneById(Long value) {
        return fetchOne(Tag.TAG.ID, value);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<de.vorb.sokrates.db.jooq.tables.pojos.Tag> fetchByName(String... values) {
        return fetch(Tag.TAG.NAME, values);
    }

    /**
     * Fetch a unique record that has <code>NAME = value</code>
     */
    public de.vorb.sokrates.db.jooq.tables.pojos.Tag fetchOneByName(String value) {
        return fetchOne(Tag.TAG.NAME, value);
    }
}