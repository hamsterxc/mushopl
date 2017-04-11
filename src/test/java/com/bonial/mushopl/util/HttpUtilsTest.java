package com.bonial.mushopl.util;

import org.junit.Assert;
import org.junit.Test;

public class HttpUtilsTest {

    @Test
    public void testExtractPathFirstPart() {
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("/admin/"));
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("/admin"));
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("admin"));
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("/admin/root/test/"));
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("///admin///"));
        Assert.assertEquals("admin", HttpUtils.extractPathFirstPart("/admin//root///test/"));

        Assert.assertEquals("root", HttpUtils.extractPathFirstPart("admin/root"));
        Assert.assertEquals("root", HttpUtils.extractPathFirstPart("admin/root/"));
        Assert.assertEquals("root", HttpUtils.extractPathFirstPart("admin/root/test"));
        Assert.assertEquals("root", HttpUtils.extractPathFirstPart("admin/root/test/"));
    }

}
