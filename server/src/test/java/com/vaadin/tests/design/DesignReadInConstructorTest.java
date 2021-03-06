/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.design;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import com.vaadin.ui.declarative.Design;

public class DesignReadInConstructorTest {

    @Test
    public void useDesignReadInConstructor() {
        DesignReadInConstructor dric = new DesignReadInConstructor();
        assertEquals(3, dric.getComponentCount());
    }

    @Test
    @Ignore("Can't currently work. There is no way to write a custom component which manually reads its design in the constructor")
    public void readAndWriteDesignReadInConstructor() throws IOException {
        DesignReadInConstructor dric = new DesignReadInConstructor();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Design.write(dric, baos);
        Document doc = Jsoup.parse(baos.toString(UTF_8.name()));

        Document d = Jsoup.parse(
                getClass().getResourceAsStream("DesignReadInConstructor.html"),
                UTF_8.name(), "");
        assertJsoupTreeEquals(d.body().child(0), doc.body().child(0));
    }

    private void assertJsoupTreeEquals(Element expected, Element actual) {
        assertEquals(expected.tagName(), actual.tagName());

        Set<String> keys = new HashSet<>();

        for (Attribute attr : expected.attributes().asList()) {
            keys.add(attr.getKey());
        }
        for (Attribute attr : actual.attributes().asList()) {
            keys.add(attr.getKey());
        }
        for (String attributeKey : keys) {
            assertEquals(expected.attr(attributeKey),
                    actual.attr(attributeKey));
        }

        assertEquals(expected.children().size(), actual.children().size());
        for (int i = 0; i < expected.children().size(); i++) {
            assertJsoupTreeEquals(expected.child(i), actual.child(i));
        }
    }
}
