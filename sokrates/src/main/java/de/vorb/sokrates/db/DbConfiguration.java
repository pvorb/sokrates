package de.vorb.sokrates.db;

import de.vorb.sokrates.db.jooq.tables.daos.PageDao;
import de.vorb.sokrates.db.jooq.tables.daos.PageTagDao;
import de.vorb.sokrates.db.jooq.tables.daos.TagDao;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class DbConfiguration {

    private final org.jooq.Configuration configuration;

    public DbConfiguration(DSLContext dslContext) {
        this.configuration = dslContext.configuration();
    }

    @Bean
    public PageDao pageDao() {
        return new PageDao(configuration);
    }

    @Bean
    public TagDao tagDao() {
        return new TagDao(configuration);
    }

    @Bean
    public PageTagDao pageTagDao() {
        return new PageTagDao(configuration);
    }

}
