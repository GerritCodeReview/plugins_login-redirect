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
import com.google.gerrit.httpd.WebSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
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
    if (path.startsWith("/login") ||
        path.startsWith("/a/") ||
        path.startsWith("/Documentation/") ||
        path.startsWith("/static/") ||
        sessionProvider.get().isSignedIn()) {
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
