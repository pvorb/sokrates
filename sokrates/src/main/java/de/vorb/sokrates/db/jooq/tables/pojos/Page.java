/*
 * This file is generated by jOOQ.
*/
package de.vorb.sokrates.db.jooq.tables.pojos;


import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;

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
public class Page implements Serializable {

    private static final long serialVersionUID = -1856231701;

    private Long      id;
    private Path      sourceFilePath;
    private String    sourceFileFormat;
    private URI       url;
    private String    title;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Locale    locale;
    private byte[]    checksum;
    private String    author;
    private String    contentHtml;
    private String    teaserImageUrl;

    public Page() {}

    public Page(Page value) {
        this.id = value.id;
        this.sourceFilePath = value.sourceFilePath;
        this.sourceFileFormat = value.sourceFileFormat;
        this.url = value.url;
        this.title = value.title;
        this.createdAt = value.createdAt;
        this.lastModifiedAt = value.lastModifiedAt;
        this.locale = value.locale;
        this.checksum = value.checksum;
        this.author = value.author;
        this.contentHtml = value.contentHtml;
        this.teaserImageUrl = value.teaserImageUrl;
    }

    public Page(
        Long      id,
        Path      sourceFilePath,
        String    sourceFileFormat,
        URI       url,
        String    title,
        LocalDate createdAt,
        LocalDate lastModifiedAt,
        Locale    locale,
        byte[]    checksum,
        String    author,
        String    contentHtml,
        String    teaserImageUrl
    ) {
        this.id = id;
        this.sourceFilePath = sourceFilePath;
        this.sourceFileFormat = sourceFileFormat;
        this.url = url;
        this.title = title;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.locale = locale;
        this.checksum = checksum;
        this.author = author;
        this.contentHtml = contentHtml;
        this.teaserImageUrl = teaserImageUrl;
    }

    public Long getId() {
        return this.id;
    }

    public Page setId(Long id) {
        this.id = id;
        return this;
    }

    public Path getSourceFilePath() {
        return this.sourceFilePath;
    }

    public Page setSourceFilePath(Path sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        return this;
    }

    public String getSourceFileFormat() {
        return this.sourceFileFormat;
    }

    public Page setSourceFileFormat(String sourceFileFormat) {
        this.sourceFileFormat = sourceFileFormat;
        return this;
    }

    public URI getUrl() {
        return this.url;
    }

    public Page setUrl(URI url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Page setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Page setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDate getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    public Page setLastModifiedAt(LocalDate lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
        return this;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public Page setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public byte[] getChecksum() {
        return this.checksum;
    }

    public Page setChecksum(byte... checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getAuthor() {
        return this.author;
    }

    public Page setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContentHtml() {
        return this.contentHtml;
    }

    public Page setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
        return this;
    }

    public String getTeaserImageUrl() {
        return this.teaserImageUrl;
    }

    public Page setTeaserImageUrl(String teaserImageUrl) {
        this.teaserImageUrl = teaserImageUrl;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Page (");

        sb.append(id);
        sb.append(", ").append(sourceFilePath);
        sb.append(", ").append(sourceFileFormat);
        sb.append(", ").append(url);
        sb.append(", ").append(title);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(lastModifiedAt);
        sb.append(", ").append(locale);
        sb.append(", ").append("[binary...]");
        sb.append(", ").append(author);
        sb.append(", ").append(contentHtml);
        sb.append(", ").append(teaserImageUrl);

        sb.append(")");
        return sb.toString();
    }
}
