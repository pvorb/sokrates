<#macro page_title>
    <!-- Title goes here -->
</#macro>

<#macro page_content>
    <!-- Content goes here -->
</#macro>

<#macro page>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <@page_title/>
</head>
<body>
    <@page_content/>
</body>
</html>
</#macro>
