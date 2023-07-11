package com.jiawa.train.gateway.config.config;

import com.jiawa.train.gateway.config.util.JwtUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginMemberFilter implements Ordered, GlobalFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/member/member/login")
                || path.contains("/member/member/send-code")) {
            LOG.info("不需要登录验证:{}", path);
            return chain.filter(exchange);
        } else {
            LOG.info("需要登录验证:{}", path);
        }

        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtil.isNullOrEmpty(token)) {
            LOG.info("token为空, 请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        boolean validate = JwtUtil.validate(token);
        if (validate) {
            LOG.info("token有效, 放行该请求");
            return chain.filter(exchange);
        } else {
            LOG.warn("token无效, 请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
