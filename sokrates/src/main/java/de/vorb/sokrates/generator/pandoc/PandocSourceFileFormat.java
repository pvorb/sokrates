package de.vorb.sokrates.generator.pandoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PandocSourceFileFormat {

    NATIVE("native"),
    JSON("json"),
    MARKDOWN("markdown"),
    MARKDOWN_STRICT("markdown_strict"),
    MARKDOWN_PHPEXTRA("markdown_phpextra"),
    MARKDOWN_MMD("markdown_mmd"),
    GFM("gfm"),
    COMMONMARK("commonmark"),
    TEXTILE("textile"),
    RST("rst"),
    HTML("html"),
    DOCBOOK("docbook"),
    T2T("t2t"),
    DOCX("docx"),
    ODT("odt"),
    EPUB("epub"),
    OPML("opml"),
    ORG("org"),
    MEDIAWIKI("mediawiki"),
    TWIKI("twiki"),
    TIKIWIKI("tikiwiki"),
    CREOLE("creole"),
    HADDOCK("haddock"),
    LATEX("latex");

    @JsonValue
    private final String format;

    @JsonCreator
    public static PandocSourceFileFormat forString(String format) {
        return valueOf(format.toUpperCase());
    }

}
