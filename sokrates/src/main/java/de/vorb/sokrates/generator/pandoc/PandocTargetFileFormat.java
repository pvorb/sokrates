package de.vorb.sokrates.generator.pandoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PandocTargetFileFormat {

    NATIVE("native"),
    JSON("json"),
    PLAIN("plain"),
    MARKDOWN("markdown"),
    MARKDOWN_STRICT("markdown_strict"),
    MARKDOWN_PHPEXTRA("markdown_phpextra"),
    MARKDOWN_MMD("markdown_mmd"),
    GFM("gfm"),
    COMMONMARK("commonmark"),
    RST("rst"),
    HTML4("html4"),
    HTML5("html5"),
    LATEX("latex"),
    BEAMER("beamer"),
    CONTEXT("context"),
    MAN("man"),
    MEDIAWIKI("mediawiki"),
    DOKUWIKI("dokuwiki"),
    ZIMWIKI("zimwiki"),
    TEXTILE("textile"),
    ORG("org"),
    TEXINFO("texinfo"),
    OPML("opml"),
    DOCBOOK4("docbook4"),
    DOCBOOK5("docbook5"),
    JATS("jats"),
    OPENDOCUMENT("opendocument"),
    ODT("odt"),
    DOCX("docx"),
    HADDOCK("haddock"),
    RTF("rtf"),
    EPUB2("epub2"),
    EPUB3("epub3"),
    FB2("fb2"),
    ASCIIDOC("asciidoc"),
    ICML("icml"),
    TEI("tei"),
    SLIDY("slidy"),
    SLIDEOUS("slideous"),
    DZSLIDES("dzslides"),
    REVEALJS("revealjs"),
    S5("s5"),
    PPTX("pptx");

    private final String format;

    @JsonCreator
    public static PandocTargetFileFormat forString(String format) {
        return valueOf(format.toUpperCase());
    }

}
