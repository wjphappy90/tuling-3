package com.tuling.v1.controller.interceptor;

import com.alibaba.csp.sentinel.*;
import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.adapter.servlet.config.WebServletConfig;
import com.alibaba.csp.sentinel.adapter.servlet.util.FilterUtil;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by smlz on 2020/2/19.
 */

@Slf4j
public class SentinelInterceptors implements HandlerInterceptor {

    /**
     * Specify whether the URL resource name should contain the HTTP method prefix (e.g. {@code POST:}).
     */
    public static final String HTTP_METHOD_SPECIFY = "HTTP_METHOD_SPECIFY";
    /**
     * If enabled, use the URL path as the context name, or else use the default
     * {@link WebServletConfig#WEB_SERVLET_CONTEXT_NAME}. Please pay attention to the number of context (EntranceNode),
     * which may affect the memory footprint.
     *
     * @since 1.7.0
     */
    public static final String WEB_CONTEXT_UNIFY = "WEB_CONTEXT_UNIFY";

    private final static String COLON = ":";

    private boolean httpMethodSpecify = false;

    private boolean webContextUnify = true;

    private static final String EMPTY_ORIGIN = "";


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


        Entry urlEntry = null;

        try {
            String target = FilterUtil.filterTarget(request);

            UrlCleaner urlCleaner = WebCallbackManager.getUrlCleaner();
            if (urlCleaner != null) {
                target = urlCleaner.clean(target);
            }


            if (!StringUtil.isEmpty(target)) {
                // Parse the request origin using registered origin parser.
                String origin = parseOrigin(request);
                ContextUtil.enter(WebServletConfig.WEB_SERVLET_CONTEXT_NAME, origin);

                if (httpMethodSpecify) {

                    String pathWithHttpMethod = request.getMethod().toUpperCase() + COLON + target;

                    urlEntry = SphU.entry(pathWithHttpMethod, ResourceTypeConstants.COMMON_WEB, EntryType.IN);
                } else {
                    urlEntry = SphU.entry(target, ResourceTypeConstants.COMMON_WEB, EntryType.IN);
                }
            }
            return true;
        } catch (BlockException e) {

            WebCallbackManager.getUrlBlockHandler().blocked(request, response, e);
        } catch ( RuntimeException e2) {
            Tracer.traceEntry(e2, urlEntry);
            throw e2;
        } finally {
            if (urlEntry != null) {
                urlEntry.exit();
            }
            ContextUtil.exit();
        }
        return false;
    }

    private String parseOrigin(HttpServletRequest request) {
        RequestOriginParser originParser = WebCallbackManager.getRequestOriginParser();
        String origin = EMPTY_ORIGIN;
        if (originParser != null) {
            origin = originParser.parseOrigin(request);
            if (StringUtil.isEmpty(origin)) {
                return EMPTY_ORIGIN;
            }
        }
        return origin;
    }


}
