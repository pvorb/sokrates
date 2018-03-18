/*
 * This file is generated by jOOQ.
*/
package de.vorb.sokrates.db.jooq;


import de.vorb.sokrates.db.jooq.tables.Page;
import de.vorb.sokrates.db.jooq.tables.PageTag;
import de.vorb.sokrates.db.jooq.tables.Tag;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling indexes of tables of the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index CONSTRAINT_INDEX_2 = Indexes0.CONSTRAINT_INDEX_2;
    public static final Index CONSTRAINT_INDEX_25 = Indexes0.CONSTRAINT_INDEX_25;
    public static final Index IDX__PAGE__CREATED_AT = Indexes0.IDX__PAGE__CREATED_AT;
    public static final Index IDX__PAGE__LAST_MODIFIED_AT = Indexes0.IDX__PAGE__LAST_MODIFIED_AT;
    public static final Index PRIMARY_KEY_2 = Indexes0.PRIMARY_KEY_2;
    public static final Index CONSTRAINT_INDEX_6 = Indexes0.CONSTRAINT_INDEX_6;
    public static final Index PRIMARY_KEY_61 = Indexes0.PRIMARY_KEY_61;
    public static final Index CONSTRAINT_INDEX_1 = Indexes0.CONSTRAINT_INDEX_1;
    public static final Index PRIMARY_KEY_1 = Indexes0.PRIMARY_KEY_1;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 extends AbstractKeys {
        public static Index CONSTRAINT_INDEX_2 = createIndex("CONSTRAINT_INDEX_2", Page.PAGE, new OrderField[] { Page.PAGE.SOURCE_FILE_PATH }, true);
        public static Index CONSTRAINT_INDEX_25 = createIndex("CONSTRAINT_INDEX_25", Page.PAGE, new OrderField[] { Page.PAGE.OUTPUT_FILE_PATH }, true);
        public static Index IDX__PAGE__CREATED_AT = createIndex("IDX__PAGE__CREATED_AT", Page.PAGE, new OrderField[] { Page.PAGE.CREATED_AT }, false);
        public static Index IDX__PAGE__LAST_MODIFIED_AT = createIndex("IDX__PAGE__LAST_MODIFIED_AT", Page.PAGE, new OrderField[] { Page.PAGE.LAST_MODIFIED_AT }, false);
        public static Index PRIMARY_KEY_2 = createIndex("PRIMARY_KEY_2", Page.PAGE, new OrderField[] { Page.PAGE.ID }, true);
        public static Index CONSTRAINT_INDEX_6 = createIndex("CONSTRAINT_INDEX_6", PageTag.PAGE_TAG, new OrderField[] { PageTag.PAGE_TAG.PAGE_ID }, false);
        public static Index PRIMARY_KEY_61 = createIndex("PRIMARY_KEY_61", PageTag.PAGE_TAG, new OrderField[] { PageTag.PAGE_TAG.TAG_ID, PageTag.PAGE_TAG.PAGE_ID }, true);
        public static Index CONSTRAINT_INDEX_1 = createIndex("CONSTRAINT_INDEX_1", Tag.TAG, new OrderField[] { Tag.TAG.NAME }, true);
        public static Index PRIMARY_KEY_1 = createIndex("PRIMARY_KEY_1", Tag.TAG, new OrderField[] { Tag.TAG.ID }, true);
    }
}
