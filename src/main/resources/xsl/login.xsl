<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="data">
        <html>
            <head>
                <title>Login - Multi-User Shopping List</title>
                <style>
                    div.login-outer {
                        display: table;
                        position: absolute;
                        width: 100%;
                        height: 100%;
                    }

                    div.login-middle {
                        display: table-cell;
                        vertical-align: middle;
                    }

                    div.login-inner {
                        margin: auto;
                        width: 200px;
                        border: 1px solid;
                        text-align: center;
                    }

                    span.error {
                        color: red;
                        font-weight: bold;
                    }
                </style>
            </head>

            <body>
                <div class="login-outer">
                    <div class="login-middle">
                        <div class="login-inner">
                            <xsl:if test="string-length(error) &gt; 0">
                                <br/>
                                <span class="error">
                                    <xsl:value-of select="error"/>
                                </span>
                                <br/>
                            </xsl:if>
                            <br/>
                            <form method="post">
                                <input name="name" type="text" maxlength="250" placeholder="Name">
                                    <xsl:if test="string-length(name) &gt; 0">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="name"/>
                                        </xsl:attribute>
                                    </xsl:if>
                                </input>
                                <br/><br/>
                                <input name="password" type="password" maxlength="250" placeholder="Password"/>
                                <br/><br/>
                                <input name="login" type="submit" value="Log in" width="150"/>
                                &#160;
                                <input name="signup" type="submit" value="Sign up" width="150"/>
                            </form>
                        </div>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
