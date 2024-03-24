package com.ims.gate.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.common.entity.Result;
import com.ims.common.entity.ResultCode;
import com.ims.common.exception.CommonException;
import com.ims.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Order(-1)
public class APIFilter implements GlobalFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("start");
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String authorization = request.getHeaders().getFirst("Authorization");
        System.out.println(path);
        if (path.equals("/user/login")) {
            return chain.filter(exchange);
        }
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            try {
                Claims claims = jwtUtils.parseJwt(token);
                System.out.println(claims);
                if (claims != null) {
                    String apis = claims.get("apis", String.class);
                    String api = (method+":"+path).replaceAll("/\\d+$", "");
                    System.out.println(api);
                    String[] itemsArray = apis.split(",");
                    List<String> apiList = Arrays.asList(itemsArray);
                    if ((apis != null && apiList.contains(api))||path.equals("/user/profile")) {
                        exchange.getRequest().mutate()
                                .header("userId", claims.getId())
                                .header("companyId", String.valueOf(claims.get("companyId")))
                                .header("companyName", String.valueOf(claims.get("companyName")))
                                .build();
                        return chain.filter(exchange);
                    } else {
                        return exchange.getResponse().writeWith(Flux.just(buffer(exchange, ResultCode.UNAUTHORISED)));
                    }
                } else {
                    return exchange.getResponse().writeWith(Flux.just(buffer(exchange, ResultCode.UNAUTHENTICATED)));
                }
            } catch (Exception e) {
                try {
                    return exchange.getResponse().writeWith(Flux.just(buffer(exchange, ResultCode.UNAUTHENTICATED)));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            try {
                return exchange.getResponse().writeWith(Flux.just(buffer(exchange, ResultCode.UNAUTHENTICATED)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private DataBuffer buffer(ServerWebExchange exchange, ResultCode resultCode) throws JsonProcessingException {
        byte[] bytes = objectMapper.writeValueAsBytes(new Result(resultCode));
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return buffer;
    }
}
