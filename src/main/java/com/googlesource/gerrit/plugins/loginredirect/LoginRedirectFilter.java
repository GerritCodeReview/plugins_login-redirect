// Copyright (C) 2017 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.googlesource.gerrit.plugins.loginredirect;

import com.google.gerrit.extensions.registration.DynamicItem;
import com.google.gerrit.extensions.restapi.Url;
import com.google.gerrit.httpd.AllRequestFilter;
import com.google.gerrit.httpd.GitOverHttpServlet;
import com.google.gerrit.httpd.WebSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class LoginRedirectFilter extends AllRequestFilter {
  @Inject private DynamicItem<WebSession> sessionProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpReq = (HttpServletRequest) request;

    String path = httpReq.getRequestURI();
    if (!httpReq.getContextPath().isEmpty()) {
      path = path.substring(httpReq.getContextPath().length());
    }
    if (path.startsWith("/a/")
        || path.equals("/config/server/healthcheck~status")
        || path.startsWith("/Documentation/")
        || path.equals("/favicon.ico")
        || path.equals("/login")
        || path.startsWith("/login/")
        || path.equals("/oauth")
        || (path.startsWith("/plugins/") && !path.startsWith("/plugins/gitiles/"))
        || path.equals("/ssh_info")
        || path.startsWith("/static/")
        || path.startsWith("/tools/hooks/")
        || path.endsWith("/info/lfs/objects/batch")
        || Pattern.compile(GitOverHttpServlet.URL_REGEX).matcher(path).matches()
        || sessionProvider.get().isSignedIn()) {
      chain.doFilter(request, response);
    } else {
      ((HttpServletResponse) response).sendRedirect(getLoginRedirectUrl(httpReq));
    }
  }

  private static String getLoginRedirectUrl(HttpServletRequest req) {
    String contextPath = req.getContextPath();
    String loginUrl = contextPath + "/login/";
    String token = req.getRequestURI();
    if (!contextPath.isEmpty()) {
      token = token.substring(contextPath.length());
    }

    String queryString = req.getQueryString();
    if (queryString != null && !queryString.isEmpty()) {
      token = token.concat("?" + queryString);
    }
    return (loginUrl + Url.encode(token));
  }
}
