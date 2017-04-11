<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="data">
        <html>
            <head>
                <title><xsl:value-of select="name"/> - Multi-User Shopping List</title>
                <style>
                    div.controls {
                        position: absolute;
                        left: 50px;
                        top: 10px;
                    }

                    div.content {
                        margin: auto;
                        width: 300px;
                        text-align: center;
                    }

                    table {
                        width: 100%;
                    }

                    table, tr, th, td {
                        border: 1px solid black;
                        border-collapse: collapse;
                    }

                    th, td {
                        padding: 2px;
                    }

                    input {
                        width: 100%;
                    }
                </style>
            </head>

            <body>
                <div class="controls">
                    <a>
                        <xsl:attribute name="href">
                            <xsl:value-of select="urlBase"/>
                            <xsl:text>/?logout</xsl:text>
                        </xsl:attribute>
                        <xsl:text>Logout</xsl:text>
                    </a>
                </div>

                <div class="content">
                    <table>
                        <xsl:for-each select="products/product">
                            <tr>
                                <td>
                                    <xsl:value-of select="."/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                    <br/>
                    <form method="post">
                        <input name="name" type="text" maxlength="250" placeholder="Product name"/>
                        <br/>
                        <input name="add" type="submit" value="Add"/>
                    </form>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
