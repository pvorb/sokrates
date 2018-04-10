<#ftl output_format="HTML"/>

<#include "base.ftl">

<#macro page_title>
    <title>${title}</title>
</#macro>

<#macro page_content>
    <h1>${title?no_esc}</h1>
    <p>${createdAt}</p>

    <div id="content">
        ${content?no_esc}
    </div>
</#macro>

<@page/>
