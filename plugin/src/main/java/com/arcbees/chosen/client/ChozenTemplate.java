/**
 * Copyright 2015 ArcBees Inc.
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

package com.arcbees.chosen.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface ChozenTemplate extends SafeHtmlTemplates {
    ChozenTemplate templates = GWT.create(ChozenTemplate.class);

    @Template("<li class=\"{1}\" id=\"{0}\">" +
            "<span>{2}</span>" +
            "<a href=\"javascript:void(0)\" class=\"{3} {6}\" rel=\"{4}\" data-chosen-value=\"{5}\"></a>" +
            "</li>")
    SafeHtml choice(
            String id, String searchChoiceClass, SafeHtml content,
            String searchChoiceCloseClass, String rel, String value, String iconCloseClass);

    @Template("<div id=\"{0}\" class=\"{1}\"></div>")
    SafeHtml container(String id, String cssClasses);

    @Template("<a href=\"javascript:void(0)\" class=\"{0} {1}\">" +
            "<span>{2}</span>" +
            "<div>" +
            "<b class=\"{7}\"></b>" +
            "</div>" +
            "</a>" +
            "<div class=\"{3}\" style=\"{6}\">" +
            "<div class=\"{4}\">" +
            "<input type=\"text\" autocomplete=\"off\" />" +
            "<i class=\"{8}\" role=\"close\"></i>" +
            "</div>" +
            "<div class=\"{9}\">" +
            "<ul class=\"{5}\"></ul>" +
            "</div>" +
            "</div>")
    SafeHtml contentMobile(
            String chznSingleClass, String chznDefaultClass, String defaultText,
            String dropClass, String chznSearchClass, String chznResultClass, SafeStyles offsets,
            String iconArrowClass, String iconCloseClass, String chznResultsHolderClass);

    @Template("<ul class=\"{0}\">" +
            "<li class=\"{1}\">" +
            "<input type=\"text\" value=\"{2}\" class=\"{3}\" " + "autocomplete=\"off\"" +
            "style=\"width:25px;\"/>" +
            "</li>" +
            "</ul>" +
            "<div class=\"{4}\" style=\"{6}\">" +
            "<ul class=\"{5}\"></ul>" +
            "</div>")
    SafeHtml contentMultiple(
            String chznChoicesClass, String chznSearchFieldClass,
            String defaultText, String defaultClass, String chznDropClass, String chznResultClass,
            SafeStyles offsets);

    @Template("<a href=\"javascript:void(0)\" class=\"{0} {1}\">" +
            "<span>{2}</span>" +
            "<div>" +
            "<b class=\"{7}\"></b>" +
            "</div>" +
            "</a>" +
            "<div class=\"{3}\" style=\"{6}\">" +
            "<div class=\"{4} {8}\">" +
            "<input type=\"text\" autocomplete=\"off\" />" +
            "</div>" +
            "<ul class=\"{5}\"></ul>" +
            "</div>")
    SafeHtml contentSingle(
            String chznSingleClass, String chznDefaultClass, String defaultText,
            String dropClass, String chznSearchClass, String chznResultClass, SafeStyles offsets,
            String iconArrowClass, String iconSearchClass);

    @Template("<li id=\"{0}\" class=\"{1}\">{2}</li>")
    SafeHtml group(String id, String groupResultClass, String content);

    @Template("<li class=\"{0}\">{1}\"<span></span>\"</li>")
    SafeHtml noResults(String noResultsClass, String content);

    @Template("<li id=\"{0}\" class=\"{1}\" style=\"{2}\">{3}</li>")
    SafeHtml option(String id, String groupResultClass, SafeStyles style, String content);

    @Template("<li id=\"{0}\" class=\"{1}\" style=\"{2}\">{3}</li>")
    SafeHtml option(String id, String groupResultClass, SafeStyles safeStyles, SafeHtml htmlContent);
}
