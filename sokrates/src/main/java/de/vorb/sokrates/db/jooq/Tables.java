/*
 * This file is generated by jOOQ.
*/
package de.vorb.sokrates.db.jooq;


import de.vorb.sokrates.db.jooq.tables.Page;
import de.vorb.sokrates.db.jooq.tables.PageTag;
import de.vorb.sokrates.db.jooq.tables.Tag;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in PUBLIC
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.8"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>PUBLIC.PAGE</code>.
     */
    public static final Page PAGE = de.vorb.sokrates.db.jooq.tables.Page.PAGE;

    /**
     * The table <code>PUBLIC.PAGE_TAG</code>.
     */
    public static final PageTag PAGE_TAG = de.vorb.sokrates.db.jooq.tables.PageTag.PAGE_TAG;

    /**
     * The table <code>PUBLIC.TAG</code>.
     */
    public static final Tag TAG = de.vorb.sokrates.db.jooq.tables.Tag.TAG;
}
